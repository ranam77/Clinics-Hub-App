package com.ranamustafa.clinicshub.data.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("p")
    private PatientModel patient;

    public String getAccessToken() {
        return accessToken;
    }

    public PatientModel getPatient() {
        return patient;
    }
}