package com.example.group5_onlinetourbookingsystem.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.BookingModel;

public class BookingDetailActivity extends AppCompatActivity {

    private TextView tvTourName, tvTourDesc, tvUserName, tvUserEmail, tvUserPhone;
    private TextView tvDate, tvTotalPrice, tvAdultChild, tvNote, tvBookingStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Kích hoạt nút Back

        }
        int bookingId = ((BookingModel) getIntent().getSerializableExtra("booking")).getId();

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        BookingModel booking = dbHelper.getBookingWithDetails(bookingId);

        // Ánh xạ view
        tvTourName = findViewById(R.id.tvTourName);
        tvTourDesc = findViewById(R.id.tvTourDesc);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserPhone = findViewById(R.id.tvUserPhone);
        tvDate = findViewById(R.id.tvDate);

        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvAdultChild = findViewById(R.id.tvAdultChild);
        tvNote = findViewById(R.id.tvNote);

        tvBookingStatus = findViewById(R.id.tvBookingStatus);

        if (booking != null) {
            tvTourName.setText("Tên tour: " + booking.getTourName());
            tvTourDesc.setText("Mô tả: " + booking.getTourDesc());
            tvUserName.setText("Người đặt: " + booking.getName());
            tvUserEmail.setText("Email: " + booking.getUserEmail());
            tvUserPhone.setText("SĐT: " + booking.getUserPhone());
            tvDate.setText("Ngày đặt: " + booking.getDate());

            tvTotalPrice.setText("Tổng giá: " + String.format("%,.0f $", booking.getTotalPrice()));
            tvAdultChild.setText("Người lớn: " + booking.getAdultCount() + ", Trẻ em: " + booking.getChildCount());
            tvNote.setText("Ghi chú: " + booking.getNote());

            tvBookingStatus.setText("Trạng thái đặt chỗ: " + booking.getStatus());
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Quay lại màn hình trước đó
        return true;
    }
}
