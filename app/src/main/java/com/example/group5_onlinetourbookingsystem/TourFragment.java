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
            databaseHelper.addTour("Tour Đà Lạt", "Đà Lạt", 1, 150.0, 3, "dalat_tour", 1, "2025-03-10 08:00:00");
            databaseHelper.addTour("Tour Phú Quốc", "Phú Quốc", 2, 200.0, 4, "phuquoc_tour", 2, "2025-03-12 09:30:00");
            databaseHelper.addTour("Tour Hà Nội", "Hà Nội", 3, 180.0, 3, "hanoi_tour", 3, "2025-03-15 07:45:00");
            databaseHelper.addTour("Tour Đà Nẵng", "Đà Nẵng", 4, 220.0, 5, "danang_tour", 4, "2025-03-18 10:15:00");
            databaseHelper.addTour("Tour Nha Trang", "Nha Trang", 5, 190.0, 4, "nhatrang_tour", 5, "2025-03-20 08:30:00");
            databaseHelper.addTour("Tour Sapa", "Sapa", 6, 170.0, 3, "sapa_tour", 6, "2025-03-25 06:00:00");
        }

    }
