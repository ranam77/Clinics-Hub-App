
package com.ranamustafa.clinicshub.data.repository;

import com.ranamustafa.clinicshub.data.api.ApiClient;
import com.ranamustafa.clinicshub.data.api.ApiService;
import com.ranamustafa.clinicshub.data.model.ListModel;
 import com.ranamustafa.clinicshub.utils.errors.ErrorHandler;
import com.ranamustafa.clinicshub.utils.errors.ApiResult;

import java.util.List;

import retrofit2.Call;
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
                            ApiResult<?> apiResult =  ErrorHandler.handleHttpError(TAG ,response.code());
                            callback.onError(apiResult.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ListModel>> call, Throwable t) {
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
            }catch (Exception e)
            {
                 ErrorHandler.handle(TAG ,e);
            }
        }


    }
}
