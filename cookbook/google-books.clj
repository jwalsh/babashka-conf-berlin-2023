#!/usr/bin/env bb

(ns google-books.core
  (:require [babashka.cli :as cli]
            [babashka.http :as http]
            [clojure.string :as str]
            [clojure.pprint :as pprint]))

(defn search [query]
  (let [url-format "https://www.googleapis.com/books/v1/volumes"
        query (str/join "+" (str/split query #"\s+"))
        query-params {:q query}
        response (http/get url-format {:query-params query-params})]
    (pprint/pprint (-> response :body :items))))

(defn get [id]
  (let [url-format (str "https://www.googleapis.com/books/v1/volumes/" id)
        response (http/get url-format)]
    (pprint/pprint (-> response :body))))

(def app
  (cli/app
    {:name "google-books"
     :about "Simple CLI for querying books on Google Books by Oyetoke Toby"
     :commands
     {"search"
      {:about "This search and return results corresponding to the given query from Google Books"
       :args {:query {:required true :type :string}}}
      "get"
      {:about "This return a particular book from the given id on Google Books"
       :args {:id {:required true :type :string}}}}}))

(cli/run! app)
