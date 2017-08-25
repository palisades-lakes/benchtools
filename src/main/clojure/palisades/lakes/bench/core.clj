(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.bench.core
  
  {:doc "Benchmark utilities."
   :author "palisades dot lakes at gmail dot com"
   :since "2017-05-29"
   :version "2017-08-24"}
  
  (:require [clojure.string :as s]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.pprint :as pp]
            [palisades.lakes.bench.random.generators :as g])
  
  (:import [clojure.lang IFn]
           [java.util Map]
           [java.nio.file FileSystems PathMatcher]
           [java.time.format DateTimeFormatter]
           [palisades.lakes.bench.java SystemInfo]))
;;----------------------------------------------------------------
(set! *warn-on-reflection* false)
(set! *unchecked-math* false)
(require '[criterium.core :as criterium])
(require '[criterium.stats])
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
;; TODO: unit test this against a wide range of cases
;; TODO: single regx?

(defn fn-name [f] 
  (let [s (str f)
        s (first (s/split s #"\@"))
        s (last (s/split s #"\$"))
        s (first (s/split s #"__"))]
    s
    #_(first (re-find #"(?<=\$)([^@]+)(?=@)" s))))
;;----------------------------------------------------------------
(defn- abbreviate [generator]
  (apply str (map #(subs % 0 2) (s/split generator #"_")))) 
;;----------------------------------------------------------------
(defn- default-nthreads 
  "Benchmarking results seem too variable. May be due to 
   temperature throttling of cpus, or maybe just other activity
   in the system. Try leaving one real cpu free to see if that
   helps."
  []
  (max 1 (- (.availableProcessors (Runtime/getRuntime)) 2)))
;;----------------------------------------------------------------
;; TODO: include dataset-generator in file name
(defn generate-datasets 
  
  ([generators
    nelements
    nthreads]
    {:nelements nelements
     :nthreads nthreads
     
     (keyword (str "generator" counter)) 
     (fn-name element-generator) 
     
     (keyword (str "dataset-generator" counter)) 
     (fn-name dataset-generator) 
     
     (keyword (str "data" counter)) 
     (repeatedly 
       nthreads 
       #(dataset-generator element-generator nelements))})
  
  ([counter
    ^IFn dataset-generator 
    ^IFn element-generator 
    nelements]
    (generate-datasets 
      counter dataset-generator element-generator nelements 
      (default-nthreads))))
;;----------------------------------------------------------------
(defn write-tsv [^java.util.List records ^java.io.File f]
  (io/make-parents f)
  (let [ks (sort (into #{} (mapcat keys records)))]
    (with-open [w (io/writer f)]
      (binding [*out* w]
        (println (s/join "\t" (map name ks)))
        (doseq [record records]
          (println 
            (s/join "\t" (map #(or (str (get record %)) "") ks))))
        (flush)))))
;;----------------------------------------------------------------
(defn read-tsv [^java.io.File f]
  (println (.getName f))
  (with-open [r (io/reader f)]
    (let [lines (line-seq r)
          header (map keyword (s/split (first lines) #"\t" -1))]
      (mapv #(zipmap header (s/split % #"\t" -1)) (rest lines)))))
(defn read-tsvs [files] (mapcat read-tsv files))
;;----------------------------------------------------------------
(let [^DateTimeFormatter dtf 
      (DateTimeFormatter/ofPattern "yyyyMMdd-HHmm")]
  (defn- now ^String []
    (.format dtf (java.time.LocalDateTime/now))))
                                  
(defn fname [generators n ext]
  (s/join "."
          [(SystemInfo/manufacturerModel)
           (s/join "-" (map fn-name generators))
           n
           (now)
           ext]))
;;----------------------------------------------------------------
(defn ^java.io.File ns-folder [prefix for-ns]
  (let [^java.io.File f (apply 
                          io/file prefix 
                          (take-last 2 (s/split (str for-ns) #"\.")))]
    (.mkdirs f)
    f))
;;----------------------------------------------------------------
(defn- log-folder [for-ns] (ns-folder "logs" for-ns))

(defn- log-file ^java.io.File [for-ns generators n]
  (io/file (log-folder for-ns) (fname generators n "txt")))

(defn log-writer ^java.io.Writer [for-ns generators n]
  (java.io.PrintWriter.
    (io/writer (log-file for-ns generators n))
    true))
;;----------------------------------------------------------------
(defn- data-folder [for-ns] (ns-folder "data" for-ns))

(defn data-file 
  ^java.io.File [for-ns generators n]
  (io/file (data-folder for-ns) (fname generators n "tsv")))
;;----------------------------------------------------------------
(defn- data-files [folder ^String glob]
  (let [^PathMatcher pm (.getPathMatcher 
                          (FileSystems/getDefault) 
                          (str "glob:**/" glob ".tsv"))]
    (filter #(.matches pm (.toPath ^java.io.File %))
            (file-seq folder))))
;;----------------------------------------------------------------
(defn read-data [for-ns glob]
  (println (data-folder for-ns))
  (println (data-files (data-folder for-ns) glob))
  (read-tsvs (data-files (data-folder for-ns) glob)))
;;----------------------------------------------------------------
(defn print-system-info [w]
  (binding [*out* w]
    #_(pp/pprint (into {} (criterium/system-properties)))
    #_(println "---------------")
    (pp/pprint (criterium/runtime-details))
    (println "---------------")
    (SystemInfo/printAll w)
    (println "---------------")
    (flush)))
;;----------------------------------------------------------------
(defn summary-table [for-ns glob]
  (let [data (read-data for-ns glob)
        table (sort-by 
                :algorithm
                (vals 
                  (reduce 
                    (fn [table record]
                      (let [algorithm (str (:algorithm record)
                                           "-"
                                           (:threads record))
                            threads (:threads record)
                            microsec (int 
                                       (Math/round 
                                         (Double/parseDouble 
                                           (:millisec record))))
                            generators (s/join "-" (map fn-name (:generators record)))
                            row (assoc 
                                  (get table algorithm 
                                       {:algorithm algorithm})
                                  generators microsec)]
                        (assoc table algorithm row)))
                    {}
                    data)))]
    (mapv (fn [record]
            (assoc record :average
                   (int 
                     (Math/round
                       (/ (double 
                            (reduce 
                              (fn ^long [^long sum [k ^long v]] 
                                (+ sum v))
                              (long 0)
                              (dissoc record :algorithm)))
                          (double (dec (count record))))))))
          table)))
;;----------------------------------------------------------------
;; criterium output
;;----------------------------------------------------------------
(defn simplify [record]
  (let [median (double (first (criterium.stats/median 
                                (map double (:samples record)))))
        median (/ median 
                  (* (double (:execution-count record)) 1.0e6))
        record (dissoc record
                       :runtime-details :os-details
                       :samples :results
                       :overhead :final-gc-time
                       #_:sample-count 
                       #_:execution-count 
                       #_:tail-quantile
                       :warmup-time :outlier-variance :outliers
                       :options :sample-mean :sample-variance
                       #_:warmup-executions
                       :variance)]
    (assoc (dissoc record :mean) 
           :generators (s/join "-" (map fn-name (:generators record)))
           :manufacturerModel (SystemInfo/manufacturerModel)
           :median median
           :millisec (* 1000.0 (double (first (:mean record))))
           ;;:variance (first (:variance record))
           :value (int (:value record))
           :lower-q (* 1000.0 (double (first (:lower-q record))))
           :upper-q (* 1000.0 (double (first (:upper-q record))))
           :now (now))))
;;----------------------------------------------------------------
(defn criterium 

  ([^IFn f ^Map data-map options]
    (let [options (merge {:tail-quantile 0.25 :samples 60} options)
          fname (fn-name f)
          nthreads (long (:nthreads data-map (default-nthreads)))
          calls (map (fn caller [data] #(f data)) (:data data-map))
          _(assert (== nthreads (count calls)))
          result (criterium/benchmark (reduce + (apply pcalls calls)) options)
          value (double (first (:results result)))
          result (simplify 
                   (assoc 
                     (merge result (dissoc data-map :data))
                     :threads nthreads
                     :value value
                     :algorithm fname))]
      (pp/pprint result)
      (println)
      (flush)
      result))
  
  ([^IFn f ^Map data-map] (criterium f data-map {})))
;;----------------------------------------------------------------
#_(defn milliseconds 
   (^double [^IFn f ^Map data-map ^long nreps]
     (let [fname (fn-name f)
           nthreads (long (:nthreads data-map (default-nthreads)))
           data (:data data-map)
           ff (fn [s0 s1]
                (let [calls 
                      (mapv (fn caller [s0i s1i] #(f s0i s1i)) s0 s1)]
                  (reduce + (apply pcalls calls))))
           start (System/nanoTime)
           warmup (reduce 
                    + 
                    (map ff 
                         (take 32 (cycle [data0 data1 data0 data1]))
                         (take 32 (cycle [data1 data1 data0 data0]))))
           wmsec (/ (* (- (System/nanoTime) start) 1.0e-6) 32)
           start (System/nanoTime)
           result (reduce 
                    + 
                    (map ff 
                         (take nreps (cycle [data0 data1 data0 data1]))
                         (take nreps (cycle [data1 data1 data0 data0]))))
           msec (/ (* (- (System/nanoTime) start) 1.0e-6) nreps)]
       (println fname 32 wmsec warmup)
       (println fname  nreps msec result)
       (println fname (/ wmsec msec))
       msec))
   (^double [^IFn f ^Map data-map] (milliseconds f data-map 256)))
;;----------------------------------------------------------------
