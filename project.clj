(defproject clj-calc "0.1.0-SNAPSHOT"
  :description "A super simple calculator written in Clojure, using a Context Free Grammar and UI components."
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [instaparse "1.4.2"]
                 [seesaw "1.4.5"]]
  :main clj-calc.core
)
