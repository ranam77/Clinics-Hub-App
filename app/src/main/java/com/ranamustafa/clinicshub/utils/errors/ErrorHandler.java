package com.ranamustafa.clinicshub.utils.errors;

import com.ranamustafa.clinicshub.utils.SessionManager;

public class ErrorHandler {
    public static ApiResult<?> handle(String contextname  ,Exception throwable) {

        if (throwable instanceof java.net.UnknownHostException) {
            return ApiResult.error(
                    "No internet connection", 0);

        } else if (throwable instanceof java.net.SocketTimeoutException) {
            return ApiResult.error(
                    "Connection timed out. Please try again.", 0);

        } else {
            return ApiResult.error(
                    "Something went wrong. Please try again.", 0);
        }
    }

    public static ApiResult<?> handleHttpError(String contextname  ,int code) {
        switch (code) {
            case 400:
                return ApiResult.error("Invalid request.", code);

            case 401:
            {
                return ApiResult.error("Session expired. Please login again.", code);

            }
            case 403:
                return ApiResult.error("You don't have permission.", code);

            case 404:
                return ApiResult.error("Requested data not found.", code);

            case 500:
            case 502:
            case 503:
                return ApiResult.error("Server error. Please try again later.", code);

            default:
                return ApiResult.error("Request failed. Please try again.", code);
        }
    }

    public static ApiResult<?> handleThrowable(String contextname  ,Throwable t) {
        return ApiResult.error(t.getMessage(), 000);
    }
}