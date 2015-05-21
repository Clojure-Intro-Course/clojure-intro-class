(defproject intro "0.2.2"
  :description "A pilot project to use Clojure for introductory computer science courses at the University of Minnesota - Morris"
  :dependencies [
                 ;[org.clojure/clojure "1.7.0-beta3"] ;;Required by libraries.
                 [org.clojure/clojure "1.6.0"]
                 [clj-stacktrace "0.2.8"] ;;No updates for over a year
                 [org.clojure/core.incubator "0.1.2"] ;;No updates for over a year
                 [seesaw "1.4.3"]
                 [expectations "2.0.9"]
                 [quil "2.2.5"]]
  :plugins [[lein-autoexpect "1.0"]
            [lein-pprint "1.1.2"]]
  :main intro.core)
