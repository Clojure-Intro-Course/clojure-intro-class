(ns intro.text
  (:require [corefns.corefns :refer :all]
            [corefns.collection_fns :refer :all]))

; Hashmap indexed by questions
;   R  - Racket
;   CM - clojure modified
;   CS - clojure standard

; tags
;  :solved: represent if the participant solved the problem or not
;       | a boolean value (true, false)
;  :min: represent the minuate when the participant solved the problem
;       | an integer value
;  :sec: represent the second when the participant solved the problem
;       | an integer value
;  :right?: represent the percentage of time thPersistentArrayMapat the participant approached the problem in the right way
;       | a symbol (:all, :most, :some, :never)
;  :runs: represent the number of runs
;       | an integer value
;  :no-error: represent if the participant saw the message or not
;       | a boolean value (true, false)
;  :time-adj: represent the amount of time we need to adjust in case of unexpected situations (optional)
;       | an integer value

; total time, total number of questions, the number of questions a person solved, R/CM/CS, -> one set of comparions
; pull the data from each question (question by question basis) -> the total time for this question, performance according to the type of error message


(def Myeongjae-S-R
  ["R0-3" {:solved true, :min 0, :sec 42, :right? :all, :runs 1, :no-error true}
   "R0-4" {:solved true, :min 4, :sec 20, :right? :all, :runs 4, :no-error false}
   "R1-2" {:solved true, :min 5, :sec 34, :right? :all, :runs 2, :no-error false}
   "R1-3" {:solved true, :min 8, :sec 26, :right? :all, :runs 2, :no-error false}
   "R2-2" {:solved true, :min 8, :sec 57, :right? :all, :runs 2, :no-error false}
   "R2-4" {:solved true, :min 10, :sec 23, :right? :all, :runs 2, :no-error false}
   "R3-1" {:solved true, :min 13, :sec 38, :right? :all, :runs 2, :no-error false}
   "R3-2" {:solved true, :min 14, :sec 41, :right? :all, :runs 2, :no-error false}]) ; There was an error in one of the tests for R3-2, but we need to drop this anyway

(def Myeongjae-S-CM
  ["CM0-1" {:solved true, :min 1, :sec 26, :right? :all, :runs 2, :no-error false, :time-adj -25}
   "CM0-2" {:solved true, :min 2, :sec 33, :right? :all, :runs 2, :no-error false}
   "CM1-1" {:solved true, :min 4, :sec 14, :right? :all, :runs 2, :no-error false}
   "CM1-4" {:solved true, :min 6, :sec 51, :right? :all, :runs 2, :no-error false}
   "CM2-1" {:solved true, :min 13, :sec 35, :right? :all, :runs 3, :no-error false}
   "CM2-3" {:solved true, :min 14, :sec 36, :right? :all, :runs 2, :no-error false}
   "CM3-3" {:solved true, :min 15, :sec 4, :right? :all, :runs 1, :no-error true} ; There was no error in the code
   "CM3-4" {:solved true, :min 17, :sec 0, :right? :all, :runs 2, :no-error false, :time-adj -42}])

(def Richard-S-R
  ["R0-1" {:solved true, :min 0, :sec 40, :right? :all, :runs 2, :no-error false}
   "R0-2" {:solved true, :min 1, :sec 23, :right? :all, :runs 2, :no-error false}
   "R1-2" {:solved true, :min 3, :sec 40, :right? :most, :runs 4, :no-error false}
   "R1-4" {:solved true, :min 5, :sec 4, :right? :all, :runs 2, :no-error false}
   "R2-2" {:solved true, :min 5, :sec 39, :right? :all, :runs 2, :no-error false}
   "R2-4" {:solved true, :min 8, :sec 3, :right? :all, :runs 4, :no-error false}
   "R3-1" {:solved true, :min 11, :sec 35, :right? :all, :runs 2, :no-error false}
   "R3-3" {:solved true, :min 19, :sec 58, :right? :all, :runs 3, :no-error false}])

(def Richard-S-CS
  ["CS0-3" {:solved true, :min 3, :sec 43, :right? :all, :runs 3, :no-error false}
   "CS0-4" {:solved true, :min 8, :sec 11, :right? :all, :runs 3, :no-error false}
   "CS1-1" {:solved true, :min 9, :sec 30, :right? :most, :runs 2, :no-error false}
   "CS1-3" {:solved true, :min 12, :sec 58, :right? :all, :runs 3, :no-error false}
   "CS2-1" {:solved true, :min 15, :sec 31, :right? :all, :runs 2, :no-error false}
   "CS2-3" {:solved true, :min 16, :sec 30, :right? :all, :runs 1, :no-error true}
   "CS3-2" {:solved true, :min 19, :sec 45, :right? :all, :runs 3, :no-error false}
   "CS3-4" {:solved false, :min 21, :sec 0, :right? :all, :runs 1, :no-error false}])

(def Laverne-S-R
  ["R0-1" {:solved true, :min 0, :sec 23, :right? :all, :runs 2, :no-error false}
   "R0-2" {:solved true, :min 1, :sec 6, :right? :all, :runs 2, :no-error false}
   "R1-3" {:solved true, :min 2, :sec 55, :right? :all, :runs 3, :no-error false}
   "R1-4" {:solved true, :min 4, :sec 9, :right? :all, :runs 2, :no-error false}
   "R2-1" {:solved true, :min 5, :sec 50, :right? :all, :runs 3, :no-error false}
   "R2-3" {:solved true, :min 6, :sec 37, :right? :all, :runs 2, :no-error false}
   "R3-2" {:solved true, :min 7, :sec 21, :right? :all, :runs 2, :no-error false}
   "R3-4" {:solved true, :min 8, :sec 32, :right? :all, :runs 3, :no-error false, :time-adj -39}])

(def Laverne-S-CM
  ["CM0-3" {:solved true, :min 2, :sec 0, :right? :all, :runs 2, :no-error false}
   "CM0-4" {:solved true, :min 3, :sec 6, :right? :all, :runs 2, :no-error false}
   "CM1-1" {:solved true, :min 5, :sec 9, :right? :all, :runs 2, :no-error false}
   "CM1-2" {:solved true, :min 7, :sec 11, :right? :all, :runs 2, :no-error false}
   "CM2-2" {:solved true, :min 8, :sec 33, :right? :all, :runs 2, :no-error false}
   "CM2-4" {:solved true, :min 10, :sec 24, :right? :all, :runs 3, :no-error false}
   "CM3-1" {:solved false, :min 17, :sec 11, :right? :all, :runs 5, :no-error false}
   "CM3-3" {:solved true, :min 19, :sec 38, :right? :all, :runs 2, :no-error false}
   "CM3-1-re-1" {:solved true, :min 23, :sec 38, :right? :all, :runs 4, :no-error false}])

(def qTables (list Richard-S-R, Richard-S-CS,
              Laverne-S-R, Laverne-S-CM,
              Myeongjae-S-R, Myeongjae-S-CM))



(defn sortAllQData [q]

  (def not-nil? (complement nil?))

    (concat

        (filter not-nil?


                (map

                  (fn [x] ((apply assoc {} x)
                           (str "R" q)))
                  qTables))

        (filter not-nil?

                (map

                  (fn [x] ((apply assoc {} x)
                           (str "CM" q)))
                  qTables))

        (filter not-nil?

                (map

                  (fn [x] ((apply assoc {} x)
                           (str "CS" q)))
                  qTables))

    )
)

(defn compareAllQData [q]

  (println

    (count (filter (fn [x] (:solved x))
                   (sortAllQData q)))

    "of"

    (/ (count qTables) 2)

    "solved question" q)

  (println "The average time for question" q "was"
           (int (Math/floor (/ (Math/round (float (/ (+ (* 60 (reduce + (map :min (sortAllQData q))))
                                                        (reduce + (map :sec (sortAllQData q))))
                                                     (count (sortAllQData q)))))
                               60)))

           "minutes and"

           (Math/round (float (mod 60 (/ (+ (* 60 (reduce + (map :min (sortAllQData q))))
                                    (reduce + (map :sec (sortAllQData q))))
                                 (count (sortAllQData q))))))

           "seconds")

  (println
    (count (filter (fn [x] (= :all (:right? x)))
                   (sortAllQData q)))
    "subjects spent all of the time approaching problem" q "correctly")

  (println
    (count (filter (fn [x] (= :most (:right? x)))
                   (sortAllQData q)))
    "subjects spent most of the time approaching problem" q "correctly")

  (println
    (count (filter (fn [x] (= :some (:right? x)))
                   (sortAllQData q)))
    "subjects spent some of the time approaching problem" q "correctly")
  (println
    (count (filter (fn [x] (= :never (:right? x)))
                   (sortAllQData q)))
    "subjects spent none of the time approaching problem" q "correctly")

  (println "The average number of runs for question" q "was"

           (int (Math/round (float (/ (reduce + (map :runs (sortAllQData q)))
                                      (count (sortAllQData q)))))))

  (println

    (count (filter (fn [x] (not (:no-error x)))
                   (sortAllQData q)))

    "of"

    (/ (count qTables) 2)

    "viewed the error message question" q)

  )




(+ (* 60 (reduce + (map :min (sortAllQData "0-1"))))
                                    (reduce + (map :sec (sortAllQData "0-1"))))


(compareAllQData "0-1")

(count (filter (fn [x] (= :all (:right? x)))
                   (sortAllQData "0-1")))
; person-report
; total-time
;(defn total-time [person]
