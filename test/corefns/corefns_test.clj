(ns corefns.corefns_test
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]
            [strings.beginner_string_library :refer :all]
            [errors.messageobj :refer :all]
            [errors.exceptions :refer :all]
            [corefns.collection_fns :refer :all]
            [intro.core :refer :all]))

;############################################
;### Testing the functionality of corefns ###
;############################################

;;testing for first
(expect 1
  (first [1 2 3 4 5]))

(expect 1
  (first '(1 2 3 4 5)))

(expect nil
  (first '()))
;;testing for rest
(expect [2 3 4 5]
  (rest [1 2 3 4 5]))

(expect '(2 3 4 5)
  (rest '(1 2 3 4 5)))

(expect '()
  (rest nil))

(expect '()
  (rest '()))

(expect '()
  (rest '(1)))
;;testing for next
(expect [2 3 4 5]
  (next [1 2 3 4 5]))

(expect '(2 3 4 5)
  (next '(1 2 3 4 5)))

(expect nil
  (next '()))
;;testing for seq
(expect true
  (every? seq ["1" [1] '(1) {:1 1} #{1}]))
;;testing for assoc
(expect [:a "cat" :b 2 :c "dog" :d 4 :e 5 "cats"]
        (assoc [:a 1 :b 2 :c 3 :d 4 :e 5] 5 "dog" 1 "cat" 10 "cats"))
(expect {:a "dog" :b 2 :c "cats" :d 4 :e "cat"}
        (assoc {:a 1 :b 2 :c 3 :d 4 :e 5} :a "dog" :e "cat" :c "cats"))
(expect [10 2 20]
        (assoc [1 2 3] 0 10 2 20))
(expect {:a 5 :v 12}
        (assoc nil :a 5 :v 12))

;;testing for dissoc
(expect {:b 2, :d 4, :f 6}
        (dissoc {:a 1 :b 2 :c 3 :d 4 :e 5 :f 6 :g 7} :a :c :e :g))
(expect {:a 1 :b 2 :c 3}
        (dissoc {:a 1 :b 2 :c 3}))
(expect nil
        (dissoc nil :a))

;; testing for map
(expect '(2 3 4 5 6)
        (map inc [1 2 3 4 5]))
(expect '(5 7 9)
        (map + [1 2 3] [4 5 6]))
(expect '(2 4 6)
        (map + [1 2 3] (iterate inc 1)))
(expect '("Hello Henry!" "Hello Emma!" "Hello Lemmon!")
        (map #(str "Hello " % "!" ) ["Henry" "Emma" "Lemmon"]))
(expect '([:a :d :g] [:b :e :h] [:c :f :i])
        (apply map vector [[:a :b :c] [:d :e :f] [:g :h :i]]))
(expect (more-of x
                 3 (count x)
                 [:a 2] (in x)
                 [:b 4] (in x)
                 [:c 6] (in x))
        (map #(vector (first %) (* 2 (second %))) {:a 1 :b 2 :c 3}))

;; testing for count
(expect 0
        (count nil))
(expect 0
        (count []))
(expect 3
        (count [4 5 6]))
(expect 2
        (count {:one 1 :two 2}))
(expect 5
        (count [1 \a "string" [1 2] {:foo :bar}]))
(expect 6
        (count "string"))
;;;; Elena: this seems to be missing the function definition,
;;;; commenting out for now
;(expect 7
;        (add-two 5))

;; testing for conj
(expect [1 2 3 4]
        (conj [1 2 3] 4))
(expect '(4 1 2 3)
        (conj '(1 2 3) 4))
(expect ["a" "b" "c" "d"]
        (conj ["a" "b" "c"] "d"))
(expect #{1 2 #{3}}
        (conj #{1 2} #{3}))
(expect '(4 3)
        (conj nil 3 4))
(expect {3 4, 1 2}
        (conj {1 2} {3 4}))

;; testing for into
(expect {1 2, 3 4}
        (into {} [[1 2] [3 4]]))
(expect [[1 2] [3 4]]
        (into [] {1 2, 3 4}))
(expect {:a 1, :b 2, :c 3}
        (into (sorted-map) {:b 2 :c 3 :a 1}))

;; testing for reduce
(expect 15
        (reduce + [1 2 3 4 5]))
(expect 6
        (reduce + 1 [2 3]))
(expect [2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67
         71 73 79 83 89 97 101 103 107 109 113 127 131 137 139
         149 151 157 163 167 173 179 181 191 193 197 199 211
         223 227 229 233 239 241 251 257 263 269 271 277 281
         283 293 307 311 313 317 331 337 347 349 353 359 367
         373 379 383 389 397 401 409 419 421 431 433 439 443
         449 457 461 463 467 479 487 491 499 503 509 521 523
         541 547 557 563 569 571 577 587 593 599 601 607 613
         617 619 631 641 643 647 653 659 661 673 677 683 691
         701 709 719 727 733 739 743 751 757 761 769 773 787
         797 809 811 821 823 827 829 839 853 857 859 863 877
         881 883 887 907 911 919 929 937 941 947 953 967 971
         977 983 991 997]
        (reduce
         (fn [primes number]
           (if (some zero? (map (partial mod number) primes))
             primes
             (conj primes number)))
         [2]
         (take 1000 (iterate inc 3))))
(expect [1 2 3 :a :b :c [4 5] 6]
        (reduce into [[1 2 3] [:a :b :c] '([4 5] 6)]))

;; testing for nth
(expect "a"
        (nth ["a" "b" "c" "d"] 0))
(expect "b"
        (nth ["a" "b" "c" "d"] 1))
(expect "nothing found"
        (nth [] 0 "nothing found"))
(expect 1337
        (nth [0 1 2] 77 1337))

;; testing for filter
(expect '(0 2 4 6 8)
        (filter even? (range 10)))
(expect '("a" "b" "n" "f" "q")
        (filter (fn [x]
                  (= (count x) 1))
                ["a" "aa" "b" "n" "f" "lisp" "clojure" "q" ""]))
(expect '("a" "b" "n" "f" "q")
        (filter #(= (count %) 1)
                ["a" "aa" "b" "n" "f" "lisp" "clojure" "q" ""]))
(expect '([:c 101] [:d 102])
        (filter #(> (second %) 100)
                {:a 1 :b 2 :c 101 :d 102 :e -1}))

;; testing for mapcat
(expect '(0 1 2 3 4 5 6 7 8 9)
        (mapcat reverse [[3 2 1 0] [6 5 4] [9 8 7]]))
(expect '("aa" "bb" "cc" "dd" "ee" "ff")
        (mapcat #(clojure.string/split % #"\d")
                ["aa1bb" "cc2dd" "ee3ff"]))

;; testing for concat
(expect '(1 2 3 4)
        (concat [1 2] [3 4]))
(expect [1 2 3 4]
        (into [] (concat [1 2] [3 4])))
(expect '(:a :b 1 [2 3] 4)
        (concat [:a :b] nil [1 [2 3] 4]))
(expect '(1 2 3 4 5 6 7 8 9 10)
        (concat [1] [2] '(3 4) [5 6 7] [8 9 10]))
(expect '(\a \b \c \d \e \f)
        (concat "abc" "def"))

;; testing for <
(expect true (< 1 2))
(expect false (< 2 1))
(expect true (< 1.5 2))
(expect true (< 2 3 4 5 6))

;; testing for <=
(expect true (<= 8 8))
(expect false (<= 9 1))
(expect true (<= 10 100))
(expect true (<= 0 0 2 4 4 9 11))

;; testing for >
(expect false (> 1 2))
(expect true (> 2 1))
(expect false (> 1.5 2))
(expect true (> 9 8 5 3 1))

;; testing for >=
(expect true (>= 9 8))
(expect false (>= 2 5))
(expect true (>= 100 100))
(expect true (>= 10 10 5 4 4 4 4 4 4 2))

;; testing for +
(expect 0 (+))
(expect 9 (+ 9))
(expect -10 (+ -10))
(expect 82 (+ 39 41 2))

;; testing for -
(expect Exception (-))
(expect -10 (- 10))
(expect 10 (- -10))
(expect 30 (- 39 5 4))

;; testing for *
(expect 1 (*))
(expect 10 (* 10))
(expect -10 (* -10))
(expect 60 (* 3 5 4))

;; testing for /
(expect Exception (/))
(expect 1/10 (/ 10))
(expect 4 (/ 12 3))
(expect 2 (/ 24 3 4))

;; testing for quot
(expect 3 (quot 10 3))
(expect -1.0 (quot -5.9 3))

;; testing for rem
(expect 1 (rem 10 9))
(expect 0 (rem 2 2))

;; testing for mod
(expect 0 (mod 10 5))
(expect 4 (mod 10 6))

;; testing for inc
(expect 2 (inc 1))
(expect 2.0 (inc 1.0))
(expect 3/2 (inc 1/2))

;; testing for dec
(expect 1 (dec 2))
(expect 1.0 (dec 2.0))
(expect 1/2 (dec 3/2))

;; testing for max
(expect Exception (max))
(expect 10 (max 10))
(expect 10 (max 10 5))
(expect 30 (max 0 30 15))

;; testing for min
(expect Exception (min))
(expect 10 (min 10))
(expect 5 (min 10 5))
(expect 0 (min 30 0 15))

;; testing for add-first
(expect '(0 1 2)
        (add-first '(1 2) 0))
(expect [0 1 2]
        (add-first [1 2] 0))
(expect (more-of x
                 0 (first x)
                 3 (count x))
         (add-first #{2 1} 0))
(expect (more-of x
                 3 (count x)
                 [:a 1] (first x))
        (add-first {:b 2 :c 3} [:a 1]))

;; testing for add-last
(expect '(1 2 0)
        (add-last '(1 2) 0))
(expect [1 2 0]
        (add-last [1 2] 0))
(expect (more-of x
                 0 (last x)
                 3 (count x))
         (add-last #{2 1} 0))
(expect (more-of x
                 3 (count x)
                 [:a 1] (last x))
        (add-last {:b 2 :c 3} [:a 1]))

;; testing for contains-value?
(expect true (contains-value? '(7 8 9) 9))
(expect false (contains-value? '(7 8 9) 2))
(expect true (contains-value? [7 8 9] 9))
(expect false (contains-value? [7 8 9] 2))
(expect true (contains-value? #{7 8 9} 9))
(expect false (contains-value? #{7 8 9} 2))
(expect true (contains-value? {:d 4 :e 5} 4))
(expect false (contains-value? {:d 4 :e 5} 8))
(expect false (contains-value? {:d 4 :e 5} :e))

;; testing for contains-key?
(expect false (contains-key? [7 8 9] 9)) ;with vectors, contains-key? works with indices
(expect true (contains-key? [7 8 9] 2))
(expect true (contains-key? #{7 8 9} 9)) ;with sets, contains-key? works just as contains-value?
(expect false (contains-key? #{7 8 9} 2))
(expect true (contains-key? {:d 4 :e 5} :d))
(expect false (contains-key? {:d 4 :e 5} :z))
(expect false (contains-key? {:d 4 :e 5} 5))

;; testing for any?
(expect true (any? odd? [1 2 3 4 5]))
(expect false (any? even? [1 3 5 7 9 11]))
(expect true (any? #(= :a %) [:a :b :c :d :e]))

;; testing for comp
(expect -4 ((comp inc #(* 5 %) dec #(* 7 %)) 0))

;; testing for repeat
(expect '("x" "x" "x" "x" "x") (take 5 (repeat "x")))
(expect '("x" "x" "x" "x" "x") (repeat 5.23 "x"))

;; testing for repeatedly
(expect '("x" "x" "x" "x" "x") (take 5 (repeatedly (constantly "x"))))
(expect '("x" "x" "x" "x" "x") (repeatedly 4.23 (constantly "x")))

;; testing for distinct
(expect '(1 2 3 4 5) (distinct [1 2 2 2 3 2 4 4 5 4]))
;doesn't work for a weird reason?
;(expect '("h" "e" "l" "o" "w" "r" "d") (distinct "helloworld"))



;; testing for odd? and even?
;(expect (odd? nil))

;#################################################
;### Testing if the corefns preconditions work ###
;#################################################

;; testing for the precondition of distinct

(expect "In function repeat, the first argument \"not a number\" must be a number but is a string."
        (get-text-no-location
          (run-and-catch-pretty-no-stacktrace 'intro.student
                                              '(repeat "not a number" 6))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing for the precondition of repeat

(expect "In function repeat, the first argument \"not a number\" must be a number but is a string."
        (get-text-no-location
          (run-and-catch-pretty-no-stacktrace 'intro.student
                                              '(repeat "not a number" 6))))


;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing for the preconditions of repeatedly
(expect "In function repeatedly, the argument \"not a function\" must be a function but is a string."
        (get-text-no-location
          (run-and-catch-pretty-no-stacktrace 'intro.student
                                              '(repeatedly "not a function"))))

;; testing for the preconditions of repeatedly
(expect "In function repeatedly, the second argument \"not a function\" must be a function but is a string."
        (get-text-no-location
          (run-and-catch-pretty-no-stacktrace 'intro.student
                                              '(repeatedly 20 "not a function"))))

;; testing for the preconditions of repeatedly
(expect "In function repeatedly, the first argument \"not a number\" must be a number but is a string."
        (get-text-no-location
          (run-and-catch-pretty-no-stacktrace 'intro.student
                                              '(repeatedly "not a number" +))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing for the precondition of comp
(expect "In function comp, the first argument \"not a function\" must be a function but is a string."
        (get-text-no-location
          (run-and-catch-pretty-no-stacktrace 'intro.student
                                              '(comp "not a function" #(* 5 %) dec #(* 7 %)))))


;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing for the preconditions on assoc, it is a compuler exception
(expect "In function assoc, the first argument \"this is a string\" must be a map or vector but is a string."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(assoc "this is a string" :key1 "val1" :key2 "val2"))))

;; testing for the preconditions on dissoc
(expect "In function dissoc, the first argument \"this is a string\" must be a map but is a string."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(dissoc "this is a string" :key1 :key2))))

(expect "In function dissoc, the argument [\"this\" \"is\" \"a\" \"vector\"] must be a map but is a vector."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(dissoc ["this" "is" "a" "vector"]))))


;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing for the first precondition of map
(expect "In function map, the first argument :not-a-function must be a function but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(doall (map :not-a-function [1 2 3])))))

;; testing for the second precondition of map
(expect "In function map, the second argument :not-a-collection must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(doall (map + :not-a-collection)))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing for the precondition of count
(expect "In function count, the argument :not-a-collection must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(count :not-a-collection))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing for the precondition of conj
(expect "In function conj, the first argument :not-a-collection must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(conj :not-a-collection))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing for the first precondition of into
(expect "In function into, the first argument :not-a-collection must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(into :not-a-collection [1 2 3]))))

;; testing for the second precondition of into
(expect "In function into, the second argument :not-a-collection must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(into #{} :not-a-collection))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing for the first precondition of reduce
(expect "In function reduce, the first argument :not-a-function must be a function but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(reduce :not-a-function [1 2 3]))))

;; testing for the second precondition of reduce
(expect "In function reduce, the second argument :not-a-collection must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(reduce + :not-a-collection))))

;; testing for the third precondition of reduce
(expect "In function reduce, the third argument :not-a-collection must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(reduce + 2 :not-a-collection))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing for the first precondition of nth, with two args
(expect "In function nth, the first argument :not-a-collection must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(nth :not-a-collection 10))))

;; testing for the second precondition of nth, with two args
(expect "In function nth, the second argument :not-a-number must be a number but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(nth [0 1 2 3 4] :not-a-number))))

;; testing for the second precondition of nth, with three args
(expect "In function nth, the first argument :not-a-collection must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(nth :not-a-collection 8 "nothing found"))))

;; testing for the second precondition of nth, with three args
(expect "In function nth, the second argument :not-a-number must be a number but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(nth [0 1 2 3 4] :not-a-number ""))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing for the first precondition of filter
(expect "In function filter, the first argument :not-a-function must be a function but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(filter :not-a-function [1 2 3]))))

;; testing for the second precondition of filter
(expect "In function filter, the second argument :not-a-collection must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(filter odd? :not-a-collection))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing for the first precondition of mapcat
(expect "In function mapcat, the first argument :not-a-function must be a function but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(mapcat :not-a-function [1 2 3] [8 9 10]))))

;; testing for the second precondition of mapcat
(expect "In function mapcat, the second argument :not-a-collection must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(mapcat reverse :not-a-collection [8 9 10]))))

;; testing for the second precondition of mapcat with multiple collections
(expect "In function mapcat, the third argument :not-a-collection must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(mapcat reverse [1 2 3] :not-a-collection))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing for the precondition of concat with one arg
(expect "In function concat, the first argument :not-a-collection must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(concat :not-a-collection))))

;; testing for the precondition of concat with multiple args
(expect "In function concat, the fourth argument :not-a-collection must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(concat [1 2] [3 4] [18 22] :not-a-collection))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing if the precondition of < works on first arg
(expect "In function <, the first argument :not-a-number must be a number but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(< :not-a-number 31))))

;; testing if the precondition of < works on second arg
(expect "In function <, the second argument :not-a-number must be a number but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(< 4 :not-a-number))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing if the precondition of > works on first arg
(expect "In function >, the first argument :not-a-number must be a number but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(> :not-a-number 31))))

;; testing if the precondition of > works on second arg
(expect "In function >, the second argument :not-a-number must be a number but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(> 4 :not-a-number))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing if the precondition of >= works on first arg
(expect "In function >=, the first argument :not-a-number must be a number but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(>= :not-a-number 31))))

;; testing if the precondition of >= works on second arg
(expect "In function >=, the second argument :not-a-number must be a number but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(>= 4 :not-a-number))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing if the precondition of <= works on first arg
(expect "In function <=, the first argument :not-a-number must be a number but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(<= :not-a-number 31))))

;; testing if the precondition of <= works on second arg
(expect "In function <=, the second argument :not-a-number must be a number but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(<= 4 :not-a-number))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing if the precondition of + works
(expect "In function +, the first argument :not-a-number must be a number but is a keyword."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.student
                                                          '(+ :not-a-number 5))))
(expect "In function +, the fourth argument :not-a-number must be a number but is a keyword."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.core
                                                          '(+ 8 9 1 :not-a-number 5))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing if the precondition of - works
(expect "In function -, the first argument :not-a-number must be a number but is a keyword."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.student
                                                          '(- :not-a-number 5))))
(expect "In function -, the fourth argument :not-a-number must be a number but is a keyword."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.student
                                                          '(- 20 9 1 :not-a-number 5))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing if the precondition of * works
(expect "In function *, the first argument :not-a-number must be a number but is a keyword."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.student
                                                          '(* :not-a-number 5))))
(expect "In function *, the fourth argument :not-a-number must be a number but is a keyword."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.student
                                                          '(* 20 9 1 :not-a-number 5))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing if the precondition of / works
(expect "In function /, the first argument :not-a-number must be a number but is a keyword."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.student
                                                          '(/ :not-a-number 5))))
(expect "In function /, the third argument :not-a-number must be a number but is a keyword."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.student
                                                          '(/ 12 3 :not-a-number))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing if the precondition of quot works
(expect "In function quot, the first argument :not-a-number must be a number but is a keyword."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.student
                                                          '(quot :not-a-number 5))))
(expect "In function quot, the second argument :not-a-number must be a number but is a keyword."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.student
                                                          '(quot 12 :not-a-number))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing if the precondition of rem works
(expect "In function rem, the first argument :not-a-number must be a number but is a keyword."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.student
                                                          '(rem :not-a-number 5))))
(expect "In function rem, the second argument :not-a-number must be a number but is a keyword."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.student
                                                          '(rem 12 :not-a-number))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing if the precondition of mod works
(expect "In function mod, the first argument :not-a-number must be a number but is a keyword."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.student
                                                          '(mod :not-a-number 5))))
(expect "In function mod, the second argument :not-a-number must be a number but is a keyword."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.student
                                                          '(mod 12 :not-a-number))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing if the precondition of inc works
(expect "In function inc, the argument :not-a-number must be a number but is a keyword."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.student
                                                          '(inc :not-a-number))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing if the precondition of dec works
(expect "In function dec, the argument :not-a-number must be a number but is a keyword."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace
                       'intro.student '(dec :not-a-number))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing if the precondition of max works
(expect "In function max, the first argument :not-a-number must be a number but is a keyword."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.student
                                                          '(max :not-a-number))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing if the precondition of min works
(expect "In function min, the first argument :not-a-number must be a number but is a keyword."
        (get-text-no-location (run-and-catch-pretty-no-stacktrace 'intro.student
                                                          '(min :not-a-number))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing for the precondition of add-first
(expect "In function add-first, the first argument :not-a-collection must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(add-first :not-a-collection [1 2 3]))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing for the precondition of add-last
(expect "In function add-last, the first argument :not-a-collection must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(add-last :not-a-collection [1 2 3]))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing for contains-value?
(expect "In function contains-value?, the first argument :not-a-collection must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(contains-value? :not-a-collection 2))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing for contains-key?    (println "matched: "matched)
(expect "In function contains-key?, the first argument :not-a-collection must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(contains-key? :not-a-collection 2))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; testing for any?
(expect "In function any?, the first argument :not-a-predicate must be a function but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(any? :not-a-predicate [1 2 3]))))

;; testing for any?
(expect "In function any?, the second argument :not-a-collection must be a sequence but is a keyword."
        (get-text-no-location
         (run-and-catch-pretty-no-stacktrace 'intro.student
                                             '(any? odd? :not-a-collection))))
