(ns errors.error_dictionary_test
  (:require [expectations :refer :all]
            [errors.messageobj :refer :all]
            [errors.exceptions :refer :all]
            [errors.prettify_exception :refer :all]
            [utilities.file_IO :refer :all]
            [intro.core :refer :all]
            ))

;#########################################
;### Testing for Class Cast Exceptions ###
;#########################################

;; testing for :class-cast-exception-cannot-cast-to-map-entry
(expect "Attempted to create a map using a keyword, but a sequence of vectors of length 2 or a sequence of maps is needed."
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'intro.core
          '(into {} [#{:x :m} #{:q :b}]))))

;; testing for :class-cast-exception
(expect "Attempted to use a string, but a character was expected."
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'intro.core
          '(int "banana"))))

;###############################################
;### Testing for Illegal Argument Exceptions ###
;###############################################

;; testing for :illegal-argument-no-val-supplied-for-key
(expect "No value found for key d. Every key must be paired with a value; the value should be immediately following the key."
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'intro.core '(hash-map "c" :d "d"))))

;; testing for :illegal-argument-vector-arg-to-map-conj
(expect "All the inner vectors in the outer collection must have length two."
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'intro.core '(into {} [[1 2] [3]]))))

;; testing for :illegal-argument-cannot-convert-type
(expect "In function cons, the second argument 2 must be a sequence but is a number."
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'intro.core '(cons 1 2))))

;; testing for :illegal-argument-even-number-of-forms


;; Elena: this is a compilation error in clojure 1.7, so we can't test it like this
;; testing for :illegal-argument-even-number-of-forms-in-binding-vector
;(expect #"Parameters for let must come in pairs, but one of them does not have a match; on line (.*) in the file intro.core"
         ;(run-and-catch-pretty-no-stacktrace 'intro.core '(let [x] (+ x 2)))))

;; Changed the test to load from a file. No exception, but the test still fails.
;; Need to look into this more: is it a compiler or run-time exception?
;; It seems to be a compiler exception thrown at run-time, not compilation time
;; testing for :illegal-argument-needs-vector-when-binding
(expect clojure.lang.Compiler$CompilerException; #"Parameters for let must come in pairs, but one of them does not have a match(.*)"
        (compile 'compilation_errors.let-odd-number-bindings));(prettify-exception e)))))

;; testing for :illegal-argument-type-not-supported
(expect "Function contains? does not allow a sequence as an argument"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'intro.core '(contains? (seq [1 3 6]) 2))))

;; Elena: this is a compilation error in clojure 1.7, so we can't test it like this
;; testing for :illegal-argument-parameters-must-be-in-vector
;(expect "Parameters in defn should be a vector, but is my-argument"
;        (get-all-text
;         (run-and-catch-pretty-no-stacktrace 'intro.core '(defn my-function my-argument))))

;; Elena: this is a compilation error in clojure 1.7, so we can't test it like this
;; testing for :illegal-argument-exactly-2-forms
;(expect #"The function when-let requires exactly 2 forms in binding vector. Line (.*) in the file intro.core"
;        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(when-let [num1 1 num2 2] "hello"))))

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

(expect "You cannot use the same key in a hash map twice, but you have duplicated the key :b."
        ;; note: let is needed in the test since the error must happen only at run time
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(let [a :b b :b] {a 1 b 2}))))


;##########################################
;### Testing for Null Pointer Exceptions###
;##########################################

;; testing for :null-pointer-non-existing-object-provided
(expect "An attempt to access a non-existing object: some message\n(NullPointerException)"
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(throw (new NullPointerException "some message")))))

;; testing for :null-pointer-non-existing-object-not-provided
(expect "An attempt to access a non-existing object. \n(NullPointerException)"
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(int nil))))

;###################################################
;### Testing for Unsupported Operation Exceptions###
;###################################################

;; testing for :unsupported-operation-wrong-type-of-argument
(expect "Function nth does not allow a map as an argument"
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(nth {:a 10 :z 4} 20))))

;##################################
;### Testing for Java Exceptions###
;##################################

;; Elena: this is a compilation error in clojure 1.7, so we can't test it like this
;; testing for :java.lang.Exception-improper-identifier
;(expect "You cannot use 7 as a variable."
;        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(let [x :two 7 :seven]))))

;######################################
;### Testing for compilation errors ###
;######################################

(expect "Compilation error: loop requires an even number of forms in binding vector, while compiling intro.core"
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(defn s [s] (loop [s])))))

(expect #"Compilation error: this recur is supposed to take 0 arguments, but you are passing 1, while compiling (.+)" ; this is giving NO_SOURCE_PATH
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(recur (inc 1)))))

(expect #"Compilation error: there is an unmatched parameter in declaration of cond, while compiling:(.+)" ; this is giving NO_SOURCE_PATH
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'intro.core '(defn my-num [x] (cond (= 1 x))))))

;; the internal representation of zero? is zero?--inliner--4238 (in this particular test), i.e. it has
;; an inliner part
(expect "Compilation error: wrong number of arguments (0) passed to a function zero?, while compiling "
        (get-all-text (butlast (run-and-catch-pretty-no-stacktrace 'intro.core '(zero?)))))

(expect #"Compilation error: recur can only occur as a tail call: no operations can be done after its return, while compiling (.+)" ; this is giving NO_SOURCE_PATH
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(defn inc-nums [x] ((recur (inc x)) (loop [x x]))))))

(expect #"Compilation error: def must be followed by a name, while compiling (.+)" ; this is giving NO_SOURCE_PATH
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(def 4 (+ 2 2)))))

(expect #"Compilation error: loop is a macro, cannot be passed to a function, while compiling (.+)" ; this is giving NO_SOURCE_PATH
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(defn my-happy [x] loop [x x]))))

(expect #"Compilation error: name banana is undefined, while compiling (.+)" ; this is giving NO_SOURCE_PATH
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(banana 5 6))))

(expect "Compilation error: there is an unmatched delimiter ), while compiling (compilation_errors/unmatched_delimiter.clj:3:20)"
        (get-all-text (:msg-info-obj (prettify-exception (import-from-file "exceptions/unmatched_delimiter.ser")))))

(expect #"Compilation error: too many arguments to def, while compiling (.+)" ; this is giving NO_SOURCE_PATH
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(def my-var 5 6))))

(expect #"Compilation error: too few arguments to def, while compiling (.+)" ; this is giving NO_SOURCE_PATH
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(def))))

(expect "Compilation error: end of file, starting at line 3, while compiling (compilation_errors/eof.clj:4:1).\nProbably a non-closing parenthesis or bracket."
        (get-all-text (:msg-info-obj (prettify-exception (import-from-file "exceptions/end_of_file.ser")))))

;; :compiler-exception-must-recur-to-function-or-loop
