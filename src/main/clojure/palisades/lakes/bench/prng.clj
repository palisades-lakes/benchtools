(set! *warn-on-reflection* true) 
(set! *unchecked-math* :warn-on-boxed)
;; TODO: Move things that don't depend on Commons RNG to 
;; generators namespace.
;;----------------------------------------------------------------
(ns palisades.lakes.bench.prng
  
  {:doc "pseudo-random number generators."
   :author "palisades dot lakes at gmail dot com"
   :since "2017-04-05"
   :version "2017-08-25"}

  (:refer-clojure 
    :exclude [bytes doubles floats ints longs objects shorts])
  
  (:require [palisades.lakes.bench.seed :as seed])

  (:import [clojure.lang IFn IFn$D IFn$L]
           [java.util Collection]
           [org.apache.commons.rng UniformRandomProvider]
           [org.apache.commons.rng.sampling CollectionSampler]
           [org.apache.commons.rng.sampling.distribution 
            ContinuousSampler ContinuousUniformSampler 
            DiscreteSampler DiscreteUniformSampler]
           [org.apache.commons.rng.simple RandomSource]))
;;----------------------------------------------------------------
;; http://www.iro.umontreal.ca/~panneton/WELLRNG.html
;; http://commons.apache.org/proper/commons-math/javadocs/api-3.6.1/org/apache/commons/math3/random/Well44497b.html
(defn- well44497b 
  ^org.apache.commons.rng.UniformRandomProvider [^ints seed]
  (assert (== 1391 (int (alength seed))))
  (RandomSource/create RandomSource/WELL_44497_B seed nil))
;;----------------------------------------------------------------
#_(defn- mersenne-twister 
    ^org.apache.commons.rng.UniformRandomProvider [^ints seed]
    (assert (== 624 (int (alength seed))))
    (RandomSource/create RandomSource/MT seed nil))
;;----------------------------------------------------------------
;; default is well44497b.
;; TODO: make this private?

(defn uniform-random-provider
  ^org.apache.commons.rng.UniformRandomProvider [seed]
  (well44497b (seed/seed seed)))
;;----------------------------------------------------------------
;; primitive numbers
;;----------------------------------------------------------------
;; These generators are written to truncate to the range of the
;; number type. Can't use unchecked-byte, etc., because that just
;; interprets the low order bits as the given type, and doesn't
;; truncate to the relevant min/max.
;;
;; FIXME: DiscreteUNiformSampler is int,int -> int.
;; Can't handle long randges or inputs.

(defn uniform-byte 
  ^clojure.lang.IFn$D [^long umin
                       ^long umax
                       ^UniformRandomProvider urp]
  (let [umin (max Byte/MIN_VALUE umin)
        umax (min Byte/MAX_VALUE umax)
        ^DiscreteSampler g (DiscreteUniformSampler. urp umin umax)]
    (fn uniform-byte ^long [] (long (.sample g)))))
;;----------------------------------------------------------------
(defn uniform-double 
  ^clojure.lang.IFn$D [^double umin
                       ^double umax
                       ^UniformRandomProvider urp]
  (let [^ContinuousSampler g (ContinuousUniformSampler. urp umin umax)]
    (fn uniform-double ^double [] (.sample g))))
;;----------------------------------------------------------------
(defn uniform-float 
  ^clojure.lang.IFn$D [^double umin
                       ^double umax
                       ^UniformRandomProvider urp]
  (let [umin (max (- Float/MAX_VALUE) umin)
        umax (min Float/MAX_VALUE umax)
        ^ContinuousSampler g (ContinuousUniformSampler. urp umin umax)]
    (fn uniform-float ^double [] (.sample g))))
;;----------------------------------------------------------------
(defn uniform-int 
  ^clojure.lang.IFn$L [^long umin
                       ^long umax
                       ^UniformRandomProvider urp]
  (let [umin (max Integer/MIN_VALUE umin)
        umax (min Integer/MAX_VALUE umax)
        ^DiscreteSampler g (DiscreteUniformSampler. urp umin umax)]
    (fn uniform-int ^long [] (long (.sample g)))))
;;----------------------------------------------------------------
;; FIXME: only handles int ranges
(defn uniform-long 
 ^clojure.lang.IFn$L [^long umin
                      ^long umax
                      ^UniformRandomProvider urp]
 (let [^DiscreteSampler g (DiscreteUniformSampler. urp umin umax)]
   (fn uniform-long ^long [] (.sample g))))
;;----------------------------------------------------------------
(defn uniform-short 
  ^clojure.lang.IFn$L [^long umin
                       ^long umax
                       ^UniformRandomProvider urp]
  (let [umin (max Short/MIN_VALUE umin)
        umax (min Short/MAX_VALUE umax)
        ^DiscreteSampler g (DiscreteUniformSampler. urp umin umax)]
    (fn uniform-short ^long [] (long (.sample g)))))
;;----------------------------------------------------------------
;; Numbers
;;----------------------------------------------------------------
(defn uniformByte 
  ^clojure.lang.IFn [^long umin
                     ^long umax
                     ^UniformRandomProvider urp]
  (let [^IFn$L g (uniform-byte umin umax urp)]
    (fn uniformByte ^Byte [] 
      (Byte/valueOf (byte (.invokePrim g))))))
;;----------------------------------------------------------------
(defn uniformDouble 
  ^clojure.lang.IFn [^double umin
                     ^double umax
                     ^UniformRandomProvider urp]
  (let [^IFn$D g (uniform-double umin umax urp)]
    (fn uniformDouble ^Double [] 
      (Double/valueOf (double (.invokePrim g))))))
;;----------------------------------------------------------------
(defn uniformFloat 
  ^clojure.lang.IFn [^double umin
                     ^double umax
                     ^UniformRandomProvider urp]
  (let [^IFn$D g (uniform-float umin umax urp)]
    (fn uniformFloat ^Float [] 
      (Float/valueOf (float (.invokePrim g))))))
;;----------------------------------------------------------------
(defn uniformInteger 
  ^clojure.lang.IFn [^long umin
                     ^long umax
                     ^UniformRandomProvider urp]
  (let [^IFn$L g (uniform-int umin umax urp)]
    (fn uniformInteger ^Integer [] 
      (Integer/valueOf (int (.invokePrim g))))))
;;----------------------------------------------------------------
;; FIXME: DiscreteUniformSampler only produces int
(defn uniformLong 
 ^clojure.lang.IFn [^long umin
                    ^long umax
                    ^UniformRandomProvider urp]
 (let [^IFn$L g (uniform-long umin umax urp)]
   (fn uniformLong ^Long [] 
      (Long/valueOf (.invokePrim g)))))
;;----------------------------------------------------------------
(defn uniformShort 
  ^clojure.lang.IFn [^long umin
                     ^long umax
                     ^UniformRandomProvider urp]
  (let [^IFn$L g (uniform-short umin umax urp)]
    (fn uniformShort ^Short [] 
      (Short/valueOf (short (.invokePrim g))))))
;;----------------------------------------------------------------
(defn uniformNumber 
  ^clojure.lang.IFn [^double umin
                     ^double umax
                     ^UniformRandomProvider urp]
  (let [lmin (long umin)
        lmax (long umax)
        ^CollectionSampler cs 
        (CollectionSampler. 
          urp 
            [(uniformByte lmin lmax urp)
             (uniformDouble umin umax urp)
             (uniformFloat umin umax urp)
             (uniformInteger lmin lmax urp)
             (uniformLong lmin lmax urp)
             (uniformShort lmin lmax urp)])]
    (fn uniformNumber ^Number [] ((.sample cs)))))
;;----------------------------------------------------------------
(defn uniform-element
  ^clojure.lang.IFn [^Collection c ^UniformRandomProvider urp]
  (let [^CollectionSampler cs (CollectionSampler. urp c)]
    (fn random-element [] ((.sample cs)))))
;;----------------------------------------------------------------
;; will throw exceptions if `generator` returns values outside
;; primitive type range
(defn bytes ^"[B" [^IFn$L generator ^long n]
  (let [n (int n)
        ^"[B" a (byte-array n)]
    (dotimes [i n] (aset-byte a i (byte (generator))))
    a))
(defn doubles ^"[D" [^IFn$D generator ^long n]
  (let [n (int n)
        ^"[D" a (double-array n)]
    (dotimes [i n] (aset-double a i (generator)))
    a))
(defn floats ^"[F" [^IFn$D generator ^long n]
  (let [n (int n)
        ^"[F" a (float-array n)]
    (dotimes [i n] (aset-float i (generator)))
    a))
(defn ints ^"[I" [^IFn$L generator ^long n]
  (let [n (int n)
        ^"[I" a (int-array n)]
    (dotimes [i n] (aset-int a i (generator)))
    a))
(defn longs ^"[J" [^IFn$L generator ^long n]
  (let [n (int n)
        ^"[J" a (long-array n)]
    (dotimes [i n] (aset-long a i (generator)))
    a))
(defn shorts ^"[S" [^IFn$L generator ^long n]
  (let [n (int n)
        ^"[S" a (short-array n)]
    (dotimes [i n] (aset-short a i (generator)))
    a))
;;----------------------------------------------------------------
(defn objects 
  (^"[Ljava.lang.Object;" [^IFn generator 
                           ^long n
                           ^Class element-type]
    (let [n (int n)
          ^"[Ljava.lang.Object;" a (make-array element-type (int n))]
      (dotimes [i (int n)] (aset a i (generator)))
      a))
  (^"[Ljava.lang.Object;" [^IFn generator 
                           ^long n]
    (objects generator n Object)))
;;----------------------------------------------------------------
;; will throw exceptions if `generator` returns objects that 
;; aren't the expected Number type.
;; TODO: figure out why names that differ only in case cause
;; compiler errors
(defn ByteArray ^"[Ljava.lang.Byte;" [^IFn generator ^long n]
  (objects generator n java.lang.Byte))
(defn DoubleArray ^"[Ljava.lang.Double;" [^IFn generator ^long n]
  (objects generator n java.lang.Double))
(defn FloatArray ^"[Ljava.lang.Float;" [^IFn generator ^long n]
  (objects generator n java.lang.Float))
(defn IntegerArray ^"[Ljava.lang.Integer;" [^IFn generator ^long n]
  (objects generator n java.lang.Integer))
(defn LongArray ^"[Ljava.lang.Long;" [^IFn generator ^long n]
  (objects generator n java.lang.Long))
(defn ShortArray ^"[Ljava.lang.Short;" [^IFn generator ^long n]
  (objects generator n java.lang.Short))
(defn NumberArray ^"[Ljava.lang.Number;" [^IFn generator ^long n]
  (objects generator n java.lang.Number))
;;----------------------------------------------------------------
;; NOT thread safe!
(defn cycling-generator ^clojure.lang.IFn [generators]
  (let [^ints i (int-array 1)
        n (int (count generators))]
    (aset-int i 0 0)
    (fn cycling-generator []
      (let [ii (aget i 0)
            gi (nth generators ii)]
        (aset-int i 0 (mod (inc ii) n))
        (gi)))))
;;----------------------------------------------------------------
;; TODO: do this so it has a better function name
;; implement IFn with toString?
(defn nested-uniform-generator 
  ^clojure.lang.IFn [generators ^UniformRandomProvider urp]
  (let [^CollectionSampler cs (CollectionSampler. urp generators)]
    (fn nested-uniform-generator [] ((.sample cs)))))
;;----------------------------------------------------------------
;; Java callable static methods
(gen-class 
  :name "palisades.lakes.bench.PRNG"
  :methods [^:static [uniformRandomProvider 
                      [Object] 
                      org.apache.commons.rng.UniformRandomProvider]
            ^:static [uniformDouble 
                      [double
                       double
                       org.apache.commons.rng.UniformRandomProvider] 
                      clojure.lang.IFn$D]
            ^:static [uniformInt 
                      [long
                       long
                       org.apache.commons.rng.UniformRandomProvider] 
                      clojure.lang.IFn$L]
            ^:static [uniformLong 
                      [long
                       long
                       org.apache.commons.rng.UniformRandomProvider] 
                      clojure.lang.IFn$L]
            ^:static [uniformElement 
                      [java.util.Collection
                       org.apache.commons.rng.UniformRandomProvider] 
                      clojure.lang.IFn]
            ])
;;----------------------------------------------------------------
(defn -uniformRandomProvider
  ^org.apache.commons.rng.UniformRandomProvider [seed]
  (uniform-random-provider seed))
(defn -uniformDouble 
  ^clojure.lang.IFn$D [^double umin
                       ^double umax
                       ^UniformRandomProvider urp]
  (uniform-double umin umax urp))
(defn -uniformInt 
  ^clojure.lang.IFn$L [^long umin
                       ^long umax
                       ^UniformRandomProvider urp]
  (uniform-int umin umax urp))
#_(defn -uniformLong 
   ^clojure.lang.IFn$L [^long umin
                        ^long umax
                        ^UniformRandomProvider urp]
   (uniform-long-generator umin umax urp))
(defn -uniformElement 
  ^clojure.lang.IFn [^Collection c ^UniformRandomProvider urp]
  (uniform-element c urp))
;;----------------------------------------------------------------
