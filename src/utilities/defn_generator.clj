(ns utilities.defn_generator
   (:require [corefns.assert_handling :refer :all]))


;;the only part of this hash map we still use is :has-type
;;the other parts used to be useful when I was also doing the re-defining, and may yet be useful
(def type-data {
                :arg   {:check nil,                   :has-type "arg",      :argument "arg",  :arg-vec ["arg"]},
                :coll  {:check "check-if-seqable?",   :has-type "seqable",  :argument "coll", :arg-vec ["coll"]},
                :n     {:check "check-if-number?",    :has-type "number",   :argument "n",    :arg-vec ["n"]},
                :colls {:check "check-if-seqables?",  :has-type "seqable",  :argument "args", :arg-vec ["&" "args"]},
                :str   {:check "check-if-string?",    :has-type "string",   :argument "str",  :arg-vec ["str"]},
                :strs  {:check "check-if-strings?",   :has-type "string",   :argument "strs", :arg-vec ["&" "strs"]},
                :f     {:check "check-if-function?",  :has-type "function", :argument "f",    :arg-vec ["f"]},
                :fs    {:check "check-if-functions?", :has-type "function", :argument "fs",   :arg-vec ["&" "fs"]}
                :args  {:check nil,                   :has-type "arg",      :argument "args", :arg-vec ["&" "args"]}})

;;mapping of rich hickey's argument names in doc-strings to a more consistent naming scheme
(def arg-type (merge
                     (zipmap [:coll :c :c1 :c2 :c3 :c4 :c5] (repeat :coll)),
                     (zipmap [:n :number :step :start :end :size] (repeat :n)),
                     (zipmap [:arg :val :argument :x :y] (repeat :arg)),
                     (zipmap [:f :function :pred] (repeat :f)),
                     (zipmap [:fs :functions :preds] (repeat :fs)),
                     (zipmap [:colls :cs] (repeat :colls)),
                     (zipmap [:string :str :s] (repeat :str)),
                     (zipmap [:strs :strings :ss] (repeat :strs))
                     (zipmap [:more :args :vals :arguments :xs :ys] (repeat :args))))

;; returns the index of the last logically false member of the array
;; coll -> number
(defn last-false-index [coll]
  (loop [remaining coll
         i 1
         j 0]
     (if (empty? remaining) j (recur (rest remaining) (inc i) (if (first remaining) j i)))))

;;does the translating from the names rich hickey gave the arguments, to something consistent
;;list of lists of strings -> list of lists of keys
(defn arglists->argtypes [arglists] (map (fn [x] (map #(arg-type (keyword %)) x)) arglists))

;;returns the longest collection in a collection of collections
;;arglists -> arglist
(defn last-arglist [arglists] (first (reverse (sort-by count arglists))))

;;returns trus if an arglists has & ____ as an argument
;;arglists -> boolean
(defn chompable? [arglists] (or (not-any? #(< 1 (count %)) arglists)  (= "&" (name (first (rest (reverse (last-arglist arglists))))))))

;;removes the second to last element of the longest coll in a coll of colls
;;this is the `&` symbol in our case
;;arglists -> arglists
(defn remove-and [arglists]
  (let [sorted-arglists (reverse (sort-by count arglists))
	f-arglists (first sorted-arglists)
	r-arglists (reverse (rest sorted-arglists))]
	(sort-by count (conj r-arglists (into (vec (drop-last 2 f-arglists)) (take-last 1 f-arglists))))))

;;returns the last element of the longest coll in a coll of colls
;;arglists -> keyword
(defn last-arg [arglists] (keyword (str (name (first (reverse (first (reverse (sort-by count arglists)))))))))

;;checks the :type-data of each argument, returning true if they are all the same
;;args -> boolean
(defn same-type? [& args]
	(apply = (map #(:has-type (type-data %)) args)))

;;helper function for chomp-arglists, appends last-arg to the end of the longest coll in arglists
;;arglists -> arglists
(defn append-last-arg [arglists last-arg]
	(conj (vec (rest (reverse (sort-by count arglists)))) (conj (vec (first (reverse (sort-by count arglists)))) last-arg)))

;;removes redundant arguments in arglists
;;arglists -> arglists
(defn chomp-arglists [arglists]
    (let [f-arglists (arglists->argtypes (remove-and arglists))
	  last-arg (first (reverse (first (reverse (sort-by count f-arglists)))))
          ]
      (loop [rem-args f-arglists
             diffs []]
        (cond
          (empty? rem-args) (append-last-arg (vec (filter #(<= (count %) (last-false-index diffs)) f-arglists)) last-arg)
          :else (recur (drop-while empty? (map rest rem-args)) (into diffs (apply map same-type? (conj rem-args [last-arg]))))))))

;;runs chomp-arglist if the & is present, else just translates to our representation
;;arglists -> arglists
(defn chomp-if-necessary [arglists]
	(if (chompable? arglists)
		(chomp-arglists arglists)
		(arglists->argtypes arglists)))

;; outputs a string of generated data for redefining functions with preconditions
;; function -> string
(defn pre-re-defn [fvar]
  (println "pre-re-defn ing: " (:name (meta fvar)))
  (let [fmeta (meta fvar)]
    (str "(re-defn #'" (:ns fmeta) "/" (:name fmeta) " "
      (apply str (vec (interpose " " (map vec (chomp-if-necessary (map #(map name %) (:arglists fmeta))))))) ")")))




;;probably depricated since spec, but below is slightly buggy code to automate the re defining of functions
;;This should be deleted after I am certain it is no longer useful. 
;
;;;adds argument counts to the hashmap being passed around, this is a helper function
;;;for the old way of redefining functions
;;;coll -> coll
;(defn add-counts [coll]
;  (let [cnt-key (fn [k coll] (count (filter #(= % k) coll)))]
;    (loop [coll-keys (keys coll)
;           coll-vals (vals coll)
;           out {}]
;      (if (empty? coll-keys)
;          out
;          (recur (rest coll-keys) (rest coll-vals)
;             (assoc out (first coll-keys) (assoc (first coll-vals) :cnt-str (if (= 1 (cnt-key (first coll-keys) (keys coll))) "" (cnt-key (first coll-keys) coll-keys)))))))))
;
;;; string vector vector -> string
;(defn gen-checks [unqualified-name data]
;  (reduce #(str %1 (str unqualified-name "(check-if-" (:type %2) "? \"" unqualified-name "\" " (:argument %2) (:cnt-str %2))) "" data))
;
;;; string vector -> string
;(defn single-re-defn [fname fnamespace arg-types only]
;  (let [f (symbol fname)
;        unqualified-name  (name f)
;        qualified-name (str  fnamespace "/"  unqualified-name)
;        arg-data (add-counts (clj->ourtypes arg-types))
;	do-apply (if (not (empty? (first arg-types))) (re-matches #".*s$" (first (reverse arg-types))) nil)
;        arg-str (apply str (interpose " " (map #(str (:arg-str %) (:cnt-str %)) arg-data)))
;        checks (gen-checks fname arg-data)
;	]
;        ;(println "arg-vec: " arg-vec)
;    (str "  " (if only "" "(") "[" arg-str "]"
;	(if (= (count checks) 0) "" (str "\n    {:pre [" checks "]}"))
;       "\n" (str "           ("(if do-apply "apply " "") qualified-name " [" arg-str "])")
;       (if only "" ")"))))
;;
;;; takes a function name, and a vector of vectors of arguments
;;; note: arguments DO NOT end in a question mark.
;(defn re-defn [fvar & arglists]
;   (str "(defn " (:name (meta fvar)) "\n  \"" (:doc (meta fvar)) "\""
;       (reduce #(str %1 "\n" (single-re-defn (:name (meta fvar)) (:ns (meta fvar)) %2 (= 1 (count arglists)))) "" arglists) ")"))


