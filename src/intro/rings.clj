(ns intro.rings
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]
            [corefns.collection_fns :refer :all]
            [quil.core :as q]
            [quil.middleware :as m]
            [quil.q_functions :refer :all]
            [errors.errorgui :refer :all]
            [errors.prettify_exception :refer :all]))


(defn mod-rings [rings]
  (loop [args rings
         vect []]
    (if (not= (count args) 0)
      (let [x (:x (first args))
            y (:y (first args))]
        (recur (rest args) (if (< 0 (:trans (first args)))
                             (conj vect (assoc (first args) :size (inc (:size (first args))) :trans (dec (:trans (first args)))))
                             vect)))
      vect)))




(defn setup []
  (q/frame-rate 60)
  (q/color-mode :rgb)

  {:angle 0
   :count 0
   :pos [{:x (/ (q/width) 2) :y (/ (q/height) 2) :size 25 :c1 0 :c2 0 :c3 0 :trans 255}]})

;-
(defn update-state [state]
  (assoc state
    :angle (- (:angle state) 0.04)
    :pos (mod-rings (:pos state))
    ))



(defn mouse [state event]
  (cond (= (:button event) :left)
    (assoc state
      :count (inc (:count state))
      :pos (conj (:pos state) {:x (q/mouse-x)
                                          :y (q/mouse-y)
                                          :size 1
                                          :c1 (mod (q/mouse-x) 255)
                                          :c2 (mod (q/mouse-y) 255)
                                          :c3 (mod (+ (q/mouse-x) (q/mouse-y)) 255)
                                          :trans 255}))
        (and (= (:button event) :right) (:color-grid state))
        (assoc state :color-grid false)
        (and (= (:button event) :right) (not (:color-grid state)))
        (assoc state :color-grid true)
    :else
        state))




;-
(defn draw-state [state]


  (if (:color-grid state)
    (doseq [i (range 51)]
    (doseq [j (range 51)]
      (let [width (/ (q/width) 50)
            height (/ (q/height) 50)
            c1 (mod (* width i) 255)
            c2 (mod (* height j) 255)
            c3 (mod (+ (* width i) (* height j)) 255)]
        (ds (create-rect width height c1 c2 c3) (* width i) (* height j)))))
  (q/background 25 25 25 25))



  (q/text-size 32)
  (f-text (str (:count state)) (/ (q/width) 2) (/ (q/height) 10))

  (doseq [i (range (count (:pos state)))]
    (let [size (:size (nth (:pos state) i))
        c1 (:c1 (nth (:pos state) i))
        c2 (:c2 (nth (:pos state) i))
        c3 (:c3 (nth (:pos state) i))
        trans (:trans (nth (:pos state) i))
        x (:x (nth (:pos state) i))
        y (:y (nth (:pos state) i))]

    (ds (create-ellipse size size c1 c2 c3 trans) x y))))



;-

(q/defsketch start
  :title "Rings"
  :size [(- (min (q/screen-width) (q/screen-height)) 30) (- (min (q/screen-width) (q/screen-height)) 30)]
  :setup setup
  :update update-state
  :draw draw-state
  :mouse-clicked mouse
  :middleware [m/fun-mode])


