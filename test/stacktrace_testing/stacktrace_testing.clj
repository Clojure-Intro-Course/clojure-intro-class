(ns stacktrace_testing.compilation_errors
  (:use [errors.prettify_exception :only [line-number-format]])
  (:require [expectations :refer :all]
            [errors.messageobj :refer :all]
            [errors.exceptions :refer :all]
            [errors.prettify_exception :refer :all]
            [utilities.file_IO :refer :all]
            ))



;##################################
;## 4. More real-life exceptions ##
;##################################


;;; We actually might want to change this exception by adding or changing a pre-condition for conj

(def dr-racket-exercise-class-cast (import-from-file "exceptions/DrRacket-Exercise2-ClassCast.ser"))

(def prettified-class-cast (prettify-exception dr-racket-exercise-class-cast))

(expect ClassCastException (:exception-class prettified-class-cast))

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
  [& filenames]
  (make-and-print-comparisons (map #(import-from-file (str "exceptions/" %)) filenames) filenames))

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

(expect true (compare-traces-of-quoted-code 'intro.student
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

(expect true (compare-traces-of-quoted-code 'intro.student
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
