(ns intro.may2015
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]
            [corefns.collection_fns :refer :all]
            [intro.student :refer :all]))

;(= ((fn [x] (for [y x :when odd? y] y)) #{1 2 3 4 5}) '(1 3 5)

(def stuff (reduce #(+ % 1) [1 2 3]))

;(print stuff)

;(defn 4 5)


;(take 5)
