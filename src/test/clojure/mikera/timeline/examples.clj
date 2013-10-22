(ns mikera.timeline.examples
  (:use mikera.timeline))

;; ==========================================================
;; timeline logging example
;;
;; in which we use an atom to accumulate changes to a timeline

;; put a timeline in an atom for logging
(def event-log (atom (timeline)))

;; add some events with a small delay between each
(dotimes [i 10]
  (Thread/sleep (rand-int 100))
  (swap! event-log log (str "Event: i")))

;; list all the timestamped events
(seq @event-log)
