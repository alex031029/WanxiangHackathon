(ns frontend.pages.application.core
  (:require ["react-native" :as rn]
            ["native-base" :as nb]
            [reagent.core :as r]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [frontend.styles.application :as a-styles])
  (:require-macros [cljs.core.async.macros :refer [go]]))

;(defn page
;  "A page for user to apply credentials"
;  [^js navigation]
;  (let [content (r/atom "")
;        m-ref (r/atom nil)
;        data (r/atom nil)]
;    (fn [^js navigation]
;      (r/as-element
;        [:> nb/Container
;         [:> nb/Header
;          [:> nb/Left]
;          [:> nb/Body
;           [:> nb/Text "申请"]]
;          [:> nb/Right]]
;
;         [:> nb/Content
;          [:> nb/Item
;           [:> nb/Icon {:name "ios-search"}]
;           [:> nb/Input {:placeholder "请搜索"
;                         :onChangeText #(reset! content %)
;                         :ref #(reset! m-ref %)}]
;           (when (not= content "")
;             nil
;             [:> nb/Icon {:name "ios-close"
;                          :on-press (fn []
;                                      (reset! content "")
;                                      ((.. ^js m-ref -_root -clear)))}])]
;          [:> nb/Button {:style a-styles/appli-button
;                         :on-press (fn []
;                                     (go (let [response (<! (http/get "https://localhost/search"
;                                                                      {:with-credentials? false
;                                                                       :query-params {"query_issuer" "did:eth:d6DaE32b2F55fBadeAEb23819d6c3b6083eFbE0d"}}))]
;                                           (reset! data (:body response)))))}
;           [:> nb/Text "搜索"]]]
;         (when (not= data nil)
;           [:> nb/Card
;            [:> nb/CardItem {:header true
;                             :button true
;                             :on-press nil}
;             [:> nb/Text ]]
;            [:> nb/CardItem
;             [:> nb/body
;              [:> nb/Text]]]
;            [:> nb/CardItem {:footer true
;                             :button true
;                             :on-press nil}
;             [:> nb/Text "申请"]]])]))))

(defn page
  [^js navigation]
  (r/create-class
    {:getInitialState (fn [this]
                        {:content ""
                         :data    nil
                         :m-ref   nil})
     :render          (fn [this]
                        [:> nb/Container
                         [:> nb/Header
                          [:> nb/Left]
                          [:> nb/Body
                           [:> nb/Text "Application"]]
                          [:> nb/Right]]

                         [:> nb/Content
                          [:> nb/Item
                           [:> nb/Icon {:name "ios-search"}]
                           [:> nb/Input {:placeholder  "please input"
                                         :onChangeText #(r/set-state this {:content %})
                                         :ref          #(r/set-state this {:m-ref %})}]
                           (when (not= (:content (r/state this)) "")
                             nil
                             [:> nb/Icon {:name     "ios-close"
                                          :on-press (fn []
                                                      (r/set-state this {:content ""})
                                                      (-> ^js (:m-ref (r/state this))
                                                          .-_root
                                                          .clear))}])]
                          [:> nb/Button {:style    a-styles/appli-button
                                         :on-press #(r/set-state this {:data "has"})}
                           [:> nb/Text "Search"]]
                          (when (not= (:data (r/state this)) nil)
                            [:> nb/Card {:style a-styles/card}
                             [:> nb/CardItem {:header   true
                                              :button   true
                                              :on-press nil}
                              [:> nb/Text "Karma_charging"]]
                             [:> nb/CardItem
                              [:> nb/Body
                               [:> nb/Text "name: Karma_charging"]
                               [:> nb/Text "industry: Charging"]
                               [:> nb/Text "model: Issuer"]]]
                             [:> nb/CardItem {:footer   true
                                              :button   true
                                              :on-press (fn []
                                                          (.navigate navigation "details" {:data (:data (r/state this))}))}
                              [:> nb/Text "Apply"]]])]])}))
