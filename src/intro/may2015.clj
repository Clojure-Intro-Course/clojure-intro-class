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



(def black-rect (create-rect 20 20 10))

(def grey-rect (create-rect 40 40 125))

(defn calc-max-h [args]
  (def max-h
    (reduce max (map #(get % :h) (vec args)))))

(defn calc-tot-h [args]
  (def tot-h
    (reduce + (map #(get % :h) (vec args)))))

(defn calc-max-w [args]
  (def max-w
    (reduce max (map #(get % :w) (vec args)))))

(defn calc-tot-w [args]
  (def tot-w
    (reduce + (map #(get % :w) (vec args)))))

(defn eval-compshape-vertical [args  numb]

(defn eval-shapes-vertical [args numb]
  (conj (if
          (not= (count args) 1)
          (if (vector? (first args))
            (eval-shapes-vertical (rest args) (+ (:th (first (first args))) numb))
            (eval-shapes-vertical (rest args) (+ (:h (first args)) numb))))

        (if
          (vector? (first args))
          (eval-compshape-vertical (first args) "add arguments here" numb)
          (assoc (first args) :dy (- (+ (quot (:h (first args)) 2) numb) (quot tot-h 2)) :tw max-w :th tot-h))))






 ;(conj (if
 ;         (not= (count args) 1)
 ;         (eval-shapes-vertical (rest args) (+ (:h (first args)) numb)))
 ;       (assoc (first args) :dy (- (+ (quot (:h (first args)) 2) numb) (quot tot-h 2)) :tw max-w :th tot-h)))


(defn eval-shapes-horizontal [args numb]
  (conj (if
          (not= (count args) 1)
          (eval-shapes-horizontal (rest args) (+ (:w (first args)) numb)))
        (assoc (first args) :dx (- (+ (quot (:w (first args)) 2) numb) (quot tot-w 2)) :tw tot-w :th max-h)))


(defn above [& args]
  (calc-tot-h (flatten args))
  (calc-max-w (flatten args))
  (conj
   (vec (eval-shapes-vertical args 0))))

(defn beside [& args]
  (calc-tot-w (flatten args))
  (calc-max-h (flatten args))
  (conj
   (vec (eval-shapes-horizontal (flatten args) 0))))




(def bg-tower
  (above black-rect
         grey-rect
         black-rect
         grey-rect))

(def big-tower
  (above bg-tower
         bg-tower))




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
     (ds big-tower 300 300)
;     (ds bg-tower 340 300)
;     (ds bg-tower 260 300)
     (q/line 0 300 300 300)
     (q/line 300 0 300 300)



     (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))


(q/defsketch my
  :title "My sketch"
  :size [600 600]
  ; Setup function called only once, during sketch initialization.
  :setup setup
  ; Update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode])
