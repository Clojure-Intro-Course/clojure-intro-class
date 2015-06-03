  (ns intro.core
  (:use [errors.prettify_exception]
        [seesaw.core])
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]
            [strings.beginner_string_library :refer :all]
            [errors.errorgui :refer :all]
            [intro.student :refer :all]
            [corefns.collection_fns :refer :all]
            [utilities.file_IO :refer :all]
            [errors.prettify_exception :refer :all]
            [clojure.pprint :refer :all]
            ))

(refer 'corefns.corefns)

(defn basic-seesaw-frame []
  (invoke-later
    (native!)
    (def f (frame :title "Hello",
                  :content (button :text "Seesaw Rocks!"),
                  :on-close :exit))
    (-> f pack! show!)))

(defn test-and-continue [quoted-exp]
  (in-ns 'intro.core) ; eval by default evaluates in its own namespace
  (try
   ;(println quoted-exp)
    (eval quoted-exp)
    (catch Throwable e (println (display-error (prettify-exception e))))
    (finally (println "Are there any exceptions left?"))))

(defn test-all-and-continue [quoted-exps]
  ;; doall is needed because map is lazy
  (doall (map test-and-continue quoted-exps)))

(defn test-arithmetic-expressions []
  ;; 3 exceptions thrown:
  (test-all-and-continue '((+ \a 2) (< 'a 8) (/ 5 0))))

;; Some of the error messages below would change once we create enough preconditions
(defn test-sequences []
  (test-all-and-continue
   '(;(nth 0 [1 2 3]) ;; attempted to use collection but number was expected
     ;(nth '(1 2 3) 7) ;; An index in a vector or a list is out of bounds
     (into 6 [1 2]) ;; attempted to use number but collection was expected
     (into {} [1 2 3]) ;; don't know how to create sequence from number. into on a hash-map requires a collection of pairs.
     (into {} [1 2 3 4]) ;; same as above. A correct one would be (into {} [[1 2] [3 4]])
     (conj "a" 2)))) ;; Attempted to use string, but collection was expected.


;##############################################
;### test-our-examples Emma and Lemmon made ###
;##############################################

(defn test-our-examples []
  (test-all-and-continue
   '((string-contains? "emma" :keyword)
     (index-of "emma" "lemmon" \e)
     (index-of "emma" \e \3)
     (doall (concat [:banana] +))
     (+ \a 2)
     (any? odd? :not-a-collection)
     (any? :not-a-predicate [1 2 3])
     (contains-key? :not-a-collection 2)
     (contains-value? :not-a-collection 2)
     (add-last :not-a-collection [1 2 3])
     (add-first :not-a-collection [1 2 3])
     (<= 4 :not-a-number)
     (<= :twenty-two 31)
     (>= 4 :not-a-number)
     (>= :twenty-two 31)
     (> 4 :not-a-number)
     (> :twenty-two 31)
     (< 4 :not-a-number)
     (< :twenty-two 31)
     (concat [1 2] [3 4] [18 22] :keyword)
     (concat :keyword)
     (mapcat reverse [1 2 3] :not-a-collection)
     (mapcat reverse :not-a-collection [8 9 10])
     (mapcat :not-a-function [1 2 3] [8 9 10])
     (filter odd? :bar)
     (filter :foo [1 2 3])
     (nth [0 1 2 3 4] :keyword)
     (nth 9 10)
     (reduce + 2 :not-a-collection)
     (reduce + :argument)
     (reduce :not-a-function [1 2 3])
     (into #{} 90)(into 42 [1 2 3])
     (conj :hi)
     (count 3)
     (map + :hello)
     (map \o [1 2 3]))))

(defn try-counts []
  (test-all-and-continue
   '((count)
     (count [] [] []))))

(defn -main [& args]
  (try


    ;(map 2 3)
    ;(print (map #(+ %1 %2) [1 2 3 4]))
    ;(map)

    ;(exercise3 "hello" 12)

    (try
      (print (load-file "src/intro/may2015.clj"))
    (catch Throwable e (print (str "itself:" (class e) ", cause: " (class (.getCause e)))) (throw e)))

    (catch Throwable e (display-error (prettify-exception e)))))

