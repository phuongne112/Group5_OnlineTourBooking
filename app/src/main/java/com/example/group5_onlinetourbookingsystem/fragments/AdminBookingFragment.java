package com.example.group5_onlinetourbookingsystem.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;

import java.util.HashMap;

public class AdminBookingFragment extends Fragment {
    private ListView listViewAdminBookings;
    private TextView tvNoAdminBooking;
    private MyDatabaseHelper dbHelper;
    private SimpleCursorAdapter adapter;
    private HashMap<Integer, String> bookingStatusMap = new HashMap<>();

    public AdminBookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_booking, container, false);
        listViewAdminBookings = view.findViewById(R.id.listViewAdminBookings);
        tvNoAdminBooking = view.findViewById(R.id.tvNoAdminBooking);
        dbHelper = new MyDatabaseHelper(requireContext());

        loadBookings();

        return view;
    }

    private void loadBookings() {
        Cursor cursor = dbHelper.getAllBookingsWithTourInfo();

        if (cursor != null && cursor.getCount() > 0) {
            String[] from = {"tour_name", "booking_date", "adult_count", "child_count", "total_price"};
            int[] to = {R.id.tvBookingTourName, R.id.tvBookingDate, R.id.tvBookingAdultsChildren, R.id.tvBookingTotalPrice};

            adapter = new SimpleCursorAdapter(
                    requireContext(),
                    R.layout.list_item_admin_booking,
                    cursor,
                    from,
                    to,
                    0
            );

            adapter.setViewBinder((view, cursor1, columnIndex) -> {
                int bookingId = cursor1.getInt(cursor1.getColumnIndexOrThrow("_id"));

                if (view.getId() == R.id.tvBookingAdultsChildren) {
                    int adults = cursor1.getInt(cursor1.getColumnIndexOrThrow("adult_count"));
                    int children = cursor1.getInt(cursor1.getColumnIndexOrThrow("child_count"));
                    ((TextView) view).setText("Người lớn: " + adults + " - Trẻ em: " + children);
                    return true;
                } else if (view.getId() == R.id.tvBookingTotalPrice) {
                    double totalPrice = cursor1.getDouble(cursor1.getColumnIndexOrThrow("total_price"));
                    ((TextView) view).setText(String.format("Tổng tiền: %,d VND", (int) totalPrice));
                    return true;
                } else if (view.getId() == R.id.spinnerBookingStatus) {
                    Spinner spinner = (Spinner) view;
                    String status = cursor1.getString(cursor1.getColumnIndexOrThrow("status"));

                    // Lưu trạng thái vào HashMap
                    bookingStatusMap.put(bookingId, status);

                    if (spinner.getAdapter() == null) {
                        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                                requireContext(), R.array.booking_status_options, android.R.layout.simple_spinner_item);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(spinnerAdapter);
                    }

                    spinner.setSelection(getStatusPosition(status), false);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String newStatus = parent.getItemAtPosition(position).toString();
                            bookingStatusMap.put(bookingId, newStatus); // Cập nhật trạng thái tạm thời
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });

                    return true;
                } else if (view.getId() == R.id.btnSaveStatus) {
                    Button btnSave = (Button) view;
                    btnSave.setOnClickListener(v -> {
                        String newStatus = bookingStatusMap.get(bookingId);
                        dbHelper.updateBookingStatus(bookingId, newStatus);
                        Toast.makeText(requireContext(), "Trạng thái đã được cập nhật!", Toast.LENGTH_SHORT).show();
                        loadBookings(); // 🔄 Refresh lại danh sách sau khi cập nhật
                    });

                    return true;
                }
                return false;
            });

            listViewAdminBookings.setAdapter(adapter);
            tvNoAdminBooking.setVisibility(View.GONE);
            listViewAdminBookings.setVisibility(View.VISIBLE);
        } else {
            tvNoAdminBooking.setVisibility(View.VISIBLE);
            listViewAdminBookings.setVisibility(View.GONE);
            if (cursor != null) cursor.close();
        }
    }


    private int getStatusPosition(String status) {
        String[] statusArray = getResources().getStringArray(R.array.booking_status_options);
        for (int i = 0; i < statusArray.length; i++) {
            if (statusArray[i].equalsIgnoreCase(status)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Close the cursor when the fragment view is destroyed
        if (adapter != null && adapter.getCursor() != null) {
            adapter.getCursor().close();
        }
    }
}