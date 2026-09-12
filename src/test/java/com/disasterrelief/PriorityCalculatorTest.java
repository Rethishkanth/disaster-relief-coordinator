package com.disasterrelief;

import com.disasterrelief.model.ResourceRequest;
import com.disasterrelief.util.PriorityCalculator;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.PriorityQueue;

public class PriorityCalculatorTest {

    @Test
    public void testScoreCalculation() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        double score = PriorityCalculator.calculateScore("CRITICAL", 1000, now, "CRITICAL");

        // Urgency: 10 * 40 = 400
        // Population: 1000 * 0.35 = 350
        // Waiting: ~0
        // Severity bonus: 25
        // Expected approx: 775.0
        Assert.assertTrue("Score should be greater than 700 for critical disaster scenario", score > 700.0);
    }

    @Test
    public void testPriorityQueueOrdering() {
        PriorityQueue<ResourceRequest> pq = new PriorityQueue<>();

        ResourceRequest lowReq = new ResourceRequest();
        lowReq.setRequestId(1);
        lowReq.setPriorityScore(150.0);

        ResourceRequest criticalReq = new ResourceRequest();
        criticalReq.setRequestId(2);
        criticalReq.setPriorityScore(820.0);

        ResourceRequest mediumReq = new ResourceRequest();
        mediumReq.setRequestId(3);
        mediumReq.setPriorityScore(420.0);

        pq.add(lowReq);
        pq.add(criticalReq);
        pq.add(mediumReq);

        // Highest score should poll first
        ResourceRequest first = pq.poll();
        Assert.assertNotNull(first);
        Assert.assertEquals(2, first.getRequestId());
        Assert.assertEquals(820.0, first.getPriorityScore(), 0.01);

        ResourceRequest second = pq.poll();
        Assert.assertNotNull(second);
        Assert.assertEquals(3, second.getRequestId());

        ResourceRequest third = pq.poll();
        Assert.assertNotNull(third);
        Assert.assertEquals(1, third.getRequestId());
    }
}
