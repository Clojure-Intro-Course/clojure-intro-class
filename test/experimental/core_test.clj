(ns experimental.core_test
  (:require [clj-stacktrace.core :as stacktrace]
            [expectations :refer :all]
            [strings.beginner_string_library :refer :all]
            [errors.exceptions :as errors])
  (:import [java.io.FileInputStream]
           [java.io.ObjectInputStream]
           [java.io.FileOutputStream]
           [java.io.ObjectOutputStream]))

;;;; A space for prototypes, examples, and experimental features.
;; NEVER refer to this file in other files.

;************************************
(def ex1 (errors/run-and-catch-raw '(1)))

(def ex2 (errors/run-and-catch-raw '(+ 2 "pie")))

(def ex3 (errors/run-and-catch-raw '(5)))

(def ex4 (errors/run-and-catch-raw '(def w 4 5)))

(def ex5 (errors/run-and-catch-raw '(defn foo (q) q)))

(def ex6 (errors/run-and-catch-raw '(count 6)))

(def st1 (stacktrace/parse-exception ex3))

;(def fst1 (map #(str "\t" (:ns %) "/" (:fn %) " (" (:file %) " line " (:line %) ")")(filter #(and (:clojure %) (not (re-matches ignore-nses (:ns %)))) (:trace-elems st1))))

;########################################
;### Testing for errors with hashmaps ###
;########################################

;(every? #(= (count %) 2) coll)
;(every? #(= (count %) 2) [{:one 1 :two 2} {:three 3 :four 4}])
;(every? #(= (count %) 2) [{:one 1 :two 2 :three 3} {:three 3 :four 4}])
;(every? #(= (count %) 2) [{:one 1 :two 2} {:three 3 :four 4 :five 5}])
;(every? #(= (count %) 2) [{:one 1 :two 2} {:three 3 :four 4}, {:eight 8 :nine 9} {:ten 10 :eleven 11}])

;(every? #(= (count %) 2) {{:one 1 :two 2} {:three 3 :four 4}})
;(every? #(= (count %) 2) {:one 1 :two 2 :three 3 :four 4})
;(every? #(= (count %) 2) {1 3 2 3 3 3})

;(into {} [{:one 1 :two 2 :three 3} {:four 4 :five 5} :six 6 :seven 7 :eight 8 :nine 9])
;(into {} [{:one 1 :two 2} {:three 3 :four 4}, {:eight 8 :nine 9} {:ten 10 :eleven 11}])

;(every? odd? {1 1 3 3})
;(odd? [1 1])

;(defn all-elems-are-map-or-vector? [coll]
;  (every? #(or (vector? %) (map? %)) coll))

;(defn all-elems-have-length-two? [coll]
;  (every? #(= (count %) 2) coll))

;  (every? #(or (vector? %) (map? %)) '())

;    (every? #(= (count %) 2) '())

;(every? odd? [1 1])
;(every? odd? [3 3])

;##################################
;### Other testing in instarepl ###
;##################################

;(hash-map [1 2] [3 4] [5])
;(hash-map [1 2] [3 4] [5 6] [7 8])
;(hash-map 1 2 3)
;(hash-map "c" :d "d")
;(hash-map #{:a :b :c :d} #{:e :f :g :h})
;(hash-map [:a :b :c :d] '(:e :f :g :h))
;(hash-map 1 2 1 3)
;(hash-map 1 2 3 5 1)
;(hash-map 1 2 1 5 9)

;(into {} [[1 2 3 4] [3 5]])
;(into {} [{2 2} [3]])
;(into {} [[1 2] [1 3]])

;(array-map 1 2)
;(array-map [1 2] [3 4])
;(array-map #{1 2 4 5} #{10 11 12 13} #{1})

;(array-map '(1 2) '(1 4))
;(array-map 1 2 1 3)
;(apply array-map [:a 1 :b 2 :c])
;(apply array-map {1 2 2 3})
;(apply array-map [1 2 3 4])
;(apply array-map [:a 1 :c 2 :c 3])
;(apply array-map [:a 1 :c 2 :c])
;(apply array-map [5 7 5])
;(apply array-map [5 7 5 3 5 2 9 2 9 11 3 11 25])
;(array-map 1 2 3 5 1)
;(apply array-map [1 2 1 3])
;(array-map 1 2 1 3)

;(zipmap [1 2 3] [:a :b :c])
;(zipmap [1 2 3] {:a :b})
;(zipmap #{1 2 3} #{1 2 3})
;(zipmap '(1 2 3) '(1 2 3))
;(zipmap {1 2 3 4} { 5 6 7 8})
;(zipmap [1 2] [1 2 3])
;(zipmap [1 1 1 1] [2 90 6 4])
;(zipmap [1 2] {:a :b} [1 23] [1 2 5])
;(zipmap [1 23 3 5 7 98] {3 1 7 3 0 8})

;(frequencies [:a :b :a :a :b])
;(frequencies {:a :b :c :d :d :d :i :d})
;(frequencies 1)

;(group-by count ["d" "6i" "qicsg"])
;(group-by count '("d" "6i" "qicsg"))
;(group-by count #{"d" "6i" "qicsg"})
;(group-by count {"9" "d" "6i" "qicsg"})
;(group-by count {"9sd" "dsdf" "6sdfi" "qsdficsg"})
;(group-by #(odd? (first %)) {2 4 8 9 1 3})
;(group-by #(odd? (second %)) {2 4 8 9 1 3})
