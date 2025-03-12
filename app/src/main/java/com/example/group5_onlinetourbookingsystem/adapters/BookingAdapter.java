package com.example.group5_onlinetourbookingsystem.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.BookingModel;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private Context context;
    private List<BookingModel> bookingList;
    private MyDatabaseHelper dbHelper;

    public BookingAdapter(Context context, List<BookingModel> bookingList, MyDatabaseHelper dbHelper) {
        this.context = context;
        this.bookingList = bookingList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingModel booking = bookingList.get(position);

        // Hiển thị đúng tên tour thay vì tourId
        holder.tvTourName.setText("Tour: " + booking.getName());
        holder.tvBookingDate.setText("Date: " + booking.getDate());
        holder.tvTotalPrice.setText(String.format("%,d $", (int) booking.getTotalPrice()));

        // Cấu hình Spinner Trạng thái Thanh toán
        String[] paymentStatuses = {"Pending", "Completed", "Failed"};
        ArrayAdapter<String> paymentAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, paymentStatuses);
        holder.spinnerPayment.setAdapter(paymentAdapter);
        holder.spinnerPayment.setSelection(getStatusPosition(paymentStatuses, booking.getPaymentStatus()));

        // Cấu hình Spinner Trạng thái Đặt chỗ
        String[] bookingStatuses = {"Pending", "Confirmed", "Cancelled"};
        ArrayAdapter<String> bookingAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, bookingStatuses);
        holder.spinnerBooking.setAdapter(bookingAdapter);
        holder.spinnerBooking.setSelection(getStatusPosition(bookingStatuses, booking.getStatus()));

        // Nếu trạng thái thanh toán chưa hoàn thành, khóa spinner booking
        holder.spinnerBooking.setEnabled(booking.getPaymentStatus().equalsIgnoreCase("Completed"));

        // Xác định màu dựa trên trạng thái thanh toán
        int statusColor;
        switch (booking.getPaymentStatus()) {
            case "Pending":
                statusColor = Color.parseColor("#FFC107"); // Màu vàng
                break;
            case "Completed":
                statusColor = Color.parseColor("#4CAF50"); // Màu xanh lá
                break;
            case "Failed":
                statusColor = Color.parseColor("#F44336"); // Màu đỏ
                break;
            default:
                statusColor = Color.GRAY; // Màu xám cho trạng thái không xác định
                break;
        }

        // Đổi màu `ImageView`
        holder.imgStatusIcon.setColorFilter(statusColor);

        // Xử lý cập nhật Trạng thái Thanh toán
        holder.spinnerPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newPaymentStatus = parent.getItemAtPosition(position).toString();
                if (!newPaymentStatus.equals(booking.getPaymentStatus())) {
                    dbHelper.updatePaymentStatus(booking.getId(), newPaymentStatus);
                    booking.setPaymentStatus(newPaymentStatus);
                    Toast.makeText(context, "Trạng thái thanh toán cập nhật!", Toast.LENGTH_SHORT).show();

                    // Cập nhật UI: Nếu thanh toán "Completed", mở khoá đặt chỗ
                    holder.spinnerBooking.setEnabled(newPaymentStatus.equalsIgnoreCase("Completed"));

                    // Cập nhật màu icon trạng thái
                    int newColor;
                    switch (newPaymentStatus) {
                        case "Pending":
                            newColor = Color.parseColor("#FFC107"); // Màu vàng
                            break;
                        case "Completed":
                            newColor = Color.parseColor("#4CAF50"); // Màu xanh lá
                            break;
                        case "Failed":
                            newColor = Color.parseColor("#F44336"); // Màu đỏ
                            break;
                        default:
                            newColor = Color.GRAY;
                            break;
                    }
                    holder.imgStatusIcon.setColorFilter(newColor);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Xử lý cập nhật Trạng thái Đặt chỗ
        holder.spinnerBooking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newBookingStatus = parent.getItemAtPosition(position).toString();
                if (!newBookingStatus.equals(booking.getStatus()) && booking.getPaymentStatus().equalsIgnoreCase("Completed")) {
                    dbHelper.updateBookingStatus(booking.getId(), newBookingStatus);
                    booking.setStatus(newBookingStatus);
                    Toast.makeText(context, "Trạng thái đặt chỗ cập nhật!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public int getItemCount() {
        return bookingList != null ? bookingList.size() : 0;
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvTourName, tvBookingDate, tvTotalPrice;
        Spinner spinnerPayment, spinnerBooking;
        ImageView imgStatusIcon;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTourName = itemView.findViewById(R.id.tvTourName);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            spinnerPayment = itemView.findViewById(R.id.spinnerPayment);
            spinnerBooking = itemView.findViewById(R.id.spinnerBooking);
            imgStatusIcon = itemView.findViewById(R.id.imgStatusIcon); // Đảm bảo ID này tồn tại trong XML
        }
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
