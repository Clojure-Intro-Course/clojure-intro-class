(ns strings.strings_test
  (:require [expectations :refer :all]
            [strings.strings :refer :all]
            [errors.messageobj :refer :all]
            [errors.exceptions :refer :all]))

;; Testing for our String Library

;; Author: Emma Sax and Aaron Lemmon

;; as a note: all clojure characters signified with a \ are stored as java characters

;#########################################
;### Testing for the better string fns ###
;#########################################

;; testing for seq->string
(expect "hello world" (seq->string '(\h \e \l \l \o \space \w \o \r \l \d)))
(expect "" (seq->string '()))
(expect "123" (seq->string [1 2 3]))
(expect "[1][2]" (seq->string [[1] [2]]))
(expect "true" (seq->string (list (odd? 3))))
(expect "hi" (seq->string "hi"))
(expect "" (seq->string nil))
(expect (more-of x
                4 (count x)
                true (string-contains? x "1")
                true (string-contains? x "2")
                true (string-contains? x "3")
                true (string-contains? x "5"))
        (seq->string #{3 2 1 5}))
(expect (more-of x
                43 (count x)
                true (string-contains? x "[:ten 10]")
                true (string-contains? x "[:twenty-two 22]")
                true (string-contains? x "[:four 4]")
                true (string-contains? x "[:five 5]"))
        (seq->string {:ten 10 :twenty-two 22 :four 4 :five 5}))

;; testing for index-of
(expect 5 (index-of "emmahenryaaronelena" \e 4))
(expect 0 (index-of "emmahenryaaronelena" \e))
(expect -1 (index-of "emmahenryaaronelena" \z))
(expect -1 (index-of "meep" \e 4))
(expect 3 (index-of "meep" \p -3))

;; testing for last-index-of
(expect 16 (last-index-of "emmahenryaaronelena" \e))
(expect 5 (last-index-of "emmahenryaaronelena" \e 7))
(expect 2 (last-index-of "meep" \e 10))
(expect -1 (last-index-of "urgh" \z))
(expect -1 (last-index-of "negative" \z -2))

;; testing for append
(expect "" (append nil))
(expect "" (append ""))
(expect "" (append))
(expect "abc" (append "abc"))
(expect "abcdef" (append "abc" "def"))
(expect "abcdefghi" (append "abc" "def" "ghi"))
(expect "" (append ""))
(expect "abcdef" (append "abc" "def" nil))
(expect "abc123" (append "abc" 123))
(expect "abc1230[1 3 4]*" (append "abc" 123 \0 nil [1 3 4] \*))
(expect "abc1230[[1 2] 3 4]*" (append "abc" 123 \0 nil [[1 2] 3 4] \*))
(expect  "{:one 1, :three 3, :two 2} abc do re mi"(append {:one 1 :two 2 :three 3} \space "abc" \space "do re mi"))
(expect (more-of x
                35 (count x)
                true (string-contains? x "{")
                true (string-contains? x ":a")
                true (string-contains? x ":b")
                true (string-contains? x ":i")
                true (string-contains? x ":f")
                true (string-contains? x ":l")
                true (string-contains? x ":z")
                true (string-contains? x ":e")
                true (string-contains? x "}")
                true (string-contains? x "abc8q[8 5 2]"))
        (append #{:a :b :i :f :l :z :e} "abc" nil \8 \q [8 5 2]))

;; testing for char-at
(expect \p (char-at "happy" 2))
(expect \e (char-at "me" 1))
(expect nil (char-at "touch the sky" -1000))
(expect nil (char-at "touch the sky" 1000))
(expect nil (char-at "" 6))

;; testing for empty-string?
(expect true (empty-string? ""))
(expect false (empty-string? "meep"))

;; testing for first-of-string
(expect \p (first-of-string "principle"))
(expect \l (first-of-string "lazy"))

;; testing for last-of-string
(expect \d (last-of-string "decorated"))
(expect \y (last-of-string "freely"))

;; testing for rest-of-string
(expect "e" (rest-of-string "me"))
(expect "reat Wall of China" (rest-of-string "Great Wall of China"))

;; testing for second-of-string
(expect \a (second-of-string "Aaron"))
(expect \e (second-of-string "testing!"))

;; testing for string-contains?
(expect true (string-contains? "Moonrise" "on"))
(expect false (string-contains? "Moo|nrise" "on"))
(expect true (string-contains? "Moonrise" "M"))
(expect true (string-contains? "moonrise" \m))

;; testing for drop-from-string
(expect "Lake City" (drop-from-string 5 "Salt Lake City"))
(expect "" (drop-from-string 10 "meep"))
(expect "prophet" (drop-from-string -10 "prophet"))

;; testing for take-from-string
(expect "Salt" (take-from-string 4 "Salt Lake City"))
(expect "meep" (take-from-string 10 "meep"))
(expect "" (take-from-string -10 "prophet"))

;###############################################
;### Testing Types for the better string fns ###
;###############################################

;; type-checking for seq->string - should return a string
(expect (string? (seq->string '(\h \e \l \l \o \space \w \o \r \l \d))))
(expect (string? (seq->string '())))
(expect (string? (seq->string [1 2 3])))
(expect (string? (seq->string [[1] [2]])))
(expect (string? (seq->string (list (odd? 3)))))
(expect (string? (seq->string "hi")))
(expect (string? (seq->string nil)))

;; type-checking for index-of - should return a number
(expect (number? (index-of "emmahenryaaronelena" \e 4)))
(expect (number? (index-of "emmahenryaaronelena" \e)))
(expect (number? (index-of "emmahenryaaronelena" \z)))
(expect (number? (index-of "meep" \e 4)))
(expect (number? (index-of "meep" \p -3)))

;; type-checking for last-index-of - should return a number
(expect (number? (last-index-of "emmahenryaaronelena" \e)))
(expect (number? (last-index-of "emmahenryaaronelena" \e 7)))
(expect (number? (last-index-of "meep" \e 10)))
(expect (number? (last-index-of "urgh" \z)))
(expect (number? (last-index-of "negative" \z -2)))

;; type-checking for append - should return a string
(expect (string? (append nil)))
(expect (string? (append "")))
(expect (string? (append)))
(expect (string? (append "abc")))
(expect (string? (append "abc" "def")))
(expect (string? (append "abc" "def" "ghi")))
(expect (string? (append "")))
(expect (string? (append "abc" "def" nil)))
(expect (string? (append "abc" 123)))
(expect (string? (append "abc" 123 \0 nil [1 3 4])))

;; type-checking for char-at - should return either a character or nil
(expect (#(or (char? %) (nil? %)) (char-at "happy" 2)))
(expect (#(or (char? %) (nil? %)) (char-at "me" 1)))
(expect (#(or (char? %) (nil? %)) (char-at "touch the sky" -1000)))
(expect (#(or (char? %) (nil? %)) (char-at "touch the sky" 1000)))
(expect (#(or (char? %) (nil? %)) (char-at "" 6)))

;; type-checking for empty-string? - should return either true or false
(expect (#(or (true? %) (false? %)) (empty-string? "")))
(expect (#(or (true? %) (false? %)) (empty-string? "meep")))

;; type-checking for first-of-string - should return a character
(expect (char? (first-of-string "principle")))
(expect (char? (first-of-string "lazy")))

;; type-checking for last-of-string - should return a character
(expect (char? (last-of-string "decorated")))
(expect (char? (last-of-string "freely")))

;; type-checking for rest-of-string - should return a string
(expect (string? (rest-of-string "me")))
(expect (string? (rest-of-string "Great Wall of China")))

;; type-checking for second-of-string - should return a character
(expect (char? (second-of-string "Aaron")))
(expect (char? (second-of-string "testing!")))

;; type-checking for string-contains? - should return either true or false
(expect (#(or (true? %) (false? %)) (string-contains? "Moonrise" "on")))
(expect (#(or (true? %) (false? %)) (string-contains? "Moo|nrise" "on")))
(expect (#(or (true? %) (false? %)) (string-contains? "Moonrise" "M")))
(expect (#(or (true? %) (false? %)) (string-contains? "moonrise" \m)))

;; type-checking for drop-from-string - should return a string
(expect (string? (drop-from-string 5 "Salt Lake City")))
(expect (string? (drop-from-string 10 "meep")))
(expect (string? (drop-from-string -10 "prophet")))

;; type-checking for take-from-string - should return a string
(expect (string? (take-from-string 4 "Salt Lake City")))
(expect (string? (take-from-string 10 "meep")))
(expect (string? (take-from-string -10 "prophet")))

;##########################################################
;### Testing Assertion Errors for the better string fns ###
;##########################################################

;; assert-checking for seq->string
(expect "in function seq->string first argument :not-a-sequence must be a sequence but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings '(seq->string :not-a-sequence))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; assert-checking for index-of, first precondition when passed 2 args
(expect "in function index-of first argument :not-a-string must be a string but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings
                                             '(index-of :not-a-string \e))))

;; assert-checking for index-of, second precondition when passed 2 args
(expect "in function index-of second argument :not-a-character must be a character but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings
                                             '(index-of "emma" :not-a-character))))

;; assert-checking for index-of, first precondition when passed 3 args
(expect "in function index-of first argument :not-a-string must be a string but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings
                                             '(index-of :not-a-string \e 2))))

;; assert-checking for index-of, second precondition when passed 3 args
(expect "in function index-of second argument :not-a-character must be a character but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings
                                             '(index-of "emma" :not-a-character 5))))

;; assert-checking for index-of, third precondition when passed 3 args
(expect "in function index-of third argument :not-a-number must be a number but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings
                                             '(index-of "emma" \e :not-a-number))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; assert-checking for last-index-of, first precondition when passed 2 args
(expect "in function last-index-of first argument :not-a-string must be a string but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings
                                             '(last-index-of :not-a-string \e))))

;; assert-checking for last-index-of, second precondition when passed 2 args
(expect "in function last-index-of second argument :not-a-character must be a character but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings
                                             '(last-index-of "emma" :not-a-character))))

;; assert-checking for last-index-of, first precondition when passed 3 args
(expect "in function last-index-of first argument :not-a-string must be a string but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings
                                             '(last-index-of :not-a-string \e 2))))

;; assert-checking for last-index-of, second precondition when passed 3 args
(expect "in function last-index-of second argument :not-a-character must be a character but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings
                                             '(last-index-of "emma" :not-a-character 5))))

;; assert-checking for last-index-of, third precondition when passed 3 args
(expect "in function last-index-of third argument :not-a-number must be a number but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings
                                             '(last-index-of "emma" \e :not-a-number))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

; nothing will throw an assertion error for append because append can take anything

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; assert-checking for char-at, first precondition
(expect "in function char-at first argument :not-a-string must be a string but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings
                                             '(char-at :not-a-string 0))))

;; assert-checking for char-at, second precondition
(expect "in function char-at second argument :not-a-number must be a number but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings
                                             '(char-at "hello world" :not-a-number))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; assert-checking for empty-string?
(expect "in function empty-string? first argument :not-a-string must be a string but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings
                                             '(empty-string? :not-a-string))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; assert-checking for first-of-string
(expect "in function first-of-string first argument :not-a-string must be a string but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings
                                             '(first-of-string :not-a-string))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; assert-checking for last-of-string
(expect "in function last-of-string first argument :not-a-string must be a string but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings
                                             '(last-of-string :not-a-string))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; assert-checking for rest-of-string
(expect "in function rest-of-string first argument :not-a-string must be a string but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings
                                             '(rest-of-string :not-a-string))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; assert-checking for second-of-string
(expect "in function second-of-string first argument :not-a-string must be a string but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings
                                             '(second-of-string :not-a-string))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; assert-checking for string-contains?, first precondition
(expect "in function string-contains? first argument :not-a-string must be a string but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings
                                             '(string-contains? :not-a-string "hello world"))))

;; assert-checking for string-contains?, second precondition
(expect "in function string-contains? second argument :not-a-string-or-character must be either a string or character but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings
                                             '(string-contains? "hello world" :not-a-string-or-character))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; assert-checking for drop-from-string, first precondition
(expect "in function drop-from-string first argument :not-a-number must be a number but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings
                                             '(drop-from-string :not-a-number "hello world"))))

;; assert-checking for drop-from-string, second precondition
(expect "in function drop-from-string second argument :not-a-string must be a string but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings
                                             '(drop-from-string 2 :not-a-string))))

;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; assert-checking for take-from-string, first precondition
(expect "in function take-from-string first argument :not-a-number must be a number but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings
                                             '(take-from-string :not-a-number "hello world"))))

;; assert-checking for take-from-string, second precondition
(expect "in function take-from-string second argument :not-a-string must be a string but is a keyword"
        (get-all-text
         (run-and-catch-pretty-no-stacktrace 'strings.strings
                                             '(take-from-string 2 :not-a-string))))


;###############################################
;### Other testing for the better string fns ###
;###############################################

;; using the functions in combinations with each other
(expect \a (first-of-string (rest-of-string (take-from-string 8 "Salt Lake City"))))
(expect "s" (seq->string (list (char-at (drop-from-string 12 (clojure.string/reverse (append
  "The chivalrous knight " "was super brave, " "although that's pretty expected."))) 5))))
(expect "oo" (seq->string (list (last-of-string "hello") (second-of-string "world"))))
(expect [1 9] (vector (index-of "hello world" \e) (last-index-of "hello world" \l)))
(expect false (and (empty-string? "") (string-contains? "hello world" \z)))
(expect true (or (empty-string? "") (string-contains? "hello world" \z)))

;; Super easy to use multiple functions at the same time, you just need to watch
;; the type of thing it returns - sometimes this might involve making a list, vector, etc.

;; non-ASCII tests
(expect "弟子規裡有一個字應該更正首孝悌的「悌」應該是「弟」" (seq->string '(弟子規裡有一個字應該更正首孝悌的「悌」應該是「弟」)))
(expect 2 (index-of "弟子規裡有一個字應該更正首孝悌的「悌」應該是「弟」" \規))
(expect 21 (last-index-of "弟子規裡有一個字應該更正首孝悌的「悌」應該規是「弟」" \規 82))
(expect "弟子規裡有一個字應該更正首孝悌的「悌」應該規是「弟」" (append "弟子規裡有一個字應該更" "正首孝悌的「悌」應該規是「弟」"))
(expect \「 (char-at "正首孝悌的「悌」應該規是「弟」" 5))
(expect false (empty-string? "「"))
(expect \正 (first-of-string "正首孝悌的「悌」應該規是「弟」"))
(expect \」 (last-of-string "正首孝悌的「悌」應該規是「弟」"))
(expect "首孝悌的「悌」應該規是「弟」" (rest-of-string "正首孝悌的「悌」應該規是「弟」"))
(expect \首 (second-of-string "正首孝悌的「悌」應該規是「弟」"))
(expect true (string-contains? "正首孝悌的「悌」應該規是「弟」" \規))
(expect false (string-contains? "正首孝悌的「悌」應該規是「弟」" \子))
(expect "正首孝" (take-from-string 3 "正首孝悌的「悌」應該規是「弟」"))
(expect "悌的「悌」應該規是「弟」" (drop-from-string 3 "正首孝悌的「悌」應該規是「弟」"))

;; If LightTable understands the non-ASCII word, then the functions will work with them.

;#########################################################################
;### these are all functions that are usually used with clojure.string ###
;#########################################################################

; testing for reverse-string
;(expect "amme" (reverse-string "emma"))
;(expect "eromhsuR tnuoM" (reverse-string "Mount Rushmore"))

; testing for to-upper-case
;(expect "HELLO WORLD" (to-upper-case "Hello World"))
;(expect "HIIIIYAH!" (to-upper-case "hiiIiyAh!"))

; testing for to-lower-case
;(expect "hello world" (to-lower-case "Hello World"))
;(expect "statue of liberty" (to-lower-case "StatUe oF LiBeRty"))

; testing for capitalize
;(expect "Hello" (capitalize "hEllO"))
;(expect "Eiffel tower" (capitalize "EiFfeL TowEr"))

; testing for separate-string
;(expect "mesh" (separate-string "mesh"))
;(expect "h.e.l.l.o. .w.o.r.l.d" (separate-string "." "hello world"))

; testing for replace-string
;(expect "The color is blue." (replace-string "The color is red." "red" "blue"))
;(expect "argh" (replace-string "urgh" "u" "a"))

; testing for split-string
;(expect ["q" "w" "e" "r" "t" "y" "u" "i" "o" "p"] (split-string
;  "q1w2e3r4t5y6u7i8o9p0" #"\d+"))
;(expect ["q" "w" "e" "r" "t5y6u7i8o9p0"] (split-string "q1w2e3r4t5y6u7i8o9p0" #"\d+" 5))

; testing for split-lines-string
;(expect ["test " " string"] (split-lines-string "test \n string"))

; testing for replace-first-of-string
;(expect "hello" (replace-first-of-string "hello" "z" "p"))
;(expect "heylo" (replace-first-of-string "hello" "l" "y"))

; testing for trim-string
;(expect "k" (trim-string "     k   "))
;(expect "" (trim-string "   "))
;(expect "d   s  i" (trim-string "d   s  i   "))

; testing for triml-string
;(expect "k   " (triml-string "     k   "))
;(expect "" (triml-string "   "))
;(expect "d   s  i   " (triml-string "d   s  i   "))

; testing for trimr-string
;(expect "     k" (trimr-string "     k   "))
;(expect "" (trimr-string "   "))
;(expect "d   s  i" (trimr-string "d   s  i   "))

; testing for trim-newline-string
;(expect "testing " (trim-newline-string "testing \n"))
;(expect "testing again..." (trim-newline-string "testing again...\r"))

; testing for string-blank?
;(expect true (string-blank? nil))
;(expect true (string-blank? false))
;(expect true (string-blank? "   "))
;(expect false (string-blank? "  a "))
