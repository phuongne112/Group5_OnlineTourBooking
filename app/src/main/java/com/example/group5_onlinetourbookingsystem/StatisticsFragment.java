package com.example.group5_onlinetourbookingsystem;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
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

        barChart = view.findViewById(R.id.barChart);
        databaseHelper = new MyDatabaseHelper(requireContext());

        loadChartData(); // Gọi hàm vẽ biểu đồ

        return view;
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
}
