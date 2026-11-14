
package com.ranamustafa.clinicshub.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.ranamustafa.clinicshub.R;
import com.ranamustafa.clinicshub.data.model.LoginRequest;
import com.ranamustafa.clinicshub.data.model.LoginResponse;
import com.ranamustafa.clinicshub.data.model.PatientModel;
import com.ranamustafa.clinicshub.data.repository.AuthRepository;
import com.ranamustafa.clinicshub.ui.main.MainActivity;
import com.ranamustafa.clinicshub.utils.SessionManager;
import com.ranamustafa.clinicshub.utils.errors.ErrorHandler;

import static com.ranamustafa.clinicshub.ui.main.MainActivity.returnhome;

public class loginFragment extends Fragment {

    private EditText tvUsername;
    private EditText tvPassword;
    private ProgressBar progressBar;
    private Button btnLogin;
    private TextView tvError;
    private LinearLayout errorContainer;
    private AuthRepository authRepository;
    private static final String TAG = loginFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        authRepository = new AuthRepository();
        btnLogin.setOnClickListener(v -> login());
    }

    private void initializeViews(View view) {
        tvUsername = view.findViewById(R.id.edtUsername);
        tvPassword = view.findViewById(R.id.edtPassword);
        progressBar = view.findViewById(R.id.progressBar);
        btnLogin = view.findViewById(R.id.btnLogin);
        tvError = view.findViewById(R.id.tvError);
        errorContainer = view.findViewById(R.id.errorContainer);
    }

    private void login() {
        String username = tvUsername.getText().toString().trim();
        String password = tvPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            tvUsername.setError(getString(R.string.username_required));
            tvUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            tvPassword.setError(getString(R.string.password_required));
            tvPassword.requestFocus();
            return;
        }

        hideError();
        setLoading(true);
        try {
            LoginRequest request = new LoginRequest(username, password);
            authRepository.loginPatient(
                    new AuthRepository.Callback() {
                        @Override
                        public void onSuccess(LoginResponse result) {
                            if (!isAdded()) return;
                            setLoading(false);
                            new SessionManager(getActivity()).saveSession(result);
                            ((MainActivity) requireActivity()).refreshAuthMenu();
                            NavController navController = Navigation.findNavController(requireView());
                            returnhome(navController);
                        }

                        @Override
                        public void onError(String message) {
                            if (!isAdded()) return;
                            setLoading(false);
                            showError(message);
                        }
                    },
                    request
            );
        } catch (Exception e) {
            ErrorHandler.handle(TAG, e);
        }

    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!loading);
        tvUsername.setEnabled(!loading);
        tvPassword.setEnabled(!loading);
    }

    private void showError(String message) {
        errorContainer.setVisibility(View.VISIBLE);
        tvError.setText(message);
    }

    private void hideError() {
        errorContainer.setVisibility(View.GONE);
    }
}
