package com.disasterrelief.servlet;

import com.disasterrelief.model.Allocation;
import com.disasterrelief.model.Disaster;
import com.disasterrelief.model.Resource;
import com.disasterrelief.model.ResourceRequest;
import com.disasterrelief.model.Shelter;
import com.disasterrelief.model.User;
import com.disasterrelief.model.Vehicle;
import com.disasterrelief.model.Volunteer;
import com.disasterrelief.service.AllocationService;
import com.disasterrelief.service.DisasterService;
import com.disasterrelief.service.RequestService;
import com.disasterrelief.service.ResourceService;
import com.disasterrelief.service.ShelterService;
import com.disasterrelief.service.VehicleService;
import com.disasterrelief.service.VolunteerService;
import com.disasterrelief.util.DBConnection;
import com.disasterrelief.util.FileLogger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard", ""})
public class DashboardServlet extends HttpServlet {

    private final DisasterService disasterService = new DisasterService();
    private final ShelterService shelterService = new ShelterService();
    private final ResourceService resourceService = new ResourceService();
    private final RequestService requestService = new RequestService();
    private final AllocationService allocationService = new AllocationService();
    private final VolunteerService volunteerService = new VolunteerService();
    private final VehicleService vehicleService = new VehicleService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        try {
            List<Disaster> disasters = disasterService.getAllDisasters();
            List<Shelter> shelters = shelterService.getAllShelters();
            List<Resource> resources = resourceService.getAllResources();
            List<ResourceRequest> pendingRequests = requestService.getPrioritizedPendingRequests();
            List<Allocation> allocations = allocationService.getAllAllocations();
            List<Volunteer> availableVolunteers = volunteerService.getAvailableVolunteers();
            List<Vehicle> availableVehicles = vehicleService.getAvailableVehicles();

            int totalSheltered = 0;
            int totalCapacity = 0;
            for (Shelter s : shelters) {
                totalSheltered += s.getCurrentPopulation();
                totalCapacity += s.getCapacity();
            }

            int totalSuppliesCount = 0;
            for (Resource r : resources) {
                totalSuppliesCount += r.getQuantity();
            }

            long activeDeliveriesCount = allocations.stream()
                    .filter(a -> "DISPATCHED".equalsIgnoreCase(a.getStatus()) || "IN_TRANSIT".equalsIgnoreCase(a.getStatus()))
                    .count();

            req.setAttribute("disasters", disasters);
            req.setAttribute("shelters", shelters);
            req.setAttribute("resources", resources);
            req.setAttribute("pendingRequests", pendingRequests);
            req.setAttribute("allocations", allocations);
            req.setAttribute("availableVolunteersCount", availableVolunteers.size());
            req.setAttribute("availableVehiclesCount", availableVehicles.size());
            req.setAttribute("totalSheltered", totalSheltered);
            req.setAttribute("totalCapacity", totalCapacity);
            req.setAttribute("totalSuppliesCount", totalSuppliesCount);
            req.setAttribute("activeDeliveriesCount", activeDeliveriesCount);
            req.setAttribute("databaseMode", DBConnection.getDatabaseType());
            req.setAttribute("recentLogs", FileLogger.getRecentLogs("system", 6));

            req.getRequestDispatcher("/dashboard.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading dashboard metrics: " + e.getMessage());
            req.getRequestDispatcher("/dashboard.jsp").forward(req, resp);
        }
    }
}
