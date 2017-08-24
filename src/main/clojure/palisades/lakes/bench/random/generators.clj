(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.bench.random.generators
  
  {:doc "Random object generators assuming prngs are represented
         as clojure.lang.Function --- in other words, no direct 
         dependence on any prng library."
   :author "palisades dot lakes at gmail dot com"
   :since "2017-05-29"
   :version "2017-08-23"}
  
  (:require [palisades.lakes.bench.random.prng :as prng])
  
  (:import [java.util Collections]
           [clojure.lang IFn IFn$D IFn$L]
           [org.apache.commons.rng UniformRandomProvider]
           [org.apache.commons.rng.sampling CollectionSampler]
           [palisades.lakes.bench.java.sets 
            ByteInterval DoubleInterval FloatInterval 
            IntegerInterval LongInterval ShortInterval]))
;;----------------------------------------------------------------
;; The arguments <code>g</code> are expected to be 'functions'
;; of zero arguments that return a different number (either double 
;; or long, as appropriate) every time they are called.
;; The usual case would be a pseudo-random number generator of
;; some kind.
;; If the generator produces numbers outside the range of the
;; number type used for the endpoints of the interval, 
;; (eg something outside [-128, 128) for ByteInterval)
;; an exception should be thrown, so care has to be taken.

(defn byte-intervals ^IFn [^IFn$L g]
  (fn ByteIntervals ^ByteInterval []
    (ByteInterval/generate g)))
(defn double-intervals ^IFn [^IFn$D g]
  (fn DoubleIntervals ^DoubleInterval []
    (DoubleInterval/generate g)))
(defn float-intervals ^IFn [^IFn$D g]
  (fn FloatIntervals ^FloatInterval []
    (FloatInterval/generate g)))
(defn integer-intervals ^IFn [^IFn$L g]
  (fn IntegerIntervals ^IntegerInterval []
    (IntegerInterval/generate g)))
(defn long-intervals ^IFn [^IFn$L g]
  (fn LongIntervals ^LongInterval []
    (LongInterval/generate g)))
(defn short-intervals ^IFn [^IFn$L g]
  (fn ShortIntervals ^ShortInterval []
    (ShortInterval/generate g)))
;;----------------------------------------------------------------
(defn random-singleton-set
  ^IFn [^IFn generator]
  (fn random-singleton-set ^java.util.Set []
    (Collections/singleton (generator))))
;;----------------------------------------------------------------
(defn interval-of-2 ^IFn [^double umin
                          ^double umax
                          ^UniformRandomProvider urp]
  (let [^IFn$D gd (prng/uniform-double-generator umin umax urp)
        ^IFn$L gl (prng/uniform-long-generator (long umin) (long umax) urp)
        ^CollectionSampler cs 
        (CollectionSampler. 
          urp 
          [(integer-intervals gl)
           (double-intervals gd)])]
    (fn IntervalOf2 [] ((.sample cs))))) 
;;----------------------------------------------------------------
(defn set-of-3 ^IFn [^double umin
                     ^double umax
                     ^UniformRandomProvider urp]
  (let [^IFn$D gd (prng/uniform-double-generator umin umax urp)
        ^IFn$L gl (prng/uniform-long-generator (long umin) (long umax) urp)
        ^IFn gn (prng/uniformNumberGenerator umin umax urp)
        ^CollectionSampler cs 
        (CollectionSampler. 
          urp 
          [(random-singleton-set gn)
           (integer-intervals gl)
           (double-intervals gd)])]
    (fn SetOf3 [] ((.sample cs))))) 
;;----------------------------------------------------------------
(defn set-of-7 ^IFn [^double umin
                     ^double umax
                     ^UniformRandomProvider urp]
  (let [^IFn$D gd (prng/uniform-double-generator umin umax urp)
        ^IFn$L gl (prng/uniform-long-generator (long umin) (long umax) urp)
        ^IFn gn (prng/uniformNumberGenerator umin umax urp)
        ^CollectionSampler cs 
        (CollectionSampler. 
          urp 
          [(random-singleton-set gn)
           (byte-intervals gl)
           (integer-intervals gl)
           (long-intervals gl)
           (double-intervals gd)
           (float-intervals gd)])]
    (fn SetOf7 [] ((.sample cs))))) 
;;----------------------------------------------------------------
(defn generate-ints 
  (^ints [^IFn$L generator 
          ^long n]
    (let [^ints a (int-array n)]
      (dotimes [i n] (aset-int a i (generator)))
      a)))
;;----------------------------------------------------------------
(defn generate-objects 
  (^objects [^IFn generator 
             ^long n
             ^Class element-type]
    (let [^objects sets (make-array element-type n)]
      (dotimes [i n] (aset sets i (generator)))
      sets))
  (^objects [^IFn generator 
             ^long n]
    (generate-objects generator n Object)))
;;----------------------------------------------------------------
