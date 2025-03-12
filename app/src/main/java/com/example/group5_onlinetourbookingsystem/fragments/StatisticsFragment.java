package com.example.group5_onlinetourbookingsystem.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.MainActivity;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.activities.ForgotPasswordActivity;
import com.example.group5_onlinetourbookingsystem.activities.ManageAccountActivity;
import com.example.group5_onlinetourbookingsystem.activities.ManageCategoryActivity;
import com.example.group5_onlinetourbookingsystem.activities.ManageCityActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {
    private BarChart barChart;
    private MyDatabaseHelper databaseHelper;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        LinearLayout navTour = view.findViewById(R.id.nav_tour);

        barChart = view.findViewById(R.id.barChart);
        databaseHelper = new MyDatabaseHelper(requireContext());

        loadChartData(); // Gọi hàm vẽ biểu đồ
// Xử lý khi bấm vào `nav_tour`
        navTour.setOnClickListener(v -> navigateToFragment(new AdminTourFragment()));



        return view;


    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout navAccountAdmin = view.findViewById(R.id.nav_accountAdmin);

        navAccountAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ManageAccountActivity.class);
            startActivity(intent);
        });
        LinearLayout cityNav = view.findViewById(R.id.nav_city);
        cityNav.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ManageCityActivity.class);
            startActivity(intent);
        });
        LinearLayout categoryNav = view.findViewById(R.id.nav_category);
        categoryNav.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ManageCategoryActivity.class);
            startActivity(intent);
        });
    }



    // 🛠️ **Hàm lấy dữ liệu & vẽ biểu đồ**
    private void loadChartData() {
        int totalBookings = databaseHelper.getTotalBookings();
        int bookedTours = databaseHelper.getBookedToursCount();
        float totalRevenue = (float) databaseHelper.getTotalRevenue();

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, totalBookings)); // Booking Count
        entries.add(new BarEntry(1, bookedTours));   // Tour Count
        entries.add(new BarEntry(2, totalRevenue));  // Revenue

        BarDataSet dataSet = new BarDataSet(entries, "Thống kê");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);

        // 🛠️ **Cấu hình trục X**
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(3);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                switch ((int) value) {
                    case 0: return "Bookings";
                    case 1: return "Tours";
                    case 2: return "Revenue";
                    default: return "";
                }
            }
        });



        // 🛠️ **Cấu hình trục Y**
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        barChart.getAxisRight().setEnabled(false); // Ẩn trục Y bên phải

        barChart.invalidate(); // 🔄 **Vẽ lại biểu đồ**
    }
    private void navigateToFragment(Fragment fragment) {
        if (getActivity() != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.cardViewStatistics, fragment);
// Đảm bảo ID này tồn tại
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }



}
