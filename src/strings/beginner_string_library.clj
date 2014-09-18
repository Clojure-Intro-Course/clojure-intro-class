(ns strings.beginner_string_library
  (:require [corefns.assert_handling :refer :all]
            [corefns.failed_asserts_info :refer :all]))

;#########################
;### Better String Fns ###
;#########################

;String Library

;Author: Emma Sax and Aaron Lemmon

;;; seq->string: sequence -> string
(defn seq->string
  "Takes a word as a sequence, a string, or nil
  and returns a string of the characters, returns an empty string for nil."
  [sequence]
  {:pre [(check-if-seqable? "seq->string" sequence 1)]}
  (apply str sequence))

;;; index-of: string, string, optional number -> number
(defn index-of
  "Takes one string and one character and an optional index number and returns the
  index of the first appearance of the character, returns -1 if the character isn't a
  substring of the first string or the index is larger than the last index of the first
  string or if the index is negative."
  ([string character]
   {:pre [(check-if-string? "index-of" string 1)
          (check-if-character? "index-of" character 2)]}
   (.indexOf string (str character)))
  ([string character index]
   {:pre [(check-if-string? "index-of" string 1)
          (check-if-character? "index-of" character 2)
          (check-if-number? "index-of" index 3)]}
   (.indexOf string (str character) index)))

;;; last-index-of: string, string, optional number -> number
(defn last-index-of
  "Takes one string and one character and an optional index number and returns the
  index of the last appearance of the character by searching backward through the
  string; else returns -1."
  ([string character]
   {:pre [(check-if-string? "last-index-of" string 1)
          (check-if-character? "last-index-of" character 2)]}
   (.lastIndexOf string (str character)))
  ([string character index]
   {:pre [(check-if-string? "last-index-of" string 1)
          (check-if-character? "last-index-of" character 2)
          (check-if-number? "last-index-of" index 3)]}
   (.lastIndexOf string (str character) index)))

;;; append: anything -> string
(defn append
  "Takes any number of anything and returns a string which appends the arguments
  together, returns an empty string for nil."
  [& args]
  (apply str args))

;;; char-at: string, number -> character
(defn char-at
  "Takes a string and an index and returns the character at the given index, returns
  nil if the index is bigger than the largest index of the string or if the index is
  negative."
  [string index]
  {:pre [(check-if-string? "char-at" string 1)
         (check-if-number? "char-at" index 2)]}
  (get string index))

;;; empty-string?: string -> boolean
(defn empty-string?
  "Takes a string and returns true if the length of the string is 0, returns false
  otherwise."
  [string]
  {:pre [(check-if-string? "empty-string?" string 1)]}
  (zero? (count string)))

;;; first-of-string: string -> character
(defn first-of-string
  "Takes a string and returns the first character of the string."
  [string]
  {:pre [(check-if-string? "first-of-string" string 1)]}
  (first string))

;;; last-of-string: string -> character
(defn last-of-string
  "Takes a string and returns the last character of the string."
  [string]
  {:pre [(check-if-string? "last-of-string" string 1)]}
  (last string))

;;; rest-of-string: string -> string
(defn rest-of-string
  "Takes a string and returns a new string with all of the elements in order
  except for the first character."
  [string]
  {:pre [(check-if-string? "rest-of-string" string 1)]}
  (apply str (rest string)))

;;; second-of-string: string -> character
(defn second-of-string
  "Takes a string and returns the second character of the string."
  [string]
  {:pre [(check-if-string? "second-of-string" string 1)]}
  (second string))

;;; string-contains?: string, string -> boolean
(defn string-contains?
  "Takes either two strings or a string and a character and returns true if the first
  string contains the second string/character, returns false otherwise."
  [string substring]
   {:pre [(check-if-string? "string-contains?" string 1)
          (check-if-string-or-character? "string-contains?" substring 2)]}
   (.contains string (str substring)))

;;; drop-from-string: number, string -> string
(defn drop-from-string
  "Takes an index and a string and returns a new string which drops all of
  the characters from 0-index (inclusive)."
  [index string]
  {:pre [(check-if-number? "drop-from-string" index 1)
         (check-if-string? "drop-from-string" string 2)]}
  (apply str (drop index string)))

;;; take-from-string: number, string -> string
(defn take-from-string
  "Takes a string and an index and returns a new string with only the characters
  from 0-index (exclusive)."
  [index string]
  {:pre [(check-if-number? "take-from-string" index 1)
         (check-if-string? "take-from-string" string 2)]}
  (apply str (take index string)))

;#########################################################################
;### these are all functions that are usually used with clojure.string ###
;#########################################################################

;;; reverse-string: string -> string
;(defn reverse-string
;  "Takes a string and returns a new string with the charactes reversed."
;  [string]
;  {:pre [(string? string)]
;   :post [(string? %)]}
;  (apply str (reverse string)))

;;; to-upper-case: string -> string
;(defn to-upper-case
;  "Takes a string and returns an new string with all of the characters
;  as upper case."
;  [string]
;  {:pre [(string? string)]
;   :post [(string? %)]}
;  (clojure.string/upper-case string))

;;; to-lower-case: string -> string
;(defn to-lower-case
;  "Takes a stringa and returns a new string with all of the characters
;  as lower case."
;  [string]
;  {:pre [(string? string)]
;   :post [(string? %)]}
;  (clojure.string/lower-case string))

;;; capitalize: string -> string
;(defn capitalize
;  "Takes a string and makes the first character uppercase and the the rest
;  lowercase."
;  [string]
;  {:pre [(string? string)]
;   :post [(string? %)]}
;  (clojure.string/capitalize string))

;;; separate-string: string, string -> string
;(defn separate-string
;  "Takes either one string and returns itself or takes two strings and
;  returns a new string with the second string's characters separated by
;  the first string."
;  ([string]
;   {:pre [(string? string)]
;   :post [(string? %)]}
;   (clojure.string/join string))
;  ([separator string]
;   {:pre [(string? separator) (string? string)]
;   :post [(string? %)]}
;   (clojure.string/join separator string)))

;;; replace-string: string, string, string -> string
;(defn replace-string
;  "Takes three strings, finds the substring of the second string in the first
;  string, and then returns a new string with the substring of the first string
;  replaced with the third string."
;  [string match replacement]
;  {:pre [(string? string) (string? match) (string? replacement)]
;   :post [(string? %)]}
;  (clojure.string/replace string match replacement))

;;;; there are no developed tests for replace-string-with-chars

;;; replace-string-with-chars: string, character, character -> string
;(defn replace-string-with-chars
;  "Takes one stringa and two characters, finds the substring of the first character
;  in the string, and then returns a new string with the first character
;  replaced with the second character."
;  [string match replacement]
;  {:pre [(string? string) (char? match) (char? replacement)]
;   :post [(string? %)]}
;  (clojure.string/replace string (str match) (str replacement)))

;;; split-string: string, string, optional number -> vector
;(defn split-string
;  "Takes a string and returns a vector which is the string but split on a
;  regular expression, or could also take a number limit that is the maximum
;  number of splits (puts the rest of the characters into the last split)."
;  ([string re]
;   {:pre [(string? string)]
;    :post [(vector? %)]}
;   (clojure.string/split string re))
;  ([string re limit]
;   {:pre [(string? string) (number? limit)]
;    :post [(vector? %)]}
;   (clojure.string/split string re limit)))

;;; split-lines-string: string -> string
;(defn split-lines-string
;  "Takes a string and returns a vector which take the string and splits it
;  on \n or \r\n."
;  [string]
;  {:pre [(string? string)]
;   :post [(vector? %)]}
;  (clojure.string/split-lines string))

;;; replace-first-of-string: string, string, string -> string
;(defn replace-first-of-string
;  "Takes a string and returns a string with the first occurrence of the second
;  string in the first string replaced by the third string, if the second string
;  doesn't occur in the first string, it just returns the first string."
;  [string match replacement]
;  {:pre [(string? string) (string? match) (string? replacement)]
;   :post [(string? %)]}
;  (clojure.string/replace-first string match replacement))

;;; trim-string: string -> string
;(defn trim-string
;  "Takes a string and returns a string with whitespace from both ends of the
;  string removed."
;  [string]
;  {:pre [(string? string)]
;   :post [(string? %)]}
;  (clojure.string/trim string))

;;; triml-string: string -> string
;(defn triml-string
;  "Takes a string and returns a string with whitespace from only the left end of the
;  string removed."
;  [string]
;  {:pre [(string? string)]
;   :post [(string? %)]}
;  (clojure.string/triml string))

;;; trimr-string: string -> string
;(defn trimr-string
;  "Takes a string and returns a string with whitespace from only the right end of the
;  string removed."
;  [string]
;  {:pre [(string? string)]
;   :post [(string? %)]}
;  (clojure.string/trimr string))

;;; trim-newline-string: string -> string
;(defn trim-newline-string
;  "Takes a string and returns a new stirng with all trailing newline \n or return
;  \r characters removed."
;  [string]
;  {:pre [(string? string)]
;   :post [(string? %)]}
;  (clojure.string/trim-newline string))

;;; string-blank?: string -> boolean
;(defn string-blank?
;  "Takes a string and returns true if the string is nil, empty, false, or contains
;  only whitespace."
;  [string]
;  {:pre [(or (string? string) (nil? string) (false? string))]
;   :post [(or (true? %) (false? %))]}
;  (clojure.string/blank? string))
