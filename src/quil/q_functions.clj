(ns quil.q_functions
	(:use [quil.core])
  (:require [errors.errorgui :refer :all]
            [errors.prettify_exception :refer :all]))


;What to do about arguments that can take in multiple arguments

(defn not-nil? [x]
  (not (nil? x)))




(def colors {:red [255 0 0] :blue [0 0 255] :yellow [255 255 0] :green [0 128 0] :purple [128 0 128] :orange [255 165 0] :pink [255 192 203] :black [0 0 0] :brown [165 42 42] :white [255 255 255]
 :grey [128 128 128] :silver [192 192 192] :gold [255 215 0] :cyan [0 255 255] :magenta [255 0 255] :maroon [128 0 0] :navy [0 0 128] :lime [0 255 0] :teal [0 128 128]})

(defn eval-color [color]
  (if (not-nil? (color colors))
    (apply fill (color colors))
    (throw (RuntimeException. (str "The keyword " color " is not a valid color"))))
  (current-fill))

(defn f-arc [& args]
  (try
    (if (not= (count args) 7)
      (throw (Exception. "f-arc expects 7 arguments.")))
    {:pre [(assert (not-nil? (first args)) "f-arc expects a number but got nil as its first argument")
           (assert (not-nil? (second args)) "f-arc expects a number but got nil as its second argument")
           (assert (not-nil? (nth args 2)) "f-arc expects a number but got nil as its third argument")
           (assert (not-nil? (nth args 3)) "f-arc expects a number but got nil as its 4th argument")
           (assert (not-nil? (nth args 4)) "f-arc expects a number but got nil as its 5th argument")
           (assert (not-nil? (nth args 5)) "f-arc expects a number but got nil as its 6th argument")
           (assert (not-nil? (nth args 6)) "f-arc expects a number but got nil as its 7th argument")]}
    (apply arc args)
    (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))



(defn f-background [& args]
  (try
     (if (< 4 (count args))
      (throw (Exception. "f-background expects either 1, 2, 3, or 4 arguments.")))

    (cond (= 1 (count args))
          {:pre [(assert (not-nil? (first args)) "f-background expects a number but got nil as its first argument")]}

          (= 2 (count args))
          {:pre [(assert (not-nil? (first args)) "f-background expects a number but got nil as its first argument")
                 (assert (not-nil? (second args)) "f-background expects a number but got nil as its second argument")]}

          (= 3 (count args))
          {:pre [(assert (not-nil? (first args)) "f-background expects a number but got nil as its first argument")
                 (assert (not-nil? (second args)) "f-background expects a number but got nil as its second argument")
                 (assert (not-nil? (nth args 2)) "f-background expects a number but got nil as its third argument")]}

          (= 4 (count args))
          {:pre [(assert (not-nil? (first args)) "f-background expects a number but got nil as its first argument")
                 (assert (not-nil? (second args)) "f-background expects a number but got nil as its second argument")
                 (assert (not-nil? (nth args 2)) "f-background expects a number but got nil as its third argument")
                 (assert (not-nil? (nth args 3)) "f-background expects a number but got nil as its 4th argument")]})

    (if (and (= 1 (count args)) (keyword? (first args)))
      (background (eval-color (first args)))
      (apply background args))

  (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))


(defn f-ellipse [x-pos y-pos width length]
  (try
    {:pre [(assert (not-nil? x-pos) "f-ellipse expects a number but got nil as its first argument")
           (assert (not-nil? y-pos) "f-ellipse expects a number but got nil as its second argument")
           (assert (not-nil? width) "f-ellipse expects a number but got nil as its third argument")
           (assert (not-nil? length) "f-ellipse expects a number but got nil as its 4th argument")]}
    (ellipse x-pos y-pos width length)

  (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))

(defn f-fill [& args]
  (try
     (if (< 4 (count args))
      (throw (Exception. "f-fill expects either 1, 2, 3, or 4 arguments.")))

    (cond (= 1 (count args))
          {:pre [(assert (not-nil? (first args)) "f-fill expects a number but got nil as its first argument")]}

          (= 2 (count args))
          {:pre [(assert (not-nil? (first args)) "f-fill expects a number but got nil as its first argument")
                 (assert (not-nil? (second args)) "f-fill expects a number but got nil as its second argument")]}

          (= 3 (count args))
          {:pre [(assert (not-nil? (first args)) "f-fill expects a number but got nil as its first argument")
                 (assert (not-nil? (second args)) "f-fill expects a number but got nil as its second argument")
                 (assert (not-nil? (nth args 2)) "f-fill expects a number but got nil as its third argument")]}

          (= 4 (count args))
          {:pre [(assert (not-nil? (first args)) "f-fill expects a number but got nil as its first argument")
                 (assert (not-nil? (second args)) "f-fill expects a number but got nil as its second argument")
                 (assert (not-nil? (nth args 2)) "f-fill expects a number but got nil as its third argument")
                 (assert (not-nil? (nth args 3)) "f-fill expects a number but got nil as its 4th argument")]})

    (if (and (= 1 (count args)) (keyword? (first args)))
      (fill (eval-color (first args)))
      (apply fill args))

  (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))



(defn f-line [x1 y1 x2 y2]
  (try
    {:pre [(assert (not-nil? x1) "f-line expects a number but got nil as its first argument")
           (assert (not-nil? y1) "f-line expects a number but got nil as its second argument")
           (assert (not-nil? x2) "f-line expects a number but got nil as its third argument")
           (assert (not-nil? y2) "f-line expects a number but got nil as its 4th argument")]}
    (line x1 y1 x2 y2)

  (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))


(defn f-quad [x1 y1 x2 y2 x3 y3 x4 y4]
  (try

     {:pre [(assert (not-nil? x1) "f-quad expects a number but got nil as its first argument")
            (assert (not-nil? y1) "f-quad expects a number but got nil as its second argument")
            (assert (not-nil? x2) "f-quad expects a number but got nil as its third argument")
            (assert (not-nil? y2) "f-quad expects a number but got nil as its 4th argument")
            (assert (not-nil? x3) "f-quad expects a number but got nil as its 5th argument")
            (assert (not-nil? y3) "f-quad expects a number but got nil as its 6th argument")
            (assert (not-nil? x4) "f-quad expects a number but got nil as its 7th argument")
            (assert (not-nil? y4) "f-quad expects a number but got nil as its 8th argument")]}
    (quad x1 y1 x2 y2 x3 y3 x4 y4)

  (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))


(defn f-rect [x-pos y-pos width length]
  (try
    {:pre [(assert (not-nil? x-pos) "f-rect expects a number but got nil as its first argument")
           (assert (not-nil? y-pos) "f-rect expects a number but got nil as its second argument")
           (assert (not-nil? width) "f-rect expects a number but got nil as its third argument")
           (assert (not-nil? length) "f-rect expects a number but got nil as its 4th argument")]}
    (rect x-pos y-pos width length)

  (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))


(defn f-stroke [& args]
  (try
     (if (< 4 (count args))
      (throw (Exception. "f-stroke expects either 1, 2, 3, or 4 arguments.")))

    (cond (= 1 (count args))
          {:pre [(assert (not-nil? (first args)) "f-stroke expects a number but got nil as its first argument")]}

          (= 2 (count args))
          {:pre [(assert (not-nil? (first args)) "f-stroke expects a number but got nil as its first argument")
                 (assert (not-nil? (second args)) "f-stroke expects a number but got nil as its second argument")]}

          (= 3 (count args))
          {:pre [(assert (not-nil? (first args)) "f-stroke expects a number but got nil as its first argument")
                 (assert (not-nil? (second args)) "f-stroke expects a number but got nil as its second argument")
                 (assert (not-nil? (nth args 2)) "f-stroke expects a number but got nil as its third argument")]}

          (= 4 (count args))
          {:pre [(assert (not-nil? (first args)) "f-stroke expects a number but got nil as its first argument")
                 (assert (not-nil? (second args)) "f-stroke expects a number but got nil as its second argument")
                 (assert (not-nil? (nth args 2)) "f-stroke expects a number but got nil as its third argument")
                 (assert (not-nil? (nth args 3)) "f-stroke expects a number but got nil as its 4th argument")]})

    (if (and (= 1 (count args)) (keyword? (first args)))
      (stroke (eval-color (first args)))
      (apply stroke args))

  (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))

(defn f-stroke-weight [weight]
  (try
    {:pre [(assert (not-nil? weight) "f-stroke-weight expects a number but got nil as its first argument")]}
    (stroke-weight weight)

  (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))

(defn f-text [string x-pos y-pos]
  (try
    {:pre [(assert (not-nil? string) "f-text expects a number but got nil as its first argument")
           (assert (not-nil? x-pos) "f-text expects a number but got nil as its second argument")
           (assert (not-nil? y-pos) "f-text expects a number but got nil as its third argument")]}
    (text string x-pos y-pos)

  (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))

(defn f-text-num [num x-pos y-pos]
  (try
    {:pre [(assert (not-nil? num) "f-text-num expects a number but got nil as its first argument")
           (assert (not-nil? x-pos) "f-text-num expects a number but got nil as its second argument")
           (assert (not-nil? y-pos) "f-text-num expects a number but got nil as its third argument")]}
    (text-num num x-pos y-pos)

  (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))


(defn f-text-size [size]
  (try
    {:pre [(assert (not-nil? size) "f-text-size expects a number but got nil as its first argument")
           (assert (pos? size) "f-text-size expects a positive number but got a negative number as its first argument")]}
    (text-size size)

  (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))


(defn f-triangle [x1 y1 x2 y2 x3 y3]
  (try
    {:pre [(assert (not-nil? x1) "f-triangle expects a number but got nil as its first argument")
           (assert (not-nil? y1) "f-triangle expects a number but got nil as its second argument")
           (assert (not-nil? x2) "f-triangle expects a number but got nil as its third argument")
           (assert (not-nil? y2) "f-triangle expects a number but got nil as its 4th argument")
           (assert (not-nil? x3) "f-triangle expects a number but got nil as its 5th argument")
           (assert (not-nil? y3) "f-triangle expects a number but got nil as its 6th argument")]}
    (triangle x1 y1 x2 y2 x3 y3)

  (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))


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
         (with-translation [x y]
           (with-rotation [(/ (* PI angle) 180)]
             (f-ellipse 0 0 wid hei)))
         (no-fill))})

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
         (with-translation [x y]
           (with-rotation [(/ (* PI angle) 180)]
             (f-arc 0 0 wid hei start stop :pie)))
         (no-fill))})

(defn create-line
  "Takes in one x and y position, a stroke weight, and any RGB value for color that quil allows, look at quil's stroke function for exact input parameters.
  The first position is atomatically (0,0) so draw your line based off of that.
  Creates a hashmap of the information relevant to the shape and its draw position and the values needed by the ds function.
  Look at quil's line function for more information."
  [x2 y2 stroke & args]
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
         (stroke-weight stroke)
         (with-translation [x y]
           (with-rotation [(/ (* PI angle) 180)]
             (f-line (- 0 (/ wid 2)) (- 0 (/ hei 2)) (+ 0 (/ wid 2)) (+ 0 (/ hei 2)))))
         (f-stroke cs)
         (stroke-weight 1))})


(defn create-triangle
  "Takes in two x and y positions and any RGB value for color that quil allows, look at quil's fill function for exact input parameters.
  The first position is atomatically (0,0) so draw your shape based off of that.
  Creates a hashmap of the information relevant to the shape and its draw position and the values needed by the ds function.
  Look at quil's triangle function for more information."
  [x2 y2 x3 y3 & args]
  {:w (+ (abs(max 0 x2 x3))  (abs(min 0 x2 x3)))
   :h (+ (abs(max 0 y2 y3))  (abs(min 0 y2 y3)))
   :tw (+ (abs(max 0 x2 x3))  (abs(min 0 x2 x3)))
   :th (+ (abs(max 0 y2 y3))  (abs(min 0 y2 y3)))
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
         (with-translation [x y]
           (with-rotation [(/ (* PI angle) 180)]
             (let [mid-x (quot (+ (max 0 x2 x3)  (min 0 x2 x3)) 2)
                   mid-y (quot (+ (max 0 y2 y3)  (min 0 y2 y3)) 2)]
               (f-triangle (+ 0 (- 0 mid-x)) (+ 0 (- 0 mid-y))
                           (+ 0 (- x2 mid-x)) (+ 0 (- y2 mid-y))
                           (+ 0 (- x3 mid-x)) (+ 0 (- y3 mid-y))))))
         (no-fill))})

(defn create-quad
  "Takes in three x and y positions and any RGB value for color that quil allows, look at quil's fill function for exact input parameters.
  The first position is atomatically (0,0) so draw your shape based off of that.
  Creates a hashmap of the information relevant to the shape and its draw position and the values needed by the ds function.
  Look at quil's quad function for more information."
  [x2 y2 x3 y3 x4 y4 & args]
  {:w (+ (abs(max 0 x2 x3 x4))  (abs(min 0 x2 x3 x4)))
   :h (+ (abs(max 0 y2 y3 y4))  (abs(min 0 y2 y3 y4)))
   :tw (+ (abs(max 0 x2 x3 x4))  (abs(min 0 x2 x3 x4)))
   :th (+ (abs(max 0 y2 y3 y4))  (abs(min 0 y2 y3 y4)))
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
  "Takes in a string of the image location.
  Creates a hashmap of the information relevant to the shape and its draw position and values needed by the ds function.
  Look at quil's load-image function for more information."
  [pic]
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
          (f-fill (first args) (second args) (second (rest args)) (second (rest (rest args))))
          :else
          (no-fill))
         (with-translation [x y]
           (with-rotation [(/ (* PI angle) 180)] (f-rect 0 0 wid hei)))
         (no-fill))})

;---
(defn ds
  "Calls the internal function :ds of the shape or image hashmap with the input variables passed to it."
  [shape x y]
  (rect-mode :center)
  (image-mode :center)
  (if
    (not (vector? shape))
    ((:ds shape) x y (:rp shape) (:w shape) (:h shape) (current-stroke) (:angle shape))
    (doseq [i (range (count shape))]
      ((:ds (nth shape i))
       (+ x (:dx (nth shape i)))
       (+ y (:dy (nth shape i)))
       (:rp (nth shape i))
       (:w (nth shape i))
       (:h (nth shape i))
       (current-stroke)
       (:angle (nth shape i))))))

(def draw-shape ds)


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
      (recur (rest args) (conj vect (assoc (first args) :dx (+ (:dx (first args)) (- (+ (/ tw 2) numb) (/ tot-w 2))) :tw tot-w :th max-h)))
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
        (recur (rest args) (+ (:tw (first args)) numb) (conj vect (assoc (first args) :dx (- (+ (/ (:tw (first args)) 2) numb) (/ tot-w 2)) :tw tot-w :th max-h))))
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
  (resize (:rp new-pic) (* (:w shape) scale-x) (* (:h shape) scale-y))
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
