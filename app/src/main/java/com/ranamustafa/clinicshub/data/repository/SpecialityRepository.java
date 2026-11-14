
package com.ranamustafa.clinicshub.data.repository;

import com.ranamustafa.clinicshub.data.api.ApiClient;
import com.ranamustafa.clinicshub.data.api.ApiService;
import com.ranamustafa.clinicshub.data.model.SpecialtyModel;
 import com.ranamustafa.clinicshub.utils.errors.ErrorHandler;
import com.ranamustafa.clinicshub.utils.errors.ApiResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SpecialityRepository {
    private final ApiService apiService;
    private static final String TAG = SpecialityRepository.class.getSimpleName();
    public interface  Callback {
        void onSuccess(List<SpecialtyModel> clinics);
        void onError(String message);
    }

    public SpecialityRepository() {
        apiService = ApiClient.getApiService();
    }

    public void getSpeciality(Callback callback , int clinicID ) {
        if(apiService == null)
            return;
        try{
            apiService.getSpeciality(clinicID).enqueue(new retrofit2.Callback<>() {
                @Override
                public void onResponse(Call<List<SpecialtyModel>> call, Response<List<SpecialtyModel>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        callback.onSuccess(response.body());
                    } else {
                        ApiResult<?> apiResult =  ErrorHandler.handleHttpError(TAG ,response.code());
                        callback.onError(apiResult.getMessage());
                    }
                }
                @Override
                public void onFailure(Call<List<SpecialtyModel>> call, Throwable t) {
                    String message = "";
                    if (t instanceof Exception) {
                        Exception exception = (Exception) t;
                        ApiResult<?> apiResult =  ErrorHandler.handle(TAG ,exception);
                        message = apiResult.getMessage();
                    }
                    else
                    {
                        ApiResult<?> apiResult =  ErrorHandler.handleThrowable(TAG ,t);
                        message = apiResult.getMessage();
                    }
                    callback.onError(message);
                }
            });
        } catch (Exception e) {
             ErrorHandler.handle(TAG ,e);
        }
    }
}
