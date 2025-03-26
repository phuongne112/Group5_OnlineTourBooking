package com.example.group5_onlinetourbookingsystem.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
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
import com.example.group5_onlinetourbookingsystem.activities.b;

import java.util.ArrayList;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private Context context;
    private List<BookingModel> bookingList;
    private MyDatabaseHelper dbHelper;
    private boolean isAdmin;

    public BookingAdapter(Context context, List<BookingModel> bookingList, MyDatabaseHelper dbHelper, boolean isAdmin) {
        this.context = context;
        this.bookingList = bookingList;
        this.dbHelper = dbHelper;
        this.isAdmin = isAdmin;

//        this.bookingList = new ArrayList<>();
//        for (BookingModel booking : bookingList) {
//            if (booking.getTotalPrice() > 0) {
//                this.bookingList.add(booking);
//            }
//        }

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
        Log.d("BookingAdapter", "Tour Namesssssss: " + booking.getTourName());
        holder.tvTourName.setText(booking.getTourName());
        holder.tvBookingDate.setText("Date: " + booking.getDate());
        holder.tvTotalPrice.setText(String.format("%,d $", (int) booking.getTotalPrice()));

        // Load image
        String imageNameRaw = booking.getTourImage();
        String imageName = "placeholder_image";
        if (imageNameRaw != null && !imageNameRaw.trim().isEmpty()) {
            imageName = imageNameRaw.replace(".jpg", "").replace(".png", "").toLowerCase().trim();
        }
        int imageResId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        holder.imgTourImage.setImageResource(imageResId != 0 ? imageResId : R.drawable.placeholder_image);

        // Payment Spinner
        String[] paymentStatuses = {"Pending", "Completed", "Failed"};
        ArrayAdapter<String> paymentAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, paymentStatuses);
        holder.spinnerPayment.setAdapter(paymentAdapter);


        String bookingPaymentStatus = (booking.getPaymentStatus() == null || booking.getPaymentStatus().isEmpty())
                ? "Pending" : booking.getPaymentStatus();
        int paymentIndex = getStatusPosition(paymentStatuses, bookingPaymentStatus);
        holder.spinnerPayment.setSelection(paymentIndex);

        // Booking Spinner
        String[] bookingStatuses = {"Pending", "Confirmed", "Cancelled"};
        ArrayAdapter<String> bookingAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, bookingStatuses);
        holder.spinnerBooking.setAdapter(bookingAdapter);

        String bookingStatus = (booking.getStatus() == null || booking.getStatus().isEmpty())
                ? "Pending" : booking.getStatus();
        int bookingIndex = getStatusPosition(bookingStatuses, bookingStatus);
        holder.spinnerBooking.setSelection(bookingIndex);

        // Disable spinner if not admin
        holder.spinnerPayment.setEnabled(isAdmin);
        holder.spinnerBooking.setEnabled(isAdmin && bookingPaymentStatus.equalsIgnoreCase("Completed"));

        // Icon color
        int statusColor;
        switch (bookingPaymentStatus) {
            case "Pending":
                statusColor = Color.parseColor("#FFC107");
                break;
            case "Completed":
                statusColor = Color.parseColor("#4CAF50");
                break;
            case "Failed":
                statusColor = Color.parseColor("#F44336");
                break;
            default:
                statusColor = Color.GRAY;
                break;
        }
        holder.imgStatusIcon.setColorFilter(statusColor);

        // Set listeners only if admin
        if (isAdmin) {
            holder.spinnerPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String newPaymentStatus = parent.getItemAtPosition(position).toString();
                    if (!newPaymentStatus.equalsIgnoreCase(booking.getPaymentStatus())) {
                        dbHelper.updatePaymentStatus(booking.getId(), newPaymentStatus);
                        booking.setPaymentStatus(newPaymentStatus);
                        Toast.makeText(context, "Trạng thái thanh toán cập nhật!", Toast.LENGTH_SHORT).show();

                        holder.spinnerBooking.setEnabled(newPaymentStatus.equalsIgnoreCase("Completed"));

                        int newColor;
                        switch (newPaymentStatus) {
                            case "Pending":
                                newColor = Color.parseColor("#FFC107");
                                break;
                            case "Completed":
                                newColor = Color.parseColor("#4CAF50");
                                break;
                            case "Failed":
                                newColor = Color.parseColor("#F44336");
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

            holder.spinnerBooking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String newBookingStatus = parent.getItemAtPosition(position).toString();
                    if (!newBookingStatus.equalsIgnoreCase(booking.getStatus()) && booking.getPaymentStatus().equalsIgnoreCase("Completed")) {
                        dbHelper.updateBookingStatus(booking.getId(), newBookingStatus);
                        booking.setStatus(newBookingStatus);
                        Toast.makeText(context, "Trạng thái đặt chỗ cập nhật!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }

        // Click to detail
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookingDetailActivity.class);
            intent.putExtra("booking", booking);
            context.startActivity(intent);
        });

        Log.d("BookingAdapterDebug", "Booking ID: " + booking.getId());
        Log.d("BookingAdapterDebug", "Payment Status: " + booking.getPaymentStatus());
        Log.d("BookingAdapterDebug", "Booking Status: " + booking.getStatus());
    }

    @Override
    public int getItemCount() {
        return bookingList != null ? bookingList.size() : 0;
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvTourName, tvBookingDate, tvTotalPrice;
        Spinner spinnerPayment, spinnerBooking;
        ImageView imgStatusIcon, imgTourImage;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTourName = itemView.findViewById(R.id.tvTourName);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            spinnerPayment = itemView.findViewById(R.id.spinnerPayment);
            spinnerBooking = itemView.findViewById(R.id.spinnerBooking);
            imgStatusIcon = itemView.findViewById(R.id.imgStatusIcon);
            imgTourImage = itemView.findViewById(R.id.imgTourImage);
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
