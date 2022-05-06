package com.nashobarobotics.nashobalib;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.DataLogManager;

public class Logger {
    private static boolean doDataLogging = false;
    private static boolean verbose = false;
    private static boolean criticalError = false;

    public static void enableDataLogging() {
        doDataLogging = true;
    }

    public static void enableVerboseLogging() {
        verbose = true;
    }

    public static enum Level {
        DEBUG, INFO, WARN, ERROR, CRITICAL
    }

    public static void debug(String msg) {
        logImpl(Level.DEBUG, msg);
    }
    public static void info(String msg) {
        logImpl(Level.INFO, msg);
    }
    public static void warn(String msg) {
        logImpl(Level.WARN, msg);
    }
    public static void error(String msg) {
        logImpl(Level.ERROR, msg);
    }
    public static void critical(String msg) {
        logImpl(Level.CRITICAL, msg);
    }

    private static void logImpl(Level lvl, String msg) {
        if(lvl == Level.DEBUG && !verbose) return;
        if(doDataLogging) {
            DataLogManager.log("[" + lvl + "] " + msg);
        }
        if(lvl == Level.WARN || lvl == Level.ERROR || lvl == Level.CRITICAL) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            StackTraceElement lastStackFrame = stackTrace[3];
            String location = lastStackFrame.toString();
            boolean isError = lvl == (Level.ERROR);
            HAL.sendError(isError, 1, false, msg, location, "", true);
        }
        if(lvl == Level.CRITICAL) {
            criticalError = true;
        }
    }

    public static boolean hasCriticalError() {
        return criticalError;
    }
}
