(ns errors.THE_ERROR_IS_HERE)

;; (filter-stacktrace (:trace-elems (stacktrace/parse-exception (run-and-catch-raw '(a)))))
(defn a []
  (into {} [#{:x :m} #{:q :b}]))

;(:trace-elems (stacktrace/parse-exception (run-and-catch-raw '(b))))
(defn b []
  (int "banana"))
