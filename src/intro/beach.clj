(ns intro.beach
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]
            [corefns.collection_fns :refer :all]
            [quil.core :as q]
            [quil.middleware :as m]
            [quil.q_functions :refer :all]
            [errors.errorgui :refer :all]
            [errors.prettify_exception :refer :all]))


(defn scale-w [x]
  (* (/ x 1000) (q/width)))

(defn scale-h [x]
  (* (/ x 1000) (q/height)))

(defn scale-min [x]
  (min (scale-w x) (scale-h x)))


(defn setup []
  (q/frame-rate 45)
  (q/color-mode :rgb)

  (def volley-ball (overlay (create-arc (scale-min 100) (scale-min 100) 0 (/ q/PI 3) 255 80 80)
                            (create-arc (scale-min 100) (scale-min 100) (/ q/PI 3) (/ (* 2 q/PI) 3) 255 255 80)
                            (create-arc (scale-min 100) (scale-min 100) (/ (* 2 q/PI) 3) q/PI 80 80 255)
                            (create-arc (scale-min 100) (scale-min 100) q/PI (/ (* 4 q/PI) 3) :orange)
                            (create-arc (scale-min 100) (scale-min 100) (/ (* 4 q/PI) 3) (/ (* 5 q/PI) 3) 255 80 255)
                            (create-arc (scale-min 100) (scale-min 100) (/ (* 5 q/PI) 3) q/TWO-PI :teal)
                            (create-ellipse (scale-min 100) (scale-min 100) 255)))


  (def player-1 (create-rect (scale-w 100) (scale-h 100)  :lime))
  (def player-2 (create-rect (scale-w 100) (scale-h 100)  :cyan))
  (def pole-1 (create-line (scale-w 0) (scale-h 500) 10 50))

  (def kappa (scale-shape  (create-picture "src/images/kappa96x130.png") (scale-w 1.2) (scale-h 1.2)))
  (def beach (scale-shape (create-picture "src/images/beach.jpg") (/ (q/width) 1000) (/ (q/height) 1000)))
  (def rich (scale-shape (create-picture "src/images/rich_hickey.png") (scale-w 1.5) (scale-h 1.5)))
  (def missing (scale-shape (create-picture "src/images/Status-image-missing-icon.png") (scale-w 0.9) (scale-h 0.9)))
  (def elena (scale-shape  (create-picture "src/images/elena.png") (scale-w 0.9) (scale-h 0.9)))

  (defn choose-image [state]
    (try
    (let [image (:image state)
          angle (:angle state)]
    (cond (= image "ball")
          (vec (map #(rotate-shape % (* 40 angle)) volley-ball))
          (= image "kappa")
          (rotate-shape kappa (* 40 angle))
          (= image "rich")
          (rotate-shape rich (* 40 angle))
          (= image "missing")
          (rotate-shape missing (* 40 angle))
          (= image "elena")
          (rotate-shape elena (* 40 angle))))
      (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))

  {:step 2
   :angle 0
   :angle2 q/HALF-PI
   :image "ball"})



;-
(defn update-state [state]
  (assoc state
    :angle (- (:angle state) 0.04)
    :angle2 (- (:angle2 state) 0.02)))



(defn mouse [state event]
  (if (= (:button event) :right)
    (cond (= (:image state) "ball")
          (assoc state :image "kappa")
          (= (:image state) "kappa")
          (assoc state :image "rich")
          (= (:image state) "rich")
          (assoc state :image "missing")
          (= (:image state) "missing")
          (assoc state :image "elena")
          (= (:image state) "elena")
          (assoc state :image "ball")
          :else
          state)
    state))





;-
(defn draw-state [state]
  (try
    (let
      [angle (:angle state)
       angle2 (:angle2 state)
       sin-a2 (q/sin angle2)]
      (f-background 237 201 175)
      (ds beach (/ (q/width) 2) (/ (q/height) 2))
      (q/no-stroke)

      (ds (scale-shape player-1
                       0.8
                       (* 0.8 (* 2 (q/sin (+ q/HALF-PI angle2)))))
          (scale-w 150)
          (scale-h (+ 610 (* 75 (q/sin (+ q/PI angle))))))

      (ds (scale-shape player-2
                       1
                       (* 2 sin-a2))
          (scale-w 850)
          (scale-h (+ 850 (* 75 (q/sin angle)))))

      (ds pole-1 (scale-w 300) (scale-h 800))
      (ds (scale-shape pole-1 0.6 0.6) (scale-w 775) (scale-h 750))

      (ds (scale-shape (choose-image state)
                       (+ 0.8 (* 0.2 (q/abs sin-a2)))
                       (+ 0.8 (* 0.2 (q/abs sin-a2))))
          (scale-w (+ 500 (* 350 (q/cos angle))))

          (scale-h (if (> (+ 500 (* 350 (q/cos angle))) 500)
                     (- 700 (* 500 (q/sin (mod angle q/PI))))
                     (- 500 (* 300 (q/sin (mod angle q/PI))))))))
    (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))

;-
(q/defsketch start
  :title "A Day at the Beach"
  :size [(- (min (q/screen-width) (q/screen-height)) 100) (- (min (q/screen-width) (q/screen-height)) 100)]
  :setup setup
  :update update-state
  :draw draw-state
  :mouse-clicked mouse
  :middleware [m/fun-mode])




