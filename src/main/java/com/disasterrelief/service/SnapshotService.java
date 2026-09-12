package com.disasterrelief.service;

import com.disasterrelief.dao.AllocationDAO;
import com.disasterrelief.dao.DisasterDAO;
import com.disasterrelief.dao.RequestDAO;
import com.disasterrelief.dao.ResourceDAO;
import com.disasterrelief.dao.ShelterDAO;
import com.disasterrelief.dao.VehicleDAO;
import com.disasterrelief.dao.VolunteerDAO;
import com.disasterrelief.model.SystemSnapshot;
import com.disasterrelief.util.FileLogger;
import com.disasterrelief.util.SerializationUtil;

import java.io.IOException;
import java.util.List;

/**
 * Service managing Disaster Relief System State Snapshots using Java Object Serialization.
 */
public class SnapshotService {

    private final DisasterDAO disasterDAO = new DisasterDAO();
    private final ShelterDAO shelterDAO = new ShelterDAO();
    private final ResourceDAO resourceDAO = new ResourceDAO();
    private final RequestDAO requestDAO = new RequestDAO();
    private final AllocationDAO allocationDAO = new AllocationDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final VolunteerDAO volunteerDAO = new VolunteerDAO();

    public String createAndSaveSnapshot(String description) throws Exception {
        SystemSnapshot snapshot = new SystemSnapshot(description);

        snapshot.setDisasters(disasterDAO.findAll());
        snapshot.setShelters(shelterDAO.findAll());
        snapshot.setResources(resourceDAO.findAll());
        snapshot.setRequests(requestDAO.findAll());
        snapshot.setAllocations(allocationDAO.findAll());
        snapshot.setVehicles(vehicleDAO.findAll());
        snapshot.setVolunteers(volunteerDAO.findAll());

        return SerializationUtil.saveSnapshot(snapshot);
    }

    public SystemSnapshot loadSnapshot(String fileName) throws IOException, ClassNotFoundException {
        return SerializationUtil.loadSnapshot(fileName);
    }

    public List<String> listAvailableSnapshots() {
        return SerializationUtil.listSnapshots();
    }
}
