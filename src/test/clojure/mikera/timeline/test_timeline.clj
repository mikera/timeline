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
     (is (== 0 (count t)))
     (is (== 0 (count (log-change t 0 nil))))))
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
      (is (== 3 (count t))))))

(deftest test-ifn
  (let [t (timeline [[0 :a] [1 :b] [2 :c]])]
    (is (= :b (t 1))))) 

(deftest logging
  (testing "log to empty timeline"
     (let [t (timeline)
           lt (log t 0 :foo)]
       (is (== 1 (count lt)))))
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

(deftest test-start-end
  (let [t1 (timeline)
        t2 (log (log t1 10 1) 20 2)]
    (is (nil? (start t1)))
    (is (nil? (end t1)))
    (is (= 10 (start t2)))
    (is (= 20 (end t2)))))

(deftest test-start-end
  (let [t1 (timeline)
        t2 (log (log t1 10 1) 20 2)]
    (is (nil? (start t1)))
    (is (nil? (end t1)))
    (is (= 10 (start t2)))
    (is (= 20 (end t2)))))

(deftest test-resample
  (let [tl (timeline)
        tl (log tl 1000 1)
        tl (log tl 1100 2)
        tl (log tl 1500 3)
        tl (log tl 2000 4)]
    (is (= 20 (count (resample tl :events 20))))
    (is (= 10 (count (resample tl :interval 100))))
    (is (= 11 (count (resample tl :interval 100 :add-last true))))))

(deftest test-slice
  (let [tl (timeline)
        tl (log tl 1000 1)
        tl (log tl 1100 2)
        tl (log tl 1500 3)
        tl (log tl 2000 4)]
    (is (= [3 4] (map second (slice tl 1500))))
    (is (= [4] (map second (slice tl 1501))))
    (is (= [2 3] (map second (slice tl 1100 2000))))
    (is (= [2 3 4] (map second (slice tl 1100 2001))))
    (is (= [] (map second (slice tl 1000 1000))))
    (is (= [] (map second (slice (timeline) 1000))))
    (is (= [] (map second (slice (timeline) -100 1000))))
    (is (= [2 3] (map second (slice-indexes tl 1 3)))) 
    ))

(deftest regressions
  (testing "seq"
    (is (= [] (map second (timeline)))))
  (testing "vals on timeline"
    (is (= [1 2] (vec (vals (timeline {1000 2 100 1})))))))

(deftest parsing
  (testing "ISO8601 string"
    (is (== 0 (long-time "1970-01-01T00:00:00.0Z")))))



