(ns utilities.repl
  (:require [expectations :refer :all]
            [clj-stacktrace.core :as stacktrace])
  (:use [utilities.file_IO :only [export-to-file import-from-file]]))

;###########################
;# Utilities for examining #
;# errors in the REPL      #
;###########################

(defn serialize-compilation
  "Serializes a java object to the the compilation folder containing other serialized repl objects.
  Takes a filename (which should describe the error) without an extension."
  [e filename]
  (export-to-file e (str "exceptions/repl/compilation/" filename ".ser")))

(defn serialize-runtime
  "Serializes a java object to the the runtime folder containing other serialized repl objects.
  Takes a filename (which should describe the error) without an extension."
  [e filename]
  (export-to-file e  (str "exceptions/repl/runtime/" filename ".ser")))

(defn examine
  "Takes an exception and an optional boolean. Pretty-prints the exception,
  and if the optional boolean is true, spits that exeception to a file in exceptions/repl"
  ([e] (examine e false))
  ([e bool]
   (let [exc (stacktrace/parse-exception e)]
     (clojure.pprint/pprint exc)
     (when bool ((spit (str "exceptions/repl/exception-" (quot (System/currentTimeMillis) 1000) ".tmp")
                     (with-out-str (clojure.pprint/pprint exc)))))
     )))



