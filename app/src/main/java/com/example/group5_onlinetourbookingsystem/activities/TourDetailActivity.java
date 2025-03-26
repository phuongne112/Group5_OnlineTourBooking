package com.example.group5_onlinetourbookingsystem.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.models.TourModel;
import com.example.group5_onlinetourbookingsystem.utils.SessionManager;
import com.squareup.picasso.Picasso;

import java.io.File;

public class TourDetailActivity extends AppCompatActivity {
    private TextView tourName, tourDestination, tourPrice, tourDuration, tourCategory, tourDescription;
    private ImageView tourImage;
    private ImageButton favoriteButton; // 🔹 Added favorite button
    private Button bookButton;
    private MyDatabaseHelper dbHelper;
    private int tourId;
    private SessionManager sessionManager;
    private double price;
    private TourModel tour;
    private int userId;
    private boolean isFavorited; // 🔹 Check if tour is already favorited

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Kích hoạt nút Back

        }
        // ✅ Initialize DB and session manager
        dbHelper = new MyDatabaseHelper(this);
        sessionManager = new SessionManager(this);
        userId = sessionManager.getUserId(); // ✅ Get logged-in user ID

        // ✅ Bind UI elements
        tourName = findViewById(R.id.detail_tour_name);
        tourDestination = findViewById(R.id.detail_tour_destination);
        tourPrice = findViewById(R.id.detail_tour_price);
        tourDuration = findViewById(R.id.detail_tour_duration);
        tourCategory = findViewById(R.id.detail_tour_category);
        tourDescription = findViewById(R.id.detail_tour_description);
        tourImage = findViewById(R.id.detail_tour_image);
        bookButton = findViewById(R.id.book_button);
        favoriteButton = findViewById(R.id.favorite_button); // 🔹 Bind favorite button

        // ✅ Get tour ID from Intent
        tourId = getIntent().getIntExtra("tour_id", -1);

        if (tourId != -1) {
            tour = dbHelper.getTourById(tourId);
            if (tour != null) {
                // ✅ Display tour details
                tourName.setText(tour.getName());
                tourDestination.setText("Destination: " + tour.getDestination());
                price = tour.getPrice();
                tourPrice.setText(String.format("$%.2f", price));
                tourDuration.setText(tour.getDuration() + " days");
                tourCategory.setText("Category: " + tour.getCategoryName());
                tourDescription.setText((tour.getDescription() != null && !tour.getDescription().isEmpty())
                        ? tour.getDescription()
                        : "Chưa có mô tả");

                // ✅ Load tour image từ bộ nhớ trong
                if (tour.getImage() != null && !tour.getImage().isEmpty()) {
                    File imageFile = new File(tour.getImage());
                    if (imageFile.exists()) {
                        Picasso.get().load(imageFile).into(tourImage);
                    } else {
                        tourImage.setImageResource(R.drawable.favorites); // Ảnh mặc định nếu file không tồn tại
                    }
                } else {
                    tourImage.setImageResource(R.drawable.favorites);
                }




                // ✅ Check if this tour is already in favorites
                isFavorited = dbHelper.isFavorite(userId, tourId);
                updateFavoriteButton();

                // ✅ Handle favorite button click
                favoriteButton.setOnClickListener(view -> {
                    if (sessionManager.isLoggedIn()) {
                        if (isFavorited) {
                            dbHelper.removeFromFavorites(userId, tourId);
                            isFavorited = false;
                            Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                        } else {
                            dbHelper.addToFavorites(userId, tourId);
                            isFavorited = true;
                            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
                        }
                        updateFavoriteButton();
                    } else {
                        Toast.makeText(this, "You need to log in to favorite this tour!", Toast.LENGTH_SHORT).show();
                    }
                });

                // ✅ Handle "Book Now" button
                bookButton.setOnClickListener(view -> {
                    if (sessionManager.isLoggedIn()) {
                        Intent intent = new Intent(TourDetailActivity.this, BookingActivity.class);
                        intent.putExtra("tour_id", tourId);
                        intent.putExtra("tour_name", tour.getName());
                        intent.putExtra("tour_destination", tour.getDestination());
                        intent.putExtra("tour_price", price);
                        intent.putExtra("tour_duration", tour.getDuration() + " days");
                        intent.putExtra("tour_category", tour.getCategoryName());

                        // ✅ Check and pass start_time
                        if (tour.getStartTime() != null) {
                            intent.putExtra("start_time", tour.getStartTime());
                        }

                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Bạn cần đăng nhập để đặt tour!", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(this, "Không tìm thấy thông tin tour!", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Lỗi: Không nhận được tour_id!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    // ✅ Function to update favorite button icon
    private void updateFavoriteButton() {
        if (isFavorited) {
            favoriteButton.setImageResource(R.drawable.favorite_selected); // 🔹 Change icon when favorited
        } else {
            favoriteButton.setImageResource(R.drawable.favourite); // 🔹 Default icon
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Quay lại màn hình trước đó
        return true;
    }

}
