(ns exception_msgs.overwritten_fns.higher_order_fns.clj
  (:use [errors.prettify_exception :only [line-number-format]])
  (:require [expectations :refer :all]
            [errors.messageobj :refer :all]
            [errors.exceptions :refer :all]
            [errors.prettify_exception :refer :all]
            [utilities.file_IO :refer :all]
            ))

;; testing for :class-cast-exception-cannot-cast-to-map-entry
(expect "Attempted to create a map using a keyword, but a sequence of vectors of length 2 or a sequence of maps is needed."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.core
          '(into {} [#{:x :m} #{:q :b}]))))


;; Note: the order of elements in a hashmap is not guaranteed, so the actual elements amy be different
(expect "In function +, the first argument {:a 6, :k \"a\", :b 4, :c +, :l 6, :m 7} must be a number but is a map,\nin the function call (+ {:a 6, :k \"a\", :b 4, :c +, :l 6, :m 7})"
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(+ {:a 6 :k "a" :b 4 :c + :l 6 :m 7}))))

;; testing for :illegal-argument-no-val-supplied-for-key
(expect "No value found for key d. Every key for a hash-map must be followed by a value."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(hash-map "c" :d "d"))))

;; testing for :illegal-argument-vector-arg-to-map-conj
(expect "Vectors added to a map must consist of two elements: a key and a value."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(into {} [[1 2] [3]]))))

;; testing for invalid token error
(expect #"You cannot use : in this position."
       (get-text-no-location (:msg-info-obj (try (load-file "exceptions/compilation_errors/invalid_token_error2.clj")
                       (catch Throwable e (prettify-exception e))))))

(expect "Vectors added to a map must consist of two elements: a key and a value."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(merge {:a 1 :b 2} [1 2 3] [1 2] [1 5]))))

(expect "Vectors added to a map must consist of two elements: a key and a value."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(conj {:a 1 :b 2} [1 2 3 4]))))

;; testing for :illegal-argument-even-number-of-forms
(expect #"Parameters for let must come in pairs, but one of them does not have a match; (.*)"
       (get-text-no-location (:msg-info-obj (try (load-file "exceptions/compilation_errors/let-no-matching-pair.clj")
                       (catch Throwable e (prettify-exception e))))))

(expect #"When declaring a let, you need to pass it a vector of arguments.(.*)"
       (get-text-no-location (:msg-info-obj (try (load-file "exceptions/compilation_errors/let-odd-number-bindings.clj")
                       (catch Throwable e (prettify-exception e))))))

;; testing for # must be followed by a symbol error
(expect #"# must be followed by a symbol."
       (get-text-no-location (:msg-info-obj (try (load-file "exceptions/compilation_errors/#_must_be_followed_by_symbol.clj")
                       (catch Throwable e (prettify-exception e))))))

;; testing for wrong number of args to a keyword
(expect #"A keyword: :a can only take one or two arguments."
       (get-text-no-location (:msg-info-obj (try (load-file "exceptions/compilation_errors/keyword_wrong_number_of_args.clj")
                       (catch Throwable e (prettify-exception e))))))

;; testing for :illegal-argument-parameters-must-be-in-vector
(expect #"Parameters for defn must be a vector, but my-argument was found instead\.(.*)"
        (get-all-text
        (run-and-catch-pretty-no-stacktrace 'intro.student '(defn my-function my-argument))))

(expect #"Parameters for defn must be a vector, but 5 was found instead\.(.*)"
        (get-all-text
        (run-and-catch-pretty-no-stacktrace 'intro.student '(defn my-function 5))))

(expect #"Parameters for defn must be a vector, but \+ was found instead\.(.*)"
        (get-all-text
        (run-and-catch-pretty-no-stacktrace 'intro.student '(defn my-function (+ x y)))))

(expect #"Parameters for defn must be a vector, but \+ was found instead\.(.*)"
        (get-all-text
        (run-and-catch-pretty-no-stacktrace 'intro.student '(defn my-function + x y))))

;; testing for :illegal-argument-exactly-2-forms
(expect #"The function when-let requires exactly 2 forms in binding vector. Line (.*) in the file intro.core"
        (get-all-text (run-and-catch-pretty-no-stacktrace 'intro.core '(when-let [num1 1 num2 2] "hello"))))

(expect "In function dissoc, the argument :s must be a map but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student '(dissoc :s))))

(expect "% must be either on its own or followed by a number or &."
       (get-text-no-location (:msg-info-obj (try (load-file "exceptions/compilation_errors/percent_followed_by_letter.clj")
                       (catch Throwable e (prettify-exception e))))))

;; testing for :index-out-of-bounds-index-not-provided
(expect "An index in a sequence is out of bounds or invalid."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.core '(nth [0 1 2 3 4 5] 10))))

;; testing for :arity-exception-wrong-number-of-arguments
(expect "You cannot pass three arguments to a function even?, need one."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '(even? 3 6 1))))

(expect "You cannot pass zero arguments to a function even?, need one."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '(even?))))

(expect "You cannot pass one argument to this anonymous function."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '(#(+ %1 %2) 1))))

(expect "You cannot pass four arguments to a function user-def-fcn."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '((defn user-def-fcn [x] (+ x 1))
    (user-def-fcn 1 2 3 4)))))

(expect "You cannot use the same key in a hash map twice, but you have duplicated the key :b."
        ;; note: let is needed in the test since the error must happen only at run time
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '(let [a :b b :b] {a 1 b 2}))))

;; testing for :unsupported-operation-wrong-type-of-argument
(expect "Function nth does not allow a map as an argument."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '(nth {:a 10 :z 4} 20))))

;;;; Elena: we might want to revisit this one
(expect #"Parameters for loop must come in pairs, but one of them does not have a match; on line (\d+) in the file intro\.core"
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '(defn s [s] (loop [s])))))

(expect "This recur is supposed to take zero arguments, but you are passing one." ;  this is giving NO_SOURCE_PATH
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '(recur (inc 1)))))

(expect "This recur is supposed to take one argument, but you are passing two." ;
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core '(loop [x 1] (recur (inc x) (dec x))))))

(expect "Parameters for cond must come in pairs, but one of them does not have a match." ; this is giving NO_SOURCE_PATH
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.core '(defn my-num [x] (cond (= 1 x))))))

(expect #"loop is a macro, cannot be passed to a function." ; this is giving NO_SOURCE_PATH
        (get-text-no-location  (run-and-catch-pretty-no-stacktrace 'intro.core '(defn my-happy [x] loop [x x]))))

(expect #"Name banana is undefined." ; this is giving NO_SOURCE_PATH
        (get-text-no-location  (run-and-catch-pretty-no-stacktrace 'intro.core '(banana 5 6))))

(expect (str "There is an unmatched delimiter ).\nFound in file unmatched_delimiter.clj" (line-number-format 3 20) ".")
        (get-all-text  (:msg-info-obj (prettify-exception (import-from-file "exceptions/unmatched_delimiter.ser")))))

(expect "Too many arguments to def." ; this is giving NO_SOURCE_PATH
        (get-text-no-location  (run-and-catch-pretty-no-stacktrace 'intro.core '(def my-var 5 6))))

(expect "Too few arguments to def." ; this is giving NO_SOURCE_PATH
        (get-text-no-location  (run-and-catch-pretty-no-stacktrace 'intro.core '(def))))
