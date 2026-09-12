package com.disasterrelief.service;

import com.disasterrelief.dao.DisasterDAO;
import com.disasterrelief.dao.RequestDAO;
import com.disasterrelief.dao.ShelterDAO;
import com.disasterrelief.exception.DatabaseException;
import com.disasterrelief.exception.InvalidRequestException;
import com.disasterrelief.model.Disaster;
import com.disasterrelief.model.ResourceRequest;
import com.disasterrelief.model.Shelter;
import com.disasterrelief.util.FileLogger;
import com.disasterrelief.util.PriorityCalculator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Service managing Shelter Resource Requests.
 * Demonstrates:
 * - Collections: PriorityQueue<ResourceRequest> for prioritizing emergency requests
 * - Algorithmic scoring formula evaluation
 */
public class RequestService {

    private final RequestDAO requestDAO = new RequestDAO();
    private final ShelterDAO shelterDAO = new ShelterDAO();
    private final DisasterDAO disasterDAO = new DisasterDAO();

    public List<ResourceRequest> getAllRequests() throws DatabaseException {
        try {
            return requestDAO.findAll();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch requests: " + e.getMessage(), e);
        }
    }

    public List<ResourceRequest> getRequestsByShelter(int shelterId) throws DatabaseException {
        try {
            return requestDAO.findByShelterId(shelterId);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch requests for shelter: " + e.getMessage(), e);
        }
    }

    public ResourceRequest getRequestById(int id) throws DatabaseException {
        try {
            return requestDAO.findById(id);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch request #" + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Demonstrates Collections: PriorityQueue
     * Ingests all pending requests into a binary heap PriorityQueue,
     * allowing retrieval in descending order of calculated urgency score.
     */
    public PriorityQueue<ResourceRequest> getPendingRequestsPriorityQueue() throws DatabaseException {
        PriorityQueue<ResourceRequest> queue = new PriorityQueue<>();
        try {
            List<ResourceRequest> pendingList = requestDAO.findPending();
            queue.addAll(pendingList);
            return queue;
        } catch (SQLException e) {
            throw new DatabaseException("Failed loading pending requests into PriorityQueue: " + e.getMessage(), e);
        }
    }

    /**
     * Returns pending requests ordered strictly by priority queue.
     */
    public List<ResourceRequest> getPrioritizedPendingRequests() throws DatabaseException {
        PriorityQueue<ResourceRequest> queue = getPendingRequestsPriorityQueue();
        List<ResourceRequest> sortedList = new ArrayList<>();
        while (!queue.isEmpty()) {
            sortedList.add(queue.poll());
        }
        return sortedList;
    }

    public int submitRequest(ResourceRequest request) throws InvalidRequestException, DatabaseException {
        if (request.getShelterId() <= 0) {
            throw new InvalidRequestException("shelterId", "Valid shelter must be specified.");
        }
        if (request.getQuantity() <= 0) {
            throw new InvalidRequestException("quantity", "Requested quantity must be positive.");
        }
        if (request.getResourceType() == null || request.getResourceType().trim().isEmpty()) {
            throw new InvalidRequestException("resourceType", "Resource type is required.");
        }
        if (request.getUrgency() == null || request.getUrgency().trim().isEmpty()) {
            request.setUrgency("MEDIUM");
        }

        // Calculate Priority Score
        try {
            String severity = "LOW";
            Shelter s = shelterDAO.findById(request.getShelterId());
            if (s != null && s.getDisasterId() != null) {
                Disaster d = disasterDAO.findById(s.getDisasterId());
                if (d != null && d.getSeverity() != null) {
                    severity = d.getSeverity();
                }
            }

            double score = PriorityCalculator.calculateScore(request.getUrgency(), request.getPeopleAffected(),
                    request.getRequestedDate(), severity);
            request.setPriorityScore(score);

            int reqId = requestDAO.create(request);
            FileLogger.logSystem("INFO", "REQUEST",
                    String.format("New request #%d created for shelter #%d: %d %s of %s (Priority Score: %.1f)",
                            reqId, request.getShelterId(), request.getQuantity(), request.getUnit(),
                            request.getResourceType(), score));
            return reqId;
        } catch (SQLException e) {
            throw new DatabaseException("Failed submitting resource request: " + e.getMessage(), e);
        }
    }

    public boolean updateStatus(int requestId, String status) throws DatabaseException {
        try {
            return requestDAO.updateStatus(requestId, status);
        } catch (SQLException e) {
            throw new DatabaseException("Failed updating request status: " + e.getMessage(), e);
        }
    }
}
