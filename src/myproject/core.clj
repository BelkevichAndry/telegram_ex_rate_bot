(ns myproject.core
  (:require [clojure.core.async :refer [<!!]]
            [clojure.string :as str]
            [environ.core :refer [env]]
            [morse.handlers :as h]
            [morse.polling :as p]
            [morse.api :as t]
	          [org.httpkit.client :as http]
            [clojure.data.json :as json]
            [clojure.core :as core]
            [clojure.zip :as zip])
  (:gen-class))
; TODO: fill correct token
(def token (env :telegram-token))
;; (use 'hickory.core)


;; (def words "Лапочка Заечка Солнышко Любимая Зайчонок Котик Богиня Принцесса Ангелочек Русалочка Фея Пчелка Лягушонок Сладкая девочка Сладенькая Бубука Страстная Козявочка любимая Шоколадочка Царевна Хулиганка Обезьянка Мышка Цветочек Мой свет Блестящая Веточка Жизнь моя Куколка Лапунечка Мурзилка Монстрик Игруша Капелька Киса Лапонька Тростиночка Рыжик Бусинка Няша Лепесток Листочек Пушистик Родная Сказочная Сердечко Стройняшка Улыбашка Хорошка Медвежонок Персик Мурлыка Совенок Шалунья Ягодка Талантище Умеха Тигренок Снежинка Рыбка Снегурочка Птичка Озорная Пташка Неповторимая Розочка Бубусечка Девчушка Мотоцикленок  Бабочка Конфетка Мартышечка Дурочка Ириска Пиявочка Цыпа Маля Мася Утя Пандочка Пупочек Милашка Олененок Чудо голубоглазое Хрюнделечек Попочка Ласточка Звездочка Барби Вишенка Клубничка Душечка Киска Красотулька Котена")
;; (def value (:body (deref  (http/get "https://afisha.tut.by/film/zayachya_shkola/"))))
;; (def word-array (clojure.string/split words  #" "))
;; word-array
;; (def happy_words (map str/lower-case word-array))


;; (def parsed-doc (parse "https://afisha.tut.by/film/zayachya_shkola/"))
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

;;   (h/message-fn
;;     (fn [{{id :id} :chat :as message}]
;;       (println "Intercepted message: " message)
;;       (t/send-text token id (str "Викуся ты моя " (rand-nth happy_words))))))


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


