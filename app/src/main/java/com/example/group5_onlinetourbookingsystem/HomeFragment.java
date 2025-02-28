package com.example.group5_onlinetourbookingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.activities.HomePage;

import com.example.group5_onlinetourbookingsystem.activities.TourDetailActivity;
import com.example.group5_onlinetourbookingsystem.adapters.CategoryAdapter;
import com.example.group5_onlinetourbookingsystem.adapters.CategoryPageAdapter;
import com.example.group5_onlinetourbookingsystem.adapters.TourAdapter;
import com.example.group5_onlinetourbookingsystem.models.CategoryModel;
import com.example.group5_onlinetourbookingsystem.models.TourModel;
import com.example.group5_onlinetourbookingsystem.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewTours, recyclerViewCategories;
    private TourAdapter tourAdapter;
    private CategoryAdapter categoryAdapter;
    private ArrayList<TourModel> tourList;
    private ArrayList<CategoryModel> categoryList;
    private MyDatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private TextView textViewUserName;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        if (getContext() == null) return view;
        sessionManager = new SessionManager(requireContext());
        databaseHelper = new MyDatabaseHelper(requireContext());



        // 👉 Cấu hình RecyclerView danh mục (phân trang)
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.HORIZONTAL, false);
        recyclerViewCategories.setLayoutManager(layoutManager);

        categoryList = databaseHelper.getAllCategories();
        if (categoryList.isEmpty()) {
            addSampleCategories();
            categoryList = databaseHelper.getAllCategories();
        }

        List<List<CategoryModel>> categoryPages = paginateCategories(categoryList, 6);
        CategoryPageAdapter pageAdapter = new CategoryPageAdapter(getContext(), categoryPages, category -> {});
        recyclerViewCategories.setAdapter(pageAdapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerViewCategories);

        // 👉 Cấu hình RecyclerView tour
        recyclerViewTours = view.findViewById(R.id.recycler_view);
        recyclerViewTours.setLayoutManager(new LinearLayoutManager(getContext()));

        tourList = databaseHelper.getAllTours();
        if (tourList.isEmpty()) {
            addSampleTours();
            tourList = databaseHelper.getAllTours();
        }

        tourAdapter = new TourAdapter(getContext(), tourList, tour -> {
            Intent intent = new Intent(getContext(), TourDetailActivity.class);
            intent.putExtra("tour_id", tour.getId());
            startActivity(intent);
        });

        recyclerViewTours.setAdapter(tourAdapter);

        return view;
    }

    private void addSampleCategories() {
        databaseHelper.addCategory("Du lịch núi", "travel");
        databaseHelper.addCategory("Du lịch biển", "travel");
        databaseHelper.addCategory("Du lịch thành phố", "travel");
        databaseHelper.addCategory("Du lịch sinh thái", "eco_travel");
        databaseHelper.addCategory("Du lịch văn hóa", "cultural_travel");
        databaseHelper.addCategory("Du lịch phiêu lưu", "adventure_travel");
    }

    private void addSampleTours() {
        databaseHelper.addTour("Tour Đà Lạt", "Đà Lạt", 1, 150.0, 3, "travel", 1);
        databaseHelper.addTour("Tour Phú Quốc", "Phú Quốc", 2, 200.0, 4, "welcome_png", 2);
    }

    private void addDefaultUser() {
        if (!databaseHelper.isUserExists("admin@gmail.com")) {
            databaseHelper.addUser(
                    "Admin",
                    "admin@gmail.com",
                    "0123456789",
                    "admin123",
                    "1990-01-01",
                    "default_user"
            );
        }
    }

    private List<List<CategoryModel>> paginateCategories(List<CategoryModel> categories, int itemsPerPage) {
        List<List<CategoryModel>> pages = new ArrayList<>();
        int totalCategories = categories.size();
        for (int i = 0; i < totalCategories; i += itemsPerPage) {
            int end = Math.min(i + itemsPerPage, totalCategories);
            pages.add(categories.subList(i, end));
        }
        return pages;
    }
}
