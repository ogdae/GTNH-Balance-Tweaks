package com.rilliko.gtnhbalancetweaks.core;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GTNHBalanceTweaksLogger {

    private static final Logger logger = Logger.getLogger("GTNHBalanceTweaks");
    private static FileHandler fileHandler;

    static {
        try {
            // Ensure logs folder exists
            File logDir = new File("logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            // Create log file in ./logs/GTNHBalanceTweaks.log
            fileHandler = new FileHandler("logs/GTNHBalanceTweaks.log", true);
            fileHandler.setFormatter(new SimpleFormatter());

            // Attach handler and configure levels
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            fileHandler.setLevel(Level.ALL);

            // If you ONLY want file output, leave this false.
            // If you also want console output, set to true.
            logger.setUseParentHandlers(false);

            logger.info(
                "[GTNHBalanceTweaks] Logger initialized at "
                    + new File("logs/GTNHBalanceTweaks.log").getAbsolutePath());

            // Clean shutdown hook to flush/close handler
            Runtime.getRuntime()
                .addShutdownHook(new Thread(() -> {
                    fileHandler.flush();
                    fileHandler.close();
                }));

        } catch (IOException e) {
            System.err.println("[GTNHBalanceTweaks] Failed to init logger: " + e.getMessage());
        }
    }

    // === INFO ===
    public static void info(String message) {
        logger.log(Level.INFO, message);
    }

    // === WARN ===
    public static void warn(String message) {
        logger.log(Level.WARNING, message);
    }

    public static void warn(String message, Throwable t) {
        logger.log(Level.WARNING, message, t);
    }

    // === ERROR ===
    public static void error(String message) {
        logger.log(Level.SEVERE, message);
    }

    public static void error(String message, Throwable t) {
        logger.log(Level.SEVERE, message, t);
    }
}
