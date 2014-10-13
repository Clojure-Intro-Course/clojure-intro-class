(ns errors.error_dictionary_test
  (:require [expectations :refer :all]
            [errors.messageobj :refer :all]
            [errors.exceptions :refer :all]))

;#########################################
;### Testing for Class Cast Exceptions ###
;#########################################

;; testing for :class-cast-exception-cannot-cast-to-map-entry
(expect "This is a test."
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'intro.core
          '(into {} [#{:x :m} #{:q :b}]))))

;; testing for :class-cast-exception
(expect "in function + second argument :two must be a number but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'intro.core
          '(+ 1 :two))))

;###############################################
;### Testing for Illegal Argument Exceptions ###
;###############################################

;; testing for :illegal-argument-no-val-supplied-for-key
(expect "This is a test."
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'intro.core '(hash-map "c" :d "d"))))

;; testing for :illegal-argument-vector-arg-to-map-conj
(expect "This is a test."
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'intro.core '(into {} [[1 2] [3]]))))

;; testing for :illegal-argument-cannot-convert-type
(expect "Don't know how to create a sequence from a number"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'intro.core '(cons 1 2))))

;; testing for :illegal-argument-even-number-of-forms

;; testing for :illegal-argument-even-number-of-forms-in-binding-vector
(expect #"A parameter for a let is missing a binding on line (.*) in the file (.*)"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'intro.core '(let [x] (+ x 2)))))

;; testing for :illegal-argument-needs-vector-when-binding
(expect #"When declaring a (.*), you need to pass it a vector of arguments. Line (.*) in the file (.*)"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'intro.core '(let (x 2)))))

;; testing for :illegal-argument-type-not-supported
(expect "Function contains? does not allow a sequence as an argument"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'intro.core '(contains? (seq [1 3 6]) 2))))

;; testing for :illegal-argument-parameters-must-be-in-vector
(expect "Parameters in defn should be a vector, but is my-argument"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'intro.core '(defn my-function my-argument))))

;; testing for :illegal-argument-exactly-2-forms
(expect "This is a test."
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(when-let [num1 1 num2 2] "hello"))))

;##################################################
;### Testing for Index Out of Bounds Exceptions ###
;##################################################

;; testing for :index-out-of-bounds-index-provided
(expect "An index in a sequence is out of bounds. The index is: 10"
        (get-all-text
          (run-and-catch-pretty-no-stacktrace 'intro.core '(throw (new IndexOutOfBoundsException "10")))))

;; testing for :index-out-of-bounds-index-not-provided
(expect "An index in a sequence is out of bounds or invalid"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'intro.core '(nth [0 1 2 3 4 5] 10))))

;###################################
;### Testing for Arity Exceptions###
;###################################

;; testing for :arity-exception-wrong-number-of-arguments
(expect "Wrong number of arguments (3) passed to a function even?"
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(even? 3 6 1))))

(expect "Wrong number of arguments (0) passed to a function odd?"
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(odd?))))

;##########################################
;### Testing for Null Pointer Exceptions###
;##########################################

;; testing for :null-pointer-non-existing-object-provided
(expect "An attempt to access a non-existing object: some message\n(NullPointerException)"
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(throw (new NullPointerException "some message")))))

;; testing for :null-pointer-non-existing-object-not-provided
(expect "in function + first argument nil must be a number but is nil"
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(+ nil 2))))

;###################################################
;### Testing for Unsupported Operation Exceptions###
;###################################################

;; testing for :unsupported-operation-wrong-type-of-argument
(expect "Function nth does not allow a map as an argument"
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(nth {:a 10 :z 4} 20))))

;##################################
;### Testing for Java Exceptions###
;##################################

;; testing for :java.lang.Exception-improper-identifier
(expect "You cannot use 7 as a variable."
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(let [x :two 7 :seven]))))

;######################################
;### Testing for compilation errors ###
;######################################

(expect #"Compilation error: loop requires an even number of forms in binding vector, while compiling (.+)"
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(defn s [s] (loop [s])))))

(expect #"Compilation error: this recur is supposed to take 0 arguments, but you are passing 1, while compiling (.+)"
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(recur (inc 1)))))

(expect #"Compilation error: there is an unmatched parameter in declaration of cond, while compiling:(.+)"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'intro.core '(defn my-num [x] (cond (= 1 x))))))

(expect "Compilation error: wrong number of arguments (0) passed to a function zero?, while compiling "
        (get-all-text (butlast (run-and-catch-pretty-no-stacktrace 'intro.core '(zero?)))))

(expect #"Compilation error: recur can only occur as a tail call, meaning no operations can be done after its return, while compiling (.+)"
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(defn inc-nums [x] ((recur (inc x)) (loop [x x]))))))

(expect #"Compilation error: def must be followed by a name, while compiling (.+)"
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(def 4 (+ 2 2)))))

(expect #"Compilation error: loop is a macro, cannot be passed to a function, while compiling (.+)"
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(defn my-happy [x] loop [x x]))))

(expect #"Compilation error: name banana is undefined, while compiling (.+)"
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(banana 5 6))))

;; thinks that the unmatched delimiter is part of the expect test not the test itself
(expect #"Compilation error: there is an unmatched delimiter ), while compiling (.+)"
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(import-from-file "unmatched_delimiter.ser")))))

(expect #"Compilation error: too many arguments to def, while compiling (.+)"
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(def my-var 5 6))))

(expect #"Compilation error: too few arguments to def, while compiling (.+)"
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(def))))

;; thinks that the EOF is in our expect test instead of the test itself
;(expect #"Compilation error: end of file, starting at line (.+), while compiling (.+)"
;         "Probably a non-closing parentheses or bracket."
;        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(+ 1 2 )))

;; :compiler-exception-must-recur-to-function-or-loop
