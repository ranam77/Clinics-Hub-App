
package com.ranamustafa.clinicshub.data.repository;

import com.ranamustafa.clinicshub.data.api.ApiClient;
import com.ranamustafa.clinicshub.data.api.ApiService;
import com.ranamustafa.clinicshub.data.model.ListModel;
import com.ranamustafa.clinicshub.utils.AppLogger;

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
                    public void onResponse(Call<List<ListModel>> call,Response<List<ListModel>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else { callback.onError("Unable to load projects. HTTP error: "+ response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ListModel>> call,Throwable throwable) {
                        String message = throwable.getMessage();
                        callback.onError( message != null? message : "Network connection error."
                        );
                    }
                });
            } catch (Exception e) {
                AppLogger.logError(TAG, "getProjects",e);
            }
        }


    }
}
