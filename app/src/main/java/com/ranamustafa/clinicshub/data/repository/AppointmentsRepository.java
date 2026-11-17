
package com.ranamustafa.clinicshub.data.repository;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ranamustafa.clinicshub.data.api.ApiClient;
import com.ranamustafa.clinicshub.data.api.ApiService;
import com.ranamustafa.clinicshub.data.model.AppointmentModel;
import com.ranamustafa.clinicshub.data.model.BookingCancelResponse;
import com.ranamustafa.clinicshub.data.model.PatientRequest;
import com.ranamustafa.clinicshub.utils.SessionManager;
import com.ranamustafa.clinicshub.utils.errors.ApiResult;
import com.ranamustafa.clinicshub.utils.errors.ErrorHandler;
import com.ranamustafa.clinicshub.utils.generalutils;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class AppointmentsRepository {
    private final ApiService apiService;
    private static final String TAG = AppointmentsRepository.class.getSimpleName();
    public interface  Callback {
        void onSuccess(List<AppointmentModel> data);
        void onSuccess(boolean deleted);
        void onError(String message);
    }

    public AppointmentsRepository() {
        apiService = ApiClient.getApiService();
    }

    public void getData(Callback callback , Context mContext) {
        if(apiService!= null)
        {
            try
            {
                String token = new SessionManager(mContext).getToken();
                PatientRequest p = new PatientRequest(new SessionManager(mContext).getPatientId());
                 apiService.getBookings("Bearer " +token , p).enqueue(new retrofit2.Callback<List<AppointmentModel>>() {
                     @Override
                     public void onResponse(Call<List<AppointmentModel>> call, Response<List<AppointmentModel>> response) {
                         Log.d(TAG, "Response: " + new Gson().toJson(response.body()));
                         if (response.isSuccessful() && response.body() != null) {
                             callback.onSuccess(response.body());
                         } else {
                             if(response.code() == 401)
                             {
                                 generalutils.logout(mContext);
                             }
                             ApiResult<?> apiResult =  ErrorHandler.handleHttpError(TAG ,response.code());

                             callback.onError(apiResult.getMessage());
                         }
                     }

                     @Override
                     public void onFailure(Call<List<AppointmentModel>> call, Throwable t) {
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
    public void cancelbook(Callback callback , String  bookingref , Context mContext) {
        if(apiService!= null)
        {
            try
            {
                String token = new SessionManager(mContext).getToken();
                apiService.cancelBooking("Bearer " + token , bookingref).enqueue(new retrofit2.Callback<BookingCancelResponse>() {
                    @Override
                    public void onResponse(Call<BookingCancelResponse> call, Response<BookingCancelResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(true);
                        } else {
                            if(response.code() == 401)
                            {
                                generalutils.logout(mContext);
                            }
                            ApiResult<?> apiResult =  ErrorHandler.handleHttpError(TAG ,response.code());
                            callback.onError(apiResult.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<BookingCancelResponse> call, Throwable t) {
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