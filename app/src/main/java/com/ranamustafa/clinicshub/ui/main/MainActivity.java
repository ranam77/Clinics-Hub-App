package com.ranamustafa.clinicshub.ui.main;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ranamustafa.clinicshub.R;
import com.ranamustafa.clinicshub.utils.SessionManager;
import com.ranamustafa.clinicshub.utils.errors.ErrorHandler;
import com.ranamustafa.clinicshub.utils.generalutils;

import static androidx.core.content.ContentProviderCompat.requireContext;

public class MainActivity extends AppCompatActivity {
    NavController navController;
    private SessionManager sessionManager;
    BottomNavigationView bottomMenu;
    private TextView tvUserHeader;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setupui();
        setupmenu();
    }

    private void setupui() {
        try {
            sessionManager = new SessionManager(this);
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            if (navHostFragment != null) {
                navController = navHostFragment.getNavController();
                NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph);
                navGraph.setStartDestination(R.id.mainFragment);
                Bundle bundle = new Bundle();
                bundle.putInt("loadtype", 1);
                bundle.putInt("loadID", 0);
                navController.setGraph(navGraph, bundle);
            }
            bottomMenu = findViewById(R.id.bottomMenu);
        } catch (Exception e) {
            ErrorHandler.handle(TAG, e);
        }
    }

    public void refreshAuthMenu() {
        updateLoginMenuItem();
    }

    private void setupmenu() {
        try {
            BottomNavigationView bottomMenu = findViewById(R.id.bottomMenu);
            tvUserHeader = findViewById(R.id.tvUserHeader);
            refreshAuthMenu();
            bottomMenu.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.home) {
                    returnhome(navController);
                    return true;
                }

                if (id == R.id.about) {
                    return true;
                }

                if (id == R.id.login) {
                    if (sessionManager.isLoggedIn()) {
                        generalutils.logout(MainActivity.this);
                    } else {
                        navController.navigate(R.id.loginFragment);
                    }

                    return true;
                }

                return false;
            });
        } catch (Exception e) {
            ErrorHandler.handle(TAG, e);
        }
    }

public static void returnhome(NavController n)
{
    Bundle bundle = new Bundle();
    bundle.putInt("loadtype", 1 );
    bundle.putInt("loadID",0);
    n.navigate(R.id.mainFragment, bundle);
}
    private void updateLoginMenuItem() {
        MenuItem loginItem = bottomMenu.getMenu().findItem(R.id.login);
        if (loginItem == null) return;
        if (sessionManager.isLoggedIn()) {
            loginItem.setTitle(R.string.logout);
            loginItem.setIcon(R.drawable.ic_logout);
            tvUserHeader.setText(String.format("Welcome, %s", sessionManager.getPatientFullName()));
            tvUserHeader.setVisibility(View.VISIBLE);
        } else {
            loginItem.setTitle(R.string.login);
            loginItem.setIcon(R.drawable.ic_login);
            tvUserHeader.setVisibility(View.GONE);
        }
    }
}