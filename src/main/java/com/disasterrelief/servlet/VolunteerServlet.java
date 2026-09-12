package com.disasterrelief.servlet;

import com.disasterrelief.model.Volunteer;
import com.disasterrelief.service.VolunteerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebServlet(name = "VolunteerServlet", urlPatterns = {"/volunteers"})
public class VolunteerServlet extends HttpServlet {

    private final VolunteerService volunteerService = new VolunteerService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Volunteer> volunteers = volunteerService.getAllVolunteers();
            Set<String> uniqueSkills = volunteerService.getUniqueSkillSet();

            req.setAttribute("volunteers", volunteers);
            req.setAttribute("uniqueSkills", uniqueSkills);
            req.getRequestDispatcher("/volunteers.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/volunteers.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("update_status".equalsIgnoreCase(action)) {
                int volunteerId = Integer.parseInt(req.getParameter("volunteerId"));
                String status = req.getParameter("status");
                volunteerService.updateStatus(volunteerId, status);
                resp.sendRedirect(req.getContextPath() + "/volunteers?msg=status_updated");
                return;
            }

            // Register volunteer
            String name = req.getParameter("name");
            String skill = req.getParameter("skill");
            int experienceYears = Integer.parseInt(req.getParameter("experienceYears"));
            String contact = req.getParameter("contact");
            String location = req.getParameter("location");

            Volunteer v = new Volunteer(0, name, name.toLowerCase().replaceAll("\\s+", "") + "@relief.org", "vol123",
                    contact, true, 0, skill, experienceYears, "AVAILABLE", location);
            volunteerService.registerVolunteer(v);
            resp.sendRedirect(req.getContextPath() + "/volunteers?msg=registered");

        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error saving volunteer: " + e.getMessage());
            doGet(req, resp);
        }
    }
}
