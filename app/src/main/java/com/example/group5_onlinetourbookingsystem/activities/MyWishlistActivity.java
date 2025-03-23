package com.example.group5_onlinetourbookingsystem.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.adapters.TourAdapter;
import com.example.group5_onlinetourbookingsystem.models.TourModel;
import com.example.group5_onlinetourbookingsystem.utils.SessionManager;

import java.util.ArrayList;

public class MyWishlistActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TourAdapter tourAdapter;
    private MyDatabaseHelper dbHelper;
    private TextView tvNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wishlist);

        recyclerView = findViewById(R.id.recyclerViewWishlist);
        tvNoData = findViewById(R.id.tvNoWishlist);

        dbHelper = new MyDatabaseHelper(this);
        SessionManager sessionManager = new SessionManager(this);
        int userId = sessionManager.getUserId();

        ArrayList<TourModel> favoriteTours = dbHelper.getFavoriteTours(userId);

        if (favoriteTours.isEmpty()) {
            tvNoData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvNoData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            tourAdapter = new TourAdapter(this, favoriteTours, tour -> {

                Log.d("Wishlist", "Click vào tour: " + tour.getName());
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(tourAdapter);
        }
    }
}
