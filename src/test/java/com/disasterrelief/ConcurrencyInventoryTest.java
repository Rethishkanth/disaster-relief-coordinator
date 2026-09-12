package com.disasterrelief;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Concurrency Test demonstrating thread-safe inventory deduction.
 * Verifies that concurrent allocation requests cannot over-allocate or corrupt inventory.
 */
public class ConcurrencyInventoryTest {

    private int totalStock = 100;
    private final ReentrantLock stockLock = new ReentrantLock();

    @Test
    public void testConcurrentStockAllocationSafety() throws InterruptedException {
        int threadsCount = 20;
        int requestAmountPerThread = 10;
        // 20 threads * 10 = 200 units requested, but totalStock is only 100!
        // Exactly 10 requests should succeed, and 10 should be rejected.

        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadsCount);
        AtomicInteger successfulAllocations = new AtomicInteger(0);
        AtomicInteger rejectedAllocations = new AtomicInteger(0);

        for (int i = 0; i < threadsCount; i++) {
            service.submit(() -> {
                try {
                    stockLock.lock();
                    try {
                        if (totalStock >= requestAmountPerThread) {
                            totalStock -= requestAmountPerThread;
                            successfulAllocations.incrementAndGet();
                        } else {
                            rejectedAllocations.incrementAndGet();
                        }
                    } finally {
                        stockLock.unlock();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        service.shutdown();

        Assert.assertEquals(10, successfulAllocations.get());
        Assert.assertEquals(10, rejectedAllocations.get());
        Assert.assertEquals(0, totalStock);
        Assert.assertTrue("Remaining stock must never be negative", totalStock >= 0);
    }
}
