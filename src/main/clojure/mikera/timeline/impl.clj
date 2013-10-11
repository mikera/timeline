(ns mikera.timeline.impl
  (:use [mikera.timeline protocols])
  (:import [mikera.timeline ISeekIndex])
  (:import [org.joda.time Instant DateTimeUtils]))

(deftype Timeline
  [^long base
   offsets
   values]
  
  ISeekIndex
    (seek [tl offset]
      (let [offset (long offset)
            n (long (count values))]
        (long 
          (loop [i (long 0) j n]
            (if (>= i j) 
              (dec i)
              (let [mi (long (quot (+ i j) 2))
                    moff (long (nth offsets mi))]
                (if (>= offset moff)
                  (recur (inc mi) j)
                  (recur i mi)))))))) 
  
  PTimeline
    (value-at 
      [tl] (value-at tl (DateTimeUtils/currentTimeMillis)))
    (value-at  [tl time]
        (let [time (long time)
              index (seek-index tl time)]
          (if index
            (nth values index)
            nil)))
    
    (seek-index [tl time]
      (let [time (long time)
            offset (- time base)
       ix (.seek tl offset)]
        (if (>= ix 0) ix nil)))
    
    (event-value [tl index]
      (nth values index))
    
    (event-time [tl index]
      (nth offsets index))
    
    (event-count [tl]
      (count values)))