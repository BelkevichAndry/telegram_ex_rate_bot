(ns telegram_ex_rate_bot.core
  (:require [clojure.core.async :refer [<!!]]
            [clojure.string :as str]
            [environ.core :refer [env]]
            [morse.handlers :as h]
            [morse.polling :as p]
            [morse.api :as t]
            [org.httpkit.client :as http]
            [clojure.data.json :as json]
            )
  (:gen-class))
; TODO: fill correct token
(def token (env :telegram-token))

(defn ex_rate_getter []
  (let [ response (json/read-str (:body (deref  (http/get "http://www.nbrb.by/API/ExRates/Rates/USD?ParamMode=2"))))
         ex_rate (get response "Cur_OfficialRate")
         date (get response "Date")]
    (str "Стоимость доллара: " ex_rate "\n"
         "Дата: " date)))

(h/defhandler handler



  (h/command-fn "start"
                (fn [{{id :id :as chat} :chat}]
                  (println "Bot joined new chat: " chat)
                  (t/send-text token id "Welcome to myproject!")))

  (h/command-fn "help"
                (fn [{{id :id :as chat} :chat}]
                  (println "Help was requested in " chat)
                  (t/send-text token id "Help is on the way")))

  (h/message-fn
    (fn [{{id :id} :chat :as message}]
      (println "Intercepted message: " message)
      (t/send-text token id (ex_rate_getter)))))


(defn -main
  [& args]
  (when (str/blank? token)
    (println "Please provde token in TELEGRAM_TOKEN environment variable!")
    (System/exit 1))

  (println "Starting the myproject")
  (<!! (p/start token handler)))


