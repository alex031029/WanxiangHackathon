(ns frontend.utils.react-navigation
  (:require ["react-navigation" :as router]
            [reagent.core :as r]
            [frontend.styles.components.header :as h-styles]
            [frontend.components.my-icon :as m-icon]))

;;;; The namespace is used to wrap component with navigationOptions
;;;; NavigationOptions is a static property for React Component
;;;; We can add it as same as to add a property for JS Object

;;; Do some transfer.
(defn keyword-size
  "Use for transforming JavaScript Object to CLJS Map
  eg: (def a #js {:foo 1, :bar 2, :baz #js [1 2 3]})

      (js->clj a :keywordize-keys true)
      ;;=> {:foo 1, :bar 2, :baz [1 2 3]}

      (js->clj a)
      ;;=> {\"foo\" 1, \"bar\" 2, \"baz\" [1 2 3]}
  Then you can operate it as CLJS Map"
  [obj]
  (js->clj obj :keywordize-keys true))

(defn navigation->state
  "Take navigation info and transform it into CLJS Map"
  [^js navigation]
  (-> navigation .-state keyword-size))


;;; Warp a component with static "navigationOptions"
;;; Also use "reactify-component" to do "reagent->react" transformation

(defn wrap-stack-screen
  "Wrap component with navigationOptions, which works for StackNavigator screens
  We can see available keys/config list:
    https://reactnavigation.org/docs/en/stack-navigator.html#navigationoptions-for-screens-inside-of-the-navigator
  Don't try to config screen Header in it.
  parameters:
  @param component   a reagent component
  @param & props     use destructure to get config parameters"
  [component {:keys [header-title                           ;string or component
                     header-left                            ;integral component
                     header-right                           ;integral component
                     header
                     ]}]
  ;; r/reactify-component do a lot things
  ;; We can use destructure which is the same as "js" directly. Normally, we should write:
  ;; (fn [props]
  ;;   (aget props "navigation") ; get navigation
  ;;   (aget props "screenProps") ; get screenProps
  ;;   )
  ;; As you know, every screen component will get a "props" passed from "Navigation", which
  ;; is a object {:screenProps ..., :navigation ...}
  ;; In generously, it should be a js object. But here, it's map(cljs, only for the outermost layer)
  ;; because of "reactify-component", so we can use {:keys [navigation]}

  ;; r/current-component can get "this" that is the same as "React Native", js object.
  ;; only in the direct function which as parameter passed to js world.
  (let [c (r/reactify-component
            (fn [{:keys [navigation]}]
              ;(println navigation)
              ;(let [this (r/current-component)]
              ;  (println "Keys" (js-keys this))
              ;  (println "props" (aget this "props")))
              [component navigation (navigation->state navigation)]))]
    (aset c "navigationOptions"
          (fn [^js navigation]
            (clj->js (merge
                       {:headerStyle {:borderBottomWidth 0}}
                       (if header {:header header} {})
                       (if header-title
                         (if (string? header-title)
                           {:headerTitleStyle h-styles/string-header-title-style}
                           {:headerTitleStyle h-styles/search-header-title-style
                            :headerTitleContainerStyle h-styles/header-title-container-style})
                         {})
                       (if header-title {:headerTitle header-title} {})
                       (if header-left {:headerLeft (r/as-element [header-left navigation])
                                        :headerLeftContainerStyle h-styles/header-left-container-style} {})
                       (if header-right {:headerRight (r/as-element [header-right navigation])
                                         :headerRightContainerStyle h-styles/header-right-container-style} {})))))
    c))

(defn wrap-bottom-tab-screen
  "Wrap component with navigationOptions, which works for BottomTabNavigator screens
   We can see available keys/config list:
         https://reactnavigation.org/docs/en/bottom-tab-navigator.html#navigationoptions-for-screens-inside-of-the-navigator
   parameters:
   @param component   a StackNavigator
   @param & props     use destructure to get config parameters"
  [component {:keys [tab-bar-label                          ;string
                     tab-bar-icon                           ;map with key `:active-icon` and `:icon`
                     tab-bar-visible
                     icon-style]}]
  (aset component "navigationOptions"
        (fn [^js navigation]
          (clj->js {:tabBarLabel tab-bar-label
                    :tabBarIcon  (fn [^js obj]
                                   (m-icon/bottom-tab-item
                                     (.-tintColor obj)
                                     (.-focused obj)
                                     (:active-icon tab-bar-icon)
                                     (:icon tab-bar-icon)))})))
  component)


;;; Wrap some "create" navigator function of "react-navigation", so
;;; we needn't to import "react-navigation" in other namespace

(defn make-stack-navigator
  "Take a route configuration and navigator configs to
  set up an actual stack navigator"
  [route-configs navigator-configs]
  (router/createStackNavigator
    (clj->js
      (into {}
            (for [config route-configs]
              (assoc {}
                (:route-name config)
                {:screen (if (:is-navigator config)
                           (:page config)
                           (wrap-stack-screen
                             (:page config)
                             (:nav-options config)))}))))
    (clj->js navigator-configs)))

(defn make-bottom-tab-navigator
  "Take a route configs and navigator configs to
  set up an actual bottom-tab navigator"
  [route-configs navigator-configs]
  (router/createBottomTabNavigator
    (clj->js
      (into {}
            (for [config route-configs]
              (assoc {}
                (:route-name config)
                {:screen (wrap-bottom-tab-screen
                           (:page config)
                           (:nav-options config))}))))
    (clj->js navigator-configs)))

(defn make-app-container
  "Take a navigator to set up an actual app container"
  [navigator]
  (router/createAppContainer navigator))
