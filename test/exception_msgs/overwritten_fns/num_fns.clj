(ns exception_msgs.overwritten_fns.num_fns
  (:use [errors.prettify_exception :only [line-number-format]])
  (:require [expectations :refer :all]
            [errors.messageobj :refer :all]
            [errors.exceptions :refer :all]
            [errors.prettify_exception :refer :all]
            [utilities.file_IO :refer :all]
            ))


;; Since spec evaluates the arguments, the tests below are no longer passing

;; testing for :pretty-print-value
;(expect "In function +, the second argument ((0 1 2 3...) (0 1 2 3...) (0 1 2 3...) (0 1 2 3...) (0 1 2 3...) (0 1 2 3...) (0 1 2 3...) (0 1 2 3...) (0 1 2 3...) (0 1 2 3...)...) must be a number but is a sequence."
;        (get-text-no-location
;         (run-and-catch-pretty-no-stacktrace 'intro.student '(+ 1 (repeat (range))))))

;(expect "In function +, the second argument (((...) (...) (...) (...)...) ((...) (...) (...) (...)...) ((...) (...) (...) (...)...) ((...) (...) (...) (...)...) ((...) (...) (...) (...)...) ((...) (...) (...) (...)...) ((...) (...) (...) (...)...) ((...) (...) (...) (...)...) ((...) (...) (...) (...)...) ((...) (...) (...) (...)...)...) must be a number but is a sequence."
;        (get-text-no-location
;         (run-and-catch-pretty-no-stacktrace 'intro.student '(+ 1 (repeat (repeat (range)))))))
;
;(expect "In function inc, the argument ((0 1 2 3...) [1 (...) (...) 4...] (1 2 1 2...) (0 1 2 3...) [1 (...) (...) 4...] (1 2 1 2...) (0 1 2 3...) [1 (...) (...) 4...] (1 2 1 2...) (0 1 2 3...)...) must be a number but is a sequence."
;        (get-text-no-location
;         (run-and-catch-pretty-no-stacktrace 'intro.student '(inc (cycle [(range) [1 (repeat 1) (range) 4 5 6 7 8 9 245 4235 5423] (cycle [1 2])])))))

;(expect "In function +, the second argument [1 2 3 4 5 6 76 (0 1 2 3...) 756 354...] must be a number but is a vector."
;        (get-text-no-location
;         (run-and-catch-pretty-no-stacktrace 'intro.student '(+ 1 [1 2 3 4 5 6 76 (range) 756 354 6 645]))))

;; testing for pretty-print-value for functions within sequences and for vectors

(expect "In function +, the argument [1 2] must be a number but is a vector,\nin the function call (+ [1 2])"
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(+ [1 2]))))

(expect "In function +, the argument [+] must be a number but is a vector,\nin the function call (+ [+])"
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(+ [+]))))

(expect "In function +, the argument [map] must be a number but is a vector,\nin the function call (+ [map])"
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(+ [map]))))

(expect "In function +, the argument (\"a\" \"b\") must be a number but is a list,\nin the function call (+ (\"a\" \"b\"))"
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(+ '("a" "b")))))

(expect "In function +, the argument #{anonymous-function} must be a number but is a set,\nin the function call (+ #{anonymous-function})"
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(+ #{#(+ % 2)}))))

(expect "In function +, the argument #{+} must be a number but is a set,\nin the function call (+ #{+})"
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(+ #{+}))))

(expect "In function +, the argument {:a +} must be a number but is a map,\nin the function call (+ {:a +})"
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(+ {:a +}))))

(expect "In function +, the argument {:a {6 \"a\", 7 true}, :b 4, :c +} must be a number but is a map,\nin the function call (+ {:a {6 \"a\", 7 true}, :b 4, :c +})"
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(+ {:a {6 "a" 7 true} :b 4 :c +}))))

;; Note: the order of elements in a hashmap is not guaranteed, so the actual elements amy be different
(expect "In function +, the argument {:a 6, :k \"a\", :b 4, :c +, :l 6, :m 7} must be a number but is a map,\nin the function call (+ {:a 6, :k \"a\", :b 4, :c +, :l 6, :m 7})"
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

;; testing for odd? and even?
(expect "In function even?, the argument must be an integer number but is nil."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(even? nil))))

(expect "In function odd?, the argument must be a integer but is nil,\nin the function call (odd? nil)"
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(odd? nil))))

(expect "In function even?, the argument [(0 1 2 3...) (0 1 2 3...)] must be an integer number but is a vector."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(even? [(range) (range)]))))

(expect "In function dec, the argument :r must be a number but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(dec :r))))

;moved from core_fns_behavior

(expect Exception (-))
(expect Exception (/))
(expect Exception (max))
(expect Exception (min))
