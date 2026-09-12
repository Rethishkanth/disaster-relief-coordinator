package com.disasterrelief.servlet;

import com.disasterrelief.model.Resource;
import com.disasterrelief.service.ResourceService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ResourceServlet", urlPatterns = {"/resources"})
public class ResourceServlet extends HttpServlet {

    private final ResourceService resourceService = new ResourceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Resource> resources = resourceService.getAllResources();
            Map<String, List<Resource>> groupedResources = resourceService.getResourcesGroupedByType();

            req.setAttribute("resources", resources);
            req.setAttribute("groupedResources", groupedResources);
            req.getRequestDispatcher("/resources.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/resources.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("restock".equalsIgnoreCase(action)) {
                int resourceId = Integer.parseInt(req.getParameter("resourceId"));
                int addQty = Integer.parseInt(req.getParameter("addQuantity"));

                Resource res = resourceService.getResourceById(resourceId);
                res.setQuantity(res.getQuantity() + addQty);
                resourceService.updateResource(res);
                resp.sendRedirect(req.getContextPath() + "/resources?msg=restocked");
                return;
            }

            // Register new resource
            String resourceName = req.getParameter("resourceName");
            String resourceType = req.getParameter("resourceType");
            int quantity = Integer.parseInt(req.getParameter("quantity"));
            String unit = req.getParameter("unit");
            String expiryDate = req.getParameter("expiryDate");
            String description = req.getParameter("description");
            String orgIdStr = req.getParameter("organizationId");
            Integer organizationId = (orgIdStr != null && !orgIdStr.isEmpty()) ? Integer.parseInt(orgIdStr) : 1;

            Resource r = new Resource(0, organizationId, resourceName, resourceType, quantity, unit,
                    expiryDate != null ? expiryDate : "N/A", "AVAILABLE", description);
            resourceService.registerResource(r);
            resp.sendRedirect(req.getContextPath() + "/resources?msg=registered");

        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error saving resource: " + e.getMessage());
            doGet(req, resp);
        }
    }
}
