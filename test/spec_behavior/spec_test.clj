(ns spec_behavior.spec_test
  (:use [corefns.specs])
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]))

;############################################
;### Tests for spec data in ExceptionInfo ###
;###                                      ###
;############################################

(defn hashmap-gen
  "takes a quoted function and an optional path to the acutal error call,
  and returns an auto-generated spec hash-map when an error happens.
  It assumes the path is [:args] without the path argument. Use
  init-hash-gen function to see how a hashmap looks like"
  ([f]
   (try (eval f)
     (catch clojure.lang.ExceptionInfo e
       ((:clojure.spec/problems (.getData e)) [:args]))))
  ([f path]
   (try (eval f)
     (catch clojure.lang.ExceptionInfo e
       ((:clojure.spec/problems (.getData e)) path)))))

(defn init-hashmap-gen
  "takes a quoted function call and returns the error data as a hashmap"
  [f]
  (try (eval f)
    (catch clojure.lang.ExceptionInfo e
      (.getData e))))

; An example error data:
;{:clojure.spec/problems {[:args] {:pred length1?, :val nil, :via [:corefns.specs/length-one], :in []}}, :clojure.spec/args nil}

;; ########## tests for empty? ##########
(expect false (empty? [1 2]))
(expect true (empty? nil))
; We need ' (quote) for the :pred value
(expect {:pred 'seqable?, :val 3, :via [], :in [0]}
        (hashmap-gen '(empty? 3) [:args :check-seqable]))
; The value of key :pred is a lazy sequence
;; (expect (more-> 'cat first
;;                 :check-seqable second
;;                 'seqable? last)
;;         (:pred (hashmap-gen '(empty? [] []))))
(expect {:pred 'length1?, :val '([] []), :via [:corefns.specs/length-one], :in []}
        (hashmap-gen '(empty? [] [])))
(expect {:pred 'length1?, :val nil, :via [:corefns.specs/length-one], :in []}
        (hashmap-gen '(empty?)))
(expect {:pred 'seqable?, :val true, :via [], :in [0]}
        (hashmap-gen '(empty? true) [:args :check-seqable]))



;; ########## tests for map ##########
(expect [2 3 4] (map inc [1 2 3]))
(expect {:pred 'length-greater1?, :val '(10), :via [:corefns.specs/length-greater-one], :in []}
        (hashmap-gen '(map 10)))
(expect {:pred 'ifn?, :val 1, :via [], :in [0]}
        (hashmap-gen '(map 1 [1 3 4] [1 2 3] [1 3 4]) [:args :check-function]))
(expect {:pred 'seqable?, :val 3, :via [], :in [4]}
        (hashmap-gen '(map empty? [1 2 3] [2 3 4] [3 4 5] 3) [:args :check-seqable]))



;; ########## tests for conj ##########
(expect [1 2 3 3 [2]] (conj [1 2] 3 3 [2]))
(expect {:pred 'length-greater1?, :val '([1]), :via [:corefns.specs/length-greater-one], :in []}
        (hashmap-gen '(conj [1])))
(expect {:pred 'seqable?, :val 1, :via [], :in [0]}
        (hashmap-gen '(conj 1 3) [:args :check-seqable]))
(expect {:pred 'seqable?, :val true, :via [], :in [0]}
        (hashmap-gen '(conj true 3) [:args :check-seqable]))



;; ########## tests for reduce ##########
(expect 6 (reduce + [1 2 3]))
(expect 16 (reduce + 10 [1 2 3]))
(expect {:pred 'ifn?, :val 1, :via [], :in [0]}
        (hashmap-gen '(reduce 1 [1 2 3]) [:args :two-case :check-function]))
(expect {:pred 'length3?, :val '(1 [1 2 3]), :via [:corefns.specs/length-three], :in []}
        (hashmap-gen '(reduce 1 [1 2 3]) [:args :three-case]))
(expect {:pred 'length2?, :val '([1 2] 0 [] 1), :via [:corefns.specs/length-two], :in []}
        (hashmap-gen '(reduce [1 2] 0 [] 1) [:args :two-case]))
(expect {:pred 'length3?, :val '([1 2] 0 [] 1), :via [:corefns.specs/length-three], :in []}
        (hashmap-gen '(reduce [1 2] 0 [] 1) [:args :three-case]))
(expect {:pred 'length2?, :val '(1), :via [:corefns.specs/length-two], :in []}
        (hashmap-gen '(reduce 1) [:args :two-case]))
(expect {:pred 'length3?, :val '(1), :via [:corefns.specs/length-three], :in []}
        (hashmap-gen '(reduce 1) [:args :three-case]))
(expect {:pred 'seqable?, :val 3, :via [], :in [1]}
        (hashmap-gen '(reduce + 3) [:args :two-case :check-seqable]))
(expect {:pred 'seqable?, :val 2, :via [], :in [2]}
        (hashmap-gen '(reduce + 2 2) [:args :three-case :check-seqable]))

