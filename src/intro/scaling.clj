(ns intro.scaling
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]
            [corefns.collection_fns :refer :all]
            [quil.core :as q]
            [quil.middleware :as m]
            [quil.q_functions :refer :all]
            [errors.errorgui :refer :all]
            [errors.prettify_exception :refer :all]))



(defn setup []
  (q/frame-rate 45)
  (q/color-mode :rgb)

  (def mouse (create-rect 10 10 255 80 80))
  (def mirror (create-line (q/width) 0  1 :white))
  (def rich (create-picture "src/images/rich_hickey.png"))

  {:angle 0
   :example 0
   :rich-pos [0 (/ (q/height) 2)]
   :safe-rich true
   :soldier-pos [(/ (q/width) 2) (/ (q/height) 2)]
   :move-to [(/ (q/width) 2) (/ (q/height) 2)]})

(defn mouse-clicked [state event]
  (cond (= (:button event) :left)
        (assoc state :example (mod (inc (:example state)) 4))
        (and (= (:button event) :right)
             (= (:example state) 3))
        (assoc state :move-to [(quot (q/mouse-x) 1) (quot (q/mouse-y) 1)])
        :else
        state))


(defn run-rich [state]
  (let [old-pos (:rich-pos state)
        x (first old-pos)
        y (second old-pos)]
    (cond (not= (:example state) 1)
          [x y]
          (> x (q/width))
          [0 y]
          (and (< (q/abs (- (q/mouse-x) x)) 100)
               (< (q/abs (- (q/mouse-y) y)) 100))
          [x y]
          :else
          [(+ x 4) y])))

(defn move-soldier [state]
  (let [x (first (:soldier-pos state))
        y (second (:soldier-pos state))
        mx (first (:move-to state))
        my (second (:move-to state))]

    [(cond (< x mx)
           (+ x 1)
           (> x mx)
           (- x 1)
           :else
           x)
     (cond (< y my)
           (+ y 1)
           (> y my)
           (- y 1)
           :else
           y)]))



;-

(defn update-state [state]
  (try
    (assoc state
      :angle (+ (:angle state) 0.02)
      :rich-pos (run-rich state)
      :safe-rich (and (< (q/abs (- (q/mouse-x) (first (:rich-pos state)))) 100)
                      (< (q/abs (- (q/mouse-y) (second (:rich-pos state)))) 100))
      :soldier-pos (move-soldier state))
    (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))



(defn boxes-on-boxes [state]
  (ds (above (create-rect (* (q/sin (:angle state)) 300) (* (q/sin (:angle state)) 300) 80 255 80)
             (create-rect (* (q/cos (:angle state)) 300) (* (q/cos (:angle state)) 300) 80 255 80)
             (create-rect (* (q/sin (:angle state)) 300) (* (q/sin (:angle state)) 300) 80 255 80))
      (/ (q/width) 2) (/ (q/height) 2)))

(defn complex-path [state]
  (ds (overlay rich
               (if (:safe-rich state)
                 (create-ellipse 200 200 255 80 80 50)
                 (create-ellipse 200 200 80 255 80 50)))
      (first (:rich-pos state))
      (second (:rich-pos state))))


(defn mirror-mouse [state]
  (ds mirror (/ (q/width) 2) (/ (q/height) 2))
  (ds mouse (q/mouse-x) (- (q/height) (q/mouse-y))))


(defn hiding-box [state]
  (ds mouse (first (:soldier-pos state)) (second (:soldier-pos state))))





;-
(defn draw-state [state]
  (try
    (f-background 25 25 25 25)
    (cond (= (:example state) 0)
          (boxes-on-boxes state)
          (= (:example state) 1)
          (complex-path state)
          (= (:example state) 2)
          (mirror-mouse state)
          (= (:example state) 3)
          (hiding-box state))
    (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))



;-
(q/defsketch start
  :title "Cool Scaling"
  :size [(- (min (q/screen-width) (q/screen-height)) 100) (- (min (q/screen-width) (q/screen-height)) 100)]
  :setup setup
  :update update-state
  :draw draw-state
  :mouse-clicked mouse-clicked
  :middleware [m/fun-mode])


