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
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.core
          '(into {} [#{:x :m} #{:q :b}]))))

;; testing for :class-cast-exception
(expect "Attempted to use a string, but a character was expected."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.core
          '(int "banana"))))

;###############################################
;### Testing for Illegal Argument Exceptions ###
;###############################################

;; testing for :pretty-print-value
(expect "In function +, the second argument ((0 1 2...) (0 1 2...) (0 1 2...) (0 1 2...) (0 1 2...) (0 1 2...) (0 1 2...) (0 1 2...) (0 1 2...) (0 1 2...)...) must be a number but is a sequence."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.core '(+ 1 (repeat (range))))))

(expect "In function +, the second argument (((...) (...) (...)...) ((...) (...) (...)...) ((...) (...) (...)...) ((...) (...) (...)...) ((...) (...) (...)...) ((...) (...) (...)...) ((...) (...) (...)...) ((...) (...) (...)...) ((...) (...) (...)...) ((...) (...) (...)...)...) must be a number but is a sequence."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.core '(+ 1 (repeat (repeat (range)))))))

(expect "In function inc, the first argument ((0 1 2...) (1 (...) (...)...) (1 2 1...) (0 1 2...) (1 (...) (...)...) (1 2 1...) (0 1 2...) (1 (...) (...)...) (1 2 1...) (0 1 2...)...) must be a number but is a sequence."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.core '(inc (cycle [(range) [1 (repeat 1) (range) 4 5 6 7 8 9 245 4235 5423] (cycle [1 2])])))))

(expect "In function +, the second argument (1 2 3 4 5 6 76 (0 1 2...) 756 354...) must be a number but is a vector."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.core '(+ 1 [1 2 3 4 5 6 76 (range) 756 354 6 645]))))

;; testing for :illegal-argument-no-val-supplied-for-key
(expect "No value found for key d. Every key for a hash-map must be followed by a value."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.core '(hash-map "c" :d "d"))))

;; testing for :illegal-argument-vector-arg-to-map-conj
(expect "Each inner vector must be a pair: a key followed by a value."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.core '(into {} [[1 2] [3]]))))

;; testing for :illegal-argument-cannot-convert-type
(expect "In function cons, the second argument 2 must be a sequence but is a number."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.core '(cons 1 2))))

;; testing for :illegal-argument-even-number-of-forms
;; Elena: this is a compilation error in clojure 1.7, so we can't test it like this
;; testing for :illegal-argument-even-number-of-forms-in-binding-vector
;(expect #"Parameters for let must come in pairs, but one of them does not have a match; on line (.*) in the file intro.core"
         ;(run-and-catch-pretty-no-stacktrace 'intro.core '(let [x] (+ x 2)))))

;; testing for :illegal-argument-needs-vector-when-binding.
;; This is a compiler error as of clojue 1.7, need load-file to test.
;; Note: using "compile" is tricky to get to work because of classpath issues:
;; we don't want the error to happen before we run this test.
;; Need to check for the location as well: currently incomplete.
(expect #"When declaring a let, you need to pass it a vector of arguments.(.*)"
       (get-text-no-location (:msg-info-obj (try (load-file "exceptions/compilation_errors/let-odd-number-bindings.clj")
                       (catch Throwable e (prettify-exception e))))))

;; testing for :illegal-argument-type-not-supported
(expect "Function contains? does not allow a sequence as an argument."
        (get-text-no-location
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

(expect "Cannot call nil as a function."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.core '(nil 5))))

;; testing for odd? and even?
(expect "In function even?, the first argument must be an integer number but is nil."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.core '(even? nil))))

(expect "In function odd?, the first argument must be an integer number but is nil."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.core '(odd? nil))))

(expect "In function even?, the first argument ((0 1 2...) (0 1 2...)) must be an integer number but is a vector."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.core '(even? [(range) (range)]))))

;############################################
;### Testing for Illegal State Exceptions ###
;############################################

(expect "% must be either on its own or followed by a number or &."
       (get-text-no-location (:msg-info-obj (try (load-file "exceptions/compilation_errors/percent_followed_by_letter.clj")
                       (catch Throwable e (prettify-exception e))))))

;##################################################
;### Testing for Index Out of Bounds Exceptions ###
;##################################################

;; testing for :index-out-of-bounds-index-provided
(expect "An index in a sequence is out of bounds. The index is: 10."
        (get-text-no-location
          (run-and-catch-pretty-no-stacktrace 'intro.core '(throw (new IndexOutOfBoundsException "10")))))

;; testing for :index-out-of-bounds-index-not-provided
(expect "An index in a sequence is out of bounds or invalid."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.core '(nth [0 1 2 3 4 5] 10))))

;###################################
;### Testing for Arity Exceptions###
;###################################

;; testing for :arity-exception-wrong-number-of-arguments
(expect "You cannot pass three arguments to a function even?, need one."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '(even? 3 6 1))))

(expect "You cannot pass zero arguments to a function even?, need one."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '(even?))))

(expect "You cannot pass one argument to this anonymous function."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '(#(+ %1 %2) 1))))

(expect "You cannot pass four arguments to a function user-def-fcn."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '((defn user-def-fcn [x] (+ x 1))
    (user-def-fcn 1 2 3 4)))))

(expect "You cannot use the same key in a hash map twice, but you have duplicated the key :b."
        ;; note: let is needed in the test since the error must happen only at run time
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '(let [a :b b :b] {a 1 b 2}))))


;##########################################
;### Testing for Null Pointer Exceptions###
;##########################################

;; testing for :null-pointer-non-existing-object-provided
(expect "An attempt to access a non-existing object: some message (NullPointerException)."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '(throw (new NullPointerException "some message")))))

;; testing for :null-pointer-non-existing-object-not-provided
(expect "An attempt to access a non-existing object (NullPointerException)."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '(int nil))))

;###################################################
;### Testing for Unsupported Operation Exceptions###
;###################################################

;; testing for :unsupported-operation-wrong-type-of-argument
(expect "Function nth does not allow a map as an argument."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '(nth {:a 10 :z 4} 20))))

;###########################################
;### Testing for ClassNotFoundException ####
;###########################################

;; We use the same message as for identfier undefined, but have a hint that is
;; more specific to dynamic class loading
(expect (more-> "Name clojure.string.split is undefined." get-text-no-location
                 #"(.*)Found in (.*) on line (\d+) at character (\d+)\." get-all-text)
              (run-and-catch-pretty-no-stacktrace 'intro.core '(clojure.string.split "a b c" " ")))



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

;;;; Elena: we might want to revisit this one
(expect #"Parameters for loop must come in pairs, but one of them does not have a match; on line (\d+) in the file intro\.core"
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '(defn s [s] (loop [s])))))

(expect "This recur is supposed to take zero arguments, but you are passing one." ;  this is giving NO_SOURCE_PATH
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '(recur (inc 1)))))

(expect "This recur is supposed to take one argument, but you are passing two." ;
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '(loop [x 1] (recur (inc x) (dec x))))))

(expect "Parameters for cond must come in pairs, but one of them does not have a match." ; this is giving NO_SOURCE_PATH
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.core '(defn my-num [x] (cond (= 1 x))))))

;; the internal representation of zero? is zero?--inliner--4238 (in this particular test), i.e. it has
;; an inliner part
(expect "You cannot pass zero arguments to a function zero?."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '(zero?))))

(expect "Recur can only occur as a tail call: no operations can be done after its return."  ; this is giving NO_SOURCE_PATH
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '(defn inc-nums [x] ((recur (inc x)) (loop [x x]))))))

(expect #"def must be followed by a name." ; this is giving NO_SOURCE_PATH
        (get-text-no-location  (run-and-catch-pretty-no-stacktrace 'intro.core '(def 4 (+ 2 2)))))

(expect #"loop is a macro, cannot be passed to a function." ; this is giving NO_SOURCE_PATH
        (get-text-no-location  (run-and-catch-pretty-no-stacktrace 'intro.core '(defn my-happy [x] loop [x x]))))

(expect #"Name banana is undefined." ; this is giving NO_SOURCE_PATH
        (get-text-no-location  (run-and-catch-pretty-no-stacktrace 'intro.core '(banana 5 6))))

(expect "There is an unmatched delimiter ).\nFound in file unmatched_delimiter.clj on line 3 at character 20."
        (get-all-text  (:msg-info-obj (prettify-exception (import-from-file "exceptions/unmatched_delimiter.ser")))))

(expect "Too many arguments to def." ; this is giving NO_SOURCE_PATH
        (get-text-no-location  (run-and-catch-pretty-no-stacktrace 'intro.core '(def my-var 5 6))))

(expect "Too few arguments to def." ; this is giving NO_SOURCE_PATH
        (get-text-no-location  (run-and-catch-pretty-no-stacktrace 'intro.core '(def))))

(expect "End of file, starting at line 3.\nProbably a non-closing parenthesis or bracket.\nFound in file eof.clj on line 4 at character 1."
        (get-all-text (:msg-info-obj (prettify-exception (import-from-file "exceptions/end_of_file.ser")))))

(expect #"Name splt does not exist in the namespace clojure\.string\.\nFound(.+)"
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(clojure.string/splt "pattern" #"/"))))

(expect #"The namespace clojure does not exist or is not accessible in your program\.\n(.+)"
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(clojure/string/splt "pattern" #"/"))))

(expect "There is an unmatched delimiter )."
       (get-text-no-location (:msg-info-obj (try (load-file "exceptions/compilation_errors/unmatched_delim_re.clj")
                       (catch Throwable e (prettify-exception e))))))

(expect "An opened \" does not have a matching closing one."
       (get-text-no-location (:msg-info-obj (try (load-file "exceptions/compilation_errors/non_closing_string.clj")
                       (catch Throwable e (prettify-exception e))))))

;; :compiler-exception-must-recur-to-function-or-loop
