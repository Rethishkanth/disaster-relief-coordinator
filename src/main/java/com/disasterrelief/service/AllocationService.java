package com.disasterrelief.service;

import com.disasterrelief.dao.AllocationDAO;
import com.disasterrelief.dao.RequestDAO;
import com.disasterrelief.dao.ResourceDAO;
import com.disasterrelief.dao.VehicleDAO;
import com.disasterrelief.dao.VolunteerDAO;
import com.disasterrelief.exception.DatabaseException;
import com.disasterrelief.exception.DeliveryUnavailableException;
import com.disasterrelief.exception.DisasterReliefException;
import com.disasterrelief.exception.InsufficientResourceException;
import com.disasterrelief.exception.ResourceExpiredException;
import com.disasterrelief.model.Allocation;
import com.disasterrelief.model.Resource;
import com.disasterrelief.model.ResourceRequest;
import com.disasterrelief.model.Vehicle;
import com.disasterrelief.model.Volunteer;
import com.disasterrelief.thread.DeliveryTrackingWorker;
import com.disasterrelief.thread.ThreadManager;
import com.disasterrelief.util.FileLogger;

import java.sql.SQLException;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Service managing Resource Allocation and Request Fulfillment.
 * Demonstrates:
 * - Thread-Safe Concurrency: ReentrantLock protects shared inventory from race conditions
 * - Custom Exceptions: InsufficientResourceException, ResourceExpiredException, DeliveryUnavailableException
 * - Collections: PriorityQueue integration for automated emergency allocation
 * - Multithreading: Triggers asynchronous DeliveryTrackingWorker background task
 */
public class AllocationService {

    private static final ReentrantLock INVENTORY_LOCK = new ReentrantLock();

    private final AllocationDAO allocationDAO = new AllocationDAO();
    private final RequestDAO requestDAO = new RequestDAO();
    private final ResourceDAO resourceDAO = new ResourceDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final VolunteerDAO volunteerDAO = new VolunteerDAO();

    public List<Allocation> getAllAllocations() throws DatabaseException {
        try {
            return allocationDAO.findAll();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch allocations: " + e.getMessage(), e);
        }
    }

    public List<Allocation> getAllocationsForVolunteer(int volunteerId) throws DatabaseException {
        try {
            return allocationDAO.findByVolunteerId(volunteerId);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch volunteer tasks: " + e.getMessage(), e);
        }
    }

    public Allocation getAllocationById(int id) throws DatabaseException {
        try {
            return allocationDAO.findById(id);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch allocation #" + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Thread-Safe Resource Allocation.
     * Prevents race conditions and over-allocation using INVENTORY_LOCK.
     */
    public int allocateResource(int requestId, int resourceId, int quantityToAllocate,
                                Integer volunteerId, Integer vehicleId) throws DisasterReliefException {

        INVENTORY_LOCK.lock();
        try {
            ResourceRequest req = requestDAO.findById(requestId);
            if (req == null) {
                throw new DisasterReliefException("Request #" + requestId + " not found.");
            }
            if ("DELIVERED".equalsIgnoreCase(req.getStatus())) {
                throw new DisasterReliefException("Request #" + requestId + " is already delivered.");
            }

            Resource res = resourceDAO.findById(resourceId);
            if (res == null) {
                throw new DisasterReliefException("Resource #" + resourceId + " not found.");
            }

            // Check expiry
            if (res.getExpiryDate() != null && !res.getExpiryDate().isEmpty() && !"N/A".equalsIgnoreCase(res.getExpiryDate())) {
                if (res.getExpiryDate().compareTo("2026-01-01") < 0) { // Simple ISO date string check
                    throw new ResourceExpiredException(res.getResourceName(), res.getExpiryDate());
                }
            }

            // Check quantity
            if (quantityToAllocate > res.getQuantity()) {
                throw new InsufficientResourceException(res.getResourceName(), quantityToAllocate, res.getQuantity());
            }

            // Atomically deduct inventory
            boolean deducted = resourceDAO.deductQuantity(resourceId, quantityToAllocate);
            if (!deducted) {
                throw new InsufficientResourceException(res.getResourceName(), quantityToAllocate, res.getQuantity());
            }

            // Update Vehicle availability
            if (vehicleId != null && vehicleId > 0) {
                vehicleDAO.updateStatus(vehicleId, "IN_TRANSIT");
            }

            // Update Volunteer availability
            if (volunteerId != null && volunteerId > 0) {
                volunteerDAO.updateStatus(volunteerId, "ASSIGNED");
            }

            // Create Allocation
            Allocation alloc = new Allocation();
            alloc.setRequestId(requestId);
            alloc.setResourceId(resourceId);
            alloc.setAllocatedQuantity(quantityToAllocate);
            alloc.setVolunteerId(volunteerId);
            alloc.setVehicleId(vehicleId);
            alloc.setStatus("PREPARING");
            alloc.setTrackingNotes("Supplies allocated and prepped at warehouse. Awaiting carrier departure.");

            int allocId = allocationDAO.create(alloc);

            // Update Request status
            requestDAO.updateStatus(requestId, "ALLOCATED");

            // Log operation
            String logMsg = String.format("Allocated %d %s of '%s' to Request #%d (Shelter: %s). Assigned Volunteer #%s, Vehicle #%s",
                    quantityToAllocate, res.getUnit(), res.getResourceName(), requestId,
                    req.getShelterName(), volunteerId, vehicleId);
            FileLogger.logAllocation(logMsg);

            // Launch background delivery tracking thread
            ThreadManager.executeTask(new DeliveryTrackingWorker(allocId));

            return allocId;

        } catch (SQLException e) {
            throw new DatabaseException("Database error during allocation transaction: " + e.getMessage(), e);
        } finally {
            INVENTORY_LOCK.unlock();
        }
    }

    /**
     * Automated Matching & Allocation of the highest priority pending request.
     * Demonstrates integration of PriorityQueue, Concurrency Lock, and Multi-entity matching.
     */
    public Allocation autoAllocateTopPriorityRequest() throws DisasterReliefException {
        RequestService requestService = new RequestService();
        PriorityQueue<ResourceRequest> queue = requestService.getPendingRequestsPriorityQueue();

        if (queue.isEmpty()) {
            throw new DisasterReliefException("No pending requests found in the PriorityQueue to allocate.");
        }

        ResourceRequest topRequest = queue.poll();
        if (topRequest == null) {
            throw new DisasterReliefException("No suitable request available.");
        }

        // Find available resource matching type
        try {
            List<Resource> availableResources = resourceDAO.findByType(topRequest.getResourceType());
            if (availableResources.isEmpty()) {
                throw new InsufficientResourceException(topRequest.getResourceType(), topRequest.getQuantity(), 0);
            }
            Resource matchedResource = availableResources.get(0);

            // Find available vehicle
            List<Vehicle> availableVehicles = vehicleDAO.findAvailable();
            Integer vehicleId = availableVehicles.isEmpty() ? null : availableVehicles.get(0).getVehicleId();

            // Find available volunteer
            List<Volunteer> availableVolunteers = volunteerDAO.findAvailable();
            Integer volunteerId = availableVolunteers.isEmpty() ? null : availableVolunteers.get(0).getVolunteerId();

            int quantity = Math.min(topRequest.getQuantity(), matchedResource.getQuantity());
            int allocId = allocateResource(topRequest.getRequestId(), matchedResource.getResourceId(), quantity, volunteerId, vehicleId);

            return getAllocationById(allocId);

        } catch (SQLException e) {
            throw new DatabaseException("Failed during automated allocation: " + e.getMessage(), e);
        }
    }
}
