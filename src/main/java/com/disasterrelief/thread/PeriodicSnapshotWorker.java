package com.disasterrelief.thread;

import com.disasterrelief.service.SnapshotService;
import com.disasterrelief.util.FileLogger;

/**
 * Scheduled background worker that periodically creates a full system state snapshot
 * using Java Serialization.
 */
public class PeriodicSnapshotWorker implements Runnable {

    private final SnapshotService snapshotService = new SnapshotService();

    @Override
    public void run() {
        try {
            String snapshotFile = snapshotService.createAndSaveSnapshot("Automated background periodic snapshot");
            FileLogger.logSystem("INFO", "AUTO_SNAPSHOT", "Periodic snapshot completed: " + snapshotFile);
        } catch (Exception e) {
            FileLogger.logSystem("WARN", "AUTO_SNAPSHOT", "Failed periodic snapshot: " + e.getMessage());
        }
    }
}
