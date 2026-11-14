
package com.ranamustafa.clinicshub.data.repository;

import com.ranamustafa.clinicshub.data.api.ApiClient;
import com.ranamustafa.clinicshub.data.api.ApiService;
import com.ranamustafa.clinicshub.data.model.DoctorModel;
 import com.ranamustafa.clinicshub.utils.errors.ErrorHandler;
import com.ranamustafa.clinicshub.utils.errors.ApiResult;

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
                            ApiResult<?> apiResult =  ErrorHandler.handleHttpError(TAG ,response.code());
                            callback.onError(apiResult.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<DoctorModel>> call, Throwable t) {
                        String message = "";
                        if (t instanceof Exception) {
                            Exception exception = (Exception) t;
                            ApiResult<?> apiResult = ErrorHandler.handle(TAG ,exception);
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
