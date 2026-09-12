package com.disasterrelief.servlet;

import com.disasterrelief.model.ResourceRequest;
import com.disasterrelief.model.ResourceType;
import com.disasterrelief.model.Shelter;
import com.disasterrelief.service.RequestService;
import com.disasterrelief.service.ShelterService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "RequestServlet", urlPatterns = {"/requests"})
public class RequestServlet extends HttpServlet {

    private final RequestService requestService = new RequestService();
    private final ShelterService shelterService = new ShelterService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<ResourceRequest> allRequests = requestService.getAllRequests();
            List<ResourceRequest> prioritizedPending = requestService.getPrioritizedPendingRequests();
            List<Shelter> shelters = shelterService.getAllShelters();

            req.setAttribute("allRequests", allRequests);
            req.setAttribute("prioritizedPending", prioritizedPending);
            req.setAttribute("shelters", shelters);
            req.setAttribute("resourceTypes", ResourceType.values());

            req.getRequestDispatcher("/requests.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/requests.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int shelterId = Integer.parseInt(req.getParameter("shelterId"));
            String resourceType = req.getParameter("resourceType");
            int quantity = Integer.parseInt(req.getParameter("quantity"));
            String unit = req.getParameter("unit");
            String urgency = req.getParameter("urgency");
            int peopleAffected = Integer.parseInt(req.getParameter("peopleAffected"));
            String requiredDate = req.getParameter("requiredDate");
            String notes = req.getParameter("notes");

            ResourceRequest request = new ResourceRequest();
            request.setShelterId(shelterId);
            request.setResourceType(resourceType);
            request.setQuantity(quantity);
            request.setUnit(unit);
            request.setUrgency(urgency);
            request.setPeopleAffected(peopleAffected);
            request.setRequiredDate(requiredDate);
            request.setNotes(notes);

            requestService.submitRequest(request);
            resp.sendRedirect(req.getContextPath() + "/requests?msg=submitted");

        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error submitting request: " + e.getMessage());
            doGet(req, resp);
        }
    }
}
