package com.ranamustafa.clinicshub.data.api;

import com.ranamustafa.clinicshub.data.model.ApiResponse;
import com.ranamustafa.clinicshub.data.model.DoctorModel;
import com.ranamustafa.clinicshub.data.model.ListModel;
import com.ranamustafa.clinicshub.data.model.LoginRequest;
import com.ranamustafa.clinicshub.data.model.LoginResponse;
import com.ranamustafa.clinicshub.data.model.SlotModel;
import com.ranamustafa.clinicshub.data.model.SlotRequest;
import com.ranamustafa.clinicshub.data.model.SpecialtyModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("api/clinics/allprojects")
    Call<List<ListModel>>getProjects();
    @GET("api/clinics/relatedclinics/{projectId}")
    Call<List<ListModel>> getClinics(@Path("projectId") int projectId);
    @GET("api/clinics/relatedspeciality/{clinicID}")
    Call<List<SpecialtyModel>> getSpeciality(@Path("clinicID") int ClinicID);
    @GET("api/Doctors/all/{cl}/{sp}")
    Call<List<DoctorModel>> getDoctors(@Path("cl") int ClinicID , @Path("sp") int sp);
    @POST("api/Auth/token/patients")
    Call<LoginResponse> loginPatient(@Body LoginRequest request);

    @POST("api/slots/validSlotes")
     Call<List<SlotModel>> getSlots(@Header("Authorization") String token, @Body SlotRequest slotRequest);



}