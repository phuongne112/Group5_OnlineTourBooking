package com.example.group5_onlinetourbookingsystem.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.HomeFragment;
import com.example.group5_onlinetourbookingsystem.ProfileFragment;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePage extends AppCompatActivity {
    private RecyclerView homeHorizontalRec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        //Fragment
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        FragmentContainerView fragmentContainer = findViewById(R.id.fragment_container);

        // Set default fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_home1) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_search2) {
                selectedFragment = new SearchFragment();
            } else if (itemId == R.id.nav_profile3) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }

            return true;
        });

    }
}