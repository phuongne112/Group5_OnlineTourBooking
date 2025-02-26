package com.example.group5_onlinetourbookingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.activities.TourDetailActivity;
import com.example.group5_onlinetourbookingsystem.adapters.CategoryAdapter;
import com.example.group5_onlinetourbookingsystem.adapters.CategoryPageAdapter;
import com.example.group5_onlinetourbookingsystem.adapters.TourAdapter;
import com.example.group5_onlinetourbookingsystem.models.CategoryModel;
import com.example.group5_onlinetourbookingsystem.models.TourModel;

import java.util.ArrayList;
import java.util.List;

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

        if (getContext() == null) return view;

        databaseHelper = new MyDatabaseHelper(getContext());

        // 👉 Cấu hình RecyclerView danh mục (phân trang)
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.HORIZONTAL, false);
        recyclerViewCategories.setLayoutManager(layoutManager);

        categoryList = databaseHelper.getAllCategories();
        if (categoryList.isEmpty()) {
            addSampleCategories();
            categoryList = databaseHelper.getAllCategories(); // Lấy lại danh sách sau khi thêm
        }

        // 👉 Chia danh mục thành các trang nhỏ (6 mục/trang)
        List<List<CategoryModel>> categoryPages = paginateCategories(categoryList, 6);

        // 👉 Dùng CategoryPageAdapter (bọc CategoryAdapter)
        CategoryPageAdapter pageAdapter = new CategoryPageAdapter(getContext(), categoryPages, category -> {
            // Xử lý click danh mục
        });
        recyclerViewCategories.setAdapter(pageAdapter);

        // 👉 Thêm hiệu ứng cuộn theo từng trang
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

        // Truyền listener vào Adapter
        tourAdapter = new TourAdapter(getContext(), tourList, tour -> {
            // Chuyển sang TourDetailActivity khi click vào tour
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
        databaseHelper.addCategory("Du lịch Trời", "travel");
        databaseHelper.addCategory("Du lịch tây ", "travel");
        databaseHelper.addCategory("Du lịch thái", "travel");
        databaseHelper.addCategory("Du lịch B", "travel");
        databaseHelper.addCategory("Du lịch A", "travel");
        databaseHelper.addCategory("Du lịch C phố", "travel");
        databaseHelper.addCategory("Du lịch D", "travel");
        databaseHelper.addCategory("Du lịch E ", "travel");
        databaseHelper.addCategory("Du lịch F", "travel");
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


    private void addSampleCities() {
        databaseHelper.addCity("Hà Nội");
        databaseHelper.addCity("Hồ Chí Minh");
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
