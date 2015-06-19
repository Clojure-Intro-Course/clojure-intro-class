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
   :angle 0
   :ds (fn [x y pict wid hei cs angle]
         (cond (= (count args) 1)
               (f-fill (first args))
               (= (count args) 2)
               (f-fill (first args) (second args))
               (= (count args) 3)
               (f-fill (first args) (second args) (second (rest args)))
               (= (count args) 4)
               (f-fill (first args) (second args) (second (rest args)) (second (rest (rest args)))))
         (q/with-translation [x y]
         (q/with-rotation [(/ (* q/PI angle) 180)]
         (f-ellipse 0 0 wid hei)))
         (q/no-fill))})

(defn create-arc [w h start stop & args]
  {:w w
   :h h
   :tw w
   :th h
   :dx 0
   :dy 0
   :angle 0
   :ds (fn [x y pict wid hei cs angle]
         (cond (= (count args) 1)
               (f-fill (first args))
               (= (count args) 2)
               (f-fill (first args) (second args))
               (= (count args) 3)
               (f-fill (first args) (second args) (second (rest args)))
               (= (count args) 4)
               (f-fill (first args) (second args) (second (rest args)) (second (rest (rest args)))))
         (q/with-translation [x y]
         (q/with-rotation [(/ (* q/PI angle) 180)]
         (q/arc 0 0 wid hei start stop :pie)))
         (q/no-fill))})

(defn create-line [x2 y2 & args]
  {:w x2
   :h y2
   :tw x2
   :th y2
   :dx 0
   :dy 0
   :angle 0
   :ds (fn [x y pict wid hei cs angle]
         (cond (= (count args) 1)
               (f-stroke (first args))
               (= (count args) 2)
               (f-stroke (first args) (second args))
               (= (count args) 3)
               (f-stroke (first args) (second args) (second (rest args)))
               (= (count args) 4)
               (f-stroke (first args) (second args) (second (rest args)) (second (rest (rest args))))
               :else (f-stroke 0))
         (q/with-translation [x y]
         (q/with-rotation [(/ (* q/PI angle) 180)]
         (f-line (- 0 (quot wid 2)) (- 0 (quot hei 2)) (+ 0 (quot wid 2)) (+ 0 (quot hei 2)))))
         (f-stroke cs))})


(defn create-triangle [x2 y2 x3 y3 & args]
  {:w (+ (q/abs(max 0 x2 x3))  (q/abs(min 0 x2 x3)))
   :h (+ (q/abs(max 0 y2 y3))  (q/abs(min 0 y2 y3)))
   :tw (+ (q/abs(max 0 x2 x3))  (q/abs(min 0 x2 x3)))
   :th (+ (q/abs(max 0 y2 y3))  (q/abs(min 0 y2 y3)))
   :dx 0
   :dy 0
   :angle 0
   :ds (fn [x y pict wid hei cs angle]
         (cond (= (count args) 1)
               (f-fill (first args))
               (= (count args) 2)
               (f-fill (first args) (second args))
               (= (count args) 3)
               (f-fill (first args) (second args) (second (rest args)))
               (= (count args) 4)
               (f-fill (first args) (second args) (second (rest args)) (second (rest (rest args)))))
         (q/with-translation [x y]
         (q/with-rotation [(/ (* q/PI angle) 180)]
         (let [mid-x (quot (+ (max 0 x2 x3)  (min 0 x2 x3)) 2)
               mid-y (quot (+ (max 0 y2 y3)  (min 0 y2 y3)) 2)]
         (f-triangle (+ 0 (- 0 mid-x)) (+ 0 (- 0 mid-y))
                     (+ 0 (- x2 mid-x)) (+ 0 (- y2 mid-y))
                     (+ 0 (- x3 mid-x)) (+ 0 (- y3 mid-y))))))
         (q/no-fill))})

(defn create-quad [x2 y2 x3 y3 x4 y4 & args]
  {:w (+ (q/abs(max 0 x2 x3 x4))  (q/abs(min 0 x2 x3 x4)))
   :h (+ (q/abs(max 0 y2 y3 y4))  (q/abs(min 0 y2 y3 y4)))
   :tw (+ (q/abs(max 0 x2 x3 x4))  (q/abs(min 0 x2 x3 x4)))
   :th (+ (q/abs(max 0 y2 y3 y4))  (q/abs(min 0 y2 y3 y4)))
   :dx 0
   :dy 0
   :angle 0
   :ds (fn [x y pict wid hei cs angle]
         (cond (= (count args) 1)
               (f-fill (first args))
               (= (count args) 2)
               (f-fill (first args) (second args))
               (= (count args) 3)
               (f-fill (first args) (second args) (second (rest args)))
               (= (count args) 4)
               (f-fill (first args) (second args) (second (rest args)) (second (rest (rest args)))))
         (q/with-translation [x y]
         (q/with-rotation [(/ (* q/PI angle) 180)]
         (let [mid-x (quot (+ (max 0 x2 x3 x4)  (min 0 x2 x3 x4)) 2)
               mid-y (quot (+ (max 0 y2 y3 y4)  (min 0 y2 y3 y4)) 2)]
         (f-quad (+ 0 (- 0 mid-x)) (+ 0 (- 0 mid-y))
                 (+ 0 (- x2 mid-x)) (+ 0 (- y2 mid-y))
                 (+ 0 (- x3 mid-x)) (+ 0 (- y3 mid-y))
                 (+ 0 (- x4 mid-x)) (+ 0 (- y4 mid-y))))))
         (q/no-fill))})


(defn create-picture [pic]
  {:w (.width (q/load-image pic))
   :h (.height (q/load-image pic))
   :tw (.width (q/load-image pic))
   :th (.height (q/load-image pic))
   :dx 0
   :dy 0
   :pic pic
   :angle 0
   :rp (q/load-image pic)
   :ds (fn [x y pict wid hei cs angle]
         (q/with-translation [x y]
         (q/with-rotation [(/ (* q/PI angle) 180)] (q/image pict 0 0))))})


(defn create-rect [w h & args]
  {:w w
   :h h
   :tw w
   :th h
   :dx 0
   :dy 0
   :angle 0
   :ds (fn [x y pict wid hei cs angle]
         (cond (= (count args) 1)
               (f-fill (first args))
               (= (count args) 2)
               (f-fill (first args) (second args))
               (= (count args) 3)
               (f-fill (first args) (second args) (second (rest args)))
               (= (count args) 4)
               (f-fill (first args) (second args) (second (rest args)) (second (rest (rest args)))))
         (q/with-translation [x y]
         (q/with-rotation [(/ (* q/PI angle) 180)] (f-rect 0 0 wid hei)))
         (q/no-fill))})

;---
(defn ds [shape x y]
  (q/rect-mode :center)
  (q/image-mode :center)
  (if
    (not (vector? shape))
    ((:ds shape) x y (:rp shape) (:w shape) (:h shape) (q/current-stroke) (:angle shape))
    (doseq [i (range (count shape))]
      ((:ds (nth shape i))
       (+ x (:dx (nth shape i)))
       (+ y (:dy (nth shape i)))
       (:rp (nth shape i))
       (:w (nth shape i))
       (:h (nth shape i))
       (q/current-stroke)
       (:angle (nth shape i))))))


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
          (- (+ (:dy arg) (quot max-h 2)) (quot (:th arg) 2)))
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

;------

(defn create-new-image-shape [shape scale-x scale-y]
  (def new-pic (create-picture (:pic shape)))
  (q/resize (:rp new-pic) (* (:w shape) scale-x) (* (:h shape) scale-y))
  new-pic)

(defn scale-image [shape scale-x scale-y]
  (assoc
    (if (not= (:rp shape) nil)
      (create-new-image-shape shape scale-x scale-y)
      shape)
    :w (* (:w shape) scale-x)
    :h (* (:h shape) scale-y)
    :tw (* (:tw shape) scale-x)
    :th (* (:th shape) scale-y)
    :dx (* (:dx shape) scale-x)
    :dy (* (:dy shape) scale-y)
    :angle (:angle shape)))

(defn scale-complex [shape scale-x scale-y]
  (conj (if
          (not= (count shape) 1)
          (scale-complex (rest shape) scale-x scale-y))
        (scale-image (first shape) scale-x scale-y)))


(defn scale-shape [shape scale-x scale-y]
  (if (vector? shape)
    (vec (flatten (scale-complex shape scale-x scale-y)))
    (scale-image shape scale-x scale-y)))

;------
(defn rotate-compshape [shape angle]
  (conj (if
          (not= (count shape) 1)
          (rotate-compshape (rest shape) angle))

        ;(println "you wanted" (:dy (first shape)) "m'lord")
        ;(println)
        ;(println "dx is being changed to" (* (q/cos (/ (* angle q/PI) 180)) (q/sqrt (+ (q/sq (:dx (first shape))) (q/sq (:dy (first shape)))))))
        ;(println "dy is being changed to" (* (q/sin (/ (* angle q/PI) 180)) (q/sqrt (+ (q/sq (:dx (first shape))) (q/sq (:dy (first shape)))))))
        ;(println)

        (let [new-dx (* (q/cos (/ (* angle q/PI) 180)) (q/sqrt (+ (q/sq (:dx (first shape))) (q/sq (:dy (first shape))))))
              new-dy (* (q/sin (/ (* angle q/PI) 180)) (q/sqrt (+ (q/sq (:dx (first shape))) (q/sq (:dy (first shape))))))]
        (assoc (first shape)
          :angle angle
          :dx (if (< (:dx (first shape)) 0) (* new-dx -1))
          :dy (if (< (:dy (first shape)) 0) (* new-dy -1))))))


(defn rotate-shape [shape angle]
  ;(if
   ; (vector? shape)
   ; (vec (flatten (rotate-compshape (reverse shape) angle)))
    (assoc shape :angle angle))



;-----------------------------------------------------------
;                   SKETCH FUNCTIONS

(defn setup []
  (try
    (q/frame-rate 5)

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
    (def sweet-line (create-line 0 200 0))
    (def long-box (create-rect 100 40 80 255 255))
    (def tall-box (create-rect 40 100 80 80 255))





    (def light-table (create-picture "/home/mcart046/Pictures/lighttable.png"))
    (def kappa (create-picture "/home/hagen715/Desktop/images/kappa96x130.png"))
    ; (def big-kappa (scale-shape (create-picture "/home/hagen715/Desktop/images/kappa96x130.png") 2 2))
    (def bigger-kappa (create-picture "/home/hagen715/Desktop/images/kappa96x130.png"))
    (def biggest-kappa (create-picture "/home/hagen715/Desktop/images/kappa96x130.png"))
    (def frankerz (create-picture "/home/hagen715/Desktop/images/frankerz220x200.jpg"))
    (def flying-thing (beside-align :bottom
                                    sweet-line
                                    (scale-shape (beside
                                                  sqr3
                                                  sqr4
                                                  sweet-line
                                                  (scale-shape kappa 0.5 0.5)
                                                  (overlay-align :top :left
                                                                 sweet-line
                                                                 sqr1
                                                                 sqr2
                                                                 sqr3
                                                                 sqr4
                                                                 sqr5)) 2 2)
                                    (overlay-align :bottom :right
                                                   sqr1
                                                   sqr2
                                                   sqr3
                                                   sqr4
                                                   sqr5)
                                    kappa))

    (def thing (overlay-align :top :right sweet-line sqr1 sqr2 sqr3 sqr4 sqr5 (overlay-align :bottom :left kappa sqr1 sqr2 sqr3 sqr4 sqr5 sqr6 sqr7 sqr8 sqr9)))
    (def tri (create-triangle 0 300 300 0 80 255 255))
    ;(def not-rotated-kappa (rotate-shape thing 90))
    (def kappa2 (beside-align :top (above (create-rect 96 170 80 255 80) kappa) (above (create-rect 96 70 80 255 80) kappa)))
    (def blokz (above tall-box
                      long-box))
    (def arc (create-arc 200 150 q/PI 6 255 80 255))
    (def arc-above (overlay-align :bottom :center arc sqr6))
    (def quadz (create-quad 100 0 100 100 0 100 80 255 255))


    {:shape flying-thing
     :scale-x 1
     :scale-y 1
     :angle 0
     :angle2 0
     :number 0}

    (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))

;-

(defn update-state [state]
  (try
    (assert (not-nil? state) "Your state is nil")


    ;(assoc state :shape (:shape state) :scale-x (+ (:scale-x state) 1) :scale-y (+ (:scale-y state) 1))
    (assoc state :angle (mod (+ (:angle state) 15) 360) :angle2 (+ (:angle2 state) 0.3) :number (if (= (:number state) 255)
                                                                                                       0
                                                                                                       255))

    (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))

;-

(defn draw-state [state]
  (try
    (f-background 80 255 80)

    (f-stroke 255 80 255)
    ;(q/stroke-weight 100)
    (f-text-size 20)
    (q/text-align :center)


    ;(q/with-translation [500 500] (q/with-rotation [q/PI] (ds flying-thing 100 100)))
    ;(ds flying-thing (+ 500 (* 150 (q/cos (:angle state)))) (+ 500 (* 150 (q/sin (:angle state)))))
    (f-fill 80 80 255)
    (q/arc 500 500 200 100 0 5 :open)

    (ds arc 500 200)
    (ds arc-above 500 700)
    (ds quadz 700 700)

    (ds (create-line 1000 0 0) 500 500)
    (ds (create-line 0 1000 0) 500 500)
    (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))

;-

(q/defsketch my
  :title "My sketch"
  :size [1000 1000]
  ; Setup function called only once, during sketch initialization.
  :setup setup
  ; Update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode])
