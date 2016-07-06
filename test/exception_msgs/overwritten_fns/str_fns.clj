(ns errors.error_dictionary_test
  (:use [errors.prettify_exception :only [line-number-format]])
  (:require [expectations :refer :all]
            [errors.messageobj :refer :all]
            [errors.exceptions :refer :all]
            [errors.prettify_exception :refer :all]
            [utilities.file_IO :refer :all]
            ))

;; testing for :class-cast-exception
(expect "Attempted to use a string, but a character was expected."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.core
          '(int "banana"))))

;; We use the same message as for identfier undefined, but have a hint that is
;; more specific to dynamic class loading
(expect (more-> "Name clojure.string.split is undefined." get-text-no-location
                (re-pattern (str "(.*)Found in (.*)" (line-number-format "(\\d+)" "(\\d+)") "\\."))  get-all-text)
              (run-and-catch-pretty-no-stacktrace 'intro.core '(clojure.string.split "a b c" " ")))
