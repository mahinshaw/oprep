(ns oprep.functions.dev-functions)

;;; Defines functions to be used by the peers. These are located
;;; with fully qualified namespaced keywords, such as
;;; oprep.functions.dev-functions/trim-spaces

(defn loud-impl [s]
  (str s "!"))

(defn question-impl [s]
  (str s "?"))

;;;;; Destructuring functions ;;;;;
(defn loud [segment]
  {:word (loud-impl (:word segment))})

(defn question [segment]
  {:word (question-impl (:word segment))})
