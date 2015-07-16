(ns quil.q_checks
	(:use [quil.core]
        [inflections.core])
  (:require [errors.errorgui :refer :all]
            [errors.prettify_exception :refer :all]))

(defn not-nil? [x]
  (not (nil? x)))

(defn check-if-not-nil? [arguments]
  (loop [args arguments
         n 1]
    (if (empty? args)
      {:boolean true
       :arg-num -1}
      (if (nil? (first args))
      	{:boolean false
         :arg-num n}
        (recur (rest args) (inc n))))))


