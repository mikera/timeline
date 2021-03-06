(ns mikera.timeline.protocols)

(defprotocol PTimeline
  (value-at 
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
    
  (slice-indexes
    [tl start-index end-index]
    "Slices a subrange of events")
  
  (add-event 
    [tl time value]
    "Adds an event with the specific value at the given time" 
    ))
