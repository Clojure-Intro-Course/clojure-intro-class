(ns utilities.defn_generator)

;(defn arg-vec-str [m]
;   (loop [s "[argument1"
;         n 1]
;    (if (= n m)
;      (str s "]")
;      (recur (str s " argument" (inc n)) (inc n)))))

;(defn arg-str [m]
;   (loop [s "argument1"
;         n 1]
;    (if (= n m)
;      (str s "")
;      (recur (str s " argument" (inc n)) (inc n)))))

;(defn arg-vec [m]
;   (loop [v []]
;    (if (= (count v) m)
;      v
;      (recur (conj v (str "argument" (inc (count v))))))))

;(defn checks [fname args]
;  (loop [s ""
;         col1 (arg-vec (count args))
;         col2 args]
;    (cond
;      (empty? col2) s
;      (nil? (first col2)) (recur s (rest col1) (rest col2))
;      :else (recur (str s "(check-if-" (first col2) "? \"" fname "\" " (first col1) ")\n        ") (rest col1) (rest col2)))))

;(println (checks "repeat" ["number?" "string?" "number?"]))

;(println (arg-vec 1))
;(println (arg-vec 2))
;(println (arg-vec 3))
;(println "######################")


;(defn pre-defn [fname & args]
;  (str "(defn " fname " " (arg-vec-str (count args)) "\n {:pre [" (checks fname args) "]}\n  (clojure.core/" fname " " (arg-str (count args)) "))"))

;(defn pre-defn-m [fname & args]
;  (str "(" (arg-vec-str (count args)) "\n {:pre [" (checks fname args) "]}\n  (clojure.core/" fname " " (arg-str (count args)) "))"))



;; the main part of the function, pumps an arg vector into all the boiler plate stuff
(defn foo [bar] (inc bar))

(defn re-defn-i [fname args]
  (let [f (symbol fname)
        unqualified-name  (name f)
        qualified-name (str (ns f) "/" unqualified-name)
        do-apply (re-matches #".*s$" (get args (dec (count args)))) ; do we use apply? this is where it's decided
        arg-vec (loop [v []]
                    (cond
                          (= (count v) (count args)) v
                          (re-matches #".*s$" (first args)) (conj (conj v "&") "args")
                          :else (recur (conj v (str "argument" (inc (count v))))))) ; vector of the arguments, as goes after the name of function
        arg-str (apply str (interpose " " arg-vec)) ; space separated arguments in string form, useful for printing.
        ;does all the check-if-.*\? stuff.
        checks (loop [ s ""
                       col1 arg-vec
                       col2 args]
                (cond
                  (empty? col2) s
                  (re-matches #"\&" (first col1)) (recur s (rest col1) col2)
                  (nil? (first col2)) (recur s (rest col1) (rest col2))
                  :else (recur (str s "(check-if-" (first col2) "? \"" fname "\" " (first col1) ")\n           ") (rest col1) (rest col2))))]
    (str "  ([" arg-str "]"
       "\n    {:pre [" checks "]}"
       "\n" (if do-apply
              (str "(apply clojure.core/" fname " [" arg-str "])")
              (str "(clojure.core/" fname " " arg-str ")"))
       ")")))


;; takes a function name, and a vector of vectors of arguments
;; note: arguments DO NOT end in a question mark.
(defn re-defn [fname & arglists]
  (str "(defn " (name (symbol fname)) "\n" (reduce #(str %1 "\n" (re-defn-i fname %2)) "" arglists) ")"))


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



























