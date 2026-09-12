package com.disasterrelief.thread;

import com.disasterrelief.dao.AllocationDAO;
import com.disasterrelief.model.Allocation;
import com.disasterrelief.util.FileLogger;

import java.sql.Timestamp;

/**
 * Background Multithreading Worker for simulating and tracking delivery status.
 * Demonstrates:
 * - Independent asynchronous worker updating domain models
 * - Concurrency and background operational tracking
 */
public class DeliveryTrackingWorker implements Runnable {

    private final int allocationId;
    private final AllocationDAO allocationDAO = new AllocationDAO();

    public DeliveryTrackingWorker(int allocationId) {
        this.allocationId = allocationId;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("DeliveryTracker-" + allocationId);
        try {
            Allocation alloc = allocationDAO.findById(allocationId);
            if (alloc == null) return;

            // Step 1: PREPARING -> DISPATCHED
            Thread.sleep(3000);
            alloc.setStatus("DISPATCHED");
            alloc.setDispatchDate(new Timestamp(System.currentTimeMillis()));
            alloc.setTrackingNotes("Vehicle departed depot. En route to designated relief sector.");
            allocationDAO.updateStatus(alloc);
            FileLogger.logDelivery("Allocation #" + allocationId + " DISPATCHED to " + alloc.getShelterName());

            // Step 2: DISPATCHED -> IN_TRANSIT
            Thread.sleep(4000);
            alloc.setStatus("IN_TRANSIT");
            alloc.setTrackingNotes("Approaching relief zone. Coordinates verified with local volunteer.");
            allocationDAO.updateStatus(alloc);
            FileLogger.logDelivery("Allocation #" + allocationId + " IN_TRANSIT to " + alloc.getShelterName());

            // Step 3: IN_TRANSIT -> DELIVERED
            Thread.sleep(4000);
            alloc.setStatus("DELIVERED");
            alloc.setDeliveryDate(new Timestamp(System.currentTimeMillis()));
            alloc.setTrackingNotes("Relief supplies successfully handed over to shelter manager and verified.");
            allocationDAO.updateStatus(alloc);
            FileLogger.logDelivery("Allocation #" + allocationId + " DELIVERED successfully at " + alloc.getShelterName());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            FileLogger.logSystem("ERROR", "DELIVERY_WORKER", "Tracking error for allocation #" + allocationId + ": " + e.getMessage());
        }
    }
}
