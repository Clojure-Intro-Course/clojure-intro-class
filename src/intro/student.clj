(ns intro.student
	;(:use [corefns.corefns]
   ;     [seesaw.core])
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]))
            ;[strings.strings :refer :all]))

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
    ; possible hint: are you sure conj is the function you should be using?
    ; possible tag :conj
(defn exercise2 [str1 str2]
 (conj str1 str2))

;; DrRacket Exercise 3
(defn exercise3 [a-string index]
  (str (subs a-string 0 index) "_" (subs a-string index (+ 2 (count a-string)))))

;; 4clojure Problem 15 - exception in exceptions/4clojure-prob15-ClassCast.ser
    ; possible hint: make sure you're using prefix notation
(defn prob15 [num]
  (2 * num))

;; 4clojure Problem 16 - exception in exceptions/4clojure-prob16-Arity.ser
    ; possible tag :cons
    ; possible hint: are you sure cons is the function you should be using?
(defn prob16 [a-string]
  (cons "Hello, " a-string "!"))

;; 4clojure Problem 17 - exception in exceptions/4clojure-prob17-NullPointer.ser
    ; possible hint: make sure you have a base (termination) case
(defn prob17 [coll-of-nums]
  (loop [coll coll-of-nums
         result '()]
    (recur (rest coll)
           (conj result (+ 5 (first coll))))))

;; 4clojure Problem 18 - exception in exceptions/4clojure-prob18-AssertionError.ser
    ; possible hint: make sure that you have a base (termination) case
(defn prob18 [coll-of-nums]
  (if (< (first coll-of-nums) 5)
    (list (first coll-of-nums)))
  (prob18 (rest coll-of-nums)))

;; 4clojure Problem 64 - exception in exceptions/4clojure-prob64-NullPointer.ser
    ; possible hint: make sure you have a base (termination) case
(defn prob64 [coll-of-nums]
  (loop [coll coll-of-nums
         result 0]
    (recur (rest coll)
           (+ result (first coll)))))

;; 4clojure Problem 57 -

;(defn prob57 [num]
;  (do
;    (conj [] num)
;    (prob57 (dec num))))
