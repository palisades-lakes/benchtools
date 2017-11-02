(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.bench.prng.seeds
  
  {:doc "Generate independent seeds for palisades.lakes.bench.prng."
   :author "palisades dot lakes at gmail dot com"
   :since "2017-04-05"
   :version "2017-07-25"}
  
  (:require [clojure.java.io :as io]
            [palisades.lakes.bench.seed :as seed])
  (:import java.time.LocalDate))
;;----------------------------------------------------------------
;; for Well44497b
(seed/write
  (seed/generate-randomdotorg-seed 1391)
  (io/file "src" "main" "resources" "seeds" 
           (str "Well44497b-" (LocalDate/now) ".edn")))
;;----------------------------------------------------------------
;; for Mersenne Twister
(seed/write
  (seed/generate-randomdotorg-seed 624)
  (io/file  "src" "main" "resources" "seeds" 
            (str "MT-" (LocalDate/now) ".edn")))
;;----------------------------------------------------------------

