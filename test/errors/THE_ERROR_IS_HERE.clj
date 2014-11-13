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
