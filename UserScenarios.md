###User Scenarios
####Day 1
Suzie starts her first “Hello World” program on the first day of class in a repl from our project. Here are all her code attempts:
* ``(print Hello World)``
  * Clojure message:
    * ``CompilerException java.lang.RuntimeException: Unable to resolve symbol: Hello in this context``
  * Our project message:
    * ``ERROR: Compilation error: name Hello is undefined, while compiling``
  * Hint/Explanation:
    * This breaks because Clojure thinks that Hello is a symbol (identifier) when Suzie	wanted it to be just plain text. Our program could respond with a suggestion that if the user wanted the phrase to be plain text to surround it with double quotes. It could also suggest to double-check that the user spelled the symbol correctly.

* ``(print 'Hello World')``
  * Clojure message:
    * ``CompilerException java.lang.RuntimeException: Unable to resolve symbol: World' in this context ``
  * Our project message:
    * ``ERROR: Compilation error: name World' is undefined, while compiling``
  * Hint/Explanation:
    * Be careful about your usage of single quotation marks, they do not behave the same as double quotes. Make sure your strings are wrapped in double quotes.

* `` print("Hello World")``
  * Clojure message:
    * ``ClassCastException java.lang.String cannot be cast to clojure.lang.IFn``
  * Our project message:
    * ``ERROR: Attempted to use a string, but a function was expected.``
  * Hint/Explanation:
    * This breaks because Clojure thinks the first thing after an open paren should be a function. Our program could respond with a suggestion to the user to make sure that the item right after the open paren is a function, otherwise check syntax.

* ``(print "Hello World")``
  * Hint/Explanation:
    * This works!


####Day 2
For the second day of Clojure, Jaden wants to define a function in a repl from within our project. Here are all his code attempts:
* ``(fn squareThis input*input)``
  * Clojure message:
    * ``IllegalArgumentException Parameter declaration input*input should be a vector``
  * Our project message:
    * ``ERROR: Parameters in defn should be a vector, but is input*input`` NOTE: WE ACTUALLY USED FN
  * Hint/Explanation:
    * In this particular case, fn was expecting a vector of parameters and it didn't find one. In the general case, something was looking for a collection and didn't find one.

* ``(fn squareThis [x] (* x x))``<br>
  ``(squareThis 5)``
  * Clojure message:
    * ``CompilerException java.lang.RuntimeException: Unable to resolve symbol: squareThis in this context``
  * Our project message:
    * ``ERROR: Compilation error: name squareThis is undefined, while compiling``
  * Hint/Explanation:
    * If you want squareThis to be a function that you can reference later, use defn instead of fn.

* ``(defn squareThis [x] (* x x))``<br>
  ``(squareThis 5)``
  * Hint/Explanation:
    * This works!

####Day 3
On the third day of Clojure, Laken is super-excited to start working with lists! She has a list of her favorite bands and she wants to add a new one. Here are her attempts to do this in Clojure:

* ``(conj "ACDC" ("Daft Punk" "U2" "ZZ Top"))``
  * Clojure message:
    * ``ClassCastException java.lang.String cannot be cast to clojure.lang.IFn``
  * Our project message:
    * ``ERROR: Attempted to use a string, but a function was expected.``
  * Hint/Explanation:
    * Be careful about what comes immediately after an open paren. Clojure might think that it is a function name.
    Example: ("add" 5 3) (first argument would be dynamic depending on what type the exception complains about

* ``(conj "ACDC" '("Daft Punk" "U2" "ZZ Top"))``
  * Clojure message:
    * ``ClassCastException java.lang.String cannot be cast to clojure.lang.IPersistentCollection``
  * Our project message:
    * ``ERROR: Attempted to use a string, but a collection was expected.``
  * Hint/Explanation:
    * Check that the order of arguments for a function are in the correct order.

* ``(conj '("Daft Punk" "U2" "ZZ Top") "ACDC")``
  * Hint/Explanation:
    * This works!

####Day 4
On the fourth day of Clojure, Keylan is a little bit nervous to work with Maps. He wants to update a value associated with a key in a map he has created. Here are his attempts:

* ``(assoc :a 3 {:a 5, :b 8, :c 9})``
  * Clojure message:
    * ``ClassCastException clojure.lang.Keyword cannot be cast to clojure.lang.Associative``
  * Our project message:
    * ``ERROR: Attempted to use a keyword, but a map or a vector was expected.``
  * Hint/Explanation:
    * Check that the order of arguments for a function are in the correct order.

* ``(assoc (:a 3) {:a 5, :b 8, :c 9})``
  * Clojure message:
    * ``ArityException Wrong number of args (2) passed to: core/assoc``
  * Our project message:
    * ``ERROR: Wrong number of arguments (2) passed to a function assoc``
  * Hint/Explanation:
    * Check the documentation of the function to see how many arguments are required. 
     (possibly adding links to the documentation to the error message)

* ``(assoc {:a 3} {:a 5, :b 8, :c 9})``
  * Clojure message:
    * ``ArityException Wrong number of args (2) passed to: core/assoc``
  * Our project message:
    * ``ERROR: Wrong number of arguments (2) passed to a function assoc``
  * Hint/Explanation:
    * Check the documentation of the function to see how many arguments are required. 

* ``(assoc {:a 5, :b 8, :c 9} :a 3)``
  * Hint/Explanation:
    * This works!

####Day 5
On the fifth day of Clojure, Addison needs to write a function which takes a sequence and returns the maximum value from the sequence without using max or key-max. Here are Addison's attempts:

* ``(defn [coll] penultimate (last (drop-last coll)))``
  * Clojure message:
    * ``IllegalArgumentException First argument to defn must be a symbol``
  * Our project message:
    * ``ERROR: First argument to defn must be a symbol``
  * Hint/Explanation:
    * 

* ``(defn penultimate [coll] (last (drop-last coll)))``<br>
  ``(penultimate [3 4 5 8 9 0 3])``
  * Hint/Explanation:
    * This works!
 
####Day 6
On the sixth day of Clojure, Darwin has been given the task to write a print statement that prints out a list of numbers using loop and he needs some help. Here are Darwin's attempts:

* ``(loop [x 10] (while (> x 1) (println x) (recur (- x 1))))``
  * Clojure message:
    * ``CompilerException java.lang.UnsupportedOperationException: Can only recur from tail position``
  * Our project message:
    * ``ERROR: Compilation error: recur can only occur as a tail call, meaning no operations can be done after its return, while compiling``
  * Hint/Explanation:
    * 

* ``(loop [x 10] (if (= x 0) x (do (println x) (recur (- x 1)))))``
  * Clojure message:
    * No error message given
  * Our project message:
    * ``ERROR: An index in a sequence is out of bounds. The index is: 10``
  * Hint/Explanation
    * This works????? (I think??)

####Day 7
On the seventh day of Clojure, Geoff has been given the task to write a factorial function that uses recurion and he needs help. Here are his attempts:

* ``(defn factorial [input] (* input (factorial (- input 1))))``<br>
  ``(factorial 5)``
  * Clojure message:
    * ``StackOverflowError   clojure.lang.Numbers$LongOps.negate (Numbers.java:518)``
  * Our project message:
    * ``Exception in thread "main" java.lang.ClassCastException: java.lang.String cannot be cast to java.util.regex.Pattern``
  * Hint/Explanation:
    * You may have forgetten a base case for your recursive/looping function.

* ``(defn factorial [input] (if (>= input 1) (* input (factorial (- input 1))) 1))``<br>
  ``(factorial 5)``<br>
  ``-> 120``
  * Hint/Explanation
    * This works!

####Day 8
On the eighth day of Clojure, Symmetra has been given the task to find the square root of the first odd number in a collection. Here are her attempts:

* ``(defn first-odd-sqrt [my-collection] (Math/sqrt (first (filter odd? my-collection))))``<br>
  ``(first-odd-sqrt [2, 4])``
  * Clojure message:
    * ``NullPointerException   clojure.lang.RT.doubleCast (RT.java:1222)``
  * Our project message:
    * ``ERROR: An attempt to access a non-existing object. (NullPointerException)``
  * Hint/Explanation:
    * One of the functions you are using might be returning nil for some reason. Try to evaluate the function calls from the inside out to see where this might be occuring.

* ``(defn first-odd-sqrt [my-collection] (Math/sqrt (filter odd? my-collection)))``<br>
  ``(first-odd-sqrt [2, 3, 9, 12, 5])``
  * Clojure message:
    * ``ClassCastException clojure.lang.LazySeq cannot be cast to java.lang.Number``
  * Our project message:
    * ``ERROR: Attempted to use a sequence, but a number was expected.``
  * Hint/Explanation:
    * 
