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


(def type-data {:and {:check nil,:argument nil,:arg-vec "&"},
                :arg {:check nil,:argument "arg",:arg-vec "arg"},
                :coll {:check "seqable",:argument "coll",:arg-vec "coll"},
                :n {:check "number",:argument "n",:arg-vec "n"},
                :colls {:check "seqables",:argument "args",:arg-vec "args"},
                :str {:check "string",:argument "str",:arg-vec "str"},
                :strs {:check "strings",:argument "strs",:arg-vec "strs"},
                :f {:check "function",:argument "f",:arg-vec "f"},
                :fs {:check "functions",:argument "fs",:arg-vec "fs"}})

(def arg-type (merge (zipmap [:and] (repeat :and)),
                     (zipmap [:coll :c] (repeat :coll)),
                     (zipmap [:n :number :step :start :end] (repeat :n)),
                     (zipmap [:arg :argument :x :y] (repeat :arg)),
                     (zipmap [:f :function :pred] (repeat :f)),
                     (zipmap [:fs :functions :preds] (repeat :fs)),
                     (zipmap [:colls] (repeat :colls)),
                     (zipmap [:string :str :s] (repeat :str)),
                     (zipmap [:strs :strings :ss] (repeat :strs))))

;(defn  s-cnt [coll s] (if (= "" s) 0 (count (filter #(re-matches (java.util.regex.Pattern/compile (str s "[0123456789]*")) %) coll))))
;(defn cnt-item [item coll] (count (filter #(= % item) coll)))

(defn add-counts [coll]
  (let [cnt-key (fn [k coll] (count (filter #(= % k) coll)))]
    (loop [coll-keys (keys coll)
           coll-vals (vals coll)
           out {}]
      (if (empty? coll-keys)
          out
          (recur (rest coll-keys) (rest coll-vals) 
             (assoc out (first coll-keys) (assoc (first coll-vals) :cnt-str (if (= 1 (cnt-key (first coll-keys) (keys coll))) "" (cnt-key (first coll-keys) coll-keys)))))))))
    
(defn clj->ourtypes [c-type] 
  (if (= "&" c-type)
    (:and arg-type)
    (((keyword (replace #"[123456890]*$")) arg-type) type-data))) 

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
          ;big-arglist (filter #(some "&" %) arglists)
          homo-args   (map (fn [coll] (into [] (comp
                                                 #_ (map #(clojure.string/replace % #"[12345678990]*s?$" ""))
                                                 #_ (filter #(not (= "&" %)))
                                                 (map arg-type)
                                                 (filter #(not (= "&" %)))
                                                 #_ (map name)) coll)) arglists)
          last-arg    (first (reverse (first (reverse (sort-by count homo-args)))))
         ; printer (println "last-arg is: " last-arg homo-args)
          ]
          ;(println "homo-args is: " homo-args)
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

(defn gen-checks [unqualified-name data] 
  (reduce #(str %1 (str unqualified-name "(check-if-" (:type %2) "? \"" unqualified-name "\" " (:argument %2) (:cnt-str %2))) "" data))

;; string vector -> string
(defn single-re-defn [fname fnamespace arg-types only]
  (let [f (symbol fname)
        unqualified-name  (name f)
        qualified-name (str  fnamespace "/"  unqualified-name)
        arg-data (add-counts (clj->ourtypes arg-types))
	do-apply (if (not (empty? (first arg-types))) (re-matches #".*s$" (first (reverse arg-types))) nil)
        arg-vec  (map :arg-str arg-data)
        arg-str (apply str (interpose " " arg-vec))
        checks (gen-checks fname arg-data)
	]
        ;(println "arg-vec: " arg-vec)
    (str "  " (if only "" "(") "[" arg-str "]"
	(if (= (count checks) 0) "" (str "\n    {:pre [" checks "]}"))
       "\n" (str "           ("(if do-apply "apply " "") qualified-name " [" arg-str "])")
       (if only "" ")"))))

;; takes a function name, and a vector of vectors of arguments
;; note: arguments DO NOT end in a question mark.
(defn re-defn [fvar & arglists]
   (str "(defn " (:name (meta fvar)) "\n  \"" (:doc (meta fvar)) "\""
       (reduce #(str %1 "\n" (single-re-defn (:name (meta fvar)) (:ns (meta fvar)) %2 (= 1 (count arglists)))) "" arglists) ")"))

































