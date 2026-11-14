
package com.ranamustafa.clinicshub.ui.doctors;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ranamustafa.clinicshub.R;
import com.ranamustafa.clinicshub.data.model.DoctorModel;
import com.ranamustafa.clinicshub.utils.SessionManager;
import com.ranamustafa.clinicshub.utils.errors.ErrorHandler;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class doctorFragment extends androidx.fragment.app.Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private TextView tvError;
    private View errorContainer;
    private Button btnRetry;
    private doctorAdapter adapter;
    private doctorViewModel viewModel;
    int SpecialtyID;
    int ClinicID;
    private static final String TAG = doctorFragment.class.getSimpleName();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            SpecialtyID = bundle.getInt("SpecialtyID", 0);
            ClinicID = bundle.getInt("ClinicID", 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.doctors));
        initializeViews(view);
        setupRecyclerView();
        setupViewModel();
        observeViewModel();
        viewModel.loadData(ClinicID, SpecialtyID);
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
            adapter = new doctorAdapter(item -> {
                if( new SessionManager(getContext()).isLoggedIn())
                {
                    Bundle bundle = new Bundle();
                    bundle.putInt("DoctorID", item.getId());
                    NavController navController = Navigation.findNavController(requireView());
                    navController.navigate(R.id.slotsFragment, bundle);
                }
                else {
                    showLoginRequiredDialog();

                }

            });
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            ErrorHandler.handle(TAG, e);
        }
    }
    private void showLoginRequiredDialog() {

        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.dialogtitle))
                .setMessage(R.string.dialogloginmsg)
                .setPositiveButton(getString(R.string.dialogbtn), (dialog, which) -> {
                    NavHostFragment.findNavController(this).navigate(R.id.loginFragment);
                })
                .setNegativeButton(getString(R.string.cancelbtn), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(doctorViewModel.class);
    }

    private void observeViewModel() {
        try{
            viewModel.getData().observe(getViewLifecycleOwner(), this::update );
            viewModel.getLoading().observe(getViewLifecycleOwner(), this::updateLoading);
            viewModel.getError().observe(getViewLifecycleOwner(), this::updateError);
            btnRetry.setOnClickListener(v -> viewModel.loadData(ClinicID, SpecialtyID));
        }
        catch (Exception e)
        {
            ErrorHandler.handle(TAG, e);
        }
    }

    private void update (List<DoctorModel> data) {
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
