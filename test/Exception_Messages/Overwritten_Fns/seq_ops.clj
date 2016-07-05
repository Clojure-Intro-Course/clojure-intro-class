(ns errors.error_dictionary_test
  (:use [errors.prettify_exception :only [line-number-format]])
  (:require [expectations :refer :all]
            [errors.messageobj :refer :all]
            [errors.exceptions :refer :all]
            [errors.prettify_exception :refer :all]
            [utilities.file_IO :refer :all]
            ))

;; testing for :illegal-argument-no-val-supplied-for-key
(expect "No value found for key d. Every key for a hash-map must be followed by a value."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(hash-map "c" :d "d"))))

;; testing for :illegal-argument-vector-arg-to-map-conj
(expect "Vectors added to a map must consist of two elements: a key and a value."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(into {} [[1 2] [3]]))))

(expect "Vectors added to a map must consist of two elements: a key and a value."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(merge {:a 1 :b 2} [1 2 3] [1 2] [1 5]))))

(expect "Vectors added to a map must consist of two elements: a key and a value."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(conj {:a 1 :b 2} [1 2 3 4]))))

;; testing for :illegal-argument-cannot-convert-type
(expect "In function cons, the second argument 2 must be a sequence but is a number."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(cons 1 2))))

(expect #"You cannot use / in this position."
       (get-text-no-location (:msg-info-obj (try (load-file "exceptions/compilation_errors/invalid_token_error1.clj")
                       (catch Throwable e (prettify-exception e))))))

;; testing for wrong number of args to a keyword
(expect #"A keyword: :a can only take one or two arguments."
       (get-text-no-location (:msg-info-obj (try (load-file "exceptions/compilation_errors/keyword_wrong_number_of_args.clj")
                       (catch Throwable e (prettify-exception e))))))

;; testing for greater than 20 arguments
(expect #"A function may not take more than more than 20 parameters."
       (get-text-no-location (:msg-info-obj (try (load-file "exceptions/compilation_errors/greater_than_20_parameters.clj")
                       (catch Throwable e (prettify-exception e))))))

(expect "Cannot call nil as a function."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(nil 5))))

(expect "In function empty?, the argument :s must be a sequence but is a keyword,\nin the function call (empty? :s)"
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(empty? :s))))

(expect "In function first, the argument :e must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(first :e))))

(expect "In function rest, the argument :a must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(rest :a))))

(expect "In function next, the argument :n must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(next :n))))

(expect "In function seq, the argument :w must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(seq :w))))

(expect "In function count, the argument :a must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(count :a))))

;; testing for :index-out-of-bounds-index-provided
(expect "An index in a sequence is out of bounds. The index is: 10."
        (get-text-no-location
          (run-and-catch-pretty-no-stacktrace 'intro.core '(throw (new IndexOutOfBoundsException "10")))))

;; testing for :null-pointer-non-existing-object-provided
(expect "An attempt to access a non-existing object: some message (NullPointerException)."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '(throw (new NullPointerException "some message")))))

;; testing for :null-pointer-non-existing-object-not-provided
(expect "An attempt to access a non-existing object (NullPointerException)."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '(int nil))))
