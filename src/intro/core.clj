(ns intro.core 
  (:use [errors.core]
        [seesaw.core]
	[turtle.core]))
(refer 'corefns.core)
        ;[turtle.extended-turtle]))

(defn basic-seesaw-frame []
  (invoke-later
    (native!)
    (def f (frame :title "Hello",
                  :content (button :text "Seesaw Rocks!"),
                  :on-close :exit))
    (-> f pack! show!))) 


(defn test-and-continue [quoted-exp]
  (in-ns 'intro.core) ; eval by default evaluates in its own namespace
  (try
    (println quoted-exp)
    (eval quoted-exp)
    (catch Throwable e (println (prettify-exception e)))
    (finally (println "Are there any exceptions left?"))))

(defn test-all-and-continue [quoted-exps]
  ;; doall is needed because map is lazy 
  (doall (map test-and-continue quoted-exps))) 

(defn test-arithmetic-expressions []
  ;; 3 exceptions thrown:
  (test-all-and-continue '((+ \a 2) (< 'a 8) (/ 5 0))))

;; Some of the error messages below would change once we create enough preconditions
(defn test-sequences []
  (test-all-and-continue '( ;(nth 0 [1 2 3]) ;; attempted to use collection but number was expected 
			    ;(nth '(1 2 3) 7) ;; An index in a vector or a list is out of bounds
			    (into 6 [1 2]) ;; attempted to use number but collection was expected
			    (into {} [1 2 3]) ;; don't know how to create sequence from number. into on a hashmap requires a collection of pairs.
			    (into {} [1 2 3 4]) ;; same as above. A correct one would be (into {} [[1 2] [3 4]])
			    (conj "a" 2) ;; Attempted to use string, but collection was expected.
			    )))

(defn test-nth []
  (test-all-and-continue '( ;(nth 3 [1 2 3]) ;; ERROR: Attempted to use a collection, but a number was expected.
			(nth [1 2 3] 3)
			(nth [1 2 3] [8 9])
			(nth #{1 2 3} 1 )
			(nth (seq {:a :b :c :d}) 1)
			(nth (seq {:a :b :c :d}) 2)
			(nth (seq #{1 0}) 2)
			(nth (seq "a b c") 1)  )))

;; try re-writing using reduce
;; (reduce f coll) ---> user=> (reduce + [1 2 3 4 5]) == 15
;(defn duplicate-seq [coll] 
;	(loop [s coll result []] ;; result can work with vector or a list
;		(if (empty? s) result
;			(recur (rest s) (add-last (add-last result (first s)) (first s))))))



(defn duplicate-seq [coll] 
	(interleave (reduce add-last '() coll) coll))

;(defn flatten-helper [coll returnStuff]
;	(if (empty? coll) returnStuff
;		(do (if (coll? (first coll) ) (flatten-helper (first coll) returnStuff)
;				(do (add-last returnStuff (first coll)) (flatten-helper (rest coll) returnStuff) )))))

(defn flatten-helper [coll returnStuff]
	(if (coll? (first coll)) (flatten-helper (rest coll) returnStuff)
				 (concat returnStuff coll)))

(defn flatten-seq [coll]
	(loop [s coll result '()] 
		(if (empty? s) result
		(recur (rest s)(do (if (coll? (first s)) (flatten-helper (first s) result )
					 (add-last result (first s))))))))

;; Maybe try to work without loop-recur? If possible that is.
(defn interleave-seq [c1 c2]
	(loop [s1 c1 s2 c2 result []] 
		(if (or (empty? s1) (empty? s2) ) result 
			(recur (rest s1) (rest s2) 
				(add-last (add-last result (first s1)) (first s2) ) ) ) ) )


;(defn flatten-seq [coll]
;	(loop [s coll result '()] 
;		(if (empty? s) result
;		(recur (rest s)(do (if (coll? (first s)) (concat result (flatten-seq [first s]))
;					 (add-last result (first s))))))))

(defn rotate-left [rotations coll]
	(loop [r rotations s coll]
		(if (zero? r) s
			(recur (dec r) (drop 1 (add-last s (first s))) ) )))

(defn rotate-right [rotations coll]
	(loop [r rotations s coll]
		(if (zero? r) s
			(recur (inc r) (drop-last (add-first s (last s))) ) )))
	
(defn rotate-seq [rotations coll]
		(if (pos? rotations) (rotate-left rotations coll) 
			             (rotate-right rotations coll)))

(defn pascals-triangle-helper [coll]
	(loop [s coll n 0 result []]
		(if (= (inc n) (count s)) (add-last (add-first result (first s)) (last s))
			(recur s (inc n) (add-last result (+ (nth s n) (nth s (+ 1 n))))))))

(defn pascals-triangle [iterations]
	(loop [n 0 result [[1]]]
		(if (= n iterations) (last result)
			(recur (inc n) (add-last result (pascals-triangle-helper (last result))))))) 

(defn pack-a-seq-helper [n coll]
	(loop [s coll result '()]
		(if (empty? s) result
			(if (= n (first s)) (recur (rest s) (add-last result (first s)))
				result))))

(defn pack-a-seq-helper2 [n coll]
	(loop [s coll v 0]
		(if (empty? s) v
			(if (= n (first s)) (recur (rest s) (inc v))
			v))))

(defn pack-a-seq [coll]
	(loop [c coll result '()]
		(if (empty? c) result 
		(recur (drop (pack-a-seq-helper2 (first c) c) c)
			(add-last result (pack-a-seq-helper (first c) c))
			))))

(defn test-recur [x]
	(if (= x 5) x
	(recur (inc x))))

(defn a-nil-key [k hash]
	(and (contains-key? hash k) (= (get hash k) nil)))

(defn test-exceptions []
  (test-all-and-continue '((throw (new IndexOutOfBoundsException))
			   (throw (new IndexOutOfBoundsException "10"))
			   (throw (new NullPointerException))
			   (throw (new NullPointerException "some message")))))

(defn test-concat []
  (test-all-and-continue '( (concat :banana [1 2]) ; need doall because concat is lazy
			   (concat [:banana] +)
			   (concat [:banana] [:banana] [] 4))))
			   

(defn test-first-rest []
  (test-all-and-continue '((first 1) ; Don't know how to create a sequence from a number
			   (rest 1) ; Don't know how to create a sequence from a number
			   (first []) ;; returns nil
			   (rest []) ;; returns an empty sequence
			   (empty? 1)))) ;Don't know how to create a sequence from a number

(defn test-conj-into []
  (test-all-and-continue '((conj 1 []) ; Attempted to use a number, but a collection was expected.
			   (into [2] 3) ; Don't know how to create a sequence from a number
			   (conj + 1) ; Attempted to use a function, but a collection was expected.
			   (into '() *)))) ; Don't know how to create a sequence from a function

(defn test-add-first-last []
  (test-all-and-continue '((add-first 1 []) ; currently failing asserts, this may change later
			   (add-last 1 [])
			   (add-first + 1)
			   (add-first \s "pie")
			   (add-last \s "ies")
			   (add-first :k [:a])
			   (add-last :k [:a])
			   (add-last + 1))))

(defn test-forgetting-a-quote []
  (test-all-and-continue '((1 2 3) ; Attempted to use a number, but a function was expected.
			   ([] 1 2) ; Wrong number of args (2) passed to: PersistentVector - hmmm
			   ([2 4] 1) ; Not an error, returns 4
			   ([] 1)))) ; An index in a vector or a list is out of bounds

(defn test-turtle []
  (test-all-and-continue '(
			   (pen-up turtle) ; using an unitialized turtle
					; strangely the exception is that a function cannot be converted into a ref
			   )))

(defn our-reverse [coll]
  (reduce add-first '() coll))

(defn our-map [f coll]
  (reduce (fn [res x] (add-last res (f x))) '() coll))
  

(defn add-first-last-examples []
  (test-all-and-continue '((our-reverse [1 2 3])
			   (our-reverse '(1 2 3))
			   (our-map inc [1 2 3])
			   (our-map inc '(1 2 3)))
			 
			 ))

(defn test-seq []
  (test-all-and-continue '((doall (concat [1 2] 5))
			   (seq 2)
			   (seq true)
			   (seq map)
			   )))

(defn test-any-contains []
	(test-all-and-continue '((any? 6 :k)
				 (any? [1 2 3] odd?)
				 (any? odd? {2 3 4 5}) ; ERROR: Argument must be an integer: [2 3] 
				 (any? #(+ % 2) [1 2 5])
				 (some? #(+ % 2) [:k :v]) ; the point is that it's a wrong type of argument
				 (some? [1 2] [3 4])
				 (contains-value? {:a :b :c :d} :a)
				 (contains-value? {:a :b :c :d} :b)
				 (contains-value? [1 2 3 4] +)
				 (contains-value? + 1)
				 (contains-value? 1 [1 2 3]))))

(defn test-contains-types []
	(test-all-and-continue '((contains? "abc" \a)
		                 (contains? "abc" 7) ; this works and returns false - why?
		                 (contains? "abc" 1) ; works, returns true
		                 (contains? "abc" 1.5) ; also returns true
		                 (contains? "abc" :b)
		                 (contains? [1 2 3] \a)
		                 (contains? nil 2))))

(defn test-wrong-arg-type [] 
	(test-all-and-continue '( (+ 6 :k)
		                  (+ 6 +)
		                  (* 6 "hello")
		                  (odd? "banana")
		                  (inc :k)
		                  (dec "orange")
		                  (< 8 "lemon") ; doesn't work on strings
		                  (< "apple" "orange"))))

(defn test-unsupported-ops []
	(test-all-and-continue '((doall (nth #{1 2 3} 1 ))
			         (doall (nth {1 2 3 4} 1))
			         (doall (nth nil 0))
			         (doall (nth "abcd" 0))))) ; don't know how to create a sequence from a symbol? 

(defn test-boolean-functions []
	(test-all-and-continue '( (not 5) ; not an error
			 	  (and true +) ; not an error 
			 	  (and nil) ; not an error 
			 	  (every? #(and %) [1 2 3])))) ; not an error, returns true

(defn test-pop-peek [] 
	(test-all-and-continue '((peek [])
			         (pop nil)
			         (peek #{1 2 3})
			         (peek {1 2 3 4})
			         (peek "abc")
			         (peek 5))))

(defn test-assoc [] 
	(test-all-and-continue '((assoc [1 2 3] 5 6)
		                 (assoc #{1 2} 3 4)
		                 (dissoc '(1 2 3) 3)
		                 (dissoc [1 2 3] 9)
		                 (assoc "abc" \a \b))))

(defn test-reversible []
	(test-all-and-continue '((rseq [1 2 3])
		                 (rseq {1 2 3 5})
		                 (rseq #{1 2 3})
		                 (rseq '())
		                 (rseq "abc"))))

(defn test-sorted-collections []
	(test-all-and-continue '((subseq [1 2 3 4] > 2)
		                 (subseq #{1 2 3} = 2)
		                 (subseq {1 2 3 4} > 2))))

(defn test-arity [] 
	(test-all-and-continue '((map [1 2 3])
		                 (conj 7)
		                 (drop [1 2 3])
		                 (drop 2 [1 2 3] 7)
		                 (#(+ % %) 1 3))))

(defn test-asserts []
	(test-all-and-continue '((map 6 7)
				 (map [1 2 3] dec)
				 (map dec inc)
				 (nth 1 [2 3 4])
				 (nth "banana" [1 2 3])
				 (nth [1 2 3] "banana"))))

(defn test-asserts-multiple-args []
	(test-all-and-continue '((mapcat dec 4)
                                 (mapcat dec inc)
                                 (mapcat + [1 2 3] :a)
                                 (mapcat + :a 9)
                                 (mapcat + nil) ;; should work
                                 (mapcat + ))))

(defn test-asserts-multiple-args-map []
	(test-all-and-continue '((map dec 4)
                                 (map dec inc)
                                 (map + [1 2 3] :a)
                                 (map + :a 9)
                                 (map + nil) ;; should work
                                 (map + )
                                 (nth (map #(+ %1 %2) [1 2 3] [3 4] [5 6]) 1))))

(defn test-reduce []
	(test-all-and-continue '((reduce + 4 [1 2 3]) ;; should work, return 10
				 (reduce 4 [1 2 5])
				 (reduce [1 2 3] +)
				 (reduce + 2 2)
				 (reduce #(+ %1 %2 %3) [1 2 3]))))

(defn test-filter []
	(test-all-and-continue '((filter 9 [1 2 3])
		             (filter odd? *)
		             (filter "abc" "123")
		             (filter (+ 2 3) "123")
		             (filter odd? even?) 
		             (filter + (fn[x] (+ x 2))) 
		             (filter odd? #(* % 2))
		             (filter + [1 2 3])
		             (nth (filter assoc [1 2 3]) 1) ; added nth because otherwise it's a lazy seq
		             (filter #(< 5 %) [1 2 3] [4 5 6]))))

(defn test-function-names []
	(test-all-and-continue '((filter even? odd?)
		                 (filter even? -)
		                 (filter even? /)
		                 (filter even? =)
		                 (filter even? <)
		                 (filter even? ==)
		                 (filter even? >=)
		                 (filter even? <=)
		                 (filter even? not=)
		                 ;(filter even? lazy-cat)
		                 (filter even? list*))))

(defn myf? [x] (+ x 2))

(defn test-qmark-bang []
	(test-all-and-continue '((filter even? nil?)
		                 (filter even? identical?)
		                 (filter even? zero?)
		                 (filter even? pos?)
		                 (filter even? neg?)
		                 (filter even? keyword?)
		                 (filter even? isa?)
		                 (filter even? odd?)
		                 (filter even? swap!)
		                 (filter even? myf?))))

(defn test-macros-names []
	(test-all-and-continue '((filter even? lazy-cat)
		                 (filter even? ->)
		                 (filter even? ->>)
		                 (filter even? cond)
		                 (filter even? and))))
		                 ;(filter even? -?>)
		                 ;(filter even? -?>>))))
(defn test-loop-recur []
	(test-all-and-continue '((loop [n 1 m] (if (= n 1) 0 (recur (inc n))))
			         (loop [n 1 m 2] (if (= n 1) 0 (recur (inc n))))
			         (loop [1 2] (if (= n 1) 0 (recur (inc n))))
			         (loop '(n 1) (if (= n 1) 0 (recur (inc n))))
			         (loop [] (if (= 1 1) 0 (recur)))
			         (loop map (if (= n 1) 0 (recur (inc n)))))))

(defn test-bindings []
	(test-all-and-continue '((def 5 6)
				 (defn a (+ 1 2))
				 (defn a :k)
				 (def f [x] (+ x 2))
				 (def f (recur 2))
				 (recur 3)
				 (def f (+ 2 (recur 2))))))

(defn test-let []
	(test-all-and-continue '((let '(n 1) (+ n 1))
				 (let [m 1 n] (+ m n))
				 (let [] (+ 1 2)) ;; no error
				 (let [] [] []) ;; no error
				 (let (+ 2 3) 8))))

(defn erun  []   
	(try (load-reader (java.io.FileReader. "src/intro/student.clj")) 
		(catch Throwable e (prettify-exception e))))
				 

(defn third [coll]
  "Returns the third element in a collection,
   or nil if the collection has fewer than three elements"
  (first (rest (rest coll))))

(defn -main [& args] 
  (try
    ;(basic-seesaw-frame)
    ;(test-turtle)
    ;(test-exceptions)
    ;(test-nth)
    ;(duplicate-seq [1 2 3])
    ;(flatten-seq '((1 2) 3 [4 [5 6]]))
    ;(interleave-seq [1 2 3] [:a :b :c])
    ;(rotate-seq -6 [1 2 3 4 5])
    ;(println ;(add-first \s "pie")
    	     ;(add-first 5 [1 2 3])
    	     ;(add-last 5 [1 2 3]))
    	     ;(add-last \s "pie"))
    ;(pascals-triangle 11)
    ;(test-concat)
					;(test-concat)
					;(test-first-rest)
					;(test-conj-into)
					;(test-add-first-last)
					;(test-forgetting-a-quote)
					;(add-first-last-examples)

    ;(third [1 2 3 4])
    ;(test-seq)
    ;(def t (add-last 4 [2 3]))
    ;(test-add-first-last)
    ;(test-any-contains)

    ;(test-wrong-arg-type)
    ;(test-contains-types)
    ;(test-recur 1)
    ;(pack-a-seq [1 1 2 1 1 1 3 3])

    ;(test-wrong-arg-type)
    ;(test-contains-types)
    ;(test-unsupported-ops)
    ;(test-pop-peek)
    ;(test-assoc)
    ;(test-reversible)
    ;(test-arity)
    ;(test-sorted-collections)
    ;(test-asserts)
    ;(test-asserts-multiple-args)
    ;(test-asserts-multiple-args-map)
    ;(test-filter)
    ;(test-reduce)
    ;(test-function-names)
    ;(test-qmark-bang)
    ;(test-macros-names)
    (test-loop-recur)
    ;(test-bindings)
    ;(test-let)
    (catch Throwable e (println (prettify-exception e)))))
