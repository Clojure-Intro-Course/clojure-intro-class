(ns intro.may2015
(:require [expectations :refer :all]
          [corefns.corefns :refer :all]
          [corefns.collection_fns :refer :all]
          [quil.core :as q]
          [quil.middleware :as m]
          [quil.q_functions :refer :all]
          [errors.errorgui :refer :all]
          [errors.prettify_exception :refer :all]))


;-------------------- Do not remove things above this line --------------------

;A few things to note

;Quil api for functions: http://quil.info/api

; !!! Add 'f-' to the front of these functions:
;background      http://quil.info/api/color/setting#background
;ellipse         http://quil.info/api/shape/2d-primitives#ellipse
;fill            http://quil.info/api/color/setting#fill
;line            http://quil.info/api/shape/2d-primitives#line
;quad            http://quil.info/api/shape/2d-primitives#quad
;rect            http://quil.info/api/shape/2d-primitives#rect
;stroke          http://quil.info/api/color/setting#stroke
;stroke-weight   http://quil.info/api/shape/attributes#stroke-weight
;text            http://quil.info/api/typography/loading-and-displaying#text
;text-num        http://quil.info/api/typography/loading-and-displaying#text-num
;text-size       http://quil.info/api/typography/attributes#text-size
;triangle        http://quil.info/api/shape/2d-primitives#triangle

;!!! Add a q/ in front of any other functions from the Quil API

;Ex.
;(f-background 80 255 80)
;(q/sqrt 64)
;

;This file currently contains code to draw something already!
;You can use this code as a reference for setting up your project

(defn setup []
  "Sets up the inital state of the game.
   From setup, you can set your framerate and add or remove variables you use."

  (q/frame-rate 30)
  (q/color-mode :rgb)


  ;Put variables and their starting values for your game in the hash-map after this comment.
  {:color 0
   :angle 0})


;Use smaller functions to update parts of the state
;----------------------
    ;Both of these helper functions take the state as an argument and both return a number
(defn update-color [state]
  (mod (+ (:color state) 0.07) q/TWO-PI))

(defn update-angle [state]
  (mod (+ (:angle state) 0.1) q/TWO-PI))
;-----------------------


(defn update-state [state]
  "Takes in the current state and returns the updated state.
   Put functions that change your world state here"
  ;-------------------- Do not remove this line --------------------
  (try
    (assert (not-nil? state) "Your state is nil")
  ;-------------------- Do not remove this line --------------------


    ;Put your code for update-state here!

    ;Here we are saying what the elements of the state should update to
    ;Make sure to include all of the elements of your state
    {:color (update-color state)
     :angle (update-angle state)}




    ;-------------------- Do not remove this line --------------------
    (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))
    ;-------------------- Do not remove this line --------------------





(defn draw-state [state]
  "Draws current state"
  ;-------------------- Do not remove this line --------------------
   (try
  ;-------------------- Do not remove this line --------------------


     ;Put your code for draw-state here!
     (f-background 240)
     (let [angle (:angle state)
           x (* 150 (q/cos angle))
           y (* 150 (q/sin angle))
           r (* 127 (+ 1 (q/sin (:color state))))
           b (* 127 (+ 1 (q/sin (+ 0.2 (:color state)))))
           g (* 127 (+ 1 (q/sin (:color state))))]
       (f-fill r g b)
       (q/with-translation [(/ (q/width) 2)
                            (/ (q/height) 2)]

         (f-ellipse x y 100 100)))


  ;-------------------- Do not remove this line --------------------
     (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))
  ;-------------------- Do not remove this line --------------------





(q/defsketch my
  :title "My sketch"
  :size [500 500]
  ; Setup function called only once, during sketch initialization.
  :setup setup
  ; Update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode])
