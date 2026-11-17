
package com.ranamustafa.clinicshub.ui.slots;

import android.content.Context;

import com.ranamustafa.clinicshub.data.model.BookingRequest;
import com.ranamustafa.clinicshub.data.model.BookingResponse;
import com.ranamustafa.clinicshub.data.model.DoctorModel;
import com.ranamustafa.clinicshub.data.model.SlotModel;
import com.ranamustafa.clinicshub.data.model.SlotRequest;
import com.ranamustafa.clinicshub.data.repository.DoctorRepository;
import com.ranamustafa.clinicshub.data.repository.SlotRepository;
import com.ranamustafa.clinicshub.utils.errors.ErrorHandler;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import static com.ranamustafa.clinicshub.utils.generalutils.getcurrentTimeDate;

public class slotsViewModel extends androidx.lifecycle.ViewModel {
    private static final String TAG = slotsViewModel.class.getSimpleName();
    private final MutableLiveData<List<SlotModel>> datalist =  new MutableLiveData<>();
    private final BookingResponse Booking = new BookingResponse();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    public LiveData<List<SlotModel>> getData() {
        return datalist;
    }
    public LiveData<Boolean> getLoading() {
        return loading;
    }
    public LiveData<String> getError() {
        return error;
    }
    public void loadData(Context mContext , int DoctorID) {
        if (Boolean.TRUE.equals(loading.getValue())|| datalist.getValue() != null) {
            return;
        }
        try{
            SlotRequest r = new SlotRequest(getcurrentTimeDate() , DoctorID);
            loading.setValue(true);
            error.setValue(null);
            SlotRepository repository = new SlotRepository();
            repository.getData(new SlotRepository.Callback() {
                @Override
                public void onSuccess(List<SlotModel> data) {
                    datalist.setValue(data);
                    loading.setValue(false);
                }

                @Override
                public void onError(String message) {
                    error.setValue(message);
                    loading.setValue(false);
                }
                @Override
                public void onSuccess(BookingResponse data) {}
            }, r, mContext);
        }
        catch (Exception e)
        {
            ErrorHandler.handle(TAG, e);
        }
    }
    public interface Callback
    {
        public void onfinish(BookingResponse res);
    }
    public void bookSlot(Context mContext ,int SlotID ,int patientID , Callback callback) {
        if (Boolean.TRUE.equals(loading.getValue())) {
            return;
        }
        try{
            BookingRequest request = new BookingRequest(SlotID , patientID);
            loading.setValue(true);
            error.setValue(null);
            SlotRepository repository = new SlotRepository();
            repository.bookSlot(new SlotRepository.Callback() {
                @Override
                public void onSuccess(List<SlotModel> data) {}

                @Override
                public void onError(String message) {
                    error.setValue(message);
                    loading.setValue(false);
                }
                @Override
                public void onSuccess(BookingResponse data) {
                    loading.setValue(false);
                    callback.onfinish(data);
                }
            }, request, mContext);
        }
        catch (Exception e)
        {
            ErrorHandler.handle(TAG, e);
        }
    }
}