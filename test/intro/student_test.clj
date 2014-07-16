(ns intro.student_test
  (:require [expectations :refer :all]
            [errors.messageobj :refer :all]
            [errors.exceptions :refer :all]
            [errors.stacktrace_functions :refer :all]
            [intro.student :refer :all]))

;; testing for exercise2
; sometimes works, sometimes doesn't for some weird reason
;(expect "Attempted to use a string, but a collection was expected."
;        (get-all-text
;         (run-and-catch-pretty-no-stacktrace 'intro.student
;                                             '(exercise2 "hello " "world"))))

;; testing for exercise3


;:filtered-stacktrace ({:anon-fn false, :fn "subs", :ns "clojure.core", :clojure true, :file "core.clj", :line 4591} {:anon-fn false, :fn "exercise3", :ns "intro.student", :clojure true, :file "student.clj", :line 57} {:anon-fn false, :fn "eval12993", :ns "intro.student", :clojure true, :file "NO_SOURCE_FILE", :line 23} {:anon-fn false, :fn "eval", :ns "clojure.core", :clojure true, :file "core.clj", :line 2927} {:anon-fn false, :fn "run-and-catch-raw", :ns "errors.exceptions", :clojure true, :file "exceptions.clj", :line 125} {:anon-fn false, :fn "run-and-catch-pretty-with-stacktrace", :ns "errors.exceptions", :clojure true, :file "exceptions.clj", :line 147} {:anon-fn true, :fn "G", :ns "intro.student-test", :clojure true, :file "student_test.clj", :line 22} {:anon-fn false, :fn "G", :ns "intro.student-test", :clojure true, :file "student_test.clj", :line 17} {:anon-fn true, :fn "test-var", :ns "expectations", :clojure true, :file "expectations.clj", :line 239} {:anon-fn false, :fn "test-var", :ns "expectations", :clojure true, :file "expectations.clj", :line 235} {:anon-fn true, :fn "test-vars", :ns "expectations", :clojure true, :file "expectations.clj", :line 280} {:anon-fn false, :fn "create-context", :ns "expectations", :clojure true, :file "expectations.clj", :line 259} {:anon-fn false, :fn "test-vars", :ns "expectations", :clojure true, :file "expectations.clj", :line 280} {:anon-fn false, :fn "run-tests", :ns "expectations", :clojure true, :file "expectations.clj", :line 313} {:anon-fn false, :fn "run-all-tests", :ns "expectations", :clojure true, :file "expectations.clj", :line 317} {:anon-fn false, :fn "eval", :ns "clojure.core", :clojure true, :file "core.clj", :line 2927})

(expect (more-of x
                 java.lang.StringIndexOutOfBoundsException (:exception-class x)
                 "String index out of range: 12" (get-all-text (:msg-info-obj x))
                 (trace-has-all-pairs? {:fn "subs" :ns "clojure.core"}) (:stacktrace x)
                 (trace-doesnt-have-pair? :ns "expectations") (:filtered-stacktrace x)
                ; "" (:filtered-stacktrace x)
                 )
        (run-and-catch-pretty-with-stacktrace 'intro.student
                                              '(exercise3 "helloworld" 5)))

(expect "String index out of range: 12"
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.student
                                              '(exercise3 "helloworld" 5))))

;; testing for prob15
(expect "Attempted to use a number, but a function was expected."
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(prob15 5))))

;; testing for prob16
(expect "Wrong number of arguments (3) passed to a function cons"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(prob16 "Dave"))))

;; testing for prob17
;(expect "An attempt to access a non-existing object.
;        (NullPointerException)"
;        (get-all-text
;         (run-and-catch-pretty-no-stacktrace 'intro.student
;                                             '(prob17 '(1 2 3)))))
