(ns errors.dictionaries
  (:use [corefns.corefns]
        [errors.messageobj]
        [corefns.failed_asserts_info]
        ))

;; A dictionary of known types and their user-friendly representations
;; potentially we can have multiple dictionaries, depending on the level

(def type-dictionary {:java.lang.String "a string"
                      :java.lang.Number "a number"
                      :clojure.lang.Keyword "a keyword"
                      :java.lang.Boolean "a boolean"
		                  ;; I think this is better for new students to lump all numbers together
		                  :java.lang.Long "a number"
		                  :java.lang.Integer "a number"
		                  :java.lang.Double "a number"
		                  :java.lang.Float "a number"
		                  :java.lang.Short  "a number"
		                  :clojure.lang.BigInt "a number"
		                  ;; perhaps add big ints and such
		                  :java.lang.Character "a character" ;; switched back from a symbol
		                  ;; to short-cut processing of error messages for
		                  ;; "Don't know how to create a sequence from ..."
		                  :clojure.lang.ISeq "a sequence"
		                  :ISeq "a sequence"
		                  ;; Refs come up in turtle graphics
		                  :clojure.lang.Ref "a mutable object"
		                  ;; regular expressions wouldn't make sense to beginners,
		                  ;; but it's better to recognize their types for easier
		                  ;; help with diagnostics
		                  :java.util.regex.Pattern "a regular expression pattern"
		                  :java.util.regex.Matcher "a regular expression matcher"
		                  ;; also not something beginners would know,
		                  ;; but useful for understanding errors
		                  :clojure.lang.Symbol "a symbol"
		                  :clojure.lang.IPersistentStack "an object that behaves as a stack (such as a vector or a list)"
                      :clojure.lang.PersistentArrayMap "a map"
		                  ;; assoc works on maps and vectors:
		                  :clojure.lang.Associative "a map or a vector"
		                  :clojure.lang.Reversible "a vector or a sorted-map"
		                  :clojure.lang.Sorted "a collection stored in a sorted manner (such as sorted-map or sorted-set)"
		                  :clojure.lang.Sequential "a sequential collection (such as a vector or a list)"
		                  ;; This is here because of shuffle. It's not ideal, too similar to Sequential
		                  :java.util.Collection " a traversable collection (such as a vector, list, or set)" ; not sure if this makes sense in relation to the previous one
		                  ;; got this in a seesaw error message. Not sure what other types are "Named"
		                  ;; source: https://groups.google.com/forum/?fromgroups#!topic/clojure/rd-MDXvn3q8
		                  :clojure.lang.Named "a keyword or a symbol"
		                  :clojure.lang.nil "nil"})


;; matching type interfaces to beginner-friendly names.
;; Note: since a type may implement more than one interface,
;; the order is essential. The lookup is done in order, so
;; the first match is returned.
;; That's why it's a vector, not a hashmap.
;; USE CAUTION WHEN ADDING NEW TYPES!

(def general-types [[Number "a number"]
                    [clojure.lang.IPersistentVector "a vector"]
                    [clojure.lang.IPersistentList "a list"]
                    [clojure.lang.IPersistentSet "a set"]
                    [clojure.lang.IPersistentMap "a map"]
                    [clojure.lang.ISeq "a sequence"]
		                ;; collections - must go before functions since some collections
		                ;; implement the IFn interface
		                [clojure.lang.IPersistentCollection "a collection"]
		                [clojure.lang.IFn "a function"]])

;; The best approximation of a type t not listed in the type-dictionary (as a string)
;;; best-approximation: type -> string
(defn best-approximation [t]
  "returns a string representation of a type t not listed in the type-dictionary for user-friendly error messages"
  (let [attempt (resolve (symbol t))
        type (if attempt attempt (clojure.lang.RT/loadClassForName (str "clojure.lang." t))) ;; may need to add clojure.lang. for some types.
        matched-type (if type (first (filter #(isa? type (first %)) general-types)))]
    (if matched-type (second matched-type) (str "unrecognized type " t))))

;;; get-type: type -> string
(defn get-type [t]
  "returns a user-friendly representation of a type if it exists in the type-dictionary,
	or its default representation as an unknown type"
  ((keyword t) type-dictionary (best-approximation t)))

;; hashmap of internal function names and their user-friendly versions
(def predefined-names {:_PLUS_ "+"  :_ "-" :_SLASH_ "/" })

;;; lookup-funct-name: predefined function name -> string
(defn lookup-funct-name [fname]
  "looks up pre-defined function names, such as _PLUS_. If not found,
	returns the original"
  (let [lookup ((keyword fname) predefined-names)]
    (if lookup lookup (-> fname
                          (clojure.string/replace #"_QMARK_" "?")
                          (clojure.string/replace #"_BANG_" "!")
                          (clojure.string/replace #"_EQ_" "=")
                          (clojure.string/replace #"_LT_" "<")
                          (clojure.string/replace #"_GT_" ">")
                          (clojure.string/replace #"_STAR_" "*")))))

;;; check-if-anonymous-function: string -> string
(defn check-if-anonymous-function [fname]
  (if (or (= fname "fn") (re-matches #"fn_(.*)" fname))
      "anonymous function" fname))

;;; get-match-name: string -> string
(defn get-match-name [fname]
  "extract a function name from a qualified name"
  (let [m (nth (re-matches #"(.*)\$(.*)" fname) 2)
        matched (if m m (nth (re-matches #"(.*)/(.*)" fname) 2))]
    (if matched
      (check-if-anonymous-function (lookup-funct-name matched))
      fname)))

;;; remove-inliner: string -> string
(defn- remove-inliner [fname]
  "If fname ends with inliner this will return everything before it"
  (let [match (nth (re-matches #"(.*)--inliner" fname) 1)]
    (if match match fname)))

;;; get-function-name: string -> string
(defn get-function-name [fname]
  (remove-inliner (get-match-name fname)))

;;; get-macro-name: string -> string
(defn get-macro-name [mname]
  "extract a macro name from a qualified name"
    (nth (re-matches #"(.*)/(.*)" mname) 2))

;;; pretty-print-value: anything, string, string -> string
(defn pretty-print-value [value fname type]
  "returns a pretty-printed value based on its class, handles various messy cases"
    ; strings are printed in double quotes:
  (if (string? value) (str "\"" value "\"")
      (if (nil? value) "nil"
          (if (= type "a function")
            ; extract a function from the class fname (easier than from value):
            (get-function-name fname)
            (str value)))))

;;; arg-str: number -> string
(defn arg-str [n]
  (case n
    1 "first argument"
    2 "second argument"
    3 "third argument"
    4 "fourth argument"
    5 "fifth argument"
    (str n "th argument")))

;;; process-asserts-obj: string or nil -> string
(defn process-asserts-obj [n]
  "Returns a msg-info-obj generated for an assert failure based on the
	global seen-failed-asserts hashmap, clears the hashmap"
  ;; and perhaps need manual error handling, in case the seen-object is empty
  (let [t (:check @seen-failed-asserts)
        cl (:class @seen-failed-asserts)
        c (if cl (.getName cl) nil)
        fname (:fname @seen-failed-asserts)
        c-type (if c (get-type c) "nil") ; perhaps want to rewrite this
        v (:value @seen-failed-asserts)
        v-print (pretty-print-value v c c-type)
        arg (arg-str (if n (Integer. n) (:arg-num @seen-failed-asserts)))]
    (empty-seen) ; empty the seen-failed-asserts hashmap
    (make-msg-info-hashes
     "in function " fname :arg " " arg " " v-print :arg
     " must be " t :type " but is " c-type :type)))
