(ns mikera.timeline
  (:use [mikera.cljutils error])
  (:use [mikera.timeline protocols impl])
  (:require [clj-time.core :as time])
  (:import [org.joda.time Instant DateTimeUtils])
  (:require [clojure.core.rrb-vector :as fv])
  (:import [org.joda.time.base AbstractInstant]))

(defn long-time
  "Returns the long value of a timestamp. time may be any Joda instant, or omitted (returns the current time)"
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
    (->Timeline (long-time) [] []))
  ([events]
    (if (empty? events)
      (timeline)
      (let [events (map (fn [[t v]] [(long-time t) v]) events)
            base-time (long (first (first events)))
            events (sort-by first events)]
       (->Timeline base-time (fv/vec (map #(- (long (first %)) base-time) events)) (fv/vec (map second events)))))))

(defn seek 
  "Returns the index of the last event before the specified time, or nil if no such event exists"
  ([tl time]
    (let [time (long-time time)]
      (seek-index tl time))))

(defn at 
  "Returns the value of a timeline at a specified time, or nil if there is no value available"
  ([tl time]
    (if-let [ix (seek tl time)]
      (event-value tl ix)
      nil)))