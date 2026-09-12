package com.disasterrelief.service;

import com.disasterrelief.dao.AllocationDAO;
import com.disasterrelief.dao.RequestDAO;
import com.disasterrelief.dao.VehicleDAO;
import com.disasterrelief.dao.VolunteerDAO;
import com.disasterrelief.exception.DatabaseException;
import com.disasterrelief.model.Allocation;
import com.disasterrelief.util.FileLogger;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Delivery and Dispatch Lifecycle Service.
 * Demonstrates:
 * - Collections: Queue<Allocation> managing FIFO pending dispatch tasks
 * - Coordinated status updates across Vehicles, Volunteers, and Requests
 */
public class DeliveryService {

    private final AllocationDAO allocationDAO = new AllocationDAO();
    private final RequestDAO requestDAO = new RequestDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final VolunteerDAO volunteerDAO = new VolunteerDAO();

    // FIFO Dispatch Queue
    private final Queue<Allocation> dispatchQueue = new LinkedList<>();

    public synchronized void enqueueForDispatch(Allocation alloc) {
        if (alloc != null) {
            dispatchQueue.offer(alloc);
            FileLogger.logDelivery("Enqueued allocation #" + alloc.getAllocationId() + " for dispatch staging.");
        }
    }

    public synchronized Allocation processNextDispatch() {
        return dispatchQueue.poll();
    }

    public synchronized int getQueueSize() {
        return dispatchQueue.size();
    }

    public boolean updateDeliveryStatus(int allocationId, String newStatus, String notes) throws DatabaseException {
        try {
            Allocation alloc = allocationDAO.findById(allocationId);
            if (alloc == null) {
                return false;
            }

            alloc.setStatus(newStatus);
            alloc.setTrackingNotes(notes);

            if ("DISPATCHED".equalsIgnoreCase(newStatus)) {
                alloc.setDispatchDate(new Timestamp(System.currentTimeMillis()));
            } else if ("DELIVERED".equalsIgnoreCase(newStatus)) {
                alloc.setDeliveryDate(new Timestamp(System.currentTimeMillis()));

                // Release vehicle back to AVAILABLE
                if (alloc.getVehicleId() != null && alloc.getVehicleId() > 0) {
                    vehicleDAO.updateStatus(alloc.getVehicleId(), "AVAILABLE");
                }
                // Release volunteer back to AVAILABLE
                if (alloc.getVolunteerId() != null && alloc.getVolunteerId() > 0) {
                    volunteerDAO.updateStatus(alloc.getVolunteerId(), "AVAILABLE");
                }
                // Mark request as DELIVERED
                requestDAO.updateStatus(alloc.getRequestId(), "DELIVERED");
            }

            boolean updated = allocationDAO.updateStatus(alloc);
            if (updated) {
                FileLogger.logDelivery(String.format("Allocation #%d status updated to [%s]: %s", allocationId, newStatus, notes));
            }
            return updated;

        } catch (SQLException e) {
            throw new DatabaseException("Failed updating delivery status: " + e.getMessage(), e);
        }
    }
}
