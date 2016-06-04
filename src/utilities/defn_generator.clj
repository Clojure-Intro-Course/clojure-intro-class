(ns utilities.defn_generator)

;; translates a function's :arglists metadata so that it can be read by our asset_handling
;; col -> col
(defn clj->ourtypes [col]
  (let [arg-types {"coll" "seqable","c1" "seqable", "c2" "seqable","c3" "seqable","colls" "seqables",
                   "n" "number",  "s" "string", "f" "function", "&" "&"}]
    (vec (map (fn [c] (vec (map #(arg-types (name %)) c))) (into [] col)))))

;; outputs a string of generated data for redefining functions with preconditions
;; function -> string
(defn pre-re-defn [f]
  (let [fmeta (meta f)]
  (str "(re-defn #'" (:ns fmeta) "/" (:name fmeta) " " (apply str (interpose " " (clj->ourtypes (:arglists fmeta)))) ")")))

;; string vector vector -> string
(defn gen-checks [unqualified-name arg-vec arg-types]
  (loop [ s ""
          col1 arg-vec
          col2 arg-types]
        (cond
          (empty? col2)         s  
          (= "&" (first col2))  (if (nil? (first (rest col2)))
				    s 
				    (str s "(check-if-" (first (rest col2)) "? \"" unqualified-name "\" args)\n           "))
          (nil? (first col2))   (recur s (rest col1) (rest col2))
          :else                 (recur (str s "(check-if-" (first col2) "? \"" unqualified-name "\" " (first col1) ")\n           ") (rest col1) (rest col2)))))

;; vec -> vec
(defn make-arg-vec [args]
  (loop [to-vec []
         from-vec args
         cnt 1]
    (cond
      (empty? from-vec) to-vec
      (= "&" (first from-vec)) (conj (conj to-vec "&") "args")
      :else (recur (conj to-vec (str "argument" cnt)) (rest from-vec) (inc cnt)))))

;; string vector -> string
(defn single-re-defn [fname fnamespace arg-types only]
  (let [f (symbol fname)
        unqualified-name  (name f)
        qualified-name (str  fnamespace "/"  unqualified-name)
	toprint (println (:ns (meta f)) "    " (:ns (meta fname)) "     " fname)
	do-apply (if (not (empty? (first arg-types))) (re-matches #".*s$" (first (reverse arg-types))) nil)
        arg-vec  (make-arg-vec arg-types); vector of the arguments, as goes after the name of function
        arg-str (apply str (interpose " " arg-vec)) ; space separated arguments in string form, useful for printing.
        ;does all the check-if-.*\? stuff.
	checks (gen-checks unqualified-name arg-vec arg-types)
	]
    (str "  " (if only "" "(") "[" arg-str "]"
	(if (= (count checks) 0) "" (str "\n    {:pre [" checks "]}"))
       "\n" (if do-apply
              (str "           (apply " qualified-name " [" arg-str "])")
              (str "           (" qualified-name " " arg-str ")"))
       (if only "" ")"))))

;; takes a function name, and a vector of vectors of arguments
;; note: arguments DO NOT end in a question mark.
(defn re-defn [fname & arglists]
  (str "(defn " (:name (meta fname)) "\n  \"" (:doc (meta fname)) "\""
       (reduce #(str %1 "\n" (single-re-defn (:name (meta fname)) (:ns (meta fname)) %2 (= 1 (count arglists)))) "" arglists) ")"))

































