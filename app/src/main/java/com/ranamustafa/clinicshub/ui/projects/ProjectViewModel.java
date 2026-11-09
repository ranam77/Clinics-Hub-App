
package com.ranamustafa.clinicshub.ui.projects;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.ranamustafa.clinicshub.data.model.Project;
import com.ranamustafa.clinicshub.data.repository.ProjectRepository;
import java.util.List;

public class ProjectViewModel extends ViewModel {

    private final ProjectRepository repository;

    private final MutableLiveData<List<Project>> projects =
            new MutableLiveData<>();

    private final MutableLiveData<Boolean> loading =
            new MutableLiveData<>(false);

    private final MutableLiveData<String> error =
            new MutableLiveData<>();

    public ProjectViewModel() {
        repository = new ProjectRepository();
    }

    public LiveData<List<Project>> getProjects() {
        return projects;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadProjects() {

        if (Boolean.TRUE.equals(loading.getValue())
                || projects.getValue() != null) {
            return;
        }

        loading.setValue(true);
        error.setValue(null);
        repository.getProjects(new ProjectRepository.ProjectsCallback() {
            @Override
            public void onSuccess(List<Project> result) {
                projects.setValue(result);
                loading.setValue(false);
            }

            @Override
            public void onError(String message) {
                error.setValue(message);
                loading.setValue(false);
            }
        });
    }
}
