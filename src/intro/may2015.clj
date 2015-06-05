(ns intro.may2015
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]
            [corefns.collection_fns :refer :all]
            [intro.student :refer :all]))

(nil 5)
;Error: Can't call nil
;Found in file /home/stock424/Documents/code/clojure-intro-class/src/intro/may2015.clj on line 7 at character 1.
;	intro.core/-main (core.clj line 111)
;Error: Can't call nil
;Found in file /home/stock424/Documents/code/clojure-intro-class/src/intro/may2015.clj on line 7 at character 1.
;	intro.core/-main (core.clj line 111)

;((first []) 5)
;(false )
;Error: Attempted to use a boolean, but a function was expected.
;Found in file /home/stock424/Documents/code/clojure-intro-class/src/intro/may2015.clj on line 7 at character 1.
;	intro.may2015/eval8626 (may2015.clj line 9)
;	intro.core/-main (core.clj line 111)
;(map nil 5)

;(5 5)






;Error: Cannot call nil as a function.









;(+ 5 (map (fn [x] (repeat x)) (range)))


;(let [x] (+ x 2))

;(hash-map 1)

;{"a" 1 "a"}

;(map #(+ %1 %2) [1 2 3])

;(

;(= ((fn [x] (for [y x :when odd? y] y)) #{1 2 3 4 5}) '(1 3 5)

;(def stuff (reduce #(+ % 1) [1 2 3]))

;(print stuff)

;(defn 4 5)

;(+ 2 "banana")

;(reduce inc [1 2 3])

;(take 5)
;(drop "not a number" '(1 2 3 4 5 6 7 8 9))

;;#######################
;;##DESTRUCTURING TESTS##
;;#######################
;(print "################################################################################")
;;the first several are from the link you sent me
;;https://gist.github.com/john2x/e1dca953548bfdfb9844

;(def my-vector [:a :b :c :d])
;(def my-nested-vector [:a :b :c :d [:x :y :z]])

;(let [[a b c d] my-vector]
;  (println a b c d))
;; => :a :b :c :d
;(let [[a _ _ d [x y z]] my-nested-vector]
;  (println a d x y z))
;; => :a :d :x :y :z
;(let [[a b c] my-vector]
;  (println a b c))
;; => :a :b :c
;(let [[a b & the-rest] my-vector]
;  (println a b the-rest))
;; => :a :b (:c :d)
;(let [[a b c d e f g] my-vector]
;  (println a b c d e f g))
;; => :a :b :c :d nil nil nil
;(let [[:as all] my-vector]
;  (println all))
;; => [:a :b :c :d]
;(let [[a :as all] my-vector]
;  (println a all))
;; => :a [:a :b :c :d]
;(let [[a _ _ _ [x y z :as nested] :as all] my-nested-vector]
;  (println a x y z nested all))
;; => :a :x :y :z [:x :y :z] [:a :b :c :d [:x :y :z]]
;(let [[a b & the-rest :as all] my-vector]
;  (println a b the-rest all))
;; => :a :b (:c :d) [:a :b :c :d]

;(defn foo [a b & more-args]
;  (println a b more-args))

;(foo :a :b) ;; => :a :b nil
;(foo :a :b :x) ;; => :a :b (:x)
;(foo :a :b :x :y :z) ;; => :a :b (:x :y :z)
;(defn foo [a b & [x y z]]
;  (println a b x y z))
;(foo :a :b) ;; => :a :b nil nil nil
;(foo :a :b :x) ;; => :a :b :x nil nil
;(foo :a :b :x :y :z) ;; => :a :b :x :y :z

;(def my-hashmap {:a "A" :b "B" :c "C" :d "D"})
;(def my-nested-hashmap {:a "A" :b "B" :c "C" :d "D" :q {:x "X" :y "Y" :z "Z"}})

;(let [{a :a d :d} my-hashmap]
;  (println a d))
;=> A D
;(let [{a :a, b :b, {x :x, y :y} :q} my-nested-hashmap]
;  (println a b x y))
;=> A B X Y
;(let [{a :a, not-found :not-found, b :b} my-hashmap]
;  (println a not-found b))
;; => A nil B
;(let [{a :a, not-found :not-found, b :b, :or {not-found ":)"}} my-hashmap]
;  (println a not-found b))
;; => A :) B
;(let [{a :a, b :b, :as all} my-hashmap]
;  (println a b all))
;; => A B {:a A :b B :c C :d D}
;(let [{a :a, b :b, not-found :not-found, :or {not-found ":)"}, :as all} my-hashmap]
;  (println a b not-found all))
;; => A B :) {:a A :b B :c C :d D}
;(let [{:keys [a d]} my-hashmap]
;  (println a d))
;; => A D
;(let [{:keys [a b], {:keys [x y]} :q} my-nested-hashmap]
;  (println a b x y))
;; => A B X Y
;(let [{:keys [a not-found b]} my-hashmap]
;  (println a not-found b))
;; => A nil B
;(let [{:keys [a not-found b], :or {not-found ":)"}} my-hashmap]
;  (println a not-found b))
;; => A :) B
;(let [{:keys [a b], :as all} my-hashmap]
;  (println a b all))
;; => A B {:a A :b B :c C :d D}
;(let [{:keys [a b not-found], :or {not-found ":)"}, :as all} my-hashmap]
;  (println a b not-found all))
;; => A B :) {:a A :b B :c C :d D}
;(let [{:strs [a d]} {"a" "A", "b" "B", "c" "C", "d" "D"}]
;  (println a d))
;; => A D
;(let [{:syms [a d]} {'a "A", 'b "B", 'c "C", 'd "D"}]
;  (println a d))
;; => A D
;(let [{:keys [a b]} '("X", "Y", :a "A", :b "B")]
;(println a b))
;; => A B

;(defn foo [a b & {:keys [x y]}]
;  (println a b x y))
;(foo "A" "B")  ;; => A B nil nil
;(foo "A" "B" :x "X")  ;; => A B X nil
;(foo "A" "B" :x "X" :y "Y")  ;; => A B X Y

;;All the examples from the link passed without any errors.






;(let [[s e a n] [:S :e :G :+]]
;  (println s e a n))
;;=> :S :e :G :+

;(let [[:a :b :c :d] {:a "A" :b "B" :c "C"}]
;  (println :a :b :c))
;;=> Error: Function nth does not allow a map as an argument.
;;Found in file H:\Git\clojure-intro-class\src\intro\may2015.clj on line 57 at character 1.
;;	intro.may2015/eval8641 (may2015.clj line 57)
;;	intro.core/-main (core.clj line 111)

;(let [[:a :b :c] ["A" "B" "C"]]
;  (println :a :b :c :p))
;=> :a :b :c

;(let [[:a :b :c] [A B D]]
;  (println a b c))
;Syntax error: Name A is undefined.
;Found in file H:\Git\clojure-intro-class\src\intro\may2015.clj on line 95 at character 1.
;	intro.core/-main (core.clj line 111)


;(let [[a b c d e f g] my-vector]
;  (println a b f))
;;=> :a :b nil


;;(let [{a :a b :b cats [:bill :joe]} {:a "AAA" :b "BBB" :bill "BILL" :joe "JOE"}]
;;  (print a b cats))
;=> AAA BBB nilnil

;(let [{a :a b :b c :c d :d} {:a "A" :b "B" :c "C"}]
;  (println a b c d))

;(let [{a :a b :b c :c d :d} [[:a :b :c :d] ["A" "B" "C" "D"]]]
;  (println a b c d))
;
;(let [{:a "A" :b "B" :c "C"} [:A :B :C :D]]
;  (println :a :b :c :d))

;(let [{a :a, b :b, {x :x, y :y} :q} {:a "A" :b "B" :c "C" :d "D" :q {:x "X" :y "Y" :z "Z"}}]
;  (println a b x y))
;; => A B X Y

;(let [{a :a, b :b, x {:x :xx, :xx :z}} {:a "A" :b "B" :c "C" :d "D" :xx {:x "X" :y "Y" :z "Z"}}]
;  (println a b x))

;(let [[a b c d] [:a :b :c :d]
;      {:a :A :b :B :c :C :d :D} {:A "A" :B "B" :C d :D d}]
;  (println a b c d))

;(let [[a b c d] [:a :b :c :d]
;      {:a :A :b :B :c :C :d :D} {:A "A" :B "B" :C d :D d}
;      [a b c d] [:A :B :C :D]]
;  (println a b c d))

;(let [[a b c d] [:a :b :c :d]
;      [a b c d] [:A :B :C :D]]
;  (println a b c d))

(let [[:1 a b] (map inc (range))]
  (println a b))

(let [[:1 a b] "str"]
  (println a b))


;;(let [[:1 a b] 1234]
;;  (println a b))
;;nth does not allow a number as an argument

;(let [[:1 a b] concat]
;  (println a b))
;nth again

;(let [[:1 a b] :abcd]
;(println a b))
;weird error

;;(let [[:1 a b] true]
;;  (println a b))
;;Error: Function nth does not allow unrecognized type Boolean as an argument.
;;Found in file /home/stock424/Documents/code/clojure-intro-class/src/intro/may2015.clj on line 216 at character 1.
;;	intro.may2015/eval8633 (may2015.clj line 228)
;;	intro.core/-main (core.clj line 111)

;(map inc true)

(let [[:1 a b] true]
  (println a b))

;(let [[a b c d] {}]
;  (println a b c d))
;;Error: nth doesnt allow map as an argument

;(let [{a :a} {:a a}
;      {a :a} {:a "b"}]
;  (println a))
;;Error: name a is undefined.

;(let [{a :a} {:a :b}
;      {:b :c} {:c "D"}]
;  (println a))

;(let [{a :a} {:a :b}
;      {a :c} {:c "D"}]
;  (println a))

;(let [{a :a} {:a b}
;      {b :c} {:c "D"}]
;  (println a))
;;Error: name b is undefined

;(let [{b :c} {:c "D"}
;      {a :a} {:a b}]
;  (println a))

;(let [{a :a} {:a "A"}]
;  (println a))

;(let [{a :a} {:a :c}
;      {:c :a} {:a "b"}]
;  (println a))

;(let [[a] [:c]
;      {a :b} {:b "A"}]
;  (println a))

;(let [[d e f] ["A" "B" "C"]
;      [a b c] [d e f]
;      {aa bb} {bb c}]
;  (println a b c aa))
;Error: the name bb is undefined


;(let [[d e f] ["A" "B" "C"]
;      [a b c] [d e f]
;      {aa :bb} {:bb c}
;      {a :h} {:h p}]
;  (println a b c aa))
;Error: the name p is undefined


;(let [[d c f] ["A" "B" "C"]
;      [a b c] [d e f]
;      {aa :bb} {:bb c}
;      {a :h} {:h c}]
;  (println a b c aa))
;Error: name e is undefined

;(let [[d e f a] ["A" "B" "C" "asdf"]
;      [a b c] [d e f]
;      {aa :bb} {:bb c}
;      {a :h} {:h c}]
;  (println a b c aa))

;(let [{a :a b :b} {:a "A" :b "B" :c "C"}]
;  (println a b c d))
;name c is undefined
;(let [{a :a b :b :c c} {:a "A" :b "B" c "C"}]
;  (println a b :c))
;Error: name c is undefined

(let [[x y _ d m] ["1" "2" "3"]]
  (println x y d))







