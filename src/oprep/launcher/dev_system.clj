(ns oprep.launcher.dev-system
  (:require [clojure.core.async :refer [chan <!!] :as async]
            [clojure.java.io :refer [resource]]
            [com.stuartsierra.component :as component]
            [onyx.plugin.core-async]
            [onyx.api]))

;;Construct the development system as a component
(defrecord OprepDevEnv [n-peers]
  component/Lifecycle

  (start [component]
    (println "Starting Onyx development environment")
    (let [onyx-id (java.util.UUID/randomUUID)
          env-config (assoc (-> "env-config.edn" resource slurp read-string)
                            :onyx/id onyx-id)
          peer-config (assoc (-> "dev-peer-config.edn" resource slurp read-string)
                             :onyx/id onyx-id)
          ;; start the environment
          env (onyx.api/start-env env-config)
          ;; start the peer group
          peer-group (onyx.api/start-peer-group peer-config)
          ;; start each peer
          peers (onyx.api/start-peers n-peers peer-group)]
      ;; set up the component record
      (assoc component
             :env env
             :peer-group peer-group
             :peers peers
             :onyx-id onyx-id)))

  (stop [component]
    (println "Stopping Onyx development environment")

    ;; shutdown each peer in teh component
    (doseq [v-peer (:peers component)]
      (onyx.api/shutdown-peer v-peer))

    ;; shutdown the peer-group and then the environment.
    (onyx.api/shutdown-peer-group (:peer-group component))
    (onyx.api/shutdown-env (:env component))

    ;; clean up the component
    (assoc component
           :env nil
           :peer-group nil
           :peers nil)))

(defn oprep-dev-env
  "Construct a development environment with n peers."
  [n-peers]
  (map->OprepDevEnv {:n-peers n-peers}))
