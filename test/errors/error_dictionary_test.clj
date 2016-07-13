(ns errors.error_dictionary_test
  (:use [errors.prettify_exception :only [line-number-format]])
  (:require [expectations :refer :all]
            [errors.messageobj :refer :all]
            [errors.exceptions :refer :all]
            [errors.prettify_exception :refer :all]
            [utilities.file_IO :refer :all]
            ))




;##################################
;### Testing for Java Exceptions###
;##################################

;; Elena: this is a compilation error in clojure 1.7, so we can't test it like this
;; testing for :java.lang.Exception-improper-identifier
;(expect "You cannot use 7 as a variable."
;        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(let [x :two 7 :seven]))))





;; :compiler-exception-must-recur-to-function-or-loop
