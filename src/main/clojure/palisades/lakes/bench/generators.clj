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
   :version "2017-08-26"}
  
  (:require [palisades.lakes.bench.prng :as prng])
  
  (:import [java.util Collections]
           [clojure.lang IFn IFn$D IFn$L]
           [org.apache.commons.rng UniformRandomProvider]
           [org.apache.commons.rng.sampling CollectionSampler]
           [palisades.lakes.bench.java.sets 
            ByteInterval DoubleInterval FloatInterval 
            IntegerInterval LongInterval ShortInterval
            Set]
           [palisades.lakes.bench.java.spaces.linear
            LinearFunction Vector]
           [palisades.lakes.bench.java.spaces.linear.r2
            B2 D2 F2 I2 L2 S2 B22 D22 F22 I22 L22 S22]))
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
  (assert (< (Math/nextDown -128.0) umin umax (Math/nextUp 127.0)))
  (let [^IFn$D gd (prng/uniform-double umin umax urp)
        ^IFn$L gl (prng/uniform-long (long umin) (long umax) urp)
        ^IFn gn (prng/uniformNumber umin umax urp)
        ^CollectionSampler cs 
        (CollectionSampler. 
          urp 
          [(singleton gn)
           (byte-interval gl)
           (short-interval gl)
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
;; 2d vectors 
;; (elements of vector spaces, not a special list implementation!)
;;----------------------------------------------------------------
;; The arguments <code>g</code> are expected to be 'functions'
;; of zero arguments that return a different number (either double 
;; or long, as appropriate) every time they are called.
;; The usual case would be a pseudo-random number generator of
;; some kind.
;; If the generator produces numbers outside the range of the
;; number type used (eg something outside [-128, 128) for B2)
;; an exception should be thrown, so care has to be taken.

(defn b2 ^IFn [^IFn$L g] (fn b2 ^B2 [] (B2/generate g)))
(defn d2 ^IFn [^IFn$D g] (fn d2 ^D2 [] (D2/generate g)))
(defn f2 ^IFn [^IFn$D g] (fn f2 ^F2 [] (F2/generate g)))
(defn i2 ^IFn [^IFn$L g] (fn i2 ^I2 [] (I2/generate g)))
(defn l2 ^IFn [^IFn$L g] (fn l2 ^L2 [] (L2/generate g)))
(defn s2 ^IFn [^IFn$L g] (fn s2 ^S2 [] (S2/generate g)))
;;----------------------------------------------------------------
(defn v2 ^IFn [^double umin
               ^double umax
               ^UniformRandomProvider urp]
  "return a generator function for random 2d vectors, of 
   equal probability of each of the coordinate types,
   coordinates uniformly distributed in [umin,umax).
   umin and umax must be within the range of signed bytes."
  (assert (< (Math/nextDown -128.0) umin umax (Math/nextUp 127.0)))
  (let [^IFn$D gd (prng/uniform-double umin umax urp)
        ^IFn$L gl (prng/uniform-long (long umin) (long umax) urp)
        ^IFn gn (prng/uniformNumber umin umax urp)
        ^CollectionSampler cs (CollectionSampler. 
                                urp 
                                [(b2 gl) (s2 gl) (i2 gl) (l2 gl) 
                (d2 gd) (f2 gd)])]
    (fn v2 [] ((.sample cs)))))
;;----------------------------------------------------------------
;; arrays of vectors with varying element types
;; assumes generator `g` returns something assignable to the 
;; element type.
;;----------------------------------------------------------------
(defn b2s ^IFn [^IFn g ^long n] (prng/objects g n B2))
(defn d2s ^IFn [^IFn g ^long n] (prng/objects g n D2))
(defn f2s ^IFn [^IFn g ^long n] (prng/objects g n F2))
(defn i2s ^IFn [^IFn g ^long n] (prng/objects g n I2))
(defn l2s ^IFn [^IFn g ^long n] (prng/objects g n L2))
(defn s2s ^IFn [^IFn g ^long n] (prng/objects g n S2))
(defn vectors ^IFn [^IFn g ^long n] (prng/objects g n Vector))
;;----------------------------------------------------------------
;; 2x2 matrices
;;----------------------------------------------------------------
(defn b22 ^IFn [^IFn$L g] (fn b22 ^B22 [] (B22/generate g)))
(defn d22 ^IFn [^IFn$D g] (fn d22 ^D22 [] (D22/generate g)))
(defn f22 ^IFn [^IFn$D g] (fn f22 ^F22 [] (F22/generate g)))
(defn i22 ^IFn [^IFn$L g] (fn i22 ^I22 [] (I22/generate g)))
(defn l22 ^IFn [^IFn$L g] (fn l22 ^L22 [] (L22/generate g)))
(defn s22 ^IFn [^IFn$L g] (fn s22 ^S22 [] (S22/generate g)))
;;----------------------------------------------------------------
(defn m22 ^IFn [^double umin
                ^double umax
                ^UniformRandomProvider urp]
  "return a generator function for random 2x2 matrices, of 
   equal probability of each of the coordinate types,
   coordinates uniformly distributed in [umin,umax).
   umin and umax must be within the range of signed bytes."
  (assert (< (Math/nextDown -128.0) umin umax (Math/nextUp 127.0)))
  (let [^IFn$D gd (prng/uniform-double umin umax urp)
        ^IFn$L gl (prng/uniform-long (long umin) (long umax) urp)
        ^IFn gn (prng/uniformNumber umin umax urp)
        ^CollectionSampler cs (CollectionSampler. 
                                urp 
                                [(b22 gl) (s22 gl) (i22 gl) 
                                 (l22 gl) (d22 gd) (f22 gd)])]
    (fn m22 [] ((.sample cs)))))
;;----------------------------------------------------------------
;; arrays of matrices with varying element types
;; assumes generator `g` returns something assignable to the 
;; element type.
;;----------------------------------------------------------------
(defn b22s ^IFn [^IFn g ^long n] (prng/objects g n B22))
(defn d22s ^IFn [^IFn g ^long n] (prng/objects g n D22))
(defn f22s ^IFn [^IFn g ^long n] (prng/objects g n F22))
(defn i22s ^IFn [^IFn g ^long n] (prng/objects g n I22))
(defn l22s ^IFn [^IFn g ^long n] (prng/objects g n L22))
(defn s22s ^IFn [^IFn g ^long n] (prng/objects g n S22))
(defn linearfunctions ^IFn [^IFn g ^long n] 
  (prng/objects g n LinearFunction))
;;----------------------------------------------------------------
