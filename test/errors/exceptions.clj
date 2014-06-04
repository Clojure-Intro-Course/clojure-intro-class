(ns errors.exceptions
  (:require [expectations :refer :all]
            [clj-stacktrace.core :as stacktrace])
  (:import [java.io.FileInputStream]
           [java.io.ObjectInputStream]
           [java.io.FileOutputStream]
           [java.io.ObjectOutputStream]
           [java.util.ArrayList]))

;;; INDEX ;;;

;1. Writing/Reading to file
;;1.1 functions
;;1.2 tests
;2. Comparing Stacktraces

;################################
;## 1. Writing/Reading to file ##
;################################

;## global vars ##
(def path "exceptions/")

; 1.1 : functions

;## NOTE ##
;;The second part of let is a series of expressions that evaluate in /order/, returning the last expression.
;;This is Important:
;;The following two functions are dependent on events occurring in chronological order.

(defn export-to-file
  "Uses Java's Serializable to write a (java) object to a file"
  [obj filepath]
  (let [file-stream (java.io.FileOutputStream. filepath)
        obj-stream (java.io.ObjectOutputStream. file-stream)
        ]
    (.writeObject obj-stream obj)
    (.close obj-stream)
    (.close file-stream)
    (println (str "data saved in project folder or: " filepath))
  ))

(defn import-from-file
  "Uses Java's Serializable to read a (java) object from a file"
  [filepath]
  (let [file-stream (java.io.FileInputStream. filepath)
        obj-stream (java.io.ObjectInputStream. file-stream)
        e (.readObject obj-stream)]
    (.close obj-stream)
    (.close file-stream)
    e))

(defn- write-objects-local
  "writes a java object to a file, creating it if it does not exist, in path (see errors.exceptions)"
  [object filename]
  (export-to-file object (str path filename)))

(defn- read-objects-local
  "reads a file in path (see errors.exceptions) as a java object"
  [filename]
  (import-from-file (str path filename)))

; 1.2 : testing reading/writing to file

(def java-arraylist (new java.util.ArrayList 5))

(expect (.equals java-arraylist
           (let [filename "testfile.silly"
                 object java-arraylist]
             (write-objects-local object filename)
             (read-objects-local filename))))


;###########################
;## Comparing Stacktraces ##
;###########################



