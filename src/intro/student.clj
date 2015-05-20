(ns intro.student
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]
            [corefns.collection_fns :refer :all]))

;##################################
;### Testing compilation errors ###
;##################################

;(def f [x] (+ x 2))
;(+ (* 2 3) 7

;(+ (* 2 3) 7)]

;(defn f[x] (+ x y))

;(defn f[x y]
;	(fn [x] (+ x y z)))

;(loop [x 1 y 2]
;	(if (= x 0) 1
;		(recur (dec x) 5)))


;#############################################################
;### 4clojure and beginner problems like students would do ###
;#############################################################

;; 4clojure Problem 44
(declare prob44)

(defn prob44-helper-positive [n coll]
  (let [result (add-last (rest coll) (first coll))]
    (prob44 (- n 1) result)))

(defn prob44-helper-negative [n coll]
  (let [result (add-first (butlast coll) (last coll))]
    (prob44 (+ n 1) result)))

(defn prob44 [n coll]
  (cond
   (= n 0) coll ;n equal to zero
   (> n 0) (prob44-helper-positive n coll) ;n is positive
   (< n 0) (prob44-helper-negative n coll))) ;n is negative

;; DrRacket Excercise 2 - exception in exceptions/DrRacket-Exercise2-ClassCast.ser
    ; ERROR: Attempted to use a string, but a collection was expected.
    ; possible hint: are you sure conj is the function you should be using?
    ; possible hint: conj cannot be used on strings
    ; possible tag :conj
(defn exercise2 [str1 str2]
 (conj str1 str2))

;; DrRacket Exercise 3 - exception in exception/DrRacket-Exercise3-IndexOutOfBounds.ser
   ; ERROR: String index out of range: 12
(defn exercise3 [a-string index]
  (str (subs a-string 0 index) "_" (subs a-string index (+ 2 (count a-string)))))

;; 4clojure Problem 15 - exception in exceptions/4clojure-prob15-ClassCast.ser\
    ; ERROR: Attempted to use a number, but a function was expected.
    ; possible hint: make sure you're using prefix notation
(defn prob15 [num]
  (2 * num))

;; 4clojure Problem 16 - exception in exceptions/4clojure-prob16-Arity.ser
    ; ERROR: Wrong number of arguments (3) passed to a function cons
    ; possible tag :cons
    ; possible hint: are you sure cons is the function you should be using?
(defn prob16 [a-string]
  (cons "Hello, " a-string "!"))

;; 4clojure Problem 17 - exception in exceptions/4clojure-prob17-NullPointer.ser
    ; ERROR: An attempt to access a non-existing object.
    ; (NullPointerException)
    ; possible hint: make sure you have a base (termination) case
        ; possible hint: make sure you're not applying an operation to nil
(defn prob17 [coll-of-nums]
  (loop [coll coll-of-nums
         result '()]
    (recur (rest coll)
           (conj result (+ 5 (first coll))))))

;; 4clojure Problem 18 - exception in exceptions/4clojure-prob18-AssertionError.ser
    ; ERROR: in function < first argument nil must be a number but is nil
    ; possible hint: make sure that you have a base (termination) case
(defn prob18 [coll-of-nums]
  (if (< (first coll-of-nums) 5)
    (list (first coll-of-nums)))
  (prob18 (rest coll-of-nums)))

;; 4clojure Problem 64 - exception in exceptions/4clojure-prob64-NullPointer.ser -- from saved exception, not from recent running
    ; ERROR: An attempt to access a non-existing object.
    ; (NullPointerException)
    ; possible hint: make sure you have a base (termination) case
    ; possible hint: make sure you're not applying an operation to nil
(defn prob64 [coll-of-nums]
  (loop [coll coll-of-nums
         result 0]
    (recur (rest coll)
           (+ result (first coll)))))

;; 4clojure Problem 57 - exception in exceptions/4clojure-prob57-ArityException.ser
    ; ERROR: Wrong number of arguments (1) passed to a function take-while
    ; possible hint: make sure you're passing the right number of args to your functions
(defn prob57 [num]
  (take-while (range num)))

;; 4clojure Problem 134 - exception in exceptions/4clojure-prob134-AssertionError.ser
    ; ERROR: in function filter first argument :a must be a function but is a keyword
    ; possible hint: keywords can't be used as functions in this context
(defn prob134 [key map]
  (nil? (filter key map)))

;(defn prob145 [num]
;  (for [x (range num)]
;    (when (= 1 (rem num 4))
;    x)))

;(defn prob145 [num]
;  (filter (fn [] (= 1 (rem 4 %))) (range num)))

;; 4clojure Problem 156 - exception in exceptions/4clojure-prob156-AssertionError.ser
    ; ERROR: in function map first argument {:a 0} must be a function but is a map
    ; possible hint: be sure that your arguments are of the correct types
(defn prob156 [default keys]
  (into {} (map (into {}
                      {(first keys) default})
                keys)))

;; 4clojure Problem 20 - exception in exceptions/4clojure-prob20-AssertionError.ser
    ; ERROR: in function nth first argument 3 must be a sequence but is a number
    ; possible hint: be sure that your arguments are in the correct order
(defn prob20 [coll]
  (nth (- (count coll) 1) coll))

;; 4clojure Problem 21 - exception in exceptions/4clojure-prob21-ArityException.ser
    ; ERROR: Wrong number of arguments (1) passed to a function take
    ; possible hint: make sure you're using the right number of arguments.
(defn prob21 [coll n]
  (drop (first coll) (take n)))

;; Something Emma made, trying to add numbers - exception in exceptions/add-five-IllegalArgException.ser
    ; ERROR: Don't know how to create a sequence from a number
    ; possible hint: cons is supposed to be used for collections
(defn add-five [n]
  (cons n 5))

;; 4clojure Problem 24 - exception in exceptions/4clojure-prob24-ClassCast.ser
    ; ERROR: Attempted to use a sequence, but a number was expected.
    ; possible hint: be sure you are using the correct argument types
(defn prob24 [coll]
  (do
    (+ (first coll) (rest coll))
    (prob24 (rest rest coll))))

;; 4clojure Problem 23 - exception in exceptions/4clojure-prob23-IndexOutOfBounds.ser
    ; ERROR: An index in a sequence is out of bounds or invalid
    ; possible hint: make sure that the sequence you're using has the desired index
(defn prob23 [coll]
  (loop [old-coll coll
         new-coll []]
    (if (empty? old-coll)
      new-coll
      (recur (rest old-coll) (cons (nth old-coll (count old-coll)) new-coll)))))

;; 4clojure Problem 27 - exception in exceptions/4clojure-prob27-ArityException.ser
    ; ERROR: Wrong number of arguments (0) passed to a function equal-to?
    ; possible hint: make sure that you're passing the correct amount of parameters
(defn equal-to? [thing1 thing2]
  (= (first thing1) (first thing2)))

(defn prob27 [thing]
  (loop [thing thing
         reverse-thing (reverse thing)]
    (if (empty? thing)
      true
      (if (equal-to?)
        (recur (rest thing) (rest reverse-thing))
        false))))

;; Dr Racket exercise 9 - exception in exceptions/DrRacket-Exercise9-ArityException.ser
    ; ERROR: Wrong number of arguments (1) passed to a function fn--6065
    ; possible hint: make sure you're arguments to your functions are correct
(defn exercise9 []
  (let [b1 true
        b2 false]
    (or (#(false?) b1)
        (#(true?) b2))))

;; 4clojure Problem 38 - exception in exceptions/4clojure-prob38-ArityException.ser
    ; ERROR: Wrong number of arguments (4) passed to a function prob38
    ; possible hint: if you want a function to take an unlimited number of arguments, use an & in the
    ; parameter vector like this: [& args]
(defn prob38 [args]
  (if (= 1 (count args))
    args
    (prob38 (if (> (first args) (second args))
              (prob38 (cons (first args) (rest (rest args))))
              (prob38 (rest args))))))

(defn error-in-anonymous []
  (doall
   (map
    #(+ % 2)
    [2 3 "hi" "bye"])))

(defn error-in-map-inc []
  (doall
   (map
    inc
    [2 3 "hi" "bye"])))
(def not-your-usual-name 5)
