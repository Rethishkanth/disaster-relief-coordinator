package com.disasterrelief.servlet;

import com.disasterrelief.model.Vehicle;
import com.disasterrelief.service.VehicleService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "VehicleServlet", urlPatterns = {"/vehicles"})
public class VehicleServlet extends HttpServlet {

    private final VehicleService vehicleService = new VehicleService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Vehicle> vehicles = vehicleService.getAllVehicles();
            req.setAttribute("vehicles", vehicles);
            req.getRequestDispatcher("/vehicles.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/vehicles.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("update_status".equalsIgnoreCase(action)) {
                int vehicleId = Integer.parseInt(req.getParameter("vehicleId"));
                String status = req.getParameter("status");
                vehicleService.updateStatus(vehicleId, status);
                resp.sendRedirect(req.getContextPath() + "/vehicles?msg=status_updated");
                return;
            }

            // Register vehicle
            String vehicleNumber = req.getParameter("vehicleNumber");
            String vehicleType = req.getParameter("vehicleType");
            int capacityKg = Integer.parseInt(req.getParameter("capacityKg"));
            String driverName = req.getParameter("driverName");
            String contact = req.getParameter("contact");

            Vehicle v = new Vehicle(0, vehicleNumber, vehicleType, capacityKg, driverName, contact, "AVAILABLE");
            vehicleService.registerVehicle(v);
            resp.sendRedirect(req.getContextPath() + "/vehicles?msg=registered");

        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error saving vehicle: " + e.getMessage());
            doGet(req, resp);
        }
    }
}
