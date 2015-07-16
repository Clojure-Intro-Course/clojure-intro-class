(ns quil.q_functions
  (:use [quil.core]
        [inflections.core])
  (:require [errors.errorgui :refer :all]
            [errors.prettify_exception :refer :all]
            [quil.q_checks :refer :all]))



(def colors {:red [255 0 0] :blue [0 0 255] :yellow [255 255 0]
             :green [0 128 0] :purple [128 0 128] :orange [255 165 0]
             :pink [255 192 203] :black [0 0 0] :brown [165 42 42]
             :white [255 255 255] :grey [128 128 128] :silver [192 192 192]
             :gold [255 215 0] :cyan [0 255 255] :magenta [255 0 255]
             :maroon [128 0 0] :navy [0 0 128] :lime [0 255 0] :teal [0 128 128]})

(defn eval-color [color]
  (if (not-nil? (color colors))
    (apply fill (color colors))
    (throw (RuntimeException. (str "The keyword " color " is not a valid color"))))
  (current-fill))

;-------------------------------------------------------------------------------------------------------------------------------*** UNDERLYING FUNCTIONS ***

(defn f-arc [& args]
  (if (not= (count args) 7)
    (throw (Exception. "f-arc expects 7 arguments.")))
  {:pre [(not (assert (:boolean (check-if-not-nil? args))
                      (str "f-arc expects a number but got nil as its "(ordinalize (- (:arg-num (check-if-not-nil? args)) 2))" argument")))]}
  (apply arc args))



(defn f-background [& args]
  (if (< 4 (count args))
    (throw (Exception. "f-background expects either 1, 2, 3, or 4 arguments.")))

  {:pre [(not (assert (:boolean (check-if-not-nil? args))
                      (str "f-background expects a number but got nil as its "(ordinalize (:arg-num (check-if-not-nil? args)))" argument")))]}
  (if (and (= 1 (count args)) (keyword? (first args)))
    (background (eval-color (first args)))
    (apply background args)))


(defn f-ellipse [& args]
  {:pre [(not (assert (:boolean (check-if-not-nil? args))
                      (str "f-ellipse expects a number but got nil as its "(ordinalize (- (:arg-num (check-if-not-nil? args)) 2))" argument")))]}
  (apply ellipse args))


(defn f-fill [& args]
  (if (< 4 (count args))
    (throw (Exception. "f-fill expects either 1, 2, 3, or 4 arguments.")))
  {:pre [(not (assert (:boolean (check-if-not-nil? args))
                      (str "A shape's color expects a number but got nil as its "(ordinalize (:arg-num (check-if-not-nil? args)))" argument")))]}
  (if (and (= 1 (count args)) (keyword? (first args)))
    (fill (eval-color (first args)))
    (apply fill args)))


(defn f-line [& args]
  {:pre [(not (assert (:boolean (check-if-not-nil? args))
                      (str "f-line expects a number but got nil as its "(ordinalize (- (:arg-num (check-if-not-nil? args)) 2))" argument")))]}
  (apply line args))


(defn f-quad [& args]
  {:pre [(not (assert (:boolean (check-if-not-nil? args))
                      (str "f-quad expects a number but got nil as its "(ordinalize (:arg-num (check-if-not-nil? args)))" argument")))]}
  (apply quad args))


(defn f-rect [& args]
  {:pre [(not (assert (:boolean (check-if-not-nil? args))
                      (str "f-rect expects a number but got nil as its "(ordinalize (- (:arg-num (check-if-not-nil? args)) 2))" argument")))]}
  (apply rect args))


(defn f-stroke [& args]
  (if (< 4 (count args))
    (throw (Exception. "f-stroke expects either 1, 2, 3, or 4 arguments.")))
  {:pre [(not (assert (:boolean (check-if-not-nil? args))
                      (str "A color expects a number but got nil as its "(ordinalize (:arg-num (check-if-not-nil? args)))" argument")))]}
  (if (and (= 1 (count args)) (keyword? (first args)))
    (stroke (eval-color (first args)))
    (apply stroke args)))


(defn f-stroke-weight [weight]
  {:pre [(assert (not-nil? weight) "f-stroke-weight expects a number but got nil as its first argument")]}
  (stroke-weight weight))


(defn f-text [& args]
  {:pre [(not (assert (:boolean (check-if-not-nil? args))
                      (str "f-text expects a number but got nil as its "(ordinalize (:arg-num (check-if-not-nil? args)))" argument")))]}
  (apply text args))


(defn f-text-num [& args]
  {:pre [(not (assert (:boolean (check-if-not-nil? args))
                      (str "f-text-num expects a number but got nil as its "(ordinalize (:arg-num (check-if-not-nil? args)))" argument")))]}
  (apply text-num args))


(defn f-text-size [size]
  {:pre [(assert (not-nil? size) "f-text-size expects a number but got nil as its first argument")
         (assert (pos? size) "f-text-size expects a positive number but got a negative number as its first argument")]}
  (text-size size))


(defn f-triangle [& args]
  {:pre [(not (assert (:boolean (check-if-not-nil? args))
                      (str "f-ellipse expects a number but got nil as its "(ordinalize (:arg-num (check-if-not-nil? args)))" argument")))]}
  (apply triangle args))

;------------------------------------------------------------------------------------------------------------------------------*** SHAPE FUNCTIONS ***

(defn create-ellipse
  "Takes in width, height, and one through four RBG values (look at quil's fill function for exact RGB parameters).
  Creates a hashmap of the information relevant to the shape and its draw position and values needed by the ds function."
  [w h & colors]
  {:pre [(not (assert (:boolean (check-if-not-nil? [w h]))
                      (str "create-ellipse expects a number but got nil as its "(ordinalize (:arg-num (check-if-not-nil? [w h])))" argument")))]}
  {:w w
   :h h
   :tw w
   :th h
   :dx 0
   :dy 0
   :angle 0
   :ds (fn [x y pict wid hei cs angle]
         (if (> (count colors) 0)
           (apply f-fill colors)
           (no-fill))
         (with-translation [x y]
           (with-rotation [(/ (* PI angle) 180)]
             (f-ellipse 0 0 wid hei)))
         (no-fill))})


(defn create-arc
  "Takes in a width, height, starting angle, ending angle, and one through four RBG values (look at quil's fill function for exact RGB parameters).
  The start and stop angles are measured in degrees, with 0 or 360 being 3 o'clock.
  Creates a hashmap of the information relevant to the shape and its draw position and the values needed by the ds function."
  [w h start stop & colors]
  {:pre [(not (assert (:boolean (check-if-not-nil? [w h start stop]))
                      (str "create-arc expects a number but got nil as its "(ordinalize (:arg-num (check-if-not-nil? [w h start stop])))" argument")))]}
  {:w w
   :h h
   :tw w
   :th h
   :dx 0
   :dy 0
   :angle 0
   :ds (fn [x y pict wid hei cs angle]
         (if (> (count colors) 0)
           (apply f-fill colors)
           (no-fill))
         (with-translation [x y]
           (with-rotation [(/ (* PI angle) 180)]
             (f-arc 0 0 wid hei start stop :pie)))
         (no-fill))})


(defn create-line
  "Takes in one x and y position, a stroke weight, and one through four RBG values (look at quil's fill function for exact RGB parameters).
  The first position is automatically (0,0) and the line is drawn to the specified (x,y).
  Creates a hashmap of the information relevant to the shape and its draw position and the values needed by the ds function."
  [x2 y2 stroke & colors]
  {:pre [(not (assert (:boolean (check-if-not-nil? [x2 y2 stroke]))
                      (str "create-line expects a number but got nil as its "(ordinalize (:arg-num (check-if-not-nil? [x2 y2 stroke])))" argument")))]}
  {:w x2
   :h y2
   :tw x2
   :th y2
   :dx 0
   :dy 0
   :angle 0
   :ds (fn [x y pict wid hei cs angle]
         (stroke-weight stroke)
         (if (> (count colors) 0)
           (apply f-stroke colors)
           (no-stroke))
         (with-translation [x y]
           (with-rotation [(/ (* PI angle) 180)]
             (f-line (- 0 (/ wid 2)) (- 0 (/ hei 2)) (+ 0 (/ wid 2)) (+ 0 (/ hei 2)))))
         (f-stroke cs)
         (stroke-weight 1))})


(defn create-triangle
  "Takes in two x and y positions and one through four RBG values (look at quil's fill function for exact RGB parameters).
  The position of the first vertex is automatically (0,0) so the triangle will be drawn based off that point.
  Creates a hashmap of the information relevant to the shape and its draw position and the values needed by the ds function."
  [x2 y2 x3 y3 & colors]
  {:pre [(not (assert (:boolean (check-if-not-nil? [x2 y2 x3 y3]))
                      (str "create-triangle expects a number but got nil as its "(ordinalize (:arg-num (check-if-not-nil? [x2 y2 x3 y3])))" argument")))]}
  {:w (+ (abs (max 0 x2 x3))  (abs (min 0 x2 x3)))
   :h (+ (abs (max 0 y2 y3))  (abs (min 0 y2 y3)))
   :tw (+ (abs (max 0 x2 x3))  (abs (min 0 x2 x3)))
   :th (+ (abs (max 0 y2 y3))  (abs (min 0 y2 y3)))
   :dx 0
   :dy 0
   :angle 0
   :ds (fn [x y pict wid hei cs angle]
         (if (> (count colors) 0)
           (apply f-fill colors)
           (no-fill))
         (with-translation [x y]
           (with-rotation [(/ (* PI angle) 180)]
             (let [mid-x (quot (+ (max 0 x2 x3)  (min 0 x2 x3)) 2)
                   mid-y (quot (+ (max 0 y2 y3)  (min 0 y2 y3)) 2)]
               (f-triangle (- 0 mid-x) (- 0 mid-y)
                           (- x2 mid-x) (- y2 mid-y)
                           (- x3 mid-x) (- y3 mid-y)))))
         (no-fill))})


(defn create-quad
  "Takes in three pairs x and y positions, ordered x1 y1 x2 y2 x3 y3, and one through four RBG values (look at quil's fill function for exact RGB parameters).
  The position of the first vertex is automatically (0,0) so the quad will be drawn based on that point.
  Creates a hashmap of the information relevant to the shape and its draw position and the values needed by the ds function."
  [x2 y2 x3 y3 x4 y4 & colors]
  {:pre [(not (assert (:boolean (check-if-not-nil? [x2 y2 x3 y3 x4 y4]))
                      (str "create-quad expects a number but got nil as its "(ordinalize (:arg-num (check-if-not-nil? [x2 y2 x3 y3 x4 y4])))" argument")))]}
  {:w (+ (abs (max 0 x2 x3 x4))  (abs (min 0 x2 x3 x4)))
   :h (+ (abs (max 0 y2 y3 y4))  (abs (min 0 y2 y3 y4)))
   :tw (+ (abs (max 0 x2 x3 x4))  (abs (min 0 x2 x3 x4)))
   :th (+ (abs (max 0 y2 y3 y4))  (abs (min 0 y2 y3 y4)))
   :dx 0
   :dy 0
   :angle 0
   :ds (fn [x y pict wid hei cs angle]
         (if (> (count colors) 0)
           (apply f-fill colors)
           (no-fill))
         (with-translation [x y]
           (with-rotation [(/ (* PI angle) 180)]
             (let [mid-x (quot (+ (max 0 x2 x3 x4)  (min 0 x2 x3 x4)) 2)
                   mid-y (quot (+ (max 0 y2 y3 y4)  (min 0 y2 y3 y4)) 2)]
               (f-quad (+ 0 (- 0 mid-x)) (+ 0 (- 0 mid-y))
                       (+ 0 (- x2 mid-x)) (+ 0 (- y2 mid-y))
                       (+ 0 (- x3 mid-x)) (+ 0 (- y3 mid-y))
                       (+ 0 (- x4 mid-x)) (+ 0 (- y4 mid-y))))))
         (no-fill))})


(defn create-picture
  "Takes in a string of the image location, ex. \"src/images/SquidwardsEmbarrasingPhoto.jpg\".
  Creates a hashmap of the information relevant to the shape and its draw position and values needed by the ds function."
  [pic]
  {:pre [(not (assert (:boolean (check-if-not-nil? [pic]))
                      (str "create-picture expects a number but got nil as its "(ordinalize (:arg-num (check-if-not-nil? [pic])))" argument")))]}
  {:w (.width (load-image pic))
   :h (.height (load-image pic))
   :tw (.width (load-image pic))
   :th (.height (load-image pic))
   :dx 0
   :dy 0
   :pic pic
   :angle 0
   :rp (load-image pic)
   :ds (fn [x y pict wid hei cs angle]
         (with-translation [x y]
           (with-rotation [(/ (* PI angle) 180)] (image pict 0 0))))})


(defn create-rect
  "Takes in width, height, and one through four RBG values (look at quil's fill function for exact RGB parameters).
  Creates a hashmap of the information relevant to the shape and its draw position and values needed by the ds function."
  [w h & colors]
  {:pre [(not (assert (:boolean (check-if-not-nil? [w h]))
                      (str "create-rect expects a number but got nil as its "(ordinalize (:arg-num (check-if-not-nil? [w h])))" argument")))]}
  {:w w
   :h h
   :tw w
   :th h
   :dx 0
   :dy 0
   :angle 0
   :ds (fn [x y pict wid hei cs angle]
         (if (> (count colors) 0)
           (apply f-fill colors)
           (no-fill))
         (with-translation [x y]
           (with-rotation [(/ (* PI angle) 180)] (f-rect 0 0 wid hei)))
         (no-fill))})


(defn ds
  "ds (or draw-shape) takes in a shape, an x position to be drawn at, and a y position to be drawn at.
  Calls the internal function :ds of the shape or image hashmap with the input variables passed to it and draws the shape or image accordingly."
  [shape x y]
  {:pre [(not (assert (:boolean (check-if-not-nil? [shape x y]))
                      (str "draw-shape expects a number but got nil as its "(ordinalize (:arg-num (check-if-not-nil? [shape x y])))" argument")))]}
  (rect-mode :center)
  (image-mode :center)
  (if (not (vector? shape))
    ((:ds shape) x y (:rp shape) (:w shape) (:h shape) (current-stroke) (:angle shape))
    (doall (map #((:ds %) (+ x (:dx %)) (+ y (:dy %)) (:rp %) (:w %) (:h %) (current-stroke) (:angle %)) shape))))

(def draw-shape ds)


;------------------------------------------------------------------------------------------------------------------------------*** RELATIVE SHAPE FUNCTIONS ***

(defn w-recur
  "Helper function for calc-max-w that goes through and grabs all the :tw within each shape or image.
  Returns a vector of those values."
  [arguments]
  (loop [shapes arguments
         vect []]
    (if (not= (count shapes) 0)
      (if (vector? (first shapes))
        (recur (rest shapes) (conj vect (:tw (first (first shapes)))))
        (recur (rest shapes) (conj vect (:tw (first shapes)))))
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
  "Helper function for calc-max-h that goes through and grabs all the :th within each shape or image.
  Returns a vector of those values."
  [arguments]
  (loop [shapes arguments
         vect []]
    (if (not= (count shapes) 0)
      (if (vector? (first shapes))
        (recur (rest shapes) (conj vect (:th (first (first shapes)))))
        (recur (rest shapes) (conj vect (:th (first shapes)))))
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

;------------------------------------------------------------------------------------------------------------------------------***  ABOVE  ***


(defn eval-compshape-vertical
  "Helper function for eval-shapes-vertical that deals with the complex shapes.
  Returns a modified complex shape adjusted for additional shapes."
  [arguments numb th]
  (loop [shapes arguments
         vect []]
    (if (not= (count shapes) 0)
      (recur (rest shapes) (conj vect (assoc (first shapes) :dy (+ (:dy (first shapes)) (- (+ (quot th 2) numb) (quot tot-h 2))) :tw max-w :th tot-h)))
      vect)))


(defn eval-shapes-vertical
  "Helper function for above that decides whether it is a complex or simple shape and calculates the change in :dy of each shape's hashmap.
  Changes the needed information about the new complex shape."
  [arguments numb]
  (loop [shapes arguments
         numb numb
         vect []]
    (if (not= (count shapes) 0)
      (if (vector? (first shapes))
        (recur (rest shapes) (+ (:th (first (first shapes))) numb) (conj vect (eval-compshape-vertical (first shapes) numb (:th (first (first shapes))))))
        (recur (rest shapes) (+ (:th (first shapes)) numb) (conj vect (assoc (first shapes) :dy (- (+ (quot (:th (first shapes)) 2) numb) (quot tot-h 2)) :tw max-w :th tot-h))))
      vect)))


(defn above
  "Takes 1 or more shapes and puts them above each other.
  The first argument will be on the top, the last argument will be on the bottom.
  This returns a new complex shape."
  [& shapes]
  {:pre [(not (assert (:boolean (check-if-not-nil? shapes))
                      (str "above expects a number but got nil as its "(ordinalize (:arg-num (check-if-not-nil? shapes)))" argument")))]}
  (calc-tot-h shapes)
  (calc-max-w shapes)
  (vec (flatten (eval-shapes-vertical shapes 0))))

;------------------------------------------------------------------------------------------------------------------------------***  BESIDE  ***

(defn eval-compshape-horizontal
  "Helper function for eval-shapes-horizontal that deals with the complex shapes.
  Returns a modified complex shape adjusted for additional shapes."
  [arguments numb tw]
  (loop [shapes arguments
         vect []]
    (if (not= (count shapes) 0)
      (recur (rest shapes) (conj vect (assoc (first shapes) :dx (+ (:dx (first shapes)) (- (+ (/ tw 2) numb) (/ tot-w 2))) :tw tot-w :th max-h)))
      vect)))


(defn eval-shapes-horizontal
  "Helper function for beside that decides whether it is a complex or simple shape and calculates the change in :dx of each shape's hashmap.
  Changes the needed information about the new complex shape."
  [arguments number]
  (loop [shapes arguments
         numb number
         vect []]
    (if (not= (count shapes) 0)
      (if (vector? (first shapes))
        (recur (rest shapes) (+ (:tw (first (first shapes))) numb) (conj vect (eval-compshape-horizontal (first shapes) numb (:tw (first (first shapes))))))
        (recur (rest shapes) (+ (:tw (first shapes)) numb) (conj vect (assoc (first shapes) :dx (- (+ (/ (:tw (first shapes)) 2) numb) (/ tot-w 2)) :tw tot-w :th max-h))))
      vect)))


(defn beside
  "Takes 1 or more shapes and puts them beside each other.
  The first argument will be on the left, the last argument will be on the right.
  This returns a new complex shape."
  [& shapes]
  {:pre [(not (assert (:boolean (check-if-not-nil? shapes))
                      (str "beside expects a number but got nil as its "(ordinalize (:arg-num (check-if-not-nil? shapes)))" argument")))]}
  (calc-tot-w shapes)
  (calc-max-h shapes)
  (vec (flatten (eval-shapes-horizontal shapes 0))))


;------------------------------------------------------------------------------------------------------------------------------***  OVERLAY  ***

(defn eval-compshape-overlay
  "Helper function for eval-shapes-overlay that deals with the complex shapes."
  [arguments]
  (loop [shapes arguments
         vect []]
    (if (not= (count shapes) 0)
      (recur (rest shapes) (conj vect (assoc (first shapes) :tw max-w :th max-h)))
      vect)))


(defn eval-shapes-overlay
  "Helper function for overlay that decides wether it is a complex or simple shape and changes the needed information about the new complex shape."
  [arguments]
  (loop [shapes arguments
         vect []]
    (if (not= (count shapes) 0)
      (if (vector? (first shapes))
        (recur (rest shapes) (conj vect (eval-compshape-overlay (first shapes))))
        (recur (rest shapes) (conj vect (assoc (first shapes) :tw max-w :th max-h))))
      vect)))


(defn overlay
  "Takes 1 or more shapes and overlays them on each other.
  The first argument will be in the foreground of the shape, the last argument will be the background of the shape.
  This returns a new complex shape."
  [& shapes]
  {:pre [(not (assert (:boolean (check-if-not-nil? shapes))
                      (str "overlay expects a number but got nil as its "(ordinalize (:arg-num (check-if-not-nil? shapes)))" argument")))]}
  (calc-max-w shapes)
  (calc-max-h shapes)
  (vec (flatten (eval-shapes-overlay (reverse shapes)))))

;------------------------------------------------------------------------------------------------------------------------------***  OVERLAY-ALIGN  ***

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
  [vert hor arguments]
  (loop [shapes arguments
         vect []]
    (if (not= (count shapes) 0)
      (recur (rest shapes) (conj vect (assoc (first shapes) :tw max-w :th max-h :dy (overlay-compshape-vertical vert (first shapes)) :dx (overlay-compshape-horizontal hor (first shapes)))))
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
  [vert hor arguments]
  (loop [shapes arguments
         vect []]
    (if (not= (count shapes) 0)
      (if (vector? (first shapes))
        (recur (rest shapes) (conj vect (eval-compshape-overlay-align vert hor (first shapes))))
        (recur (rest shapes) (conj vect (assoc (first shapes) :tw max-w :th max-h :dy (overlay-vertical vert (first shapes)) :dx (overlay-horizontal hor (first shapes))))))
      vect)))

(defn overlay-align
  "Takes in a first argument of :top :center or :bottom for the vertical orientation, a second argument of :left :center or :right for the horizontal orientation,
  and 1 or more shapes and overlays them on each other with the specified orientation.
  The first argument will be in the foreground of the shape, the last argument will be the background of the shape.
  This returns a new complex shape."
  [vert hor & shapes]
  {:pre [(not (assert (:boolean (check-if-not-nil? shapes))
                      (str "overlay-align expects a number but got nil as its "(ordinalize (:arg-num (check-if-not-nil? shapes)))" argument")))]}
  (calc-max-w shapes)
  (calc-tot-w shapes)
  (calc-max-h shapes)
  (calc-tot-h shapes)
  (vec (flatten (eval-shapes-overlay-align vert hor (reverse shapes)))))


;-------------------------------------------------------------------------------------------------------------------------------***  ABOVE-ALIGN  ***

(defn eval-compshape-above-align-right
  "Helper function for eval-shapes-above-align-right that deals with complex shapes."
  [arguments numb th]
  (loop [shapes arguments
         vect []]
    (if (not= (count shapes) 0)
      (recur (rest shapes) (conj vect (assoc (first shapes)
                                      :dy (+ (:dy (first shapes)) (- (+ (quot th 2) numb) (quot tot-h 2)))
                                      :tw max-w
                                      :th tot-h
                                      :dx (if (= (:tw (first shapes)) max-w)
                                            (:dx (first shapes))
                                            (- (+ (:dx (first shapes)) (quot max-w 2)) (quot (:tw (first shapes)) 2))))))
      vect)))

(defn eval-shapes-above-align-right
  "Helper function for above-align that decides what changes to make in the hashmap for :right alignment."
  [arguments numb]
  (loop [shapes arguments
         numb numb
         vect []]
    (if (not= (count shapes) 0)
      (if (vector? (first shapes))
        (recur (rest shapes) (+ (:th (first (first shapes))) numb) (conj vect (eval-compshape-above-align-right (first shapes) numb (:th (first (first shapes))))))
        (recur (rest shapes) (+ (:th (first shapes)) numb) (conj vect (assoc (first shapes)
                                                                    :dy (- (+ (quot (:th (first shapes)) 2) numb) (quot tot-h 2))
                                                                    :dx (- (quot max-w 2) (quot (:tw (first shapes)) 2))
                                                                    :tw max-w
                                                                    :th tot-h))))
      vect)))

(defn eval-compshape-above-align-left
  "Helper function for eval-shapes-above-align-left that deals with complex shapes."
  [arguments numb th]
  (loop [shapes arguments
         vect []]
    (if (not= (count shapes) 0)
      (recur (rest shapes) (conj vect (assoc (first shapes)
                                      :dy (+ (:dy (first shapes)) (- (+ (quot th 2) numb) (quot tot-h 2)))
                                      :tw max-w
                                      :th tot-h
                                      :dx (if (= (:tw (first shapes)) max-w)
                                            (:dx (first shapes))
                                            (+ (- (:dx (first shapes)) (quot max-w 2)) (quot (:tw (first shapes)) 2))))))
      vect)))

(defn eval-shapes-above-align-left
  "Helper function for above-align that decides what changes to make in the hashmap for :left alignment."
  [arguments numb]
  (loop [shapes arguments
         numb numb
         vect []]
    (if (not= (count shapes) 0)
      (if (vector? (first shapes))
        (recur (rest shapes) (+ (:th (first (first shapes))) numb) (conj vect (eval-compshape-above-align-left (first shapes) numb (:th (first (first shapes))))))
        (recur (rest shapes) (+ (:th (first shapes)) numb) (conj vect (assoc (first shapes)
                                                                    :dy (- (+ (quot (:th (first shapes)) 2) numb) (quot tot-h 2))
                                                                    :dx (- (quot (:tw (first shapes)) 2) (quot max-w 2))
                                                                    :tw max-w
                                                                    :th tot-h))))
      vect)))

(defn above-align
  "Takes in a first argument of :right :left for the vertical orientation,
  and 1 or more shapes and puts them above each other with the specified alignment.
  The first argument will be on top, the last argument will be on bottom.
  This returns a new complex shape."
  [align & shapes]
  {:pre [(not (assert (:boolean (check-if-not-nil? shapes))
                      (str "above-align expects a number but got nil as its "(ordinalize (:arg-num (check-if-not-nil? shapes)))" argument")))]}
  (calc-tot-h shapes)
  (calc-max-w shapes)
  (cond
   (= align :right)
   (vec (flatten (eval-shapes-above-align-right shapes 0)))
   (= align :left)
   (vec (flatten (eval-shapes-above-align-left shapes 0)))
   :else
   (throw (Exception. "The function above-align takes in :right or :left as its first argument."))))


;------------------------------------------------------------------------------------------------------------------------------*** BESIDE-ALIGN ***

(defn eval-compshape-beside-align-top
  "Helper function for eval-shapes-beside-align-top that deals with complex shapes."
  [arguments numb tw]
  (loop [shapes arguments
         vect []]
    (if (not= (count shapes) 0)
      (recur (rest shapes) (conj vect (assoc (first shapes)
                                      :dx (+ (:dx (first shapes)) (- (+ (quot tw 2) numb) (quot tot-w 2)))
                                      :tw tot-w
                                      :th max-h
                                      :dy (if (= (:th (first shapes)) max-h)
                                            (:dy (first shapes))
                                            (+ (- (:dy (first shapes)) (quot max-h 2)) (quot (:th (first shapes)) 2))))))
      vect)))

(defn eval-shapes-beside-align-top
  "Helper function for beside-align that decides what changes to make in the hashmap for :top alignment."
  [arguments numb]
  (loop [shapes arguments
         numb numb
         vect []]
    (if (not= (count shapes) 0)
      (if (vector? (first shapes))
        (recur (rest shapes) (+ (:tw (first (first shapes))) numb) (conj vect (eval-compshape-beside-align-top (first shapes) numb (:tw (first (first shapes))))))
        (recur (rest shapes) (+ (:tw (first shapes)) numb) (conj vect (assoc (first shapes)
                                                                    :dx (- (+ (quot (:tw (first shapes)) 2) numb) (quot tot-w 2))
                                                                    :tw tot-w
                                                                    :th max-h
                                                                    :dy (- (quot (:th (first shapes)) 2) (quot max-h 2))))))
      vect)))

(defn eval-compshape-beside-align-bottom
  "Helper function for eval-shapes-beside-align-bottom that deals with complex shapes."
  [arguments numb tw]
  (loop [shapes arguments
         vect []]
    (if (not= (count shapes) 0)
      (recur (rest shapes) (conj vect (assoc (first shapes)
                                      :dx (+ (:dx (first shapes)) (- (+ (quot tw 2) numb) (quot tot-w 2)))
                                      :tw tot-w
                                      :th max-h
                                      :dy (if (= (:th (first shapes)) max-h)
                                            (:dy (first shapes))
                                            (- (+ (:dy (first shapes)) (quot max-h 2)) (quot (:th (first shapes)) 2))))))
      vect)))

(defn eval-shapes-beside-align-bottom
  "Helper function for beside-align that decides what changes to make in the hashmap for :bottom alignment."
  [arguments numb]
  (loop [shapes arguments
         numb numb
         vect []]
    (if (not= (count shapes) 0)
      (if (vector? (first shapes))
        (recur (rest shapes) (+ (:tw (first (first shapes))) numb) (conj vect (eval-compshape-beside-align-bottom (first shapes) numb (:tw (first (first shapes))))))
        (recur (rest shapes) (+ (:tw (first shapes)) numb) (conj vect (assoc (first shapes)
                                                                    :dx (- (+ (quot (:tw (first shapes)) 2) numb) (quot tot-w 2))
                                                                    :tw tot-w
                                                                    :th max-h
                                                                    :dy (- (quot max-h 2) (quot (:th (first shapes)) 2))))))
      vect)))

(defn beside-align
  "Takes in a first argument of :top :bottom for the vertical orientation,
  and 1 or more shapes and puts them beside each other with the specified alignment.
  The first argument will be on the left, the last argument will be on the right.
  This returns a new complex shape."
  [align & shapes]
  {:pre [(not (assert (:boolean (check-if-not-nil? shapes))
                      (str "beside-align expects a number but got nil as its "(ordinalize (:arg-num (check-if-not-nil? shapes)))" argument")))]}
  (calc-tot-w shapes)
  (calc-max-h shapes)
  (cond
   (= align :top)
   (vec (flatten (eval-shapes-beside-align-top shapes 0)))
   (= align :bottom)
   (vec (flatten (eval-shapes-beside-align-bottom shapes 0)))
   :else
   (throw (Exception. "The function beside-align takes in :top or :bottom as its first argument."))))

;------------------------------------------------------------------------------------------------------------------------------*** SCALE-SHAPE ***

(defn create-new-image-shape
  "Helper function for scale-image that makes it so side effects are not a problem"
  [shape scale-x scale-y]
  (def new-pic (create-picture (:pic shape)))
  (resize (:rp new-pic) (* (:w shape) scale-x) (* (:h shape) scale-y))
  new-pic)

(defn scale-image-helper
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
  (map #(scale-image-helper % scale-x scale-y) shape))


(defn scale-shape
  "Takes in a shape or image, x-axis scale, and y-axis scale.
  Scales the shape based off the values input.
  This returns a new complex shape if given a complex shape otherwise returns a new simple shape."
  [shape scale-x scale-y]
  {:pre [(not (assert (:boolean (check-if-not-nil? [shape scale-x scale-y]))
                      (str "scale-shape expects a number but got nil as its "(ordinalize (:arg-num (check-if-not-nil? [shape scale-x scale-y])))" argument")))]}
  (if (vector? shape)
    (vec (flatten (scale-complex shape scale-x scale-y)))
    (scale-image-helper shape scale-x scale-y)))

;------------------------------------------------------------------------------------------------------------------------------*** ROTATE-SHAPE ***

(defn rotate-shape
  "Function that takes in a shape or image and an angle in degrees.
  It then rotates the shape or image.
  This function returns a new shape.
  This function cannot be used on complex shapes."
  [shape angle]
  {:pre [(not (assert (:boolean (check-if-not-nil? [shape angle]))
                      (str "rotate-shape expects a number but got nil as its "(ordinalize (:arg-num (check-if-not-nil? [shape angle])))" argument")))]}
  (assoc shape :angle angle))
