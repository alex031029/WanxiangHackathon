(ns frontend.router
  (:require [re-frame.core :as rf]
            [cpc-did.event]
            [cpc-did.subs]))
;;;; App router


;;; --re-frame start-------------------------------------------
;(rf/dispatch-sync [:initialize-db])

;;; -- Some Options -------------------------------------------

;;; -- Navigator ----------------------------------------------
;;; Different navigator can nest, compose them to implement more functions.
(def app-container (r-nav/make-app-container tab-nav))
