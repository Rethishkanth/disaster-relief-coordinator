package com.disasterrelief.util;

import com.disasterrelief.model.SystemSnapshot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Object Serialization Utility.
 * Demonstrates:
 * - Java Serialization via ObjectOutputStream and ObjectInputStream
 * - Saving application state to .ser files and restoring operational data
 */
public class SerializationUtil {

    private static final String SNAPSHOT_DIR = "snapshots";
    private static final SimpleDateFormat FILE_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss");

    static {
        File dir = new File(SNAPSHOT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Serializes the system state object to a .ser file on disk.
     */
    public static synchronized String saveSnapshot(SystemSnapshot snapshot) throws IOException {
        if (snapshot == null) {
            throw new IllegalArgumentException("Snapshot object cannot be null");
        }

        String fileName = "snapshot_" + FILE_FORMAT.format(new Date()) + ".ser";
        File targetFile = new File(SNAPSHOT_DIR + File.separator + fileName);

        try (FileOutputStream fos = new FileOutputStream(targetFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(snapshot);
            oos.flush();
        }

        FileLogger.logSystem("INFO", "SERIALIZATION",
                "System snapshot successfully saved to file: " + targetFile.getName());
        return targetFile.getName();
    }

    /**
     * Deserializes a SystemSnapshot object from a .ser file on disk.
     */
    public static synchronized SystemSnapshot loadSnapshot(String fileName) throws IOException, ClassNotFoundException {
        File sourceFile = new File(SNAPSHOT_DIR + File.separator + fileName);
        if (!sourceFile.exists()) {
            throw new IOException("Snapshot file not found: " + fileName);
        }

        try (FileInputStream fis = new FileInputStream(sourceFile);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            SystemSnapshot snapshot = (SystemSnapshot) ois.readObject();
            FileLogger.logSystem("INFO", "SERIALIZATION",
                    "System snapshot successfully restored from file: " + fileName);
            return snapshot;
        }
    }

    /**
     * Lists all saved .ser snapshot files.
     */
    public static List<String> listSnapshots() {
        List<String> filesList = new ArrayList<>();
        File dir = new File(SNAPSHOT_DIR);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".ser"));
            if (files != null) {
                for (File f : files) {
                    filesList.add(f.getName());
                }
            }
        }
        return filesList;
    }
}
