(ns compilation_errors.only_recur_from_tail)

(loop [x 10] (while (> x 1) (println x) (recur (- x 1))))
