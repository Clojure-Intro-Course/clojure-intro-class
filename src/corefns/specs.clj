;; (ns corefns.specs
;;   (:require [clojure.spec :as s]))
(ns corefns.specs
  (require [clojure.spec :as s]
           [clojure.spec.test :as stest]))

;;                        Spec for clojure.spec  by Tony
;; =======================================================================================|
;; * For every function with inlining, we can't use spec without overwriting it.          |
;;                                                                                        |
;; * put spec before a funtion -> specify conditions for the core function                |
;;   vs                                                                                   |
;;   put spec after a function -> specify conditions for the overwritten function         |
;;                                                                                        |
;; * There are two cases that we need to put spec after we overwrite a function           |
;;     1. When the function has different numbers of :inline-arities like #{2 3}          |
;;     2. When the function has only :inline without :inline-arities                      |
;;                                                                                        |
;; * s/cat gives a key(name) to each element in a sequence and returns a hashmap          |
;;                                                                                        |
;; * To get the function name, we first need to attach the spec to the core function      |
;;   and then overwrite them (overwriting first doesn't work)                             |
;;     1. Overwritten map    : #object[corefns.corefns$map ... ]                          |
;;     2. Not overwritten map: #object[clojure.spec.test$spec_checking_fn$fn__12959 ... ] |
;;                                                                                        |
;; * 'NO' means the function doesn't need to be overwritten                               |
;;   'O' means the function should be overwritten                                         |
;; =======================================================================================|


;; #######################################
;; #     helper functions/predicates     #
;; #######################################
(defn length1? [coll] (= (count coll) 1))
(defn length2? [coll] (= (count coll) 2))
(defn length3? [coll] (= (count coll) 3))
(defn length-greater0? [coll] (> (count coll) 0))
(defn length-greater1? [coll] (> (count coll) 1))
(defn length-greater2? [coll] (> (count coll) 2))

(s/def ::length-one length1?)
(s/def ::length-two length2?)
(s/def ::length-three length3?)
(s/def ::length-greater-zero length-greater0?)
(s/def ::length-greater-one length-greater1?)
(s/def ::length-greater-two length-greater2?)


;; #######################################
;; #          specs                      #
;; #######################################

; ##### NO #####
; For alpha version 7
;; (s/fdef empty?
;;   :args (s/cat :check-seqable seqable?))
;; (s/instrument #'empty?)
;; (s/fdef empty?
;;   :args (s/and ::length-one
;;                (s/cat :check-seqable seqable?)))
;; (s/instrument #'empty?)
(s/fdef clojure.core/empty?
  :args (s/and ::length-one
               (s/cat :check-seqable seqable?)))
(stest/instrument 'clojure.core/empty?)

; ##### NO #####
;; (s/fdef map
;;       :args (s/cat :check-function ifn? :check-seqable (s/+ seqable?)))
;; (s/instrument #'map)
;; (s/fdef map
;;   :args (s/and ::length-greater-one
;;                (s/cat :check-function ifn? :check-seqable (s/+ seqable?))))
;; (s/instrument #'map)
(s/fdef clojure.core/map
  :args (s/and ::length-greater-one
               (s/cat :check-function ifn? :check-seqable (s/+ seqable?))))
(stest/instrument 'clojure.core/map)


; ##### NO #####
;; (s/fdef conj
;;   :args (s/cat :check-seqable seqable? :dummy (s/+ ::s/any)))
;; (s/instrument #'conj)
;; (s/fdef conj
;;   :args (s/and ::length-greater-one
;;                (s/cat :check-seqable seqable? :dummy (s/+ ::s/any))))
;; (s/instrument #'conj)
(s/fdef clojure.core/conj
  :args (s/and ::length-greater-one
               (s/cat :check-seqable seqable? :dummy (s/+ any?))))
(stest/instrument 'clojure.core/conj)

; ##### NO #####
;; (s/fdef into
;;       :args (s/cat :check-seqable seqable? :check-seqable seqable?))
;; (s/instrument #'into)
;; (s/fdef into
;;   :args (s/and ::length-two
;;                (s/cat :check-seqable seqable? :check-seqable seqable?)))
;; (s/instrument #'into)
(s/fdef clojure.core/into
  :args (s/and ::length-two
               (s/cat :check-seqable seqable? :check-seqable seqable?)))
(stest/instrument 'clojure.core/into)

; ##### NO #####
;; (s/fdef cons
;;       :args (s/cat :dummy ::s/any :check-seqable seqable?))
;; (s/instrument #'cons)
;; (s/fdef cons
;;   :args (s/and ::length-two
;;                (s/cat :dummy ::s/any :check-seqable seqable?)))
;; (s/instrument #'cons)
(s/fdef clojure.core/cons
  :args (s/and ::length-two
               (s/cat :dummy any? :check-seqable seqable?)))
(stest/instrument 'clojure.core/cons)

; ##### NO #####
; This doesn't differenciate two and three arity cases - issue #116
;; (s/fdef reduce
;;         :args (s/cat :check-funtion ifn? :dummy (s/? ::s/any) :check-seqable seqable?))
; Doesn't work - s/cat returns a hashmap
;; (s/fdef reduce
;;   :args (s/and (s/or :two (s/cat :dummy ::s/any :dummy ::s/any)
;;                      :three (s/cat :dummy ::s/any :dummy ::s/any :dummy ::s/any))
;;                (s/or :three-args (s/cat :check-function ifn? :dummy ::s/any :check-seqable seqable?)
;;                      :two-args (s/cat :check-function ifn? :check-seqable seqable?))))
;; (s/fdef reduce
;;         :args (s/or
;;                 :three (s/cat :check-function ifn? :dummy ::s/any :check-seqable seqable?)
;;                 :two (s/cat :check-function ifn? :check-seqable seqable?)))

;; (s/fdef reduce
;;         :args (s/or :two-case (s/and ::length-two
;;                                      (s/cat :check-function ifn? :check-seqable seqable?))
;;                     :three-case (s/and ::length-three
;;                                        (s/cat :check-function ifn? :dummy ::s/any :check-seqable seqable?))))
;; (s/instrument #'reduce)
(s/fdef clojure.core/reduce
        :args (s/or :two-case (s/and ::length-two
                                     (s/cat :check-function ifn? :check-seqable seqable?))
                    :three-case (s/and ::length-three
                                       (s/cat :check-function ifn? :dummy any? :check-seqable seqable?))))
(stest/instrument 'clojure.core/reduce)

; ##### O ##### - TODO: doesn't work unless the spec is after the overwritten function

;; (s/fdef nth
;;   :args (s/cat :check-seqable seqable? :check-number number? :dummy (s/? ::s/any)))
;; (s/instrument #'nth)
;; (s/fdef nth
;;   :args (s/or :two-case (s/and ::length-two
;;                                (s/cat :check-seqable seqable? :check-number number?))
;;               :three-case (s/and ::length-three
;;                                  (s/cat :check-seqable seqable? :check-number number? :dummy ::s/any))))
;; (s/instrument #'nth)


;; List of functions that have specs and aren't overwritten:
; ##### NO #####
;; (s/fdef filter
;;   :args (s/and ::length-two
;;                (s/cat :check-function ifn? :check-seqable seqable?)))
;; (s/instrument #'filter)
(s/fdef clojure.core/filter
  :args (s/and ::length-two
               (s/cat :check-function ifn? :check-seqable seqable?)))
(stest/instrument 'clojure.core/filter)


; ##### NO #####
;; (s/fdef mapcat
;;   :args (s/cat :check-function ifn? :check-seqable (s/+ seqable?)))
;; (s/instrument #'mapcat)
;; (s/fdef mapcat
;;   :args (s/and ::length-greater-one
;;                (s/cat :check-function ifn? :check-seqable (s/+ seqable?))))
;; (s/instrument #'mapcat)
(s/fdef clojure.core/mapcat
  :args (s/and ::length-greater-one
               (s/cat :check-function ifn? :check-seqable (s/+ seqable?))))
(stest/instrument 'clojure.core/mapcat)

; ##### NO #####
; We need s/nilable here because map? and vector? return false for nil
;; (s/fdef assoc
;;   :args (s/cat :check-map-or-vector (s/or :check-map (s/nilable map?) :check-vector vector?)
;;                :dummy ::s/any
;;                :dummies (s/+ ::s/any)))
;; (s/instrument #'assoc)
;; (s/fdef assoc
;;   :args (s/and ::length-greater-two
;;                (s/cat :check-map-or-vector (s/or :check-map (s/nilable map?) :check-vector vector?)
;;                       :dummy ::s/any
;;                       :dummies (s/+ ::s/any))))
;; (s/instrument #'assoc)
(s/fdef clojure.core/assoc
  :args (s/and ::length-greater-two
               (s/cat :check-map-or-vector (s/or :check-map (s/nilable map?) :check-vector vector?)
                      :dummy any?
                      :dummies (s/+ any?))))
(stest/instrument 'clojure.core/assoc)

; ##### NO #####
; We need s/nilable here because map? returns false for nil
;; (s/fdef dissoc
;;   :args (s/cat :check-map (s/nilable map?) :dummies (s/* ::s/any)))
;; (s/instrument #'dissoc)
;; (s/fdef dissoc
;;   :args (s/and ::length-greater-zero
;;                (s/cat :check-map (s/nilable map?) :dummies (s/* ::s/any))))
;; (s/instrument #'dissoc)
(s/fdef clojure.core/dissoc
  :args (s/and ::length-greater-zero
               (s/cat :check-map (s/nilable map?) :dummies (s/* any?))))
(stest/instrument 'clojure.core/dissoc)

; ##### NO #####
;; (s/fdef odd?
;;   :args (s/cat :check-integer integer?))
;; (s/instrument #'odd?)
;; (s/fdef odd?
;;   :args (s/and ::length-one
;;                (s/cat :check-integer integer?)))
;; (s/instrument #'odd?)
(s/fdef clojure.core/odd?
  :args (s/and ::length-one
               (s/cat :check-integer integer?)))
(stest/instrument 'clojure.core/odd?)

; ##### NO #####
;; (s/fdef even?
;;   :args (s/and ::length-one
;;                (s/cat :check-integer integer?)))
;; (s/instrument #'even?)
(s/fdef clojure.core/even?
  :args (s/and ::length-one
               (s/cat :check-integer integer?)))
(stest/instrument 'clojure.core/even?)

; ##### O #####
;; (s/fdef <
;;   :args (s/cat :check-number (s/+ number?)))
;; (s/instrument #'<)
;; (s/fdef <
;;   :args (s/and ::length-greater-zero
;;                (s/cat :check-number (s/+ number?))))
;; (s/instrument #'<)
(s/fdef clojure.core/<
  :args (s/and ::length-greater-zero
               (s/cat :check-number (s/+ number?))))
(stest/instrument 'clojure.core/<)

; ##### O ##### - TODO: doesn't work. the same behavior as nth
;; (s/fdef quot
;;   :args (s/cat :check-number number? :check-number number?))
;; (s/instrument #'quot)
;; (s/fdef quot
;;   :args (s/and
;;           ::length-two
;;           (s/cat :check-number number? :check-number number?)))
;; (s/instrument #'quot)

; ##### NO #####
;; (s/fdef comp
;;   :args (s/cat :check-function (s/* ifn?)))
;; (s/instrument #'comp)
(s/fdef clojure.core/comp
  :args (s/cat :check-function (s/* ifn?)))
(stest/instrument 'clojure.core/comp)

; ##### NO #####
;; (s/fdef repeatedly
;;   :args (s/cat :check-number (s/? number?) :check-function ifn?))
;; (s/fdef repeatedly
;;   :args (s/or :one-case (s/and ::length-one
;;                                (s/cat :check-function ifn?))
;;               :two-case (s/and ::length-two
;;                                (s/cat :check-number number? :check-function ifn?))))
;; (s/instrument #'repeatedly)
(s/fdef clojure.core/repeatedly
  :args (s/or :one-case (s/and ::length-one
                               (s/cat :check-function ifn?))
              :two-case (s/and ::length-two
                               (s/cat :check-number number? :check-function ifn?))))
(stest/instrument 'clojure.core/repeatedly)

; ##### NO #####
;; (s/fdef repeat
;;   :args (s/cat :check-number (s/? number?) :dummy ::s/any))
;; (s/fdef repeat
;;   :args (s/or :one-case ::length-one
;;               :two-case (s/and ::length-two
;;                                (s/cat :check-number number? :dummy ::s/any))))
;; (s/instrument #'repeat)
(s/fdef clojure.core/repeat
  :args (s/or :one-case ::length-one
              :two-case (s/and ::length-two
                               (s/cat :check-number number? :dummy any?))))
(stest/instrument 'clojure.core/repeat)

; ##### NO #####
;; (s/fdef distinct
;;   :args (s/cat :check-seqable seqable?))
;; (s/instrument #'distinct)
;; (s/fdef distinct
;;   :args (s/and ::length-one
;;                (s/cat :check-seqable seqable?)))
;; (s/instrument #'distinct)
(s/fdef clojure.core/distinct
  :args (s/and ::length-one
               (s/cat :check-seqable seqable?)))
(stest/instrument 'clojure.core/distinct)

(def corefns-map
  {(str map) "map", (str filter) "filter", (str reduce) "reduce", (str empty?) "empty?",
   (str conj) "conj", (str into) "into", (str cons) "cons", (str mapcat) "mapcat",
   (str assoc) "assoc", (str dissoc) "dissoc", (str odd?) "odd?", (str even?) "even?",
   (str <) "<", (str comp) "comp", (str repeatedly) "repeatedly", (str repeat) "repeat",
   (str distinct) "distinct"})
