package com.example.group5_onlinetourbookingsystem.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.adapters.BookingAdapter;
import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.fragments.ProfileFragment;
import com.example.group5_onlinetourbookingsystem.models.BookingModel;
import java.util.ArrayList;

public class TourGuideDashboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private MyDatabaseHelper dbHelper;
    private ArrayList<BookingModel> bookingList;
    private Button btnUpcoming, btnPrevious;
    private LinearLayout navProfile; // Thêm biến navProfile

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_guide_dashboard);

        recyclerView = findViewById(R.id.recyclerViewBookings);
        btnUpcoming = findViewById(R.id.btnUpcoming);
        btnPrevious = findViewById(R.id.btnPrevious);
        ImageView btnBack = findViewById(R.id.btnBack);
        LinearLayout navProfile = findViewById(R.id.nav_profile); // ⬅️ Lấy ID của Profile Button

        dbHelper = new MyDatabaseHelper(this);

        // 🆕 Load danh sách booking mặc định
        loadBookings("upcoming");

        btnUpcoming.setOnClickListener(v -> loadBookings("upcoming"));
        btnPrevious.setOnClickListener(v -> loadBookings("previous"));
        btnBack.setOnClickListener(v -> finish());

        // 🆕 Khi nhấn vào "nav_profile", mở ProfileFragment
        navProfile.setOnClickListener(v -> {
            Log.d("TourGuideDashboard", "nav_profile clicked! Chuyển đến ProfileFragment...");
            openFragment(new ProfileFragment());
        });
    }


    private void loadBookings(String filter) {
        bookingList = dbHelper.getBookings(filter);

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        adapter = new BookingAdapter(this, bookingList, dbHelper);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    // 🆕 Hàm mở Fragment
    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);

        // Hiển thị `fragment_container` và ẩn Dashboard
        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
        findViewById(R.id.topNavigation).setVisibility(View.GONE);
        findViewById(R.id.tabContainer).setVisibility(View.GONE);
        findViewById(R.id.recyclerViewBookings).setVisibility(View.GONE);

        transaction.commit();
    }



}
