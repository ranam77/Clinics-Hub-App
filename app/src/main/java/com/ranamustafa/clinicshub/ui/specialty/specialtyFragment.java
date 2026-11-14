
package com.ranamustafa.clinicshub.ui.specialty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ranamustafa.clinicshub.R;
import com.ranamustafa.clinicshub.data.model.SpecialtyModel;
import com.ranamustafa.clinicshub.utils.errors.ErrorHandler;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class specialtyFragment extends androidx.fragment.app.Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private TextView tvError;
    private View errorContainer;
    private Button btnRetry;
    private SpecialtyAdapter adapter;
    private SpecialtyViewModel viewModel;
    int ClinicID;
    private static final String TAG = specialtyFragment.class.getSimpleName();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            ClinicID = bundle.getInt("ClinicID", 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listmodel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.Specialities_title));
        initializeViews(view);
        setupRecyclerView();
        setupViewModel();
        observeViewModel();
        viewModel.loadData(ClinicID);
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
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        adapter = new SpecialtyAdapter(item -> {
            Bundle bundle = new Bundle();
            bundle.putInt("SpecialtyID", item.getId());
            bundle.putInt("ClinicID", ClinicID);
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.doctorFragment, bundle);

        });
        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(SpecialtyViewModel.class);
    }

    private void observeViewModel() {
        try{
            viewModel.getData().observe(getViewLifecycleOwner(), this::updateitems);
            viewModel.getLoading().observe(getViewLifecycleOwner(), this::updateLoading);
            viewModel.getError().observe(getViewLifecycleOwner(), this::updateError);
            btnRetry.setOnClickListener(v -> viewModel.loadData(ClinicID));
        }
        catch (Exception e)
        {
            ErrorHandler.handle(TAG, e);
        }

    }

    private void updateitems(List<SpecialtyModel> data) {
        try{
            adapter.submitList(data);
            boolean isEmpty = data == null || data.isEmpty();
            tvEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        }
        catch (Exception e)
        {
            ErrorHandler.handle(TAG, e);
        }
    }

    private void updateLoading(Boolean isLoading) {
        boolean loading = Boolean.TRUE.equals(isLoading);
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    private void updateError(String message) {
        boolean hasError = message != null && !message.isEmpty();
        errorContainer.setVisibility(hasError ? View.VISIBLE : View.GONE);
        if (hasError) {
            tvError.setText(message);
        }
    }
}
