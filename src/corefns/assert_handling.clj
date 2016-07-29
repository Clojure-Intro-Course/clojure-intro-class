(ns corefns.assert_handling
;; seqable? is in core since Clojure veresion 1.9
;;   (:use [clojure.core.incubator])
  (:require [corefns.failed_asserts_info :refer :all]))


;;; Functions to check pre-conditions and record the offenders for
;;; error processing.


(defn check-if-seqable?
  "returns true if x is seqable and false otherwise, sets data
  in seen-failed-asserts. If the second argument is present, it's added
  to the seen-failed-asserts as the number of the argument"
  [fname x & [n]]
  (if (seqable? x) true
    (do (add-to-seen {:check "a sequence"
                      :class (class x)
                      :value x
                      :fname fname})
      (if n (add-to-seen {:arg-num n}))
      false)))
    ;  (cond
    ;   (number? n) (add-to-seen {:arg-num n})
    ;   (string? n) (add-to-seen {:number-of-args (read-string n)}))
    ;  (if n
    ;    (if (number? n) (add-to-seen {:arg-num n})
    ;      (if (string? n) (add-to-seen {:number-of-args (read-string n)})))
    ;  (if q (add-to-seen {:number-of-args (read-string n)}))

;this is currently unused
(defn check-if-sequential?
  "returns true if x is sequential and false otherwise, sets data
  in seen-failed-asserts. If the second argument is present, it's added
  to the seen-failed-asserts as the number of the argument"
  [fname x & [n]]
  (if (sequential? x) true
    (do (add-to-seen {:check "a sequence"
                      :class (class x)
                      :value x
                      :fname fname})
      (if n (add-to-seen {:arg-num n}))
      false)))

;i just added this, needs tests, used in assoc pre-conditions
(defn check-if-map-or-vector? [fname x & [n]]
  (if (or (vector? x) (map? x) (nil? x)) true
    (do (add-to-seen {:check "a map or vector"
                      :class (class x)
                      :value x
                      :fname fname})
      (if n (add-to-seen {:arg-num n}))
      false)))

(defn check-if-map? [fname x & [n]]
  (if (or (map? x) (nil? x)) true
    (do (add-to-seen {:check "a map"
                      :class (class x)
                      :value x
                      :fname fname})
      (if n (add-to-seen {:arg-num n}))
      false)))

(defn check-if-function? [fname x & [n]]
  (if (ifn? x) true
    (do (add-to-seen {:check "a function"
                      :class (class x)
                      :value x
                      :fname fname})
      (if n (add-to-seen {:arg-num n}))
      false)))

(defn check-if-number? [fname x & [n]]
  (if (number? x) true
    (do (add-to-seen {:check "a number"
                      :class (class x)
                      :value x
                      :fname fname})
      (if n (add-to-seen {:arg-num n}))
      false)))

(defn check-if-integer? [fname x & [n]]
  (if (integer? x) true
    (do (add-to-seen {:check "an integer number"
                      :class (class x)
                      :value x
                      :fname fname})
      (if n (add-to-seen {:arg-num n}))
      false)))

(defn check-if-string? [fname x & [n]]
  (if (string? x) true
    (do (add-to-seen {:check "a string"
                      :class (class x)
                      :value x
                      :fname fname})
      (if n (add-to-seen {:arg-num n}))
      false)))

(defn check-if-character? [fname x & [n]]
  (if (char? x) true
    (do (add-to-seen {:check "a character"
                      :class (class x)
                      :value x
                      :fname fname})
      (if n (add-to-seen {:arg-num n}))
      false)))

(defn check-if-var? [fname x & [n]]
  (if (var? x) true
    (do (add-to-seen {:check "a var"
                      :class (class x)
                      :value x
                      :fname fname})
      (if n (add-to-seen (:arg-num n)))
      false)))

(defn check-if-string-or-nil? [fname x & [n]]
  (if (or (instance? CharSequence x) (nil? x)) true
    (do (add-to-seen {:check "a string or nil"
                      :class (class x)
                      :value x
                      :fname fname})
      (if n (add-to-seen {:arg-num n}))
      false)))

(defn check-if-regex? [fname x & [n]]
  (if (= (class x) java.util.regex.Pattern) true
    (do (add-to-seen {:check "a regular expression"
                      :class (class x)
                      :value x
                      :fname fname})
      (if n (add-to-seen {:arg-num n}))
      false)))

(defn check-if-string-or-character? [fname x & [n]] ;for string-contains?
  (if (or (string? x) (char? x)) true
    (do (add-to-seen {:check "either a string or character"
                      :class (class x)
                      :value x
                      :fname fname})
      (if n (add-to-seen {:arg-num n}))
      false)))

;; should pass the starting arg number: it's different for different functions
(defn check-if-seqables? [fname arguments start]
  (loop [args arguments n start]
    (if (empty? args)
      true
      (if (not (check-if-seqable? fname (first args) n))
      	false
        (recur (rest args) (inc n))))))

;; should pass the starting arg number: it's different for different functions
(defn check-if-numbers? [fname arguments start]
  (loop [args arguments n start]
    (if (empty? args)
      true
      (if (not (check-if-number? fname (first args) n))
        false
        (recur (rest args) (inc n))))))

;; should pass the starting arg number: it's different for different functions
(defn check-if-strings? [fname arguments start]
  (loop [args arguments n start]
    (if (empty? args)
      true
      (if (not (check-if-string? fname (first args) n))
        false
        (recur (rest args) (inc n))))))

;; allows for us to not say nth arg when there is only one arg
(defn only-arg [arg] (if arg arg (when-not arg (add-to-seen {:only-arg true}) arg)))

;; should pass the starting arg number: it's different for different functions (not sure if true for this one)
(defn check-if-functions? [fname arguments start]
  (loop [args arguments n start]
    (if (empty? args)
      true
      (if (not (check-if-function? fname (first args) n))
        false
        (recur (rest args) (inc n))))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;(defn all-elems-are-map-or-vector? [coll]
;  (every? #(or (vector? %) (map? %)) coll))
;
;(defn all-elems-have-length-two? [coll]
;  (every? #(= (count %) 2) coll)) ;with hashmaps, this function will ALWAYS return true because it breaks up hashmaps into
;                                  ;vectors of 2, so it will only return false if the hashmap is not a valid hashmap, since
;                                  ;hashmaps always are built up of pairs of 2
;
;(defn all-elems-are-map-or-vector-with-length-2? [fname coll & [n]]
;  (if (and (all-elems-are-map-or-vector? coll)
;           (all-elems-have-length-two? coll))
;      true ;return true
;      (do (add-to-seen {:check "either a hashmap, or a collection of vectors or hashmaps of length 2,"
;                        :class (class coll) ;else add-to seen
;                        :value coll
;                        :fname fname})
;        (if n (add-to-seen {:arg-num n}))
;        false))) ;and return false
;
;(defn check-if-can-convert-to-hashmap [fname arg1 arg2 & [n]]
;  (if (map? arg1) ;is arg1 a hashmap?
;    (if (map? arg2) ;if so, is arg2 a hashmap?
;      true ;if both args are hashmaps, return true
;      (all-elems-are-map-or-vector-with-length-2? fname arg2 n)) ;if arg1 is a hashmap, but not arg2, do all-elems-are-map-or-vector-with-length-2?
;    true)) ;if arg1 is NOT a hashamp, return true
