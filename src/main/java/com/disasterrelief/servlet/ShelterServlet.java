package com.disasterrelief.servlet;

import com.disasterrelief.model.Disaster;
import com.disasterrelief.model.Shelter;
import com.disasterrelief.service.DisasterService;
import com.disasterrelief.service.ShelterService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ShelterServlet", urlPatterns = {"/shelters"})
public class ShelterServlet extends HttpServlet {

    private final ShelterService shelterService = new ShelterService();
    private final DisasterService disasterService = new DisasterService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Shelter> shelters = shelterService.getAllShelters();
            List<Disaster> disasters = disasterService.getAllDisasters();
            req.setAttribute("shelters", shelters);
            req.setAttribute("disasters", disasters);
            req.getRequestDispatcher("/shelters.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/shelters.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("update_population".equalsIgnoreCase(action)) {
                int shelterId = Integer.parseInt(req.getParameter("shelterId"));
                int currentPopulation = Integer.parseInt(req.getParameter("currentPopulation"));
                String status = req.getParameter("status");

                Shelter s = shelterService.getShelterById(shelterId);
                s.setCurrentPopulation(currentPopulation);
                if (status != null && !status.isEmpty()) {
                    s.setStatus(status);
                }
                shelterService.updateShelter(s);
                resp.sendRedirect(req.getContextPath() + "/shelters?msg=updated");
                return;
            }

            // Create shelter
            String name = req.getParameter("name");
            String location = req.getParameter("location");
            int capacity = Integer.parseInt(req.getParameter("capacity"));
            int population = Integer.parseInt(req.getParameter("currentPopulation"));
            String contact = req.getParameter("contact");
            String disasterIdStr = req.getParameter("disasterId");
            Integer disasterId = (disasterIdStr != null && !disasterIdStr.isEmpty()) ? Integer.parseInt(disasterIdStr) : null;

            Shelter shelter = new Shelter(0, disasterId, null, name, location, capacity, population, contact, "OPERATIONAL");
            shelterService.registerShelter(shelter);
            resp.sendRedirect(req.getContextPath() + "/shelters?msg=registered");

        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error saving shelter: " + e.getMessage());
            doGet(req, resp);
        }
    }
}
