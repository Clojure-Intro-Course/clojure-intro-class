(ns errors.exceptionobj
  (:require [clj-stacktrace.core :as stacktrace])
  (:use [errors.messageobj]))

;; Not currently used anywhere
(defn exception->exceptionobj
  ""
  [e]
  (let [e-class nil
        msg-obj nil
        st nil
        fst nil
        hints nil]
    {:exception-class e-class
     :msg-info-obj msg-obj ;; without the stack trace
     :stacktrace st
     :filtered-stacktrace fst
     :hints hints}))
