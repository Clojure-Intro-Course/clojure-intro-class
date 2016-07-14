(ns location.compilation_errors
  (:use [errors.prettify_exception :only [line-number-format]])
  (:require [expectations :refer :all]
            [errors.messageobj :refer :all]
            [errors.testing_tools :refer :all]
            [errors.prettify_exception :refer :all]
            [utilities.file_IO :refer :all]
            ))

;######################################
;### Testing for compilation errors ###
;######################################

(expect (str "End of file, starting at line 3.\nProbably a non-closing parenthesis or bracket.\nFound in file eof.clj" (line-number-format 4 1) ".")
        (get-all-text (:msg-info-obj (prettify-exception (import-from-file "exceptions/end_of_file.ser")))))

(expect (str "There is an unmatched delimiter ).\nFound in file unmatched_delimiter.clj" (line-number-format 3 20) ".")
        (get-all-text  (:msg-info-obj (prettify-exception (import-from-file "exceptions/unmatched_delimiter.ser")))))

;###########################################
;### Testing for ClassNotFoundException ####
;###########################################
(expect (more-> "Name clojure.string.split is undefined." get-text-no-location
                (re-pattern (str "(.*)Found in (.*)" (line-number-format "(\\d+)" "(\\d+)") "\\."))  get-all-text)
              (run-and-catch-pretty-no-stacktrace 'intro.core '(clojure.string.split "a b c" " ")))


;; testing for :illegal-argument-exactly-2-forms
(expect #"The function when-let requires exactly 2 forms in binding vector. Line (.*) in the file intro.core"
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(when-let [num1 1 num2 2] "hello"))))
