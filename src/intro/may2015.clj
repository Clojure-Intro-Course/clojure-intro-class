(ns intro.may2015
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]
            [corefns.collection_fns :refer :all]
            [quil.core :as q]
            [quil.middleware :as m]
            [quil.q_functions :refer :all]
            [errors.errorgui :refer :all]
            [errors.prettify_exception :refer :all]))



(defn create-ellipse
  "Takes in width height and any RGB value for color that quil allows, look at quil's fill function for exact input parameters.
  Creates a hashmap of the information relevant to the shape and its draw position and values needed by the ds function.
  Look at quil's ellipse function for more information."
  [w h & args]
  {:w w
   :h h
   :tw w
   :th h
   :dx 0
   :dy 0
   :angle 0
   :ds (fn [x y pict wid hei cs angle]
         (cond
          (= (count args) 1)
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

(defn create-arc
  "Takes in a width height start stop and any RGB value for color that quil allows, look at quil's fill function for exact input parameters.
  The start and stop are measured in radians.
  Creates a hashmap of the information relevant to the shape and its draw position and the values needed by the ds function.
  Look at quil's arc function for more information."
  [w h start stop & args]
  {:w w
   :h h
   :tw w
   :th h
   :dx 0
   :dy 0
   :angle 0
   :ds (fn [x y pict wid hei cs angle]
         (cond
          (= (count args) 1)
          (f-fill (first args))
          (= (count args) 2)
          (f-fill (first args) (second args))
          (= (count args) 3)
          (f-fill (first args) (second args) (second (rest args)))
          (= (count args) 4)
          (f-fill (first args) (second args) (second (rest args)) (second (rest (rest args)))))
         (q/with-translation [x y]
           (q/with-rotation [(/ (* q/PI angle) 180)]
             (f-arc 0 0 wid hei start stop :pie)))
         (q/no-fill))})

(defn create-line
  "Takes in one x and y position and any RGB value for color that quil allows, look at quil's stroke function for exact input parameters.
  The first position is atomatically (0,0) so draw your line based off of that.
  Creates a hashmap of the information relevant to the shape and its draw position and the values needed by the ds function.
  Look at quil's line function for more information."
  [x2 y2 & args]
  {:w x2
   :h y2
   :tw x2
   :th y2
   :dx 0
   :dy 0
   :angle 0
   :ds (fn [x y pict wid hei cs angle]
         (cond
          (= (count args) 1)
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


(defn create-triangle
  "Takes in two x and y positions and any RGB value for color that quil allows, look at quil's fill function for exact input parameters.
  The first position is atomatically (0,0) so draw your shape based off of that.
  Creates a hashmap of the information relevant to the shape and its draw position and the values needed by the ds function.
  Look at quil's triangle function for more information."
  [x2 y2 x3 y3 & args]
  {:w (+ (q/abs(max 0 x2 x3))  (q/abs(min 0 x2 x3)))
   :h (+ (q/abs(max 0 y2 y3))  (q/abs(min 0 y2 y3)))
   :tw (+ (q/abs(max 0 x2 x3))  (q/abs(min 0 x2 x3)))
   :th (+ (q/abs(max 0 y2 y3))  (q/abs(min 0 y2 y3)))
   :dx 0
   :dy 0
   :angle 0
   :ds (fn [x y pict wid hei cs angle]
         (cond
          (= (count args) 1)
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

(defn create-quad
  "Takes in three x and y positions and any RGB value for color that quil allows, look at quil's fill function for exact input parameters.
  The first position is atomatically (0,0) so draw your shape based off of that.
  Creates a hashmap of the information relevant to the shape and its draw position and the values needed by the ds function.
  Look at quil's quad function for more information."
  [x2 y2 x3 y3 x4 y4 & args]
  {:w (+ (q/abs(max 0 x2 x3 x4))  (q/abs(min 0 x2 x3 x4)))
   :h (+ (q/abs(max 0 y2 y3 y4))  (q/abs(min 0 y2 y3 y4)))
   :tw (+ (q/abs(max 0 x2 x3 x4))  (q/abs(min 0 x2 x3 x4)))
   :th (+ (q/abs(max 0 y2 y3 y4))  (q/abs(min 0 y2 y3 y4)))
   :dx 0
   :dy 0
   :angle 0
   :ds (fn [x y pict wid hei cs angle]
         (cond
          (= (count args) 1)
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


(defn create-picture
  "Takes in a string of the image location.
  Creates a hashmap of the information relevant to the shape and its draw position and values needed by the ds function.
  Look at quil's load-image function for more information."
  [pic]
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


(defn create-rect
  "Takes in width height and any RGB value for color that quil allows, look at quil's fill function for exact input parameters.
  Creates a hashmap of the information relevant to the shape and its draw position and values needed by the ds function.
  Look at quil's rect function for more information."
  [w h & args]
  {:w w
   :h h
   :tw w
   :th h
   :dx 0
   :dy 0
   :angle 0
   :ds (fn [x y pict wid hei cs angle]
         (cond
          (= (count args) 1)
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
(defn ds
  "Calls the internal function :ds of the shape or image hashmap with the input variables passed to it."
  [shape x y]
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
(defn w-recur
  "Helper function for calc-max-w that goes through and grabs all the :tw within each shape or image."
  [args]
  (loop [args args
         vect []]
    (if (not= (count args) 0)
      (if (vector? (first args))
        (recur (rest args) (conj vect (:tw (first (first args)))))
        (recur (rest args) (conj vect (:tw (first args)))))
      vect)))

(defn calc-max-w
  "Calculates the max width by finding the maximum :tw of all of the shapes.
  This is different from calc-tot-w which adds all of the :tw of the shapes together."
  [args]
  (def max-w
    (apply max (w-recur args))))

(defn calc-tot-w
  "Calculates the total width by adding all of the :tw of each shape together.
  This is different from calc-max-w which grabs the largest :tw of all the shapes."
  [args]
  (def tot-w
    (reduce + (w-recur args))))

;---
(defn h-recur
  "Helper function for calc-max-h that goes through and grabs all the :th within each shape or image."
  [args]
  (loop [args args
         vect []]
    (if (not= (count args) 0)
      (if (vector? (first args))
        (recur (rest args) (conj vect (:th (first (first args)))))
        (recur (rest args) (conj vect (:th (first args)))))
      vect)))

(defn calc-max-h
  "Calculates the max height by finding the maximum :th of all of the shapes.
  This is different from calc-tot-h which adds all of the :th of the shapes together."
  [args]
  (def max-h
    (apply max (h-recur args))))

(defn calc-tot-h
  "Calculates the total height by adding all of the :th of each shape together.
  This is different from calc-max-h which grabs the largest :th of all the shapes."
  [args]
  (def tot-h
    (reduce + (h-recur args))))

;------                  ***  ABOVE  ***


(defn eval-compshape-vertical
  "Helper function for eval-shapes-vertical that deals with the complex shapes."
  [args numb th]
  (loop [args args
         vect []]
    (if (not= (count args) 0)
      (recur (rest args) (conj vect (assoc (first args) :dy (+ (:dy (first args)) (- (+ (quot th 2) numb) (quot tot-h 2))) :tw max-w :th tot-h)))
      vect)))


(defn eval-shapes-vertical
  "Helper function for above that decides wether it is a complex or simple shape and calculates the change in :dy of each shape's hashmap.
  Changes the needed information about the new complex shape."
  [args numb]
  (loop [args args
         numb numb
         vect []]
    (if (not= (count args) 0)
      (if (vector? (first args))
        (recur (rest args) (+ (:th (first (first args))) numb) (conj vect (eval-compshape-vertical (first args) numb (:th (first (first args))))))
        (recur (rest args) (+ (:th (first args)) numb) (conj vect (assoc (first args) :dy (- (+ (quot (:th (first args)) 2) numb) (quot tot-h 2)) :tw max-w :th tot-h))))
      vect)))


(defn above
  "Takes 1 or more shapes and puts them above each other.
  The first argument will be on the top, the last argument will be on the bottom.
  This returns a new complex shape."
  [& args]
  (calc-tot-h args)
  (calc-max-w args)
  (vec (flatten (eval-shapes-vertical args 0))))

;------                  ***  BESIDE  ***

(defn eval-compshape-horizontal
  "Helper function for eval-shapes-horizontal that deals with the complex shapes."
  [args numb tw]
  (loop [args args
         vect []]
    (if (not= (count args) 0)
      (recur (rest args) (conj vect (assoc (first args) :dx (+ (:dx (first args)) (- (+ (quot tw 2) numb) (quot tot-w 2))) :tw tot-w :th max-h)))
      vect)))


(defn eval-shapes-horizontal
  "Helper function for beside that decides wether it is a complex or simple shape and calculates the change in :dx of each shape's hashmap.
  Changes the needed information about the new complex shape."
  [args numb]
  (loop [args args
         numb numb
         vect []]
    (if (not= (count args) 0)
      (if (vector? (first args))
        (recur (rest args) (+ (:tw (first (first args))) numb) (conj vect (eval-compshape-horizontal (first args) numb (:tw (first (first args))))))
        (recur (rest args) (+ (:tw (first args)) numb) (conj vect (assoc (first args) :dx (- (+ (quot (:tw (first args)) 2) numb) (quot tot-w 2)) :tw tot-w :th max-h))))
      vect)))


(defn beside
  "Takes 1 or more shapes and puts them beside each other.
  The first argument will be on the left, the last argument will be on the right.
  This returns a new complex shape."
  [& args]
  (calc-tot-w args)
  (calc-max-h args)
  (vec (flatten (eval-shapes-horizontal args 0))))


;------                  ***  OVERLAY  ***

(defn eval-compshape-overlay
  "Helper function for eval-shapes-overlay that deals with the complex shapes."
  [args]
  (loop [args args
         vect []]
    (if (not= (count args) 0)
      (recur (rest args) (conj vect (assoc (first args) :tw max-w :th max-h)))
      vect)))


(defn eval-shapes-overlay
  "Helper function for overlay that decides wether it is a complex or simple shape and changes the needed information about the new complex shape."
  [args]
  (loop [args args
         vect []]
    (if (not= (count args) 0)
      (if (vector? (first args))
        (recur (rest args) (conj vect (eval-compshape-overlay (first args))))
        (recur (rest args) (conj vect (assoc (first args) :tw max-w :th max-h))))
      vect)))


(defn overlay
  "Takes 1 or more shapes and overlays them on each other.
  The first argument will be on the front of the shape, the last argument will be the back of the shape.
  This returns a new complex shape."
  [& args]
  (calc-max-w args)
  (calc-max-h args)
  (vec (flatten (eval-shapes-overlay (reverse args)))))

;------                  ***  OVERLAY-ALIGN  ***

(defn overlay-compshape-vertical
  "Helper function for eval-compshape-overlay-align that decides what the :dy needs to be changed to."
  [vert arg]
  (cond
   (= vert :top)
   (if (= (:th arg) max-h)
     (:dy arg)
     (+ (- (:dy arg) (quot max-h 2)) (quot (:th arg) 2)))
   (= vert :center)
   (:dy arg)
   (= vert :bottom)
   (if (= (:th arg) max-h)
     (:dy arg)
     (- (+ (:dy arg) (quot max-h 2)) (quot (:th arg) 2)))
   :else
   (throw (Exception. "The function overlay-align takes :top, :center, or :bottom as its first argument."))))

(defn overlay-compshape-horizontal
  "Helper function for eval-compshape-overlay-align that decides what the :dx needs to be changed to."
  [hor arg]
  (cond
   (= hor :left)
   (if (= (:tw arg) max-w)
     (:dx arg)
     (+ (- (:dx arg) (quot max-w 2)) (quot (:tw arg) 2)))
   (= hor :center)
   (:dx arg)
   (= hor :right)
   (if (= (:tw arg) max-w)
     (:dx arg)
     (- (+ (:dx arg) (quot max-w 2)) (quot (:tw arg) 2)))
   :else
   (throw (Exception. "The function overlay-align takes :left, :center, or :right as its second argument."))))

(defn eval-compshape-overlay-align
  "Helper function for eval-shapes-overlay-align that deals with complex shapes."
  [vert hor args]
  (loop [args args
         vect []]
    (if (not= (count args) 0)
      (recur (rest args) (conj vect (assoc (first args) :tw max-w :th max-h :dy (overlay-compshape-vertical vert (first args)) :dx (overlay-compshape-horizontal hor (first args)))))
      vect)))

(defn overlay-vertical
  "Helper function for eval-shapes-overlay-align that decides what the :dy needs to be changed to."
  [vert arg]
  (cond
   (= vert :top)
   (- (quot (:th arg) 2) (quot max-h 2))
   (= vert :center)
   (:dy arg)
   (= vert :bottom)
   (- (quot max-h 2) (quot (:th arg) 2))
   :else
   (throw (Exception. "The function overlay-align takes :top, :center, or :bottom as its first argument."))))

(defn overlay-horizontal
  "Helper function for eval-shapes-overlay-align that decides what the :dx needs to be changed to."
  [hor arg]
  (cond
   (= hor :left)
   (- (quot (:tw arg) 2) (quot max-w 2))
   (= hor :center)
   (:dx arg)
   (= hor :right)
   (- (quot max-w 2) (quot (:tw arg) 2))
   :else
   (throw (Exception. "The function overlay-align takes :left, :center, or :right as its second argument."))))

(defn eval-shapes-overlay-align
  "Helper function for overlay-align that decides which information needs to be changed and to what."
  [vert hor args]
  (loop [args args
         vect []]
    (if (not= (count args) 0)
      (if (vector? (first args))
        (recur (rest args) (conj vect (eval-compshape-overlay-align vert hor (first args))))
        (recur (rest args) (conj vect (assoc (first args) :tw max-w :th max-h :dy (overlay-vertical vert (first args)) :dx (overlay-horizontal hor (first args))))))
      vect)))

(defn overlay-align
  "Takes in a first argument of :top :center or :bottom for the vertical orientation, a second argument of :left :center or :right for the horizontal orientation,
  and 1 or more shapes and overlays them on each other with the specified orientation.
  The first argument will be on the front of the shape, the last argument will be the back of the shape.
  This returns a new complex shape."
  [vert hor & args]
  (calc-max-w args)
  (calc-tot-w args)
  (calc-max-h args)
  (calc-tot-h args)
  (vec (flatten (eval-shapes-overlay-align vert hor (reverse args)))))


;------                  ***  ABOVE-ALIGN  ***

(defn eval-compshape-above-align-right
  "Helper function for eval-shapes-above-align-right that deals with complex shapes."
  [args numb th]
  (loop [args args
         vect []]
    (if (not= (count args) 0)
      (recur (rest args) (conj vect (assoc (first args)
                                      :dy (+ (:dy (first args)) (- (+ (quot th 2) numb) (quot tot-h 2)))
                                      :tw max-w
                                      :th tot-h
                                      :dx (if (= (:tw (first args)) max-w)
                                            (:dx (first args))
                                            (- (+ (:dx (first args)) (quot max-w 2)) (quot (:tw (first args)) 2))))))
      vect)))

(defn eval-shapes-above-align-right
  "Helper function for above-align that decides what changes to make in the hashmap for :right alignment."
  [args numb]
  (loop [args args
         numb numb
         vect []]
    (if (not= (count args) 0)
      (if (vector? (first args))
        (recur (rest args) (+ (:th (first (first args))) numb) (conj vect (eval-compshape-above-align-right (first args) numb (:th (first (first args))))))
        (recur (rest args) (+ (:th (first args)) numb) (conj vect (assoc (first args)
                                                                    :dy (- (+ (quot (:th (first args)) 2) numb) (quot tot-h 2))
                                                                    :dx (- (quot max-w 2) (quot (:tw (first args)) 2))
                                                                    :tw max-w
                                                                    :th tot-h))))
      vect)))

(defn eval-compshape-above-align-left
  "Helper function for eval-shapes-above-align-left that deals with complex shapes."
  [args numb th]
  (loop [args args
         vect []]
    (if (not= (count args) 0)
      (recur (rest args) (conj vect (assoc (first args)
                                      :dy (+ (:dy (first args)) (- (+ (quot th 2) numb) (quot tot-h 2)))
                                      :tw max-w
                                      :th tot-h
                                      :dx (if (= (:tw (first args)) max-w)
                                            (:dx (first args))
                                            (+ (- (:dx (first args)) (quot max-w 2)) (quot (:tw (first args)) 2))))))
      vect)))

(defn eval-shapes-above-align-left
  "Helper function for above-align that decides what changes to make in the hashmap for :left alignment."
  [args numb]
  (loop [args args
         numb numb
         vect []]
    (if (not= (count args) 0)
      (if (vector? (first args))
        (recur (rest args) (+ (:th (first (first args))) numb) (conj vect (eval-compshape-above-align-left (first args) numb (:th (first (first args))))))
        (recur (rest args) (+ (:th (first args)) numb) (conj vect (assoc (first args)
                                                                    :dy (- (+ (quot (:th (first args)) 2) numb) (quot tot-h 2))
                                                                    :dx (- (quot (:tw (first args)) 2) (quot max-w 2))
                                                                    :tw max-w
                                                                    :th tot-h))))
      vect)))

(defn above-align
  "Takes in a first argument of :right :left for the vertical orientation,
  and 1 or more shapes and puts them above each other with the specified orientation.
  The first argument will be on top, the last argument will be on bottom.
  This returns a new complex shape."
  [align & args]
  (calc-tot-h args)
  (calc-max-w args)
  (cond
   (= align :right)
   (vec (flatten (eval-shapes-above-align-right args 0)))
   (= align :left)
   (vec (flatten (eval-shapes-above-align-left args 0)))
   :else
   (throw (Exception. "The function above-align takes in :right or :left as its first argument."))))


;------                       *** BESIDE-ALIGN ***

(defn eval-compshape-beside-align-top
  "Helper function for eval-shapes-beside-align-top that deals with complex shapes."
  [args numb tw]
  (loop [args args
         vect []]
    (if (not= (count args) 0)
      (recur (rest args) (conj vect (assoc (first args)
                                      :dx (+ (:dx (first args)) (- (+ (quot tw 2) numb) (quot tot-w 2)))
                                      :tw tot-w
                                      :th max-h
                                      :dy (if (= (:th (first args)) max-h)
                                            (:dy (first args))
                                            (+ (- (:dy (first args)) (quot max-h 2)) (quot (:th (first args)) 2))))))
      vect)))

(defn eval-shapes-beside-align-top
  "Helper function for beside-align that decides what changes to make in the hashmap for :top alignment."
  [args numb]
  (loop [args args
         numb numb
         vect []]
    (if (not= (count args) 0)
      (if (vector? (first args))
        (recur (rest args) (+ (:tw (first (first args))) numb) (conj vect (eval-compshape-beside-align-top (first args) numb (:tw (first (first args))))))
        (recur (rest args) (+ (:tw (first args)) numb) (conj vect (assoc (first args)
                                                                    :dx (- (+ (quot (:tw (first args)) 2) numb) (quot tot-w 2))
                                                                    :tw tot-w
                                                                    :th max-h
                                                                    :dy (- (quot (:th (first args)) 2) (quot max-h 2))))))
      vect)))

(defn eval-compshape-beside-align-bottom
  "Helper function for eval-shapes-beside-align-bottom that deals with complex shapes."
  [args numb tw]
  (loop [args args
         vect []]
    (if (not= (count args) 0)
      (recur (rest args) (conj vect (assoc (first args)
                                      :dx (+ (:dx (first args)) (- (+ (quot tw 2) numb) (quot tot-w 2)))
                                      :tw tot-w
                                      :th max-h
                                      :dy (if (= (:th (first args)) max-h)
                                            (:dy (first args))
                                            (- (+ (:dy (first args)) (quot max-h 2)) (quot (:th (first args)) 2))))))
      vect)))

(defn eval-shapes-beside-align-bottom
  "Helper function for beside-align that decides what changes to make in the hashmap for :bottom alignment."
  [args numb]
  (loop [args args
         numb numb
         vect []]
    (if (not= (count args) 0)
      (if (vector? (first args))
        (recur (rest args) (+ (:tw (first (first args))) numb) (conj vect (eval-compshape-beside-align-bottom (first args) numb (:tw (first (first args))))))
        (recur (rest args) (+ (:tw (first args)) numb) (conj vect (assoc (first args)
                                                                    :dx (- (+ (quot (:tw (first args)) 2) numb) (quot tot-w 2))
                                                                    :tw tot-w
                                                                    :th max-h
                                                                    :dy (- (quot max-h 2) (quot (:th (first args)) 2))))))
      vect)))

(defn beside-align
  "Takes in a first argument of :top :bottom for the vertical orientation,
  and 1 or more shapes and puts them beside each other with the specified orientation.
  The first argument will be on the left, the last argument will be on the right.
  This returns a new complex shape."
  [align & args]
  (calc-tot-w args)
  (calc-max-h args)
  (cond
   (= align :top)
   (vec (flatten (eval-shapes-beside-align-top args 0)))
   (= align :bottom)
   (vec (flatten (eval-shapes-beside-align-bottom args 0)))
   :else
   (throw (Exception. "The function beside-align takes in :top or :bottom as its first argument."))))

;------                       *** SCALE-SHAPE ***

(defn create-new-image-shape
  "Helper function for scale-image that makes it so side effects are not a problem"
  [shape scale-x scale-y]
  (def new-pic (create-picture (:pic shape)))
  (q/resize (:rp new-pic) (* (:w shape) scale-x) (* (:h shape) scale-y))
  new-pic)

(defn scale-image
  "Helper function for scale-shape that decides which values should be changed to what for scale-shape."
  [shape scale-x scale-y]
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

(defn scale-complex
  "Helper function for scale-shape that breaks a complex shape into simple shapes."
  [shape scale-x scale-y]
  (loop [shape shape
         vect []]
    (if (not= (count shape) 0)
      (recur (rest shape) (conj vect (scale-image (first shape) scale-x scale-y)))
      vect)))

(defn scale-shape
  "Takes in a shape or image, x-axis scale, and y-axis scale.
  Scales the shape based off the values input.
  This returns a new complex shape if given a complex shape otherwise returns a new simple shape.
  Look at quil's scale function for more information."
  [shape scale-x scale-y]
  (if (vector? shape)
    (vec (flatten (scale-complex shape scale-x scale-y)))
    (scale-image shape scale-x scale-y)))

;------                       *** ROTATE-SHAPE ***

(defn rotate-shape
  "Function that takes in a shape or image and an angle in degrees.
  It then rotates the shape or image.
  This function returns a new shape.
  This function cannot be used on complex shapes.
  Look at quil's rotate function for more information."
  [shape angle]
  (assoc shape :angle angle))

;-----------------------------------------------------------
;                   SKETCH FUNCTIONS

(defn setup []
  (try
    (q/frame-rate 1)
    (q/color-mode :rgb)

    (def black-circle (create-ellipse 1 1 80 255 255))

    (def black-rect (create-rect 20 20 10))

    (def grey-rect (create-rect 40 40 125))

    (def white-circle (create-ellipse 20 20 225))

    (def big-rect (create-rect 100 100 255 80 255))

    (def bigger-rect (create-rect 150 150 255 80 80))


    (def sqr1 (create-rect 20 20 255 80 80))
    (def sqr2 (create-rect 40 40 80 255 80))
    (def sqr3 (create-rect 60 60 :orange))
    (def sqr4 (create-rect 80 80 :blue))
    (def sqr5 (create-rect 100 100 255 80 255))
    (def sqr6 (create-rect 120 120 80 255 255))
    (def sqr7 (create-rect 140 140 80 80 80))
    (def sqr8 (create-rect 160 160 255 255 255))
    (def sqr9 (create-rect 180 180 255 0 0))
    (def sqr10 (create-rect 200 200 0 80 80))
    (def sweet-line (create-line 0 200 0))
    (def long-box (create-rect 100 40 80 255 255))
    (def tall-box (create-rect 40 100 80 80 255))

    (def ellipse1 (create-ellipse 75 125 0 50 100))
    (def rot-ellipse1 (rotate-shape ellipse1 45))

    (def arc1 (create-arc 75 125 0 q/PI 50 100 150))
    (def rot-arc1 (rotate-shape arc1 90))

    (def line1 (create-line 150 150 0))
    (def rot-line1 (rotate-shape line1 135))

    (def triangle1 (create-triangle 100 125 75 -25 100 150 200))
    (def rot-triangle1 (rotate-shape triangle1 180))

    (def rect1 (create-rect 50 150 150 200 250))
    (def rot-rect1 (rotate-shape rect1 225))

    (def rich (scale-shape (create-picture "/home/hagen715/Desktop/images/rich_hickey.png") 0.3 0.3))
    (def rot-rich (rotate-shape rich 270))

    (def quad1 (create-quad 50 0 50 50 0 50 80 255 80))
    (def rot-quad1 (rotate-shape quad1 315))

    (def kappa (create-picture "/home/hagen715/Desktop/images/kappa96x130.png"))

    (def test-sqrs (above
                    (above sqr6
                           (beside (scale-shape sqr4 2 2)
                                   sqr3))
                    (above sqr6
                           (beside-align :bottom tall-box
                                         (scale-shape kappa 0.5 0.5)
                                         (overlay-align :top :left
                                                        sqr4
                                                        sqr3
                                                        (scale-shape (beside sqr4 sqr4) 2 2))))))


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
    (def kappa2 (beside-align :top (above (create-rect 96 170 80 255 80) kappa) (above (create-rect 96 70 80 255 80) kappa)))
    (def blokz (above tall-box
                      long-box))
    (def arc (create-arc 200 150 q/PI 6 255 80 255))
    (def arc-above (overlay-align :bottom :center arc sqr6))
    (def quadz (create-quad 100 0 100 100 0 100 80 255 255))



    {:shape black-circle
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
    (assoc state
      :angle (- (:angle state) 0.01)
      :angle2 (+ (:angle2 state) 0.7)
      :number (if (= (:number state) 255)
                0
                255))

    (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))

;-

(defn draw-state [state]
  (try
    (f-background :lime)
    (q/stroke-weight 5)
    ;(f-stroke :magenta)
    (f-text-size 40)
    (q/text-align :center)

    ;(q/with-translation [500 500] (q/with-rotation [q/PI] (ds flying-thing 100 100)))
    ;(ds flying-thing (+ 500 (* 150 (q/cos (:angle state)))) (+ 500 (* 150 (q/sin (:angle state)))))
    (f-fill 255 80 255)

    (ds arc1 500 500)

    ;(ds test-sqrs 500 500)
    (q/no-fill)
    ;(q/no-stroke)
    (q/text-num 500 500 100)
    ;(q/text "hello" 500 200)
    ;(ds (create-line 0 1000 255) 500 500)
    ;(ds (create-line 1000 0 255) 500 500)
    (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))

;-

(q/defsketch my
  :title "My sketch"
  :size [1920 1000]
  ; Setup function called only once, during sketch initialization.
  :setup setup
  ; Update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode])
