(ns intro.student_test
  (:require [expectations :refer :all]
            [errors.messageobj :refer :all]
            [errors.exceptions :refer :all]
            [errors.stacktrace_functions :refer :all]
            [intro.student :refer :all]))

;; testing for exercise2
;; we'll try to fix this one by fixing conj if we have time
;(expect (more-of x
;                 java.lang.ClassCastException (:exception-class x)
;                 "Attempted to use a string, but a collection was expected." (get-all-text (:msg-info-obj x))
;                 (trace-has-all-pairs? {:fn "exercise2" :ns "intro.student"}) (:stacktrace x))
;        (run-and-catch-pretty-with-stacktrace 'intro.student
;                                              '(exercise2 "hello " "world")))

;; testing for exercise3
(expect (more-of x
                 java.lang.StringIndexOutOfBoundsException (:exception-class x)
                 "String index out of range: 12" (get-all-text (:msg-info-obj x))
                 (trace-has-all-pairs? {:fn "subs" :ns "clojure.core"}) (:stacktrace x)
                 (trace-doesnt-have-pair? :ns "expectations") (:filtered-stacktrace x))
        (run-and-catch-pretty-with-stacktrace 'intro.student
                                              '(exercise3 "helloworld" 5)))

;; testing for prob15
(expect (more-of x
                 java.lang.ClassCastException (:exception-class x)
                 "Attempted to use a number, but a function was expected." (get-all-text (:msg-info-obj x))
                 (trace-has-all-pairs? {:fn "prob15" :ns "intro.student"}) (:stacktrace x))
        (run-and-catch-pretty-with-stacktrace 'intro.student
                                              '(prob15 5)))

;; testing for prob16
(expect (more-of x
                 clojure.lang.ArityException (:exception-class x)
                 "Wrong number of arguments (3) passed to a function cons" (get-all-text (:msg-info-obj x))
                 (trace-has-all-pairs? {:fn "prob16" :ns "intro.student"}) (:stacktrace x))
        (run-and-catch-pretty-with-stacktrace 'intro.student
                                              '(prob16 "Dave")))

;; testing for prob17
(expect (more-of x
                 java.lang.AssertionError (:exception-class x)
                 "in function + second argument nil must be a number but is nil" (get-all-text (:msg-info-obj x))
                 (trace-has-all-pairs? {:fn "prob17" :ns "intro.student"}) (:stacktrace x))
        (run-and-catch-pretty-with-stacktrace 'intro.student
                                              '(prob17 '(1 2 3))))

;; testing for prob18
(expect (more-of x
                 java.lang.AssertionError (:exception-class x)
                 "in function < first argument nil must be a number but is nil" (get-all-text (:msg-info-obj x))
                 (trace-has-all-pairs? {:fn "prob18" :ns "intro.student"}) (:stacktrace x))
        (run-and-catch-pretty-with-stacktrace 'intro.student
                                              '(prob18 '(3 4 5 6 7))))

;; testing for prob64
(expect (more-of x
                 java.lang.AssertionError (:exception-class x)
                 "in function + second argument nil must be a number but is nil" (get-all-text (:msg-info-obj x))
                 (trace-has-all-pairs? {:fn "prob64" :ns "intro.student"}) (:stacktrace x))
        (run-and-catch-pretty-with-stacktrace 'intro.student
                                              '(prob64 [1 2 3 4 5])))

;; testing for prob57
(expect (more-of x
                 clojure.lang.ArityException (:exception-class x)
                 "Wrong number of arguments (1) passed to a function take-while" (get-all-text (:msg-info-obj x))
                 (trace-has-all-pairs? {:fn "prob57" :ns "intro.student"}) (:stacktrace x))
        (run-and-catch-pretty-with-stacktrace 'intro.student
                                              '(prob57 5)))

;; testing for error-in-anonymous
;(expect (more-of x
;                 ;clojure.lang.ArityException (:exception-class x)
;                 ;"Wrong number of arguments (1) passed to a function take-while" (get-all-text (:msg-info-obj x))
;                 ;(trace-has-all-pairs? {:fn "prob57" :ns "intro.student"}) (:stacktrace x)
;                 ; 2nd elem should be anony in error-in-anonymous
;                 "" (:filtered-stacktrace x))
;        (run-and-catch-pretty-with-stacktrace 'intro.student
;                                              '(error-in-anonymous)))

;(expect (more-of x
;                 ;clojure.lang.ArityException (:exception-class x)
;                 ;"Wrong number of arguments (1) passed to a function take-while" (get-all-text (:msg-info-obj x))
;                 ;(trace-has-all-pairs? {:fn "prob57" :ns "intro.student"}) (:stacktrace x)
;                 ; 2nd elem should be anony in error-in-anonymous
;                 "" (:filtered-stacktrace x))
;        (run-and-catch-pretty-with-stacktrace 'intro.student
;                                              '(error-in-map-inc)))
