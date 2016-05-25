(ns intro.core
  (:use [errors.prettify_exception]
        [seesaw.core])
  (:require [expectations :refer :all]
            ;[corefns.corefns :refer :all]
            [strings.beginner_string_library :refer :all]
            [errors.errorgui :refer :all]
            [intro.student :refer :all]
            [corefns.collection_fns :refer :all]
            [utilities.file_IO :refer :all]
            [errors.prettify_exception :refer :all]
            [clojure.pprint :refer :all]
            ))

(defn -main [& args]
  (try
    ;(clojure.string/splt "/home/stock424/Documents/code/clojure-intro-class/src/intro/may2015.clj" #"/")
   ; (try
    (if (= (first args) "s")
      (print (load-file "src/intro/test_standard.clj"))
      (print (load-file "src/intro/test.clj")))
    ;(catch Throwable e (print (str "itself:" (class e) ", cause: " (class (.getCause e)))) (throw e)))

    (catch Throwable e ;(print (str "itself:" (class e) ", cause: " (class (.getCause e))))
      (if (= (first args) "s")
        (display-error (standard e))
        (display-error (prettify-exception e))))))

