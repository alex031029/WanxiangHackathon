(ns frontend.styles.components.header
  (:require [clojure.string :as c-str]))

;;; A transformation function

(defn first-to-lower
  "Convert first character of s to lower case"
  [s]
  (str (-> s (subs 0 1) .toLowerCase) (subs s 1)))
(defn hyphens-to-camel
  "Convert styles key to camel format"
  [styles]
  (let [ks (keys styles)]
    (into {}
          (for [k ks]
            (assoc {}
              (-> k
                  name
                  (c-str/split #"-")
                  (->> (map c-str/capitalize))
                  (c-str/join)
                  first-to-lower
                  keyword)
              (get styles k))))))

;;; styles
(def search-header-title-style
  {:left  0
   :right 0})

(def string-header-title-style
  ;; The styles pass to React style function without reagent transform, so
  ;; must use a camel format.
  (hyphens-to-camel {:color       "#000"
                     :font-size   20
                     :font-weight "bold"}))

(def header-left-container-style
  (hyphens-to-camel {:padding-left 24}))

(def header-right-container-style
  (hyphens-to-camel {:padding-right 16}))

(def header-title-container-style
  (hyphens-to-camel {:left 10 :right 10}))
