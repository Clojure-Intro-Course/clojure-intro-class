###User Scenarios
####Day 1
Suzie starts her first “Hello World” program on the first day of class in a repl from our project. Here are all her code attempts:
* ``(print Hello World)``
  * Clojure message:
    * ``java.lang.Exception: Unable to resolve symbol: Hello in this context``
  * Our project message:
    * Currently not handling this type of error
  * Hint/Explanation:
    * This breaks because Clojure thinks that Hello is a symbol (identifier) when Suzie 	wanted it to be just plain text. Our program could respond with a suggestion that if the user wanted the phrase to be plain text to surround it with double quotes. It could also suggest to double-check that the user spelled the symbol correctly.

* ``(print 'Hello World')``
  * Clojure message:
    * ``java.lang.Exception: Unmatched delimiter: )``
  * Our project message:
    * Currently not handling this type of error
  * Hint/Explanation:
    * Work on THIS

* ``print("Hello World")``
  * Clojure message:
    * ``java.lang.ClassCastException: java.lang.String cannot be cast to clojure.lang.IFn``
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
    * ``java.lang.RuntimeException: java.lang.UnsupportedOperationException: nth not supported on this type: Symbol``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * In this particular case, fn was expecting a vector of parameters and it didn't find one. In the general case, something was looking for a collection and didn't find one.

* ``(fn squareThis [x] (* x x))``
    
  ``(squareThis 5)``
  * Clojure message:
    * ``java.lang.Exception: Unable to resolve symbol: squareThis in this context``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 

* ``(defn squareThis [x] (* x x))``
  * Hint/Explanation:
    * This works!

####Day 3

* ``(conj "ACDC" ("Daft Punk" "U2" "ZZ Top"))``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 

* ``(conj "ACDC" '("Daft Punk" "U2" "ZZ Top"))``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 

* ``(conj '("Daft Punk" "U2" "ZZ Top") "ACDC")``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 

####Day 4

* ``(assoc :a 3 {:a 5, :b 8, :c 9})``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 

* ``(assoc (:a 3) {:a 5, :b 8, :c 9})``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 

* ``(assoc {:a 3} {:a 5, :b 8, :c 9})``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 

* ``(assoc {:a 5, :b 8, :c 9} :a 3)``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 

####Day 5

* ``(defn [coll] penultimate (last (drop-last coll)))``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 

* ``(defn penultimate [coll] (last (drop-last coll)))``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 
