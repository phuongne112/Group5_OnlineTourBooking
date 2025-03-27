package com.example.group5_onlinetourbookingsystem.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.List;

public class StatisticsFragment extends Fragment {
    private BarChart barChart;
    private MyDatabaseHelper databaseHelper;
    private EditText etDay, etMonthYear;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        // Khởi tạo các view cần thiết
        barChart = view.findViewById(R.id.barChart);
        etDay = view.findViewById(R.id.etDay);
        etMonthYear = view.findViewById(R.id.etMonthYear);
        databaseHelper = new MyDatabaseHelper(requireContext());

        // Thêm sự kiện click để mở DatePicker
        etDay.setOnClickListener(v -> showDatePickerDialog(true)); // Chọn ngày
        etMonthYear.setOnClickListener(v -> showDatePickerDialog(true)); // Chọn tháng/năm

        // Cập nhật dữ liệu khi bấm nút thống kê
        LinearLayout navTour = view.findViewById(R.id.nav_tour);
        navTour.setOnClickListener(v -> loadChartData());

        // Vẽ biểu đồ mặc định khi vào trang
        loadChartData();

        return view;
    }

    // Hàm để hiển thị DatePickerDialog
    private void showDatePickerDialog(boolean isDay) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year1, month1, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            if (isDay) {
                etDay.setText(selectedDate);
            } else {
                etMonthYear.setText((month1 + 1) + "/" + year1);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                dateSetListener,
                year, month, day
        );

        datePickerDialog.show();
    }

    // Hàm lấy dữ liệu và vẽ biểu đồ
    private void loadChartData() {
        String startDate = etDay.getText().toString();
        String endDate = etMonthYear.getText().toString();

        List<BarEntry> entries = new ArrayList<>();

        if (startDate.isEmpty() || endDate.isEmpty()) {
            // Nếu không có ngày tháng, vẽ tất cả dữ liệu
            int totalBookings = databaseHelper.getBookingsCountInRange(0, 0, 0, 9999, 12, 31); // Lấy tất cả dữ liệu
            int bookedTours = databaseHelper.getBookedToursCountInRange(0, 0, 0, 9999, 12, 31);
            float totalRevenue = (float) databaseHelper.getTotalRevenueInRange(0, 0, 0, 9999, 12, 31);

            entries.add(new BarEntry(0, totalBookings)); // Booking Count
            entries.add(new BarEntry(1, bookedTours));   // Tour Count
            entries.add(new BarEntry(2, totalRevenue));  // Revenue
        } else {
            // Nếu có khoảng thời gian, vẽ dữ liệu theo khoảng thời gian
            String[] startDateParts = startDate.split("/");
            String[] endDateParts = endDate.split("/");

            int startDay = Integer.parseInt(startDateParts[0]);
            int startMonth = Integer.parseInt(startDateParts[1]) - 1; // Tháng trong Calendar bắt đầu từ 0
            int startYear = Integer.parseInt(startDateParts[2]);

            int endDay = Integer.parseInt(endDateParts[0]);
            int endMonth = Integer.parseInt(endDateParts[1]) - 1;
            int endYear = Integer.parseInt(endDateParts[2]);

            // Truy vấn cơ sở dữ liệu theo khoảng thời gian đã chọn
            int totalBookings = databaseHelper.getBookingsCountInRange(startYear, startMonth, startDay, endYear, endMonth, endDay);
            int bookedTours = databaseHelper.getBookedToursCountInRange(startYear, startMonth, startDay, endYear, endMonth, endDay);
            float totalRevenue = (float) databaseHelper.getTotalRevenueInRange(startYear, startMonth, startDay, endYear, endMonth, endDay);

            entries.add(new BarEntry(0, totalBookings)); // Booking Count
            entries.add(new BarEntry(1, bookedTours));   // Tour Count
            entries.add(new BarEntry(2, totalRevenue));  // Revenue
        }

        // Tạo dữ liệu cho biểu đồ
        BarDataSet dataSet = new BarDataSet(entries, "Thống kê");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);

        // Cấu hình trục X
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

        // Cấu hình trục Y
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        barChart.getAxisRight().setEnabled(false);

        barChart.invalidate(); // Vẽ lại biểu đồ
    }
}
