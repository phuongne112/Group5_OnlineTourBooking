package com.example.group5_onlinetourbookingsystem.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.activities.AdminDashboardActivity;
import com.example.group5_onlinetourbookingsystem.activities.TourDetailActivity;
import com.example.group5_onlinetourbookingsystem.activities.TourGuideDashboardActivity;
import com.example.group5_onlinetourbookingsystem.adapters.CategoryAdapter;
import com.example.group5_onlinetourbookingsystem.adapters.TourAdapter;
import com.example.group5_onlinetourbookingsystem.models.CategoryModel;
import com.example.group5_onlinetourbookingsystem.models.TourModel;
import com.example.group5_onlinetourbookingsystem.utils.SessionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewCategories, recyclerViewTours;
    private CategoryAdapter categoryAdapter;
    private TourAdapter tourAdapter;
    private ArrayList<CategoryModel> categoryList;
    private ArrayList<TourModel> tourList;
    private MyDatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private TextView textViewUserName;
    private ProgressBar progressBar; // ProgressBar hiển thị khi tải dữ liệu
    private int currentPage = 0;
    private final int PAGE_SIZE = 5; // Số tour tải mỗi lần
    private boolean isLoading = false; // Kiểm soát trạng thái tải dữ liệu

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        if (getContext() == null) return view;

        sessionManager = new SessionManager(requireContext());
        databaseHelper = new MyDatabaseHelper(requireContext());
// 🔹 Kiểm tra Role ID trước khi load nội dung
        checkUserRole();
        // Kiểm tra và thêm dữ liệu mẫu nếu cần
        addSampleRoles();
        addSampleCategories();
        addSampleTours();
        addSampleCities(); // 🔹 Gọi để thêm dữ liệu mẫu khi khởi chạy
        // Gán UI components
        EditText editTextSearch = view.findViewById(R.id.editTextSearch);
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        recyclerViewTours = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progressBar); // Gán ID ProgressBar

        // Cấu hình tìm kiếm tour
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchTours(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Cấu hình RecyclerView danh mục
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryList = databaseHelper.getAllCategories();
        categoryAdapter = new CategoryAdapter(getContext(), categoryList, category -> {
            Log.d("HomeFragment", "Clicked category: " + category.getName());
            filterToursByCategory(category.getId());
        });
        recyclerViewCategories.setAdapter(categoryAdapter);

        // Cấu hình RecyclerView danh sách tour
        recyclerViewTours.setLayoutManager(new LinearLayoutManager(getContext()));
        tourList = new ArrayList<>();
        tourAdapter = new TourAdapter(getContext(), tourList, tour -> {
            Intent intent = new Intent(getContext(), TourDetailActivity.class);
            intent.putExtra("tour_id", tour.getId());
            startActivity(intent);
        });
        recyclerViewTours.setAdapter(tourAdapter);

        // Tải dữ liệu ban đầu
        loadMoreTours();

        // Xử lý sự kiện cuộn để tải thêm dữ liệu
        recyclerViewTours.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading && layoutManager != null && layoutManager.findLastVisibleItemPosition() >= tourList.size() - 1) {
                    loadMoreTours();
                }
            }
        });

        return view;
    }

    private void loadMoreTours() {
        if (isLoading) return; // Tránh gọi nhiều lần

        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(() -> {
            ArrayList<TourModel> newTours = databaseHelper.getToursByPage(currentPage * PAGE_SIZE, PAGE_SIZE);
            if (!newTours.isEmpty()) {
                tourList.addAll(newTours);
                tourAdapter.notifyDataSetChanged();
                currentPage++;
            }
            isLoading = false;
            progressBar.setVisibility(View.GONE);
        }, 1000); // Giả lập delay tải dữ liệu
    }

    private void searchTours(String query) {
        tourList.clear();
        if (query.isEmpty()) {
            tourList.addAll(databaseHelper.getAllTours());
        } else {
            tourList.addAll(databaseHelper.searchTours(query));
        }
        tourAdapter.notifyDataSetChanged();
    }

    private void filterToursByCategory(int categoryId) {
        tourList.clear();
        if (categoryId == -1) { // Nếu chọn "Tất cả danh mục"
            tourList.addAll(databaseHelper.getAllTours());
        } else {
            tourList.addAll(databaseHelper.getToursByCategory(categoryId));
        }
        tourAdapter.notifyDataSetChanged();
    }

    private void addSampleRoles() {
        if (databaseHelper.getAllRoles().isEmpty()) {
            databaseHelper.addRole("Customer");
            databaseHelper.addRole("Admin");
            databaseHelper.addRole("Tour Guide");
        }
    }
    private void addSampleCities() {
        if (databaseHelper.getAllCities().isEmpty()) { // Kiểm tra nếu chưa có dữ liệu
            databaseHelper.addCity("Hà Nội");
            databaseHelper.addCity("Đà Nẵng");
            databaseHelper.addCity("Hồ Chí Minh");
            databaseHelper.addCity("Đà Lạt");
            databaseHelper.addCity("Nha Trang");
        }
    }

    private void addSampleCategories() {
        if (databaseHelper.getAllCategories().isEmpty()) {
            databaseHelper.addCategory("Du lịch núi", "mountain");
            databaseHelper.addCategory("Du lịch biển", "sea");
            databaseHelper.addCategory("Du lịch thành phố", "city");
            databaseHelper.addCategory("Du lịch sinh thái", "eco");
            databaseHelper.addCategory("Du lịch văn hóa", "cultural");
            databaseHelper.addCategory("Du lịch phiêu lưu", "adventure");
        }
    }

    private void addSampleTours() {
        if (databaseHelper.getAllTours().isEmpty()) {
            // ✅ Sao chép ảnh từ drawable vào bộ nhớ trong
            String dalatImagePath = copyDrawableToInternalStorage(R.drawable.dalat_tour, "dalat_tour.jpg");
            String phuquocImagePath = copyDrawableToInternalStorage(R.drawable.phuquoc_tour, "phuquoc_tour.jpg");
            String hanoiImagePath = copyDrawableToInternalStorage(R.drawable.hanoi_tour, "hanoi_tour.jpg");

            // ✅ Thêm tour với đường dẫn ảnh từ bộ nhớ trong
            databaseHelper.addTour("Tour Đà Lạt", "Đà Lạt", 1, 150.0, 3, dalatImagePath, 1, "2025-03-10 08:00:00", "Thưởng thức khí hậu mát mẻ và cảnh đẹp thơ mộng của Đà Lạt.");
            databaseHelper.addTour("Tour Phú Quốc", "Phú Quốc", 2, 200.0, 4, phuquocImagePath, 2, "2025-03-12 09:30:00", "Khám phá hòn đảo ngọc với bãi biển tuyệt đẹp và hải sản tươi ngon.");
            databaseHelper.addTour("Tour Hà Nội", "Hà Nội", 3, 180.0, 3, hanoiImagePath, 3, "2025-03-15 07:45:00", "Trải nghiệm văn hóa, lịch sử thủ đô với 36 phố phường và Hồ Gươm.");
        }
    }


    /**
     * ✅ Sao chép ảnh từ drawable vào bộ nhớ trong và trả về đường dẫn file.
     */
    private String copyDrawableToInternalStorage(int drawableId, String fileName) {
        File directory = new File(requireContext().getFilesDir(), "tour_images");

        if (!directory.exists()) {
            directory.mkdirs(); // Tạo thư mục nếu chưa tồn tại
        }

        File file = new File(directory, fileName);
        if (!file.exists()) { // Chỉ sao chép nếu chưa tồn tại
            try (InputStream inputStream = getResources().openRawResource(drawableId);
                 FileOutputStream outputStream = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null; // Lỗi khi sao chép ảnh
            }
        }
        return file.getAbsolutePath(); // Trả về đường dẫn đầy đủ
    }

    private void checkUserRole() {
        int roleId = sessionManager.getUserRoleId();
        Log.d("SESSION_DEBUG", "Role ID tại HomeFragment: " + roleId);

        // Nếu user là Admin hoặc Tour Guide, chuyển họ về đúng Dashboard
        if (roleId == 2) {
            startActivity(new Intent(getActivity(), AdminDashboardActivity.class));
            getActivity().finish();
        } else if (roleId == 3) {
            startActivity(new Intent(getActivity(), TourGuideDashboardActivity.class));
            getActivity().finish();
        }
    }

}
