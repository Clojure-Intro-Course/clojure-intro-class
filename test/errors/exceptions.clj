(ns errors.exceptions
  (:require [expectations :refer :all]
            [clj-stacktrace.core :as stacktrace]
            [errors.prettify_exception :refer :all]
            [errors.messageobj :refer :all]
            [errors.stacktrace_functions :refer :all])
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
    "4clojure-prob24-ClassCast.ser"
    "4clojure-prob57-ArityException.ser"
    "4clojure-prob64-NullPointer.ser"
    "add-five-IllegalArgException.ser"
    "DrRacket-Exercise2-ClassCast.ser"})


;;; INDEX ;;;

;1. Writing/Reading to file
;|-1.1 functions
;|-1.2 tests
;2 Generating exceptions
;|-2.1 functions
;|-2.2 tests
;3. Comparing Stacktraces
;|-3.1 functions
;|-3.2 tests
;4. Testing for hints
;5. More Real-Life Exceptions
;6. Comparing top elements of stacktraces

;################################
;## 1. Writing/Reading to file ##
;################################

;## global vars ##
(def path "exceptions/")

; 1.1 functions

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

(defn write-objects-local
  "writes a java object to a file, creating it if it does not exist, in path (see errors.exceptions)"
  [object filename]
  (export-to-file object (str path filename)))

(defn read-objects-local
  "reads a file in path (see errors.exceptions) as a java object"
  [filename]
  (import-from-file (str path filename)))

; 1.2 testing reading/writing to file

(def java-arraylist (new java.util.ArrayList 5))

(expect (.equals java-arraylist
           (let [filename "testfile.silly"
                 object java-arraylist]
             (write-objects-local object filename)
             (read-objects-local filename))))

;##############################
;## 2. Generating Exceptions ##
;##############################

; 2.1 functions

(defn run-and-catch
  "A function that takes quoted code and runs it, attempting to catch any exceptions it may throw. Returns the exeception or nil."
  [code] (try
           (eval code)
           (catch Throwable e e)))

(defn run-and-catch-corefns-exc
  "A function that takes quoted code and runs in the corefns namespace,
  attempting to catch any exceptions it may throw. Returns the exeception or nil."
  [code]
  (in-ns 'intro.core)
  (try
           (eval code)
           (catch Throwable e e)))

(defn run-and-catch-dictionaries [code]
  "A function that takes quoted code and runs it, attempting to catch any
  exceptions it may throw. Returns the exeception or nil."
  (in-ns 'intro.core)
   (try (eval code)
           (catch Throwable e (prettify-exception-no-stacktrace e))))

(defn run-and-catch-student-code-examples [code]
  "A function that takes quoted code and runs it, attempting to catch any
  exceptions it may throw. Returns the exeception or nil."
  (in-ns 'errors.student_code_examples)
   (try (eval code)
           (catch Throwable e (prettify-exception-no-stacktrace e))))

(defn run-and-catch-strings [code]
  "A function that takes quoted code and runs it, attempting to catch any
  exceptions it may throw. Returns the exeception or nil."
  (in-ns 'strings.strings)
   (try (eval code)
           (catch Throwable e (prettify-exception-no-stacktrace e))))

(defn run-and-catch-corefns [code]
  "A function that takes quoted code and runs it, attempting to catch any
  exceptions it may throw. Returns the exeception or nil."
  (in-ns 'intro.core)
   (try (eval code)
           (catch Throwable e (prettify-exception-no-stacktrace e))))

(defn exception->string
  "Converts exceptions to strings, returning a string or the original e if it is not an exception"
  [e] (if (instance? Exception e)
                                (.getMessage e)
                                e))

; 2.2 tests

(expect "java.lang.Long cannot be cast to clojure.lang.IFn"
        (exception->string (run-and-catch '(1 3))))

;; Hmmm??? - there's no exception here, is there?
(expect 3
        (exception->string (run-and-catch '(+ 1 2))))

;##############################
;## 3. Comparing Stacktraces ##
;##############################

; 3.1 functions

(defn get-keyword-in-stacktrace
  "Gets all of the keywords mentioned in a parsed stacktrace"
  [a-keyword trace]
  (filter
   (fn [ele]
     (not (nil? ele)))
   (map a-keyword (:trace-elems trace))))

(defn- get-keyword-but-not-x-in-stacktrace
  "Don't actually use this - wrap it in a helper function. Gets all the values of a keyword mentioned in a parsed stacktrace, except those that the predicate returns true for"
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
  "Gets all evaulation numbers - a random number (confirm?) that is attached to all evals in the stacktrace. Used to confirm that two stacktraces came from the same exception."
  [trace]
  (map (fn [ele] ele)
       (get-keyword-in-stacktrace :fn trace)))

; 3.2 tests

(def ex1 (run-and-catch '(+ 2 "pie")))

(expect "eval" (first (get-fns-in-stacktrace (stacktrace/parse-exception ex1))))

;##################################
;## 4. Testing for hints         ##
;##################################

(expect :class-cast-exception (:key (first-match ClassCastException
                                                 "java.lang.String cannot be cast to clojure.core.Number")))

;; because this is now an assertion error, no hint is actually written yet
;(expect #"The error happens when a function's argument is not of the type for which the function is defined."
;	(:hints (prettify-exception (run-and-catch '(+ 2 "string")))))

(expect #"Make sure you have the correct number of arguments"
         (:hints (prettify-exception (run-and-catch '(assoc {1 2} 3)))))

(expect :assertion-error-with-argument (:key (first-match AssertionError "Assert failed: (check-if-sequable? \"filter\" argument2)")))

;(expect "" (:hints (prettify-exception (run-and-catch-corefns-exc '(filter odd? 5)))))

;##################################
;## 5. More real-life exceptions ##
;##################################


;;; We actually might want to change this exception by adding or changing a pre-condition for conj

(def dr-racket-exercise-class-cast (read-objects-local "DrRacket-Exercise2-ClassCast.ser"))

(def prettified-class-cast (prettify-exception dr-racket-exercise-class-cast))

(expect ClassCastException (:exception-class prettified-class-cast))

(expect "Attempted to use a string, but a collection was expected." (get-all-text (:msg-info-obj prettified-class-cast)))

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
;## 6. Comparing top elements of stacktraces ##
;##############################################

;; filename should be in the form of a string
(defn compare-top-elems-of-stacktraces [filename]
  (let [prettified-exception (prettify-exception (read-objects-local filename))
        top-of-stacktrace (first (:stacktrace prettified-exception))
        top-of-filtered-stacktrace (first (:filtered-stacktrace prettified-exception))]
    {:filename filename
     :tops-match (= top-of-stacktrace top-of-filtered-stacktrace)
     :top-of-stacktrace top-of-stacktrace
     :top-of-filtered-stacktrace top-of-filtered-stacktrace}))

(defn print-comparison [comparison]
  (do
    (println (:filename comparison) "\n"
             (:tops-match comparison) "\n"
             (:top-of-stacktrace comparison) "\n"
             (:top-of-filtered-stacktrace comparison) "\n")
    true))
(defn print-all-comparisons [comparisons]
  (do
    (doall (map print-comparison comparisons))
    true))

(expect '({:filename "4clojure-prob57-ArityException.ser"
           :tops-match false
           :top-of-stacktrace {:method "throwArity", :class "clojure.lang.AFn", :java true, :file "AFn.java", :line 429}
           :top-of-filtered-stacktrace {:anon-fn false, :fn "prob57", :ns "intro.student", :clojure true, :file "student.clj", :line 107}}
          {:filename "DrRacket-Exercise2-ClassCast.ser"
           :tops-match true
           :top-of-stacktrace {:anon-fn false, :fn "conj", :ns "clojure.core", :clojure true, :file "core.clj", :line 83}
           :top-of-filtered-stacktrace {:anon-fn false, :fn "conj", :ns "clojure.core", :clojure true, :file "core.clj", :line 83}}
          {:filename "4clojure-prob21-ArityException.ser"
           :tops-match false
           :top-of-stacktrace {:method "throwArity", :class "clojure.lang.AFn", :java true, :file "AFn.java", :line 429}
           :top-of-filtered-stacktrace {:anon-fn false, :fn "prob21", :ns "intro.student", :clojure true, :file "student.clj", :line 138}}
          {:filename "4clojure-prob18-AssertionError.ser"
           :tops-match true
           :top-of-stacktrace {:anon-fn false, :fn "<", :ns "corefns.corefns", :clojure true, :file "corefns.clj", :line 135}
           :top-of-filtered-stacktrace {:anon-fn false, :fn "<", :ns "corefns.corefns", :clojure true, :file "corefns.clj", :line 135}}
          {:filename "add-five-IllegalArgException.ser"
           :tops-match false
           :top-of-stacktrace {:method "seqFrom", :class "clojure.lang.RT", :java true, :file "RT.java", :line 505}
           :top-of-filtered-stacktrace {:anon-fn false, :fn "cons", :ns "clojure.core", :clojure true, :file "core.clj", :line 29}}
          {:filename "4clojure-prob15-ClassCast.ser"
           :tops-match true
           :top-of-stacktrace {:anon-fn false, :fn "prob15", :ns "intro.student", :clojure true, :file "student.clj", :line 58}
           :top-of-filtered-stacktrace {:anon-fn false, :fn "prob15", :ns "intro.student", :clojure true, :file "student.clj", :line 58}}
          {:filename "4clojure-prob20-AssertionError.ser"
           :tops-match true
           :top-of-stacktrace {:anon-fn false, :fn "nth", :ns "corefns.corefns", :clojure true, :file "corefns.clj", :line 95}
           :top-of-filtered-stacktrace {:anon-fn false, :fn "nth", :ns "corefns.corefns", :clojure true, :file "corefns.clj", :line 95}}
          {:filename "4clojure-prob64-NullPointer.ser"
           :tops-match false
           :top-of-stacktrace {:method "ops", :class "clojure.lang.Numbers", :java true, :file "Numbers.java", :line 961}
           :top-of-filtered-stacktrace {:anon-fn false, :fn "prob64", :ns "intro.student", :clojure true, :file "student.clj", :line 89}}
          {:filename "4clojure-prob24-ClassCast.ser"
           :tops-match false
           :top-of-stacktrace {:method "add", :class "clojure.lang.Numbers", :java true, :file "Numbers.java", :line 126}
           :top-of-filtered-stacktrace {:anon-fn false, :fn "prob24", :ns "intro.student", :clojure true, :file "student.clj", :line 153}}
          {:filename "4clojure-prob156-AssertionError.ser"
           :tops-match true
           :top-of-stacktrace {:anon-fn false, :fn "map", :ns "corefns.corefns", :clojure true, :file "corefns.clj", :line 34}
           :top-of-filtered-stacktrace {:anon-fn false, :fn "map", :ns "corefns.corefns", :clojure true, :file "corefns.clj", :line 34}}
          {:filename "4clojure-prob17-NullPointer.ser"
           :tops-match false
           :top-of-stacktrace {:method "ops", :class "clojure.lang.Numbers", :java true, :file "Numbers.java", :line 961}
           :top-of-filtered-stacktrace {:anon-fn false, :fn "prob17", :ns "intro.student", :clojure true, :file "student.clj", :line 74}}
          {:filename "4clojure-prob16-Arity.ser"
           :tops-match false
           :top-of-stacktrace {:method "throwArity", :class "clojure.lang.AFn", :java true, :file "AFn.java", :line 429}
           :top-of-filtered-stacktrace {:anon-fn false, :fn "prob16", :ns "intro.student", :clojure true, :file "student.clj", :line 63}}
          {:filename "4clojure-prob134-AssertionError.ser"
           :tops-match true
           :top-of-stacktrace {:anon-fn false, :fn "filter", :ns "corefns.corefns", :clojure true, :file "corefns.clj", :line 108}
           :top-of-filtered-stacktrace {:anon-fn false, :fn "filter", :ns "corefns.corefns", :clojure true, :file "corefns.clj", :line 108}})
        (map compare-top-elems-of-stacktraces saved-exceptions))


(expect true
        (print-all-comparisons (map compare-top-elems-of-stacktraces saved-exceptions)))


