(ns frontend.pages.credentials.core
  (:require ["react-native" :as rn]
            ["native-base" :as nb]
            [reagent.core :as r]))

(defn page
  "A page to show credentials"
  [^js navigation]
  (r/as-element
    [:> nb/Container
     [:> nb/Header
      [:> nb/Left]
      [:> nb/Body
       [:> nb/Title "Credential"]]
      [:> nb/Right
       [:> nb/Button {:transparent true
                      :on-press #(.navigate navigation "info")}
        [:> nb/Icon {:name "ios-qr-scanner"}]]]]
     [:> nb/Content {:padder true}
      [:> nb/Card
       [:> nb/CardItem {:header true
                        :button true
                        :bordered true
                        :on-press #(.navigate navigation "details")}
        [:> nb/Text "Charging"]]
       [:> nb/CardItem {:bordered true}
        [:> nb/Body
         [:> nb/Text "Type: charging"]
         [:> nb/Text "Color: red"]
         [:> nb/Text "Brand: Karma"]]]]]]))
