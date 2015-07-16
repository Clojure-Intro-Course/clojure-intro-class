clojure-intro-class
===================

A pilot project to use Clojure for introductory computer science courses at the University of Minnesota - Morris

This repository contains both error-related tools, versions of core functions with preconditions, and a string library for beginners.

This project abstracts over Quil's fun-mode to add more Racket style functionality.

Some added examples of functionality.

Drawing some Lime rectangles next to each other

Quil's fun-mode:
(let [x 100
      y 100
      width 50
      height 50
  		numb 6
  		tot-w (* numb width)]
	  (q/fill 80 255 80)
	  (map (q/rect (+ (- x (/ (tot-w) 2)) (* (/ width 2) %)) y width height) (+ 1 (range numb)))
	  (q/no-fill)
	  
	  
Our super-fun-mode:
(def lime-rect (create-rect 50 50 :lime))
(def lime-rects (beside lime-rect
                        lime-rect
                        lime-rect
                        lime-rect
                        lime-rect
                        lime-rect)
(ds lime-rects 100 100)
