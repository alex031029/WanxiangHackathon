(ns frontend.utils.cljs-js-transit
  (:require [cognitect.transit :as t]))

;;; Define reader for json
(def r (t/reader :json))

;;; Define writer for json
(def w (t/writer :json))

;;; Read
;;; It's not compatible well with js/JSON.
(defn json->cljs
  [json]
  (t/read r json))

;;; Write
;;; It's not compatible well with js/JSON.
(defn cljs->json
  [cljs]
  (t/write w cljs))

;;; Use clj->js to do other converting from cljs to js
(defn ->js
  [cljs]
  (clj->js cljs))

;;; Use js->cljs to do other converting from js to cljs
(defn ->cljs
  [m-js]
  (js->clj m-js :keywordize-keys true))