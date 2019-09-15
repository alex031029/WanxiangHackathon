(ns frontend.pages.start-page.core
  (:require ["native-base" :as nb]
            [reagent.core :as r]
            [frontend.styles.start :as s-styles]))

(defn page
  "docstring"
  [^js navigation]
  (r/as-element
    [:> nb/Container {:style s-styles/container}
     [:> nb/Button {:style s-styles/button
                    :on-press #(.navigate navigation "input")}
      [:> nb/Text {:style s-styles/text} "Start Creation"]]]))
