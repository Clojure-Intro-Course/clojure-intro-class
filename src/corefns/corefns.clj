(ns corefns.corefns
  (:use ;[clojure.core.incubator]
        [corefns.specs])
;;   (:refer-clojure :exclude [map nth])
  (:require [corefns.assert_handling :refer :all]
            [corefns.failed_asserts_info :refer :all]
            [clojure.spec :as s]
            [clojure.spec.test :as stest]))


;;; Overwritten clojure.core functions to get meaningful error data


;; Including the standard Clojure documentation to make sure that asserts
;; and cases are consistent with the standard Clojure.

;; count, into, conj, nth, drop, take, concat, filter, reduce
;; Maps and the like: key, val, keys, vals - careful with pre-conds for key!
;; odd?, even?, etc - check for numbers!



;; ######### Spec error generator ##########
(defn ex-info-process
  "Takes a function name, a sequence of arguments, and a spec predicate.
  Throws ExceptionInfo if the argument sequence doesn't satisfy the spec"
  [f-name args pred]
  (try (s/assert pred args)
    (catch clojure.lang.ExceptionInfo e
      (throw
        (ex-info "Spec assertion failed"
                 (conj (ex-data e) [:f-name f-name] [:args args]))))))



;; ######### Predicate definitions #########

;; Basic building blocks
(s/def ::check-num number?)
(s/def ::check-int integer?)
(s/def ::check-seq seqable?)
(s/def ::check-fn ifn?)
;; (s/def ::check-coll coll?)

;; Spec predicates
(s/def ::check-one-int
  (s/cat ::arg ::check-int))
(s/def ::check-one-seq
  (s/cat ::arg ::check-seq))
(s/def ::check-any-seq
  (s/cat ::first-arg any? ::second-arg ::check-seq))
(s/def ::check-fn-seq
  (s/cat ::first-arg ::check-fn ::second-arg ::check-seq))
(s/def ::check-fn-seqs
  (s/cat ::first-arg ::check-fn ::second-args (s/+ ::check-seq)))
(s/def ::check-fn-any-seq
  (s/cat ::first-arg ::check-fn ::second-arg any? ::third-arg ::check-seq))
(s/def ::check-seq-int
  (s/cat ::first-arg ::check-seq ::second-arg ::check-int))
(s/def ::check-seq-int-any
  (s/cat ::first-arg ::check-seq ::second-arg ::check-int ::third-arg any?))
(s/def ::check-seq-seq
  (s/cat ::first-arg ::check-seq ::second-arg ::check-seq))
(s/def ::check-seq-anys
  (s/cat ::first-arg ::check-seq ::second-args (s/+ any?)))
(s/def ::check-nums
  (s/cat ::arg-list (s/* ::check-num)))



;; ######### Overwritten core functions ###########

;; (empty? coll)
;; Returns true if coll has no items.
;; (defn empty? [argument1]
;;   {:pre [(only-arg (check-if-seqable? "empty?" argument1))]}
;;   (clojure.core/empty? argument1))
(defn empty? [coll]
  (do
    (ex-info-process "empty?" [coll] ::check-one-seq)
    (clojure.core/empty? coll)))

;;(first coll)
;;Returns the first item in the collection
;;Calls seq on its argument. If coll is nil, returns nil.
(defn first [coll]
  (do
    (ex-info-process "first" [coll] ::check-one-seq)
    (clojure.core/first coll)))

;;(rest coll)
;;Returns a possibly empty seq of the items after the first.
;; Calls seq on its argument.
(defn rest [coll]
  (do
    (ex-info-process "rest" [coll] ::check-one-seq)
    (clojure.core/rest coll)))

;;(next coll)
;:Returns a seq of the items after the first.
;;Calls seq on its argument.
;;If there are no more items, returns nil.
(defn next [coll]
  (do
    (ex-info-process "next" [coll] ::check-one-seq)
    (clojure.core/next coll)))

;;(seq coll)
;;Returns a seq on the collection. If the collection is empty, returns nil.
;;(seq nil) returns nil. seq also works on Strings, native Java arrays
;;(of reference types) and any objects that implement Iterable. Note that seqs
;;cache values, thus seq should not be used on any Iterable whose iterator
;;repeatedly returns the same mutable object.
(defn seq [coll]
  (do
    (ex-info-process "seq" [coll] ::check-one-seq)
    (clojure.core/seq coll)))

;; As of clojure 1.7 allows (map f)
;; (map f coll)
;; (map f c1 c2)
;; (map f c1 c2 c3)
;; (map f c1 c2 c3 & colls)
;; Returns a lazy sequence consisting of the result of applying f to the
;; set of first items of each coll, followed by applying f to the set
;; of second items in each coll, until any one of the colls is
;; exhausted. Any remaining items in other colls are ignored. Function
;; f should accept number-of-colls arguments.
(defn map [f coll & colls]
  (do
    (ex-info-process "map" [f coll & colls] ::check-fn-seqs)
    (apply clojure.core/map f coll colls)))

;; (count coll)
;; Returns the number of items in the collection. (count nil) returns
;; 0. Also works on strings, arrays, and Java Collections and Maps
(defn count [coll]
  (do
    (ex-info-process "count" [coll] ::check-one-seq)
    (clojure.core/count coll)))

;; (conj coll x)
;; (conj coll x & xs)
;; conj[oin]. Returns a new collection with the xs
;;'added'. (conj nil item) returns (item). The 'addition' may
;; happen at different 'places' depending on the concrete type.
;; (defn conj [argument1 argument2 & args]
;;   {:pre [(check-if-seqable? "conj" argument1)]}
;;   (apply clojure.core/conj argument1 argument2 args))
(defn conj [coll x & xs]
  (do
    (ex-info-process "conj" [coll x xs] ::check-seq-anys)
    (apply clojure.core/conj coll x xs)))

;; (into to from)
;; Returns a new coll consisting of to-coll with all of the items of
;; from-coll conjoined.
(defn into [to from]
  (do
    (ex-info-process "into" [to from] ::check-seq-seq)
    (clojure.core/into to from)))
;; commented out the transducer case for now Elena June 1st 2016
  ;([argument1 argument2 argument3]
  ; {:pre [(check-if-seqable? "into" argument1)
  ;        (check-if-function? "into" argument2)
  ;        (check-if-seqable? "into" argument3)]}
  ; (clojure.core/into argument1 argument2 argument3)))

;; (cons x seq)
;; Returns a new seq where x is the first element and seq is
;;  the rest.
;; (defn cons [argument1 argument2]
;;   {:pre [(check-if-seqable? "cons" argument2)]}
;;   (clojure.core/cons argument1 argument2))


;; (reduce f coll)
;; (reduce f val coll)
;; f should be a function of :not-a-function2 arguments. If val is not supplied,
;; returns the result of applying f to the first 2 items in coll, then
;; applying f to that result and the 3rd item, etc. If coll contains no
;; items, f must accept no arguments as well, and reduce returns the
;; result of calling f with no arguments. If coll has only 1 item, it
;; is returned and f is not called. If val is supplied, returns the
;; result of applying f to val and the first item in coll, then
;; applying f to that result and the 2nd item, etc. If coll contains no
;; items, returns val and f is not called.
(defn reduce
  ([f coll]
   (do
     (ex-info-process "reduce" [f coll] ::check-fn-seq)
     (clojure.core/reduce f coll)))
  ([f val coll]
   (do
     (ex-info-process "reduce" [f val coll] ::check-fn-any-seq)
     (clojure.core/reduce f val coll))))

;; (nth coll index)
;; (nth coll index not-found)
;; Returns the value at the index. get returns nil if index out of
;; bounds, nth throws an exception unless not-found is supplied. nth
;; also works for strings, Java arrays, regex Matchers and Lists, and,
;; in O(n) time, for sequences.
;; (defn nth ;; there may be an optional 3rd arg
;;   ([argument1 argument2]
;;    {:pre [(check-if-seqable? "nth" argument1)
;;           (check-if-number? "nth" argument2)]}
;;    (clojure.core/nth argument1 argument2))
;;   ([argument1 argument2 argument3]
;;    {:pre [(check-if-seqable? "nth" argument1)
;;           (check-if-number? "nth" argument2)]}
;;    (clojure.core/nth argument1 argument2 argument3)))
(defn nth
  ([coll index]
   (do
     (ex-info-process "nth" [coll index] ::check-seq-int)
     (clojure.core/nth coll index)))
  ([coll index not-found]
   (do
     (ex-info-process "nth" [coll index not-found] ::check-seq-int-any)
     (clojure.core/nth coll index not-found))))


;; As of clojure 1.7 allows (filter f)
;; (filter pred coll)
;; Returns a lazy sequence of the items in coll for which
;; (pred item) returns true. pred must be free of side-effects.
;; (defn filter
;;  ; ([argument1]
;;   ; {:pre [(check-if-function? "filter" argument1)]}
;;    ;(clojure.core/filter argument1))
;;   ([argument1 argument2]
;;    {:pre [(check-if-function? "filter" argument1)
;;          (check-if-seqable? "filter" argument2)]}
;;   (clojure.core/filter argument1 argument2)))

;; (mapcat f & colls)
;; Returns the result of applying concat to the result of applying map
;; to f and colls. Thus function f should return a collection.
;; (defn mapcat [argument1 & args]
;;   {:pre [(check-if-function? "mapcat" argument1)
;;          (check-if-seqables? "mapcat" args 2)]}
;;   (apply clojure.core/mapcat argument1 args))

;; (assoc map key val)(assoc map key val & kvs)
;; assoc[iate]. When applied to a map, returns a new map of the
;; same (hashed/sorted) type, that contains the mapping of key(s) to
;; val(s). When applied to a vector, returns a new vector that
;; contains val at index. Note - index must be <= (count vector).
;; (defn assoc
;;   [mymap & key-values];check-if-seqable? is not restrictive enough
;;   {:pre [(check-if-map-or-vector? "assoc" mymap 1)]}
;;   (apply clojure.core/assoc (into [mymap] key-values)))

;; (dissoc map)(dissoc map key)(dissoc map key & ks)
;; dissoc[iate]. Returns a new map of the same (hashed/sorted) type,
;; that does not contain a mapping for key(s).
;; (defn dissoc
;;   ([mymap];check-if-seqable? is not restrictive enough
;;    {:pre [(only-arg (check-if-map? "dissoc" mymap 1))]}
;;    (clojure.core/dissoc mymap))
;;   ([mymap & keys]
;;    {:pre [(check-if-map? "dissoc" mymap 1)]}
;;    (apply clojure.core/dissoc (into [mymap] keys))))

;; (concat)
;; (concat x)
;; (concat x y)
;; (concat x y & zs)
;; Returns a lazy seq representing the concatenation of the elements in the supplied colls.
(defn concat [& args]
  {:pre [(check-if-seqables? "concat" args 1)]}
  (apply clojure.core/concat args))

;; (drop n coll)
;; Returns a lazy sequence of all but the first n items in coll.
(defn drop [argument1 argument2]
  {:pre [(check-if-number? "drop" argument1)
         (check-if-seqable? "drop" argument2)]}
  (clojure.core/drop argument1 argument2))

;; (take n coll)
;; Returns a lazy sequence of the first n items in coll, or all items if there are fewer than n.
(defn take [argument1 argument2]
  {:pre [(check-if-number? "take" argument1)
         (check-if-seqable? "take" argument2)]}
  (clojure.core/take argument1 argument2))

;;(odd? n)
;;Returns true if n is odd, throws an exception if n is not an integer
;; (defn odd? [n]
;;   {:pre [(only-arg (check-if-integer? "odd?" n 1))]}
;;   (clojure.core/odd? n))
(defn odd? [x]
  (do
    (ex-info-process "odd?" [x] ::check-one-int)
    (clojure.core/odd? x)))

;;(even? n)
;;Returns true if n is even, throws an exception if n is not an integer
;; (defn even? [n]
;;   {:pre [(only-arg (check-if-integer? "even?" n 1))]}
;;   (clojure.core/even? n))

;;    (< x)
;;    (< x y)
;;    (< x y & more)
;; Returns non-nil if nums are in monotonically increasing order,
;; otherwise false.
;; (defn < [argument1 & args]
;;    {:pre [(check-if-number? "<" argument1)
;;           (check-if-numbers? "<" args 2)]}
;;    (apply clojure.core/< argument1 args))
(defn < [argument1 & args]
   (apply clojure.core/< argument1 args))


;;    (<= x)
;;    (<= x y)
;;    (<= x y & more)
;; Returns non-nil if nums are in monotonically non-decreasing order,
;; otherwise false.
(defn <= [argument1 & args]
  {:pre [(check-if-number? "<=" argument1)
         (check-if-numbers? "<=" args 2)]}
  (apply clojure.core/<= argument1 args))

;;    (> x)
;;    (> x y)
;;    (> x y & more)
;; Returns non-nil if nums are in monotonically decreasing order,
;; otherwise false.
(defn > [argument1 & args]
   {:pre [(check-if-number? ">" argument1)
          (check-if-numbers? ">" args 2)]}
   (apply clojure.core/> argument1 args))

;;    (>= x)
;;    (>= x y)
;;    (>= x y & more)
;; Returns non-nil if nums are in monotonically non-increasing order,
;; otherwise false.
(defn >= [argument1 & args]
   {:pre [(check-if-number? ">=" argument1)
          (check-if-numbers? ">=" args 2)]}
   (apply clojure.core/>= argument1 args))

;;    (+)
;;    (+ x)
;;    (+ x y)
;;    (+ x y & more)
;; Returns the sum of nums. (+) returns 0. Does not auto-promote longs, will
;; throw on overflow.
;; (defn + [& args]
;;   {:pre [(check-if-numbers? "+" args 1)]}
;;   (apply clojure.core/+ args))
(defn + [& args]
  (apply clojure.core/+ args))


;;    (- x)
;;    (- x y)
;;    (- x y & more)
;; If no ys are supplied, returns the negation of x, else subtracts
;; the ys from x and returns the result.
;; (defn - [argument1 & args]
;;   {:pre [(check-if-number? "-" argument1)
;;          (check-if-numbers? "-" args 2)]}
;;   (apply clojure.core/- argument1 args))
(defn - [argument1 & args]
  (apply clojure.core/- argument1 args))


;;    (*)
;;    (* x)
;;    (* x y)
;;    (* x y & more)
;; Returns the product of nums. (*) returns 1.
(defn * [& args]
  {:pre [(check-if-numbers? "*" args 1)]}
  (apply clojure.core/* args))

;;    (/ x)
;;    (/ x y)
;;    (/ x y & more)
;; If no denominators are supplied, returns 1/numerator,
;; else returns numerator divided by all of the denominators.
(defn / [argument1 & args]
  {:pre [(check-if-number? "/" argument1)
         (check-if-numbers? "/" args 2)]}
  (apply clojure.core// argument1 args))

;;    (quot num div)
;; quot[ient] of dividing numerator by denominator.
;; (defn quot [argument1 argument2]
;;   {:pre [(check-if-number? "quot" argument1)
;;          (check-if-number? "quot" argument2)]}
;;   (clojure.core/quot argument1 argument2))
;;    (quot num div)
;; quot[ient] of dividing numerator by denominator.
(defn quot [argument1 argument2]
  (clojure.core/quot argument1 argument2))

(s/fdef corefns.corefns/quot
  :args (s/and ::corefns.specs/length-two
               (s/cat :check-number number? :check-number number?)))
(stest/instrument 'corefns.corefns/quot)

;;    (rem num div)
;; remainder of dividing numerator by denominator.
(defn rem [argument1 argument2]
  {:pre [(check-if-number? "rem" argument1)
         (check-if-number? "rem" argument2)]}
  (clojure.core/rem argument1 argument2))

;;    (mod num div)
;; Modulus of num and div. Truncates toward negative infinity.
(defn mod [argument1 argument2]
  {:pre [(check-if-number? "mod" argument1)
         (check-if-number? "mod" argument2)]}
  (clojure.core/mod argument1 argument2))

;;    (inc x)
;; Returns a number one greater than num.
(defn inc [x]
  {:pre [(only-arg (check-if-number? "inc" x 1))]}
  (clojure.core/inc x))

;;    (dec x)
;; Returns a number one less than num.
(defn dec [x]
  {:pre [(only-arg (check-if-number? "dec" x 1))]}
  (clojure.core/dec x))

;;    (max x)
;;    (max x y)
;;    (max x y & more)
;; Returns the greatest of the nums.
(defn max [argument1 & args]
  {:pre [(check-if-number? "max" argument1)
         (check-if-numbers? "max" args 2)]}
  (apply clojure.core/max argument1 args))

;;    (min x)
;;    (min x y)
;;    (min x y & more)
;; Returns the least of the nums.
(defn min [argument1 & args]
  {:pre [(check-if-number? "min" argument1)
         (check-if-numbers? "min" args 2)]}
  (apply clojure.core/min argument1 args))

;;    (comp) --> This is an identity function
;;    (comp f)
;;    (comp f g)
;;    (comp f g & fs)
;; Takes a set of functions and returns a fn that is the composition
;; of those fns.  The returned fn takes a variable number of args,
;; applies the rightmost of fns to the args, the next
;; fn (right-to-left) to the result, etc.
;; (defn comp [& args]
;;   {:pre [(check-if-functions? "comp" args 1)]}
;;   (apply clojure.core/comp args))


;;    (repeatedly f)
;;    (repeatedly n f)
;; Takes a function of no args, presumably with side effects, and
;; returns an infinite (or length n if supplied) lazy sequence of calls
;; to it
;; (defn repeatedly
;;   ([argument1]
;;    {:pre [(only-arg (check-if-function? "repeatedly" argument1))]}
;;    (clojure.core/repeatedly argument1))
;;   ([argument1 argument2]
;;    {:pre [(check-if-number? "repeatedly" argument1)
;;           (check-if-function? "repeatedly" argument2)]}
;;    (clojure.core/repeatedly argument1 argument2)))

;;    (repeat f)
;;    (repeat n f)
;; Returns a lazy (infinite!, or length n if supplied) sequence of xs.
;; (defn repeat
;;   ([argument1](defn distinct [argument1]
;;   {:pre [(check-if-seqable? "distinct" argument1)]}
;;   (clojure.core/distinct argument1))
;;    (clojure.core/repeat argument1))
;;   ([argument1 argument2]
;;    {:pre [(check-if-number? "repeat" argument1)]}
;;    (clojure.core/repeat argument1 argument2)))

;;    (distinct)
;;    (distinct coll)
;; Returns a lazy sequence of the elements of coll with duplicates removed.
;; Returns a stateful transducer when no collection is provided.
;; (defn distinct [argument1]
;;   {:pre [(check-if-seqable? "distinct" argument1)]}
;;   (clojure.core/distinct argument1))


;; Turn on all the assert checks
(s/check-asserts true)

;; This hashmap is used to get function names because 'speced' functions are stored differently
(def corefns-map {(str nth) "nth", (str quot) "quot"})

