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
;; * 'NO' means the function doesn't need to be overwritten                          |
;;   'O' means the function should be overwritten                                    |
;; ==================================================================================|


; NO
(s/fdef empty?
  :args (s/cat :check-seqable seqable?))
(s/instrument #'empty?)

; NO
(s/fdef map
      :args (s/cat :check-function ifn? :check-seqable (s/+ seqable?)))
(s/instrument #'map)

; NO
(s/fdef conj
  :args (s/cat :check-seqable seqable? :dummy (s/+ ::s/any)))
(s/instrument #'conj)

; NO
(s/fdef into
      :args (s/cat :check-seqable seqable? :check-seqable seqable?))
(s/instrument #'into)

; NO
(s/fdef cons
      :args (s/cat :dummy ::s/any :check-seqable seqable?))
(s/instrument #'cons)

; NO
; This doesn't differenciate two and three arity cases
;; (s/fdef reduce
;;         :args (s/cat :check-funtion ifn? :dummy (s/? ::s/any) :check-seqable seqable?))
; Doesn't work
;; (s/fdef reduce
;;   :args (s/and (s/or :two (s/cat :dummy ::s/any :dummy ::s/any)
;;                      :three (s/cat :dummy ::s/any :dummy ::s/any :dummy ::s/any))
;;                (s/or :three-args (s/cat :check-function ifn? :dummy ::s/any :check-seqable seqable?)
;;                      :two-args (s/cat :check-function ifn? :check-seqable seqable?))))
;; (s/fdef reduce
;;         :args (s/or
;;                 :three (s/cat :check-function ifn? :dummy ::s/any :check-seqable seqable?)
;;                 :two (s/cat :check-function ifn? :check-seqable seqable?)))

(defn length2?
  "returns true if a coll has two elements"
  [coll]
  (= (count coll) 2))
(defn length3?
  "returns true if a coll has three elements"
  [coll]
  (= (count coll) 3))

(s/def ::length-two length2?)
(s/def ::length-three length3?)

(s/fdef reduce
        :args (s/or :two-case (s/and ::length-two
                                   (s/cat :check-function ifn? :check-seqable seqable?))
                    :three-case (s/and ::length-three
                                     (s/cat :check-function ifn? :dummy ::s/any :check-seqable seqable?))))
(s/instrument #'reduce)

(s/instrument #'reduce)

; O - TODO: doesn't work unless the spec is after the overwritten function
;; (s/fdef nth
;;   :args (s/cat :check-seqable seqable? :check-number number? :dummy (s/? ::s/any)))
;; (s/instrument #'nth)

; NO
(s/fdef filter
  :args (s/cat :check-function ifn? :check-seqable seqable?))
(s/instrument #'filter)

; NO
(s/fdef mapcat
  :args (s/cat :check-function ifn? :check-seqable (s/+ seqable?)))
(s/instrument #'mapcat)

; NO
(s/fdef assoc
  :args (s/cat :check-map-or-vector (s/or :check-map (s/nilable map?) :check-vector vector?)
               :dummy ::s/any
               :dummies (s/+ ::s/any)))
(s/instrument #'assoc)

; NO
(s/fdef dissoc
  :args (s/cat :check-map (s/nilable map?) :dummies (s/* ::s/any)))
(s/instrument #'dissoc)

; NO
(s/fdef odd?
  :args (s/cat :check-integer integer?))
(s/instrument #'odd?)

; O
(s/fdef <
  :args (s/cat :check-number (s/+ number?)))
(s/instrument #'<)

; O - TODO: doesn't work. the same behavior as nth
;; (s/fdef quot
;;   :args (s/cat :check-number number? :check-number number?))
;; (s/instrument #'quot)

; NO
(s/fdef comp
  :args (s/cat :check-function (s/* ifn?)))
(s/instrument #'comp)

; NO
(s/fdef repeatedly
  :args (s/cat :check-number (s/? number?) :check-function ifn?))
(s/instrument #'repeatedly)

; NO
(s/fdef repeat
  :args (s/cat :check-number (s/? number?) :dummy ::s/any))
(s/instrument #'repeat)

; NO
(s/fdef distinct
  :args (s/cat :check-seqable seqable?))
(s/instrument #'distinct)
