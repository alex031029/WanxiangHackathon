(ns frontend.styles.components.my-icon)

(defn bottom-tab-label
  [focused]
  {:font-size 18
   :color (if focused
            "#acd"
            "#000")
   :margin-bottom 10})

(def default-icon-style {:margin-top 14})
