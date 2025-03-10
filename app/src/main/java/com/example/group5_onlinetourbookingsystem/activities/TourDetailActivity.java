package com.example.group5_onlinetourbookingsystem.activities;

import android.content.Intent;
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

public class TourDetailActivity extends AppCompatActivity {
    private TextView tourName, tourDestination, tourPrice, tourDuration, tourCategory, tourDescription;
    private ImageView tourImage;
    private ImageButton favoriteButton;  // ⭐ Nút yêu thích
    private Button bookButton;
    private MyDatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private int tourId, userId;
    private boolean isFavorite; // Trạng thái yêu thích
    private TourModel tour;
    private double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);

        dbHelper = new MyDatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // ✅ Ánh xạ UI
        tourName = findViewById(R.id.detail_tour_name);
        tourDestination = findViewById(R.id.detail_tour_destination);
        tourPrice = findViewById(R.id.detail_tour_price);
        tourDuration = findViewById(R.id.detail_tour_duration);
        tourCategory = findViewById(R.id.detail_tour_category);
        tourDescription = findViewById(R.id.detail_tour_description);
        tourImage = findViewById(R.id.detail_tour_image);
        bookButton = findViewById(R.id.book_button);
        favoriteButton = findViewById(R.id.favorite_button); // ⭐

        // ✅ Lấy user_id từ Session
        userId = sessionManager.getUserId();

        // ✅ Lấy tour_id từ Intent
        tourId = getIntent().getIntExtra("tour_id", -1);

        if (tourId != -1) {
            tour = dbHelper.getTourById(tourId);
            if (tour != null) {
                // ✅ Hiển thị thông tin Tour
                tourName.setText(tour.getName());
                tourDestination.setText("Destination: " + tour.getDestination());
                price = tour.getPrice();
                tourPrice.setText(String.format("$%.2f", price));
                tourDuration.setText(tour.getDuration() + " days");
                tourCategory.setText("Category: " + tour.getCategoryName());

                // ✅ Xử lý mô tả tour
                tourDescription.setText((tour.getDescription() != null && !tour.getDescription().isEmpty())
                        ? tour.getDescription()
                        : "Chưa có mô tả");

                // ✅ Xử lý hình ảnh
                if (tour.getImage().startsWith("http")) {
                    Picasso.get().load(tour.getImage()).into(tourImage);
                } else {
                    int imageResource = getResources().getIdentifier(tour.getImage(), "drawable", getPackageName());
                    tourImage.setImageResource(imageResource != 0 ? imageResource : R.drawable.favorites);
                }

                // ⭐ Kiểm tra xem tour có trong danh sách yêu thích không
                isFavorite = dbHelper.isFavorite(userId, tourId);
                updateFavoriteIcon();

                // ⭐ Xử lý khi bấm vào nút yêu thích
                favoriteButton.setOnClickListener(view -> {
                    if (isFavorite) {
                        dbHelper.removeFromFavorites(userId, tourId);
                        isFavorite = false;
                        Toast.makeText(this, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    } else {
                        dbHelper.addToFavorites(userId, tourId);
                        isFavorite = true;
                        Toast.makeText(this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    }
                    updateFavoriteIcon();
                });

                // ✅ Xử lý khi bấm nút "Đặt Tour"
                bookButton.setOnClickListener(view -> {
                    if (sessionManager.isLoggedIn()) {
                        Intent intent = new Intent(TourDetailActivity.this, BookingActivity.class);
                        intent.putExtra("tour_id", tourId);
                        intent.putExtra("tour_name", tour.getName());
                        intent.putExtra("tour_destination", tour.getDestination());
                        intent.putExtra("tour_price", price);
                        intent.putExtra("tour_duration", tour.getDuration() + " days");
                        intent.putExtra("tour_category", tour.getCategoryName());

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

    // ⭐ Cập nhật icon Favorite
    private void updateFavoriteIcon() {
        if (isFavorite) {
            favoriteButton.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            favoriteButton.setImageResource(R.drawable.ic_favorite_border);
        }
    }
}
