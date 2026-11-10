package com.ranamustafa.clinicshub.utils;


import android.util.Log;

public final class AppLogger {

    private static final String TAG = "AppLogger";
    private AppLogger() {
        // Prevent instantiation
    }

    public static void logError(String tag, Throwable error) {
        Log.e(TAG + " - " + tag, "Unexpected error", error);
    }

    public static void logError(String tag, String message, Throwable error) {
        Log.e(TAG + " - " + tag, message, error);
    }
}