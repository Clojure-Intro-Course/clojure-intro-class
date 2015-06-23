(ns errors.error_hints_test
  (:require ;[errors.dictionaries :refer :all]
            [expectations :refer :all]
            [corefns.corefns :refer :all]
            [errors.prettify_exception :refer :all]
            [errors.exceptions :refer :all]
            ;[errors.messageobj :refer :all]
            [errors.error_hints :refer :all]))


(expect #"(.*)Check the spelling of namespaces you might be using, such as clojure.string\n(.*)"
        (:hints (run-and-catch-pretty-with-stacktrace 'intro.core '(clojure/string/splt "pattern" #"/"))))
