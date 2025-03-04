package com.example.group5_onlinetourbookingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.TourModel;
import com.squareup.picasso.Picasso;

public class TourDetailActivity extends AppCompatActivity {
    private TextView tourName, tourDestination, tourPrice, tourDuration, tourCategory,tourDescription;
    private ImageView tourImage;
    private Button bookButton;
    private MyDatabaseHelper dbHelper;
    private int tourId;
    private double price; // Lưu giá tour dưới dạng số để truyền qua BookingActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);

        tourName = findViewById(R.id.detail_tour_name);
        tourDestination = findViewById(R.id.detail_tour_destination);
        tourPrice = findViewById(R.id.detail_tour_price);
        tourDuration = findViewById(R.id.detail_tour_duration);
        tourCategory = findViewById(R.id.detail_tour_category);
        tourDescription = findViewById(R.id.detail_tour_description); // Thêm mô tả
        tourImage = findViewById(R.id.detail_tour_image);
        bookButton = findViewById(R.id.book_button);

        dbHelper = new MyDatabaseHelper(this);

        // Lấy tour_id từ Intent
        tourId = getIntent().getIntExtra("tour_id", -1);
        if (tourId != -1) {
            TourModel tour = dbHelper.getTourById(tourId);
            if (tour != null) {
                tourName.setText(tour.getName());
                tourDestination.setText("Destination: " + tour.getDestination());
                price = tour.getPrice();
                tourPrice.setText(String.format("$%.2f", price));
                tourDuration.setText(tour.getDuration() + " days");
                tourCategory.setText("Category: " + tour.getCategoryId());
                // Hiển thị mô tả tour, kiểm tra null để tránh lỗi
                if (tour.getDescription() != null && !tour.getDescription().isEmpty()) {
                    tourDescription.setText(tour.getDescription());
                } else {
                    tourDescription.setText("Chưa có mô tả");
                }

                // Load ảnh từ database (nếu là URL)
                if (tour.getImage().startsWith("http")) {
                    Picasso.get().load(tour.getImage()).into(tourImage);
                } else {
                    // Nếu là ảnh trong drawable
                    int imageResource = getResources().getIdentifier(tour.getImage(), "drawable", getPackageName());
                    if (imageResource != 0) {
                        tourImage.setImageResource(imageResource);
                    } else {
                        tourImage.setImageResource(R.drawable.favorites);
                    }
                }
            } else {
                Toast.makeText(this, "Không tìm thấy thông tin tour!", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Lỗi: Không nhận được tour_id!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Sự kiện nhấn "Book Now"
        bookButton.setOnClickListener(view -> {
            Intent intent = new Intent(TourDetailActivity.this, BookingActivity.class);

            // Truyền dữ liệu sang BookingActivity
            intent.putExtra("tour_id", tourId);
            intent.putExtra("tour_name", tourName.getText().toString());
            intent.putExtra("tour_destination", tourDestination.getText().toString());
            intent.putExtra("tour_price", price); // Truyền số thực thay vì chuỗi
            intent.putExtra("tour_duration", tourDuration.getText().toString());
            intent.putExtra("tour_category", tourCategory.getText().toString());

            startActivity(intent);
        });
    }
}
