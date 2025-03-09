package com.example.group5_onlinetourbookingsystem.activities;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.group5_onlinetourbookingsystem.ProfileFragment;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.AccountFragment;
import com.example.group5_onlinetourbookingsystem.AdminBookingFragment;
import com.example.group5_onlinetourbookingsystem.AdminTourFragment;
import com.example.group5_onlinetourbookingsystem.StatisticsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        View fragmentContainer = findViewById(R.id.fragmentContainer);

        if (fragmentContainer != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new StatisticsFragment())
                    .commit();
        }

        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_statistics) {
                selectedFragment = new StatisticsFragment();
            } else if (itemId == R.id.nav_tours) {
                selectedFragment = new AdminTourFragment();
            } else if (itemId == R.id.nav_bookings) {
                selectedFragment = new AdminBookingFragment();
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
}
