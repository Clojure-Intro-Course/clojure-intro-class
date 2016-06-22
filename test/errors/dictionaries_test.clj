(ns errors.dictionaries_test
  (:require [errors.dictionaries :refer :all]
            [expectations :refer :all]))

;#########################################
;### Tests for supplementary functions ###
;### in errors.dictionaries            ###
;#########################################

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

(expect ["[" "]"] (delimeters [6 7]))
(expect ["{" "}"] (delimeters (assoc {} :a 1)))
(expect ["#{" "}"] (delimeters (set [2 3 2 3])))
(expect ["(" ")"] (delimeters (range)))

;;
(expect "2" (preview-arg 2 10 3))
(expect "\"hi\"" (preview-arg "hi" 10 3))
(expect "[2 3]" (preview-arg [2 3] 10 3))
(expect "(2 3)" (preview-arg '(2 3) 10 3))

(expect '(2 " " 3 "," " " 4 " " 5) (add-commas '(2 " " 3 " " 4 " " 5)))

(expect '(1 2 3 "," 4 5 6 7 "," 8 9 10 11) (add-commas '(1 2 3 4 5 6 7 8 9 10 11)))

(expect '(1 2 3 "," 4 5 6 7 "," 8 9 10 11 "," 12 13 14) (add-commas '(1 2 3 4 5 6 7 8 9 10 11 12 13 14)))
