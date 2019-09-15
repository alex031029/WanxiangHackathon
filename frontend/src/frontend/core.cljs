(ns frontend.core
  (:require [reagent.core :as r]
            [frontend.router :as router]
            [frontend.utils.register-app :as register]))

(defn start
  {:dev/after-load true}
  []
  (register/render-root (r/as-element [:> router/app-container])))

(defn init []
  (start))
