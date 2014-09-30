(ns utilities.stacktrace_functions_test
  (:require [expectations :refer :all]
            [errors.prettify_exception :refer :all]
            [utilities.stacktrace_functions :refer :all]))

;###################
;### Stacktraces ###
;###################

(def complete-stack
  [{:anon-fn false, :fn "map", :ns "corefns.corefns", :clojure true, :file "corefns.clj", :line 34}
   {:method "invoke", :class clojure.lang.RestFn, :java true, :file "RestFn.java", :line 423}
   {:anon-fn false, :fn "eval6415", :ns "intro.core", :clojure true, :file "NO_SOURCE_FILE", :line 302}
   {:method "eval", :class clojure.lang.Compiler, :java true, :file "Compiler.java", :line 6703}
   {:method "eval", :class clojure.lang.Compiler, :java true, :file "Compiler.java", :line 6666}
   {:anon-fn false, :fn "eval", :ns "clojure.core", :clojure true, :file "core.clj", :line 2927}
   {:anon-fn false, :fn "test-and-continue", :ns "intro.core", :clojure true, :file "core.clj", :line 22}
   {:anon-fn true, :fn "map", :ns "clojure.core", :clojure true, :file "core.clj", :line 2559}
   {:method "sval", :class clojure.lang.LazySeq, :java true, :file "LazySeq.java", :line 40}
   {:method "seq", :class clojure.lang.LazySeq, :java true, :file "LazySeq.java", :line 49}
   {:method "seq", :class clojure.lang.RT, :java true, :file "RT.java", :line 484}
   {:anon-fn false, :fn "seq", :ns "clojure.core", :clojure true, :file "core.clj", :line 133}
   {:anon-fn false, :fn "dorun", :ns "clojure.core", :clojure true, :file "core.clj", :line 2855}
   {:anon-fn false, :fn "doall", :ns "clojure.core", :clojure true, :file "core.clj", :line 2871}
   {:anon-fn false, :fn "test-all-and-continue", :ns "intro.core", :clojure true, :file "core.clj", :line 28}
   {:anon-fn false, :fn "test-asserts", :ns "intro.core", :clojure true, :file "core.clj", :line 301}
   {:anon-fn false, :fn "-main", :ns "intro.core", :clojure true, :file "core.clj", :line 599}
   {:method "invoke", :class clojure.lang.RestFn, :java true, :file "RestFn.java", :line 397}
   {:method "invoke", :class clojure.lang.Var, :java true, :file "Var.java", :line 375}
   {:anon-fn false, :fn "eval6408", :ns "user", :clojure true, :file "NO_SOURCE_FILE", :line 1}
   {:method "eval", :class clojure.lang.Compiler, :java true, :file "Compiler.java", :line 6703}
   {:method "eval", :class clojure.lang.Compiler, :java true, :file "Compiler.java", :line 6693}
   {:method "eval", :class clojure.lang.Compiler, :java true, :file "Compiler.java", :line 6666}
   {:anon-fn false, :fn "eval", :ns "clojure.core", :clojure true, :file "core.clj", :line 2927}
   {:anon-fn false, :fn "eval-opt", :ns "clojure.main", :clojure true, :file "main.clj", :line 288}
   {:anon-fn false, :fn "initialize", :ns "clojure.main", :clojure true, :file "main.clj", :line 307}
   {:anon-fn false, :fn "null-opt", :ns "clojure.main", :clojure true, :file "main.clj", :line 342}
   {:anon-fn false, :fn "main", :ns "clojure.main", :clojure true, :file "main.clj", :line 420}
   {:method "invoke", :class clojure.lang.RestFn, :java true, :file "RestFn.java", :line 421}
   {:method "invoke", :class clojure.lang.Var, :java true, :file "Var.java", :line 383}
   {:method "applyToHelper", :class clojure.lang.AFn, :java true, :file "AFn.java", :line 156}
   {:method "applyTo", :class clojure.lang.Var, :java true, :file "Var.java", :line 700}
   {:method "main", :class clojure.main, :java true, :file "main.java", :line 37}])

(def filtered-stack
  [{:anon-fn false, :fn "map", :ns "corefns.corefns", :clojure true, :file "corefns.clj", :line 34}
   {:anon-fn false, :fn "eval6415", :ns "intro.core", :clojure true, :file "NO_SOURCE_FILE", :line 302}
   {:anon-fn false, :fn "eval", :ns "clojure.core", :clojure true, :file "core.clj", :line 2927}
   {:anon-fn false, :fn "test-and-continue", :ns "intro.core", :clojure true, :file "core.clj", :line 22}
   {:anon-fn true, :fn "map", :ns "clojure.core", :clojure true, :file "core.clj", :line 2559}
   {:anon-fn false, :fn "seq", :ns "clojure.core", :clojure true, :file "core.clj", :line 133}
   {:anon-fn false, :fn "dorun", :ns "clojure.core", :clojure true, :file "core.clj", :line 2855}
   {:anon-fn false, :fn "doall", :ns "clojure.core", :clojure true, :file "core.clj", :line 2871}
   {:anon-fn false, :fn "test-all-and-continue", :ns "intro.core", :clojure true, :file "core.clj", :line 28}
   {:anon-fn false, :fn "test-asserts", :ns "intro.core", :clojure true, :file "core.clj", :line 301}
   {:anon-fn false, :fn "-main", :ns "intro.core", :clojure true, :file "core.clj", :line 599}
   ;{:anon-fn false, :fn "eval6408", :ns "user", :clojure true, :file "NO_SOURCE_FILE", :line 1}
   {:anon-fn false, :fn "eval", :ns "clojure.core", :clojure true, :file "core.clj", :line 2927}])

(def complete-stack2
  [{:method "add", :class "clojure.lang.Numbers", :java true, :file "Numbers.java", :line 126}
   {:method "add", :class "clojure.lang.Numbers", :java true, :file "Numbers.java", :line 3523}
   {:anon-fn false, :fn "eval9481", :ns "experimental.core-test", :clojure true, :file "core_test.clj", :line 57}
   {:method "eval", :class "clojure.lang.Compiler", :java true, :file "Compiler.java", :line 6619}
   {:method "eval", :class "clojure.lang.Compiler", :java true, :file "Compiler.java", :line 6582}
   {:anon-fn false, :fn "eval", :ns "clojure.core", :clojure true, :file "core.clj", :line 2852}
   {:anon-fn false, :fn "run-and-catch-raw", :ns "experimental.core-test", :clojure true, :file "core_test.clj", :line 38}
   {:method "applyToHelper", :class "clojure.lang.AFn", :java true, :file "AFn.java", :line 161}
   {:method "applyTo", :class "clojure.lang.AFn", :java true, :file "AFn.java", :line 151}
   {:method "eval", :class "clojure.lang.Compiler$InvokeExpr", :java true, :file "Compiler.java", :line 3458}
   {:method "eval", :class "clojure.lang.Compiler$DefExpr", :java true, :file "Compiler.java", :line 408}
   {:method "eval", :class "clojure.lang.Compiler", :java true, :file "Compiler.java", :line 6624}
   {:method "load", :class "clojure.lang.Compiler", :java true, :file "Compiler.java", :line 7064}
   {:method "loadResourceScript", :class "clojure.lang.RT", :java true, :file "RT.java", :line 370}
   {:method "loadResourceScript", :class "clojure.lang.RT", :java true, :file "RT.java", :line 361}
   {:method "load", :class "clojure.lang.RT", :java true, :file "RT.java", :line 440}
   {:method "load", :class "clojure.lang.RT", :java true, :file "RT.java", :line 411}
   {:anon-fn true, :fn "load", :ns "clojure.core", :clojure true, :file "core.clj", :line 5530}
   {:anon-fn false, :fn "load", :ns "clojure.core", :clojure true, :file "core.clj", :line 5529}
   {:method "invoke", :class "clojure.lang.RestFn", :java true, :file "RestFn.java", :line 408}
   {:anon-fn false, :fn "load-one", :ns "clojure.core", :clojure true, :file "core.clj", :line 5336}
   {:anon-fn true, :fn "load-lib", :ns "clojure.core", :clojure true, :file "core.clj", :line 5375}
   {:anon-fn false, :fn "load-lib", :ns "clojure.core", :clojure true, :file "core.clj", :line 5374}
   {:method "applyTo", :class "clojure.lang.RestFn", :java true, :file "RestFn.java", :line 142}
   {:anon-fn false, :fn "apply", :ns "clojure.core", :clojure true, :file "core.clj", :line 619}
   {:anon-fn false, :fn  "load-libs", :ns "clojure.core", :clojure true, :file "core.clj", :line 5413}
   {:method "applyTo", :class "clojure.lang.RestFn", :java true, :file "RestFn.java", :line 137}
   {:anon-fn false, :fn "apply", :ns "clojure.core", :clojure true, :file "core.clj", :line 619}
   {:anon-fn false, :fn "require", :ns "clojure.core", :clojure true, :file "core.clj", :line 5496}
   {:method "invoke", :class "clojure.lang.RestFn", :java true, :file "RestFn.java", :line 421}
   {:anon-fn false, :fn "track-reload-one", :ns "clojure.tools.namespace.reload", :clojure true, :file "reload.clj", :line 35}
   {:anon-fn false, :fn "track-reload", :ns "clojure.tools.namespace.reload", :clojure true, :file "reload.clj", :line 52}
   {:method "applyToHelper", :class "clojure.lang.AFn", :java true, :file "AFn.java", :line 161}
   {:method "applyTo", :class "clojure.lang.AFn", :java true, :file "AFn.java", :line 151}
   {:method "alterRoot", :class "clojure.lang.Var", :java true, :file "Var.java", :line 336}
   {:anon-fn false, :fn "alter-var-root", :ns "clojure.core", :clojure true, :file "core.clj", :line 4946}
   {:method "invoke", :class "clojure.lang.RestFn", :java true, :file "RestFn.java", :line 425}
   {:anon-fn false, :fn "do-refresh", :ns "clojure.tools.namespace.repl", :clojure true, :file "repl.clj", :line 94}
   {:anon-fn false, :fn "refresh", :ns "clojure.tools.namespace.repl", :clojure true, :file "repl.clj", :line 142}
   {:method "invoke", :class "clojure.lang.RestFn", :java true, :file "RestFn.java", :line 397}
   {:anon-fn false, :fn "refresh-environment", :ns "autoexpect.runner", :clojure true, :file "runner.clj", :line 23}
   {:anon-fn true, :fn "run-tests", :ns "autoexpect.runner", :clojure true, :file "runner.clj", :line 50}
   {:anon-fn false, :fn "run-tests", :ns "autoexpect.runner", :clojure true, :file "runner.clj", :line 50}
   {:anon-fn true, :fn "monitor-project", :ns "autoexpect.runner", :clojure true, :file "runner.clj", :line 69}
   {:anon-fn true, :fn "monitor-project", :ns "autoexpect.runner", :clojure true, :file "runner.clj", :line 68}
   {:anon-fn false, :fn "monitor-project", :ns "autoexpect.runner", :clojure true, :file "runner.clj", :line 66}
   {:anon-fn false, :fn "eval1187", :ns "user", :clojure true, :file "form-init6834699387848419871.clj", :line 1}
   {:method "eval", :class "clojure.lang.Compiler", :java true, :file "Compiler.java", :line 6619}
   {:method "eval", :class "clojure.lang.Compiler", :java true, :file "Compiler.java", :line 6609}
   {:method "load", :class "clojure.lang.Compiler", :java true, :file "Compiler.java", :line 7064}
   {:method "loadFile", :class "clojure.lang.Compiler", :java true, :file "Compiler.java", :line 7020}
   {:anon-fn false, :fn "load-script", :ns "clojure.main", :clojure true, :file "main.clj", :line 294}
   {:anon-fn false, :fn "init-opt", :ns "clojure.main", :clojure true, :file "main.clj", :line 299}
   {:anon-fn false, :fn "initialize", :ns "clojure.main", :clojure true, :file "main.clj", :line 327}
   {:anon-fn false, :fn "null-opt", :ns "clojure.main", :clojure true, :file "main.clj", :line 362}
   {:anon-fn false, :fn "main", :ns "clojure.main", :clojure true, :file "main.clj", :line 440}
   {:method "invoke", :class "clojure.lang.RestFn", :java true, :file "RestFn.java", :line 421}
   {:method "invoke", :class "clojure.lang.Var", :java true, :file "Var.java", :line 419}
   {:method "applyToHelper", :class "clojure.lang.AFn", :java true, :file "AFn.java", :line 163}
   {:method "applyTo", :class "clojure.lang.Var", :java true, :file "Var.java", :line 532}
   {:method "main", :class "clojure.main", :java true, :file "main.java", :line 37}])

(def filtered-stack2
  [{:anon-fn false, :fn "eval9481", :ns "experimental.core-test", :clojure true, :file "core_test.clj", :line 57}
   {:anon-fn false, :fn "eval", :ns "clojure.core", :clojure true, :file "core.clj", :line 2852}
   {:anon-fn false, :fn "run-and-catch-raw", :ns "experimental.core-test", :clojure true, :file "core_test.clj", :line 38}
   {:anon-fn false, :fn "apply", :ns "clojure.core", :clojure true, :file "core.clj", :line 619}
   {:anon-fn false, :fn "apply", :ns "clojure.core", :clojure true, :file "core.clj", :line 619}])


;##############################################
;### Testing individual stacktrace elements ###
;##############################################

(expect not-true? (keep-stack-trace-elem {:method "add", :class "clojure.lang.Numbers",
				                                  :java true, :file "Numbers.java", :line 126}))

(expect not-true? (keep-stack-trace-elem {:method "eval", :class "clojure.lang.Compiler",
				                                  :java true, :file "Compiler.java", :line 6619}))

; "load" in clojure.core is explicitly ignored
(expect not-true? (keep-stack-trace-elem {:anon-fn true, :fn "load", :ns "clojure.core",
                                      :clojure true, :file "core.clj", :line 5530}))

(expect not-true? (keep-stack-trace-elem {:anon-fn false, :fn "track-reload", :ns "clojure.tools.namespace.reload",
                                          :clojure true, :file "reload.clj", :line 52}))

(expect not-true? (keep-stack-trace-elem {:method "main", :class "clojure.main", :java true,
                                          :file "main.java", :line 37}))

;; Artificial example (in reality :java true) to test filtering
;; Note: this returns true since "clojure.main" is a class, not a namespace
(expect true? (keep-stack-trace-elem {:method "main", :class "clojure.main",
				                              :clojure true, :file "main.java", :line 37}))

(expect not-true? (keep-stack-trace-elem {:method "main", :ns "clojure.main",
					                                :clojure true, :file "main.java", :line 37}))

(expect not-true? (keep-stack-trace-elem {:anon-fn false, :fn "main", :ns "clojure.main",
                                          :clojure true, :file "main.clj", :line 420}))

(expect not-true? (keep-stack-trace-elem {:anon-fn false, :fn "main", :ns "clojure.lang",
                                          :clojure true, :file "main.clj", :line 420}))

;###########################################################
;### Tests for functions that check filtered stacktraces ###
;###########################################################

;; testing for filter-stacktrace
(expect filtered-stack (filter-stacktrace complete-stack))
(expect filtered-stack2 (filter-stacktrace complete-stack2))
(expect (check-stack-count? 5) (filter-stacktrace complete-stack2))
(expect {:anon-fn true, :fn "map", :ns "clojure.core", :clojure true, :file "core.clj", :line 2559}
	(in (filter-stacktrace complete-stack)))

;######################################
;### Tests for stacktrace functions ###
;######################################

;; testing for helper-trace-elem-has-function?
(expect true (helper-trace-elem-has-function? "map" {:fn "map" :ns "corefns.corefns"}))
(expect false (helper-trace-elem-has-function? "soup" {:fn "map" :ns "corefns.corefns"}))

;; testing for trace-elem-has-function?
(expect (trace-elem-has-function? "map") (in filtered-stack))
(expect true ((trace-elem-has-function? "map") {:fn "map" :ns "corefns.corefns"}))
(expect false ((trace-elem-has-function? "donkey") {:fn "map" :ns "corefns.corefns"}))

;; testing for helper-trace-elem-has-namespace?
(expect true (helper-trace-elem-has-namespace? "corefns.corefns" {:fn "map" :ns "corefns.corefns"}))
(expect false (helper-trace-elem-has-namespace? "soup" {:fn "map" :ns "corefns.corefns"}))

;; testing for trace-elem-has-namespace?
(expect (trace-elem-has-namespace? "corefns.corefns") (in filtered-stack))
(expect true ((trace-elem-has-namespace? "corefns.corefns") {:fn "map" :ns "corefns.corefns"}))
(expect false ((trace-elem-has-namespace? "donkey") {:fn "map" :ns "corefns.corefns"}))

;; testing for helper-trace-elem-has-pair?
(expect true (helper-trace-elem-has-pair? :ns "corefns.corefns" {:fn "map" :ns "corefns.corefns"}))
(expect false (helper-trace-elem-has-pair? :emma "lemmon" {:fn "map" :ns "corefns.corefns"}))

;; testing for trace-elem-has-pair?
(expect (trace-elem-has-pair? :fn "-main") (in filtered-stack))
(expect true ((trace-elem-has-pair? :ns "corefns.corefns") {:fn "map" :ns "corefns.corefns"}))
(expect false ((trace-elem-has-pair? :emma "lemmon") {:fn "map" :ns "corefns.corefns"}))

;; testing for helper-trace-elem-has-all-pairs?
(expect true (helper-trace-elem-has-all-pairs? {:ns "corefns.corefns", :fn "map"} {:fn "map" :ns "corefns.corefns"}))
(expect false (helper-trace-elem-has-all-pairs? {:emma "lemmon", :not "pass"} {:fn "map" :ns "corefns.corefns"}))

;; testing for trace-elem-has-all-pairs?
(expect (trace-elem-has-all-pairs? {:fn "test-and-continue", :ns "intro.core"}) (in filtered-stack))
(expect true ((trace-elem-has-all-pairs? {:ns "corefns.corefns" :fn "map"}) {:fn "map" :ns "corefns.corefns"}))
(expect false ((trace-elem-has-all-pairs? {:emma "lemmon" :not "pass"}) {:fn "map" :ns "corefns.corefns"}))

;; testing for helper-trace-elem-doesnt-have-function?
(expect false (helper-trace-elem-doesnt-have-function? "map" {:fn "map" :ns "corefns.corefns"}))
(expect true (helper-trace-elem-doesnt-have-function? "soup" {:fn "map" :ns "corefns.corefns"}))

;; testing for trace-elem-doesnt-have-function?
(expect (trace-elem-doesnt-have-function? "smooth") (in filtered-stack))
(expect false ((trace-elem-doesnt-have-function? "map") {:fn "map" :ns "corefns.corefns"}))
(expect true ((trace-elem-doesnt-have-function? "donkey") {:fn "map" :ns "corefns.corefns"}))

;; testing for helper-trace-elem-doesnt-have-namespace?
(expect false (helper-trace-elem-doesnt-have-namespace? "corefns.corefns" {:fn "map" :ns "corefns.corefns"}))
(expect true (helper-trace-elem-doesnt-have-namespace? "soup" {:fn "map" :ns "corefns.corefns"}))

;; testing for trace-elem-doesnt-have-namespace?
(expect (trace-elem-doesnt-have-namespace? "lemmon.emma") (in filtered-stack))
(expect false ((trace-elem-doesnt-have-namespace? "corefns.corefns") {:fn "map" :ns "corefns.corefns"}))
(expect true ((trace-elem-doesnt-have-namespace? "donkey") {:fn "map" :ns "corefns.corefns"}))

;; testing for helper-trace-elem-doesnt-have-pair?
(expect false (helper-trace-elem-doesnt-have-pair? :ns "corefns.corefns" {:fn "map" :ns "corefns.corefns"}))
(expect true (helper-trace-elem-doesnt-have-pair? :emma "lemmon" {:fn "map" :ns "corefns.corefns"}))

;; testing for trace-elem-doesnt-have-pair?
(expect (trace-elem-doesnt-have-pair? :fn "hippo") (in filtered-stack))
(expect false ((trace-elem-doesnt-have-pair? :ns "corefns.corefns") {:fn "map" :ns "corefns.corefns"}))
(expect true ((trace-elem-doesnt-have-pair? :emma "lemmon") {:fn "map" :ns "corefns.corefns"}))

;; testing for helper-trace-elem-doesnt-have-all-pairs?
(expect false (helper-trace-elem-doesnt-have-all-pairs? {:ns "corefns.corefns", :fn "map"} {:fn "map" :ns "corefns.corefns"}))
(expect true (helper-trace-elem-doesnt-have-all-pairs? {:emma "lemmon", :not "pass"} {:fn "map" :ns "corefns.corefns"}))

;; testing for trace-elem-doesnt-have-all-pairs?
(expect (trace-elem-doesnt-have-all-pairs? {:fn "test-and-continue", :ns "rhino.squirrel"}) (in filtered-stack))
(expect false ((trace-elem-doesnt-have-all-pairs? {:ns "corefns.corefns" :fn "map"}) {:fn "map" :ns "corefns.corefns"}))
(expect true ((trace-elem-doesnt-have-all-pairs? {:emma "lemmon" :not "pass"}) {:fn "map" :ns "corefns.corefns"}))

;; testing for helper-stack-count?
(expect (helper-stack-count? 2 [:z :q]))
(expect (helper-stack-count? 0 []))

;; testing for check-stack-count?
(expect (check-stack-count? 3) [3 6 7])
(expect (check-stack-count? 0) [])
(expect (check-stack-count? 12) (filter-stacktrace complete-stack))


;; testing for helper-trace-has-function?
(expect true (helper-trace-has-function? "map" filtered-stack))
(expect true (helper-trace-has-function? "doall" filtered-stack))
(expect false (helper-trace-has-function? "soup" filtered-stack))
(expect false (helper-trace-has-function? "map" []))

;; testing for trace-has-function?
(expect (trace-has-function? "map") filtered-stack)
(expect true ((trace-has-function? "map") [{:fn "map" :ns "corefns.corefns"}
                                           {:something "else" :emma "lemmon"}]))
(expect false ((trace-has-function? "donkey") [{:fn "map" :ns "corefns.corefns"}
                                               {:something "else" :emma "lemmon"}]))

;; testing for helper-trace-has-namespace?
(expect true (helper-trace-has-namespace? "corefns.corefns" filtered-stack))
(expect true (helper-trace-has-namespace? "clojure.core" filtered-stack))
(expect false (helper-trace-has-namespace? "soup" filtered-stack))
(expect false (helper-trace-has-namespace? "map" []))

;; testing for trace-has-namespace?
(expect (trace-has-namespace? "intro.core") filtered-stack)
(expect true ((trace-has-namespace? "corefns.corefns") [{:fn "map" :ns "corefns.corefns"}
                                                        {:something "else" :emma "lemmon"}]))
(expect false ((trace-has-namespace? "donkey") [{:fn "map" :ns "corefns.corefns"}
                                                {:something "else" :emma "lemmon"}]))

;; testing for helper-trace-has-pair?
(expect true (helper-trace-has-pair? :fn "map" filtered-stack))
(expect true (helper-trace-has-pair? :fn "doall" filtered-stack))
(expect false (helper-trace-has-pair? :fn "soup" filtered-stack))
(expect false (helper-trace-has-pair? :fn "map" []))

;; testing for trace-has-pair?
(expect (trace-has-pair? :fn "map") filtered-stack)
(expect true ((trace-has-pair? :something "else") [{:fn "map" :ns "corefns.corefns"}
                                                   {:something "else" :emma "lemmon"}]))
(expect false ((trace-has-pair? :shark "donkey") [{:fn "map" :ns "corefns.corefns"}
                                                  {:something "else" :emma "lemmon"}]))

;; testing for helper-trace-has-pair?
(expect true (helper-trace-has-pair? :fn "map" filtered-stack))
(expect true (helper-trace-has-pair? :fn "doall" filtered-stack))
(expect false (helper-trace-has-pair? :fn "soup" filtered-stack))
(expect false (helper-trace-has-pair? :fn "map" []))

;; testing for trace-has-pair?
(expect (trace-has-pair? :fn "map") filtered-stack)
(expect true ((trace-has-pair? :something "else") [{:fn "map" :ns "corefns.corefns"}
                                                   {:something "else" :emma "lemmon"}]))
(expect false ((trace-has-pair? :shark "donkey") [{:fn "map" :ns "corefns.corefns"}
                                                  {:something "else" :emma "lemmon"}]))

;; testing for helper-trace-has-all-pairs?
(expect true (helper-trace-has-all-pairs? {:anon-fn false, :fn "seq"} filtered-stack))
(expect false (helper-trace-has-all-pairs? {:fn "doall" :foo "bar"} filtered-stack))
(expect false (helper-trace-has-all-pairs? {:fn "doall" :anon-fn true} filtered-stack))
(expect false (helper-trace-has-all-pairs? {:fn "soup" :despicable-me "minions"} filtered-stack))
(expect false (helper-trace-has-all-pairs? {:fn "map" :loser "winner"} []))

;; testing for trace-has-all-pairs?
(expect (trace-has-all-pairs? {:fn "map" :clojure true}) filtered-stack)
(expect true ((trace-has-all-pairs? {:something "else" :emma "lemmon"}) [{:fn "map" :ns "corefns.corefns"}
                                                                         {:something "else" :emma "lemmon"}]))
(expect false ((trace-has-all-pairs? {:shark "donkey" :porpoise "laughter"}) [{:fn "map" :ns "corefns.corefns"}
                                                                              {:something "else" :emma "lemmon"}]))
(expect false ((trace-has-all-pairs? {:shark "donkey" :ns "corefns.corefns"}) [{:fn "map" :ns "corefns.corefns"}
                                                                               {:something "else" :emma "lemmon"}]))
(expect false ((trace-has-all-pairs? {:something "else" :ns "corefns.corefns"}) [{:fn "map" :ns "corefns.corefns"}
                                                                                 {:something "else" :emma "lemmon"}]))

;; testing for helper-top-elem-has-pair?
(expect true (helper-top-elem-has-pair? :fn "map" filtered-stack))
(expect false (helper-top-elem-has-pair? :emma "lemmon" filtered-stack))

;; testing for top-elem-has-pair?
(expect (top-elem-has-pair? :fn "map") filtered-stack)
(expect true ((top-elem-has-pair? :ns  "corefns.corefns") [{:fn "map" :ns "corefns.corefns"}
                                                           {:something "else" :whatever "computer science"}]))
(expect false ((top-elem-has-pair? :emma "lemmon") [{:fn "map" :ns "corefns.corefns"}
                                                    {:something "else" :emma "lemmon"}]))

;; testing for helper-top-elem-has-all-pairs?
(expect true (helper-top-elem-has-all-pairs? {:ns "corefns.corefns", :fn "map"} filtered-stack))
(expect false (helper-top-elem-has-all-pairs? {:emma "lemmon", :not "pass"} filtered-stack))

;; testing for top-elem-has-all-pairs?
(expect (top-elem-has-all-pairs? {:ns "corefns.corefns", :fn "map"}) filtered-stack)
(expect true ((top-elem-has-all-pairs? {:ns "corefns.corefns" :fn "map"}) [{:fn "map" :ns "corefns.corefns"}
                                                                           {:something "else" :emma "lemmon"}]))
(expect false ((top-elem-has-all-pairs? {:emma "lemmon" :not "pass"}) [{:fn "map" :ns "corefns.corefns"}
                                                                       {:something "else" :emma "lemmon"}]))

;; testing for helper-nth-elem-has-pair?
(expect true (helper-nth-elem-has-pair? 1 :ns "intro.core" filtered-stack))
(expect false (helper-nth-elem-has-pair? 1 :emma "lemmon" filtered-stack))

;; testing for nth-elem-has-pair?
(expect (nth-elem-has-pair? 4 :fn "map") filtered-stack)
(expect true ((nth-elem-has-pair? 0 :ns  "corefns.corefns") [{:fn "map" :ns "corefns.corefns"}
                                                             {:something "else" :whatever "computer science"}]))
(expect false ((nth-elem-has-pair? 0 :emma "lemmon") [{:fn "map" :ns "corefns.corefns"}
                                                      {:something "else" :emma "lemmon"}]))

;; testing for helper-nth-elem-has-all-pairs?
(expect true (helper-nth-elem-has-all-pairs? 4 {:ns "clojure.core", :fn "map"} filtered-stack))
(expect false (helper-nth-elem-has-all-pairs? 4 {:emma "lemmon", :not "pass"} filtered-stack))

;; testing for nth-elem-has-all-pairs?
(expect (nth-elem-has-all-pairs? 4 {:ns "clojure.core", :fn "map"}) filtered-stack)
(expect true ((nth-elem-has-all-pairs? 0 {:ns "corefns.corefns" :fn "map"}) [{:fn "map" :ns "corefns.corefns"}
                                                                             {:something "else" :emma "lemmon"}]))
(expect false ((nth-elem-has-all-pairs? 1 {:emma "lemmon" :not "pass"}) [{:fn "map" :ns "corefns.corefns"}
                                                                         {:something "else" :emma "lemmon"}]))

;; testing for helper-trace-doesnt-have-function?
(expect false (helper-trace-doesnt-have-function? "map" filtered-stack))
(expect false (helper-trace-doesnt-have-function? "doall" filtered-stack))
(expect true (helper-trace-doesnt-have-function? "soup" filtered-stack))
(expect true (helper-trace-doesnt-have-function? "map" []))

;; testing for trace-doesnt-have-function?
(expect (trace-doesnt-have-function? "singapore") filtered-stack)
(expect false ((trace-doesnt-have-function? "map") [{:fn "map" :ns "corefns.corefns"}
                                                    {:something "else" :emma "lemmon"}]))
(expect true ((trace-doesnt-have-function? "donkey") [{:fn "map" :ns "corefns.corefns"}
                                                      {:something "else" :emma "lemmon"}]))

;; testing for helper-trace-doesnt-have-namespace?
(expect false (helper-trace-doesnt-have-namespace? "corefns.corefns" filtered-stack))
(expect false (helper-trace-doesnt-have-namespace? "intro.core" filtered-stack))
(expect true (helper-trace-doesnt-have-namespace? "soup" filtered-stack))
(expect true (helper-trace-doesnt-have-namespace? "map" []))

;; testing for trace-doesnt-have-namespace?
(expect (trace-doesnt-have-namespace? "sausage.pineapple") filtered-stack)
(expect false ((trace-doesnt-have-namespace? "corefns.corefns") [{:fn "map" :ns "corefns.corefns"}
                                                                 {:something "else" :emma "lemmon"}]))
(expect true ((trace-doesnt-have-namespace? "donkey") [{:fn "map" :ns "corefns.corefns"}
                                                       {:something "else" :emma "lemmon"}]))

;; testing for helper-trace-doesnt-have-pair?
(expect false (helper-trace-doesnt-have-pair? :fn "map" filtered-stack))
(expect false (helper-trace-doesnt-have-pair? :fn "doall" filtered-stack))
(expect true (helper-trace-doesnt-have-pair? :fn "soup" filtered-stack))
(expect true (helper-trace-doesnt-have-pair? :fn "map" []))

;; testing for trace-doesnt-have-pair?
(expect (trace-doesnt-have-pair? :fn "nachos") filtered-stack)
(expect false ((trace-doesnt-have-pair? :something "else") [{:fn "map" :ns "corefns.corefns"}
                                                            {:something "else" :emma "lemmon"}]))
(expect true ((trace-doesnt-have-pair? :shark "donkey") [{:fn "map" :ns "corefns.corefns"}
                                                         {:something "else" :emma "lemmon"}]))

;; testing for helper-trace-doesnt-have-all-pairs?
(expect false (helper-trace-doesnt-have-all-pairs? {:anon-fn false, :fn "seq"} filtered-stack))
(expect true (helper-trace-doesnt-have-all-pairs? {:fn "doall" :foo "bar"} filtered-stack))
(expect true (helper-trace-doesnt-have-all-pairs? {:fn "doall" :anon-fn true} filtered-stack))
(expect true (helper-trace-doesnt-have-all-pairs? {:fn "soup" :despicable-me "minions"} filtered-stack))
(expect true (helper-trace-doesnt-have-all-pairs? {:fn "map" :loser "winner"} []))

;; testing for trace-doesnt-have-all-pairs?
(expect (trace-doesnt-have-all-pairs? {:fn "map" :clojure "ruby"}) filtered-stack)
(expect false ((trace-doesnt-have-all-pairs? {:something "else" :emma "lemmon"}) [{:fn "map" :ns "corefns.corefns"}
                                                                                  {:something "else" :emma "lemmon"}]))
(expect true ((trace-doesnt-have-all-pairs? {:shark "donkey" :porpoise "laughter"}) [{:fn "map" :ns "corefns.corefns"}
                                                                                     {:something "else" :emma "lemmon"}]))
(expect true ((trace-doesnt-have-all-pairs? {:shark "donkey" :ns "corefns.corefns"}) [{:fn "map" :ns "corefns.corefns"}
                                                                                      {:something "else" :emma "lemmon"}]))
(expect true ((trace-doesnt-have-all-pairs? {:something "else" :ns "corefns.corefns"}) [{:fn "map" :ns "corefns.corefns"}
                                                                                        {:something "else" :emma "lemmon"}]))
