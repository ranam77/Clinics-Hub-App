
package com.ranamustafa.clinicshub.ui.mainlists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ranamustafa.clinicshub.R;
import com.ranamustafa.clinicshub.data.model.ListModel;
import com.ranamustafa.clinicshub.utils.errors.ErrorHandler;

import java.util.List;

public class mainFragment extends androidx.fragment.app.Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private TextView tvError;
    private View errorContainer;
    private Button btnRetry;
    private Adapter adapter;
    private ViewModel viewModel;
    int loadtype;
    int loadID;
    private static final String TAG = mainFragment.class.getSimpleName();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            loadtype = bundle.getInt("loadtype", 0);
            loadID = bundle.getInt("loadID", 0);
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
        if (loadtype == 1)
            tvTitle.setText(getString(R.string.partners_title));
        if (loadtype == 2)
            tvTitle.setText(getString(R.string.clinics_title));
        initializeViews(view);
        setupRecyclerView();
        setupViewModel();
        observeViewModel();
        viewModel.loadData(loadtype, loadID);
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
        try{
            recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
            adapter = new Adapter(item -> {
                if (loadtype == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("loadtype", 2 );
                    bundle.putInt("loadID", item.getId());
                    NavController navController = Navigation.findNavController(requireView());
                    navController.navigate(R.id.mainFragment, bundle);
                } if (loadtype == 2) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("ClinicID", item.getId());

                    NavController navController =
                            Navigation.findNavController(requireView());

                    navController.navigate(R.id.specialtyFragment, bundle);
                }
            });
            recyclerView.setAdapter(adapter);
        }
        catch (Exception e)
        {
            ErrorHandler.handle(TAG, e);
        }

    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ViewModel.class);
    }

    private void observeViewModel() {
        try{
            viewModel.getData().observe(getViewLifecycleOwner(), this::updateProjects);
            viewModel.getLoading().observe(getViewLifecycleOwner(), this::updateLoading);
            viewModel.getError().observe(getViewLifecycleOwner(), this::updateError);
            btnRetry.setOnClickListener(v -> viewModel.loadData(loadtype, loadID));
        }
        catch (Exception e)
        {
            ErrorHandler.handle(TAG, e);
        }
    }

    private void updateProjects(List<ListModel> data) {
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
