(ns frontend.pages.auth
  (:require ["react-native" :as rn]
            [reagent.core :as r]
            [frontend.utils.cljs-js-transit :as t])
  (:require-macros [frontend.utils.macros :refer [promise->]]))

(defn page
  "docstring"
  [^js navigation]
  (.getItem rn/AsyncStorage
            "keystore"
            (fn [err res]
              (if res
                (.navigate navigation "tab-nav")
                (.navigate navigation "start-nav"))))
  (r/as-element nil))
