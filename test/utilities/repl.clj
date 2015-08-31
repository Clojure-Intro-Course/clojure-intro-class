(ns utilities.repl
  (:require [expectations :refer :all]
            [clj-stacktrace.core :refer [parse-exception]]
            [errors.prettify_exception :refer [filter-stacktrace]]
            [utilities.file_IO :refer [export-to-file import-from-file]]))

;###########################
;# Utilities for examining #
;# errors in the REPL      #
;###########################

(defn serialize-compilation!
  "Creates two files: a .ser file, which contains the serialized exception, and a .txt file which contains a text summary of the exception.
  These files are created in exceptions/repl/compilation/. Takes an exception and a filename (which should describe the error) without an extension.
  Amazingly thread unsafe."
  [e filename]
  (let [exc (parse-exception e)
        stacktrace (:trace-elems exc)
        filtered (filter-stacktrace stacktrace)]
    ;;serialize the file
    (export-to-file e (str "exceptions/repl/compilation/" filename ".ser"))
    ;;spit the message
    (spit (str "exceptions/repl/compilation/" filename ".txt" ) (str "\n\n+++ message +++\n\n" (:message exc)) )
    ;;append the filtered trace
    (spit (str "exceptions/repl/compilation/" filename ".txt") (str "\n\n+++ filtered +++\n\n" filtered) :append true)
    ;;append the unfiltered trace
    (spit (str "exceptions/repl/compilation/" filename ".txt") (str "\n\n+++ unfiltered +++\n\n"stacktrace) :append true)))

  (defn serialize-runtime!
  "Creates two files: a .ser file, which contains the serialized exception, and a .txt file which contains a text summary of the exception.
  These files are created in exceptions/repl/runtime/. Takes an exception and a filename (which should describe the error) without an extension.
  Amazingly thread unsafe."
  [e filename]
  (let [exc (parse-exception e)
        stacktrace (:trace-elems exc)
        filtered (filter-stacktrace stacktrace)]
    ;;serialize the file
    (export-to-file e (str "exceptions/repl/runtime/" filename ".ser"))
    ;;spit the message
    (spit (str "exceptions/repl/runtime/" filename ".txt" ) (str "\n\n+++ message +++\n\n" (:message exc)))
    ;;append the filtered trace
    (spit (str "exceptions/repl/runtime/" filename ".txt") (str "\n\n+++ filtered +++\n\n" filtered) :append true)
    ;;append the unfiltered trace
    (spit (str "exceptions/repl/runtime/" filename ".txt")  (str "\n\n+++ unfiltered +++\n\n" stacktrace) :append true)))

(defn examine
  "Takes an exception. Prints the message and the filtered stacktrace."
  [e]
   (let [exc (parse-exception e)
         stk (:trace-elems exc)
         msg (:message exc)]
     (println (str msg "\n\n+++filtered+++\n\n"))))


