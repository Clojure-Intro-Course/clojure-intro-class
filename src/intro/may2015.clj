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





(defn create-ellipse [w h color]
  {:w w
   :h h
   :tw w
   :th h
   :dx 0
   :dy 0
   :ds (fn [x y]
         (f-fill color)
         (f-ellipse x y w h)
         (q/no-fill))})






(defn create-rect [w h color]
  {:w w
   :h h
   :tw w
   :th h
   :dx 0
   :dy 0
   :ds (fn [x y]
         (f-fill color)
         (f-rect x y w h)
         (q/no-fill))})





(defn ds [shape x y]
  (if
    (not (vector? shape))
    ((:ds shape) x y)
    (doseq [i (range (count shape))]
    ((:ds (nth shape i)) (+ x (:dx (nth shape i))) (+ y (:dy (nth shape i)))))))

(def black-circle (create-ellipse 40 40 25))

(def black-rect (create-rect 20 20 10))

(def grey-rect (create-rect 40 40 125))

(def white-circle (create-ellipse 20 20 225))



;---
(defn max-w-recur [args]
  (conj
   (if
     (not= (count args) 1)
     (max-w-recur (rest args)))
   (if
     (vector? (first args))
     (:tw (first (first args)))
     (:tw (first args)))))

(defn calc-max-w [args]
  (def max-w
    (apply max (max-w-recur args))))

;---
(defn tot-w-recur [args]
  (conj
   (if
     (not= (count args) 1)
     (tot-w-recur (rest args)))
   (if
     (vector? (first args))
     (:tw (first (first args)))
     (:tw (first args)))))

(defn calc-tot-w [args]
  (def tot-w
    (reduce + (tot-w-recur args))))

;---
(defn max-h-recur [args]
  (conj
   (if
     (not= (count args) 1)
     (max-h-recur (rest args)))
   (if
     (vector? (first args))
     (:th (first (first args)))
     (:th (first args)))))

(defn calc-max-h [args]
  (def max-h
    (apply max (max-h-recur args))))

;---
(defn tot-h-recur [args]
  (conj
   (if
     (not= (count args) 1)
     (tot-h-recur (rest args)))
   (if
     (vector? (first args))
     (:th (first (first args)))
     (:th (first args)))))

(defn calc-tot-h [args]
  (def tot-h
    (reduce + (tot-h-recur args))))

;---


(defn eval-compshape-vertical [args numb th]
  (conj
   (if
     (not= (count args) 1)
     (eval-compshape-vertical (rest args) numb th))
   (assoc (first args) :dy (+ (:dy (first args)) (- (+ (quot th 2) numb) (quot tot-h 2))) :tw max-w :th tot-h)))

(defn eval-shapes-vertical [args numb]
  (conj (if
          (not= (count args) 1)
          (if (vector? (first args))
            (eval-shapes-vertical (rest args) (+ (:th (first (first args))) numb))
            (eval-shapes-vertical (rest args) (+ (:th (first args)) numb))))

        (if
          (vector? (first args))
          (eval-compshape-vertical (first args) numb (:th (first (first args))))
          (assoc (first args) :dy (- (+ (quot (:th (first args)) 2) numb) (quot tot-h 2)) :tw max-w :th tot-h))))

;---
(defn eval-compshape-horizontal [args numb tw]
  (conj
   (if
     (not= (count args) 1)
     (eval-compshape-horizontal (rest args) numb tw))
   (assoc (first args) :dx (+ (:dx (first args)) (- (+ (quot tw 2) numb) (quot tot-w 2))) :tw tot-w :th max-h)))

(defn eval-shapes-horizontal [args numb]
  (conj (if
          (not= (count args) 1)
          (if (vector? (first args))
            (eval-shapes-horizontal (rest args) (+ (:tw (first (first args))) numb))
            (eval-shapes-horizontal (rest args) (+ (:tw (first args)) numb))))

        (if
          (vector? (first args))
          (eval-compshape-horizontal (first args) numb (:tw (first (first args))))
          (assoc (first args) :dx (- (+ (quot (:tw (first args)) 2) numb) (quot tot-w 2)) :tw tot-w :th max-h))))

;---
(defn eval-compshape-overlay [args]
  (conj
   (if
     (not= (count args) 1)
     (eval-compshape-overlay (rest args)))
   (assoc (first args) :tw max-w :th max-h)))

(defn eval-shapes-overlay [args]
  (conj (if
          (not= (count args) 1)
          (eval-shapes-overlay (rest args)))

        (if
          (vector? (first args))
          (eval-compshape-overlay (first args))
          (assoc (first args) :tw max-w :th max-h))))


(defn above [& args]
  (calc-tot-h args)
  (calc-max-w args)
  (vec (flatten (eval-shapes-vertical args 0))))

(defn beside [& args]
  (calc-tot-w args)
  (calc-max-h args)
  (vec (flatten (eval-shapes-horizontal args 0))))

(defn overlay [& args]
  (calc-max-w args)
  (calc-max-h args)
  (vec (flatten (eval-shapes-overlay (reverse args)))))


(def bg-tower
  (above black-circle
         black-rect
         grey-rect
         black-rect
         grey-rect))

(def big-tower
  (beside bg-tower
          bg-tower
          black-rect))

(def super-tower
  (beside big-tower
          big-tower))

(def over-tower
  (overlay black-rect
           black-circle
           bg-tower
           big-tower
           super-tower))

(def super-super-tower
  (above super-tower
         super-tower
         super-tower))

(def ssst
  (beside super-super-tower
          super-super-tower))

(def two-circles
  (overlay white-circle
           black-circle))




(defn setup []
  (q/frame-rate 1)
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
     (f-stroke 0)
     (f-fill 255)
     (f-text-size 20)
     ;(green-rect (* (+ (q/sin (:x state)) 1) 200) 100 100 100)
     ;(green-rect 100 (* (+ (q/sin (:y state)) 1) 200) 50 50)
     ;(ds black-rect 100 100)
     (f-fill 80 255 80)
     ;(q/arc 250 250 100 100 (- q/PI) 0)
     (q/rect-mode :center)
     (ds over-tower 400 400)
;     (ds bg-tower 340 300)
;     (ds bg-tower 260 300)
     (q/line 0 400 400 400)
     (q/line 400 0 400 400)



     (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))


(q/defsketch my
  :title "My sketch"
  :size [800 800]
  ; Setup function called only once, during sketch initialization.
  :setup setup
  ; Update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode])
