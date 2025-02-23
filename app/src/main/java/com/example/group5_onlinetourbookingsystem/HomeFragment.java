package com.example.group5_onlinetourbookingsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.adapters.CategoryAdapter;
import com.example.group5_onlinetourbookingsystem.adapters.TourAdapter;
import com.example.group5_onlinetourbookingsystem.models.CategoryModel;
import com.example.group5_onlinetourbookingsystem.models.TourModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewTours, recyclerViewCategories;
    private TourAdapter tourAdapter;
    private CategoryAdapter categoryAdapter;
    private ArrayList<TourModel> tourList;
    private ArrayList<CategoryModel> categoryList;
    private MyDatabaseHelper databaseHelper;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        databaseHelper = new MyDatabaseHelper(getContext());

        // Cấu hình RecyclerView cho danh mục
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        recyclerViewCategories.setLayoutManager(new GridLayoutManager(getContext(), 3));

        categoryList = databaseHelper.getAllCategories();
        if (categoryList.isEmpty()) {
            addSampleCategories();
            categoryList = databaseHelper.getAllCategories(); // Lấy lại danh sách sau khi thêm
        }

        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        recyclerViewCategories.setAdapter(categoryAdapter);

        // Cấu hình RecyclerView cho danh sách tour
        recyclerViewTours = view.findViewById(R.id.recycler_view);
        recyclerViewTours.setLayoutManager(new LinearLayoutManager(getContext()));

        tourList = databaseHelper.getAllTours();
        if (tourList.isEmpty()) {
            addSampleTours();
            tourList = databaseHelper.getAllTours(); // Lấy lại danh sách sau khi thêm
        }

        tourAdapter = new TourAdapter(getContext(), tourList);
        recyclerViewTours.setAdapter(tourAdapter);

        return view;
    }

    private void addSampleCategories() {
        databaseHelper.addCategory("Du lịch núi", "travel");
        databaseHelper.addCategory("Du lịch biển", "travel");
        databaseHelper.addCategory("Du lịch thành phố", "travel");
    }

    private void addSampleTours() {
        databaseHelper.addTour("Tour Đà Lạt", "Đà Lạt", 150.0, 3, "travel", 1);
        databaseHelper.addTour("Tour Phú Quốc", "Phú Quốc", 200.0, 4, "welcome_png", 2);
        databaseHelper.addTour("Tour Hà Nội", "Hà Nội", 180.0, 2, "travel", 3);
        databaseHelper.addTour("Tour Sapa2", "Sapa", 220.0, 5, "travel", 3);
        databaseHelper.addTour("Tour Đà Lạt", "Đà Lạt", 150.0, 3, "travel", 1);
        databaseHelper.addTour("Tour Phú Quốc", "Phú Quốc", 200.0, 4, "travel", 2);
        databaseHelper.addTour("Tour Hà Nội", "Hà Nội", 180.0, 2, "travel", 3);
        databaseHelper.addTour("Tour Sapa", "Sapa", 220.0, 5, "travel", 3);
    }
}
