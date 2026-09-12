package com.disasterrelief.servlet;

import com.disasterrelief.model.Disaster;
import com.disasterrelief.service.DisasterService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DisasterServlet", urlPatterns = {"/disasters"})
public class DisasterServlet extends HttpServlet {

    private final DisasterService disasterService = new DisasterService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Disaster> disasters = disasterService.getAllDisasters();
            req.setAttribute("disasters", disasters);
            req.getRequestDispatcher("/disasters.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/disasters.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String disasterType = req.getParameter("disasterType");
        String location = req.getParameter("location");
        String severity = req.getParameter("severity");
        String status = req.getParameter("status");
        String description = req.getParameter("description");

        try {
            Disaster d = new Disaster(0, name, disasterType, location, severity, status != null ? status : "ACTIVE", description);
            disasterService.registerDisaster(d);
            resp.sendRedirect(req.getContextPath() + "/disasters?msg=created");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Could not register disaster: " + e.getMessage());
            doGet(req, resp);
        }
    }
}
