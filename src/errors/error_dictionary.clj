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
    :make-msg-info-obj (fn [matches] (process-asserts-obj (nth matches 2)))}

   {:key :assertion-error-without-argument
    :class AssertionError
    :match #"Assert failed: \((.*)\)"
    :make-msg-info-obj (fn [matches] (process-asserts-obj nil))}

   ;#############################
   ;### Class Cast Exceptions ###
   ;#############################

   {:key :class-cast-exception-cannot-cast-to-map-entry
    :class ClassCastException
    :match #"(.*) cannot be cast to java.util.Map\$Entry(.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Attempted to create a map using "
                                                           (get-type (nth matches 1)) :type
                                                           ", but a sequence of vectors of length 2 or a sequence of maps is needed."))}
   {:key :class-cast-exception
    :class ClassCastException
    :match #"(.*) cannot be cast to (.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Attempted to use "
                                                           (get-type (nth matches 1)) :type ", but "
                                                           (get-type (nth matches 2)) :type " was expected."))}

   ;###################################
   ;### Illegal Argument Exceptions ###
   ;###################################

   {:key :illegal-argument-no-val-supplied-for-key
    :class IllegalArgumentException
    :match #"No value supplied for key: (.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "No value found for key "
                                                           ; is this too wordy?
                                                           ;(nth matches 1) :arg ". Every key must be paired with a value; the value should be immediately following the key."))
                                                           (nth matches 1) :arg ". Every key for a hash-map must be followed by a value."))}
   {:key :illegal-argument-vector-arg-to-map-conj
    :class IllegalArgumentException
    :match #"Vector arg to map conj must be a pair(.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Each inner vector must be a pair: a key followed by a value."))}

   {:key :illegal-argument-cannot-convert-type
    :class IllegalArgumentException
    :match #"Don't know how to create (.*) from: (.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Don't know how to create "
                                                           (get-type (nth matches 1)) :type
                                                           " from "(get-type (nth matches 2)) :type "."))}
   {:key :illegal-argument-even-number-of-forms
    :class IllegalArgumentException
    :match #"(.*) requires an even number of forms"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Parameters for " (nth matches 1) :arg " must come in pairs, but one of them does not have a match."))}

   {:key :illegal-argument-even-number-of-forms-in-binding-vector
    :class IllegalArgumentException
    :match #"(.*) requires an even number of forms in binding vector in (.*):(.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Parameters for " (nth matches 1) :arg
                                                           " must come in pairs, but one of them does not have a match; on line "
                                                           (nth matches 3) " in the file " (nth matches 2)))}

   {:key :illegal-argument-needs-vector-when-binding
    :class IllegalArgumentException
    :match #"(.*) requires a vector for its binding in (.*):(.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "When declaring a " (nth matches 1)
                                                           ", you need to pass it a vector of arguments. "
                                                           ; we need to make this consistent with file/line reporting for comp. error.
                                                           "Line "
                                                           (nth matches 3) " in the file " (nth matches 2)))}
   {:key :illegal-argument-type-not-supported
    :class IllegalArgumentException
    :match #"(.*) not supported on type: (.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Function " (nth matches 1) :arg
                                                           " does not allow " (get-type (nth matches 2)) :type " as an argument."))}
   {:key :illegal-argument-parameters-must-be-in-vector
    :class IllegalArgumentException
    :match #"Parameter declaration (.*) should be a vector"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Parameters in " "defn" :arg
                                                           " should be a vector, but is " (nth matches 1) :arg "."))}
   {:key :illegal-argument-exactly-2-forms
    :class IllegalArgumentException
    :match #"(.*) requires exactly 2 forms in binding vector in (.*):(.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "The function " (nth matches 1) :arg
                                                           " requires exactly 2 forms in binding vector. "
                                                           ; we need to make this consistent with file/line reporting for comp. error.
                                                           "Line "
                                                           (nth matches 3) " in the file " (nth matches 2)))}

   {:key :duplicate-key-hashmap ;example: {1 2 1 3}
    :class IllegalArgumentException
    :match #"Duplicate key: (.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "You cannot use the same key in a hash map twice, but you have duplicated the key "
                                                           (nth matches 1) :arg "."))}

  {:key :cant-call-nil
    :class IllegalArgumentException
    :match #"Can't call nil(.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Cannot call "
                                                           "nil" :arg " as a function."))}


   {:key :compiler-exception-wrong-number-of-arguments-to-recur
    :class IllegalArgumentException
    :match #"Mismatched argument count to recur, expected: (.*) args, got: (.*)"
    :make-msg-info-obj (fn [matches]
                         (let [first-arg (nth matches 1)
                               sec-arg (nth matches 2)
                               arg-args (if  (= "1" (nth matches 1)) " argument" " arguments")]
                         (make-msg-info-hashes "This recur is supposed to take "
                                               (number-word first-arg) arg-args
                                               ", but you are passing "
                                               (number-word sec-arg) ".")))
    :hints "1. You are passing a wrong number of arguments to recur. Check its function or loop.\n
    2. recur might be outside of the scope of its function or loop."}

   ;######################################
   ;### Index Out of Bounds Exceptions ###
   ;######################################

   {:key :index-out-of-bounds-index-provided
    :class IndexOutOfBoundsException
    :match #"(\d+)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "An index in a sequence is out of bounds."
                                                           " The index is: " (nth matches 0) :arg  "."))}

   {:key :string-index-out-of-bounds
    :class StringIndexOutOfBoundsException
    :match #"String index out of range: (\d+)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Position " (nth matches 1) :arg " is outside of the string."))
    :hint "String positions start at zero, so there is no character at a position equal to the string length.
    Example: a string \"hi\" does not have a character at position 2. \n
    Also the string may be empty, in this case accessing any position in it gives this error."}

   {:key :index-out-of-bounds-index-not-provided
    :class IndexOutOfBoundsException
    :match #"" ; an empty message
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "An index in a sequence is out of bounds or invalid."))}

   ;########################
   ;### Arity Exceptions ###
   ;########################

   {:key :arity-exception-wrong-number-of-arguments
    :class clojure.lang.ArityException
    :match #"Wrong number of args \((.*)\) passed to: (.*)"
    :make-msg-info-obj (fn [matches]
                         (let [fstr  (clojure.string/replace (get-function-name (nth matches 2)) #"--(\d+)$" "")
                               arity (lookup-arity  fstr)
                               ;; now the message doesn't make sense for anonymous functions, need a special case?
                               funstr (if (= fstr "anonymous function")
                                        "this "
                                        (str "a function "))
                               ;;corrects plurality of argument(s)
                               arg-args (if  (= "1" (nth matches 1))
                                          " argument"
                                          " arguments")]
                           (make-msg-info-hashes "You cannot pass "
                                                 ;; fix plural/singular
                                                 (number-word (nth matches 1)) arg-args " to " funstr fstr :arg
                                                 (if arity (str ", need " arity) "")
                                                 ".")))}

   ;###############################
   ;### Null Pointer Exceptions ###
   ;###############################

   {:key :null-pointer-non-existing-object-provided
    :class NullPointerException
    :match #"(.+)" ; for some reason (.*) matches twice. Since we know there is at least one symbol, + is fine
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "An attempt to access a non-existing object: "
                                                           (nth matches 1) :arg " (NullPointerException)."))}
   {:key :null-pointer-non-existing-object-not-provided
    :class NullPointerException
    :match  #""
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "An attempt to access a non-existing object (NullPointerException)."))}

   ;########################################
   ;### Unsupported Operation Exceptions ###
   ;########################################

   {:key :unsupported-operation-wrong-type-of-argument
    :class UnsupportedOperationException
    :match #"(.*) not supported on this type: (.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Function " (nth matches 1) :arg
                                                           " does not allow " (get-type (nth matches 2)) :type " as an argument."))}

   {:key :compiler-exception-must-recur-from-tail-position
    :class java.lang.UnsupportedOperationException
    :match #"(.*)Can only recur from tail position(.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Recur can only occur "
                                                           "as a tail call: no operations can"
                                                           " be done after its return."))}

   ;##############################
   ;### ClassNotFoundException ###
   ;##############################

   {:key :class-not-found-exception
    :class ClassNotFoundException
    :match #"(.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Name " (nth matches 1) :arg " is undefined."))
    :hint "If you are using functions from another file, make sure you use dots for namespaces and slashes for functions, such as clojure.string/split."}

   ;##############################################
   ;### Runtime Exceptions ###
   ;##############################################

   {:key :compiler-exception-first-argument-must-be-symbol
    :class java.lang.RuntimeException
    :match #"First argument to (.*) must be a Symbol(.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes (nth matches 1) :arg " must be followed by a name."))}

   {:key :compiler-exception-cannot-take-value-of-macro
    :class java.lang.RuntimeException
    :match #"Can't take value of a macro: (.+)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes
                                                           (get-macro-name (nth matches 1)) :arg
                                                           " is a macro, cannot be passed to a function."))}
   {:key :compiler-exception-cannot-resolve-symbol
    :class java.lang.RuntimeException
    :match #"Unable to resolve symbol: (.+) in this context"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Name "
                                                           (nth matches 1) :arg " is undefined."))}
   {:key :compiler-exception-map-literal-even
    :class java.lang.RuntimeException
    :match #"Map literal must contain an even number of forms"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "A hash map must consist of key/value pairs; you have a key that's missing a value."))}

   {:key :compiler-exception-unmatched-delimiter
    :class java.lang.RuntimeException
    :match #"Unmatched delimiter: (.+)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "There is an unmatched delimiter " (nth matches 1) :arg "."))}

   {:key :compiler-exception-too-many-arguments
    :class java.lang.RuntimeException
    :match #"Too many arguments to (.+)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Too many arguments to "
                                                           (nth matches 1) :arg "."))}
   {:key :compiler-exception-too-few-arguments
    :class java.lang.RuntimeException
    :match #"Too few arguments to (.+)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Too few arguments to "
                                                           (nth matches 1) :arg "."))}

   {:key :compiler-exception-end-of-file
    :class java.lang.RuntimeException
    :match #"EOF while reading, starting at line (.+)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "End of file, starting at line " (nth matches 1) :arg
                                                           ".\nProbably a non-closing parenthesis or bracket."))}
   ;; Order mattters: do not re-order this one and :compiler-exception-no-such-var-no-namespace
   {:key :compiler-exception-no-such-var-with-namespace
    :class java.lang.RuntimeException
    :match #"No such var: (.+)/(.+)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Name " (nth matches 2) :arg " does not exist in the namespace "
                                                           (nth matches 1) :arg "."))}

   ;; Order mattters: do not re-order this one and :compiler-exception-no-such-var-with-namespace
   ;; I don't know a case for this, but it's here in case a no-such-var error happens with no namespace info
   {:key :compiler-exception-no-such-var-no-namespace
    :class java.lang.RuntimeException
    :match #"No such var: (.+)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "Name " (nth matches 1) :arg " is undefined."))}

   {:key :compiler-exception-no-such-namespace
    :class java.lang.RuntimeException
    :match #"No such namespace: (.+)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "The namespace " (nth matches 1) :arg " does not exist or is not accessible in your program."))
    ;; We might also need to include a hint for accidentally using a dot or a slash
    :hint "1. If you are using functions from other namespaces, make sure you use 'refer' in the beginning of your file to include the namespace.\n
    2. Check the spelling of namespaces you might be using, such as clojure.string\n
    3. Make sure that your namespace uses dots, and the function in the namespaces is separated by a slash: clojure.string/split,
    where clojure.string is the namespace, and split is the function."}

   ;################################################
   ;### clojure.lang.LispReader$ReaderException ####
   ;################################################

   ;; Apparently the cause of an unmatched delimeter may be clojure.lang.LispReader$ReaderException

   {:key :lisp-reader-exception-unmatched-delimiter
    :class clojure.lang.LispReader$ReaderException
    :match #"(.*)Unmatched delimiter: (.+)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "There is an unmatched delimiter " (nth matches 2) :arg "."))}



   ;############################
   ;### Stack Overflow Error ###
   ;############################

   {:key make-mock-preobj
    :class StackOverflowError
    :match "????????"
    :make-msg-info-obj make-mock-preobj}

   ;#######################
   ;### Java Exceptions ###
   ;#######################

   {:key :java.lang.Exception-improper-identifier
    :class java.lang.Exception
    :match #"Unsupported binding form: (.*)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "You cannot use " (nth matches 1) :arg
                                                           " as a variable."))}


   ;#######################################################
   ;### Compilation Errors: Illegal Argument Exceptions ###
   ;#######################################################

   ;;; These probably need to be removed, too - Elena

   {:key :compiler-exception-even-numbers-in-binding-vector
    :class clojure.lang.Compiler$CompilerException
    :true-exception java.lang.IllegalArgumentException
    :match #"(.+): (.+) requires an even number of forms in binding vector in (.+):(.+)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes (nth matches 2)
                                                           " requires an even number of forms in binding vector."))}


   {:key :compiler-exception-even-number-of-forms-needed
    :class clojure.lang.Compiler$CompilerException
    :true-exception java.lang.IllegalArgumentException
    :match #"(.*): (.*) requires an even number of forms, compiling:\((.+)\)"
    :make-msg-info-obj (fn [matches] (make-msg-info-hashes "There is an unmatched parameter in declaration of "
                                                           (nth matches 2) :arg "."))}


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
                           (make-msg-info-hashes "Wrong number of arguments ("
                                                 (nth matches 2) ") passed to " funstr fstr :arg ".")))}

   ])

