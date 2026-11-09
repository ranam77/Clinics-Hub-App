package com.ranamustafa.clinicshub.data.api;

import com.ranamustafa.clinicshub.data.model.Project;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api/clinics/allprojects")
   Call<List<Project>>getProjects();
}