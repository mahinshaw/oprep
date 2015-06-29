(set-env!
 :dependencies '[;; clj
                 [org.clojure/clojure "1.7.0-RC2"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [org.onyxplatform/onyx "0.6.0"]
                 [org.clojure/data.csv "0.1.2"]
                 [clojure-csv/clojure-csv "2.0.1"]
                 [semantic-csv "0.1.0"]
                 [com.stuartsierra/component "0.2.3"]

                 [org.clojure/tools.namespace "0.2.10" :scope "test"]]

 :source-paths #{"src"}
 :resource-paths #{"resources"}
 :target-path "target")

(task-options!
 pom {:project 'oprep
      :version "0.1.0"})

(require
 '[clojure.tools.namespace.repl :as repl])
