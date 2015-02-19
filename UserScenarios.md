###User Scenarios
####Day 1
Suzie starts her first “Hello World” program on the first day of class in a repl from our project. Here are all her code attempts:
* ``(print Hello World)``
  * Clojure message:
    * ``java.lang.Exception: Unable to resolve symbol: Hello in this context``
  * Our projFor the second day, Jaden wants to define a function in a repl from within our project. Here are all his code attempts:ect message:
    * ``message``
  * Hint/Explanation:
    * This breaks because Clojure thinks that Hello is a symbol (identifier) when Suzie 	wanted it to be just plain text. Our program could respond with a suggestion that if the user wanted the phrase to be plain text to surround it with double quotes. It could also suggest to double-check that the user spelled the symbol correctly.

* ``print("Hello World")``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
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
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 

* ``code3``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 

* ``code3``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 

####Day 3

* ``code3``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 

* ``code3``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 

* ``code3``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 

* ``code3``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 

####Day 4

* ``code3``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 

* ``code3``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 

* ``code3``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 

* ``code3``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 

####Day 5

* ``code3``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 

* ``code3``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 

* ``code3``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 

* ``code3``
  * Clojure message:
    * ``message``
  * Our project message:
    * ``message``
  * Hint/Explanation:
    * 
