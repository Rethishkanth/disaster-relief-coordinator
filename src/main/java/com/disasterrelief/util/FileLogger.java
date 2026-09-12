package com.disasterrelief.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * File I/O Logging Utility.
 * Demonstrates:
 * - Java File Handling & Streams (BufferedWriter, FileWriter, BufferedReader, FileReader)
 * - Persistent operational logging outside the database
 */
public class FileLogger {

    private static final String LOG_DIR = "logs";
    private static final String SYSTEM_LOG = LOG_DIR + File.separator + "system_activity.log";
    private static final String ALLOCATION_LOG = LOG_DIR + File.separator + "allocations.log";
    private static final String DELIVERY_LOG = LOG_DIR + File.separator + "delivery_tracking.log";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        File dir = new File(LOG_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static synchronized void logSystem(String level, String module, String message) {
        writeLog(SYSTEM_LOG, String.format("[%s] [%s] [%s] %s", getTimestamp(), level, module, message));
    }

    public static synchronized void logAllocation(String message) {
        writeLog(ALLOCATION_LOG, String.format("[%s] [ALLOCATION] %s", getTimestamp(), message));
    }

    public static synchronized void logDelivery(String message) {
        writeLog(DELIVERY_LOG, String.format("[%s] [DELIVERY] %s", getTimestamp(), message));
    }

    private static void writeLog(String filePath, String formattedLine) {
        try (FileWriter fw = new FileWriter(filePath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(formattedLine);
        } catch (IOException e) {
            System.err.println("[FileLogger] Error writing to " + filePath + ": " + e.getMessage());
        }
    }

    /**
     * Reads the last N lines from a specified log file.
     * Demonstrates FileReader and BufferedReader usage.
     */
    public static List<String> getRecentLogs(String logType, int limit) {
        String path = SYSTEM_LOG;
        if ("allocation".equalsIgnoreCase(logType)) {
            path = ALLOCATION_LOG;
        } else if ("delivery".equalsIgnoreCase(logType)) {
            path = DELIVERY_LOG;
        }

        File file = new File(path);
        if (!file.exists()) {
            return Collections.emptyList();
        }

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            lines.add("Error reading log file: " + e.getMessage());
        }

        // Return recent lines (reverse order)
        int size = lines.size();
        int startIndex = Math.max(0, size - limit);
        List<String> recent = new ArrayList<>(lines.subList(startIndex, size));
        Collections.reverse(recent);
        return recent;
    }

    private static String getTimestamp() {
        return DATE_FORMAT.format(new Date());
    }
}
