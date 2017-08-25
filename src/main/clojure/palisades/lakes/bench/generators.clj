(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;; TODO: move dependence on Commons RNG to prng. This should only
;; use no arg generator functions as its source of 
;; 'random' numbers/objects.
;;----------------------------------------------------------------
(ns palisades.lakes.bench.generators
  
  {:doc "Random object generators assuming prngs are represented
         as clojure.lang.Function --- in other words, no direct 
         dependence on any prng library."
   :author "palisades dot lakes at gmail dot com"
   :since "2017-05-29"
   :version "2017-08-25"}
  
  (:require [palisades.lakes.bench.prng :as prng])
  
  (:import [java.util Collections]
           [clojure.lang IFn IFn$D IFn$L]
           [org.apache.commons.rng UniformRandomProvider]
           [org.apache.commons.rng.sampling CollectionSampler]
           [palisades.lakes.bench.java.sets 
            ByteInterval DoubleInterval FloatInterval 
            IntegerInterval LongInterval ShortInterval
            Set]))
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

(defn byte-interval ^IFn [^IFn$L g]
  (fn byte-interval ^ByteInterval [] (ByteInterval/generate g)))
(defn double-interval ^IFn [^IFn$D g]
  (fn double-interval ^DoubleInterval [] (DoubleInterval/generate g)))
(defn float-interval ^IFn [^IFn$D g]
  (fn float-interval ^FloatInterval [] (FloatInterval/generate g)))
(defn integer-interval ^IFn [^IFn$L g]
  (fn integer-interval ^IntegerInterval [] (IntegerInterval/generate g)))
(defn long-interval ^IFn [^IFn$L g] 
  (fn long-interval ^LongInterval [] (LongInterval/generate g)))
(defn short-interval ^IFn [^IFn$L g] 
  (fn short-interval ^ShortInterval [] (ShortInterval/generate g)))
;;----------------------------------------------------------------
(defn singleton
  ^IFn [^IFn generator]
  (fn singleton ^java.util.Set []
    (Collections/singleton (generator))))
;;----------------------------------------------------------------
(defn interval-of-2 ^IFn [^double umin
                          ^double umax
                          ^UniformRandomProvider urp]
  (let [^IFn$D gd (prng/uniform-double umin umax urp)
        ^IFn$L gl (prng/uniform-long (long umin) (long umax) urp)
        ^CollectionSampler cs 
        (CollectionSampler. 
          urp 
          [(integer-interval gl)
           (double-interval gd)])]
    (fn IntervalOf2 [] ((.sample cs))))) 
;;----------------------------------------------------------------
(defn set-of-3 ^IFn [^double umin
                     ^double umax
                     ^UniformRandomProvider urp]
  (let [^IFn$D gd (prng/uniform-double umin umax urp)
        ^IFn$L gl (prng/uniform-long (long umin) (long umax) urp)
        ^IFn gn (prng/uniformNumber umin umax urp)
        ^CollectionSampler cs 
        (CollectionSampler. 
          urp 
          [(singleton gn)
           (integer-interval gl)
           (double-interval gd)])]
    (fn SetOf3 [] ((.sample cs))))) 
;;----------------------------------------------------------------
(defn set-of-7 ^IFn [^double umin
                     ^double umax
                     ^UniformRandomProvider urp]
  (let [^IFn$D gd (prng/uniform-double umin umax urp)
        ^IFn$L gl (prng/uniform-long (long umin) (long umax) urp)
        ^IFn gn (prng/uniformNumber umin umax urp)
        ^CollectionSampler cs 
        (CollectionSampler. 
          urp 
          [(singleton gn)
           (byte-interval gl)
           (integer-interval gl)
           (long-interval gl)
           (double-interval gd)
           (float-interval gd)])]
    (fn SetOf7 [] ((.sample cs))))) 
;;----------------------------------------------------------------
;; could just call prng/objects, but helpful to have the function
;; names? Wouldn't be necessary if we could get at the lexical
;; environment of the function returned by objects.

(defn ByteIntervals ^IFn [^IFn g ^long n] 
  (prng/objects g n ByteInterval))
(defn DoubleIntervals ^IFn [^IFn g ^long n]
  (prng/objects g n DoubleInterval))
(defn FloatIntervals ^IFn [^IFn g ^long n] 
  (prng/objects g n FloatInterval))
(defn IntegerIntervals ^IFn [^IFn g ^long n] 
  (prng/objects g n IntegerInterval))
(defn LongIntervals ^IFn [^IFn g ^long n] 
  (prng/objects g n LongInterval))
(defn ShortIntervals ^IFn [^IFn g ^long n] 
  (prng/objects g n ShortInterval))
(defn Sets ^IFn [^IFn g ^long n] 
  (prng/objects g n Set))
;;----------------------------------------------------------------
