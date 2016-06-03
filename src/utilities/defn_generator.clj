(ns utilities.defn_generator)





(defn gen-checks [unqualified-name arg-vec arg-types]
  (loop [ s ""
          col1 arg-vec
          col2 arg-types]
        (cond
          (empty? col2) s
          (re-matches #"\&" (first col1)) (recur s (rest col1) col2)
          (nil? (first col2)) (recur s (rest col1) (rest col2))
          :else (recur (str s "(check-if-" (first col2) "? \"" unqualified-name "\" " (first col1) ")\n           ") (rest col1) (rest col2)))))

(defn make-arg-vec [arg-types]
  (loop [v []]
        (cond
          (= (count v) (count arg-types)) v
          (re-matches #".*s$" (first arg-types)) (conj (conj v "&") "args")
          :else (recur (conj v (str "argument" (inc (count v))))))))

(defn single-re-defn [fname arg-types]
  (let [f (symbol fname)
        unqualified-name  (name f)
        qualified-name (str (ns f) "/" unqualified-name)
        do-apply (re-matches #".*s$" (get arg-types (dec (count arg-types)))) ; do we use apply? this is where it's decided
        arg-vec  (make-arg-vec arg-types); vector of the arguments, as goes after the name of function
        arg-str (apply str (interpose " " arg-vec)) ; space separated arguments in string form, useful for printing.
        ;does all the check-if-.*\? stuff.
        ]

    (str "  ([" arg-str "]"
       "\n    {:pre [" (gen-checks unqualified-name arg-vec arg-types) "]}"
       "\n" (if do-apply
              (str "           (apply " qualified-name " [" arg-str "])")
              (str "           (" qualified-name " " arg-str ")"))
       ")")))


;; takes a function name, and a vector of vectors of arguments
;; note: arguments DO NOT end in a question mark.
(defn re-defn [fname & arglists]
  (str "(defn " (name (symbol fname)) "\n" (reduce #(str %1 "\n" (single-re-defn fname %2)) "" arglists) ")"))


;; translates a function's :arglists metadata so that it can be read by our asset_handling
;; col -> col
(defn- clj->ourtypes [col]
  (let [arg-types {"coll" "seqable","c1" "seqable", "c2" "seqable","c3" "seqable","colls" "seqables",
                   "n" "number",  "s" "string", "f" "function"}]
    (vec (map (fn [c] (vec (map #(arg-types (name %)) c))) (into [] col)))))


;; outputs a string of generated data for redefining functions with preconditions
;; function -> string
(defn pre-re-defn [f]
  (let [fmeta (meta f)]
  (str "(re-defn " (:ns fmeta) "/" (:name fmeta) " " (apply str (interpose " " (clj->ourtypes (:arglists fmeta)))) ")")))



























