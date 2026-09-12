package com.disasterrelief.servlet;

import com.disasterrelief.model.SystemSnapshot;
import com.disasterrelief.service.SnapshotService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "BackupServlet", urlPatterns = {"/backups"})
public class BackupServlet extends HttpServlet {

    private final SnapshotService snapshotService = new SnapshotService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> snapshots = snapshotService.listAvailableSnapshots();
        req.setAttribute("snapshots", snapshots);
        req.getRequestDispatcher("/backups.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("create".equalsIgnoreCase(action)) {
                String desc = req.getParameter("description");
                if (desc == null || desc.trim().isEmpty()) {
                    desc = "Manual snapshot taken by Coordinator";
                }
                String fileName = snapshotService.createAndSaveSnapshot(desc);
                resp.sendRedirect(req.getContextPath() + "/backups?msg=created&file=" + fileName);
                return;
            } else if ("restore".equalsIgnoreCase(action)) {
                String fileName = req.getParameter("fileName");
                SystemSnapshot snap = snapshotService.loadSnapshot(fileName);
                req.setAttribute("restoredSnapshot", snap);
                req.setAttribute("successMessage", "Snapshot " + fileName + " successfully loaded and validated in memory!");
                doGet(req, resp);
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/backups");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Serialization error: " + e.getMessage());
            doGet(req, resp);
        }
    }
}
