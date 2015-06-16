(ns errors.dictionaries_test
  (:require [errors.dictionaries :refer :all]
            [expectations :refer :all]
            [corefns.corefns :refer :all]
            [errors.prettify_exception :refer :all]
            [errors.messageobj :refer :all]
            [errors.exceptions :refer :all]))

;#####################################
;### Testing for get-function-name ###
;#####################################

(expect "days" (get-function-name "happy$days"))
(expect "days" (get-function-name "happy/days"))
(expect "blahBlahBlahBlahNuthin'" (get-function-name "blahBlahBlahBlahNuthin'"))

;###############################################
;### Testing for check-if-anonymous-function ###
;###############################################

(expect "anonymous function" (check-if-anonymous-function "fn"))
(expect "anonymous function" (check-if-anonymous-function "fn_test"))
(expect "anonymous function" (check-if-anonymous-function "fn_"))
(expect "random_function" (check-if-anonymous-function "random_function"))

;######################################
;### Testing for location functions ###
;######################################

(expect {} (get-compile-error-location ""))

;(expect "(2 3 4 5)" (eval-first-n (map inc [1 2 3 4]) 10))
;(expect "(1 2 3 4 5 6 7 8 9 10...)" (eval-first-n (map inc (range)) 10))
;(expect "a sequence that we cannot evaluate" (eval-first-n (map #(/ % 0) [1 2 3]) 10))
