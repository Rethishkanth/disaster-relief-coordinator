package com.disasterrelief.servlet;

import com.disasterrelief.service.ReportService;
import com.disasterrelief.util.FileLogger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@WebServlet(name = "ReportServlet", urlPatterns = {"/reports"})
public class ReportServlet extends HttpServlet {

    private final ReportService reportService = new ReportService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String downloadFile = req.getParameter("download");
        if (downloadFile != null && !downloadFile.trim().isEmpty()) {
            handleDownload(downloadFile, resp);
            return;
        }

        List<String> systemLogs = FileLogger.getRecentLogs("system", 20);
        List<String> allocationLogs = FileLogger.getRecentLogs("allocation", 20);
        List<String> deliveryLogs = FileLogger.getRecentLogs("delivery", 20);

        req.setAttribute("systemLogs", systemLogs);
        req.setAttribute("allocationLogs", allocationLogs);
        req.setAttribute("deliveryLogs", deliveryLogs);

        req.getRequestDispatcher("/reports.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");

        try {
            if ("csv".equalsIgnoreCase(type)) {
                String fileName = reportService.generateAllocationCSVReport();
                resp.sendRedirect(req.getContextPath() + "/reports?msg=generated&download=" + fileName);
                return;
            } else if ("txt".equalsIgnoreCase(type)) {
                String fileName = reportService.generateOperationalBriefingTxt();
                resp.sendRedirect(req.getContextPath() + "/reports?msg=generated&download=" + fileName);
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/reports");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error generating report: " + e.getMessage());
            doGet(req, resp);
        }
    }

    private void handleDownload(String fileName, HttpServletResponse resp) throws IOException {
        // Prevent path traversal
        File file = reportService.getReportFile(new File(fileName).getName());
        if (!file.exists()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Report file not found: " + fileName);
            return;
        }

        String mime = fileName.endsWith(".csv") ? "text/csv" : "text/plain";
        resp.setContentType(mime);
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        resp.setContentLengthLong(file.length());

        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = resp.getOutputStream()) {
            byte[] buf = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buf)) != -1) {
                os.write(buf, 0, bytesRead);
            }
        }
    }
}
