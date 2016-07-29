(ns corefns.collection_fns
  (:require [corefns.assert_handling :refer :all]
            [corefns.failed_asserts_info :refer :all]))


;;; Beginner-friendly functions for type-independent sequence handling.


(defn add-first [argument1 argument2]
  {:pre [(check-if-seqable? "add-first" argument1)]}
  (cons argument2 argument1))

(defn add-last [argument1 argument2]
  {:pre [(check-if-seqable? "add-last" argument1)]}
  (doall (concat argument1 [argument2])))

;; user-friendly versions of confusing functions
(defn contains-value? [coll x]
  {:pre [(check-if-seqable? "contains-value?" coll 1)]}
	(let [values (if (map? coll) (vals coll) coll)]
		(not (every? #(not= x %) values))))

(defn contains-key? [coll key]
  {:pre [(check-if-seqable? "contains-key?" coll 1)]}
  (clojure.core/contains? coll key))

;; more content tests
(defn any? [pred coll] ;;unsure how to check if somethings a predicate
  {:pre [(check-if-function? "any?" pred 1)
         (check-if-seqable? "any?" coll 2)]}
  (not (not-any? pred coll)))
    ; yes, I know :-(
;(defn some? [pred coll]
;  (not (not-any? pred coll)))
