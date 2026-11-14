
package com.ranamustafa.clinicshub.data.repository;

import com.ranamustafa.clinicshub.data.api.ApiClient;
import com.ranamustafa.clinicshub.data.api.ApiService;
import com.ranamustafa.clinicshub.data.model.ListModel;
 import com.ranamustafa.clinicshub.utils.errors.ErrorHandler;
import com.ranamustafa.clinicshub.utils.errors.ApiResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectRepository {
    private final ApiService apiService;
    private static final String TAG = ProjectRepository.class.getSimpleName();
    public interface ProjectsCallback {
        void onSuccess(List<ListModel> projects);
        void onError(String message);
    }

    public ProjectRepository() {
        apiService = ApiClient.getApiService();
    }

    public void getProjects(ProjectsCallback callback) {
        if(apiService!= null) {
            try {
                apiService.getProjects().enqueue(new Callback<List<ListModel>>() {
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
                            ApiResult<?> apiResult = ErrorHandler.handleThrowable(TAG ,t);
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
}
