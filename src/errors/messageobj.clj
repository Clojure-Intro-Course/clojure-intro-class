(ns errors.messageobj)
  ;(:refer corefn/core :only [add-fisrt add-last]))
;; Functions related to a message object. msg-info-obj
;; is a vector of parts of a message (in order). Each
;; part is a hash-map that contains the message text :msg,
;; the formatting id (e.g. :reg), the length of the text
;; :length
;; A msg-info-obj doesn't have :start

(defn make-msg-info-hash
	"creates a hash-map for a msg-info-obj out of a msg and style, with the form {:msg message :stylekey style :length n}"
	([msg style] (let [m (str msg)]
			{:msg m :stylekey style :length (count m)}))
	([msg] (let [m (str msg)]
			{:msg m :stylekey :reg :length (count m)})))

(defn- make-msg-info-hashes-helper [messages result]
	(if (empty? messages) result
		(let [next (second messages)]
			(if (keyword? next) (recur (rest (rest messages))
					           (conj result (make-msg-info-hash (first messages) next)))
				            (recur (rest messages)
				            	   (conj result (make-msg-info-hash (first messages))))))))

(defn make-msg-info-hashes [& args]
	"creates a vector of hash-maps out of a vector that are strings, possibly followed by optional keywords"
	(make-msg-info-hashes-helper args []))


(defn add-to-msg-info
  "adds an addition (converted to a string) to the end of an existing msg-info-obj, with an optional style keyword"
  ([old-msg-info addition style] (conj old-msg-info (make-msg-info-hash addition style)))
  ([old-msg-info addition] (conj old-msg-info (make-msg-info-hash addition))))


;(defn make-msg-info-hashes  [messages]
;	"creates a vector of hash-maps out of a vector of vectors of msg + optional style"
;	;; apply is needed since messages contains vectors of 1 or 2 elements
;	(map #(apply make-msg-info-hash %) messages))

(defn make-display-msg [msg-info-obj] ; msg-info-obj is a vector of hash-maps
  "fills in the starting points of objects in the hash maps, in the context of the output from make-msg-info-hashes "
  (loop [hashes msg-info-obj start 0 res []]
    (if (empty? hashes) res
      (recur (rest hashes)
      	     (+ start (:length (first hashes)))
      	     (conj res (assoc (first hashes) :start start))))))

(defn get-all-text [msg-obj]
   "concatenate all text from a message object into a string"
   (reduce #(str %1 (:msg %2)) "" msg-obj))

(defn make-mock-preobj [matches]
  "creates a test msg-info-obj. Used for testing so that things don't break"
  (make-msg-info-hashes  "This is a " "test." :arg))
