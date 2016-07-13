 (ns corefns.corefns_test
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]
            [errors.messageobj :refer :all]
            [errors.exceptions :refer :all]
            [corefns.collection_fns :refer :all]
            [intro.core :refer :all]))

;#############################################
;### Testing the functionality of corefns  ###
;### These tests check that we didn't mess ###
;### up core functionality by overwriting  ###
;#############################################

;;testing for assoc

(expect {:b "cat", :a "dog"}
        (assoc nil :a "dog" :b "cat"))

;; testing for reduce

(expect 0
        (reduce + nil))

(expect 0
        (reduce + 0 nil))-

;; testing for empty?
(expect true
        (empty? '()))
(expect true
        (empty? nil))
(expect false
        (empty? '(1 2)))


;;; Elena, 6/22/16: Tests below this line should be moved from here

;#################################################
;### Testing if the corefns preconditions work ###
;#################################################

;; testing for the precondition of repeat

(expect "You cannot pass three arguments to a function repeat,\nin the function call (repeat 3 \"x\" 10)"
        (get-text-no-location
          (run-and-catch-pretty-no-stacktrace 'intro.student
                                              '(repeat 3 "x" 10))))

;; testing for more than two arguments in repeatedly
(expect "You cannot pass three arguments to a function repeatedly,\nin the function call (repeatedly 5 6 anonymous-function)"
        (get-text-no-location
          (run-and-catch-pretty-no-stacktrace 'intro.student
                                              '(repeatedly 5 6 #(+ % 2)))))

;; testing for correct pair syntax given to assoc
(expect "The arguments following the map or vector in assoc must come in pairs, but one of them does not have a match."
        (get-text-no-location
          (run-and-catch-pretty-no-stacktrace 'intro.student
                                              '(assoc [1 2 3] 0 5 2))))
;; testing for the preconditions on dissoc

(expect "You cannot pass zero arguments to a function dissoc, need at least one,\nin the function call (dissoc )"
        (get-text-no-location
          (run-and-catch-pretty-no-stacktrace 'intro.student
                                              '(dissoc ))))

;; testing zero arguments after the function given to map
(expect "You cannot pass one argument to a function map, need at least two,\nin the function call (map +)"
        (get-text-no-location
          (run-and-catch-pretty-no-stacktrace 'intro.student
                                              '(map + ))))
;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


(expect "Vectors added to a map must consist of two elements: a key and a value."
        (get-text-no-location
          (run-and-catch-pretty-no-stacktrace 'intro.student
                                              '(conj {1 2} [3 5] [7]))))

;; testing for the arguments less than two in reduce
(expect "You cannot pass zero arguments to a function reduce, need two or three,\nin the function call (reduce )"
        (get-text-no-location
          (run-and-catch-pretty-no-stacktrace 'intro.student
                                              '(reduce))))

(expect "You cannot pass one argument to a function reduce, need two or three,\nin the function call (reduce +)"
        (get-text-no-location
          (run-and-catch-pretty-no-stacktrace 'intro.student
                                              '(reduce + ))))

;; testing for the arguments more than three in reduce
(expect "You cannot pass four arguments to a function reduce, need two or three,\nin the function call (reduce + 0 [1 2 3] 4)"
        (get-text-no-location
          (run-and-catch-pretty-no-stacktrace 'intro.student
                                              '(reduce + 0 [1 2 3] 4))))

;; testing for function as first argument of reduce
(expect "In function reduce, the first argument 1 must be a function but is a number,\nin the function call (reduce 1 2)"
        (get-text-no-location
          (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(reduce 1 2))))

;; testing for zero arguments given to empty?
(expect "You cannot pass zero arguments to a function empty?, need one,\nin the function call (empty? )"
        (get-text-no-location
          (run-and-catch-pretty-no-stacktrace 'intro.student
                                              '(empty? ))))

;; testing for more than one argument given to empty?
(expect "You cannot pass two arguments to a function empty?, need one,\nin the function call (empty? () ())"
        (get-text-no-location
          (run-and-catch-pretty-no-stacktrace 'intro.student
                                              '(empty? () ()))))
