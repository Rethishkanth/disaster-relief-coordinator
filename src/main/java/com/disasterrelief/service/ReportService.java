package com.disasterrelief.service;

import com.disasterrelief.dao.AllocationDAO;
import com.disasterrelief.dao.RequestDAO;
import com.disasterrelief.dao.ResourceDAO;
import com.disasterrelief.dao.ShelterDAO;
import com.disasterrelief.model.Allocation;
import com.disasterrelief.model.Resource;
import com.disasterrelief.model.ResourceRequest;
import com.disasterrelief.model.Shelter;
import com.disasterrelief.util.FileLogger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Report Generation Service.
 * Demonstrates:
 * - Java File Handling & I/O (FileWriter, PrintWriter)
 * - Structured export of operational summaries in CSV and TXT formats
 */
public class ReportService {

    private static final String REPORT_DIR = "reports";
    private static final SimpleDateFormat TS_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss");
    private static final SimpleDateFormat READABLE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        File dir = new File(REPORT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private final AllocationDAO allocationDAO = new AllocationDAO();
    private final ResourceDAO resourceDAO = new ResourceDAO();
    private final ShelterDAO shelterDAO = new ShelterDAO();
    private final RequestDAO requestDAO = new RequestDAO();

    /**
     * Generates a CSV file containing all allocations.
     */
    public String generateAllocationCSVReport() throws IOException {
        String fileName = "allocations_" + TS_FORMAT.format(new Date()) + ".csv";
        File file = new File(REPORT_DIR + File.separator + fileName);

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("AllocationID,RequestID,ResourceName,ResourceType,Quantity,Unit,ShelterName,Volunteer,Vehicle,Status,AllocationDate");
            List<Allocation> list = allocationDAO.findAll();
            for (Allocation a : list) {
                writer.printf("%d,%d,\"%s\",\"%s\",%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                        a.getAllocationId(),
                        a.getRequestId(),
                        escapeCsv(a.getResourceName()),
                        escapeCsv(a.getResourceType()),
                        a.getAllocatedQuantity(),
                        escapeCsv(a.getUnit()),
                        escapeCsv(a.getShelterName()),
                        escapeCsv(a.getVolunteerName() != null ? a.getVolunteerName() : "Unassigned"),
                        escapeCsv(a.getVehicleNumber() != null ? a.getVehicleNumber() : "None"),
                        a.getStatus(),
                        a.getAllocationDate() != null ? a.getAllocationDate().toString() : "");
            }
        } catch (Exception e) {
            throw new IOException("Error creating allocation CSV: " + e.getMessage(), e);
        }

        FileLogger.logSystem("INFO", "REPORT", "Generated CSV report: " + file.getName());
        return file.getName();
    }

    /**
     * Generates a text operational briefing report.
     */
    public String generateOperationalBriefingTxt() throws IOException {
        String fileName = "briefing_" + TS_FORMAT.format(new Date()) + ".txt";
        File file = new File(REPORT_DIR + File.separator + fileName);

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("================================================================================");
            writer.println("             DISASTER RELIEF OPERATIONS - EXECUTIVE BRIEFING REPORT             ");
            writer.println("             Generated: " + READABLE_FORMAT.format(new Date()));
            writer.println("================================================================================");
            writer.println();

            List<Shelter> shelters = shelterDAO.findAll();
            writer.println("--- 1. ACTIVE SHELTERS STATUS ---");
            int totalCap = 0, totalPop = 0;
            for (Shelter s : shelters) {
                totalCap += s.getCapacity();
                totalPop += s.getCurrentPopulation();
                writer.printf("• [%s] %s | Pop: %d/%d (%.1f%%) | Status: %s | Contact: %s%n",
                        s.getDisasterName() != null ? s.getDisasterName() : "General",
                        s.getName(), s.getCurrentPopulation(), s.getCapacity(),
                        s.getOccupancyRate(), s.getStatus(), s.getContact());
            }
            writer.printf("Total Shelters: %d | Total Evacuees Sheltered: %d / %d capacity%n%n",
                    shelters.size(), totalPop, totalCap);

            List<Resource> resources = resourceDAO.findAll();
            writer.println("--- 2. CRITICAL WAREHOUSE INVENTORY ---");
            for (Resource r : resources) {
                writer.printf("• %-30s | %-12s | Qty: %5d %-12s | Status: %s%n",
                        r.getResourceName(), r.getResourceType(), r.getQuantity(), r.getUnit(), r.getAvailabilityStatus());
            }
            writer.println();

            List<ResourceRequest> pending = requestDAO.findPending();
            writer.println("--- 3. URGENT PENDING REQUESTS (PRIORITY RANKED) ---");
            for (ResourceRequest req : pending) {
                writer.printf("• [Priority Score: %5.1f] [Urgency: %-8s] Shelter: %-32s | Needs: %d %s %s%n",
                        req.getPriorityScore(), req.getUrgency(), req.getShelterName(),
                        req.getQuantity(), req.getUnit(), req.getResourceType());
            }
            writer.println();
            writer.println("================================================================================");
            writer.println("                       END OF OPERATIONAL BRIEFING                              ");
            writer.println("================================================================================");
        } catch (Exception e) {
            throw new IOException("Error creating briefing TXT: " + e.getMessage(), e);
        }

        FileLogger.logSystem("INFO", "REPORT", "Generated briefing TXT report: " + file.getName());
        return file.getName();
    }

    public File getReportFile(String fileName) {
        return new File(REPORT_DIR + File.separator + fileName);
    }

    private String escapeCsv(String val) {
        if (val == null) return "";
        return val.replace("\"", "\"\"");
    }
}
