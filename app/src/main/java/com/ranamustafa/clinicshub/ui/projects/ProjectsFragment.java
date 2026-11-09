
package com.ranamustafa.clinicshub.ui.projects;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ranamustafa.clinicshub.R;
import com.ranamustafa.clinicshub.data.model.Project;

import java.util.List;

public class ProjectsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private TextView tvError;
    private View errorContainer;
    private Button btnRetry;

    private ProjectsAdapter adapter;
    private ProjectViewModel viewModel;

    public ProjectsFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_projects,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        setupRecyclerView();
        setupViewModel();
        observeViewModel();
        viewModel.loadProjects();
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.rvProjects);
        progressBar = view.findViewById(R.id.progressBar);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        tvError = view.findViewById(R.id.tvError);
        errorContainer = view.findViewById(R.id.errorContainer);
        btnRetry = view.findViewById(R.id.btnRetry);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(
                new GridLayoutManager(requireContext(), 2)
        );
        adapter = new ProjectsAdapter(project -> {
            Toast.makeText(requireContext(),project.getName(),Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ProjectViewModel.class);
    }

    private void observeViewModel() {
        viewModel.getProjects().observe(getViewLifecycleOwner(),this::updateProjects);
        viewModel.getLoading().observe(getViewLifecycleOwner(),this::updateLoading);
        viewModel.getError().observe(getViewLifecycleOwner(),this::updateError);
        btnRetry.setOnClickListener(v -> viewModel.loadProjects());
    }

    private void updateProjects(List<Project> projects) {
        adapter.submitList(projects);
        boolean isEmpty = projects == null || projects.isEmpty();
        tvEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility( isEmpty ? View.GONE : View.VISIBLE);
    }

    private void updateLoading(Boolean isLoading) {
        boolean loading = Boolean.TRUE.equals(isLoading);
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    private void updateError(String message) {
        boolean hasError = message != null && !message.isEmpty();
        errorContainer.setVisibility(hasError ? View.VISIBLE : View.GONE);
        if (hasError) { tvError.setText(message);}
    }
}
