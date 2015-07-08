(ns compilation_errors.keyword_wrong_number_of_args2)

(let [f (if (= (+ 1 1) 2) (:a) +)]
  (f 1 2 3))
