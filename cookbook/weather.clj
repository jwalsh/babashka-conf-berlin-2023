#!/usr/bin/env bb

(ns weather.core
  (:require [babashka.cli :as cli]))

(defn main
  [{:keys [city country]}]
  (println city country))

(def app
  (cli/app
    {:name "weather"
     :about "Simple CLI for weather information"
     :fn main
     :args
     {"city"
      {:required true
       :type :string
       :prompt true
       :help "City name"}
      "country"
      {:required true
       :type :string
       :prompt true
       :help "Country name"}}}))

(cli/run! app)
