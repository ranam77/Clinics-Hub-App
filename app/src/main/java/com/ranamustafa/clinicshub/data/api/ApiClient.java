package com.ranamustafa.clinicshub.data.api;

import com.ranamustafa.clinicshub.utils.errors.ErrorHandler;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "http://alextechboxclinic.runasp.net/";
    public static final String IMAGE_BASE_URL = BASE_URL + "images/";
    private static Retrofit retrofit;
    private static ApiService apiService;
    private static final String TAG = ApiClient.class.getSimpleName();
    public static ApiService getApiService() {
        try {
            if (apiService == null) {

                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                apiService = retrofit.create(ApiService.class);
            }
        }catch (Exception e) {
            ErrorHandler.handle(TAG, e);
        }

        return apiService;
    }
}