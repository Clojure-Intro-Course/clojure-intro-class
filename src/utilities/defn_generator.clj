(ns utilities.defn_generator
   (:require [corefns.assert_handling :refer :all]))

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

(def arg-type (merge
                     (zipmap [:coll :c :c1 :c2 :c3 :c4 :c5] (repeat :coll)),
                     (zipmap [:n :number :step :start :end] (repeat :n)),
                     (zipmap [:arg :argument :x :y] (repeat :arg)),
                     (zipmap [:f :function :pred] (repeat :f)),
                     (zipmap [:fs :functions :preds] (repeat :fs)),
                     (zipmap [:colls :cs] (repeat :colls)),
                     (zipmap [:string :str :s] (repeat :str)),
                     (zipmap [:strs :strings :ss] (repeat :strs))
                     (zipmap [:args :arguments :xs :ys] (repeat :args))))

(defn add-counts [coll]
  (let [cnt-key (fn [k coll] (count (filter #(= % k) coll)))]
    (loop [coll-keys (keys coll)
           coll-vals (vals coll)
           out {}]
      (if (empty? coll-keys)
          out
          (recur (rest coll-keys) (rest coll-vals)
             (assoc out (first coll-keys) (assoc (first coll-vals) :cnt-str (if (= 1 (cnt-key (first coll-keys) (keys coll))) "" (cnt-key (first coll-keys) coll-keys)))))))))

(defn clj->ourtypes [c-type] ((keyword c-type) arg-type))

;; returns the index of the last logically false member of the array
(defn last-false-index [coll]
  (loop [remaining coll
         i 1
         j 0]
     (if (empty? remaining) j (recur (rest remaining) (inc i) (if (first remaining) j i)))))


;list of lists of strings -> list of lists of keys
(defn arglists->argtypes [arglists] (map (fn [x] (map #(arg-type (keyword %)) x)) arglists))

(defn last-arglist [arglists] (first (reverse (sort-by count arglists))))

(defn chompable? [arglists] (re-matches #".*s$" (name (first (reverse (last-arglist arglists))))))

(defn remove-and [arglists]
  (let [sorted-arglists (reverse (sort-by count arglists))
	f-arglists (first sorted-arglists)
	r-arglists (reverse (rest sorted-arglists))]
	(sort-by count (conj r-arglists (into (vec (drop-last 2 f-arglists)) (take-last 1 f-arglists))))))

(defn last-arg
  ([arglists]
   (keyword (str (name (first (reverse (first (reverse (sort-by count arglists)))))))))
  ([arglists property]
   (property (arg-type (keyword (str (name (first (reverse (first (reverse (sort-by count arglists)))))))))))
  )

(defn same-type? [& args]
	;(println "same typed on: " args)
	(apply = (map #(:has-type (type-data %)) args)))

(defn chomp-arglists [arglists]
    (let [f-arglists (arglists->argtypes (remove-and arglists))
	  last-arg (first (reverse (first (reverse (sort-by count f-arglists)))))
          ]
      (loop [rem-args f-arglists
             diffs []]
	;(println "rem-args: " rem-args)
	;(println "                                                                     diffs: " diffs)
        (cond
          (empty? rem-args) (vec (filter #(<= (count %) (last-false-index diffs)) f-arglists))
          :else (recur (drop-while empty? (map rest rem-args)) (into diffs (apply map same-type? (conj rem-args [last-arg]))))))))


;; outputs a string of generated data for redefining functions with preconditions
;; function -> string
(defn pre-re-defn [fvar]
  (let [fmeta (meta fvar)]
    (str "(re-defn #'" (:ns fmeta) "/" (:name fmeta) " "
      (apply str (vec (interpose " " (map vec (chomp-arglists (map #(map name %) (:arglists fmeta))))))) ")")))




;;probably depricated since spec, but below is slightly buggy code to automate the re defining of functions
;;; string vector vector -> string
;
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

































