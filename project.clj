(defproject intro "0.2.3"
  :description "A pilot project to use Clojure for introductory computer science courses at the University of Minnesota - Morris"
  :dependencies [
                 [org.clojure/clojure "1.8.0"]
                 [clj-stacktrace "0.2.8"] ;;No updates for over a year
                 [org.clojure/core.incubator "0.1.2"] ;;No updates for over a year
                 [seesaw "1.4.5"]
                 [expectations "2.1.1"]]
  :plugins [[lein-expectations "0.0.7"]
            [lein-autoexpect "1.0"]
            [lein-pprint "1.1.2"]]
  :main intro.core)
