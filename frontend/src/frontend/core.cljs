(ns frontend.core
  (:require [reagent.core :as r]
            [cpc-did.router :as router]
            [cpc-did.utils.register-app :as register]))

(defn start
  {:dev/after-load true}
  []
  (register/render-root (r/as-element [:> router/app-container])))

(defn init []
  (start))
