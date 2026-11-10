
package com.ranamustafa.clinicshub.ui.doctors;

import com.ranamustafa.clinicshub.data.model.DoctorModel;
import com.ranamustafa.clinicshub.data.model.ListModel;
import com.ranamustafa.clinicshub.data.repository.ClinicRepository;
import com.ranamustafa.clinicshub.data.repository.DoctorRepository;
import com.ranamustafa.clinicshub.data.repository.ProjectRepository;
import com.ranamustafa.clinicshub.utils.AppLogger;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class doctorViewModel extends androidx.lifecycle.ViewModel {
    private static final String TAG = doctorViewModel.class.getSimpleName();
    private final MutableLiveData<List<DoctorModel>> datalist =  new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    public LiveData<List<DoctorModel>> getData() {
        return datalist;
    }
    public LiveData<Boolean> getLoading() {
        return loading;
    }
    public LiveData<String> getError() {
        return error;
    }
    public void loadData( int ClId, int SpId) {
        if (Boolean.TRUE.equals(loading.getValue())|| datalist.getValue() != null) {
            return;
        }
        try{
            loading.setValue(true);
            error.setValue(null);
            DoctorRepository repository = new DoctorRepository();
            repository.getData(new DoctorRepository.Callback() {
                @Override
                public void onSuccess(List<DoctorModel> result) {
                    datalist.setValue(result);
                    loading.setValue(false);
                }

                @Override
                public void onError(String message) {
                    error.setValue(message);
                    loading.setValue(false);
                }
            }, ClId, SpId);
        }
        catch (Exception e)
        {
            AppLogger.logError(TAG, "loadData",e);
        }
    }
}
