(ns frontend.components.my-icon
  (:require ["react-native" :as rn]
            [frontend.styles.components.my-icon :as m-styles]
            [reagent.core :as r]))

(defn judge [condition result-a result-b]
  (if condition result-a result-b))

(defn bottom-tab-item [color focused select-image normal-image]
  (r/as-element
    [:> rn/Image {:style {:tintColor color :width 25 :height 25}
                  :source (judge focused select-image normal-image)}]))

(defn back-end-icon
  [source on-press]
  [:> rn/TouchableOpacity {:on-press on-press}
   [:> rn/Image {:source source}]])
