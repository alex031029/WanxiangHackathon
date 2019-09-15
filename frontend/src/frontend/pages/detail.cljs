(ns frontend.pages.detail
  (:require ["react-native" :as rn]
            ["native-base" :as nb]
            [reagent.core :as r]))

(defn page
  "docstring"
  [^js navigation]
  [:> nb/Container
   [:> nb/Content {:padder true}
    [:> nb/Card
     [:> nb/CardItem {:header true
                      :bordered true}
      [:> nb/Text "Charging"]]
     [:> nb/CardItem {:bordered true}
      [:> nb/Body
       [:> nb/Text "Type: charging"]
       [:> nb/Text "Color: red"]
       [:> nb/Text "Brand: karma"]
       [:> nb/Text "Signature: \"0xf036e92dba5722e76bff2b359f3705e63e7f77694c58c829b91c7973547a45922f3f1bd7816ba750dec074a0945fbebe1d1aab1fac8084557e984ab08505235701"]
       [:> nb/Text "Create-time: \"2019-09-15T09:05:48.724"]
       [:> nb/Text "expired_time: \"2020-09-15T09:05:48.724"]]]]]])
