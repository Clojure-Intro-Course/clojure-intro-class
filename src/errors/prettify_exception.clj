(ns errors.prettify_exception
  (:require [clj-stacktrace.core :as stacktrace]
            [expectations :refer :all]
            [errors.error_dictionary :refer :all]
            [errors.error_hints :refer :all])
  (:use [errors.dictionaries]
	      [errors.messageobj]
	      [errors.errorgui]
        [seesaw.core]))

;;(def ignore-nses #"(clojure|java)\..*")
;;(def ignore-nses #"(user|clojure|java)\..*")
;; We should think of making this customizable: building blocks???

(defn first-match [e-class message]
	(first (filter #(and (= (:class %) e-class) (re-matches (:match %) message))
			error-dictionary)))

(defn msg-from-matched-entry [entry message]
  (if entry ((:make-msg-info-obj entry) (re-matches (:match entry) message))
            (make-msg-info-hashes message)))

(defn hints-for-matched-entry [entry]
  (let [key-for-hints (:key entry)
        lookup-hint (if key-for-hints (key-for-hints hints) "")]
        (if lookup-hint lookup-hint "")))

;; Putting together a message (perhaps should be moved to errors.dictionaries?
(defn get-pretty-message [e-class message]
  (if-let [entry (first-match e-class message)]
    ;; if there's a match for the exception and the message, replace the
    ;; message according to the dictionary and make a msg-info-obj out of it
    ((:make-msg-info-obj entry) (re-matches (:match entry) message))
    ;; else just make a msg-info-obj out the message itself
    (make-msg-info-hashes message)))

;; namespaces to ignore:

;; regular expressions for namespaces to be ignored. Any namespace equal to
;; or contaning these regexps would be ignored
(def ignored-namespaces ["clojure.main" "clojure.lang" "java" "clojure.tools" "user" "autoexpect.runner" "expectations"])

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
(def ignore-functions [{:clojure.core [#"load.*" "require" "alter-var-root"]}])

(defn- ignore-function? [str-or-regex fname]
  (if (string? str-or-regex) (= str-or-regex fname)
                             (re-matches str-or-regex fname)))

(expect true (ignore-function? "require" "require"))
(expect false (ignore-function? "require" "require5"))
(expect "load" (ignore-function? #"load" "load"))
(expect "load5" (ignore-function? #"load.*" "load5"))

(defn- ignored-function? [nspace fname]
  (let [key-ns (keyword nspace)
        ;; There should be only one match for filter
        functions-for-namespace (first (filter #(not (nil? (key-ns %))) ignore-functions))
        names (key-ns functions-for-namespace)]
    (if (nil? names) false (not (empty? (filter #(ignore-function? % fname) names))))))

(expect true (ignored-function? "clojure.core" "require"))
(expect false (ignored-function? "clojure.lang" "require"))
(expect false (ignored-function? "clojure.core" "require5"))
(expect true (ignored-function? "clojure.core" "load-one"))


(defn keep-stack-trace-elem [st-elem]
  "returns true if the stack trace element should be kept
   and false otherwise"
  (let [nspace (:ns st-elem)
	      namespace (if nspace nspace "") ;; in case there's no :ns element
        fname (:fn st-elem)]
  (and (:clojure st-elem) (not (re-matches ns-pattern namespace))
       (not (ignored-function? namespace fname)))))

(defn filter-stacktrace [stacktrace]
  "takes a stack trace and filters out unneeded elements"
  ;(println stacktrace)
  ;(println (filter keep-stack-trace-elem stacktrace))
  (filter keep-stack-trace-elem stacktrace))

(defn get-exception-location-hashmap
  "takes an error-dictionary entry and a message and returns a hashmap with the exception's
  filepath, filename, line number, character number, and exception type (runtime or compilation)."
  [entry message]
  ((:exc-location entry) (re-matches (:match entry) message)))

;; All together:
(defn prettify-exception [e]
  (let [e-class (class e)
        m (.getMessage e)
        message  (if m m "") ; converting an empty message from nil to ""
        exc (stacktrace/parse-exception e)
        stacktrace (:trace-elems exc)
        filtered-trace (filter-stacktrace stacktrace)
        entry (first-match e-class message)
        msg-info-obj (msg-from-matched-entry entry message)
        ;exception-location-hashmap (get-exception-location-hashmap entry message)
        hint-message (hints-for-matched-entry entry)]
    ;; create an exception object
    {:exception-class e-class
     :msg-info-obj msg-info-obj
     :stacktrace stacktrace
     :filtered-stacktrace filtered-trace
     :hints hint-message
     ;:path (:path exception-location-hashmap)
     ;:filename (:filename exception-location-hashmap)
     ;:line (:line exception-location-hashmap)
     ;:character (:character exception-location-hashmap)
     ;:exception-type (:exception-type exception-location-hashmap)
     }))

;;; Elena's note: we are not using get-pretty-message anymore
;;; in prettify-exception, so we need to retire it, but it seems
;;; to be used in some tests.....
(defn prettify-exception-no-stacktrace [e]
  (let [e-class (class e)
        m (.getMessage e)
        message (if m m "")] ; converting an empty message from nil to ""
    (get-pretty-message e-class message)))
