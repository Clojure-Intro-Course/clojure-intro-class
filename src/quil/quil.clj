(ns quil.quil
	(:use [quil.core])
)
;;Example of student code for show function :snake fn[coords] (map (rect #(:x %) #(:y %) "green" 100 100) coords)
;;Two possible options for dealing with order, extra level passed in to their function, or possible "z" axis (mergesort shapes by  axis before drawing)
;;Possible third option, pass in a display order vector that would be a vector of the student's keywords that would specify the order in which to draw the shapes

(def opaque 255)

(def color-codes
     {:red [255 0 0] :blue [0 0 255] :yellow [255 255 0] :green [0 128 0] :purple [128 0 128] :orange [255 165 0] :pink [255 192 203] :black [0 0 0] :brown [165 42 42] :white [255 255 255] :grey [128 128 128] :silver [192 192 192] :gold [255 215 0] :cyan [0 255 255] :magenta [255 0 255] :maroon [128 0 0] :navy [0 0 128] :lime [0 255 0] :teal [0 128 128]}
)

(def key-codes
  {:up-arrow 38, :down-arrow 40, :left-arrow 37, :right-arrow 39, :space 32, :shift 16, :tab 9, :backspace 8, :enter 10, :ctrl 17, :alt 18, :inverted-comma 192, :grave 222}
)

;; Easier Color Usage
;; red, blue, yellow, green, purple, orange, pink, black, white, gray, cyan, magenta, maroon, navy, lime, silver, gold
;; transparent colors too

(defn fill-color
  ([str] (let [color-key ((keyword str) color-codes)] (fill (first color-key) (second color-key) (last color-key) 255)))
  ([str transparency] (let [color-key ((keyword str) color-codes)] (fill (first color-key) (second color-key) (last color-key) transparency)))
)

(defn background-color
  ([str] (let [color-key ((keyword str) color-codes)] (background (first color-key) (second color-key) (last color-key) opaque)))
  ([str transparency] (let [color-key ((keyword str) color-codes)] (background (first color-key) (second color-key) (last color-key) transparency)))
)


(defn stroke-color
  ([str] (let [color-key ((keyword str) color-codes)] (stroke (first color-key) (second color-key) (last color-key) opaque)))
  ([str transparency] (let [color-key ((keyword str) color-codes)] (stroke (first color-key) (second color-key) (last color-key) transparency)))
)

;;Text
;;List of available fonts in text file "Fonts.txt"
(defn write-text [str size x y & [color-str font]]
	(cond
	(and (= color-str nil) (= font nil))
 		(do (fill-color "black")
		    (text-font (create-font "Liberation Sans" size true))
  		    (text str x y))
	(= color-str nil)
		(do (fill-color "black")
		    (text-font (create-font font size true))
  		    (text str x y))
	(= font nil)
		(do (fill-color color-str)
		    (text-font (create-font "Liberation Sans" size true))
  		    (text str x y))
	(not (or (= color-str nil) (= font nil)))
		(do (fill-color color-str)
	    		(text-font (create-font font size true))
  	    		(text str x y))
	)
)

;;(defn write-text
;;	([str size x y] (fill-color "black") (text-font (create-font "Liberation Sans" size true)) (text str x y))
;;	([str size x y font] (fill-color "black") (text-font (create-font font size true)) (text str x y))
;;	([str size x y color-str] (fill-color color-str) (text-font (create-font "Liberation Sans" size true)) (text str x y))
;;	([str size x y color-str font] (fill-color color-str) (text-font (create-font font size true)) (text str x y))
;;)


;; Shapes
(defn draw-rect
  ([x y width height str] (fill-color str opaque) (rect x y width height))
  ([x y width height str transparency] (fill-color str transparency) (rect x y width height))
)

(defn draw-circle
  ([x y radius str] (fill-color str opaque) (ellipse x y radius radius))
  ([x y radius str transparency] (fill-color str transparency) (ellipse x y radius radius))
)

(defn draw-ellipse
  ([x y width height str] (fill-color str opaque) (ellipse x y width height))
  ([x y width height str transparency] (fill-color str transparency) (ellipse x y width height))
)


(defn draw-triangle
  ([x1 y1 x2 y2 x3 y3 str] (fill-color str opaque) (triangle x1 y1 x2 y2 x3 y3))
  ([x1 y1 x2 y2 x3 y3 str transparency] (fill-color str transparency) (triangle x1 y1 x2 y2 x3 y3))
)

;;key-codes
(defn key-input []
	(let [key ((keyword (str (key-code))) key-codes)]
	     (if (nil? key) (char (key-code))
		key))
)
