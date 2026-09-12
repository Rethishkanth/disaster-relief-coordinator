package com.disasterrelief.thread;

import com.disasterrelief.util.FileLogger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Background worker for asynchronous file logging.
 * Demonstrates:
 * - Multithreading & Producer-Consumer Pattern
 * - BlockingQueue for thread-safe cross-thread message passing
 */
public class AsyncFileLoggingWorker implements Runnable {

    public static class LogTask {
        public final String type; // "SYSTEM", "ALLOCATION", "DELIVERY"
        public final String level;
        public final String module;
        public final String message;

        public LogTask(String type, String level, String module, String message) {
            this.type = type;
            this.level = level;
            this.module = module;
            this.message = message;
        }
    }

    private final BlockingQueue<LogTask> queue = new LinkedBlockingQueue<>();
    private volatile boolean active = true;

    public void enqueue(LogTask task) {
        if (task != null) {
            queue.offer(task);
        }
    }

    public void stop() {
        active = false;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("AsyncLogger-Worker");
        while (active || !queue.isEmpty()) {
            try {
                LogTask task = queue.poll(500, java.util.concurrent.TimeUnit.MILLISECONDS);
                if (task != null) {
                    if ("ALLOCATION".equalsIgnoreCase(task.type)) {
                        FileLogger.logAllocation(task.message);
                    } else if ("DELIVERY".equalsIgnoreCase(task.type)) {
                        FileLogger.logDelivery(task.message);
                    } else {
                        FileLogger.logSystem(task.level != null ? task.level : "INFO",
                                task.module != null ? task.module : "APP",
                                task.message);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
