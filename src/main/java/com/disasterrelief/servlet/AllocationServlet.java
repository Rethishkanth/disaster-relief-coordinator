package com.disasterrelief.servlet;

import com.disasterrelief.model.Allocation;
import com.disasterrelief.model.Resource;
import com.disasterrelief.model.ResourceRequest;
import com.disasterrelief.model.Vehicle;
import com.disasterrelief.model.Volunteer;
import com.disasterrelief.service.AllocationService;
import com.disasterrelief.service.RequestService;
import com.disasterrelief.service.ResourceService;
import com.disasterrelief.service.VehicleService;
import com.disasterrelief.service.VolunteerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AllocationServlet", urlPatterns = {"/allocations"})
public class AllocationServlet extends HttpServlet {

    private final AllocationService allocationService = new AllocationService();
    private final RequestService requestService = new RequestService();
    private final ResourceService resourceService = new ResourceService();
    private final VolunteerService volunteerService = new VolunteerService();
    private final VehicleService vehicleService = new VehicleService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Allocation> allocations = allocationService.getAllAllocations();
            List<ResourceRequest> pendingRequests = requestService.getPrioritizedPendingRequests();
            List<Resource> resources = resourceService.getAllResources();
            List<Volunteer> availableVolunteers = volunteerService.getAvailableVolunteers();
            List<Vehicle> availableVehicles = vehicleService.getAvailableVehicles();

            req.setAttribute("allocations", allocations);
            req.setAttribute("pendingRequests", pendingRequests);
            req.setAttribute("resources", resources);
            req.setAttribute("availableVolunteers", availableVolunteers);
            req.setAttribute("availableVehicles", availableVehicles);

            req.getRequestDispatcher("/allocations.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/allocations.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("auto_allocate".equalsIgnoreCase(action)) {
                Allocation autoAlloc = allocationService.autoAllocateTopPriorityRequest();
                resp.sendRedirect(req.getContextPath() + "/allocations?msg=auto_allocated&allocId=" + autoAlloc.getAllocationId());
                return;
            }

            // Manual Allocation
            int requestId = Integer.parseInt(req.getParameter("requestId"));
            int resourceId = Integer.parseInt(req.getParameter("resourceId"));
            int quantity = Integer.parseInt(req.getParameter("quantity"));

            String volIdStr = req.getParameter("volunteerId");
            Integer volunteerId = (volIdStr != null && !volIdStr.isEmpty()) ? Integer.parseInt(volIdStr) : null;

            String vehIdStr = req.getParameter("vehicleId");
            Integer vehicleId = (vehIdStr != null && !vehIdStr.isEmpty()) ? Integer.parseInt(vehIdStr) : null;

            int allocId = allocationService.allocateResource(requestId, resourceId, quantity, volunteerId, vehicleId);
            resp.sendRedirect(req.getContextPath() + "/allocations?msg=allocated&allocId=" + allocId);

        } catch (Exception e) {
            req.setAttribute("errorMessage", "Allocation Failed: " + e.getMessage());
            doGet(req, resp);
        }
    }
}
