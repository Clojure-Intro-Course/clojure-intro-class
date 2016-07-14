(ns errors.testing_tools
  (:require [expectations :refer :all]
            [clj-stacktrace.core :as stacktrace]
            [errors.prettify_exception :refer :all]
            [errors.messageobj :refer :all]
            [utilities.stacktrace_functions :refer :all]
            [corefns.collection_fns :refer :all]
            [utilities.file_IO :refer :all])
  (:import [java.io.FileInputStream]
           [java.io.ObjectInputStream]
           [java.io.FileOutputStream]
           [java.io.ObjectOutputStream]
           [java.util.ArrayList]))


; Add files to this set when you make them.
(def saved-exceptions
  #{"4clojure-prob134-AssertionError.ser"
    "4clojure-prob15-ClassCast.ser"
    "4clojure-prob156-AssertionError.ser"
    "4clojure-prob16-Arity.ser"
    "4clojure-prob17-NullPointer.ser"
    "4clojure-prob18-AssertionError.ser"
    "4clojure-prob20-AssertionError.ser"
    "4clojure-prob21-ArityException.ser"
    "4clojure-prob24-pre-plus-rewrite-ClassCast.ser"
    "4clojure-prob57-ArityException.ser"
    "4clojure-prob64-NullPointer.ser"
    "add-five-IllegalArgException.ser"
    "DrRacket-Exercise2-ClassCast.ser"
    "4clojure-prob23-IndexOutOfBounds.ser"
    "4clojure-prob27-ArityException.ser"
    "DrRacket-Exercise9-ArityException.ser"
    "4clojure-prob38-ArityException.ser"
    "DrRacket-Exercise3-IndexOutOfBounds.ser"})

;;; INDEX ;;;

;1 Generating exceptions
;|-1.1 functions
;|-1.2 tests
;2. Comparing Stacktraces
;|-2.1 functions
;|-2.2 tests
;3. Testing for hints
;4. More Real-Life Exceptions
;5. Comparing top elements of stacktraces
;|-5.1 functions
;|-5.2 prints
;|-5.3 tests

;##############################
;## 1. Generating Exceptions ##
;##############################

; 1.1 functions

(defn run-and-catch-raw
  "A function that takes quoted code and runs it, attempting to catch any
  exceptions it may throw. Returns the exeception or nil. If a quoted namespace is
  given, it runs the code in that namespace.Attempted to use a symbol, but unrecognized type clojure.lang.Namespace was expected."
  ([code]
   (try
     (eval code)
     (catch Throwable e e)))
  ([name-space code]
    (try
      (let [our-ns (find-ns name-space)]
        ;(print (filter (fn [x] (re-matches #".*intro.*" (str x)))  (all-ns)))
        ;(print (str "printing namespace: " (nil? our-ns)))
        (binding [*ns* our-ns] (eval code)))
     (catch Throwable e e))))

(defn run-and-catch-pretty-no-stacktrace
  "A function that takes quoted code and runs it, attempting to catch any
   exceptions it may throw. Returns the prettified exception without a stacktrace. If a quoted
  namespace is given, it runs the code in that namespace."
  ([code]
   ;(println  (str "MSG: " (count (:msg-info-obj (prettify-exception (run-and-catch-raw code))))))
   (:msg-info-obj (prettify-exception (run-and-catch-raw code))))
  ([name-space code]
   ;(println  (str "MSG: " (count (:msg-info-obj (prettify-exception (run-and-catch-raw name-space code)))))
   (:msg-info-obj (prettify-exception (run-and-catch-raw name-space code)))))

;; when giving a namespace, pass in a quoted namespace
(defn run-and-catch-pretty-with-stacktrace
  "A function that takes quoted code and runs it, attempting to catch any
  exceptions it may throw. Returns the prettified exception with a stacktrace. If a quoted namespace is given,
  it runs the code in that namespace."
  ([code]
   (prettify-exception (run-and-catch-raw code)))
  ([name-space code]
   (prettify-exception (run-and-catch-raw name-space code))))

(defn exception->string
  "Converts exceptions to strings, returning a string or the original e if it
  is not an exception"
  [e] (if (instance? Throwable e) (.getMessage e) e))

(defn get-text-no-location [m]
  ;(println (str "MESSAGE" (get-all-text m)))
  (let [text-no-newln (nth (re-matches #"(.*)\nFound(.*)" (get-all-text m)) 1)
        text (if text-no-newln
               text-no-newln
               (let [matches (re-matches #"(.*)\n(.*)\nFound(.*)" (get-all-text m))]
                 (str (nth matches 1) "\n" (nth matches 2))))]
    text))


; 1.2 tests

(expect "java.lang.Long cannot be cast to clojure.lang.IFn"
        (exception->string (run-and-catch-raw '(1 3))))

;; Hmmm??? - there's no exception here, is there?
(expect 3
        (exception->string (run-and-catch-raw '(+ 1 2))))

;##############################
;## 2. Comparing Stacktraces ##
;##############################

; 2.1 functions

(defn get-keyword-in-stacktrace
  "Gets all of the keywords mentioned in a parsed stacktrace"
  [a-keyword trace]
  (filter
   (fn [ele]
     (not (nil? ele)))
   (map a-keyword (:trace-elems trace))))

(defn- get-keyword-but-not-x-in-stacktrace
  "Don't actually use this - wrap it in a helper function. Gets all the values of a keyword mentioned in a
  parsed stacktrace, except those that the predicate returns true for"
  [a-keyword pred trace]
  (filter
   (fn [ele]
     (not (or (nil? ele)
              (pred ele))))
   (map a-keyword (:trace-elems trace))))

;; an example of what get-keyword-but-not-x-in-stacktrace
(defn get-fns-in-stacktrace
  "Gets all of the functions mentioned in a parsed stacktrace"
  [trace]
  (get-keyword-but-not-x-in-stacktrace
   :fn
   (fn [ele] (not (clojure.string/blank? (re-matches #"eval\d.*" ele))))
   trace))


(defn get-eval-nums
  "Gets all evaulation numbers - a random number (confirm?) that is attached to all evals in the stacktrace. Used
  to confirm that two stacktraces came from the same exception."
  [trace]
  (map (fn [ele] ele)
       (get-keyword-in-stacktrace :fn trace)))

; 2.2 tests

(def ex1 (run-and-catch-raw '(+ 2 "pie")))

(expect "eval" (first (get-fns-in-stacktrace (stacktrace/parse-exception ex1))))

;##################################
;## 3. Testing for hints         ##
;##################################

(expect :class-cast-exception (:key (first-match ClassCastException
                                                 "java.lang.String cannot be cast to clojure.core.Number")))

;; because this is now an assertion error, no hint is actually written yet
;(expect #"The error happens when a function's argument is not of the type for which the function is defined."
;	(:hints (prettify-exception (run-and-catch-raw '(+ 2 "string")))))

;; Elena commented out testing for hints since we currently don't have hints

;(expect #"Make sure you have the correct number of arguments"
;         (:hints (prettify-exception (run-and-catch-raw '(assoc {1 2} 3)))))

(expect :assertion-error-with-argument (:key (first-match AssertionError "Assert failed: (check-if-sequable? \"filter\" argument2)")))

;(expect "" (:hints (prettify-exception (run-and-catch-raw 'intro.core '(filter odd? 5)))))

