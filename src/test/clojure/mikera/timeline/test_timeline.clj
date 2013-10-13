(ns mikera.timeline.test-timeline
  (:use clojure.test)
  (:use mikera.timeline)
  (:use mikera.timeline.utils)
  (:use [mikera.cljutils error]))

(deftest basic-lookup
  (testing "empty timeline"
    (let [t (timeline)]
     (is (nil? (seek t 0)))
     (is (nil? (seek t (+ (long-time) 1000))))
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
  (testing "log multiple values"
           (let [t (timeline)
                 tl (log t (long-time) 1 2 3)]
             (is (= [1 2 3] (map second tl))))))



