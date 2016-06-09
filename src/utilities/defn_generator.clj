(ns utilities.defn_generator
   (:require [corefns.assert_handling :refer :all]))

;; translates a function's :arglists metadata so that it can be read by our asset_handling
;; col -> col
(def clj->ourtypes {"coll" "seqable","c" "seqable","c1" "seqable", "c2" "seqable","c3" "seqable","colls" "seqables",
                    "n" "number",  "s" "string", "f" "function", "&" "&", "start" "number", 
                    "end" "number", "pred" "function", "step" "number","more" "args","x" "arg", "y" "arg"
                    "seqable" "seqable", "number" "number", "function" "function", "seqables" "seqables",
                    "string" "string", "strings" "strings", "functions" "functions", "numbers" "numbers"
                    "arg" "arg","arg1" "arg","args" "args","arg2" "arg","arg3" "arg","arg4" "arg"})

;; returns the index of the last logically false member of the array
(defn last-false-index [coll]
  (loop [remaining coll
         i 1
         j 0]
     (if (empty? remaining) j (recur (rest remaining) (inc i) (if (first remaining) j i)))))

;; appends (str last-arg "s") to the end of coll, 
(defn assemble-args [coll last-arg]
   (let [sorted-args (reverse (sort-by count coll))]
   (conj (vec (reverse (rest sorted-args))) (conj (conj (vec (first sorted-args)) "&") (str last-arg "s")))))

;;removes redundant information from a clojure docstring :arglists
(defn chomp-arglists [arglists]
  (if (re-matches #".*s$" (name (first (reverse (first (reverse (sort-by count arglists)))))))
    (let [;moreprinter (println "got here")
          big-arglist (filter #(some "&" %) arglists)
          homo-args   (map (fn [coll] (into [] (comp
                                                 (map #(clojure.string/replace % #"[12345678990]*s?$" ""))
                                                 (filter #(not (= "&" %)))
                                                 (map clj->ourtypes)
                                                 #_ (map name)) coll)) arglists)
          last-arg    (first (reverse (first (reverse (sort-by count homo-args)))))
         ; printer (println "last-arg is: " last-arg homo-args)
          ]
          (println "homo-args is: " homo-args)
       (loop [rem-args homo-args
              diffs []]
         ;why is everything so true?!
         (cond 
           (empty? rem-args) (assemble-args (vec (filter #(<= (count %) (last-false-index diffs)) homo-args)) last-arg)
           :else (recur (filter #(not (empty? %)) (map rest rem-args)) (into diffs (apply map = (conj rem-args [last-arg])))))))
        arglists))

;; outputs a string of generated data for redefining functions with preconditions
;; function -> string
(defn pre-re-defn [fvar]
  (let [fmeta (meta fvar)]
    (str "(re-defn #'" (:ns fmeta) "/" (:name fmeta) " " 
      (apply str (vec (interpose " " (map vec (chomp-arglists (map #(map name %) (:arglists fmeta))))))) ")")))

;; string vector vector -> string
(defn gen-checks [unqualified-name arg-vec arg-types]
  (let [skip ["&" "args" "nil" "arg"]]
    (loop [ s ""
            col1 arg-vec
            col2 (map clj->ourtypes arg-types)]
 ;         (println "arg-vec: " arg-vec " arg-types: " arg-types)
          (cond
            (empty? col2)            s
            (or (nil? (first col2)) (nil? (first col1)) (some #(= % (first col1)) skip) (some #(= % (first col2)) skip))   (recur s (rest col1) (rest col2))
            :else                    (recur (str s "(check-if-" (first col2) "? \"" unqualified-name "\" " (first col1) 
                                        (if (re-matches #".*s$" (first col2)) (str " " (- (count arg-vec) (count col2)))  "") ")\n           ") (rest col1) (rest col2))))))

(def arg-names {"f" "f","val" "arg","coll" "coll","x" "arg","y" "arg","seqable" "coll","seqables" "colls","number" "n","function" "f","string" "s","arg" "arg",nil "arg","nil" "arg","&" "&","more" "args", "args" "args"})

(defn  s-cnt [coll s] (if (= "" s) 0 (count (filter #(re-matches (java.util.regex.Pattern/compile (str s "[0123456789]*")) %) coll))))

;; vector -> vector
(defn make-arg-vec [args]
;     (println "args in make-arg-vec: " args)
     (loop [to-vec []
            from-vec (map arg-names args)]
;     (println "make-arg-vec: " args " from-vec: " from-vec)
       (cond
         (empty? from-vec) to-vec
         :else (recur (conj to-vec (str (first from-vec) 
                                     (if 
                                       (and (= 0 (s-cnt to-vec (first from-vec))) (not (some #(= (first from-vec) %) (rest from-vec)))) 
                                       "" 
                                       (inc (s-cnt to-vec (first from-vec)))))) (rest from-vec)))))

;; string vector -> string
(defn single-re-defn [fname fnamespace arg-types only]
  (let [f (symbol fname)
        unqualified-name  (name f)
        qualified-name (str  fnamespace "/"  unqualified-name)
	do-apply (if (not (empty? (first arg-types))) (re-matches #".*s$" (first (reverse arg-types))) nil)
        arg-vec  (make-arg-vec arg-types); vector of the arguments, as goes after the name of function
        arg-str (apply str (interpose " " arg-vec)) ; space separated arguments in string form, useful for printing.
        ;does all the check-if-.*\? stuff.
	checks (gen-checks unqualified-name arg-vec arg-types)
	]
        ;(println "arg-vec: " arg-vec)
    (str "  " (if only "" "(") "[" arg-str "]"
	(if (= (count checks) 0) "" (str "\n    {:pre [" checks "]}"))
       "\n" (if do-apply
              (str "           (apply " qualified-name " [" arg-str "])")
              (str "           (" qualified-name " " arg-str ")"))
       (if only "" ")"))))

;; takes a function name, and a vector of vectors of arguments
;; note: arguments DO NOT end in a question mark.
(defn re-defn [fvar & arglists]
   (str "(defn " (:name (meta fvar)) "\n  \"" (:doc (meta fvar)) "\""
       (reduce #(str %1 "\n" (single-re-defn (:name (meta fvar)) (:ns (meta fvar)) %2 (= 1 (count arglists)))) "" arglists) ")"))

































