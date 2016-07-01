(ns errors.error_dictionary_test
  (:use [errors.prettify_exception :only [line-number-format]])
  (:require [expectations :refer :all]
            [errors.messageobj :refer :all]
            [errors.exceptions :refer :all]
            [errors.prettify_exception :refer :all]
            [utilities.file_IO :refer :all]
            ))

(expect "In function +, the first argument [1 2] must be a number but is a vector,\nin the function call (+ [1 2])"
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(+ [1 2]))))

(expect "In function +, the first argument [+] must be a number but is a vector,\nin the function call (+ [+])"
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(+ [+]))))

(expect "In function +, the first argument [map] must be a number but is a vector,\nin the function call (+ [map])"
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(+ [map]))))

(expect "In function +, the first argument (\"a\" \"b\") must be a number but is a list,\nin the function call (+ (\"a\" \"b\"))"
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(+ '("a" "b")))))

(expect "In function +, the first argument #{anonymous-function} must be a number but is a set,\nin the function call (+ #{anonymous-function})"
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(+ #{#(+ % 2)}))))

(expect "In function +, the first argument #{+} must be a number but is a set,\nin the function call (+ #{+})"
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(+ #{+}))))

(expect "In function +, the first argument {:a +} must be a number but is a map,\nin the function call (+ {:a +})"
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(+ {:a +}))))

(expect "In function +, the first argument {:a {6 \"a\", 7 true}, :b 4, :c +} must be a number but is a map,\nin the function call (+ {:a {6 \"a\", 7 true}, :b 4, :c +})"
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(+ {:a {6 "a" 7 true} :b 4 :c +}))))

;; Note: the order of elements in a hashmap is not guaranteed, so the actual elements amy be different
(expect "In function +, the first argument {:a 6, :k \"a\", :b 4, :c +, :l 6, :m 7} must be a number but is a map,\nin the function call (+ {:a 6, :k \"a\", :b 4, :c +, :l 6, :m 7})"
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(+ {:a 6 :k "a" :b 4 :c + :l 6 :m 7}))))

;; testing for invalid number exception
(expect #"Invalid number: 1.2.2."
       (get-text-no-location (:msg-info-obj (try (load-file "exceptions/compilation_errors/invalid_number.clj")
                       (catch Throwable e (prettify-exception e))))))

;; testing for :illegal-argument-type-not-supported
(expect "Function contains? does not allow a sequence as an argument."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(contains? (seq [1 3 6]) 2))))

