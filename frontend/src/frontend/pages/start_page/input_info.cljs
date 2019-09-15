(ns frontend.pages.start-page.input-info
  (:require ["react-native" :as rn]
            ["native-base" :as nb]
            ["ethers" :as ethers]
            [reagent.core :as r]
            [frontend.styles.input-info :as input-styles]
            [frontend.utils.cljs-js-transit :as t])
  (:require-macros [frontend.utils.macros :refer [promise->]]))

(defn gen-keystore
  [password vin ^js navigation]
  (let [random-wallet (-> ^js ethers
                          .-ethers
                          .-Wallet
                          .createRandom)
        info (.-signingKey ^js random-wallet)]
    (promise->
      (.encrypt random-wallet password)
      #(.setItem rn/AsyncStorage
                "keystore"
                 %)
      (.setItem rn/AsyncStorage
                "VIN"
                vin)
      (.navigate navigation "tab-nav" {:VIN vin}))))

(defn page
  "docstring"
  [^js navigation]
  (let [vin (r/atom "")
        password (r/atom "")
        is-loading (r/atom false)]
    (fn [^js navigation]
      (r/as-element
        (if @is-loading
          [:> nb/Container {:style input-styles/container}
           [:> nb/Spinner {:color "blue"}]]

          [:> nb/Container {:style input-styles/container}
           [:> rn/Text {:style input-styles/text-hint} "Please input Vehicle VIN"]
           [:> rn/TextInput {:placeholder "Please input"
                             :style       input-styles/input-info
                             :onChangeText #(reset! vin %)}]
           [:> rn/Text {:style input-styles/text-hint} "Please input password"]
           [:> rn/TextInput {:placeholder "Please input"
                             :style       input-styles/input-info
                             :onChangeText #(reset! password %)}]
           [:> nb/Button {:style    input-styles/button
                          :on-press (fn []
                                      (reset! is-loading true)
                                      (gen-keystore @password @vin navigation))}
            [:> nb/Text "Next Step"]]])))))
