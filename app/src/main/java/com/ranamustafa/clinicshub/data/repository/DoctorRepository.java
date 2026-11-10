
package com.ranamustafa.clinicshub.data.repository;

import com.ranamustafa.clinicshub.data.api.ApiClient;
import com.ranamustafa.clinicshub.data.api.ApiService;
import com.ranamustafa.clinicshub.data.model.DoctorModel;
import com.ranamustafa.clinicshub.data.model.ListModel;
import com.ranamustafa.clinicshub.utils.AppLogger;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class DoctorRepository {
    private final ApiService apiService;
    private static final String TAG = DoctorRepository.class.getSimpleName();
    public interface  Callback {
        void onSuccess(List<DoctorModel> clinics);
        void onError(String message);
    }

    public DoctorRepository() {
        apiService = ApiClient.getApiService();
    }

    public void getData(Callback callback , int ClId , int SpId) {
        if(apiService!= null)
        {
            try
            {
                apiService.getDoctors(ClId, SpId).enqueue(new retrofit2.Callback<>() {
                    @Override
                    public void onResponse(Call<List<DoctorModel>> call, Response<List<DoctorModel>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onError("Unable to load clinics. HTTP error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<DoctorModel>> call, Throwable t) {
                        String message = t.getMessage();
                        callback.onError(message != null ? message : "Network connection error."
                        );
                    }
                });
            }catch (Exception e)
            {
                AppLogger.logError(TAG, "getData",e);
            }
        }

    }
}
