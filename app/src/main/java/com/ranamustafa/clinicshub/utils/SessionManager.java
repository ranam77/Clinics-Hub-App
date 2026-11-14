
package com.ranamustafa.clinicshub.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ranamustafa.clinicshub.data.model.LoginResponse;
import com.ranamustafa.clinicshub.data.model.PatientModel;

public class SessionManager {

    private static final String PREF_NAME = "clinic_session";

    private static final String KEY_TOKEN = "access_token";
    private static final String KEY_PATIENT_ID = "patient_id";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";

    private final SharedPreferences preferences;

    public SessionManager(Context context) {
        preferences = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Save login session
    public void saveSession(LoginResponse response) {

        if (response == null || response.getPatient() == null) {
            return;
        }

        PatientModel patient = response.getPatient();

        preferences.edit()
                .putString(KEY_TOKEN, response.getAccessToken())
                .putInt(KEY_PATIENT_ID, patient.getId())
                .putString(KEY_FIRST_NAME, patient.getFirstName())
                .putString(KEY_LAST_NAME, patient.getLastName())
                .apply();
    }

    // Check whether the patient is logged in
    public boolean isLoggedIn() {
        String token = getToken();
        return token != null && !token.isEmpty();
    }

    public String getToken() {
        return preferences.getString(KEY_TOKEN, null);
    }

    public int getPatientId() {
        return preferences.getInt(KEY_PATIENT_ID, -1);
    }

    public String getFirstName() {
        return preferences.getString(KEY_FIRST_NAME, "");
    }

    public String getLastName() {
        return preferences.getString(KEY_LAST_NAME, "");
    }

    public String getPatientFullName() {
        return (getFirstName() + " " + getLastName()).trim();
    }
    public void clearSession() {
        preferences.edit().clear().apply();
    }

}
