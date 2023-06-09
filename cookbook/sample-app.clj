#!/usr/bin/env bb

(ns sample-app.core
  (:require [babashka.fs :as fs]
            [babashka.process :as process]
            [babashka.shell :as shell]
            [babashka.jdbc :as jdbc]
            [babashka.json :as json]
            [babashka.pods :as pods]
            [babashka.timer :as timer]))

;; File System Operations
(fs/mkdir "data")
(fs/touch "data/sample.txt")
(fs/copy "data/sample.txt" "data/sample_copy.txt")
(fs/delete "data/sample.txt")

;; Process Execution
(let [result (process/exec "ls" "-l")]
  (println (str "Exit code: " (:exit result)))
  (println (str "Output: " (:out result)))
  (println (str "Error: " (:err result))))

;; Shell Commands
(shell/sh "echo Hello, World!")
(shell/cp "data/sample.txt" "data/sample_copy.txt")
(shell/rm "data/sample.txt")

;; Database Connectivity
(def db (jdbc/get-datasource {:classname "org.h2.Driver"
                             :subprotocol "h2:mem"
                             :subname "sample-db"}))

(jdbc/execute! db ["CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR)"])
(jdbc/insert! db :users {:id 1 :name "John Doe"})
(jdbc/query db ["SELECT * FROM users"])

;; JSON Manipulation
(def data {:name "John Doe" :age 30})
(def json-str (json/write-str data))
(def parsed-data (json/read-str json-str))

;; Babashka Pods
(def p (pods/default-pod))
(pods/in-ns p 'clojure.string)
(println (join " " ["Hello," "World!"]))

;; Timers
(timer/set! :my-timer (timer/schedule #(println "Timer executed") 1000))

;; Main Function
(defn -main []
  (println "Sample Application"))

(-main)
