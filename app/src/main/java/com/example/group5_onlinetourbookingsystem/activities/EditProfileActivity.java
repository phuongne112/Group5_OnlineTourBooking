package com.example.group5_onlinetourbookingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group5_onlinetourbookingsystem.Database.MyDatabaseHelper;
import com.example.group5_onlinetourbookingsystem.R;
import com.example.group5_onlinetourbookingsystem.adapters.DrawableImageAdapter;
import com.example.group5_onlinetourbookingsystem.models.UserModel;
import com.example.group5_onlinetourbookingsystem.utils.SessionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class EditProfileActivity extends AppCompatActivity {

    private EditText edtUserName, edtUserPhone, edtUserBirth;
    private ImageView imgUserProfile;
    private Button btnSaveProfile, btnChangeImage;
    private MyDatabaseHelper myDB;
    private SessionManager sessionManager;
    private int userId;
    private String selectedImageName = ""; // Lưu tên ảnh
    private final String[] drawableImages = {
            "avatar1", "avatar2", "avatar3", "avatar4", "avatar5" // Thay bằng tên các ảnh trong drawable của bạn
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(this);
        userId = sessionManager.getUserId();

        if (userId == -1) {
            Toast.makeText(this, "Lỗi: Không lấy được userId! Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, UserProfileActivity.class));
            finish();
            return;
        }

        edtUserName = findViewById(R.id.edtUserName);
        edtUserPhone = findViewById(R.id.edtUserPhone);
        edtUserBirth = findViewById(R.id.edtUserBirth);
        imgUserProfile = findViewById(R.id.imgUserProfile);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnChangeImage = findViewById(R.id.btnChangeImage);

        myDB = new MyDatabaseHelper(this);
        loadUserProfile(userId);

        btnChangeImage.setOnClickListener(v -> showImageSelectionDialog());
        btnSaveProfile.setOnClickListener(v -> updateUserProfile());
    }

    private void showImageSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_select_image, null);
        builder.setView(dialogView);

        RecyclerView recyclerViewImages = dialogView.findViewById(R.id.recyclerViewImages);
        recyclerViewImages.setLayoutManager(new GridLayoutManager(this, 3));

        DrawableImageAdapter imageAdapter = new DrawableImageAdapter(this, Arrays.asList(drawableImages), imageName -> {
            selectedImageName = imageName; // Lưu tên tài nguyên ảnh
            int resourceId = getResources().getIdentifier(selectedImageName, "drawable", getPackageName());
            if (resourceId != 0) {
                imgUserProfile.setImageResource(resourceId); // Hiển thị ảnh
            } else {
                Toast.makeText(this, "Không tìm thấy ảnh: " + selectedImageName, Toast.LENGTH_SHORT).show();
            }
            // Đóng dialog sau khi chọn
            AlertDialog dialog = (AlertDialog) recyclerViewImages.getTag();
            if (dialog != null) {
                dialog.dismiss();
            }
        });


        recyclerViewImages.setAdapter(imageAdapter);


        AlertDialog dialog = builder.create();
        recyclerViewImages.setTag(dialog); // Lưu dialog để đóng sau khi chọn
        dialog.show();
    }

    private void loadUserProfile(int userId) {
        UserModel user = myDB.getUserById(userId);
        if (user != null) {
            edtUserName.setText(user.getName());
            edtUserPhone.setText(user.getPhone());
            edtUserBirth.setText(user.getBirthDate());
            selectedImageName = user.getImage(); // Lưu tên ảnh từ database
            Log.d("Database", "User image from DB: " + selectedImageName);
            if (selectedImageName != null && !selectedImageName.isEmpty()) {
                int resourceId = getResources().getIdentifier(selectedImageName, "drawable", getPackageName());
                if (resourceId != 0) {
                    imgUserProfile.setImageResource(resourceId);
                } else {
                    imgUserProfile.setImageResource(R.drawable.favorites);
                }
            }
        }
    }

    private void updateUserProfile() {
        String newName = edtUserName.getText().toString().trim();
        String newPhone = edtUserPhone.getText().toString().trim();
        String newBirthDate = edtUserBirth.getText().toString().trim();

        if (newName.isEmpty() || newPhone.isEmpty() || newBirthDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidBirthDate(newBirthDate)) {
            Toast.makeText(this, "Ngày sinh không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Nếu chưa chọn ảnh mới, lấy ảnh cũ từ database
        if (selectedImageName.isEmpty()) {
            UserModel user = myDB.getUserById(userId);
            if (user != null && user.getImage() != null) {
                selectedImageName = user.getImage();
            }
        }

        boolean isUpdated = myDB.updateUser(userId, newName, newPhone, newBirthDate, selectedImageName);
        if (isUpdated) {
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi cập nhật!", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isValidBirthDate(String birthDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sdf.setLenient(false);
        try {
            Date date = sdf.parse(birthDate);
            return !date.after(new Date());
        } catch (ParseException e) {
            return false;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
