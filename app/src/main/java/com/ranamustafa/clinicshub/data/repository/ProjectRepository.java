
package com.ranamustafa.clinicshub.data.repository;

import com.ranamustafa.clinicshub.data.api.ApiClient;
import com.ranamustafa.clinicshub.data.api.ApiService;
import com.ranamustafa.clinicshub.data.model.Project;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectRepository {

    private final ApiService apiService;

    public interface ProjectsCallback {
        void onSuccess(List<Project> projects);
        void onError(String message);
    }

    public ProjectRepository() {
        apiService = ApiClient.getApiService();
    }

    public void getProjects(ProjectsCallback callback) {

        apiService.getProjects().enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call,Response<List<Project>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else { callback.onError("Unable to load projects. HTTP error: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Project>> call,Throwable throwable) {
                String message = throwable.getMessage();
                callback.onError( message != null? message : "Network connection error."
                );
            }
        });
    }
}
