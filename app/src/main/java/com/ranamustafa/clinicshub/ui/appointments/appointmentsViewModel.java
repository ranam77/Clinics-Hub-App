
package com.ranamustafa.clinicshub.ui.appointments;

import android.content.Context;

import com.ranamustafa.clinicshub.data.model.AppointmentModel;
import com.ranamustafa.clinicshub.data.model.BookingCancelResponse;
import com.ranamustafa.clinicshub.data.repository.AppointmentsRepository;
import com.ranamustafa.clinicshub.utils.errors.ErrorHandler;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class appointmentsViewModel extends androidx.lifecycle.ViewModel {
    private static final String TAG = appointmentsViewModel.class.getSimpleName();
    private final MutableLiveData<List<AppointmentModel>> datalist =  new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    public LiveData<List<AppointmentModel>> getData() {
        return datalist;
    }
    public LiveData<Boolean> getLoading() {
        return loading;
    }
    public LiveData<String> getError() {
        return error;
    }
    public void loadData(Context mContext) {
        if (Boolean.TRUE.equals(loading.getValue())|| datalist.getValue() != null) {
            return;
        }
        try{
            loading.setValue(true);
            error.setValue(null);
            AppointmentsRepository repository = new AppointmentsRepository();
            repository.getData(new AppointmentsRepository.Callback() {
                @Override
                public void onSuccess(List<AppointmentModel> data) {
                    datalist.setValue(data);
                    loading.setValue(false);
                }
                @Override
                public void onError(String message) {
                    error.setValue(message);
                    loading.setValue(false);
                }
                @Override
                public void onSuccess(boolean deleted) {}
            },  mContext);
        }
        catch (Exception e)
        {
            ErrorHandler.handle(TAG, e);
        }
    }
    interface Callback
    {
        void onfinish(boolean deleted);
    }
    public void cancelBooking(Context mContext, String bookingref , Callback  callback) {
        if (Boolean.TRUE.equals(loading.getValue())) {
            return;
        }
        try{
            loading.setValue(true);
            error.setValue(null);
            AppointmentsRepository repository = new AppointmentsRepository();
            repository.cancelbook(new AppointmentsRepository.Callback() {
                @Override
                public void onSuccess(List<AppointmentModel> data) {
                    datalist.setValue(data);
                    loading.setValue(false);
                }

                @Override
                public void onError(String message) {
                    error.setValue(message);
                    loading.setValue(false);
                }
                @Override
                public void onSuccess(boolean deleted) {
                    callback.onfinish(deleted);
                    loading.setValue(false);

                }
            },  bookingref, mContext);
        }
        catch (Exception e)
        {
            ErrorHandler.handle(TAG, e);
        }
    }
    public void ChnageAppointmentState(String ref) {
        List<AppointmentModel> currentList = datalist.getValue();
        if (currentList == null) return;
        List<AppointmentModel> updatedList = new ArrayList<>(currentList);
        for (AppointmentModel appointment : updatedList) {
            if (appointment.getBookingRef().equals(ref)) {
                appointment.setStatus(1);
                break;
            }
        }
        datalist.setValue(updatedList);
    }

}