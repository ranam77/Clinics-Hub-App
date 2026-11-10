package com.ranamustafa.clinicshub.ui.main;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ranamustafa.clinicshub.R;
import com.ranamustafa.clinicshub.ui.doctors.doctorViewModel;
import com.ranamustafa.clinicshub.utils.AppLogger;

public class MainActivity extends AppCompatActivity  {
    NavController navController ;
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setupmenu();
        setupui();
    }

    private void setupui() {
        try{
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            navController = navHostFragment.getNavController();
            NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph);
            navGraph.setStartDestination(R.id.mainFragment);
            Bundle bundle = new Bundle();
            bundle.putInt("loadtype", 1);
            bundle.putInt("loadID", 0);
            navController.setGraph(navGraph, bundle);
        }
        catch (Exception e)
        {
            AppLogger.logError(TAG, "setupui",e);
        }
    }

    private void setupmenu() {
        try {
            BottomNavigationView bottomMenu = findViewById(R.id.bottomMenu);
            bottomMenu.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.home) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("loadtype", 1);
                    bundle.putInt("loadID", 0);
                    navController.navigate(R.id.mainFragment, bundle);
                    return true;
                }

                if (id == R.id.about) {
                    return true;
                }

                if (id == R.id.login) {
                    return true;
                }

                return false;
            });
        }catch (Exception e)
        {
            AppLogger.logError(TAG, "setupmenu",e);
        }

    }


}