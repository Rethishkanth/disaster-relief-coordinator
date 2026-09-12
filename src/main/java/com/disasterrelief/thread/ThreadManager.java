package com.disasterrelief.thread;

import com.disasterrelief.util.FileLogger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Centralized Multithreading & Background Worker Manager.
 * Demonstrates:
 * - Multithreading: Thread pools, ExecutorService, ScheduledExecutorService
 * - Safe lifecycle management and graceful shutdown
 */
public class ThreadManager {

    private static final ExecutorService taskExecutor = Executors.newFixedThreadPool(4);
    private static final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(2);
    private static volatile boolean running = true;

    private static AsyncFileLoggingWorker loggingWorker;

    public static synchronized void init() {
        if (loggingWorker == null) {
            loggingWorker = new AsyncFileLoggingWorker();
            taskExecutor.submit(loggingWorker);
            FileLogger.logSystem("INFO", "THREAD_MGR", "Async logging background worker started.");
        }
    }

    public static void executeTask(Runnable runnable) {
        if (running) {
            taskExecutor.submit(runnable);
        }
    }

    public static void schedulePeriodicTask(Runnable runnable, long initialDelaySeconds, long periodSeconds) {
        if (running) {
            scheduledExecutor.scheduleAtFixedRate(runnable, initialDelaySeconds, periodSeconds, TimeUnit.SECONDS);
        }
    }

    public static AsyncFileLoggingWorker getLoggingWorker() {
        return loggingWorker;
    }

    public static synchronized void shutdown() {
        running = false;
        if (loggingWorker != null) {
            loggingWorker.stop();
        }
        taskExecutor.shutdown();
        scheduledExecutor.shutdown();
        try {
            if (!taskExecutor.awaitTermination(3, TimeUnit.SECONDS)) {
                taskExecutor.shutdownNow();
            }
            if (!scheduledExecutor.awaitTermination(3, TimeUnit.SECONDS)) {
                scheduledExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            taskExecutor.shutdownNow();
            scheduledExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        FileLogger.logSystem("INFO", "THREAD_MGR", "All background worker threads shut down cleanly.");
    }
}
