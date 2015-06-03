(ns intro.may2015
(:require [expectations :refer :all]
          [corefns.corefns :refer :all]
          [corefns.collection_fns :refer :all]
          [quil.core :as q]
          [quil.middleware :as m]
          [quil.quil :refer :all :as qq]
          [errors.errorgui :refer :all]
          [errors.prettify_exception :refer :all]))


(defn setup []
  "Sets up the inital state of the game.
   From setup, you can set your framerate and add or remove variables you use."


  (q/frame-rate 1)
  (q/color-mode :rgb)
   {:light 0
    :p1-pos [0 450]
    :p2-pos [900 450]
    :m-nill 100
    })

(defn not-nil? [x]
  (not (nil? x)))

(defn make-nil [state]
  (if (= (rand-int 2) 0)
    "A"
    100))


(defn update-state [state]
  (try
    ;(assert
    ;  state
    ;"Not nil is failing")


    {:light 0
     :p1-pos [0 450]
     :p2-pos [900 450]
     :m-nill (make-nil state)}


    (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))





(defn draw-state [state]
  (println "Start of frame" (q/frame-count) "with :m-nill being" (:m-nill state))
  (try
    (let [x1 (first (:p1-pos state))
          y1 (second (:p1-pos state))
          x2 (first (:p2-pos state))
          y2 (second (:p2-pos state))]
      (q/background (* 127 (+ 1 (q/sin (:light state)))))
      (q/fill 80 255 80)
      (q/text-size 72)
      (q/text-align :center)
      (q/text-num (q/frame-count) 500 850)


      (println (q/fill 0))
      (println )
      (q/fill 80 255 80)
      (assert (not-nil? x1) "One of your inputs is nil")
      (assert (not-nil? y1) "One of your inputs is nil")
      (assert (not-nil? (:m-nill state)) "One of your inputs is nil")
      (assert (not-nil? 100) "One of your inputs is nil")
      (q/rect x1 y1 (:m-nill state) 100)
      (q/fill 80 255 255)
      (q/rect x2 y2 100 100)
      (q/fill 80 255 80)
      (q/rect 450 0 100 100))

    (catch NullPointerException e (println "\n NullPointerException is:" e "\n"))
    (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e))))
  )


(q/defsketch gol
  :title "Game"
  :size [1000 1000]
  ; Setup function called only once, during sketch initialization.
  :setup setup
  ; Update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  ;:key-pressed move
  :middleware [m/fun-mode])


