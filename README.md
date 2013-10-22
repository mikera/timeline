timeline
========

Adds the time dimension to Clojure data.

A timeline is a chronologically tagged sequence of values, stored as an immutable persistent data structure.

### Motivation

Often it is helpful to see how values change over time. A timeline is an immutable value that itself, but 
represents a progression of values over time (which can also be seen as events).

Intended use cases are:

 - Fast in-memory querying / manipulation of time based data
 - Sensor / M2M data storage
 - Logging with timestamps
 - Recording a "history" of transactions or other values

### Features:

 - 100% immutable, persistent data structure
 - Accepts anything as values
 - View of timelines as a sequence of changes ("events")
 - Ability to look up the most recently set value for any timeline
 - Each event is a [time value] pair
 - Builds on **RRB-Trees** (https://github.com/clojure/core.rrb-vector) to enable efficient merging, slicing and concatenation of timelines
 - Builds on **clj-time / Joda time** for a decent immutable timestamp handling API

### Getting Started

To use Timeline, you can get the latest released version from Clojars:

 - https://clojars.org/net.mikera/timeline
 
### Examples

```clojure
(let [time-now (now)
      t (timeline) 
      t (log t time-now "Now")
      t (log t (+ time-now 1000) "One second later")
      t (log t (- time-now 1000) "One second before")]
  (seq t))
  
=> ([#<Instant 2013-10-16T02:16:46.879Z> "One second before"] 
    [#<Instant 2013-10-16T02:16:47.879Z> "Now"] 
    [#<Instant 2013-10-16T02:16:48.879Z> "One second later"])
``` 

Some more example usage is provided in the following test namespaces:

 - https://github.com/mikera/timeline/blob/develop/src/test/clojure/mikera/timeline/examples.clj
 - https://github.com/mikera/timeline/blob/develop/src/test/clojure/mikera/timeline/test_timeline.clj
 