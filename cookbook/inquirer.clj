#!/usr/bin/env bb

(ns inquirer.core
  (:require [prompts :as prompts]))

(defn main []
  (let [choices ["metric" "imperial"]
        selected-unit (prompts/select "Metric or imperial?" {:choices choices})]
    (println selected-unit)))

(main)
