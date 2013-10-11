(ns mikera.timeline
  (:use [mikera.cljutils error])
  (:use [mikera.timeline protocols impl])
  (:require [clj-time.core :as time])
  (:import [org.joda.time Instant DateTimeUtils])
  (:require [clojure.core.rrb-vector :as fv])
  (:import [org.joda.time.base AbstractInstant]))

(defn long-time
  "Returns the long value of a timestamp. time may be any Joda instant, or ommitted (returns the current time)"
  (^long []
    (DateTimeUtils/currentTimeMillis))
  (^long [time]
    (cond 
      (number? time) (long time)
      (instance? Instant time) (.getMillis ^Instant time)
      (instance? AbstractInstant time) (.getMillis (.toInstant ^AbstractInstant time))
      :else (error "Time format not recognised: " time))))

(defn timeline 
  "Constructs a new timeline object"
  ([] 
    (->Timeline (long-time) [] [])))

(defn seek 
  "Returns the index of the last event before the specified time, or nil if no such event exists"
  ([tl time]
    (let [time (long-time time)]
      (seek-index tl time))))