(ns intro.student_test
  (:require [expectations :refer :all]
            [errors.messageobj :refer :all]
            [errors.exceptions :refer :all]
            [utilities.stacktrace_functions :refer :all]
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
                 "In function +, the second argument nil must be a number but is nil." (get-all-text (:msg-info-obj x))
                 (trace-has-all-pairs? {:fn "prob17" :ns "intro.student"}) (:stacktrace x))
        (run-and-catch-pretty-with-stacktrace 'intro.student
                                              '(prob17 '(1 2 3))))

;; testing for prob18
(expect (more-of x
                 java.lang.AssertionError (:exception-class x)
                 "In function <, the first argument nil must be a number but is nil." (get-all-text (:msg-info-obj x))
                 (trace-has-all-pairs? {:fn "prob18" :ns "intro.student"}) (:stacktrace x))
        (run-and-catch-pretty-with-stacktrace 'intro.student
                                              '(prob18 '(3 4 5 6 7))))

;; testing for prob64
(expect (more-of x
                 java.lang.AssertionError (:exception-class x)
                 "In function +, the second argument nil must be a number but is nil." (get-all-text (:msg-info-obj x))
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

;; testing for prob134
(expect (more-of x
                 java.lang.AssertionError (:exception-class x)
                 "In function filter, the first argument :a must be a function but is a keyword." (get-all-text (:msg-info-obj x))
                 (trace-has-all-pairs? {:fn "prob134" :ns "intro.student"}) (:filtered-stacktrace x))
        (run-and-catch-pretty-with-stacktrace 'intro.student
                                              '(prob134 :a {:a nil :b 2})))

;; testing for prob156
(expect (more-of x
                 java.lang.AssertionError (:exception-class x)
                 "In function map, the first argument {:a 0} must be a function but is a map." (get-all-text (:msg-info-obj x))
                 (trace-has-all-pairs? {:fn "prob156" :ns "intro.student"}) (:filtered-stacktrace x))
        (run-and-catch-pretty-with-stacktrace 'intro.student
                                              '(prob156 0 [:a :b :c])))

;; testing for prob20
(expect (more-of x
                 java.lang.AssertionError (:exception-class x)
                 "In function nth, the first argument 3 must be a sequence but is a number." (get-all-text (:msg-info-obj x))
                 (trace-has-all-pairs? {:fn "prob20" :ns "intro.student"}) (:filtered-stacktrace x))
        (run-and-catch-pretty-with-stacktrace 'intro.student
                                              '(prob20 [:a :b :c :d])))

;; testing for prob21
(expect (more-of x
                 clojure.lang.ArityException (:exception-class x)
                 "Wrong number of arguments (1) passed to a function take" (get-all-text (:msg-info-obj x))
                 (trace-has-all-pairs? {:fn "prob21" :ns "intro.student"}) (:filtered-stacktrace x))
        (run-and-catch-pretty-with-stacktrace 'intro.student
                                              '(prob21 '(4 5 6 7) 2)))

;; testing for add-five
(expect (more-of x
                 java.lang.IllegalArgumentException (:exception-class x)
                 "Don't know how to create a sequence from a number" (get-all-text (:msg-info-obj x))
                 (trace-has-all-pairs? {:fn "add-five" :ns "intro.student"}) (:filtered-stacktrace x))
        (run-and-catch-pretty-with-stacktrace 'intro.student
                                              '(add-five 5)))

;; testing for prob24
(expect (more-of x
                 java.lang.AssertionError (:exception-class x)
                 "In function +, the second argument (2 3) must be a number but is a sequence." (get-all-text (:msg-info-obj x))
                 (trace-has-all-pairs? {:fn "prob24" :ns "intro.student"}) (:filtered-stacktrace x))
        (run-and-catch-pretty-with-stacktrace 'intro.student
                                              '(prob24 [1 2 3])))

;; testing for prob23
(expect (more-of x
                 java.lang.IndexOutOfBoundsException (:exception-class x)
                 "An index in a sequence is out of bounds or invalid" (get-all-text (:msg-info-obj x))
                 (trace-has-all-pairs? {:fn "prob23" :ns "intro.student"}) (:filtered-stacktrace x))
        (run-and-catch-pretty-with-stacktrace 'intro.student
                                              '(prob23 [5 4 3 2 1])))

;; testing for prob27
(expect (more-of x
                 clojure.lang.ArityException (:exception-class x)
                 "Wrong number of arguments (0) passed to a function equal-to?" (get-all-text (:msg-info-obj x))
                 (trace-has-all-pairs? {:fn "prob27" :ns "intro.student"}) (:filtered-stacktrace x))
        (run-and-catch-pretty-with-stacktrace 'intro.student
                                              '(prob27 '(\r \a \c \e \c \a \r))))


;; testing for exercise9
(expect (more-of x
                 clojure.lang.ArityException (:exception-class x)
                 #"Wrong number of arguments \(1\) passed to an anonymous function" (get-all-text (:msg-info-obj x))
                 (trace-has-all-pairs? {:fn "exercise9" :ns "intro.student"}) (:filtered-stacktrace x))
        (run-and-catch-pretty-with-stacktrace 'intro.student
                                              '(exercise9)))

;; testing for prob38
(expect (more-of x
                 clojure.lang.ArityException (:exception-class x)
                 "Wrong number of arguments (4) passed to a function prob38" (get-all-text (:msg-info-obj x)))
        (run-and-catch-pretty-with-stacktrace 'intro.student
                                              '(prob38 1 8 3 4)))

;; testing for error-in-anonymous
(expect (more-of x
                 java.lang.AssertionError (:exception-class x)
                 "In function +, the first argument \"hi\" must be a number but is a string." (get-all-text (:msg-info-obj x))
                 (trace-has-all-pairs? {:fn "error-in-anonymous" :anon-fn true :ns "intro.student"}) (:filtered-stacktrace x)
                 (trace-has-all-pairs? {:fn "error-in-anonymous" :anon-fn false :ns "intro.student"}) (:filtered-stacktrace x))
        (run-and-catch-pretty-with-stacktrace 'intro.student
                                              '(error-in-anonymous)))

;; testing for error-in-map-inc
(expect (more-of x
                 java.lang.AssertionError (:exception-class x)
                 "In function inc, the first argument \"hi\" must be a number but is a string." (get-all-text (:msg-info-obj x))
                 (trace-has-all-pairs? {:fn "error-in-map-inc" :anon-fn false :ns "intro.student"}) (:filtered-stacktrace x))
        (run-and-catch-pretty-with-stacktrace 'intro.student
                                              '(error-in-map-inc)))
