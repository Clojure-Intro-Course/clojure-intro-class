(ns intro.may2015
  (:require [expectations :refer :all]
            [corefns.corefns :refer :all]
            [corefns.collection_fns :refer :all]
            [quil.core :as q]
            [quil.middleware :as m]
            [quil.q_functions :refer :all]
            [errors.errorgui :refer :all]
            [errors.prettify_exception :refer :all]))


;; (defn track-to-pos [poses step]
;;   (loop [args poses
;;          vect []]
;;     (if (not= (count args) 0)
;;       (let [x (:x (first args))
;;             y (:y (first args))
;;             mx (q/mouse-x)
;;             my (q/mouse-y)
;;             dx (+ 0.1 (q/abs (- mx x)))
;;             dy (+ 0.1 (q/abs (- my y)))
;;             hyp (q/sqrt (+ (q/sq dx) (q/sq dy)))
;;             step-x (* dx (/ step hyp))
;;             step-y (* dy (/ step hyp))]

;;         (recur  (rest args) (conj vect {:x (cond (< x mx)
;;                                                  (+ x step-x)
;;                                                  (> x mx)
;;                                                  (- x step-x)
;;                                                  :else
;;                                                  x)
;;                                         :y (cond (< y my)
;;                                                  (+ y step-y)
;;                                                  (> y my)
;;                                                  (- y step-y)
;;                                                  :else
;;                                                  y)
;;                                         :shape (:shape (first args))})))
;;         vect)))






;-----------------------------------------------------------
;                   SKETCH FUNCTIONS

(defn setup []
  (try
    (q/frame-rate 45)
    (q/color-mode :rgb)


    (def sqr1 (create-rect 20 20 255 80 80))
    (def sqr2 (create-rect 40 40 80 255 80))
    (def sqr3 (create-rect 60 60 80 255 255))
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
    (def rich (scale-shape (create-picture "/home/hagen715/Desktop/images/rich_hickey.png") 0.3 0.3))
    (def rot-rich (rotate-shape rich 270))
    (def kappa (create-picture "/home/hagen715/Desktop/images/kappa96x130.png"))
    (def frankerz (create-picture "/home/hagen715/Desktop/images/frankerz220x200.jpg"))


;;     ;-----------------------------------------------------------
;;     (let [line-length 800
;;           box-length 800]

;;     (def diag-line (create-line (dec line-length) (dec line-length) 255 100))

;;     (def inviz-block (create-rect box-length box-length)))

;;     (def block-line (above diag-line
;;                            inviz-block))

;;     (def row (loop [w (/ (+ 200 (q/width)) (:tw (first block-line)))
;;                     shape block-line]
;;                (if (>= w 0)
;;                  (recur (dec w) (beside shape block-line))
;;                  shape)))

;;      (def lines (loop [h (/ (+ 200 (q/height)) (:th (first row)))
;;                     shape row]
;;                (if (>= h 0)
;;                  (recur (dec h) (above shape row))
;;                  shape)))
;;   {:angle 0
;;   :angle2 0}
    ;-----------------------------------------------------------



    (def volley-ball (overlay (create-arc 100 100 0 (/ q/PI 3) 255 80 80)
                              (create-arc 100 100 (/ q/PI 3) (/ (* 2 q/PI) 3) 255 255 80)
                              (create-arc 100 100 (/ (* 2 q/PI) 3) q/PI 80 80 255)
                              (create-arc 100 100 q/PI (/ (* 4 q/PI) 3) :orange)
                              (create-arc 100 100 (/ (* 4 q/PI) 3) (/ (* 5 q/PI) 3) 255 80 255)
                              (create-arc 100 100 (/ (* 5 q/PI) 3) q/TWO-PI :teal)
                              (create-ellipse 100 100 255)))


    (def player-1 (create-rect 100 100 80 255 80))
    (def player-2 (create-rect 100 100 80 255 255))
    (def pole-1 (create-line 0 500 100))


    ;(def beach (create-picture "/home/hagen715/Desktop/images/beach.jpg"))




{:step 2
     :angle 0
     :angle2 q/HALF-PI
     :pos [{:x 500 :y 500 :shape sqr1}
          ; {:x 400 :y 600 :shape sqr1}
          ; {:x 300 :y 700 :shape sqr1}
          ; {:x 200 :y 800 :shape sqr1}
           {:x 100 :y 900 :shape sqr1}]}





    (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))

;-
(defn update-state [state]
  (try
    (assert (not-nil? state) "Your state is nil")




    (assoc state
      :angle (- (:angle state) 0.04)
      :angle2 (- (:angle2 state) 0.02)

      ;:pos (track-to-pos (:pos state) (:step state))
      )


    (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))

;-
(defn draw-state [state]
  (try
    (f-background 25 25 25 25)
    ;-----------------------------------------------------------
;;     (f-stroke 255 80)
;;     (let [half-w (/ (q/width) 2)
;;           half-h (/ (q/height) 2)
;;           move-dist (:tw (first block-line))
;;           quarter-row (/ (:th (first row)) 4)]

;;     (ds lines (+ half-w (* (q/sin (:angle state)) move-dist)) (- half-h quarter-row))
;;     (ds lines (+ half-w (* (q/sin (:angle2 state)) move-dist)) (+ half-h quarter-row)))
    ;-----------------------------------------------------------


;;     (doseq [i (range (count (:pos state)))]
;;       (ds (:shape (nth (:pos state) i)) (:x (nth (:pos state) i)) (:y (nth (:pos state) i))))
    ;-----------------------------------------------------------


    ;-----------------------------------------------------------
;;     (def shape (scale-shape (create-rect 100 100 80 255 80) (+ 1.1 (q/sin (:angle state))) (+ 1.1 (q/sin (:angle state)))))
;;     (def other-shape (above (scale-shape sqr3 (+ 1.1 (q/sin (:angle2 state))) (+ 1.1 (q/sin (:angle2 state)))) shape (scale-image sqr3 (+ 1.1 (q/sin (:angle2 state))) (+ 1.1 (q/sin (:angle2 state))))))
;;     (ds other-shape 500 500)
    ;-----------------------------------------------------------
    (q/stroke 0)
    ;(ds beach 500 500)


     (q/stroke 255)
    (ds (scale-shape player-1 0.8 (* 0.8(* 2 (q/sin (+ q/HALF-PI (:angle2 state))))))
        150 (+ 630 (* 75 (q/sin (+ q/PI (:angle state))))))

    (ds (scale-shape player-2 1 (* 2 (q/sin (:angle2 state))))
        850 (+ 850 (* 75 (q/sin (:angle state)))))

    (q/stroke-weight 10)
    (ds pole-1 500 750)
    (q/stroke-weight 1)

    (ds volley-ball (+ 500 (* 350 (q/cos (:angle state))))

        (if (> (+ 500 (* 350 (q/cos (:angle state))))  500)
          (- 700 (* 500 (q/sin (mod (:angle state) q/PI))))
          (- 500 (* 300 (q/sin (mod (:angle state) q/PI))))))


    (catch Throwable e (println (.getCause e)) (display-error (prettify-exception e)))))


;-
(q/defsketch my
  :title "My sketch"
  :size [1000 1000]
  :setup setup
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode])
