#!/usr/bin/env bb

(ns change.core
  (:require [babashka.cli :as cli]
            [clojure.string :as str]))

(defn change [amount]
  (let [coins [1 5 10 25]  ; value of pennies, nickels, dimes, quarters
        coin-lookup {25 "quarters"
                     10 "dimes"
                     5 "nickels"
                     1 "pennies"}]
    (letfn [(calc-change [amt coin]
              (let [num-rem (->> (math/floor (* amt 100))
                                 (int)
                                 (-> (int coin) rem))]
                (if (pos? num-rem)
                  (let [num (quot num-rem coin)
                        rem (rem num-rem coin)]
                    [{num (coin-lookup coin)} (calc-change rem coin)])
                  {})))]
      (calc-change amount (last coins)))))

(defn make-change [amount]
  (let [result (change (float amount))]
    (println (str/format "Change for %s:" amount))
    (doseq [correct-change result]
      (doseq [[num coin] correct-change]
        (println (str/format "%s: %s" coin num))))))

(def app
  (cli/app
    {:name "make-change"
     :about "Gives Correct Change"
     :commands
     {"make-change"
      {:about "Creates change for dollar and cents value: i.e. 1.34"
       :options
       {"--amount"
        {:required true
         :prompt "Amount: "
         :type :string
         :help "Creates change for dollar and cents value: i.e. 1.34"}}}
     :exec-fn make-change}}))

(cli/run! app)
