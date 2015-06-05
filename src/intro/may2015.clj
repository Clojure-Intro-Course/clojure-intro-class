(ns intro.may2015
(:require [expectations :refer :all]
          [corefns.corefns :refer :all]
          [corefns.collection_fns :refer :all]
          [quil.core :as q]
          [quil.middleware :as m]
          [quil.q_functions :refer :all]
          [errors.errorgui :refer :all]
          [errors.prettify_exception :refer :all]))

;(def best-image (q/blend (f-quad 50 0 100 50 50 100 0 50) (f-rect 0 0 100 100) 0 0 100 100 0 0 100 100 :add))




(defn create-rect [w h color]
  {:w w
   :h h
   :dx 0
   :dy 0
   :ds (fn [x y]
    (f-fill color)
    (f-rect x y w h)
    (q/no-fill))})



(defn ds [shape x y]
  ((:ds shape) x y))

(def black-rect (create-rect 100 100 10))

(def grey-rect (create-rect 200 200 125))

(defn above [& args]
  (cons (first args) (vec (for [i (range 1 (count args))]
    (assoc (get (vec args) i) :dy (quot (+ (:h (get (vec args) i)) (:h (get (vec args) (- i 1)))) 2))))))

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :rgb)


  {:color 0
   :angle 0
   :x 1
   :y 1
   :picture-1 (q/load-image "/home/hagen715/Desktop/images/404.png")})


(defn update-state [state]
  (try
    (assert (not-nil? state) "Your state is nil")

    (assoc state :x (+ (:x state) 0.1) :y (+ (:y state) 0.1))



    (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))



(defn draw-state [state]
   (try
     (f-background 255)
     ;(q/image (:picture-1 state) 0 0)
     ;(f-fill 80 255 80)
     ;(f-rect 100 100 100 100)
     (f-stroke 255)
     (f-fill 255)
     (f-text-size 20)
     ;(green-rect (* (+ (q/sin (:x state)) 1) 200) 100 100 100)
     ;(green-rect 100 (* (+ (q/sin (:y state)) 1) 200) 50 50)

     (ds black-rect 100 100)
     (f-fill 80 255 80)
     (q/arc 250 250 100 100 (- q/PI) 0)
     (println (above black-rect
                     grey-rect))


     (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))


(q/defsketch my
  :title "My sketch"
  :size [500 500]
  ; Setup function called only once, during sketch initialization.
  :setup setup
  ; Update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode])
