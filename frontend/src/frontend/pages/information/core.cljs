(ns frontend.pages.information.core
  (:require ["react-native" :as rn]
            ["native-base" :as nb]
            [reagent.core :as r]))

(defn page
  "A page to show user vehicle information"
  [^js navigation]
  (r/as-element
    [:> nb/Container
     [:> nb/Header
      [:> nb/Left]
      [:> nb/Body
       [:> nb/Title "Information"]]
      [:> nb/Right]]
     [:> nb/Content {:padder true}
      [:> nb/Card
       [:> nb/CardItem
        [:> nb/H1 "Vehicle Information"]]
       [:> nb/CardItem
        [:> nb/Body
         [:> nb/Text (str "VIN：LVGBY8095KG883255")]]]
       [:> nb/CardItem
        [:> nb/Body
         [:> nb/Text "Brand： Karma"]]]
       [:> nb/CardItem
        [:> nb/Body
         [:> nb/Text "Color： Red"]]]
       [:> nb/CardItem
        [:> nb/Body
         [:> nb/Text "Production Time： 2018-10-12"]]]
       [:> nb/CardItem
        [:> nb/Body
         [:> nb/Text "Purchase Time： 2019-02-21"]]]
       [:> nb/CardItem
        [:> nb/Body
         [:> nb/Text "Count Number： 1"]]]]]]))