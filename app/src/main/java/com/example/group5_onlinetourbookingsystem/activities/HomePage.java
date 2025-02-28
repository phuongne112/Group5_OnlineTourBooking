package com.example.group5_onlinetourbookingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.BookingFragment;
import com.example.group5_onlinetourbookingsystem.FavouriteFragment;
import com.example.group5_onlinetourbookingsystem.HomeFragment;
import com.example.group5_onlinetourbookingsystem.MainActivity;
import com.example.group5_onlinetourbookingsystem.ProfileFragment;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.TourFragment;
import com.example.group5_onlinetourbookingsystem.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePage extends AppCompatActivity {
    private SessionManager sessionManager;
    private TextView textViewUserName;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        sessionManager = new SessionManager(this);



        // Fragment Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home1) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_tour2) {
                selectedFragment = new TourFragment();
            } else if (itemId == R.id.nav_profile3) {
                selectedFragment = new ProfileFragment();
            } else if (itemId == R.id.nav_booking5) {
                if (sessionManager.isLoggedIn()) {
                    selectedFragment = new BookingFragment();
                } else {
                    Toast.makeText(HomePage.this, "Bạn cần đăng nhập để đặt tour!", Toast.LENGTH_SHORT).show();
                    return false; // Không cho chuyển trang
                }
            } else if (itemId == R.id.nav_favourite3) {
                selectedFragment = new FavouriteFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });
    }




}