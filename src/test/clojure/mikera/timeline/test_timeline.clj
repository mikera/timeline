(ns mikera.timeline.test-timeline
  (:use clojure.test)
  (:use mikera.timeline)
  (:use mikera.timeline.utils)
  (:use [mikera.cljutils error]))

(deftest basic-lookup
  (testing "empty timeline"
    (let [t (timeline)]
     (is (nil? (seek t 0)))
     (is (nil? (seek t (+ (now) 1000))))
     (is (== 0 (event-count t)))))
  (testing "single value" 
	  (let [t (timeline {10 :foo})]
	    (is (nil? (at t 0)))
	    (is (= :foo (at t 10)))
	    (is (= :foo (at t 15)))))
  (testing "multi-values" 
	  (let [t (timeline {10 :foo 40 :bar 30 :baz})]
	    (is (nil? (at t 0)))
	    (is (= :foo (at t 10)))
	    (is (= :baz (at t 35)))
	    (is (= :bar (at t 55)))
      (is (== 3 (event-count t))))))

(deftest logging
  (testing "log to empty timeline"
     (let [t (timeline)
           lt (log t 0 :foo)]
       (is (== 1 (event-count lt)))))
  (testing "log multiple values at once"
           (let [t (timeline)
                 tl (log t (now) 1 2 3)]
             (is (= [1 2 3] (map second tl)))))
  (testing "log in middle of timeline"
           (let [tl (timeline)
                 tl (log tl 0 1)
                 tl (log tl 10 2)
                 tl (log tl 0 3)]
             (is (= [1 3 2] (map second tl))))))

(deftest parsing
  (testing "ISO8601 string"
    (is (== 0 (long-time "1970-01-01T00:00:00.0Z")))))



