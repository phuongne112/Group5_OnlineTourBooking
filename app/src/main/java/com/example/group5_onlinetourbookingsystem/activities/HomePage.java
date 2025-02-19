package com.example.group5_onlinetourbookingsystem.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.adapters.HomeHorAdapter;
import com.example.group5_onlinetourbookingsystem.models.HomeHorModel;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {
    private RecyclerView homeHorizontalRec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        List<HomeHorModel> homeHorModelList = new ArrayList<>();
        homeHorModelList.add(new HomeHorModel(R.drawable.bottom_btn1,"Home"));
        homeHorModelList.add(new HomeHorModel(R.drawable.bottom_btn2,"Salary"));
        homeHorModelList.add(new HomeHorModel(R.drawable.favorites,"Favourites"));
        homeHorizontalRec = (RecyclerView) findViewById(R.id.home_hor_rec);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        homeHorizontalRec.setLayoutManager(layoutManager);
        homeHorizontalRec.setHasFixedSize(true);
        homeHorizontalRec.setAdapter(new HomeHorAdapter(this,homeHorModelList));
    }
}