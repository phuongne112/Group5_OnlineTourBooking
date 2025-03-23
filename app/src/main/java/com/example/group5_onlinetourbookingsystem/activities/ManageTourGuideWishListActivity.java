package com.example.group5_onlinetourbookingsystem.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.adapters.ManageTourGuideWishListAdapter;
import com.example.group5_onlinetourbookingsystem.models.BookingModel;

import java.util.ArrayList;

public class ManageTourGuideWishListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ManageTourGuideWishListAdapter adapter;
    private MyDatabaseHelper dbHelper;
    private ArrayList<BookingModel> wishlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_tour_guide_wish_list);

        recyclerView = findViewById(R.id.recyclerManageTourGuideWishlist);
        dbHelper = new MyDatabaseHelper(this);

        wishlist = (ArrayList<BookingModel>) dbHelper.getAllTourGuideWishlists();

        adapter = new ManageTourGuideWishListAdapter(this, wishlist, (booking, newStatus) -> {
            boolean updated = dbHelper.updateApplyStatus(booking.getTourId(), booking.getUserId(), newStatus);
            Toast.makeText(this, updated ? "✅ Cập nhật thành công" : "❌ Cập nhật thất bại", Toast.LENGTH_SHORT).show();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
