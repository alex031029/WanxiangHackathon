(ns frontend.styles.qr-code)

(def layer-top {:flex 1
                :background-color "rgba(0,0,0,.6)"})

(def layer-center {:flex 0
                   :height 216
                   :flex-direction "row"})

(def layout-bottom {:flex 1
                    :background-color "rgba(0,0,0,.6)"})

(def center-left {:flex 1
                  :background-color "rgba(0,0,0,.6)"})
(def center-center {:flex 0
                    :width 216
                    :height 216
                    :justify-content "center"
                    :align-items "center"})
(def center-right {:flex 1
                   :background-color "rgba(0,0,0,.6)"})
(def back-end-info {:margin-top 44
                    :margin-left 16
                    :flex-direction "row"
                    :justify-content "flex-start"})
(def back-end-text {:margin-left 8
                    :font-size 20
                    :font-weight "bold"
                    :color "#FFFFFF"})

(def hint-info {:margin-top 53
                :text-align "center"
                :font-size 20
                :color "#FFFFFF"})

;;; animation

(def base {:width 24
           :height 24
           :border-color "#4664E2"
           :background-color "rgba(0,0,0,0)"
           :position "absolute"})
(def left-top (merge
                base
                {:border-left-width 4
                 :border-top-width 4
                 :left 0
                 :top 0}))
(def left-bottom (merge
                   base
                   {:border-left-width 4
                    :border-bottom-width 4
                    :left 0
                    :bottom 0}))
(def right-top (merge
                 base
                 {:border-top-width 4
                  :border-right-width 4
                  :right 0
                  :top 0}))
(def right-bottom (merge
                    base
                    {:border-right-width 4
                     :border-bottom-width 4
                     :right 0
                     :bottom 0}))

