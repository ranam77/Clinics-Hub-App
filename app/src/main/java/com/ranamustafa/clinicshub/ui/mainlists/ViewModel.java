
package com.ranamustafa.clinicshub.ui.mainlists;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ranamustafa.clinicshub.data.model.ListModel;
import com.ranamustafa.clinicshub.data.repository.ClinicRepository;
import com.ranamustafa.clinicshub.data.repository.ProjectRepository;
import com.ranamustafa.clinicshub.utils.AppLogger;

import java.util.List;

public class ViewModel extends androidx.lifecycle.ViewModel {
    private final MutableLiveData<List<ListModel>> datalist =  new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    public LiveData<List<ListModel>> getData() {
        return datalist;
    }
    public LiveData<Boolean> getLoading() {
        return loading;
    }
    public LiveData<String> getError() {
        return error;
    }
    private static final String TAG = ViewModel.class.getSimpleName();
    public void loadData(int type, int ID) {
        if (Boolean.TRUE.equals(loading.getValue())|| datalist.getValue() != null) {
            return;
        }
        try{
            loading.setValue(true);
            error.setValue(null);
            if(type == 1)
            {
                ProjectRepository repository = new ProjectRepository();
                repository.getProjects(new ProjectRepository.ProjectsCallback() {
                    @Override
                    public void onSuccess(List<ListModel> result) {
                        datalist.setValue(result);
                        loading.setValue(false);
                    }

                    @Override
                    public void onError(String message) {
                        error.setValue(message);
                        loading.setValue(false);
                    }
                });
            }
            if(type == 2)
            {
                ClinicRepository repository = new ClinicRepository();
                repository.getClinic(new ClinicRepository.Callback() {
                    @Override
                    public void onSuccess(List<ListModel> result) {
                        datalist.setValue(result);
                        loading.setValue(false);
                    }

                    @Override
                    public void onError(String message) {
                        error.setValue(message);
                        loading.setValue(false);
                    }
                }, ID);
            }
        }
        catch (Exception e){
            AppLogger.logError(TAG, "loadData",e);
        }
    }
}
