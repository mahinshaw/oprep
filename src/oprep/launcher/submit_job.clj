(ns oprep.launcher.submit-job
  (:require [clojure.core.async :refer [>!!] :as async]
            [clojure.java.io :refer [resource]]
            [oprep.launcher.dev-system :refer [oprep-dev-env]]
            [oprep.workflows.dev-workflow :refer [workflow]]
            [oprep.catalogs.dev-catalog :refer [build-catalog] :as dc]
            [oprep.lifecycles.dev-lifecycle :refer [build-lifecycles] :as dl]
            [oprep.flow-conditions.dev-flow-conditions :as df]
            ;; allows for lookup of functions when we import the catalog
            [oprep.functions.dev-functions]
            [oprep.dev-inputs.csv-inputs :as csv-inputs]
            ))

(defn submit-job-dev [dev-env] 
  (let [dev-cfg (-> "dev-peer-config.edn" resource slurp read-string)
        peer-config (assoc dev-cfg :onyx/id (:onyx-id dev-env))
        dev-catalog (build-catalog 10 50) 
        dev-lifecycles (build-lifecycles)]
    ;; Automatically pipes the data structure into the channel, attaching :done at the end
    (sl/bind-inputs! dev-lifecycles {:in csv-inputs/input-segments})
    (let [job {:workflow workflow
               :catalog dev-catalog
               :lifecycles dev-lifecycles
               :flow-conditions sf/flow-conditions
               :task-scheduler :onyx.task-scheduler/balanced}]
      (onyx.api/submit-job peer-config job)
      ;; Automatically grab output from the stubbed core.async channels,
      ;; returning a vector of the results with data structures representing
      ;; the output.
      (sl/collect-outputs! dev-lifecycles [:loud-output :question-output]))))
