(ns errors.error_hints)

(def hints
  {:class-cast-exception "Attempted to use a <type1>, but a <type2> was expected.
                             The error happens when a function's argument is not of the type for which the function is defined.
                             For instance, (+ 1 \"2\") results in this error because \"2\" is not a number, but a string.
                             Likewise, (< 1 :two) would cause this error because :two is a keyword, and not a number. "
					;This error may happen if you were using a variable and had a wrong value in the variable
      ; or if you have switched the order of arguments in a function.

    ;                      Error example for (+ 1 :two):\n
    ;                      \"The + function on line 7 was expecting a number but was given a keyword.\"\n
    ;                      Error example for(< 8 \"12\"):\n
    ;                      \"The < function on line 52 was expecting a number but was given a string.\"\n
    ;                      Error example for (num \"ten\"):\n
    ;                      \"The num function on line 42 was expecting a number but was given a string.\""
   :illegal-argument-cannot-convert-type "The ___ function on line ___ was expecting ___ but was given ___.
                                             Error example for (cons 1 2):
                                             \"The cons function on line 4 was expecting a sequence but was given a number.\"
                                             Error example for (into {} [1 2 3]):
                                             \"The into function on line 7 was expecting a vector of vectors but was given a vector.\""
   :index-out-of-bounds-index-not-provided "Trying to look at a certain location that doesn’t exist in a specific collection.
                                              Error example for (nth [1 2 3] 5):
                                              \"Trying to look at a certain location that doesn’t exist in a specific collection.\""
   :arity-exception-wrong-number-of-arguments "Make sure you have the correct number of arguments.
                                                 Error example for (first [2 1] [3 6]):
                                                 \"Make sure you have the correct number of arguments.\"
                                                 Error example for (conj 2):
                                                 \"Make sure you have the correct number of arguments.\""
   :compiler-exception-cannot-resolve-symbol "____ is undefined; the compiler doesn’t know what it means. See below for possible mistakes.
                                                Cases:
	                                              \t -you wanted it to be a string:
                                                \t \t If you want ___ to be a string (words), put quotes around it:
                                                \t \t \"hello\" instead of hello
	                                              \t -you wanted it to be a variable
                                                \t \t If you want ___ to be a variable (name for a value), define the value:
                                                \t \t (def hello 8) instead of hello
	                                              \t -you wanted it to be a keyword
                                                \t \t If you want ___ to be a keyword, put a colon in front of it:
                                                \t \t :hello instead of hello
	                                              \t -you mistyped a higher-order function
                                                \t \t Look back at your code for spelling errors:(first [2 3]) instead of (firST [2 3])"

  ;; We might also need to include a hint for accidentally using a dot or a slash
   :compiler-exception-no-such-namespace "1. If you are using functions from other namespaces, make sure you use 'refer' in the beginning of your file to include the namespace.\n
    2. Check the spelling of namespaces you might be using, such as clojure.string\n
    3. Make sure that your namespace uses dots, and the function in the namespaces is separated by a slash: clojure.string/split,
    where clojure.string is the namespace, and split is the function."

   :class-not-found-exception "If you are using functions from another file, make sure you use dots for namespaces and slashes for functions, such as clojure.string/split."

   :string-index-out-of-bounds "String positions start at zero, so there is no character at a position equal to the string length.
    Example: a string \"hi\" does not have a character at position 2. \n
    Also the string may be empty, in this case accessing any position in it gives this error."

   :compiler-exception-wrong-number-of-arguments-to-recur "1. You are passing a wrong number of arguments to recur. Check its function or loop.\n
    2. recur might be outside of the scope of its function or loop."

   :null-pointer-non-existing-object-provided "Put hint here"
  }
)
