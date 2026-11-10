
package com.ranamustafa.clinicshub.data.repository;

import com.ranamustafa.clinicshub.data.api.ApiClient;
import com.ranamustafa.clinicshub.data.api.ApiService;
import com.ranamustafa.clinicshub.data.model.ListModel;
import com.ranamustafa.clinicshub.utils.AppLogger;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClinicRepository {
    private final ApiService apiService;
    private static final String TAG = ClinicRepository.class.getSimpleName();
    public interface  Callback {
        void onSuccess(List<ListModel> clinics);
        void onError(String message);
    }

    public ClinicRepository() {
        apiService = ApiClient.getApiService();
    }

    public void getClinic(Callback callback , int ProjectID) {
        if(apiService != null)
        {
            try{
                apiService.getClinics(ProjectID).enqueue(new retrofit2.Callback<>() {
                    @Override
                    public void onResponse(Call<List<ListModel>> call, Response<List<ListModel>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onError("Unable to load clinics. HTTP error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ListModel>> call, Throwable t) {
                        String message = t.getMessage();
                        callback.onError(message != null ? message : "Network connection error."
                        );
                    }
                });
            }catch (Exception e)
            {
                AppLogger.logError(TAG, "getClinic",e);
            }
        }


    }
}
