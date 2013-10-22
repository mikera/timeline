(ns mikera.timeline.utils
  (:use [mikera.cljutils error])
  (:require [clj-time.core :as time])
  (:import [org.joda.time.format DateTimeFormatter ISODateTimeFormat])
  (:import [org.joda.time Instant DateTimeUtils])
  (:import [org.joda.time.base AbstractInstant]))

(set! *warn-on-reflection* true)
(set! *unchecked-math* true)

(def ^DateTimeFormatter time-format (ISODateTimeFormat/dateTime))

(defn now 
  "Returns the current time, as a long number of milliseconds since the Java / UNIX epoch"
  (^long []
    (DateTimeUtils/currentTimeMillis)))

(defn parse-datetime ^long [^String s]
  (.parseMillis time-format s))

(defn long-time
  "Returns the value of a timestamp, as a long number of milliseconds since the Java / UNIX epoch 
   time may be any Joda instant, or an ISO8601 DateTime string"
  (^long [time]
    (cond 
      (number? time) (long time)
      (instance? Instant time) (.getMillis ^Instant time)
      (instance? AbstractInstant time) (.getMillis (.toInstant ^AbstractInstant time))
      (string? time) (parse-datetime time) 
      :else (error "Time format not recognised: " time))))