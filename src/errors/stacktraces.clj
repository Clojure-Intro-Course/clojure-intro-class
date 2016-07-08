(ns errors.stacktraces
  (:require [clj-stacktrace.core :as stacktrace]
            [expectations :refer :all]
            [errors.error_dictionary :refer :all])
  (:use [errors.dictionaries]
	      [errors.messageobj]))

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
