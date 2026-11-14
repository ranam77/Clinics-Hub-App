
package com.ranamustafa.clinicshub.data.repository;

import android.content.Context;
import android.util.Log;

import com.ranamustafa.clinicshub.data.api.ApiClient;
import com.ranamustafa.clinicshub.data.api.ApiService;
import com.ranamustafa.clinicshub.data.model.ApiResponse;
import com.ranamustafa.clinicshub.data.model.DoctorModel;
import com.ranamustafa.clinicshub.data.model.SlotModel;
import com.ranamustafa.clinicshub.data.model.SlotRequest;
import com.ranamustafa.clinicshub.utils.SessionManager;
import com.ranamustafa.clinicshub.utils.errors.ApiResult;
import com.ranamustafa.clinicshub.utils.errors.ErrorHandler;
import com.ranamustafa.clinicshub.utils.generalutils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SlotRepository {
    private final ApiService apiService;
    private static final String TAG = SlotRepository.class.getSimpleName();
    public interface  Callback {
        void onSuccess(List<SlotModel> data);
        void onError(String message);
    }

    public SlotRepository() {
        apiService = ApiClient.getApiService();
    }

    public void getData(Callback callback , SlotRequest slotrequest , Context mContext) {
        if(apiService!= null)
        {
            try
            {
                String token = new SessionManager(mContext).getToken();
                 apiService.getSlots("Bearer " +token , slotrequest).enqueue(new retrofit2.Callback<List<SlotModel>>() {
                     @Override
                     public void onResponse(Call<List<SlotModel>> call, Response<List<SlotModel>> response) {
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
                     public void onFailure(Call<List<SlotModel>> call, Throwable t) {
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