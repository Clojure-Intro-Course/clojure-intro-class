(ns errors.exceptions
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
   (:msg-info-obj (prettify-exception (run-and-catch-raw code))))
  ([name-space code]
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
  (nth (re-matches #"(.*)\nFound(.*)" (get-all-text m)) 1))


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

(expect #"Make sure you have the correct number of arguments"
         (:hints (prettify-exception (run-and-catch-raw '(assoc {1 2} 3)))))

(expect :assertion-error-with-argument (:key (first-match AssertionError "Assert failed: (check-if-sequable? \"filter\" argument2)")))

;(expect "" (:hints (prettify-exception (run-and-catch-raw 'intro.core '(filter odd? 5)))))

;##################################
;## 4. More real-life exceptions ##
;##################################


;;; We actually might want to change this exception by adding or changing a pre-condition for conj

(def dr-racket-exercise-class-cast (import-from-file "exceptions/DrRacket-Exercise2-ClassCast.ser"))

(def prettified-class-cast (prettify-exception dr-racket-exercise-class-cast))

(expect ClassCastException (:exception-class prettified-class-cast))

(expect "Attempted to use a string, but a collection was expected.\nFound in file student.clj on line 50 in function exercise2."
        (get-all-text (:msg-info-obj prettified-class-cast)))

(expect (trace-has-pair? :fn "exercise2") (:stacktrace prettified-class-cast))

(expect (trace-has-pair? :fn "exercise2") (:filtered-stacktrace prettified-class-cast))

(expect (trace-has-all-pairs? {:fn "exercise2" :ns "intro.student" :file "student.clj" :line 50})
        (:filtered-stacktrace prettified-class-cast))

(expect (trace-has-all-pairs? {:fn "-main" :ns "intro.core" :file "core.clj"})
        (:filtered-stacktrace prettified-class-cast))

(expect (trace-doesnt-have-all-pairs? {:ns "core.main"}) prettified-class-cast)

(expect (check-stack-count? 6) (:filtered-stacktrace prettified-class-cast))

(expect (trace-has-all-pairs? {:fn "apply" :ns "clojure.core"}) (:filtered-stacktrace prettified-class-cast))

(expect (top-elem-has-all-pairs? {:fn "conj" :ns "clojure.core"}) (:filtered-stacktrace prettified-class-cast))

(expect (trace-has-all-pairs? {:fn "conj" :ns "corefns.corefns"}) (:filtered-stacktrace prettified-class-cast))

;##############################################
;## 5. Comparing top elements of stacktraces ##
;##############################################

;#####################
;### 5.1 functions ###
;#####################

(defn important-part?
  "Takes a vector of a key-value pair and then returns true if the key matches any key in the vector of keys"
  [pair]
  (any? true? (map #(= (first pair) %) [:method :class :java :fn :ns :clojure])))

(defn get-important-in-stacktrace
  "Takes a stacktrace (a vector of hashmaps), and then only keeps what returns true from important-part?"
  [stacktrace]
  (vec (map #(into {} (filter important-part? %)) stacktrace)))

(defn make-trace-comparison
  "Takes an exception and a piece of quoted code or a string filename, makes an exception, and then returns a hashmap consisting
  of the filename/quoted code, a boolean stating whether the top elements of both the filtered and unfiltered stacktraces match,
  and then the unfiltered stacktrace up until the first match of the top element of the filtered stacktrace to the unfiltered,
  excluding unimportant parts, and finally the top element of the filtered, excluding unimportant parts."
  [exception source]
  (let [prettified-exception (prettify-exception exception)
        stacktrace (:stacktrace prettified-exception)
        top-of-filtered-stacktrace (first (:filtered-stacktrace prettified-exception))
        beginning-of-stacktrace (take (inc (.indexOf stacktrace top-of-filtered-stacktrace)) stacktrace)]
    {:source source
     :top-elements-match? (= (first beginning-of-stacktrace) top-of-filtered-stacktrace)
     :beginning-of-unfiltered-trace (get-important-in-stacktrace beginning-of-stacktrace)
     :top-element-of-filtered-trace (into {} (filter important-part? top-of-filtered-stacktrace))}))

(defn format-and-print-comparison
  "Takes a result of make-trace-comparison and will print everything out prettily in the terminal, with new lines and tabs"
  [trace-comparison]
  (do
    (println "source: "(:source trace-comparison) "\n"
             "top elements match?: "(:top-elements-match? trace-comparison) "\n"
             "beginning of unfiltered trace:" (interpose "\n \t \t \t \t" (:beginning-of-unfiltered-trace trace-comparison)) "\n"
             "top element of filtered trace: " (:top-element-of-filtered-trace trace-comparison) "\n")
    true))

;; To print comparisons, uncomment the doall line.
(defn format-and-print-comparisons
  "Takes in a list of hashmaps (results of make-trace-comparison), and calls a function to print everything and returns true.
  Uncomment the third line here if you want to see a trace-comparison of stacktrace tops."
  [trace-comparisons]
  (do
    ;(doall (map format-and-print-comparison trace-comparisons))
    true))

(defn make-and-print-comparisons
  "Takes in a collection of exceptions and a collection of either strings or quoted code, and then generates a trace-comparison
  for them, and then prints them nicely in the terminal."
  [coll-of-exceptions coll-of-sources]
  (format-and-print-comparisons (map make-trace-comparison coll-of-exceptions coll-of-sources)))

(defn compare-traces-of-quoted-code
  "Takes a namespace, and an unlimited number of pieces of quoted code, and then turns the quoted code into an exceptions,
  and then calls make-and-print-comparisons to print trace-comparisons."
  [name-space & quoted-code]
  (make-and-print-comparisons (map #(run-and-catch-raw name-space %) quoted-code) quoted-code))

(defn compare-traces-of-saved-exceptions
  "Takes an unlimited number of filenames, and then calls make-and-print-comparisons on the saved exceptions to print the trace-comparisons."
  [& filename]
  (make-and-print-comparisons (map import-from-file (str "exceptions/" filename)) filename))

(defn print-n-elements-of-stacktrace
  "Takes a number n and a stacktrace (filtered or unfiltered), and then prints that many elements of the stacktrace"
  [n trace]
  (do
    (println "\n First" n "elements of the stacktrace: \n"(interpose "\n" (take n trace)))
    true))

;##################
;### 5.2 prints ###
;##################

;; to actually print the result in the terminal, uncomment the line in format-and-print-comparisons

;; saved-exceptions is actually at the top of this file

(expect true (apply compare-traces-of-saved-exceptions saved-exceptions))

(expect true (compare-traces-of-quoted-code 'intro.core
                                            '(+ 2 "string")
                                            '(cons 1 2)
                                            '(inc "apple")
                                            '(prob16 "Dave")
                                            '(exercise2 "hello " "world")
                                            '(exercise9)
                                            '(error-in-anonymous)))

;(expect true (print-n-elements-of-stacktrace 100 (:stacktrace (run-and-catch-pretty-with-stacktrace
;                                                                    'intro.student
;                                                                    '(error-in-map-inc)))))

;(expect true (print-n-elements-of-stacktrace 100 (:stacktrace (run-and-catch-pretty-with-stacktrace
;                                                                    'intro.student
;                                                                    '(error-in-anonymous)))))

(expect true (compare-traces-of-quoted-code 'intro.core
                                            '(cons 16 79)))

(expect true (compare-traces-of-saved-exceptions "4clojure-prob156-AssertionError.ser"))

;#################
;### 5.3 tests ###
;#################

;; testing for make-trace-comparison
;; Elena: this used to be a test for non-matching top of teh stack, but now that we
;; have overwritten cons this is no longer valid. Commenting out.
;(expect {:source '(cons 16 79)
;         :top-elements-match? false
;         :beginning-of-unfiltered-trace '({:method "seqFrom", :class "clojure.lang.RT", :java true}
;                                          {:method "seq", :class "clojure.lang.RT", :java true}
;                                        {:method "cons", :class "clojure.lang.RT", :java true}
;                                          {:fn "cons", :ns "clojure.core", :clojure true})
;         :top-element-of-filtered-trace {:fn "cons", :ns "clojure.core", :clojure true}}
;        (make-trace-comparison (run-and-catch-raw 'intro.student
;                                                  '(cons 16 79)) '(cons 16 79)))

(expect {:source  "4clojure-prob156-AssertionError.ser"
         :top-elements-match?  true
         :beginning-of-unfiltered-trace '({:fn "map", :ns "corefns.corefns", :clojure true})
         :top-element-of-filtered-trace   {:fn "map", :ns "corefns.corefns", :clojure true}}
        (make-trace-comparison (import-from-file "exceptions/4clojure-prob156-AssertionError.ser") "4clojure-prob156-AssertionError.ser"))
