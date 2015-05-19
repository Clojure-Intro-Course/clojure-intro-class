(ns errors.error_dictionary
  (:use [corefns.corefns]
        [errors.messageobj]
        [corefns.failed_asserts_info]
        [errors.dictionaries]))

(def error-dictionary
  [;########################
   ;### Assertion Errors ###
   ;########################

   {:key :assertion-error-with-argument
    :class AssertionError
    :match #"Assert failed: \((.*) argument(.*)\)"
    :make-msg-info-obj (fn [matches] (process-asserts-obj (nth matches 2)))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :runtime})}

   {:key :assertion-error-without-argument
    :class AssertionError
    :match #"Assert failed: \((.*)\)"
    :make-msg-info-obj (fn [matches] (process-asserts-obj nil))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :runtime})}

   ;#############################
   ;### Class Cast Exceptions ###
   ;#############################

   {:key :class-cast-exception-cannot-cast-to-map-entry
    :class ClassCastException
    :match #"(.*) cannot be cast to java.util.Map\$Entry(.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Attempted to create a map using "
                                                           (get-type (nth matches 1)) :type
                                                           ", but a sequence of vectors of length 2 or a sequence of maps is needed."))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :runtime})}
   {:key :class-cast-exception
    :class ClassCastException
    :match #"(.*) cannot be cast to (.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Attempted to use "
                                                           (get-type (nth matches 1)) :type ", but "
                                                           (get-type (nth matches 2)) :type " was expected."))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}

   ;###################################
   ;### Illegal Argument Exceptions ###
   ;###################################

   {:key :illegal-argument-no-val-supplied-for-key
    :class IllegalArgumentException
    :match #"No value supplied for key: (.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "No value found for key "
                                                           (nth matches 1) :arg ". Every key must be paired with a value; the value should be immediately following the key."))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}
   {:key :illegal-argument-vector-arg-to-map-conj
    :class IllegalArgumentException
    :match #"Vector arg to map conj must be a pair(.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "All the inner vectors in the outer collection must have length two."))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}
   {:key :illegal-argument-cannot-convert-type
    :class IllegalArgumentException
    :match #"Don't know how to create (.*) from: (.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Don't know how to create "
                                                           (get-type (nth matches 1)) :type
                                                           " from "(get-type (nth matches 2)) :type))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}
   {:key :illegal-argument-even-number-of-forms
    :class IllegalArgumentException
    :match #"(.*) requires an even number of forms"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "There is an unmatched parameter in declaration of "
                                                           (nth matches 1) :arg))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}
   {:key :illegal-argument-even-number-of-forms-in-binding-vector
    :class IllegalArgumentException
    :match #"(.*) requires an even number of forms in binding vector in (.*):(.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "A parameter for a " (nth matches 1)
                                                           " is missing a binding on line "
                                                           (nth matches 3) " in the file " (nth matches 2)))
    :exc-location (fn [matches] {:path :unknown, :filename (nth matches 2), :line (read-string (nth matches 3)), :character :unknown, :exception-type :runtime})}

   {:key :illegal-argument-needs-vector-when-binding
    :class IllegalArgumentException
    :match #"(.*) requires a vector for its binding in (.*):(.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "When declaring a " (nth matches 1)
                                                           ", you need to pass it a vector of arguments. Line "
                                                           (nth matches 3) " in the file " (nth matches 2)))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}
   {:key :illegal-argument-type-not-supported
    :class IllegalArgumentException
    :match #"(.*) not supported on type: (.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Function " (nth matches 1) :arg
                                                           " does not allow " (get-type (nth matches 2)) :type " as an argument"))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}
   {:key :illegal-argument-parameters-must-be-in-vector
    :class IllegalArgumentException
    :match #"Parameter declaration (.*) should be a vector"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Parameters in " "defn" :arg
                                                           " should be a vector, but is " (nth matches 1) :arg))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}
   {:key :illegal-argument-exactly-2-forms
    :class IllegalArgumentException
    :match #"(.*) requires exactly 2 forms in binding vector in (.*):(.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "The function " (nth matches 1) :arg
                                                           " requires exactly 2 forms in binding vector. Line "
                                                           (nth matches 3) " in the file " (nth matches 2)))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}

   ;######################################
   ;### Index Out of Bounds Exceptions ###
   ;######################################

   {:key :index-out-of-bounds-index-provided
    :class IndexOutOfBoundsException
    :match #"(\d+)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "An index in a sequence is out of bounds."
                                                           " The index is: " (nth matches 0) :arg))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}
   {:key :index-out-of-bounds-index-not-provided
    :class IndexOutOfBoundsException
    :match #"" ; an empty message
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "An index in a sequence is out of bounds or invalid"))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}

   ;########################
   ;### Arity Exceptions ###
   ;########################

   {:key :arity-exception-wrong-number-of-arguments
    :class clojure.lang.ArityException
    :match #"Wrong number of args \((.*)\) passed to: (.*)"
    :make-msg-info-obj (fn [matches]
                         (let [fstr (get-function-name (nth matches 2))
                               funstr (if (= fstr "anonymous function")
                                        "an "
                                        (str "a function "))]
                           (make-msg-info-hashes "Wrong number of arguments ("
                                                 (nth matches 1) ") passed to " funstr fstr :arg)))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}

   ;###############################
   ;### Null Pointer Exceptions ###
   ;###############################

   {:key :null-pointer-non-existing-object-provided
    :class NullPointerException
    :match #"(.+)" ; for some reason (.*) matches twice. Since we know there is at least one symbol, + is fine
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "An attempt to access a non-existing object: "
                                                           (nth matches 1) :arg "\n(NullPointerException)"))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}
   {:key :null-pointer-non-existing-object-not-provided
    :class NullPointerException
    :match  #""
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "An attempt to access a non-existing object. \n(NullPointerException)"))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}

   ;########################################
   ;### Unsupported Operation Exceptions ###
   ;########################################

   {:key :unsupported-operation-wrong-type-of-argument
    :class UnsupportedOperationException
    :match #"(.*) not supported on this type: (.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Function " (nth matches 1) :arg
                                                           " does not allow " (get-type (nth matches 2)) :type " as an argument"))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}

   ;############################
   ;### Stack Overflow Error ###
   ;############################

   {:key make-mock-preobj
    :class StackOverflowError
    :match "????????"
    :make-msg-info-obj make-mock-preobj
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}

   ;#######################
   ;### Java Exceptions ###
   ;#######################

   {:key :java.lang.Exception-improper-identifier
    :class java.lang.Exception
    :match #"Unsupported binding form: (.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "You cannot use " (nth matches 1) :arg
                                                           " as a variable."))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}

   ;################################################
   ;### Compilation Errors: Class Cast Exception ###
   ;################################################

   {:key :compiler-class-cast-exception-compiling
    :class clojure.lang.Compiler$CompilerException
    :match #"(.+): (.*) cannot be cast to (.*), compiling:(.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Attempted to use "
                                                           (get-type (nth matches 2)) :type ", but "
                                                           (get-type (nth matches 3)) :type " was expected."
                                                           ", while compiling " (nth matches 4)))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}

   ;#######################################################
   ;### Compilation Errors: Illegal Argument Exceptions ###
   ;#######################################################

   {:key :compiler-exception-even-numbers-in-binding-vector
    :class clojure.lang.Compiler$CompilerException
    :true-exception java.lang.IllegalArgumentException
    :match #"(.+): (.+) requires an even number of forms in binding vector in (.+):(.+), compiling:(.+)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Compilation error: " (nth matches 2)
                                                           " requires an even number of forms in binding vector, while compiling "
                                                           (nth matches 3)))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}

   {:key :compiler-exception-wrong-number-of-arguments-to-recur
    :class clojure.lang.Compiler$CompilerException
    :true-exception java.lang.IllegalArgumentException
    :match #"(.*) Mismatched argument count to recur, expected: (.*) args, got: (.*), compiling:(.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Compilation error: this recur is supposed to take "
                                                           (nth matches 2) " arguments, but you are passing " (nth matches 3)
                                                           ", while compiling " (nth matches 4)))
    ;;TODO: handle singular/plural arguments
    :hints "1. You are passing a wrong number of arguments to recur. Check its function or loop.
    2. recur might be outside of the scope of its function or loop"
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}
   {:key :compiler-exception-even-number-of-forms-needed
    :class clojure.lang.Compiler$CompilerException
    :true-exception java.lang.IllegalArgumentException
    :match #"(.*): (.*) requires an even number of forms, compiling:\((.+)\)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Compilation error: there is an unmatched parameter in declaration of "
                                                           (nth matches 2) :arg ", while compiling: " (nth matches 3)))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}

   ;############################################
   ;### Compilation Errors: Arity Exceptions ###
   ;############################################

   {:key :compiler-exception-wrong-number-of-arguments
    :class clojure.lang.Compiler$CompilerException
    :true-exception clojure.lang.ArityException
    :match #"(.+): Wrong number of args \((.*)\) passed to: (.*), compiling:(.*)"
    :make-msg-info-obj (fn [matches]
                         (let [fstr (get-function-name (nth matches 3))
                               funstr (if (= fstr "anonymous function")
                                        "an "
                                        (str "a function "))]
                           (make-msg-info-hashes "Compilation error: wrong number of arguments ("
                                                 (nth matches 2) ") passed to " funstr fstr :arg
                                                 ", while compiling "
                                                 (nth matches 4) :arg)))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}

   ;############################################################
   ;### Compilation Errors: Unsupported Operation Exceptions ###
   ;############################################################

   {:key :compiler-exception-must-recur-from-tail-position
    :class clojure.lang.Compiler$CompilerException
    :true-exception java.lang.UnsupportedOperationException
    :match #"(.*) Can only recur from tail position, compiling:(.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Compilation error: recur can only occur "
                                                           "as a tail call, meaning no operations can"
                                                           " be done after its return, while compiling "
                                                           (nth matches 2)))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}

   ;##############################################
   ;### Compilation Errors: Runtime Exceptions ###
   ;##############################################

   {:key :compiler-exception-first-argument-must-be-symbol
    :class clojure.lang.Compiler$CompilerException
    :true-exception java.lang.RuntimeException
    :match #"(.*) First argument to (.*) must be a Symbol, compiling:\((.+)\)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Compilation error: "
                                                           (nth matches 2) :arg " must be followed by a name, while compiling "
                                                           (nth matches 3)))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}
   {:key :compiler-exception-cannot-take-value-of-macro
    :class clojure.lang.Compiler$CompilerException
    :true-exception java.lang.RuntimeException
    :match #"(.+): Can't take value of a macro: (.+), compiling:\((.+)\)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Compilation error: "
                                                           (get-macro-name (nth matches 2)) :arg
                                                           " is a macro, cannot be passed to a function, while compiling "
                                                           (nth matches 3)))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}
   {:key :compiler-exception-cannot-resolve-symbol
    :class clojure.lang.Compiler$CompilerException
    :true-exception java.lang.RuntimeException
    :match #"(.+): Unable to resolve symbol: (.+) in this context, compiling:\((.+)\)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Compilation error: " "name "
                                                           (nth matches 2) :arg " is undefined, while compiling "
                                                           (nth matches 3) :arg))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}

   ;###########################################
   ;### Compilation Errors: Java Exceptions ###
   ;###########################################

   {:key :compiler-exception-unmatched-delimiter
    :class clojure.lang.Compiler$CompilerException
    :true-exception java.lang.Exception
    :match #"(.+): Unmatched delimiter: (.+), compiling:(.+)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Compilation error: there is an unmatched delimiter " (nth matches 2) :arg
                                                           ", while compiling " (nth matches 3) :arg))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}
   {:key :compiler-exception-too-many-arguments
    :class clojure.lang.Compiler$CompilerException
    :true-exception java.lang.Exception
    :match #"(.+): Too many arguments to (.+), compiling:(.+)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Compilation error: too many arguments to "
                                                           (nth matches 2) :arg ", while compiling "
                                                           (nth matches 3) :arg))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}
   {:key :compiler-exception-too-few-arguments
    :class clojure.lang.Compiler$CompilerException
    :true-exception java.lang.Exception
    :match #"(.+): Too few arguments to (.+), compiling:(.+)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Compilation error: too few arguments to "
                                                           (nth matches 2) :arg  ", while compiling "
                                                           (nth matches 3) :arg))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}

   ;###################################
   ;### Compilation Errors: Unknown ###
   ;###################################

   {:key :compiler-exception-end-of-file
    :class clojure.lang.Compiler$CompilerException
    :true-exception :unknown
    :match #"(.+): EOF while reading, starting at line (.+), compiling:(.+)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Compilation error: end of file, starting at line " (nth matches 2) :arg
                                                           ", while compiling " (nth matches 3) :arg
                                                           ".\nProbably a non-closing parenthesis or bracket."))
    :exc-location (fn [matches] {:path :unknown, :filename :unknown, :line :unknown, :character :unknown, :exception-type :unknown})}
   ])

;; This is probably somewhat fragile: it occurs in an unbounded recur, but
;; may occur elsewhere. We need to be careful to not catch a wider rnage of exceptions:
; {:key :compiler-exception-must-recur-to-function-or-loop
;  :class clojure.lang.Compiler$CompilerException
;  :true-exception make-mock-preobj
;  :match #"(.*): clojure.lang.Var\$Unbound cannot be cast to clojure.lang.IPersistentVector, compiling:(.*)"
;  :make-msg-info-obj (fn [matches] (make-msg-info-hashes "recur" :arg
;                       " does not refer to any function or loop."
;                       " Compiling " (nth matches 2)))}
