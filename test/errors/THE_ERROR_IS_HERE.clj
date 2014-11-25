(ns errors.THE_ERROR_IS_HERE
  (:require
   [corefns.corefns :refer :all]
   ))

;; (filter-stacktrace (:trace-elems (stacktrace/parse-exception (run-and-catch-raw '(a)))))
(defn a []
  (into {} [#{:x :m} #{:q :b}]))

;(:trace-elems (stacktrace/parse-exception (run-and-catch-raw '(b))))
(defn b []
  (int "banana"))

;(filter-stacktrace (:trace-elems (stacktrace/parse-exception (run-and-catch-raw '(c)))))
(defn c []
  (doall (map :not-a-function [1 2 3])))

(defn d []
  (hash-map "c" :d "d"))

(defn e []
  (into {} [[1 2] [3]]))

;calling the function 'f' breaks the compiler, I guess
(defn g []
  (cons 1 2))

;(defn h []
;  "stuff")

; let requires an even number of forms in binding vector in errors.THE_ERROR_IS_HERE:33
;(defn i []
;  (let [x] (+ x 2)))

; let requires a vector for its binding in errors.THE_ERROR_IS_HERE:37
;(defn j []
;   (let (x 2)))

(defn k []
  (contains? (seq [1 3 6]) 2))

; Parameter declaration my-argument should be a vector, compiling:(errors/THE_ERROR_IS_HERE.clj:44:3)
;(defn l []
;  (defn my-function my-argument))

;  when-let requires exactly 2 forms in binding vector in errors.THE_ERROR_IS_HERE:48, compiling:(errors/THE_ERROR_IS_HERE.clj:48:3)
;(defn m []
;  (when-let [num1 1 num2 2] "hello"))

(defn n []
  (throw (new IndexOutOfBoundsException "10")))

(defn o []
   (nth [0 1 2 3 4 5] 10))

(defn p []
   (even? 3 6 1))

(defn q []
   (throw (new NullPointerException "some message")))

(defn r []
   (int nil))

;(defn s []
;   (nth {:a 10 :z 4} 20))

; Unsupported binding form: 7, compiling:(errors/THE_ERROR_IS_HERE.clj:69:4)
;(defn t []
;   (let [x :two 7 :seven]))

;(defn u []
;  (defn s [s] (loop [s])))
