(ns intro.core_test
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]
            [strings.beginner_string_library :refer :all]
            [errors.dictionaries :refer :all]
            [errors.exceptions :refer :all]))

;;; EVERYTHING BELOW WE'VE EITHER MOVED OR IS UNIMPORTANT, BUT WE'LL KEEP IT HERE
;;; FOR SAFE-KEEPING FOR NOW

;(expect [{:msg "Attempted to use ", :stylekey :reg, :length 17}
;         {:msg "a character", :stylekey :type, :length 11}
;         {:msg ", but ", :stylekey :reg, :length 6}
;         {:msg "a number", :stylekey :type, :length 8}
;         {:msg " was expected.", :stylekey :reg, :length 14}]
;          (my-prettify-exception
;            (run-and-catch-raw'(+ \a 2))))

;; Same as above, but putting all of the :msg pieces of the hash-maps together into
;; one string
;(expect "Attempted to use a character, but a number was expected."
;        (get-all-text (:msg
;           (my-prettify-exception
;              (run-and-catch-raw'(+ \a 2))))))

;(expect "in function map first argument 6 must be a function but is a number"
;        (get-all-text (:msg
;                       (my-prettify-exception
;                        (run-and-catch-raw'(map 6 7))))))

;(expect [{:msg "Divide by zero", :stylekey :reg, :length 14}]
;          (my-prettify-exception
;            (run-and-catch-raw'(/ 5 0))))

;(expect [{:msg "An index in a sequence is out of bounds",
;          :stylekey :reg,
;          :length 39}]
;        (my-prettify-exception
;          (run-and-catch-raw'(nth ["hello" "world" "I" "rock"] 9))))

;(expect [{:msg "An index in a sequence is out of bounds",
;          :stylekey :reg,
;          :length 39}]
;        (my-prettify-exception
;          (run-and-catch-raw'(nth (seq #{1 0}) 2))))

;################################################################
;### Skipped from duplicate-seq to a-nil-key, they seem hard. ###
;################################################################

;(expect [{:msg "An attempt to access a non-existing object: ", :stylekey :reg, :length 44}
;         {:msg "some message", :stylekey :arg, :length 12}
;         {:msg "\n(NullPointerException)", :stylekey :reg, :length 23}]
;        (my-prettify-exception
;          (run-and-catch-raw'(throw (new NullPointerException "some message")))))

;(expect [{:msg "Don't know how to create ", :stylekey :reg, :length 25}
;         {:msg "a sequence", :stylekey :type, :length 10}
;         {:msg " from ", :stylekey :reg, :length 6}
;         {:msg "a function", :stylekey :type, :length 10}]
;        (my-prettify-exception
;         (run-and-catch-raw'(doall (concat [:banana] +)))))

;(expect [{:msg "Don't know how to create ", :stylekey :reg, :length 25}
;         {:msg "a sequence", :stylekey :type, :length 10}
;         {:msg " from ", :stylekey :reg, :length 6}
;         {:msg "a number", :stylekey :type, :length 8}]
;        (my-prettify-exception
;         (run-and-catch-raw'(rest 1))))

;(expect [{:stylekey :reg, :length 17, :msg "Attempted to use "}
;         {:stylekey :type, :length 10, :msg "a function"}
;         {:stylekey :reg, :length 6, :msg ", but "}
;         {:stylekey :type, :length 12, :msg "a collection"}
;         {:stylekey :reg, :length 14, :msg " was expected."}]
;        (my-prettify-exception
;         (run-and-catch-raw'(conj + 1))))

;(expect [{:msg "Compilation error: ", :stylekey :reg, :length 19}
;         {:msg "name ", :stylekey :reg, :length 5}
;         {:msg "index-of", :stylekey :arg, :length 8}
;         {:msg " is undefined, while compiling ", :stylekey :reg, :length 31}]
;        (butlast
;        (my-prettify-exception
;         (run-and-catch-raw'(index-of "emma" \e \0)))))

;(expect #"NO_SOURCE_PATH"
;        (:msg
;         (last
;           (my-prettify-exception
;             (run-and-catch-raw'(index-of "emma" \e \0))))))

;; This is a group
;;###################################################################
;(expect [{:msg "Don't know how to create ", :stylekey :reg, :length 25}
;         {:msg "a sequence", :stylekey :type, :length 10}
;         {:msg " from ", :stylekey :reg :length 19}
;         {:msg "name ", :stylekey :reg, :length 5}
;         {:msg "some?", :stylekey :arg, :length 5}
;         {:msg " is undefined, while compiling ", :stylekey :reg, :length 31}
;         {:msg "NO_SOURCE_PATH:105:26", :stylekey :arg, :length 21}]
;        (my-prettify-exception
;         (run-and-catch-raw'(some? #(+ % 2) [:k :v]))))

;(expect [{:msg "Don't know how to create ", :stylekey :reg, :length 25}
;         {:msg "a sequence", :stylekey :type, :length 10}
;         {:msg " from ", :stylekey :reg :length 19}
;         {:msg "name ", :stylekey :reg, :length 5}
;         {:msg "some?", :stylekey :arg, :length 5}
;         {:msg " is undefined, while compiling ", :stylekey :reg, :length 31}]
;        (butlast
;         (my-prettify-exception
;          (run-and-catch-raw'(any? #(+ % 2) [:k :v])))))

;({:msg "Compilation error: ", :stylekey :reg, :length 19}
;       {:msg "name ", :stylekey :reg, :length 5}
;       {:msg "some?", :stylekey :arg, :length 5}
;       {:msg " is undefined, while compiling ", :stylekey :reg, :length 31})

;(expect #"NO_SOURCE_PATH"
;        (:msg
;         (last
;           (my-prettify-exception
;             (run-and-catch-raw'(any? #(+ % 2) [:k :v]))))))

;; This is a group
;;###################################################################
;(expect [{:msg "Compilation error: ", :stylekey :reg, :length 19}
;         {:msg "name ", :stylekey :reg, :length 5}
;         {:msg "add-first", :stylekey :arg, :length 9}
;         {:msg " is undefined, while compiling ", :stylekey :reg, :length 31}]
;        (butlast
;         (my-prettify-exception
;           (run-and-catch-raw'(add-first 1 [])))))

;(expect #"NO_SOURCE_PATH"
;        (:msg
;         (last
;           (my-prettify-exception
;             (run-and-catch-raw'(add-first 1 []))))))

;; This is a group
;;###################################################################
;(expect [{:msg "Compilation error: ", :stylekey :reg, :length 19}
;         {:msg "name ", :stylekey :reg, :length 5}
;         {:msg "some?", :stylekey :arg, :length 5}
;         {:msg " is undefined, while compiling ", :stylekey :reg, :length 31}
;         {:msg "NO_SOURCE_PATH:105:26", :stylekey :arg, :length 21}]
;        (my-prettify-exception
;         (run-and-catch-raw'(some? #(+ % 2) [:k :v]))))
