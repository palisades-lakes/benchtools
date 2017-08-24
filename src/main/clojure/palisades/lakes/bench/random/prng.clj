(set! *warn-on-reflection* true) 
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.bench.random.prng
  
  {:doc "pseudo-random number generators."
   :author "palisades dot lakes at gmail dot com"
   :since "2017-04-05"
   :version "2017-08-23"}
  
  (:require [palisades.lakes.bench.random.seed :as seed])
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

(defn uniform-byte-generator 
  ^clojure.lang.IFn$D [^long umin
                       ^long umax
                       ^UniformRandomProvider urp]
  (let [umin (max Byte/MIN_VALUE umin)
        umax (min Byte/MAX_VALUE umax)
        ^DiscreteSampler g (DiscreteUniformSampler. urp umin umax)]
    (fn uniform-byte-generator ^long [] (long (.sample g)))))
;;----------------------------------------------------------------
(defn uniform-double-generator 
  ^clojure.lang.IFn$D [^double umin
                       ^double umax
                       ^UniformRandomProvider urp]
  (let [^ContinuousSampler g (ContinuousUniformSampler. urp umin umax)]
    (fn uniform-double-generator ^double [] (.sample g))))
;;----------------------------------------------------------------
(defn uniform-float-generator 
  ^clojure.lang.IFn$D [^double umin
                       ^double umax
                       ^UniformRandomProvider urp]
  (let [umin (max (- Float/MAX_VALUE) umin)
        umax (min Float/MAX_VALUE umax)
        ^ContinuousSampler g (ContinuousUniformSampler. urp umin umax)]
    (fn uniform-float-generator ^double [] (.sample g))))
;;----------------------------------------------------------------
(defn uniform-int-generator 
  ^clojure.lang.IFn$L [^long umin
                       ^long umax
                       ^UniformRandomProvider urp]
  (let [umin (max Integer/MIN_VALUE umin)
        umax (min Integer/MAX_VALUE umax)
        ^DiscreteSampler g (DiscreteUniformSampler. urp umin umax)]
    (fn uniform-int-generator ^long [] (long (.sample g)))))
;;----------------------------------------------------------------
;; FIXME:
(defn uniform-long-generator 
 ^clojure.lang.IFn$L [^long umin
                      ^long umax
                      ^UniformRandomProvider urp]
 (let [^DiscreteSampler g (DiscreteUniformSampler. urp umin umax)]
   (fn uniform-long-generator ^long [] (.sample g))))
;;----------------------------------------------------------------
(defn uniform-short-generator 
  ^clojure.lang.IFn$L [^long umin
                       ^long umax
                       ^UniformRandomProvider urp]
  (let [umin (max Short/MIN_VALUE umin)
        umax (min Short/MAX_VALUE umax)
        ^DiscreteSampler g (DiscreteUniformSampler. urp umin umax)]
    (fn uniform-short-generator ^long [] (long (.sample g)))))
;;----------------------------------------------------------------
;; Numbers
;;----------------------------------------------------------------
(defn uniformByteGenerator 
  ^clojure.lang.IFn [^long umin
                     ^long umax
                     ^UniformRandomProvider urp]
  (let [^IFn$L g (uniform-byte-generator umin umax urp)]
    (fn UniformByte ^Byte [] 
      (Byte/valueOf (byte (.invokePrim g))))))
;;----------------------------------------------------------------
(defn uniformDoubleGenerator 
  ^clojure.lang.IFn [^double umin
                     ^double umax
                     ^UniformRandomProvider urp]
  (let [^IFn$D g (uniform-double-generator umin umax urp)]
    (fn UniformDouble ^Double [] 
      (Double/valueOf (double (.invokePrim g))))))
;;----------------------------------------------------------------
(defn uniformFloatGenerator 
  ^clojure.lang.IFn [^double umin
                     ^double umax
                     ^UniformRandomProvider urp]
  (let [^IFn$D g (uniform-float-generator umin umax urp)]
    (fn UniformFloat ^Float [] 
      (Float/valueOf (float (.invokePrim g))))))
;;----------------------------------------------------------------
(defn uniformIntegerGenerator 
  ^clojure.lang.IFn [^long umin
                     ^long umax
                     ^UniformRandomProvider urp]
  (let [^IFn$L g (uniform-int-generator umin umax urp)]
    (fn UniformInteger ^Integer [] 
      (Integer/valueOf (int (.invokePrim g))))))
;;----------------------------------------------------------------
;; FIXME: DiscreteUniformSampler only produces int
(defn uniformLongGenerator 
 ^clojure.lang.IFn [^long umin
                    ^long umax
                    ^UniformRandomProvider urp]
 (let [^IFn$L g (uniform-long-generator umin umax urp)]
   (fn UniformLong ^Long [] 
      (Long/valueOf (.invokePrim g)))))
;;----------------------------------------------------------------
(defn uniformShortGenerator 
  ^clojure.lang.IFn [^long umin
                     ^long umax
                     ^UniformRandomProvider urp]
  (let [^IFn$L g (uniform-short-generator umin umax urp)]
    (fn UniformShort ^Short [] 
      (Short/valueOf (short (.invokePrim g))))))
;;----------------------------------------------------------------
(defn uniform-element-generator 
  ^clojure.lang.IFn [^Collection c ^UniformRandomProvider urp]
  (let [^CollectionSampler cs (CollectionSampler. urp c)]
    (fn random-set [] ((.sample cs)))))
;;----------------------------------------------------------------
(defn uniformNumberGenerator 
  ^clojure.lang.IFn [^double umin
                     ^double umax
                     ^UniformRandomProvider urp]
  (let [lmin (long umin)
        lmax (long umax)
        ^CollectionSampler cs 
        (CollectionSampler. 
          urp 
            [(uniformByteGenerator lmin lmax urp)
             (uniformDoubleGenerator umin umax urp)
             (uniformFloatGenerator umin umax urp)
             (uniformIntegerGenerator lmin lmax urp)
             (uniformLongGenerator lmin lmax urp)
             (uniformShortGenerator lmin lmax urp)])]
    (fn UniformNumber ^Number [] ((.sample cs)))))
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
  :name "palisades.lakes.bench.random.PRNG"
  :methods [^:static [uniformRandomProvider 
                      [Object] 
                      org.apache.commons.rng.UniformRandomProvider]
            ^:static [uniformDoubleGenerator 
                      [double
                       double
                       org.apache.commons.rng.UniformRandomProvider] 
                      clojure.lang.IFn$D]
            ^:static [uniformIntGenerator 
                      [long
                       long
                       org.apache.commons.rng.UniformRandomProvider] 
                      clojure.lang.IFn$L]
            ^:static [uniformLongGenerator 
                      [long
                       long
                       org.apache.commons.rng.UniformRandomProvider] 
                      clojure.lang.IFn$L]
            ^:static [uniformElementGenerator 
                      [java.util.Collection
                       org.apache.commons.rng.UniformRandomProvider] 
                      clojure.lang.IFn]
            ])
;;----------------------------------------------------------------
(defn -uniformRandomProvider
  ^org.apache.commons.rng.UniformRandomProvider [seed]
  (uniform-random-provider seed))
(defn -uniformDoubleGenerator 
  ^clojure.lang.IFn$D [^double umin
                       ^double umax
                       ^UniformRandomProvider urp]
  (uniform-double-generator umin umax urp))
(defn -uniformIntGenerator 
  ^clojure.lang.IFn$L [^long umin
                       ^long umax
                       ^UniformRandomProvider urp]
  (uniform-int-generator umin umax urp))
#_(defn -uniformLongGenerator 
   ^clojure.lang.IFn$L [^long umin
                        ^long umax
                        ^UniformRandomProvider urp]
   (uniform-long-generator umin umax urp))
(defn -uniformElementGenerator 
  ^clojure.lang.IFn [^Collection c ^UniformRandomProvider urp]
  (uniform-element-generator c urp))
;;----------------------------------------------------------------
