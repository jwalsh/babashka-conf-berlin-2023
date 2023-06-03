#!/usr/bin/env bb

(require '[babashka.fs :as fs])
(require '[clojure.edn :as edn])
(require '[babashka.cli :as cli])

(def ENTRIES-LOCATION "entries.edn")

(defn read-entries []
  (if (fs/exists? ENTRIES-LOCATION)
    (try
      (edn/read-string (slurp ENTRIES-LOCATION))
      (catch Exception _
        []))
    []))

(defn add-entry [entry]
  (let [entries (read-entries)]
    (spit ENTRIES-LOCATION
          (conj entries {:timestamp (System/currentTimeMillis)
                         :entry     entry}))))

(defn delete-entry [timestamp]
  (let [entries (read-entries)
        updated-entries (remove #(= (:timestamp %) timestamp) entries)]
    (spit ENTRIES-LOCATION
          updated-entries)))

(defn update-entry [timestamp new-text]
  (let [entries (read-entries)
        updated-entries (map #(if (= (:timestamp %) timestamp)
                                (assoc % :entry new-text)
                                %) entries)]
    (spit ENTRIES-LOCATION
          updated-entries)))

(defn list-entries []
  (let [entries (read-entries)]
    (if (empty? entries)
      (println "No entries found.")
      (doseq [entry entries]
        (println (str "Timestamp: " (:timestamp entry)))
        (println (str "Entry: " (:entry entry)))
        (println "")))))

(def cli-opts
  {:add    {:alias   :a
            :desc    "Add a new entry."
            :args    {:entry {:desc "Text of the entry."
                             :required true
                             :doc-format :positional}}}
   :delete {:alias   :d
            :desc    "Delete an entry."
            :args    {:timestamp {:desc "Timestamp of the entry to delete."
                                  :required true
                                  :doc-format :positional}}}
   :update {:alias   :u
            :desc    "Update an entry."
            :args    {:timestamp {:desc "Timestamp of the entry to update."
                                  :required true
                                  :doc-format :positional}
                      :entry     {:desc "New text for the entry."
                                  :required true
                                  :doc-format :positional}}}
   :list   {:alias   :l
            :desc    "List all entries."}})

(defn -main []
  (let [result (cli/parse-opts *command-line-args* {:spec cli-opts})]
    (cond (:add result) (add-entry (:entry (:add result)))
          (:delete result) (delete-entry (Long/parseLong (:timestamp (:delete result))))
          (:update result) (update-entry (Long/parseLong (:timestamp (:update result))) (:entry (:update result)))
          (:list result) (list-entries)
          :else
          (do
            (list-entries)
            (when (seq *command-line-args*)
              (println "Invalid command. Usage: bb-diary [add/delete/update/list] [args...]"))))))

(-main)
