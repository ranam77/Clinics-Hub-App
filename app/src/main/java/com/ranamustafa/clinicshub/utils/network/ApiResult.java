package com.ranamustafa.clinicshub.utils.network;

public class ApiResult<T> {

    public enum Status {
        LOADING,
        SUCCESS,
        ERROR
    }

    private final Status status;
    private final T data;
    private final String message;
    private final int errorCode;

    private ApiResult(Status status, T data, String message, int errorCode) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.errorCode = errorCode;
    }

    public static <T> ApiResult<T> loading() {
        return new ApiResult<>(Status.LOADING, null, null, 0);
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(Status.SUCCESS, data, null, 0);
    }

    public static <T> ApiResult<T> error(String message, int code) {
        return new ApiResult<>(Status.ERROR, null, message, code);
    }

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getErrorCode() {
        return errorCode;
    }
}