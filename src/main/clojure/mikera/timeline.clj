(ns mikera.timeline
  (:use [mikera.cljutils error])
  (:use [mikera.timeline utils protocols impl])
  (:require [clj-time.core :as time])
  (:require [clojure.core.rrb-vector :as fv]))


(defn timeline 
  "Constructs a new timeline object. events may be timestamp/value pairs or a map of timestamps to values.
   Events do not need to be in sorted order."
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