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

  {})

;-
(defn update-state [state])


;-
(defn draw-state [state]
  (draw-shape
   (create-rect 200 200 80 255 80)
   400 400))









;-

(q/defsketch start
  :title ""
  :size [(- (min (q/screen-width) (q/screen-height)) 100) (- (min (q/screen-width) (q/screen-height)) 100)]
  :setup setup
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode])








