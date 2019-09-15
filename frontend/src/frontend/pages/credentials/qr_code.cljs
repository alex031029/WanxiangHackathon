(ns frontend.pages.credentials.qr-code
  (:require [reagent.core :as r]
            ["react-native" :as rn]
            ["native-base" :as nb]
            [frontend.styles.qr-code :as q-style]
            [frontend.styles.utils.layout :as layout]
            [frontend.utils.cljs-js-transit :as t]))

(defn page
  "A qr-code page, which scans the qr-code and return some info"
  [^js navigation]
  (r/create-class
    {:get-initial-state   (fn [this]
                            {:has-camera-permission nil
                             :scanned false})
     :component-did-mount (fn [this]
                            (let [perm (t/->cljs permission)]
                              (promise-> ((:askAsync perm) (:CAMERA perm))
                                         (fn [value]
                                           (let [status (:status (t/->cljs value))]
                                             (r/set-state
                                               this
                                               {:has-camera-permission (identical?
                                                                         status
                                                                         "granted")}))))))
     :render              (fn [this]
                            (let [perm (:has-camera-permission (r/state this))]
                              (case perm
                                (case perm
                                  nil [:> rn/View]
                                  false [:> rn/Text "No access to camera"]
                                  [:> bc-scanner/BarCodeScanner {:style layout/container}
                                   [:> rn/View {:style q-style/layer-top}
                                    [:> rn/View {:style q-style/back-end-info}
                                     [:> nb/Button {:transparent true
                                                    :on-press nil}
                                      [:> nb/Icon {:name "md-arrow-back"}]]
                                     [:> rn/Text  {:style q-style/back-end-text} "扫码授权"]]]
                                   [:> rn/View {:style q-style/layer-center}
                                    [:> rn/View {:style q-style/center-left}]
                                    [:> rn/View {:style q-style/center-center}
                                     [:> rn/View {:style q-style/left-top}]
                                     [:> rn/View {:style q-style/left-bottom}]
                                     [:> rn/View {:style q-style/right-top}]
                                     [:> rn/View {:style q-style/right-bottom}]]
                                    [:> rn/View {:style q-style/center-right}]]
                                   [:> rn/View {:style q-style/layout-bottom}
                                    [:> rn/Text {:style q-style/hint-info} "扫描商户二维码，授权我的认证"]]])))
                            )}))
