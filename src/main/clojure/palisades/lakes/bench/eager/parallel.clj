(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.bench.eager.parallel
  
  {:doc "Quick and dirty borrowing from Zana." 
   :author "palisades dot lakes at gmail dot com"
   :since "2017-09-18"
   :version "2017-09-18"}
  
  (:refer-clojure :exclude [pmap])
  (:import [java.util ArrayList Arrays Collection Collections HashMap 
            IdentityHashMap Iterator List Map Map$Entry NoSuchElementException 
            RandomAccess Set]
           [java.util.concurrent Executors ExecutorService Future 
            ThreadLocalRandom]
           [clojure.lang Counted IFn IPersistentMap IPersistentCollection 
            Seqable Sequential]
           [com.google.common.base Function Predicate]
           [com.google.common.collect ImmutableList ImmutableTable Iterables 
            Iterators Multimap Multiset Ordering Sets Table Table$Cell]
           [com.google.common.primitives Booleans Bytes Chars Doubles Floats 
            Ints Longs Shorts]))
;;------------------------------------------------------------------------------
;; mapc: iterate over data structure for side effects
;; TODO: (mapc f) -> transducer?
;; TODO: (map-into nil f things0 ...)?
;;------------------------------------------------------------------------------
(defmulti mapc 
  "Apply a function to each 'element' of some data structure for side effects, 
   returning <code>nil</code>."
  {:arglists '([f things0] [f things0 things1] [f things0 things1 things2])}
  (fn mapc-dispatch ([& args] (mapv class args))))

(defmethod mapc [IFn Iterator] [^IFn f ^Iterator things]
  (while (.hasNext things) (f (.next things)))
  nil)

(defmethod mapc [IFn Iterable] [^IFn f ^Iterable things]
  (mapc f (.iterator things)))

;; Note: requires f to be a function that takes 2 args.
;; Could use destructuring of Map$Entry --- need to compare performance.
(defmethod mapc [IFn Map] [^IFn f ^Map things]
  (let [it (.iterator (.entrySet things))]
    (while (.hasNext it)
      (let [^Map$Entry nxt (.next it)
            k (.getKey nxt)
            v (.getValue nxt)]
        (f k v)))
    nil))

(prefer-method mapc [IFn Map] [IFn Iterable])

(defmethod mapc [IFn Table] [^IFn f ^Table table]
  (assert (ifn? f))
  (assert table "no table to mapc over")
  (doseq [^Table$Cell cell (.cellSet table)]
    (assert (not (nil? cell)) (print-str "Nil cell:" table))
    (f cell))
  nil)
;;------------------------------------------------------------------------------
;; 3 args

(defmethod mapc 
  [IFn Iterator Iterator] 
  [^IFn f ^Iterator things0 ^Iterator things1]
  (while (and (.hasNext things0) (.hasNext things1))
    (f (.next things0) (.next things1))))

(defmethod mapc 
  [IFn Iterable Iterable] 
  [^IFn f ^Iterable things0 ^Iterable things1]
  (mapc f (.iterator things0) (.iterator things1)))

;;------------------------------------------------------------------------------
;; 4 args

(defmethod mapc 
  [IFn Iterator Iterator Iterator] 
  [^IFn f ^Iterator i0 ^Iterator i1 ^Iterator i2]
  (while (and (.hasNext i0) (.hasNext i1) (.hasNext i2))
    (f (.next i0) (.next i1) (.next i2)))
  nil)

(defmethod mapc 
  [IFn Iterable Iterable Iterable] 
  [^IFn f ^Iterable things0 ^Iterable things1 ^Iterable things2]
  (mapc f (.iterator things0) (.iterator things1) (.iterator things2)))
;;------------------------------------------------------------------------------
;; concurrent mapping
;;------------------------------------------------------------------------------
;; Looks like clojure functions are both Callable and Runnable,
;; and ExecutorService treats them as Runnable (no return value).
(defn- callable 
  (^java.util.concurrent.Callable [f x] 
    (assert (ifn? f) (print-str "Not a function:" f))
    (reify java.util.concurrent.Callable (call [this] (f x))))
  (^java.util.concurrent.Callable [f x0 x1] 
    (assert (ifn? f) (print-str "Not a function:" f))
    (reify java.util.concurrent.Callable (call [this] (f x0 x1))))
  (^java.util.concurrent.Callable [f x0 x1 x2] 
    (assert (ifn? f) (print-str "Not a function:" f))
    (reify java.util.concurrent.Callable (call [this] (f x0 x1 x2))))
  (^java.util.concurrent.Callable [f x0 x1 x2 & args] 
    (assert (ifn? f) (print-str "Not a function:" f))
    (reify java.util.concurrent.Callable (call [this] (apply f x0 x1 x2 args)))))
;;------------------------------------------------------------------------------
;; TODO: how expensive does f have to be for this to be faster than a serial
;; loop
(defn pool-map-doubles
  "Like [[pool-map]] with output in a <code>double[]</code>."
  (^doubles [^ExecutorService pool ^clojure.lang.IFn$OD f 
             things]
    (let [tasks (map #(callable f %) things)
          b (double-array (count things))
          futures (.iterator (.invokeAll pool tasks))]
      (loop [i 0]
        (when (.hasNext futures)
          (let [^Future future (.next futures)]
            (aset b i (double (.get future))))
          (recur (inc i))))
      b))
  (^doubles [^ExecutorService pool ^clojure.lang.IFn$OD f 
             things0 things1]
    (let [tasks (map (fn [x0 x1] (callable f x0 x1)) things0 things1)
          b (double-array (min (count things0) (count things1)))
          futures (.iterator (.invokeAll pool tasks))]
      (loop [i 0]
        (when (.hasNext futures)
          (let [^Future future (.next futures)]
            (aset b i (double (.get future))))
          (recur (inc i))))
      b))
  (^doubles [^ExecutorService pool ^clojure.lang.IFn$OD f 
             things0 things1 things2]
    (let [tasks (map (fn [x0 x1 x2] (callable f x0 x1 x2))
                     things0 things1 things2)
          b (double-array (min (count things0) (count things1) (count things2)))
          futures (.iterator (.invokeAll pool tasks))]
      (loop [i 0]
        (when (.hasNext futures)
          (let [^Future future (.next futures)]
            (aset b i (double (.get future))))
          (recur (inc i))))
      b)))
;;------------------------------------------------------------------------------
;; TODO: how expensive does f have to be for this to be faster than a serial
;; loop
(defn nmap-doubles
  "Like [[nmap]] with output in a <code>double[]</code>."
  (^doubles [^long n ^clojure.lang.IFn$OD f things]
    (let [pool (Executors/newFixedThreadPool (int n))]
      (try
        (pool-map-doubles pool f things)
        (finally (.shutdown pool)))))
  (^doubles [^long n ^clojure.lang.IFn$OD f things0 things1]
    (let [pool (Executors/newFixedThreadPool (int n))]
      (try
        (pool-map-doubles pool f things0 things1)
        (finally (.shutdown pool)))))
  (^doubles [n ^clojure.lang.IFn$OD f things0 things1 things2]
    (let [pool (Executors/newFixedThreadPool (int n))]
      (try
        (pool-map-doubles pool f things0 things1 things2)
        (finally (.shutdown pool))))))
;;------------------------------------------------------------------------------
(defn pmap-doubles
  "Like [[pmap]] with output in a <code>double[]</code>."
  (^doubles [^clojure.lang.IFn$OD f things]
    (assert (ifn? f) (print-str "Not a function:" f))
    (nmap-doubles (.availableProcessors (Runtime/getRuntime)) f things))
  (^doubles [^clojure.lang.IFn$OD f things0 things1]
    (assert (ifn? f) (print-str "Not a function:" f))
    (nmap-doubles (.availableProcessors (Runtime/getRuntime)) f things0 things1))
  (^doubles [^clojure.lang.IFn$OD f things0 things1 things2]
    (assert (ifn? f) (print-str "Not a function:" f))
    (nmap-doubles (.availableProcessors (Runtime/getRuntime)) 
                  f things0 things1 things2)))
;;------------------------------------------------------------------------------
(defmulti pool-map 
  "Apply a function to each 'element' of some data structure, returning a
   structure similar to the first argument. Use <code>n</code> threads to 
   evaluate the function calls in parallel."
  {:arglists '( [pool f things0] 
                [pool f things0 things1] 
                [pool f things0 things1 things2])}
  (fn pool-map-dispatch 
    ([pool f things0] 
      [(class f) (class things0)])
    ([pool f things0 things1]
      [(class f) (class things0) (class things1)]) 
    ([pool f things0 things1 things2] 
      [(class f) (class things0) (class things1) (class things2)])))

(defn- pool-map-collect-results 
  ^Iterable 
  [^ExecutorService pool ^java.util.Collection tasks]
  (let [a (ArrayList. (count tasks))]
    (mapc #(.add a (.get ^Future %)) (.invokeAll pool tasks))
    (Collections/unmodifiableList a)))

(defmethod pool-map 
  [IFn Iterator] 
  [^ExecutorService pool ^IFn f ^Iterator things]
  (pool-map-collect-results pool (map #(callable f %) things)))

(defmethod pool-map 
  [IFn Iterable] 
  [^ExecutorService pool  ^IFn f ^Iterable things]
  (pool-map pool f (.iterator things)))

(defmethod pool-map 
  [IFn Iterator Iterator] 
  [^ExecutorService pool ^IFn f ^Iterator things0 ^Iterator things1]
  (pool-map-collect-results 
    pool (map (fn [x0 x1] (callable f x0 x1)) things0 things1)))

(defmethod pool-map 
  [IFn Iterable Iterable] 
  [^ExecutorService pool
   ^IFn f 
   ^Iterable things0 ^Iterable things1]
  (pool-map pool f (.iterator things0) (.iterator things1)))

(defmethod pool-map 
  [IFn Iterator Iterator Iterator] 
  [^ExecutorService pool  ^IFn f 
   ^Iterator i0 ^Iterator i1 ^Iterator i2]
  (pool-map-collect-results 
    pool (map (fn [x0 x1 x2] (callable f x0 x1 x2)) i0 i1 i2)))

(defmethod pool-map 
  [IFn Iterable Iterable Iterable] 
  [^ExecutorService pool  
   ^IFn f 
   ^Iterable things0 ^Iterable things1 ^Iterable things2]
  (pool-map pool f (.iterator things0) (.iterator things1) (.iterator things2)))
;;------------------------------------------------------------------------------
(defn nmap
  "Like [[nmap]] with output in a <code>double[]</code>."
  (^doubles [^long n ^clojure.lang.IFn$OD f things]
    (let [pool (Executors/newFixedThreadPool (int n))]
      (try
        (pool-map pool f things)
        (finally (.shutdown pool)))))
  (^doubles [^long n ^clojure.lang.IFn$OD f things0 things1]
    (let [pool (Executors/newFixedThreadPool (int n))]
      (try
        (pool-map pool f things0 things1)
        (finally (.shutdown pool)))))
  (^doubles [n ^clojure.lang.IFn$OD f things0 things1 things2]
    (let [pool (Executors/newFixedThreadPool (int n))]
      (try
        (pool-map pool f things0 things1 things2)
        (finally (.shutdown pool))))))
;;------------------------------------------------------------------------------
(defn pmap
  
  "Eager version of <code>clojure.core/pmap</code> that uses as many threads as
   there are 
   <a href=\"https://docs.oracle.com/javase/8/docs/api/java/lang/Runtime.html#availableProcessors--\">
   available processors."
  
  (^Iterable [f things]
    (assert (ifn? f) (print-str "Not a function:" f))
    (nmap (.availableProcessors (Runtime/getRuntime)) f things))
  
  (^Iterable [f things0 things1]
    (assert (ifn? f) (print-str "Not a function:" f))
    (nmap (.availableProcessors (Runtime/getRuntime)) f things0 things1))
  
  (^Iterable [f things0 things1 things2]
    (assert (ifn? f) (print-str "Not a function:" f))
    (nmap (.availableProcessors (Runtime/getRuntime)) f things0 things1 things2)))
;;------------------------------------------------------------------------------

