(ns intro.may2015
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]
            [corefns.collection_fns :refer :all]
            [quil.core :as q]
            [quil.middleware :as m]
            [quil.q_functions :refer :all]
            [errors.errorgui :refer :all]
            [errors.prettify_exception :refer :all])
  (use [overtone.core]
      ; [overtone.inst.piano]
       ))

(boot-external-server)

(definst bar [freq 220] (saw freq))


(defn p-bar [freq x dur pitch]
  (at (+ (* x dur) (now)) (bar (+ freq (* (/ 110 12) pitch))))
  (at (+ (* (+ 0.95 x) dur) (now)) (kill bar)))


;; (defn play-octave [start-freq dur]
;;   (loop [i 0]
;;     (p-bar start-freq i dur)
;;     (if (< i 13)
;;      (recur (inc i)))))

(defn play-octave [start-freq dur]
  (doseq [i (range 13)]
    (p-bar start-freq i dur i)))


(play-octave 110 500)
