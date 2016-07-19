(ns errors.prettify_exception
  (:require [clj-stacktrace.core :as stacktrace]
            [expectations :refer :all]
            [clojure.string :as str]
            [errors.error_dictionary :refer :all]
            [errors.error_hints :refer :all])
  (:use [errors.dictionaries]
	      [errors.messageobj]
	      [errors.errorgui]
        [errors.stacktraces]
        [seesaw.core]))


(defn first-match
  [e-class message]
	(first (filter #(and (= (:class %) e-class) (re-matches (:match %) message))
			error-dictionary)))

(defn fn-name
  "Takes a function object and returns a symbol that corresponds to the result of
   the lookup of its name.
   If no name is found, a symbol 'anonymous function' (non-conformant)
   is returned.
   Handles spec-checking functions differently since they are looked up in corefns-map
   by full name.
   Warning: 'anonymous function' symbol is non-conformant"
  [f]
  (let [f-str (str f)]
    (if (re-matches #"clojure\.spec\.test\$spec_checking_fn(.*)" f-str)
        (symbol (get-function-name f-str))
        (symbol (get-function-name (.getName (type f)))))))

(defn is-function?
  "Uses our dictionary to check if a value should be printed as a function"
  [v]
  ;; checking for nil first:
  (and v (= (get-type (.getName (type v))) "a function")))


(defn single-val-str
  "Takes a single (non-collection) value and returns its string represntation.
   Returns a string 'nil' for nil, encloses strings into double quotes,
   performs a lookup for function names, returns 'anonymous function' for
   anonymous functions"
  [v]
  (cond
    (nil? v) "nil"
    (string? v) (str "\"" v "\"")
    (is-function? v) (fn-name v)
    :else (str v)))

(expect "nil" (single-val-str nil))
(expect "\"hi\"" (single-val-str "hi"))
(expect ":hi" (single-val-str :hi))
;; testing for function names requires namespaces magic, so not doing it here right now

(defn lookup-fns
  "Recursively replace internal Clojure function names with user-readable ones
   in the given value"
  [v]
  (cond
    (not (coll? v)) (if (is-function? v) (fn-name v) v)
    (vector? v) (into [] (map lookup-fns v))
    (seq? v) (into '() (reverse (map lookup-fns v)))
    (set? v) (into #{} (map lookup-fns v))
    (map? v) (reduce #(apply assoc %1 %2) {} (map lookup-fns v));; map has key/val pairs
    :else v))

(defn val-str
  "If v is a not a collection, returns the result of apply single-val-str to it.
   Otherwise returns the same collection, but with functions replaced by their
   names, recursively throughout the collection."
  [v]
  (if-not (coll? v) (single-val-str v) (lookup-fns v)))

(defn- message-arity
  "Gives the arity part of the message when there is an arity error detected by spec"
  ;; currently doesn't use the reason, for consistency with the wording of non-spec arity errors
  ;; the reasons may be "Insufficient input" and "Extra input" (as strings)
  [reason args fname]
  (let [arg-args (if (= 1 (count args)) " argument" " arguments")
        arity (lookup-arity fname)]
  (make-msg-info-hashes "You cannot pass " (number-word (str (count args))) arg-args " to a function "
                        fname :arg (if arity (str ", need " arity) ""))))

(defn- function-call-string
  "Gives the function call part of the message for spec error messages"
  [args fname]
  (let [all-args-str (if args (str (val-str args)) "")
        call-str (str "(" fname " " (if args (subs all-args-str 1) ")"))]
   (make-msg-info-hashes ",\n" "in the function call " call-str :call)))

(defn- type-from-failed-pred
  "Returns a type name from the name of a failed predicate"
  [pred-str]
  (cond (= pred-str "seqable?") "a sequence"
        (= pred-str "ifn?") "a function"
        (= pred-str "map?") "a hashmap"
        (= pred-str "integer?") "an integer number"
        :else (str "a " (subs pred-str 0 (dec (count pred-str))))))

(defn- or-str
  "Takes a vector of predicates, returns a string of their names separated by 'or'"
  [pred-strs]
  (apply str (interpose " or " (filter #(not (str/starts-with? % "a length")) ;; to weed out the args-length mismatches
                                       (distinct (map type-from-failed-pred pred-strs))))))

(defn- messages-types
  "Gives the part of the message for spec conditions failure"
  [problems value arg-num n]
  (let [pred-str (if (map? problems) (str (:pred problems)) (map #(str (:pred %)) problems)) ;; it's a map when it's a single predcate
        pred-type (if (map? problems) (type-from-failed-pred pred-str) (or-str pred-str))
        value-str (val-str value)
        value-type (get-type-with-nil value)
        arg-num-str (if arg-num (if (= n 1) "argument" (arg-str (inc (first arg-num)))) "")]
   (if (nil? value)
       (make-msg-info-hashes (str ", the " arg-num-str) " must be " pred-type :type " but is " value-type :arg)
       (make-msg-info-hashes (str ", the " arg-num-str " ") value-str :arg  " must be " pred-type :type " but is " value-type :type))))

(defn- get-predicates
  "If there is only one non-nil predicate in data, the hash map for that predicate
   is returned. If there are several non-nil predicates, a vector of their hash maps is
   returned."
  [data]
  (let [predicates (:clojure.spec/problems data)]
    (if (= 1 (count predicates))
        (first predicates)
        (let [non-nils (filter #(not= "nil?" (str (:pred %))) predicates)]
             (if (= (count non-nils) 1) (first non-nils) non-nils)))))

(defn- arity-error?
  "Returns true if all predicates have arity errors and false otherwise.
   Assumes that spec predicates for non-matching number of arguments start with 'length'"
  [problems]
  (every? #(str/starts-with? (str (:pred %)) "length") problems))

(defn- first-non-length
  "Returns the first non-length predicate"
  [problems]
  (first (filter #(not (str/starts-with? (str (:pred %)) "length")) problems)))

(defn msg-info-obj-with-data
  "Creates a message info object from an exception that contains data"
  [entry message data]
  (let [entry-info ((:make-msg-info-obj entry) (re-matches (:match entry) message))
        fname (:msg (second entry-info))
        problems (get-predicates data) ; returns a predicate hashmap or a vector of pred hashmaps
        problem (if (map? problems) problems (first-non-length problems)) ; to make it easy to extract fields
        args (:clojure.spec/args data)
        reason (:reason problem)
        arity? (if reason true (arity-error? (if (map? problems) [problems] problems)))
        value (:val problem)
        arg-num (:in problem)]
    ;(println "Data:" data)
    ;(println "Problem" problem)
    (if arity?
      (into (message-arity reason args fname) (function-call-string args fname))
      (into entry-info (into (messages-types problems value arg-num (count args)) (function-call-string args fname))))))

(defn msg-from-matched-entry [entry message data]
  "Creates a message info object from an exception and its data, if exists"
  (cond
    (and data entry) (msg-info-obj-with-data entry message data)
    entry ((:make-msg-info-obj entry) (re-matches (:match entry) message))
    :else (make-msg-info-hashes message)))

(defn hints-for-matched-entry
  "Returns all hints found for an exception key, as one string."
  [entry-key]
  (apply str (vals (filter #((first %) entry-key) hints))))


(defn get-location-info
  "returns the location at the top-most occurrence of student code,
  assuming that the stack trace has already been filtered"
  [filtered-trace]
  (let [top-student-line (first (filter is-student-code? filtered-trace))]
    {:file (:file top-student-line), :line (:line top-student-line), :fn (:fn top-student-line)}))

(expect
 {:file  "student.clj", :line 154, :fn "my-fn"}
 (get-location-info (filter-stacktrace
                            [{:file "AFn.java", :line 429, :java true, :class "clojure.lang.AFn", :method "throwArity"}
                             {:file "AFn.java", :line 44, :java true, :class "clojure.lang.AFn", :method "invoke"}
                             {:file "Compiler.java", :line 6792, :java true, :class "clojure.lang.Compiler", :method "eval"}
                             {:file "Compiler.java", :line 6755, :java true, :class "clojure.lang.Compiler", :method "eval"}
                             {:file "core.clj", :line 3079, :clojure true, :ns "clojure.core", :fn "eval", :anon-fn false}
                             {:file "student.clj", :line 154, :clojure true, :ns "intro.student", :fn "my-fn", :anon-fn false}
                             {:file "student_test.clj", :line 153, :clojure true, :ns "intro.student-test", :fn "expect1132213279", :anon-fn true}
                             {:file "student_test.clj", :line 150, :clojure true, :ns "intro.student-test", :fn "expect1132213279", :anon-fn false}
                             {:file "expectations.clj", :line 229, :clojure true, :ns "expectations", :fn "test-var", :anon-fn true}
                             {:file "expectations.clj", :line 225, :clojure true, :ns "expectations", :fn "test-var", :anon-fn false}
                             {:file "expectations.clj", :line 263, :clojure true, :ns "expectations", :fn "test-vars", :anon-fn true}
                             {:file "expectations.clj", :line 243, :clojure true, :ns "expectations", :fn "create-context", :anon-fn false}
                             {:file "expectations.clj", :line 263, :clojure true, :ns "expectations", :fn "test-vars", :anon-fn false}
                             {:file "expectations.clj", :line 308, :clojure true, :ns "expectations", :fn "run-tests", :anon-fn false}
                             {:file "expectations.clj", :line 314, :clojure true, :ns "expectations", :fn "run-all-tests", :anon-fn false}
                             {:file "expectations.clj", :line 571, :clojure true, :ns "expectations", :fn "eval2603", :anon-fn true}
                             {:file nil, :line nil, :java true, :class "expectations.proxy$java.lang.Thread$ff19274a", :method "run"}])))

(defn get-cause-if-needed
  "returns the cause of a compilation exception in cases when we need
   to process the cause, not the exception itself"
  [e]
  (let [cause (.getCause e)]
    (if (= (class cause) clojure.lang.Compiler$CompilerException)
      ;recursively look for non-compiler exception cause
      (get-cause-if-needed cause)
      (if (and (= (class e) clojure.lang.Compiler$CompilerException)
               cause) ; has a non-nil cause
        cause e))))

(defn compiler-error?
  "an ad-hoc method to determine if an exception is really a compilation error:
  it's a compilation error with no cause or a generic cause."
  [e]
  (let [cause (.getCause e)]
    (and (= (class e) clojure.lang.Compiler$CompilerException)
         (or (nil? cause) (= (class cause) java.lang.RuntimeException)))))

;#########################################
;############ Location format  ###########
;#########################################

(defn line-number-format
  "Takes a line number and a character poistion and returns a string
   of how they are reported in an error message"
  [line ch]
  (str " on, or before, line " line))

(defn location-info
  "Takes the location hashmap (possibly empty) and returns a message info
  object to be merged with the rest of the message"
  [location]
  (if (empty? location) ""
    (let [file (:file location)
          line (:line location)
          character (:char location)
          fn-name (:fn location)
          ;character-msg (if character (make-msg-info-hashes " at character " character :loc) nil)
          ;character-msg nil ; Removed character info since it seems to confuse people
          fn-msg (if fn-name (make-msg-info-hashes " in function " fn-name :loc) nil)]
      (reduce into [(make-msg-info-hashes "\nFound in file " (clojure.string/replace file #".*\/" "") :loc (line-number-format line character) :loc)
            ;character-msg
            fn-msg
            (make-msg-info-hashes ".")]))))

(defn location-from-data
  "Takes exception data from a spec condition failure,
   returns the location hashmap"
  [data]
  (select-keys (:clojure.spec.test/caller data) [:file :line]))

(defn get-exc-message
  "returns the message in an exception or an empty string if the message is nil"
  [e]
  (let [m (.getMessage e)]
    (if m m "")))

(defn- cut-stack-trace
  [stacktrace limit]
  (if (> (count stacktrace) limit) (conj (vec (take limit stacktrace)) {:file "" :line nil :clojure true :ns "and more..."}) stacktrace))

;; All together:
(defn prettify-exception [ex]
  (let [compiler? (compiler-error? ex)
        e (get-cause-if-needed ex)
        ;; replacing clojure.lang.LispReader$ReaderException by RuntimeException
        ;; to avoid code duplication in error handling since their errors
        ;; overlap.
        e-class (if (= (class e) clojure.lang.LispReader$ReaderException) RuntimeException (class e))
        message (get-exc-message e) ; converting an empty message from nil to ""
        entry (first-match e-class message) ; We use error dictionary here in first-match
        data (ex-data e)
        exc (stacktrace/parse-exception e)
        stacktrace (:trace-elems exc)
        filtered-trace (filter-stacktrace stacktrace)
        spec-location (when data (location-from-data data))
        comp-location (if (empty? spec-location) (get-compile-error-location (get-exc-message ex)) spec-location)
        location (if (empty? comp-location) (get-location-info filtered-trace) comp-location)
        msg-info-obj (into (msg-from-matched-entry entry message data) (location-info location))
        hint-message (hints-for-matched-entry (:key entry))]
;;     (println ex)
;;     (println spec-location)
;;     (println location)
    ;; create an exception object
    {:exception-class e-class
     :compiler? compiler?
     :msg-info-obj msg-info-obj
     :stacktrace stacktrace
     :filtered-stacktrace filtered-trace
     :hints hint-message}))

(defn standard
  "returns a non-transformed error message with the non-filtered stack trace
  as an exception object"
  [ex]
  ;; create an exception object
  (let [e (get-cause-if-needed ex)
        ;; replacing clojure.lang.LispReader$ReaderException by RuntimeException
        ;; to avoid code duplication in error handling since their errors
        ;; overlap.
        e-class (if (= (class e) clojure.lang.LispReader$ReaderException) RuntimeException (class e))
        message (.getMessage e)
        exc (stacktrace/parse-exception e)
        stacktrace (:trace-elems exc)]
    {:exception-class e-class
     :compiler? false
     :msg-info-obj (make-msg-info-hashes (str (.getName e-class)) ": " (if (> (count message) 40) (str "\n\t" message) message))
     :stacktrace stacktrace  ;(cut-stack-trace stacktrace 45)
     :filtered-stacktrace (cut-stack-trace stacktrace 40)
     :hints ""}))

(defn trace-hashmap-to-StackTraceElement
  "Converts a clojure stacktrace element (hashmap) to a java StackTraceElement"
  [trace-hashmap]
  (let [declaringClass (if-let [class-name (:class trace-hashmap)]
                     class-name
                     (:ns trace-hashmap))
        methodName (if-let [method (:method trace-hashmap)]
                     method
                     (:fn trace-hashmap))
        fileName (:file trace-hashmap)
        lineNumber (:line trace-hashmap)]
    (new StackTraceElement declaringClass methodName fileName lineNumber)))

(defn exception-obj-to-java-Throwable
  "Converts an exception-obj hashmap into the correct subtype of Java Throwable"
  [exception-obj]
  (let [e-class (:exception-class exception-obj)
        message (get-all-text (:msg-info-obj exception-obj))
        java-Throwable (eval `(new ~e-class ~message))
        stack-trace-element-sequence (map trace-hashmap-to-StackTraceElement (:filtered-stacktrace exception-obj)); "for each thing in :filtered-stacktrace, turn it into StackTraceElement and put it in the array"
        stack-trace-element-array (into-array stack-trace-element-sequence)
        ]
    (do (.setStackTrace java-Throwable stack-trace-element-array)
        java-Throwable)))
