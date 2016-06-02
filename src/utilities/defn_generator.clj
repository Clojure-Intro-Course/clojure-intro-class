(ns utilities.defn_generator)

(defn arg-vec-str [m]
   (loop [s "[argument1"
         n 1]
    (if (= n m)
      (str s "]")
      (recur (str s " argument" (inc n)) (inc n)))))

(defn arg-str [m]
   (loop [s "argument1"
         n 1]
    (if (= n m)
      (str s "")
      (recur (str s " argument" (inc n)) (inc n)))))

(defn arg-vec [m]
   (loop [v []]
    (if (= (count v) m)
      v
      (recur (conj v (str "argument" (inc (count v))))))))

(defn checks [fname args]
  (loop [s ""
         col1 (arg-vec (count args))
         col2 args]
    (cond
      (empty? col2) s
      (nil? (first col2)) (recur s (rest col1) (rest col2))
      :else (recur (str s "(check-if-" (first col2) " \"" fname "\" " (first col1) ")\n        ") (rest col1) (rest col2)))))

(println (checks "repeat" ["number?" "string?" "number?"]))

(println (arg-vec 1))
(println (arg-vec 2))
(println (arg-vec 3))
(println "######################")


(defn pre-defn [fname & args]
  (str "(defn " fname " " (arg-vec-str (count args)) "\n {:pre [" (checks fname args) "]}\n  (clojure.core/" fname " " (arg-str (count args)) "))"))

(defn pre-defn-m [fname & args]
  (str "(" (arg-vec-str (count args)) "\n {:pre [" (checks fname args) "]}\n  (clojure.core/" fname " " (arg-str (count args)) "))"))




(defn re-defn-i [fname & args]
  (let [do-apply (re-matches #".*s" (get (dec (count args))))
        arg-vec (loop [v []]
                    (cond
                          (= (count v) (count args)) v
                          (re-matches #".*s" (first args)) (conj (conj v "&") "args")
                          (recur (conj v (str "argument" (inc (count v)))))))
        arg-str (apply str (interpose " " arg-vec))]



;takes a fully qualified function name, and a vector of vectors of arguments
(defn re-defn [fname & arglists]
  (reduce












(println (re-defn "repeat" "number?" nil "string?"))


(println "######################")

(def arg-types {"col" "seqable", "n" "number",  "s" "string"});;"re" regex,

(println (re-defn "take" "number?" "seqable?"))


(defn pre-re-defn [fname]
  (let [fmeta (meta fname)]
    (if (= 1 (count (:arglists fmeta)))
