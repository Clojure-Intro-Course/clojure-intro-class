(ns intro.tracking
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]
            [corefns.collection_fns :refer :all]
            [quil.core :as q]
            [quil.middleware :as m]
            [quil.q_functions :refer :all]
            [errors.errorgui :refer :all]
            [errors.prettify_exception :refer :all]))


(defn track-to-pos [poses step]
  (loop [args poses
         vect []]
    (if (not= (count args) 0)
      (let [x (:x (first args))
            y (:y (first args))
            mx (q/mouse-x)
            my (q/mouse-y)
            dx (+ 0.1 (q/abs (- mx x)))
            dy (+ 0.1 (q/abs (- my y)))
            hyp (q/sqrt (+ (q/sq dx) (q/sq dy)))
            step-x (* dx (/ step hyp))
            step-y (* dy (/ step hyp))]

        (recur (rest args) (conj vect {:x (cond (< x mx)
                                                (+ x step-x)
                                                (> x mx)
                                                (- x step-x)
                                                :else
                                                x)
                                       :y (cond (< y my)
                                                (+ y step-y)
                                                (> y my)
                                                (- y step-y)
                                                :else
                                                y)
                                       :shape (:shape (first args))})))
      vect)))




(defn sum-x [vect]
  (loop [sum 0
         things vect]
  (if (not= (count things) 0)
    (recur (+ sum (:x (first vect))) (rest things))
    sum)))

(defn sum-y [vect]
  (loop [sum 0
         things vect]
  (if (not= (count things) 0)
    (recur (+ sum (:y (first vect))) (rest things))
    sum)))


(defn stacked [poses]
  (not (and (> (/ (sum-x poses) (count poses)) (- (:x (last poses)) 3))
            (< (/ (sum-x poses) (count poses)) (+ (:x (last poses)) 3))
            (> (/ (sum-y poses) (count poses)) (- (:y (last poses)) 3))
            (< (/ (sum-y poses) (count poses)) (+ (:y (last poses)) 3)))))


(defn rand-pos [poses step]
  (loop [args poses
         vect []]
    (if (not= (count args) 0)
      (let [x (:x (first args))
            y (:y (first args))
            mx (q/mouse-x)
            my (q/mouse-y)
            dx (+ 0.1 (q/abs (- mx x)))
            dy (+ 0.1 (q/abs (- my y)))
            hyp (q/sqrt (+ (q/sq dx) (q/sq dy)))
            step-x (* dx (/ step hyp))
            step-y (* dy (/ step hyp))]

        (recur (rest args) (conj vect {:x (cond (< x mx)
                                                (- x (rand-int 200))
                                                (> x mx)
                                                (+ x (rand-int 200))
                                                :else
                                                x)
                                       :y (cond (< y my)
                                                (- y (rand-int 200))
                                                (> y my)
                                                (+ y (rand-int 200))
                                                :else
                                                y)
                                       :shape (:shape (first args))})))
      vect)))





(defn setup []
  (q/frame-rate 45)
  (q/color-mode :rgb)

  (def sqr1 (create-rect 20 20 255 80 80))


  {:step 2
   :angle 0
   :angle2 q/HALF-PI
   :pos [{:x 700 :y 0 :shape sqr1}
         {:x 600 :y 0 :shape sqr1}
         {:x 500 :y 0 :shape sqr1}
         {:x 400 :y 0 :shape sqr1}
         {:x 300 :y 0 :shape sqr1}
         {:x 200 :y 0 :shape sqr1}
         {:x 100 :y 0 :shape sqr1}]})

;-
(defn update-state [state]
  (assoc state
    :angle (- (:angle state) 0.04)
    :angle2 (- (:angle2 state) 0.02)
    :pos (if (stacked (:pos state))
           (track-to-pos (:pos state) (:step state))
           (rand-pos (:pos state) (:step state)))))

;-
(defn draw-state [state]
  (q/background 25 25 25 25)
  (doseq [i (range (count (:pos state)))]
    (ds (:shape (nth (:pos state) i)) (:x (nth (:pos state) i)) (:y (nth (:pos state) i)))))
;-

(q/defsketch start
  :title "Tracking"
  :size [(- (min (q/screen-width) (q/screen-height)) 100) (- (min (q/screen-width) (q/screen-height)) 100)]
  :setup setup
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode])


