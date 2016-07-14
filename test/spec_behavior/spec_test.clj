(ns spec_behavior.spec_test
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]))

;############################################
;### Tests for spec data in ExceptionInfo ###
;###                                      ###
;############################################

(defn hashmap-gen
  "takes a quoted function and an optional path to the acutal error call,
  and returns an auto-generated spec hash-map when an error happens.
  It gets the first hashmap in the resulting vector without the n-th argument.
  Use init-hash-gen function to see how an initial hashmap looks like"
  ([f]
   (try (eval f)
     (catch clojure.lang.ExceptionInfo e
       (clojure.core/nth (:clojure.spec/problems (.getData e)) 0))))
  ([f n-th]
   (try (eval f)
     (catch clojure.lang.ExceptionInfo e
       (clojure.core/nth (:clojure.spec/problems (.getData e)) n-th)))))

(defn init-hashmap-gen
  "takes a quoted function call and returns the error data as a hashmap
  when an error happens"
  [f]
  (try (eval f)
    (catch clojure.lang.ExceptionInfo e
      (.getData e))))

; 0 failures 0 errors
; An example error data:
;{:clojure.spec/problems [{:path [:args], :pred length1?, :val ([] []), :via [:spec-alpha.specs/length-one], :in []}], ...}

;; ########## tests for empty? ##########
(expect false (empty? [1 2]))
(expect true (empty? nil))
; We need ' (quote) for the :pred value
(expect {:path [:args :check-seqable], :pred 'seqable?, :val 3, :via [], :in [0]}
        (hashmap-gen '(empty? 3)))
; The value of key :pred is a lazy sequence
;; (expect (more-> 'cat first
;;                 :check-seqable second
;;                 'seqable? last)
;;         (:pred (hashmap-gen '(empty? [] []))))
(expect {:path [:args], :pred 'length1?, :val '([] []), :via [:corefns.specs/length-one], :in []}
        (hashmap-gen '(empty? [] [])))
(expect {:path [:args], :pred 'length1?, :val 'nil, :via [:corefns.specs/length-one], :in []}
        (hashmap-gen '(empty?)))
(expect {:path [:args :check-seqable], :pred 'seqable?, :val true, :via [], :in [0]}
        (hashmap-gen '(empty? true)))


;; ########## tests for map ##########
(expect [2 3 4] (map inc [1 2 3]))
(expect {:path [:args], :pred 'length-greater1?, :val '(10), :via [:corefns.specs/length-greater-one], :in []}
        (hashmap-gen '(map 10)))
(expect {:path [:args :check-function], :pred 'ifn?, :val 1, :via [], :in [0]}
        (hashmap-gen '(map 1 [1 3 4] [1 2 3] [1 3 4])))
(expect {:path [:args :check-seqable] :pred 'seqable?, :val 3, :via [], :in [4]}
        (hashmap-gen '(map empty? [1 2 3] [2 3 4] [3 4 5] 3)))


;; ########## tests for conj ##########
(expect [1 2 3 3 [2]] (conj [1 2] 3 3 [2]))
(expect {:path [:args], :pred 'length-greater1?, :val '([1]), :via [:corefns.specs/length-greater-one], :in []}
        (hashmap-gen '(conj [1])))
(expect {:path [:args :check-seqable], :pred 'seqable?, :val 1, :via [], :in [0]}
        (hashmap-gen '(conj 1 3)))
(expect {:path [:args :check-seqable], :pred 'seqable?, :val true, :via [], :in [0]}
        (hashmap-gen '(conj true 3)))


;; ########## tests for reduce ##########
(expect 6 (reduce + [1 2 3]))
(expect 16 (reduce + 10 [1 2 3]))
(expect {:path [:args :two-case :check-function], :pred 'ifn?, :val 1, :via [], :in [0]}
        (hashmap-gen '(reduce 1 [1 2 3]) 0))
(expect {:path [:args :three-case], :pred 'length3?, :val '(1 [1 2 3]), :via [:corefns.specs/length-three], :in []}
        (hashmap-gen '(reduce 1 [1 2 3]) 1))
(expect {:path [:args :two-case], :pred 'length2?, :val '([1 2] 0 [] 1), :via [:corefns.specs/length-two], :in []}
        (hashmap-gen '(reduce [1 2] 0 [] 1) 0))
(expect {:path [:args :three-case], :pred 'length3?, :val '([1 2] 0 [] 1), :via [:corefns.specs/length-three], :in []}
        (hashmap-gen '(reduce [1 2] 0 [] 1) 1))
(expect {:path [:args :two-case], :pred 'length2?, :val '(1), :via [:corefns.specs/length-two], :in []}
        (hashmap-gen '(reduce 1) 0))
(expect {:path [:args :three-case], :pred 'length3?, :val '(1), :via [:corefns.specs/length-three], :in []}
        (hashmap-gen '(reduce 1) 1))
(expect {:path [:args :two-case :check-seqable], :pred 'seqable?, :val 3, :via [], :in [1]}
        (hashmap-gen '(reduce + 3) 0))
(expect {:path [:args :three-case :check-seqable], :pred 'seqable?, :val 2, :via [], :in [2]}
        (hashmap-gen '(reduce + 2 2) 1))


;; ########## tests for nth ############
; using a helper function and late eval doesn't work for some reason.
; need to use try-catch directly
(expect "lazy" (nth ["lazy" "person" "you"] 0))
(expect "working" (nth ["lazy" "person" "you"] 4 "working"))
(expect {:path [:args :two-case :check-seqable] :pred 'seqable?, :val 1, :via [], :in [0]}
        (try (nth 1 "")
          (catch clojure.lang.ExceptionInfo e
            (clojure.core/nth (:clojure.spec/problems (.getData e)) 0))))
(expect {:path [:args :three-case] :pred 'length3?, :val '(1 ""), :via [:corefns.specs/length-three], :in []}
        (try (nth 1 "")
          (catch clojure.lang.ExceptionInfo e
            (clojure.core/nth (:clojure.spec/problems (.getData e)) 1))))
(expect {:path [:args :two-case] :pred 'length2?, :val '([1 2 3] [1] "Boo"), :via [:corefns.specs/length-two], :in []}
        (try (nth [1 2 3] [1] "Boo")
          (catch clojure.lang.ExceptionInfo e
            (clojure.core/nth (:clojure.spec/problems (.getData e)) 0))))
(expect {:path [:args :three-case :check-number] :pred 'number?, :val [1], :via [], :in [1]}
        (try (nth [1 2 3] [1] "Boo")
          (catch clojure.lang.ExceptionInfo e
            (clojure.core/nth (:clojure.spec/problems (.getData e)) 1))))


;; ########## tests for assoc ##########
(expect {:key1 "value", :key2 "another value"} (assoc {} :key1 "value" :key2 "another value"))
(expect [1 nil] (assoc [1 2] 1 nil))
(expect {nil nil} (assoc {} nil nil))
(expect {:path [:args], :pred 'length-greater2?, :val '([1 2]), :via [:corefns.specs/length-greater-two], :in []}
        (hashmap-gen '(assoc [1 2])))
(expect {:path [:args], :pred 'length-greater2?, :val '({} :random), :via [:corefns.specs/length-greater-two], :in []}
        (hashmap-gen '(assoc {} :random)))
(expect {:path [:args :check-map-or-vector :check-map :clojure.spec/nil], :pred 'nil?, :val '(), :via [], :in [0]}
        (hashmap-gen '(assoc '() nil nil)))
(expect {:path [:args :check-map-or-vector :check-map :clojure.spec/pred], :pred 'map?, :val '(), :via [], :in [0]}
        (hashmap-gen '(assoc '() nil nil) 1))
(expect {:path [:args :check-map-or-vector :check-vector], :pred 'vector?, :val '(), :via [], :in [0]}
        (hashmap-gen '(assoc '() nil nil) 2))


;; ########## tests for dissoc ##########
(expect {:c 3} (dissoc {:a 1 :b 2 :c 3} :a :b))
(expect {:path [:args], :pred 'length-greater0?, :val nil, :via [:corefns.specs/length-greater-zero], :in []}
        (hashmap-gen '(dissoc)))
(expect {:path [:args :check-map :clojure.spec/nil], :pred 'nil?, :val [0], :via [], :in [0]}
        (hashmap-gen '(dissoc [0])))
(expect {:path [:args :check-map :clojure.spec/pred], :pred 'map?, :val [0], :via [], :in [0]}
        (hashmap-gen '(dissoc [0]) 1))


;; ########## tests for quot   ###########
; using a helper function and late eval doesn't work for some reason.
; need to use try-catch directly
(expect 3 (quot 12 4))
(expect 3 (quot 9 3))
(expect {:path [:args :check-number], :pred 'number?, :val false, :via [], :in [0]}
        (try (quot false 10)
          (catch clojure.lang.ExceptionInfo e
            (clojure.core/nth (:clojure.spec/problems (.getData e)) 0))))
(expect {:path [:args], :pred 'length2?, :val '(30 "" true), :via [::corefns.specs/length-two], :in []}
        (try (quot 30 "" true)
          (catch clojure.lang.ExceptionInfo e
            (clojure.core/nth (:clojure.spec/problems (.getData e)) 0))))


;; ########## tests for repeat ###########
(expect '("x" "x" "x" "x" "x") (take 5 (repeat "x")))
(expect '("x" "x" "x" "x" "x") (repeat 5 "x"))
(expect {:path [:args :one-case], :pred 'length1?, :val '("" 5), :via [:corefns.specs/length-one], :in []}
        (hashmap-gen '(repeat "" 5)))
(expect {:path [:args :two-case :check-number], :pred 'number?, :val "", :via [], :in [0]}
        (hashmap-gen '(repeat "" 5) 1))
(expect {:path [:args :one-case], :pred 'length1?, :val '(5 :some :any 100.0), :via [:corefns.specs/length-one], :in []}
        (hashmap-gen '(repeat 5 :some :any 100.0)))
(expect {:path [:args :two-case], :pred 'length2?, :val '(5 :some :any 100.0), :via [:corefns.specs/length-two], :in []}
        (hashmap-gen '(repeat 5 :some :any 100.0) 1))
