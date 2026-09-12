package com.disasterrelief.servlet;

import com.disasterrelief.model.Allocation;
import com.disasterrelief.model.DeliveryStatus;
import com.disasterrelief.service.AllocationService;
import com.disasterrelief.service.DeliveryService;
import com.disasterrelief.util.FileLogger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DeliveryServlet", urlPatterns = {"/delivery"})
public class DeliveryServlet extends HttpServlet {

    private final AllocationService allocationService = new AllocationService();
    private final DeliveryService deliveryService = new DeliveryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Allocation> allocations = allocationService.getAllAllocations();
            List<String> deliveryLogs = FileLogger.getRecentLogs("delivery", 15);

            req.setAttribute("allocations", allocations);
            req.setAttribute("statuses", DeliveryStatus.values());
            req.setAttribute("deliveryLogs", deliveryLogs);

            req.getRequestDispatcher("/delivery_tracker.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/delivery_tracker.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int allocationId = Integer.parseInt(req.getParameter("allocationId"));
            String status = req.getParameter("status");
            String notes = req.getParameter("notes");

            deliveryService.updateDeliveryStatus(allocationId, status, notes);
            resp.sendRedirect(req.getContextPath() + "/delivery?msg=updated&allocId=" + allocationId);

        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error updating delivery: " + e.getMessage());
            doGet(req, resp);
        }
    }
}
