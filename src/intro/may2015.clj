(ns intro.may2015
(:require [expectations :refer :all]
            [corefns.corefns :refer :all]
            [corefns.collection_fns :refer :all]
            [quil.core :as q]
            [quil.middleware :as m]
            [quil.quil :refer :all :as qq]))

(defn setup []
  "Sets up the inital state of the game.
   From setup, you can set your framerate and add or remove variables you use."

  (q/frame-rate 30)
  (q/color-mode :rgb)
  ;Put variables and their starting values for your game in the hash-map after this comment.
  {:speed 1
   :level 1
   :box-1-points 0
   :box-2-points 0
   :box-1-pos {:x 0 :y (- (q/height) 50)}
   :box-2-pos {:x (- (q/width) 50) :y (- (q/height) 50)}
   :rocks []
   :hit-player 0})

(defn update-speed [state] (:speed state))
(defn update-level[state] (:level state))
(defn update-box-1-points [state] (:box-1-points state))
(defn update-box-2-points [state] (:box-2-points state))

(defn spawn-rocks? [state] (= (quot (rand-int 6) 1) 5))


(defn move-rocks [state]
  (vec (for [x (:rocks state)
             :when (> (q/height) (second x))]
         (vector (first x) (+ 10 (second x))))))



(defn update-rocks [state]
  (move-rocks
   (if (spawn-rocks? state)
    (assoc state :rocks (conj (:rocks state) [(* (quot (rand-int 16) 1) 50) 0]))
    state)))


(defn hit [x1 y1 x2 y2 rx ry]
  (println "got to hit")
  (cond (and (= rx x1) (> ry y1) (< ry (+ 50 y1)))
        1
        (and (= rx x2) (> ry y2) (< ry (+ 50 y2)))
        2
        :else
        0))


(defn hit-player [state]
  (first
  (conj (vec (for [r (:rocks state)
                   :let [x1 (:x (:box-1-pos state))
                         y1 (:y (:box-1-pos state))
                         x2 (:x (:box-2-pos state))
                         y2 (:y (:box-2-pos state))
                         check (hit x1 y1 x2 y2 (first r) (second r))]
                   :when (not= check 0)]
                   check)) 0)))



(defn draw-rocks [state]

  (doseq [a (:rocks state)]
  (q/fill (+ 100 (rand-int 106)) 0 0)
  (q/rect (first a) (second a) 50 50)))


(defn direction [dir]
  (cond (or (= dir :w) (= dir :up))
        [0 -50]
        (or (= dir :s) (= dir :down))
        [0 50]
        (or (= dir :a) (= dir :left))
        [-50 0]
        (or (= dir :d) (= dir :right))
        [50 0]
        :else (println "You broke your keyboard!")))

(defn edge-of-map1? [dir state]
  (or (and (= (- (q/width) 50) (:x (:box-1-pos state))) (= dir [50 0]))
      (and (= 0 (:x (:box-1-pos state))) (= dir [-50 0]))
      (and (= (- (q/height) 50) (:y (:box-1-pos state))) (= dir [0 50]))
      (and (= 0 (:y (:box-1-pos state))) (= dir [0 -50]))))

(defn edge-of-map2? [dir state]
  (or (and (= (- (q/width) 50) (:x (:box-2-pos state))) (= dir [50 0]))
      (and (= 0 (:x (:box-2-pos state))) (= dir [-50 0]))
      (and (= (- (q/height) 50) (:y (:box-2-pos state))) (= dir [0 50]))
      (and (= 0 (:y (:box-2-pos state))) (= dir [0 -50]))))


(defn update-box-1-pos [dir state]
  (if (not (edge-of-map1? dir state))
  (assoc (:box-1-pos state)
    :x (+ (:x (:box-1-pos state)) (first dir))
    :y (+ (:y (:box-1-pos state)) (second dir)))
    (:box-1-pos state)))
;For changing player speed, look here! ^^^


(defn update-box-2-pos [dir state]
  (if (not (edge-of-map2? dir state))
  (assoc (:box-2-pos state)
    :x (+ (:x (:box-2-pos state)) (first dir))
    :y (+ (:y (:box-2-pos state)) (second dir)))
    (:box-2-pos state)))
;For changing player speed, look here! ^^^

(defn move [state event]
  (cond (or (= (:key event) :d) (= (:key event) :a) (= (:key event) :w) (= (:key event) :s))
        (assoc state :box-1-pos (update-box-1-pos (direction (:key event)) state))

        (or (= (:key event) :right) (= (:key event) :left) (= (:key event) :up) (= (:key event) :down))
        (assoc state :box-2-pos (update-box-2-pos (direction (:key event)) state))
        :else state))


(defn draw-points [state]
    (q/fill 255)
    (q/text-num (:box-1-points state) 25 50)
    (q/text-num (:box-2-points state) 750 50))



(defn update-state [state]
  "Takes in the current state and returns the updated state.
   Put functions that change your world state here"
  (cond (= (:hit-player state) 1)
        ({:speed (update-speed state)
        :level (update-level state)
        :box-1-points (update-box-1-points state)
        :box-2-points (update-box-2-points state)
        :box-1-pos (:box-1-pos state)
        :box-2-pos (:box-2-pos state)
        :rocks (update-rocks state)
        :hit-player (hit-player state)})

        (= (:hit-player state) 2)
        ({:speed (update-speed state)
          :level (update-level state)
          :box-1-points (update-box-1-points state)
          :box-2-points (update-box-2-points state)
          :box-1-pos (:box-1-pos state)
          :box-2-pos (:box-2-pos state)
          :rocks (update-rocks state)
          :hit-player (hit-player state)})
      :else
        {:speed (update-speed state)
        :level (update-level state)
        :box-1-points (update-box-1-points state)
        :box-2-points (update-box-2-points state)
        :box-1-pos (:box-1-pos state)
        :box-2-pos (:box-2-pos state)
        :rocks (update-rocks state)
         :hit-player (hit-player state)}))




(defn draw-state [state]
  (let [x1 (:x (:box-1-pos state))
        y1 (:y (:box-1-pos state))
        x2 (:x (:box-2-pos state))
        y2 (:y (:box-2-pos state))]
    (q/background 0)
    (q/stroke 255)
    (q/fill 80 255 80)
    (q/rect x1 y1 50 50)
    (if (and (= x1 x2) (= y1 y2))
      (q/fill 60 235 148)
    (q/fill 80 255 255))
    (q/rect x2 y2 50 50)

    (draw-rocks state)
    (draw-points state)

    (println state)))

(q/defsketch gol
  :title "Game"
  :size [800 1000]
  ; Setup function called only once, during sketch initialization.
  :setup setup
  ; Update-state is called on each iteration before draw-state.
  :update update-state
  (try
  :draw draw-state
    (catch Exception e (str "caught exception: " (.getMessage e))))
  :key-pressed move
  :middleware [m/fun-mode])

