(ns spec_behavior.spec_test
  (:use [corefns.specs])
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]))

;############################################
;### Tests for spec data in ExceptionInfo ###
;###                                      ###
;############################################

(defn hashmap-gen [f]
  (try (eval f)
    (catch clojure.lang.ExceptionInfo e
      (first (vals (:clojure.spec/problems (.getData e)))))))

; We need ' (quote) for the :pred value
(expect {:pred 'seqable?, :val 3, :via [], :in [0]} (hashmap-gen '(empty? 3)))

; The value of key :pred is a lazy sequence
(expect (more-> 'cat first
                :check-seqable second
                'seqable? last)
        (:pred (hashmap-gen '(empty? [] []))))

;; (expect {:reason "Extra input", :pred (lazy-seq (list 'cat :check-seqable 'seqable?)), :val ([]), :via [], :in [1]}
;;         (hashmap-gen '(empty? [] [])))

;; (empty? [] [])
;; (empty?)
;; (empty? true)
;; (empty? [1 2])
;; (empty? nil)

