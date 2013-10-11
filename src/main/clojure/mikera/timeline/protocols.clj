(ns mikera.timeline.protocols)

(defprotocol PTimeline
  (value-at 
    [tl]
    [tl time] 
    "Gets the value of the timeline at a specified time. time must be a long instant")
  (seek-index 
    [tl time]
    "Returns the event index of the last event before the specified time, or nil if no such event exists.
     time must be a long instant"))
