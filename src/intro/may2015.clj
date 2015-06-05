(ns intro.may2015
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]
            [corefns.collection_fns :refer :all]
            [intro.student :refer :all]))

;(+ 5 (filter odd? [2 3 4 5 6 3  33 3 3 3 3 33 3 3  3 3 3  3 3 3 3 3 3 ]) 5)

(+ 5 (map #(/ % 0) (range)))

;(+ 5 (map #(repeat %) (range)))

(zero?)

;(let [x] (+ x 2))

;(hash-map 1)

;{"a" 1 "a"}

;(map #(+ %1 %2) [1 2 3])

;(

;(= ((fn [x] (for [y x :when odd? y] y)) #{1 2 3 4 5}) '(1 3 5)

;(def stuff (reduce #(+ % 1) [1 2 3]))

;(print stuff)

;(defn 4 5)

;(+ 2 "banana")

;(reduce inc [1 2 3])

;(take 5)
