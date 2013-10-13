(ns mikera.timeline.examples
  (:use mikera.timeline))

;; ==========================================================
;; timeline logging example
;;
;; in which we use an atom to accumulate changes to a timeline

(def event-log (atom (timeline)))

(dotimes [i 10]
  (Thread/sleep (rand-int 100))
  (swap! event-log log (str "Event: i")))
