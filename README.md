timeline
========

Adds the time dimension to Clojure data.

A timeline is a chronologically tagged sequence of values, stored as an immutable persistent data structure.

### Motivation

Often it is helpful to see how values change over time.

### Features:

 - Accepts anything as values
 - View of timelines as a sequence of changes ("events")
 - Builds on **RRB-Trees** (https://github.com/clojure/core.rrb-vector) to enable efficient merging, slicing and concatenation of timelines
 - Builds on **clj-time / Joda time* for a decent immutable timestamp handling API

### Getting Started

To use Timeline, you can get the latest released version from Clojars:

 - https://clojars.org/net.mikera/timeline
 
### Examples
 
```clojure


```