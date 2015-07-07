(ns intro.may2015
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]
            [corefns.collection_fns :refer :all]
            [quil.core :as q]
            [quil.middleware :as m]
            [quil.q_functions :refer :all]
            [errors.errorgui :refer :all]
            [errors.prettify_exception :refer :all]))

(defn -main [& args]
  (try
    (println "Got into the cond")
;;   (cond (= (first args) "tracking")
;;         (println "got here")
;;         (= (first args) "lines")
;;         ()
;;         (= (first args) "beach")
;;         ()
;;         :else
;;         (println "Got through cond"))
  (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))
