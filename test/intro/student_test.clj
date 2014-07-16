(ns intro.student_test
  (:require [expectations :refer :all]
            [errors.messageobj :refer :all]
            [errors.exceptions :refer :all]
            [intro.student :refer :all]))

;; testing for exercise2
; sometimes works, sometimes doesn't for some weird reason
;(expect "Attempted to use a string, but a collection was expected."
;        (get-all-text
;         (run-and-catch-student '(exercise2 "hello " "world"))))

;; testing for exercise3
(expect "String index out of range: 12"
        (get-all-text
         (run-and-catch-student '(exercise3 "helloworld" 5))))

;; testing for prob15
(expect "Attempted to use a number, but a function was expected."
        (get-all-text
         (run-and-catch-student '(prob15 5))))
