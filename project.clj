(defproject intro "0.1"
  :description "A pilot project to use Clojure for introductory computer science courses at the University of Minnesota - Morris"
  :dependencies [
                 [org.clojure/clojure "1.6.0"]
                 [clj-stacktrace "0.2.8"]
                 [org.clojure/core.incubator "0.1.2"]
                 [seesaw "1.4.3"]
                 [expectations "2.0.6"]]
  :plugins [[lein-autoexpect "1.0"]
            [lein-pprint "1.1.2"]]
  :main intro.core)
