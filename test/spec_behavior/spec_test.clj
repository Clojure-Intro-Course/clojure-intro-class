(ns spec_behavior.spec_test
  (:use [corefns.specs])
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]))

;############################################
;### Tests for spec data in ExceptionInfo ###
;###                                      ###
;############################################

(defn hashmap-gen
  "takes a quoted function call and returns an auto-generated
  spec hash-map when an error happens"
  [f]
  (try (eval f)
    (catch clojure.lang.ExceptionInfo e
      (first (vals (:clojure.spec/problems (.getData e)))))))

; We need ' (quote) for the :pred value
(expect {:pred 'seqable?, :val 3, :via [], :in [0]} (hashmap-gen '(empty? 3)))

; The value of key :pred is a lazy sequence
;; (expect (more-> 'cat first
;;                 :check-seqable second
;;                 'seqable? last)
;;         (:pred (hashmap-gen '(empty? [] []))))
(expect {:pred 'length1?, :val '([] []), :via [:corefns.specs/length-one], :in []} (hashmap-gen '(empty? [] [])))
(expect {:pred 'length1?, :val nil, :via [:corefns.specs/length-one], :in []} (hashmap-gen '(empty?)))
(expect {:pred 'seqable?, :val true, :via [], :in [0]} (hashmap-gen '(empty? true)))
(expect false (empty? [1 2]))
(expect true (empty? nil))


