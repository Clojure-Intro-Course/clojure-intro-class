(ns utilities.stacktrace_functions
  (:require [expectations :refer :all]
            [corefns.collection_fns :refer :all]))

;; a helper function to test for either nil or false
(defn not-true? [x] (not (true? x)))

;###############################
;### Checks a single element ###
;###############################

;### Item exists in trace elem ###

(defn helper-trace-elem-has-function? [fun trace-elem]
  (= fun (:fn trace-elem)))

(defn trace-elem-has-function? [fun]
  (partial helper-trace-elem-has-function? fun))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

(defn helper-trace-elem-has-namespace? [name-space trace-elem]
  (= name-space (:ns trace-elem)))

(defn trace-elem-has-namespace? [name-space]
  (partial helper-trace-elem-has-namespace? name-space))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
.
(defn helper-trace-elem-has-pair? [k v trace-elem]
  (= v (k trace-elem)))

(defn trace-elem-has-pair? [k v]
  (partial helper-trace-elem-has-pair? k v))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

(defn helper-trace-elem-has-all-pairs? [kv-pairs trace-elem]
  "checks that every binding in kv-pairs also appears in trace-elem"
  (every? true? (map #(helper-trace-elem-has-pair? (first %) (second %) trace-elem) kv-pairs)))

(defn trace-elem-has-all-pairs? [kv-pairs]
  (partial helper-trace-elem-has-all-pairs? kv-pairs))

;### Item doesn't exists in trace elem ###

(defn helper-trace-elem-doesnt-have-function? [fun trace-elem]
  (not (helper-trace-elem-has-function? fun trace-elem)))

(defn trace-elem-doesnt-have-function? [fun]
  (partial helper-trace-elem-doesnt-have-function? fun))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

(defn helper-trace-elem-doesnt-have-namespace? [name-space trace-elem]
  (not (helper-trace-elem-has-namespace? name-space trace-elem)))

(defn trace-elem-doesnt-have-namespace? [name-space]
  (partial helper-trace-elem-doesnt-have-namespace? name-space))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

(defn helper-trace-elem-doesnt-have-pair? [k v trace-elem]
  (not (helper-trace-elem-has-pair? k v trace-elem)))

(defn trace-elem-doesnt-have-pair? [k v]
  (partial helper-trace-elem-doesnt-have-pair? k v))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
(defn helper-trace-elem-doesnt-have-all-pairs? [kv-pairs trace-elem]
  "checks that every binding in kv-pairs also appears in trace-elem"
  (not (helper-trace-elem-has-all-pairs? kv-pairs trace-elem)))

(defn trace-elem-doesnt-have-all-pairs? [kv-pairs]
  (partial helper-trace-elem-doesnt-have-all-pairs? kv-pairs))

;#################################
;### Checks a whole stacktrace ###
;#################################

;; a helper function to test the size of a stacktrace
(defn helper-stack-count? [n stacktrace]
  (= n (count stacktrace)))

(defn check-stack-count? [n]
  (partial helper-stack-count? n))

;### Item exists in stacktrace ###

(defn helper-trace-has-function? [fun trace]
  (any? (trace-elem-has-function? fun) trace))

(defn trace-has-function? [fun]
  (partial helper-trace-has-function? fun))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

(defn helper-trace-has-namespace? [name-space trace]
  (any? (trace-elem-has-namespace? name-space) trace))

(defn trace-has-namespace? [name-space]
  (partial helper-trace-has-namespace? name-space))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

(defn helper-trace-has-pair? [k v trace]
  (any? (trace-elem-has-pair? k v) trace))

(defn trace-has-pair? [k v]
  (partial helper-trace-has-pair? k v))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

(defn helper-trace-has-all-pairs? [kv-pairs trace]
  (any? (trace-elem-has-all-pairs? kv-pairs) trace))

(defn trace-has-all-pairs? [kv-pairs]
  (partial helper-trace-has-all-pairs? kv-pairs))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

(defn helper-top-elem-has-pair? [k v trace]
  (helper-trace-elem-has-pair? k v (first trace)))

(defn top-elem-has-pair? [k v]
  (partial helper-top-elem-has-pair? k v))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

(defn helper-top-elem-has-all-pairs? [kv-pairs trace]
  (helper-trace-elem-has-all-pairs? kv-pairs (first trace)))

(defn top-elem-has-all-pairs? [kv-pairs]
  (partial helper-top-elem-has-all-pairs? kv-pairs))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

(defn helper-nth-elem-has-pair? [n k v trace]
  (helper-trace-elem-has-pair? k v (nth trace n)))

(defn nth-elem-has-pair? [n k v]
  (partial helper-nth-elem-has-pair? n k v))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

(defn helper-nth-elem-has-all-pairs? [n kv-pairs trace]
  (helper-trace-elem-has-all-pairs? kv-pairs (nth trace n)))

(defn nth-elem-has-all-pairs? [n kv-pairs]
  (partial helper-nth-elem-has-all-pairs? n kv-pairs))

;### Item doesn't exists in stacktrace ###

(defn helper-trace-doesnt-have-function? [fun trace]
  (not (helper-trace-has-function? fun trace)))

(defn trace-doesnt-have-function? [fun]
  (partial helper-trace-doesnt-have-function? fun))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

(defn helper-trace-doesnt-have-namespace? [name-space trace]
  (not (helper-trace-has-namespace? name-space trace)))

(defn trace-doesnt-have-namespace? [name-space]
  (partial helper-trace-doesnt-have-namespace? name-space))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

(defn helper-trace-doesnt-have-pair? [k v trace]
  (not (helper-trace-has-pair? k v trace)))

(defn trace-doesnt-have-pair? [k v]
  (partial helper-trace-doesnt-have-pair? k v))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

(defn helper-trace-doesnt-have-all-pairs? [kv-pairs trace]
  (not (helper-trace-has-all-pairs? kv-pairs trace)))

(defn trace-doesnt-have-all-pairs? [kv-pairs]
  (partial helper-trace-doesnt-have-all-pairs? kv-pairs))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
