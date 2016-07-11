(ns corefns.specs
  (:require [clojure.spec :as s]))

;;                        Spec for clojure.spec  by Tony
;; ==================================================================================|
;; * For every function with inlining, we can't use spec without overwriting it.     |
;;                                                                                   |
;; * put spec before a funtion -> specify conditions for the core function           |
;;   vs                                                                              |
;;   put spec after a function -> specify conditions for the overwritten function    |
;;                                                                                   |
;; * There are two cases that we need to put spec after we overwrite a function      |
;;     1. When the function has different numbers of :inline-arities like #{2 3}     |
;;     2. When the function has only :inline without :inline-arities                 |
;;                                                                                   |
;; * s/cat gives a key(name) to each element in a sequence and returns a hashmap     |
;;                                                                                   |
;; * 'NO' means the function doesn't need to be overwritten                          |
;;   'O' means the function should be overwritten                                    |
;; ==================================================================================|


;; #######################################
;; #     helper functions/predicates     #
;; #######################################
(defn length1?
  "returns true if a coll has one elements"
  [coll]
  (= (count coll) 1))
(defn length2?
  "returns true if a coll has two elements"
  [coll]
  (= (count coll) 2))
(defn length3?
  "returns true if a coll has three elements"
  [coll]
  (= (count coll) 3))
(defn length-greater0?
  "returns true if a coll has at least one elements"
  [coll]
  (> (count coll) 0))
(defn length-greater1?
  "returns true if a coll has at least two elements"
  [coll]
  (> (count coll) 1))
(defn length-greater2?
  "returns true if a coll has at least three elements"
  [coll]
  (> (count coll) 2))

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
;; (s/fdef empty?
;;   :args (s/cat :check-seqable seqable?))
;; (s/instrument #'empty?)
(s/fdef empty?
  :args (s/and ::length-one
               (s/cat :check-seqable seqable?)))
(s/instrument #'empty?)

; ##### NO #####
;; (s/fdef map
;;       :args (s/cat :check-function ifn? :check-seqable (s/+ seqable?)))
;; (s/instrument #'map)
(s/fdef map
  :args (s/and ::length-greater-one
               (s/cat :check-function ifn? :check-seqable (s/+ seqable?))))
(s/instrument #'map)


; ##### NO #####
;; (s/fdef conj
;;   :args (s/cat :check-seqable seqable? :dummy (s/+ ::s/any)))
;; (s/instrument #'conj)
(s/fdef conj
  :args (s/and ::length-greater-one
               (s/cat :check-seqable seqable? :dummy (s/+ ::s/any))))
(s/instrument #'conj)

; ##### NO #####
;; (s/fdef into
;;       :args (s/cat :check-seqable seqable? :check-seqable seqable?))
;; (s/instrument #'into)
(s/fdef into
  :args (s/and ::length-two
               (s/cat :check-seqable seqable? :check-seqable seqable?)))
(s/instrument #'into)

; ##### NO #####
;; (s/fdef cons
;;       :args (s/cat :dummy ::s/any :check-seqable seqable?))
;; (s/instrument #'cons)
(s/fdef cons
  :args (s/and ::length-two
               (s/cat :dummy ::s/any :check-seqable seqable?)))
(s/instrument #'cons)

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

(s/fdef reduce
        :args (s/or :two-case (s/and ::length-two
                                   (s/cat :check-function ifn? :check-seqable seqable?))
                    :three-case (s/and ::length-three
                                     (s/cat :check-function ifn? :dummy ::s/any :check-seqable seqable?))))
(s/instrument #'reduce)

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

; ##### NO #####
;; (s/fdef filter
;;   :args (s/cat :check-function ifn? :check-seqable seqable?))
;; (s/instrument #'filter)
(s/fdef filter
  :args (s/and ::length-two
               (s/cat :check-function ifn? :check-seqable seqable?)))
(s/instrument #'filter)

; ##### NO #####
;; (s/fdef mapcat
;;   :args (s/cat :check-function ifn? :check-seqable (s/+ seqable?)))
;; (s/instrument #'mapcat)
(s/fdef mapcat
  :args (s/and ::length-greater-one
               (s/cat :check-function ifn? :check-seqable (s/+ seqable?))))
(s/instrument #'mapcat)

; ##### NO #####
; We need s/nilable here because map? and vector? return false for nil
;; (s/fdef assoc
;;   :args (s/cat :check-map-or-vector (s/or :check-map (s/nilable map?) :check-vector vector?)
;;                :dummy ::s/any
;;                :dummies (s/+ ::s/any)))
;; (s/instrument #'assoc)
(s/fdef assoc
  :args (s/and
          ::length-greater-two
          (s/cat :check-map-or-vector (s/or :check-map map? :check-vector vector?)
                 :dummy ::s/any
                 :dummies (s/+ ::s/any))))
(s/instrument #'assoc)

; ##### NO #####
; We need s/nilable here because map? returns false for nil
;; (s/fdef dissoc
;;   :args (s/cat :check-map (s/nilable map?) :dummies (s/* ::s/any)))
;; (s/instrument #'dissoc)
(s/fdef dissoc
  :args (s/and
          ::length-greater-zero
          (s/cat :check-map (s/nilable map?) :dummies (s/* ::s/any))))
(s/instrument #'dissoc)

; ##### NO #####
;; (s/fdef odd?
;;   :args (s/cat :check-integer integer?))
;; (s/instrument #'odd?)
(s/fdef odd?
  :args (s/and ::length-one
               (s/cat :check-integer integer?)))
(s/instrument #'odd?)

; ##### O #####
;; (s/fdef <
;;   :args (s/cat :check-number (s/+ number?)))
;; (s/instrument #'<)
(s/fdef <
  :args (s/and
          ::length-greater-zero
          (s/cat :check-number (s/+ number?))))
(s/instrument #'<)

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
(s/fdef comp
  :args (s/cat :check-function (s/* ifn?)))
(s/instrument #'comp)

; ##### NO #####
;; (s/fdef repeatedly
;;   :args (s/cat :check-number (s/? number?) :check-function ifn?))
(s/fdef repeatedly
        :args (s/or :one-case (s/and ::length-one
                                     (s/cat :check-function ifn?))
                    :two-case (s/and ::length-two
                                     (s/cat :check-number number? :check-function ifn?))))
(s/instrument #'repeatedly)

; ##### NO #####
;; (s/fdef repeat
;;   :args (s/cat :check-number (s/? number?) :dummy ::s/any))
(s/fdef repeat
        :args (s/or :one-case ::length-one
                    :two-case (s/and ::length-two
                                     (s/cat :check-number number? :dummy ::s/any))))
(s/instrument #'repeat)

; ##### NO #####
;; (s/fdef distinct
;;   :args (s/cat :check-seqable seqable?))
;; (s/instrument #'distinct)
(s/fdef distinct
  :args (s/and ::length-one
               (s/cat :check-seqable seqable?)))
(s/instrument #'distinct)
