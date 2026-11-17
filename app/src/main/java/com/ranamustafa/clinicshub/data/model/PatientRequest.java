package com.ranamustafa.clinicshub.data.model;

public class PatientRequest {

    private int patientId;

    public PatientRequest(int patientId) {
        this.patientId = patientId;
    }

    public int getPatientId() {
        return patientId;
    }
}