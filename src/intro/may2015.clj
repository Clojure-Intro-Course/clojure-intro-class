(ns intro.may2015
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]
            [corefns.collection_fns :refer :all]
            [quil.core :as q]
            [quil.middleware :as m]
            [quil.q_functions :refer :all]
            [errors.errorgui :refer :all]
            [errors.prettify_exception :refer :all]))

(defn setup []
  (q/frame-rate 60)
  (q/color-mode :rgb)

  (def tower (create-rect 15 50 100))
  (def tight-rope (create-line 20 0 3 0))

  (def b1 (create-rect 20 25 150))
  (def b2 (create-rect 30 35 100))
  (def b3 (create-rect 40 45 50))


  (def left-tower (above-align :right
                            b1
                            b2
                            b3))
   (def right-tower (above-align :left
                            b1
                            b2
                            b3))

  (def thing (beside-align :top
                           left-tower
                           tight-rope
                           right-tower))


  {:angle 0
   :count 0})

;-
(defn update-state [state]
  (assoc state
    :angle (- (:angle state) 0.04)))


;-
(defn draw-state [state]
  (f-background 255)
  (ds thing (/ (q/width) 2) (/ (q/height) 2))

  (if (= (q/frame-count) 1)
    (q/save-frame "two-tower.png")))










;-

(q/defsketch start
  :title "Rings"
  :size [110 110]
  :setup setup
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode])








