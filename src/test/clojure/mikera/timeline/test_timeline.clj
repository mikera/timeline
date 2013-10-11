(ns mikera.timeline.test-timeline
  (:use clojure.test)
  (:use mikera.timeline)
  (:use [mikera.cljutils error]))

(deftest empty-timeline
  (let [t (timeline)]))