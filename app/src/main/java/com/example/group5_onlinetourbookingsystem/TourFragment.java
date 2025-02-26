package com.example.group5_onlinetourbookingsystem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.adapters.TourAdapter;
import com.example.group5_onlinetourbookingsystem.models.TourModel;

import java.util.ArrayList;


public class TourFragment extends Fragment {

    private RecyclerView recyclerViewTours, recyclerViewCategories;
    private TourAdapter tourAdapter;
    private ArrayList<TourModel> tourList;
    private MyDatabaseHelper databaseHelper;

    public TourFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tour, container, false);

        if (getContext() == null) return view;

        databaseHelper = new MyDatabaseHelper(getContext());

        // 👉 Cấu hình RecyclerView tour
        recyclerViewTours = view.findViewById(R.id.recycler_view);
        recyclerViewTours.setLayoutManager(new LinearLayoutManager(getContext()));

        tourList = databaseHelper.getAllTours();
        if (tourList.isEmpty()) {
            addSampleTours();
            tourList = databaseHelper.getAllTours();
        }

        tourAdapter = new TourAdapter(getContext(), tourList, tour -> {
            // Xử lý khi click vào tour, ví dụ mở chi tiết tour
            Toast.makeText(getContext(), "Clicked: " + tour.getName(), Toast.LENGTH_SHORT).show();
        });

        recyclerViewTours.setAdapter(tourAdapter);

        return view;
    }
    private void addSampleTours() {
        databaseHelper.addTour("Tour Đà Lạt", "Đà Lạt", 1, 150.0, 3, "travel", 1);
        databaseHelper.addTour("Tour Phú Quốc", "Phú Quốc", 2, 200.0, 4, "welcome_png", 2);
        databaseHelper.addTour("Tour Hà Nội", "Hà Nội", 3, 180.0, 2, "travel", 3);
        databaseHelper.addTour("Tour Sapa", "Sapa", 4, 220.0, 5, "travel", 3);
        databaseHelper.addTour("Tour Nha Trang", "Nha Trang", 5, 190.0, 3, "travel", 2);
        databaseHelper.addTour("Tour Huế", "Huế", 6, 170.0, 4, "travel", 1);
        databaseHelper.addTour("Tour Đà Nẵng", "Đà Nẵng", 7, 210.0, 3, "travel", 2);
        databaseHelper.addTour("Tour Cần Thơ", "Cần Thơ", 8, 160.0, 2, "travel", 3);
    }
}