(ns intro.may2015
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]
            [corefns.collection_fns :refer :all]
            [quil.core :as q]
            [quil.middleware :as m]
            [quil.q_functions :refer :all]
            [errors.errorgui :refer :all]
            [errors.prettify_exception :refer :all]))



(defn return-color [args]
  (if (not= (count args) 1)
    (return-color (rest args)))
  (first args))

(defn create-ellipse [w h & args]
  {:w w
   :h h
   :tw w
   :th h
   :dx 0
   :dy 0
   :ds (fn [x y]
         (cond (= (count args) 1)
               (f-fill (first args))
               (= (count args) 2)
               (f-fill (first args) (second args))
               (= (count args) 3)
               (f-fill (first args) (second args) (second (rest args)))
               (= (count args) 4)
               (f-fill (first args) (second args) (second (rest args)) (second (rest (rest args)))))
         (f-ellipse x y w h)
         (q/no-fill))})


(defn create-picture [pic]
  {:w (.width (q/load-image pic))
   :h (.height (q/load-image pic))
   :tw (.width (q/load-image pic))
   :th (.height (q/load-image pic))
   :dx 0
   :dy 0
   :ds (fn [x y]
         (q/image (q/load-image pic) x y))})


(defn create-rect [w h & args]
  {:w w
   :h h
   :tw w
   :th h
   :dx 0
   :dy 0
   :ds (fn [x y]
         (cond (= (count args) 1)
               (f-fill (first args))
               (= (count args) 2)
               (f-fill (first args) (second args))
               (= (count args) 3)
               (f-fill (first args) (second args) (second (rest args)))
               (= (count args) 4)
               (f-fill (first args) (second args) (second (rest args)) (second (rest (rest args)))))
         (f-rect x y w h)
         (q/no-fill))})

;---
(defn ds [shape x y]
  (if
    (not (vector? shape))
    ((:ds shape) x y)
    (doseq [i (range (count shape))]
      ((:ds (nth shape i)) (+ x (:dx (nth shape i))) (+ y (:dy (nth shape i)))))))


;------
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

;------
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


;------                  ***  OVERLAY-ALIGN  ***

(defn overlay-compshape-vertical [vert arg]
  (cond (= vert :top)
        (if
          (= (:th arg) max-h)
          (:dy arg)
          (+ (- (:dy arg) (quot max-h 2)) (quot (:th arg) 2)))
        (= vert :center)
        (:dy arg)
        (= vert :bottom)
        (if
          (= (:th arg) max-h)
          (:dy arg)
          (+ (- (:dy arg) (quot max-h 2)) (quot (:th arg) 2)))
        :else
        (throw (Exception. "The function overlay-align takes :top, :center, or :bottom as its first argument."))))

(defn overlay-compshape-horizontal [hor arg]
  (cond (= hor :left)
        (if
          (= (:tw arg) max-w)
          (:dx arg)
          (+ (- (:dx arg) (quot max-w 2)) (quot (:tw arg) 2)))
        (= hor :center)
        (:dx arg)
        (= hor :right)
        (if
          (= (:tw arg) max-w)
          (:dx arg)
          (- (+ (:dx arg) (quot max-w 2)) (quot (:tw arg) 2)))
        :else
        (throw (Exception. "The function overlay-align takes :left, :center, or :right as its second argument."))))

(defn eval-compshape-overlay-align [vert hor args]
  (conj
   (if
     (not= (count args) 1)
     (eval-compshape-overlay-align vert hor (rest args)))
   (assoc (first args) :tw max-w :th max-h :dy (overlay-compshape-vertical vert (first args)) :dx (overlay-compshape-horizontal hor (first args)))))

(defn overlay-vertical [vert arg]
  (cond (= vert :top)
        (- (quot (:th arg) 2) (quot max-h 2))
        (= vert :center)
        (:dy arg)
        (= vert :bottom)
        (- (quot max-h 2) (quot (:th arg) 2))
        :else
        (throw (Exception. "The function overlay-align takes :top, :center, or :bottom as its first argument."))))

(defn overlay-horizontal [hor arg]
  (cond (= hor :left)
        (- (quot (:tw arg) 2) (quot max-w 2))
        (= hor :center)
        (:dx arg)
        (= hor :right)
        (- (quot max-w 2) (quot (:tw arg) 2))
        :else
        (throw (Exception. "The function overlay-align takes :left, :center, or :right as its second argument."))))

(defn eval-shapes-overlay-align [vert hor args]
  (conj (if
          (not= (count args) 1)
          (eval-shapes-overlay-align vert hor (rest args)))

        (if
          (vector? (first args))
          (eval-compshape-overlay-align vert hor (first args))
          (assoc (first args) :tw max-w :th max-h :dy (overlay-vertical vert (first args)) :dx (overlay-horizontal hor (first args))))))

(defn overlay-align [vert hor & args]
  (calc-max-w args)
  (calc-tot-w args)
  (calc-max-h args)
  (calc-tot-h args)
  (vec (flatten (eval-shapes-overlay-align vert hor (reverse args)))))


;------                  ***  ABOVE-ALIGN  ***

(defn eval-compshape-above-align-right [args numb th]
  (conj
   (if
     (not= (count args) 1)
     (eval-compshape-above-align-right (rest args) numb th))
   (assoc (first args) :dy (+ (:dy (first args)) (- (+ (quot th 2) numb) (quot tot-h 2))) :tw max-w :th tot-h :dx (if
                                                                                                                    (= (:tw (first args)) max-w)
                                                                                                                    (:dx (first args))
                                                                                                                    (- (+ (:dx (first args)) (quot max-w 2)) (quot (:tw (first args)) 2))))))

(defn eval-shapes-above-align-right [args numb]
  (conj (if
          (not= (count args) 1)
          (if (vector? (first args))
            (eval-shapes-above-align-right (rest args) (+ (:th (first (first args))) numb))
            (eval-shapes-above-align-right (rest args) (+ (:th (first args)) numb))))

        (if
          (vector? (first args))
          (eval-compshape-above-align-right (first args) numb (:th (first (first args))))
          (assoc (first args) :dy (- (+ (quot (:th (first args)) 2) numb) (quot tot-h 2)) :dx (- (quot max-w 2) (quot (:tw (first args)) 2)) :tw max-w :th tot-h))))

(defn eval-compshape-above-align-left [args numb th]
  (conj
   (if
     (not= (count args) 1)
     (eval-compshape-above-align-left (rest args) numb th))
   (assoc (first args) :dy (+ (:dy (first args)) (- (+ (quot th 2) numb) (quot tot-h 2))) :tw max-w :th tot-h :dx (if
                                                                                                                    (= (:tw (first args)) max-w)
                                                                                                                    (:dx (first args))
                                                                                                                    (+ (- (:dx (first args)) (quot max-w 2)) (quot (:tw (first args)) 2))))))

(defn eval-shapes-above-align-left [args numb]
  (conj (if
          (not= (count args) 1)
          (if (vector? (first args))
            (eval-shapes-above-align-left (rest args) (+ (:th (first (first args))) numb))
            (eval-shapes-above-align-left (rest args) (+ (:th (first args)) numb))))

        (if
          (vector? (first args))
          (eval-compshape-above-align-left (first args) numb (:th (first (first args))))
          (assoc (first args) :dy (- (+ (quot (:th (first args)) 2) numb) (quot tot-h 2)) :dx (- (quot (:tw (first args)) 2) (quot max-w 2)) :tw max-w :th tot-h))))


(defn above-align [align & args]
  (calc-tot-h args)
  (calc-max-w args)
  (cond (= align :right)
        (vec (flatten (eval-shapes-above-align-right args 0)))
        (= align :left)
        (vec (flatten (eval-shapes-above-align-left args 0)))
        :else
        (throw (Exception. "The function above-align takes in :right or :left as its first argument."))))


;------                       *** BESIDE-ALIGN ***
(defn eval-compshape-beside-align-top [args numb tw]
  (conj
   (if
     (not= (count args) 1)
     (eval-compshape-beside-align-top (rest args) numb tw))
   (assoc (first args) :dx (+ (:dx (first args)) (- (+ (quot tw 2) numb) (quot tot-w 2))) :tw tot-w :th max-h :dy (if
                                                                                                                    (= (:th (first args)) max-h)
                                                                                                                    (:dy (first args))
                                                                                                                    (+ (- (:dy (first args)) (quot max-h 2)) (quot (:th (first args)) 2))))))


(defn eval-shapes-beside-align-top [args numb]
  (conj (if
          (not= (count args) 1)
          (if (vector? (first args))
            (eval-shapes-beside-align-top (rest args) (+ (:tw (first (first args))) numb))
            (eval-shapes-beside-align-top (rest args) (+ (:tw (first args)) numb))))

        (if
          (vector? (first args))
          (eval-compshape-beside-align-top (first args) numb (:tw (first (first args))))
          (assoc (first args) :dx (- (+ (quot (:tw (first args)) 2) numb) (quot tot-w 2)) :tw tot-w :th max-h :dy (- (quot (:th (first args)) 2) (quot max-h 2))))))

(defn eval-compshape-beside-align-bottom [args numb tw]
  (conj
   (if
     (not= (count args) 1)
     (eval-compshape-beside-align-bottom (rest args) numb tw))
   (assoc (first args) :dx (+ (:dx (first args)) (- (+ (quot tw 2) numb) (quot tot-w 2))) :tw tot-w :th max-h :dy (if
                                                                                                                    (= (:th (first args)) max-h)
                                                                                                                    (:dy (first args))
                                                                                                                    (- (+ (:dy (first args)) (quot max-h 2)) (quot (:th (first args)) 2))))))

(defn eval-shapes-beside-align-bottom [args numb]
  (conj (if
          (not= (count args) 1)
          (if (vector? (first args))
            (eval-shapes-beside-align-bottom (rest args) (+ (:tw (first (first args))) numb))
            (eval-shapes-beside-align-bottom (rest args) (+ (:tw (first args)) numb))))

        (if
          (vector? (first args))
          (eval-compshape-beside-align-bottom (first args) numb (:tw (first (first args))))
          (assoc (first args) :dx (- (+ (quot (:tw (first args)) 2) numb) (quot tot-w 2)) :tw tot-w :th max-h :dy (- (quot max-h 2) (quot (:th (first args)) 2))))))


(defn beside-align [align & args]
  (calc-tot-w args)
  (calc-max-h args)
  (cond (= align :top)
        (vec (flatten (eval-shapes-beside-align-top args 0)))
        (= align :bottom)
        (vec (flatten (eval-shapes-beside-align-bottom args 0)))
        :else
        (throw (Exception. "The function beside-align takes in :top or :bottom as its first argument."))))

;-----------------------------------------------------------
;                   SKETCH FUNCTIONS

(defn setup []
  (try

    (def black-circle (create-ellipse 20 20 80 255 255))

    (def black-rect (create-rect 20 20 10))

    (def grey-rect (create-rect 40 40 125))

    (def white-circle (create-ellipse 20 20 225))

    (def big-rect (create-rect 100 100 255 80 255))

    (def bigger-rect (create-rect 150 150 255 80 80))


    (def sqr1 (create-rect 20 20 255 80 80))
    (def sqr2 (create-rect 40 40 80 255 80))
    (def sqr3 (create-rect 60 60 80 80 255))
    (def sqr4 (create-rect 80 80 255 255 80))
    (def sqr5 (create-rect 100 100 255 80 255))
    (def sqr6 (create-rect 120 120 80 255 255))
    (def sqr7 (create-rect 140 140 80 80 80))
    (def sqr8 (create-rect 160 160 255 255 255))
    (def sqr9 (create-rect 180 180 255 0 0))
    (def sqr10 (create-rect 200 200 0 80 80))

    (def thing (overlay-align :top :right sqr1 sqr8))



    (def light-table (create-picture "/home/mcart046/Pictures/lighttable.png"))
    (def kappa (create-picture "/home/hagen715/Desktop/images/kappa96x130.png"))
    (def frankerz (create-picture "/home/hagen715/Desktop/images/frankerz220x200.jpg"))

    (def thing5 (above grey-rect
                      black-rect
                      grey-rect))

    (def thing1 (overlay-align :center :center black-rect grey-rect big-rect bigger-rect))

    (def thing2 (overlay-align :bottom :right kappa thing1))

    {:color 0
     :angle 0
     :x 400
     :y 400
     :shape-1 black-rect}

    (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))

;-

(defn update-state [state]
  (try
    (assert (not-nil? state) "Your state is nil")
    (assoc state :x (+ (:x state) 0.05) :y (+ (:y state) 0.05))
    (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))

;-

(defn draw-state [state]
  (try
    ;(ds ssst (+ 400 (* 200 (q/cos (:x state)))) (+ 200 (* 200 (+ 1 (q/sin (:y state))))))
    (f-background 80 255 80)

    (f-stroke 255)
    (f-fill 255)
    (f-text-size 20)
    (q/rect-mode :center)
    (q/image-mode :center)

    (ds thing  400 400)



    (f-fill 80 255 80)
    (q/text-num (q/frame-count) 400 100)
    ;(q/line 0 400 400 400)
    ;(q/line 400 0 400 400)


    (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))

;-

(q/defsketch my
  :title "My sketch"
  :size [800 800]
  ; Setup function called only once, during sketch initialization.
  :setup setup
  ; Update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode])
