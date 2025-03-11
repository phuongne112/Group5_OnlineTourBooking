package com.example.group5_onlinetourbookingsystem.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;

import java.util.HashMap;

public class AdminBookingFragment extends Fragment {
    private TableLayout tableLayoutBookings;
    private TextView tvNoAdminBooking;
    private MyDatabaseHelper dbHelper;
    private HashMap<Integer, String> bookingStatusMap = new HashMap<>();
    private HashMap<Integer, String> paymentStatusMap = new HashMap<>();

    public AdminBookingFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_booking, container, false);
        tableLayoutBookings = view.findViewById(R.id.tableLayoutBookings);
        tvNoAdminBooking = view.findViewById(R.id.tvNoAdminBooking);
        dbHelper = new MyDatabaseHelper(requireContext());

        loadBookings();
        return view;
    }

    private void loadBookings() {
        tableLayoutBookings.removeAllViews(); // Xóa dữ liệu cũ
        Cursor cursor = dbHelper.getAllBookingsWithTourInfo();

        if (cursor == null || cursor.getCount() == 0) {
            tvNoAdminBooking.setVisibility(View.VISIBLE);
            return;
        }
        tvNoAdminBooking.setVisibility(View.GONE);

        // Thêm tiêu đề cột
        TableRow headerRow = new TableRow(getContext());
        headerRow.setPadding(8, 8, 8, 8);
        addHeaderCell(headerRow, "Tour", 2);
        addHeaderCell(headerRow, "Ngày", 2);
        addHeaderCell(headerRow, "Tổng tiền", 2);
        addHeaderCell(headerRow, "Trạng thái Thanh toán", 2);
        addHeaderCell(headerRow, "Trạng thái Đặt chỗ", 2);
        tableLayoutBookings.addView(headerRow);

        while (cursor.moveToNext()) {
            int bookingId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            String tourName = cursor.getString(cursor.getColumnIndexOrThrow("tour_name"));
            String bookingDate = cursor.getString(cursor.getColumnIndexOrThrow("booking_date"));
            double totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("total_price"));
            String bookingStatus = cursor.getString(cursor.getColumnIndexOrThrow("booking_status"));
            String paymentStatus = cursor.getString(cursor.getColumnIndexOrThrow("payment_status"));

            TableRow row = new TableRow(getContext());
            row.setPadding(8, 8, 8, 8);
            row.setBackgroundColor(Color.LTGRAY);

            addCell(row, tourName, 2);
            addCell(row, bookingDate, 2);
            addCell(row, String.format("%,d VND", (int) totalPrice), 2);

            // Spinner trạng thái Thanh toán
            Spinner paymentSpinner = createStatusSpinner(new String[]{"Pending", "Completed", "Failed"}, paymentStatus);
            row.addView(paymentSpinner);

            // Spinner trạng thái Đặt chỗ
            Spinner bookingSpinner = createStatusSpinner(new String[]{"Pending", "Confirmed", "Cancelled"}, bookingStatus);
            row.addView(bookingSpinner);

            paymentStatusMap.put(bookingId, paymentStatus);
            bookingStatusMap.put(bookingId, bookingStatus);

            // Cập nhật trạng thái Thanh toán
            paymentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String newPaymentStatus = parent.getItemAtPosition(position).toString();
                    if (!newPaymentStatus.equals(paymentStatus)) {
                        dbHelper.updatePaymentStatus(bookingId, newPaymentStatus);
                        paymentStatusMap.put(bookingId, newPaymentStatus);
                        Toast.makeText(getContext(), "Trạng thái thanh toán cập nhật!", Toast.LENGTH_SHORT).show();
                        loadBookings();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            // Cập nhật trạng thái Đặt chỗ (Chỉ khi Thanh toán = Completed)
            bookingSpinner.setEnabled(paymentStatus.equalsIgnoreCase("Completed"));
            bookingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String newBookingStatus = parent.getItemAtPosition(position).toString();
                    if (!newBookingStatus.equals(bookingStatus) && paymentStatusMap.get(bookingId).equalsIgnoreCase("Completed")) {
                        dbHelper.updateBookingStatus(bookingId, newBookingStatus);
                        bookingStatusMap.put(bookingId, newBookingStatus);
                        Toast.makeText(getContext(), "Trạng thái đặt chỗ cập nhật!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            tableLayoutBookings.addView(row);
        }
        cursor.close();
    }

    private Spinner createStatusSpinner(String[] statusArray, String currentStatus) {
        Spinner spinner = new Spinner(getContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, statusArray);
        spinner.setAdapter(adapter);
        spinner.setSelection(getStatusPosition(statusArray, currentStatus));
        return spinner;
    }

    private void addCell(TableRow row, String text, float weight) {
        TextView tv = new TextView(getContext());
        tv.setText(text);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.BLACK);
        tv.setPadding(8, 8, 8, 8);
        row.addView(tv);
    }

    private void addHeaderCell(TableRow row, String text, float weight) {
        TextView tv = new TextView(getContext());
        tv.setText(text);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.WHITE);
        tv.setPadding(8, 8, 8, 8);
        tv.setBackgroundColor(Color.DKGRAY);
        row.addView(tv);
    }

    private int getStatusPosition(String[] statusArray, String status) {
        for (int i = 0; i < statusArray.length; i++) {
            if (statusArray[i].equalsIgnoreCase(status)) {
                return i;
            }
        }
        return 0;
    }
}
