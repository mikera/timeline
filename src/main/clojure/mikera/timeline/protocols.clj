(ns mikera.timeline.protocols)

(defprotocol PTimeline
  (value-at 
    [tl]
    [tl time] 
    "Gets the value of the timeline at a specified time. time must be a long instant")
  (seek-index 
    [tl time]
    "Returns the event index of the last event before the specified time, or nil if no such event exists.
     time must be a long instant")
  (event-value
    [tl index]
    "Gets the value from the event on timeline at the specified index. Index must be valid")
  (event-time
    [tl index]
    "Gets the time from the event on timeline at the specified index. Index must be valid")
  (event-count
    [tl]
    "Returns the total number of distinct events on the timeline"))
