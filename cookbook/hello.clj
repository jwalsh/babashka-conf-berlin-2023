#!/usr/bin/env bb

(ns hello.core
  (:require [babashka.cli :as cli]))

(defn -main [& args]
  (cli/with-command-line args
    "Simple program that greets NAME for a total of COUNT times."
    {:count ["--count" "Number of greetings." :default 1 :parse-fn int]
     :name ["--name" "Your name" :prompt true :parse-fn str]})
  (let [count (cli/get-arg :count)
        name (cli/get-arg :name)]
    (doseq [_ (range count)]
      (println (str "Hello " name "!")))))

(-main)
