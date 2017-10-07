(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.bench.core
  
  {:doc "Benchmark utilities."
   :author "palisades dot lakes at gmail dot com"
   :since "2017-05-29"
   :version "2017-09-19"}
  
  (:require [clojure.string :as s]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.pprint :as pp]
            [palisades.lakes.bench.generators :as g]
            [palisades.lakes.bench.eager.parallel :as para])
  
  (:import [clojure.lang IFn]
           [java.util Map]
           [java.util.concurrent Executors ExecutorService]
           [java.nio.file FileSystems PathMatcher]
           [java.time.format DateTimeFormatter]
           [palisades.lakes.bench.java SystemInfo]
           [palisades.lakes.bench.java.spaces.linear 
            Axpy LinearFunction Sum Vector]))
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
;; TODO: use spec to check generators sequence?
(defn generate-datasets
  
  "`generators` is a sequence of functions,
   with an even number of elements, like 
   `[container-generator0 element-generator0 
     container-generator1 element-generator1 ...]`
   
   These are called as if
   `[(container-generator0 element-generator0 nelements)
     (container-generator1 element-generator1 nelements) ...]`,
   returning `[dataset0 dataset1 ...]`, the arguments for a 
   function `f` to be benchmarked, which is done
   as if calling `(apply f [dataset0 dataset1 ...])`.

   This is done `nthreads` times, returning a vector of 
   `nthreads` vectors of data sets `[datasets0 datasets1 ...]`
   
   Finally, the benchmarking calls `(apply f datasetsi)`
   in parallel, in `nthreads` threads. The benchmark time
   is the time for all threads to complete."
  
  ([generators nelements nthreads]
    (assert (even? (count generators)))
    (assert (every? ifn? generators))
    
    (pp/pprint (partition 2 (map fn-name generators)))
    
    {:nelements nelements
     :nthreads nthreads
     :generators generators 
     :data
     (repeatedly 
       nthreads 
       (fn thread-datasets []
         (mapv 
           (fn dataset [[^IFn container-generator ^IFn element-generator]]
             (container-generator element-generator nelements))
           (partition 2 generators))))})
  
  ([generators nelements]
    (generate-datasets generators nelements (default-nthreads))))
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
(defn ^String benchname [for-ns]
  (first (take-last 2 (s/split (str for-ns) #"\."))))
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
                    (fn add-record-to-table [table record]
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
    (mapv (fn add-average-to-record [record]
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
           :value (:value record)
           :lower-q (* 1000.0 (double (first (:lower-q record))))
           :upper-q (* 1000.0 (double (first (:upper-q record))))
           :now (now))))
;;----------------------------------------------------------------
(def defaults {:tail-quantile 0.05 
               :samples 50
               :n (* 1024 1024)
               :pause 16
               ;; about 2 minutes warmup -- default is 10s
               :warmup-jit-period (* 128 1024 1024 1024)})
;;----------------------------------------------------------------
(defn milliseconds 
  ([^ExecutorService pool ^IFn f ^Map data-map ^Map options]
    (let [options (merge defaults options)
          samples (int (:samples options))
          pause (int (:pause options))
          fname (fn-name f)
          start (System/nanoTime)
          af (fn ^double [datasets] (double (apply f datasets)))
          runit (fn ^double []
                  (loop [i (int 0)
                         mx Double/NEGATIVE_INFINITY]
                    (if (>= i samples)
                      mx
                      (let [v (Sum/naive 
                                (para/pool-map-doubles 
                                  pool af (:data data-map)))]
                        (if (< mx v)
                          (recur (inc i) v)
                          (recur (inc i) mx))))))
          _ (Thread/sleep (* pause 1000)) 
          start (System/nanoTime)
          v0 (runit)  
          msec0 (/ (* (- (System/nanoTime) start) 1.0e-6) samples)
          _ (Thread/sleep (* pause 1000)) 
          start (System/nanoTime)
          v1 (runit)
          msec1 (/ (* (- (System/nanoTime) start) 1.0e-6) samples)]
      #_(println fname samples v0 msec0)
      #_(println fname samples v1 msec1)
      #_(println fname (/ msec0 msec1))
      (pp/pprint
        {:algorithm fname 
         :benchmark (benchname *ns*)
         :threads (:nthreads data-map (default-nthreads))
         :warmup msec0
         :value v1
         :millisec msec1
         :ratio (/ msec0 msec1)})))
  ([^ExecutorService pool ^IFn f ^Map data-map] 
    (milliseconds f data-map {})))
;;----------------------------------------------------------------
(defn profile 
  ([generators fns ^Map options]
    (let [options (merge defaults options)
          pause (int (:pause options))
          n (int (:n options))]
      (assert (every? ifn? generators))
      (assert (every? ifn? fns))
      (println (s/join " " (map fn-name generators)))
      (println n) 
      (println (.toString (java.time.LocalDateTime/now))) 
      (time
        (with-open [w (log-writer *ns* generators n)]
          (binding [*out* w]
            (print-system-info w)
            (println "generate-datasets")
            (let [data-map (time (generate-datasets generators n))
                  nthreads (int (:nthreads data-map (default-nthreads)))
                  pool (Executors/newFixedThreadPool nthreads)]
              (try 
                (reduce
                  (fn add-record [records record]
                    (if record
                      (let [records (conj records record)]
                        (write-tsv records (data-file *ns* generators n))
                        records)
                      records))
                  []
                  (map
                    (fn profile-one-fn [f]
                      (Thread/sleep (int (* pause 1000))) 
                      (println (.toString (java.time.LocalDateTime/now))) 
                      (time (milliseconds pool f data-map options)))
                    fns))
                (finally (.shutdown pool)))))))))
  ([generators fns] (profile generators fns {})))
;;----------------------------------------------------------------
(defn criterium 
  
  ([^ExecutorService pool ^IFn f ^Map data-map ^Map options]
    (let [options (merge defaults options)
          fname (fn-name f)
          af (fn ^double [datasets] (double (apply f datasets)))
          result (criterium/benchmark 
                   (Sum/naive 
                     (para/pool-map-doubles 
                       pool af (:data data-map)))
                   options)
          value (first (:results result))
          result (simplify 
                   (assoc 
                     (merge result (dissoc data-map :data))
                     :benchmark (benchname *ns*)
                     :threads (:nthreads data-map (default-nthreads))
                     :value value
                     :algorithm fname))]
      (pp/pprint result)
      (println)
      (flush)
      result))
  
  ([^ExecutorService pool ^IFn f ^Map data-map] (criterium f data-map {})))
;;----------------------------------------------------------------
(defn bench 
  ([generators fns ^Map options]
    (let [options (merge defaults options)
          n (int (:n options))
          pause (int (:pause options))]
      (assert (every? ifn? generators))
      (assert (every? ifn? fns))
      (println (s/join " " (map fn-name generators)))
      (println n) 
      (println (.toString (java.time.LocalDateTime/now))) 
      (Thread/sleep (* pause 1000)) 
      (time
        (with-open [w (log-writer *ns* generators n)]
          (binding [*out* w]
            (print-system-info w)
            (println "generate-datasets")
            (let [data-map (time (generate-datasets generators n))
                  nthreads (int (:nthreads data-map (default-nthreads)))
                  pool (Executors/newFixedThreadPool nthreads)]
              (try
                (reduce
                  (fn add-record [records record]
                    (if record
                      (let [records (conj records record)]
                        (write-tsv records (data-file *ns* generators n))
                        records)
                      records))
                  []
                  (map
                    (fn benchmark-one-fn [f]
                      (Thread/sleep (* pause 1000)) 
                      (println (.toString (java.time.LocalDateTime/now))) 
                      (time (criterium pool f data-map options)))
                    fns))
                (finally (.shutdown pool)))))))))
  ([generators fns] (bench generators fns {})))
;;----------------------------------------------------------------
;;----------------------------------------------------------------
;; Count the truthy values returned by <code>f</code>
;; applied to the elements of 2 container operands.
;; A macro since <code>f</code> may be a Java method and not 
;; Clojure functions.

;; TODO: pass int lexical type hints for containers, elements,
;; and return values; support collections in addition to arrays.

(defmacro defcounter [benchname f]
  (let [a (gensym "a") 
        b (gensym "b")
        args [(with-meta a {:tag 'objects})
              (with-meta b {:tag 'objects})]
        args (with-meta args {:tag 'long})]
    #_(binding [*print-meta* true] (pp/pprint args))
    `(defn ~benchname ~args
       (assert (== (alength ~a) (alength ~b)))
       (let [n# (int (alength ~a))]
         (loop [i# (int 0)
                total# (long 0)]
           (cond (>= i# n#) (long total#)
                 (~f (aget ~a i#) (aget ~b i#)) 
                 (recur (inc i#) (inc total#))
                 :else (recur (inc i#) total#)))))))
;;----------------------------------------------------------------
;; Find the max of a <code>double</code> valued <code>f</code>
;; applied to the elements of 1 container operand.
;; A macro since <code>f</code> may be a Java method and not 
;; Clojure functions.

;; TODO: pass int lexical type hints for containers, elements,
;; and return values; support collections in addition to arrays.

(defmacro defmax [benchname f]
  (let [a (gensym "a") 
        args [(with-meta a {:tag 'objects})]
        args (with-meta args {:tag 'double})]
    `(defn ~benchname ~args
       (let [n# (int (alength ~a))]
         (loop [i# (int 0)
                dmax# Double/NEGATIVE_INFINITY]
           (if (>= i# n#) 
             dmax#
             (recur 
               (inc i#) 
               (Math/max dmax# (double (~f (aget ~a i#)))))))))))
;;----------------------------------------------------------------
;; Find the max L1 norm of a vector valued <code>f</code>
;; applied to the elements of 3 container operands.
;; A macro since <code>f</code> may be a Java method and not 
;; Clojure functions.

;; TODO: pass int lexical type hints for containers, elements,
;; and return values; support collections in addition to arrays.

(defmacro defmaxl1 [benchname f]
  (let [a (gensym "a") 
        x (gensym "x")
        y (gensym "y")
        args [(with-meta a {:tag 'objects})
              (with-meta x {:tag 'objects})
              (with-meta y {:tag 'objects})]
        args (with-meta args {:tag 'double})]
    `(defn ~benchname ~args
       (assert (== (alength ~a) (alength ~x) (alength ~y)))
       (let [n# (int (alength ~a))]
         (loop [i# (int 0)
                max# Double/NEGATIVE_INFINITY]
           (if (>= i# n#) 
             max#
             (let [^Vector v# (~f (aget ~a i#) (aget ~x i#) (aget ~y i#))
                   l1# (.l1Norm v#)]
               (recur (inc i#) (Math/max max# l1#)))))))))
;;----------------------------------------------------------------
;; Count the truthy values returned by <code>f</code>
;; applied to the elements of 2 container operands.
;; A macro since <code>f</code> may be a Java method and not 
;; Clojure functions.

;; TODO: pass int lexical type hints for containers, elements,
;; and return values; support collections in addition to arrays.

(defmacro defcounter [benchname f]
  (let [a (gensym "a") 
        b (gensym "b")
        args [(with-meta a {:tag 'objects})
              (with-meta b {:tag 'objects})]
        args (with-meta args {:tag 'long})]
    #_(binding [*print-meta* true] (pp/pprint args))
    `(defn ~benchname ~args
       (assert (== (alength ~a) (alength ~b)))
       (let [n# (int (alength ~a))]
         (loop [i# (int 0)
                total# (long 0)]
           (cond (>= i# n#) (long total#)
                 (~f (aget ~a i#) (aget ~b i#)) 
                 (recur (inc i#) (inc total#))
                 :else (recur (inc i#) total#)))))))
;;----------------------------------------------------------------
;; Find the max of a <code>double</code> valued <code>f</code>
;; applied to the elements of 1 container operand.
;; A macro since <code>f</code> may be a Java method and not 
;; Clojure functions.

;; TODO: pass int lexical type hints for containers, elements,
;; and return values; support collections in addition to arrays.

(defmacro defmax [benchname f]
  (let [a (gensym "a") 
        args [(with-meta a {:tag 'objects})]
        args (with-meta args {:tag 'double})]
    `(defn ~benchname ~args
       (let [n# (int (alength ~a))]
         (loop [i# (int 0)
                dmax# Double/NEGATIVE_INFINITY]
           (if (>= i# n#) 
             dmax#
             (recur 
               (inc i#) 
               (Math/max dmax# (double (~f (aget ~a i#)))))))))))
;;----------------------------------------------------------------
;; Find the max L1 norm of a vector valued <code>f</code>
;; applied to the elements of 3 container operands.
;; A macro since <code>f</code> may be a Java method and not 
;; Clojure functions.

;; TODO: pass int lexical type hints for containers, elements,
;; and return values; support collections in addition to arrays.

(defmacro defmaxl1 [benchname f]
  (let [a (gensym "a") 
        x (gensym "x")
        y (gensym "y")
        args [(with-meta a {:tag 'objects})
              (with-meta x {:tag 'objects})
              (with-meta y {:tag 'objects})]
        args (with-meta args {:tag 'double})]
    `(defn ~benchname ~args
       (assert (== (alength ~a) (alength ~x) (alength ~y)))
       (let [n# (int (alength ~a))]
         (loop [i# (int 0)
                max# Double/NEGATIVE_INFINITY]
           (if (>= i# n#) 
             max#
             (let [^Vector v# (~f (aget ~a i#) (aget ~x i#) (aget ~y i#))
                   l1# (.l1Norm v#)]
               (recur (inc i#) (Math/max max# l1#)))))))))
;;----------------------------------------------------------------
;; Count the truthy values returned by <code>f</code>
;; applied to the elements of 2 container operands.
;; A macro since <code>f</code> may be a Java method and not 
;; Clojure functions.

;; TODO: pass int lexical type hints for containers, elements,
;; and return values; support collections in addition to arrays.

(defmacro defcounter [benchname f]
  (let [a (gensym "a") 
        b (gensym "b")
        args [(with-meta a {:tag 'objects})
              (with-meta b {:tag 'objects})]
        args (with-meta args {:tag 'long})]
    #_(binding [*print-meta* true] (pp/pprint args))
    `(defn ~benchname ~args
       (assert (== (alength ~a) (alength ~b)))
       (let [n# (int (alength ~a))]
         (loop [i# (int 0)
                total# (long 0)]
           (cond (>= i# n#) (long total#)
                 (~f (aget ~a i#) (aget ~b i#)) 
                 (recur (inc i#) (inc total#))
                 :else (recur (inc i#) total#)))))))
;;----------------------------------------------------------------
;; Find the max of a <code>double</code> valued <code>f</code>
;; applied to the elements of 1 container operand.
;; A macro since <code>f</code> may be a Java method and not 
;; Clojure functions.

;; TODO: pass int lexical type hints for containers, elements,
;; and return values; support collections in addition to arrays.

(defmacro defmax [benchname f]
  (let [a (gensym "a") 
        args [(with-meta a {:tag 'objects})]
        args (with-meta args {:tag 'double})]
    `(defn ~benchname ~args
       (let [n# (int (alength ~a))]
         (loop [i# (int 0)
                dmax# Double/NEGATIVE_INFINITY]
           (if (>= i# n#) 
             dmax#
             (recur 
               (inc i#) 
               (Math/max dmax# (double (~f (aget ~a i#)))))))))))
;;----------------------------------------------------------------
;; Find the max L1 norm of a vector valued <code>f</code>
;; applied to the elements of 3 container operands.
;; A macro since <code>f</code> may be a Java method and not 
;; Clojure functions.

;; TODO: pass int lexical type hints for containers, elements,
;; and return values; support collections in addition to arrays.

(defmacro defmaxl1 [benchname f]
  (let [a (gensym "a") 
        x (gensym "x")
        y (gensym "y")
        args [(with-meta a {:tag 'objects})
              (with-meta x {:tag 'objects})
              (with-meta y {:tag 'objects})]
        args (with-meta args {:tag 'double})]
    `(defn ~benchname ~args
       (assert (== (alength ~a) (alength ~x) (alength ~y)))
       (let [n# (int (alength ~a))]
         (loop [i# (int 0)
                max# Double/NEGATIVE_INFINITY]
           (if (>= i# n#) 
             max#
             (let [^Vector v# (~f (aget ~a i#) (aget ~x i#) (aget ~y i#))
                   l1# (.l1Norm v#)]
               (recur (inc i#) (Math/max max# l1#)))))))))
;;----------------------------------------------------------------
