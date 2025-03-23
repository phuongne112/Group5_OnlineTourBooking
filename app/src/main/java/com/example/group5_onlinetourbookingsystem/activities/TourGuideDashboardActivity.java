package com.example.group5_onlinetourbookingsystem.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.fragments.ProfileFragment;
import com.example.group5_onlinetourbookingsystem.fragments.TourGuideFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TourGuideDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_guide_dashboard);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);

        // ✅ Load TourGuideFragment as the default fragment when activity starts
        if (savedInstanceState == null) {
            loadFragment(new TourGuideFragment());
            bottomNavigation.setSelectedItemId(R.id.nav_tourguide); // optional: highlight default
        }

        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_tourguide) {
                selectedFragment = new TourGuideFragment();
            } else if (itemId == R.id.nav_accounts) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
            }
            return true;
        });

    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
