
package com.ranamustafa.clinicshub.ui.specialty;

import com.ranamustafa.clinicshub.data.model.SpecialtyModel;
import com.ranamustafa.clinicshub.data.repository.SpecialityRepository;
import com.ranamustafa.clinicshub.utils.errors.ErrorHandler;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SpecialtyViewModel extends androidx.lifecycle.ViewModel {
    private final MutableLiveData<List<SpecialtyModel>> datalist = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<List<SpecialtyModel>> getData() {
        return datalist;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    private static final String TAG = SpecialtyViewModel.class.getSimpleName();

    public void loadData(int ClinicID) {

        if (Boolean.TRUE.equals(loading.getValue()) || datalist.getValue() != null) {
            return;
        }
        try {
            loading.setValue(true);
            error.setValue(null);
            SpecialityRepository repository = new SpecialityRepository();
            repository.getSpeciality(new SpecialityRepository.Callback() {
                @Override
                public void onSuccess(List<SpecialtyModel> result) {
                    datalist.setValue(result);
                    loading.setValue(false);
                }

                @Override
                public void onError(String message) {
                    error.setValue(message);
                    loading.setValue(false);
                }
            }, ClinicID);
        } catch (Exception e) {
            ErrorHandler.handle(TAG, e);
        }
    }
}
