(ns quil.q_functions
	(:use [quil.core])
  (:require [errors.errorgui :refer :all]
            [errors.prettify_exception :refer :all]))


;What to do about arguments that can take in multiple arguments

(defn not-nil? [x]
  (not (nil? x)))


(defn f-background [color & args]
  (try
     (if (< 3 (count args))
      (throw (Exception. "f-background expects either 1, 2, 3, or 4 arguments.")))

    (cond (= 0 (count args))
          {:pre [(assert (not-nil? color) "f-background expects a number but got nil as its first argument")]}

          (= 1 (count args))
          {:pre [(assert (not-nil? color) "f-background expects a number but got nil as its first argument")
                 (assert (not-nil? (first args)) "f-background expects a number but got nil as its second argument")]}

          (= 2 (count args))
          {:pre [(assert (not-nil? color) "f-background expects a number but got nil as its first argument")
                 (assert (not-nil? (first args)) "f-background expects a number but got nil as its second argument")
                 (assert (not-nil? (second args)) "f-background expects a number but got nil as its third argument")]}

          (= 3 (count args))
          {:pre [(assert (not-nil? color) "f-background expects a number but got nil as its first argument")
                 (assert (not-nil? (first args)) "f-background expects a number but got nil as its second argument")
                 (assert (not-nil? (second args)) "f-background expects a number but got nil as its third argument")
                 (assert (not-nil? (second (rest args))) "f-background expects a number but got nil as its 4th argument")]})


    (cond (= 0 (count args))
          (background color)

          (= 1 (count args))
          (background color (first args))

          (= 2 (count args))
          (background color (first args) (second args))

          (= 3 (count args))
          (background color (first args) (second args) (second (rest args))))

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
                 (assert (not-nil? (second (rest args))) "f-fill expects a number but got nil as its third argument")]}

          (= 4 (count args))
          {:pre [(assert (not-nil? (first args)) "f-fill expects a number but got nil as its first argument")
                 (assert (not-nil? (second args)) "f-fill expects a number but got nil as its second argument")
                 (assert (not-nil? (second (rest args))) "f-fill expects a number but got nil as its third argument")
                 (assert (not-nil? (second (rest (rest args)))) "f-fill expects a number but got nil as its 4th argument")]})


    (cond (vector? (first args))
          (fill (first (first args)) (second (first args)) (second (rest (first args))) (second (rest (rest (first args)))))

          (= 1 (count args))
          (fill (first args))

          (= 2 (count args))
          (fill (first args) (second args))

          (= 3 (count args))
          (fill (first args) (second args) (second (rest args)))

          (= 4 (count args))
          (fill (first args) (second args) (second (rest args)) (second (rest (rest args)))))

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


(defn f-stroke [color & args]
  (try
     (if (< 3 (count args))
      (throw (Exception. "f-stroke expects either 1, 2, 3, or 4 arguments.")))

    (cond (= 0 (count args))
          {:pre [(assert (not-nil? color) "f-stroke expects a number but got nil as its first argument")]}

          (= 1 (count args))
          {:pre [(assert (not-nil? color) "f-stroke expects a number but got nil as its first argument")
                 (assert (not-nil? (first args)) "f-stroke expects a number but got nil as its second argument")]}

          (= 2 (count args))
          {:pre [(assert (not-nil? color) "f-stroke expects a number but got nil as its first argument")
                 (assert (not-nil? (first args)) "f-stroke expects a number but got nil as its second argument")
                 (assert (not-nil? (second args)) "f-stroke expects a number but got nil as its third argument")]}

          (= 3 (count args))
          {:pre [(assert (not-nil? color) "f-stroke expects a number but got nil as its first argument")
                 (assert (not-nil? (first args)) "f-stroke expects a number but got nil as its second argument")
                 (assert (not-nil? (second args)) "f-stroke expects a number but got nil as its third argument")
                 (assert (not-nil? (second (rest args))) "f-stroke expects a number but got nil as its 4th argument")]})


    (cond (= 0 (count args))
          (stroke color)

          (= 1 (count args))
          (stroke color (first args))

          (= 2 (count args))
          (stroke color (first args) (second args))

          (= 3 (count args))
          (stroke color (first args) (second args) (second (rest args))))

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



