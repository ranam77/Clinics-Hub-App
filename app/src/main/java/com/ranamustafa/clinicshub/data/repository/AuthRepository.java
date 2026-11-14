
package com.ranamustafa.clinicshub.data.repository;

import com.ranamustafa.clinicshub.data.api.ApiClient;
import com.ranamustafa.clinicshub.data.api.ApiService;
import com.ranamustafa.clinicshub.data.model.LoginRequest;
import com.ranamustafa.clinicshub.data.model.LoginResponse;
import com.ranamustafa.clinicshub.utils.errors.ErrorHandler;
import com.ranamustafa.clinicshub.utils.errors.ApiResult;
import retrofit2.Call;
import retrofit2.Response;

public class AuthRepository {
    private final ApiService apiService;
    private static final String TAG = AuthRepository.class.getSimpleName();
    public interface  Callback {
        void onSuccess(LoginResponse user);
        void onError(String message);
    }
    public AuthRepository() {
        apiService = ApiClient.getApiService();
    }

    public void loginPatient(Callback callback  , LoginRequest user) {
        if(apiService != null)
        {
            try{
                apiService.loginPatient(user).enqueue(new retrofit2.Callback<>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                             ApiResult<?> apiResult =  ErrorHandler.handleHttpError(TAG ,response.code());
                            callback.onError(apiResult.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        String message = "";
                        if (t instanceof Exception) {
                            Exception exception = (Exception) t;
                             ApiResult<?> apiResult =   ErrorHandler.handle(TAG,exception);
                            message = apiResult.getMessage();
                        }
                        else
                        {
                             ApiResult<?> apiResult =  ErrorHandler.handleThrowable(TAG,t);
                            message = apiResult.getMessage();
                        }
                        callback.onError(message);
                    }

                });
            }catch (Exception e)
            {
              ErrorHandler.handle(TAG , e);
            }
        }
    }
}
