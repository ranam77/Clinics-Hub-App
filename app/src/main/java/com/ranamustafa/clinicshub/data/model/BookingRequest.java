package com.ranamustafa.clinicshub.data.model;

public class BookingRequest {

    private int slotId;
    private int patientId;

    public BookingRequest(int slotId, int patientId) {
        this.slotId = slotId;
        this.patientId = patientId;
    }

    public int getSlotId() {
        return slotId;
    }

    public int getPatientId() {
        return patientId;
    }
}