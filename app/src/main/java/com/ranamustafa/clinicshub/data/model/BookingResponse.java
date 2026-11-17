package com.ranamustafa.clinicshub.data.model;

import java.io.Serializable;

public class BookingResponse implements Serializable {

    private boolean success;
    private int id;
    private String bookingRef;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public int getId() {
        return id;
    }

    public String getBookingRef() {
        return bookingRef;
    }

    public String getMessage() {
        return message;
    }
}