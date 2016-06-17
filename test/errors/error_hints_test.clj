(ns errors.error_hints_test
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]
            [errors.prettify_exception :refer :all]
            [errors.exceptions :refer :all]
            [errors.error_hints :refer :all]))


(expect #"(.*)Check the spelling of namespaces you might be using, such as clojure.string\n(.*)"
        (:hints (run-and-catch-pretty-with-stacktrace 'intro.core '(clojure/string/splt "pattern" #"/"))))

(expect "If you are using functions from another file, make sure you use dots for namespaces and slashes for functions, such as clojure.string/split."
        (:hints (run-and-catch-pretty-with-stacktrace 'intro.core '(clojure.string.splt "pattern" #"/"))))

(expect #"(.*)Make sure you have the correct number of arguments(.*)"
        (:hints (run-and-catch-pretty-with-stacktrace 'intro.student '(#(+ % 1) 2 3))))

;; Currently there are no hints for this, using it as a test for empty hints.
(expect ""
        (:hints (run-and-catch-pretty-with-stacktrace 'intro.student '(map 2 3))))

