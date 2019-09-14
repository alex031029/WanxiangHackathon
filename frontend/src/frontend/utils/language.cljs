(ns frontend.utils.language
  (:require ["i18n-js" :as i18n]
            ["expo-localization" :as Localization]
            [clojure.string :as cljs-str]))

(defonce enCN (js/require "../resources/translations/en.json"))
(defonce zhHansCN (js/require "../resources/translations/zh.json"))
(set! (.-fallback ^js i18n) true)
(set! (.-translations ^js i18n) #js {:enCN enCN :zhHansCN zhHansCN})
(set! (.-locale ^js i18n) (apply str
                             (-> ^js Localization
                                 .-locale
                                 (cljs-str/split "-"))))

(def lang i18n)
