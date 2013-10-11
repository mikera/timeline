(ns mikera.timeline.utils
  (:use [mikera.cljutils error])
  (:import [org.joda.time Instant DateTimeUtils])
  (:import [org.joda.time.base AbstractInstant]))

(defn long-time
  "Returns the value of a timestamp, as a long number of milliseconds since the Java / UNIX epoch 
   time may be any Joda instant, or omitted (returns the current time)"
  (^long []
    (DateTimeUtils/currentTimeMillis))
  (^long [time]
    (cond 
      (number? time) (long time)
      (instance? Instant time) (.getMillis ^Instant time)
      (instance? AbstractInstant time) (.getMillis (.toInstant ^AbstractInstant time))
      :else (error "Time format not recognised: " time))))