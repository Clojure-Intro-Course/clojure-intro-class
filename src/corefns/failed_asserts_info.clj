(ns corefns.failed_asserts_info)

;; a global hashmap of recorded types/messages
(def seen-failed-asserts (atom {}))

(defn add-to-seen
  "adds bindings to seen objects"
  [binds]
  (swap! seen-failed-asserts merge binds)) ; merge overwrites the same fields but adds new ones

(defn empty-seen
  "removes all bindings from seen objects"
  []
  (swap! seen-failed-asserts {}))
