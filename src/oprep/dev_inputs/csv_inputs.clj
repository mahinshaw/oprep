(ns oprep.dev-inputs.csv-inputs
  (:require [clojure.java.io :refer [resource reader]]
            [clojure.data.csv :as csv]
            [clojure-csv.core :as ccsv]
            [semantic-csv.core :as sc]))

(defn csv->map [file]
  (with-open [in-file (reader file)]
    (->>
     (ccsv/parse-csv in-file)
     sc/mappify
     (map #(hash-map :row %1))
     doall)))

(def accounting (resource "Accounting.csv"))

(def body-fat (resource "Bodyfat.csv"))

(def input-segments (csv->map accounting))
