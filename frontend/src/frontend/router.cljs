(ns frontend.router
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
    ;[cpc-did.event]
    ;[cpc-did.subs]
            [frontend.components.static-assets :as assets]
            [frontend.utils.react-navigation :as r-nav]
            [frontend.pages.information.core :as info]
            [frontend.pages.start-page.core :as start]
            [frontend.pages.credentials.core :as cred]
            [frontend.pages.application.core :as appli]
            [frontend.pages.auth :as auth]
            [frontend.pages.start-page.input-info :as input-info]
            [frontend.pages.detail :as detail]))
;;;; App router


;;; --re-frame start-------------------------------------------
;(rf/dispatch-sync [:initialize-db])

;;; -- Some Options -------------------------------------------

(def info-stack-nav-options {:tab-bar-label "信息"
                             :tab-bar-icon  {:active-icon assets/icon
                                             :icon        assets/icon}})
(def cred-stack-nav-options {:tab-bar-label "证书"
                             :tab-bar-icon  {:active-icon assets/icon
                                             :icon        assets/icon}})
(def appli-stack-nav-options {:tab-bar-label "申请"
                              :tab-bar-icon  {:active-icon assets/icon
                                              :icon        assets/icon}})
(def tab-bar-options {:activeTintColor   "#4CB4E7"
                      :inactiveTintColor "#FFEE93"
                      :style             {:backgroundColor "#FFC09F"}})

;;; -- Navigator ----------------------------------------------
;;; Different navigator can nest, compose them to implement more functions.
(def info-stack
  (r-nav/make-stack-navigator
    [{:route-name  :info
      :page        info/page
      :nav-options nil}]
    {:initialRouteName "info"
     :headerMode       "none"}))

(def cred-stack
  (r-nav/make-stack-navigator
    [{:route-name  :cred
      :page        cred/page
      :nav-options nil}]
    {:initialRouteName "cred"
     :headerMode       "none"}))

(def appli-stack
  (r-nav/make-stack-navigator
    [{:route-name  :appli
      :page        appli/page
      :nav-options nil}]
    {:initialRouteName "appli"
     :headerMode       "none"}))
(def tab-nav
  (r-nav/make-bottom-tab-navigator
    [{:route-name  :info-nav
      :page        info-stack
      :nav-options info-stack-nav-options}
     {:route-name  :cred-nav
      :page        cred-stack
      :nav-options cred-stack-nav-options}
     {:route-name  :appli-nav
      :page        appli-stack
      :nav-options appli-stack-nav-options}]
    {:initialRouteName "info-nav"
     :tabBarOptions     tab-bar-options
     :headerMode       "none"}))

(def start-stack
  (r-nav/make-stack-navigator
    [{:route-name  :start
      :page        start/page
      :nav-options nil}
     {:route-name  :input
      :page        input-info/page
      :nav-options nil}
     ]
    {:initialRouteName "start"}))

(def Auth-stack
  (r-nav/make-stack-navigator
    [{:route-name :details
      :page detail/page
      :nav-options nil}
     {:route-name :auth
      :page auth/page
      :nav-options nil}
     {:route-name :start-nav
      :page start-stack
      :nav-options nil
      :is-navigator true}
     {:route-name  :tab-nav
      :page        tab-nav
      :nav-options nil
      :is-navigator true}]
    {:initialRouteName "auth"
     :headerMode "none"}))

(def app-container (r-nav/make-app-container tab-nav))
