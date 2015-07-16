(ns intro.tracking
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]
            [corefns.collection_fns :refer :all]
            [quil.core :as q]
            [quil.middleware :as m]
            [quil.q_functions :refer :all]
            [errors.errorgui :refer :all]
            [errors.prettify_exception :refer :all]))


(def shape (scale-shape (above (create-rect 100 100 80 255 80)
                               (create-rect 100 100 80 255 80))
                        2 2))


(defn setup []
  (q/frame-rate 1)
  (q/color-mode :rgb)


  {:shape shape})

;-

(defn update-state [state]
  state)

;-

(defn draw-state [state]
  (f-background 25 25 25 25)
  (ds shape (/ (q/width) 2) (/ (q/height) 2)))

;-

(q/defsketch start
  :title "Tracking"
  :size [(- (min (q/screen-width) (q/screen-height)) 100) (- (min (q/screen-width) (q/screen-height)) 100)]
  :setup setup
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode])
