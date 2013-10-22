(ns mikera.timeline
  (:use [mikera.cljutils error])
  (:use [mikera.timeline utils impl])
  (:require [mikera.timeline [protocols :as ep] [impl :as imp]]) 
  (:require [clj-time.core :as time])
  (:require [clojure.core.rrb-vector :as fv]))

(set! *warn-on-reflection* true)
(set! *unchecked-math* true)

(defn timeline 
  "Constructs a new timeline object. events may be a sequence of timestamp/value pairs or a map of timestamps to values.
   Events do not need to be in sorted order."
  ([] 
    (imp/->Timeline [] []))
  ([events]
    (if (empty? events)
      (timeline)
      (let [events (map (fn [[t v]] [(long-time t) v]) events)
            ; base-time (long (first (first events)))
            events (sort-by first events)]
       (imp/->Timeline (fv/vec (map first events)) (fv/vec (map second events)))))))

(defn seek 
  "Returns the index of the last event before the specified time, or nil if no such event exists"
  ([tl time]
    (let [time (long-time time)]
      (ep/seek-index tl time))))

(defn at 
  "Returns the value of a timeline at a specified time, or nil if there is no value available"
  ([tl time]
    (if-let [ix (seek tl time)]
      (ep/event-value tl ix)
      nil)))

(defn start
  "Returns the time of the first event in the timeline, or nil if there are no events."
  ([timeline]
    (if (== 0 (ep/event-count timeline))
      nil
      (ep/event-time timeline 0)))) 

(defn end
  "Returns the time of the last event in the timeline, or nil if there are no events."
  ([timeline]
    (let [n (ep/event-count timeline)]
      (if (== 0 n)
        nil
        (ep/event-time timeline (dec n)))))) 

(defn event-count 
  "Counts the total number of events in a timeline."
 ([tl] (ep/event-count tl)))

(defn log
  "Adds value to a timeline. If time is omitted, uses the current system time."
  ([tl value]
    (log tl (now) value))
  ([tl time value]
    (let [time (long-time time)] 
      (ep/add-event tl time value)))
  ([tl time value & more-values]
    (let [time (long-time time)] 
      (loop [tl (ep/add-event tl time value)
             vs more-values]
        (if (empty? vs)
          tl
          (recur (ep/add-event tl time (first vs)) (next vs)))))))

(defn log-change
  "Logs a value to a timeline if and only if the value is a change to the previous value. This has the effect of
   collapsing identically values events into the first such event."
  ([tl value]
    (log-change tl (now) value))
  ([tl time value]
    (let [time (long-time time)]
      (if (= value (at tl time))
        tl
        (ep/add-event tl time value))))
  ([tl time value & more-values]
    (let [time (long-time time)]
      (reduce #(log-change tl time %) (log-change tl time value) more-values)))) 

(defn resample 
  "Resamples a timeline, logging all values at regular intervals 

   Must provide either desired number of events or the sampling interval."
  ([timeline & {:keys [start end interval events add-last]}]
    (let [t0 (long (if start (long-time start) (long (mikera.timeline/start timeline))))
          tn (long (if end (long-time end) (mikera.timeline/end timeline)))
          interval (double (if interval 
                            (double interval)
                            (/ (- tn t0) (double events))))
          events (double (if events (double events) (/ (- tn t0) (double interval))))]
      (loop [tl (mikera.timeline/timeline) i (long 0)]
        (if (< i events)
          (let [ti (+ t0 (* interval i))]
            (recur (log tl ti (at timeline ti))
                  (inc i)))
          (if add-last ;; append the last value from the resampling
            (log tl tn (at timeline tn))
            tl))))))

(defn slice 
  "Slaices a timeline from the start time (inclusive) to the end time (exclusive)"
  ([timeline start]
    (let [start (long-time start)
          si (inc (or (ep/seek-index timeline (dec start)) -1))
          ei (ep/event-count timeline)]
      (ep/slice-indexes timeline si ei)))
  ([timeline start end]
    (let [start (long-time start)
          end (long-time end)
          si (inc (or (ep/seek-index timeline (dec start)) -1))
          ei (inc (or (ep/seek-index timeline (dec end)) -1))]
      (ep/slice-indexes timeline si ei))))

;    (ep/slice tl 
;              (long-time start)
;              (long-time end))))