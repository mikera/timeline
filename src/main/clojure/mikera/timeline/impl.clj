(ns mikera.timeline.impl
  (:use [mikera.timeline protocols])
  (:require [clojure.core.rrb-vector :as fv])
  (:import [mikera.timeline ISeekIndex])
  (:import [org.joda.time Instant DateTimeUtils]))

(deftype Timeline
  [times
   values]
  
  ISeekIndex
    (seek [tl time]
      (let [time (long time)
            n (long (count values))]
        (long 
          (loop [i (long 0) j n]
            (if (>= i j) 
              (dec i)
              (let [mi (long (quot (+ i j) 2))
                    moff (long (nth times mi))]
                (if (>= time moff)
                  (recur (inc mi) j)
                  (recur i mi)))))))) 
  
  clojure.lang.Seqable
    (seq [tl]
      (map vector (map #(Instant. (long %)) times) values))  
  
  Object
    (toString [tl]
      (str (vec (seq tl))))  
    
  PTimeline
    (value-at  [tl time]
        (let [time (long time)
              index (seek-index tl time)]
          (if index
            (nth values index)
            nil)))
    
    (seek-index [tl time]
      (let [time (long time)
       ix (.seek tl time)]
        (if (>= ix 0) ix nil)))
    
    (event-value [tl index]
      (nth values index))
    
    (event-time [tl index]
      (nth times index))
    
    (event-count [tl]
      (count values))
    
    (add-event [tl time value]
      (let [ix (inc (.seek tl time))
            n (count values)]
        (Timeline. (fv/catvec (conj (fv/subvec times 0 ix) time) (fv/subvec times ix n))
                   (fv/catvec (conj (fv/subvec values 0 ix) value)   (fv/subvec values ix n))))))