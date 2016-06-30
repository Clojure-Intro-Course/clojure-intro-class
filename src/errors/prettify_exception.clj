(ns errors.prettify_exception
  (:require [clj-stacktrace.core :as stacktrace]
            [expectations :refer :all]
            [errors.error_dictionary :refer :all]
            [errors.error_hints :refer :all])
  (:use [errors.dictionaries]
	      [errors.messageobj]
	      [errors.errorgui]
        [seesaw.core]))


(defn get-all-match-strings
  "Returns the list of all match strings and their corresponding exception types
   in error dictionary"
  []
  (map #(vector (:match %) (:class %)) error-dictionary))


(defn first-match [e-class message]
	(first (filter #(and (= (:class %) e-class) (re-matches (:match %) message))
			error-dictionary)))

(defn fn-name
  "Takes a function object and returns a symbol that corresponds to the result of
   the lookup of its name.
   If no name is found, a symbol 'anonymous function' (non-conformant)
   is returned.
   Warning: 'anonymous function' symbol is non-conformant"
  [f]
  (symbol (get-function-name (.getName (type f)))))

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

(defn msg-info-obj-with-data
  "Creates a message info object from an exception that contains data"
  [entry message data]
  (let [entry-info ((:make-msg-info-obj entry) (re-matches (:match entry) message))
        fname (:msg (second entry-info))
        problems (second (first (:clojure.spec/problems data))) ; getting the val matched to the first key
        args (:clojure.spec/args data)
        all-args-str (str (val-str args))
        pred-str (str (:pred problems))
        ;; remove the ? at the end to get the type; add an article:
        pred-type (if (= pred-str "seqable?") "a sequence" (str "a " (subs pred-str 0 (dec (count pred-str)))))
        value (:val problems)
        value-str (val-str value)
        value-type (get-type-with-nil value)
        arg-num-str (if (= (count args) 1) "argument" (arg-str (inc (first (:in problems)))))
        call-str (str "(" fname " " (subs all-args-str 1))]
    (println problems)
    (into entry-info
          (if (nil? value)
            ;; here nil is an arg value, so the formatting is :arg, not :type
            (make-msg-info-hashes (str ", the " arg-num-str) " must be " pred-type :type " but is " value-type :arg ".");", in the function call " )
            (make-msg-info-hashes (str ", the " arg-num-str " ") value-str :arg  " must be " pred-type :type " but is " value-type :type ".")))))


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

;; namespaces to ignore:

;; regular expressions for namespaces to be ignored. Any namespace equal to
;; or contaning these regexps would be ignored
(def ignored-namespaces ["clojure.main" "clojure.lang" "java" "clojure.tools" "clojure.spec" "user" "autoexpect.runner" "expectations" "clojure.core.protocols"])

(defn- replace-dots [strings]
  (map #(clojure.string/replace % #"\." "\\\\.") strings))


(def namespaces-to-ignore (replace-dots ignored-namespaces))

(expect ["clojure\\.main" "clojure\\.lang" "java" "clojure\\.tools"] (replace-dots ["clojure.main" "clojure.lang" "java" "clojure.tools"]))

(defn- surround-by-parens [strings]
  (map #(str "(" % ")") strings))

(expect ["(aaa)" "(bbb)"] (surround-by-parens ["aaa" "bbb"]))

(defn- add-postfix [strings postfix]
  (map #(str % postfix) strings))

(expect ["aaa((\\.|/)?(.*))" "bbb((\\.|/)?(.*))"] (add-postfix ["aaa" "bbb"] "((\\.|/)?(.*))"))

(defn- add-symbol-except-last [strings]
  (let [or-sym "|"]
    (conj (vec (add-postfix (butlast strings) or-sym)) (last strings))))

(expect ["aaa|" "bbb|" "ccc"] (add-symbol-except-last ["aaa" "bbb" "ccc"]))


(defn- make-pattern-string [to-ignore]
  (let [dot-or-slash-or-nothing "((\\.|/)?(.*))"]
    (apply str (add-symbol-except-last (surround-by-parens (add-postfix to-ignore dot-or-slash-or-nothing))))))


(expect "(aaa((\\.|/)?(.*)))|(bbb((\\.|/)?(.*)))"
        (make-pattern-string ["aaa" "bbb"]))

(def ns-pattern
  (re-pattern (make-pattern-string namespaces-to-ignore)))


;; first is needed in tests when multiple matches are returned
(expect "clojure.main" (first (re-matches ns-pattern "clojure.main")))
(expect "clojure.main" (re-matches (re-pattern "clojure\\.main") "clojure.main"))
(expect "clojure.main" (first (re-matches (re-pattern "clojure\\.main((\\.|/)?(.*))") "clojure.main")))

;; specify namespaces and function names or patterns
(def ignore-functions {:clojure.core [#"load.*" "require" "alter-var-root" "ex-info"]})

;; these functions are probably not needed for beginners, but might be needed at
;; more advanced levels
(def ignore-utils-functions {:clojure.core ["print-sequential" "pr-on" "pr"] :intro.test [#"eval.*"]})

(defn- ignore-function? [str-or-regex fname]
  (if (string? str-or-regex) (= str-or-regex fname)
                             (re-matches str-or-regex fname)))

(expect true (ignore-function? "require" "require"))
(expect false (ignore-function? "require" "require5"))
(expect "load" (ignore-function? #"load" "load"))
(expect "load5" (ignore-function? #"load.*" "load5"))


;; this thing really needs refactoring
(defn- ignored-function? [nspace fname]
  (let [key-ns (keyword nspace)
        ;; There should be only one match for filter
        names (key-ns (merge-with into ignore-functions ignore-utils-functions))]
        ;names (key-ns functions-for-namespace)]
    (if (nil? names) false (not (empty? (filter #(ignore-function? % fname) names))))))

(expect true (ignored-function? "clojure.core" "require"))
(expect false (ignored-function? "clojure.lang" "require"))
(expect false (ignored-function? "clojure.core" "require5"))
(expect true (ignored-function? "clojure.core" "load-one"))


(defn keep-stack-trace-elem
  "returns true if the stack trace element should be kept
   and false otherwise"
  [st-elem]
  (let [nspace (:ns st-elem)
	      namespace (if nspace nspace "") ;; in case there's no :ns element
        fname (:fn st-elem)]
  (and (:clojure st-elem) (not (re-matches ns-pattern namespace))
       (not (ignored-function? namespace fname)))))

(defn filter-stacktrace
  "takes a stack trace and filters out unneeded elements"
  [stacktrace]
  ;(println stacktrace)
  ;(println (filter keep-stack-trace-elem stacktrace))
  (filter keep-stack-trace-elem stacktrace))

(def non-student-namespaces ["clojure." "corefns"])

(def ns-pattern-non-student
  (re-pattern (make-pattern-string non-student-namespaces)))

(defn is-student-code?
  "determines whether a stack trace element corresponds to student code,
  assuming that the element appears in the filtered stacktrace"
  [st-elem]
  (let [nspace (:ns st-elem)
        namespace (if nspace nspace "")]
    (not (re-matches ns-pattern-non-student namespace))))

(expect true (is-student-code? {:file "NO_SOURCE_FILE", :line 154, :clojure true, :ns "intro.student", :fn "eval15883"}))
(expect false (is-student-code? {:file "core.clj", :line 3079, :clojure true, :ns "clojure.core", :fn "eval", :anon-fn false}))

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
        exc (stacktrace/parse-exception e)
        stacktrace (:trace-elems exc)
        filtered-trace (filter-stacktrace stacktrace)
        comp-location (get-compile-error-location (get-exc-message ex))
        location (if (empty? comp-location) (get-location-info filtered-trace) comp-location)
        entry (first-match e-class message) ; We use error dictionary here in first-match
        data (ex-data e) ; nil if not an instance of IExceptionInfo
        msg-info-obj (into (msg-from-matched-entry entry message data) (location-info location))
        hint-message (hints-for-matched-entry (:key entry))]
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

;;; Elena's note: we are not using get-pretty-message anymore
;;; in prettify-exception, so we need to retire it, but it seems
;;; to be used in some tests.....
;(defn prettify-exception-no-stacktrace [e]
;  (let [e-class (class e)
;        m (.getMessage e)
;        message (if m m "")] ; converting an empty message from nil to ""
;    (get-pretty-message e-class message)))
