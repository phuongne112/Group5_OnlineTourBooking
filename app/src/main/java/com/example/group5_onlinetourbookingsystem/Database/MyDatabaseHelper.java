package com.example.group5_onlinetourbookingsystem.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.group5_onlinetourbookingsystem.models.CategoryModel;
import com.example.group5_onlinetourbookingsystem.models.TourModel;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "tourbooking.db";
    private static final int DATABASE_VERSION = 4;

    // Bảng Users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PHONE = "phone";
    private static final String COLUMN_USER_IMAGE = "image";
    private static final String COLUMN_USER_PASSWORD = "password";

    // Bảng Categories
    private static final String TABLE_CATEGORIES = "categories";
    private static final String COLUMN_CATEGORY_ID = "id";
    private static final String COLUMN_CATEGORY_NAME = "name";
    private static final String COLUMN_CATEGORY_IMAGE = "image";

    // Bảng Tours
    private static final String TABLE_TOURS = "tours";
    private static final String COLUMN_TOUR_ID = "id";
    private static final String COLUMN_TOUR_NAME = "name";
    private static final String COLUMN_TOUR_DESTINATION = "destination";
    private static final String COLUMN_TOUR_PRICE = "price";
    private static final String COLUMN_TOUR_DURATION = "duration";
    private static final String COLUMN_TOUR_IMAGE = "image";
    private static final String COLUMN_TOUR_CATEGORY_ID = "category_id";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_NAME + " TEXT, " +
                COLUMN_USER_EMAIL + " TEXT UNIQUE, " +
                COLUMN_USER_PASSWORD + " TEXT, " +
                COLUMN_USER_PHONE + " TEXT, " +
                COLUMN_USER_IMAGE + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_CATEGORIES + " (" +
                COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORY_NAME + " TEXT UNIQUE, " +
                COLUMN_CATEGORY_IMAGE + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_TOURS + " (" +
                COLUMN_TOUR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TOUR_NAME + " TEXT, " +
                COLUMN_TOUR_DESTINATION + " TEXT, " +
                COLUMN_TOUR_PRICE + " REAL, " +
                COLUMN_TOUR_DURATION + " INTEGER, " +
                COLUMN_TOUR_IMAGE + " TEXT, " +
                COLUMN_TOUR_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_TOUR_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + COLUMN_CATEGORY_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOURS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // 🌟 Thêm danh mục
    public void addCategory(String name, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, name);
        values.put(COLUMN_CATEGORY_IMAGE, imagePath);
        db.insert(TABLE_CATEGORIES, null, values);
        db.close();
    }

    // 🌟 Lấy tất cả danh mục
    public ArrayList<CategoryModel> getAllCategories() {
        ArrayList<CategoryModel> categoryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Categories", null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);

                    // Kiểm tra nếu bảng có hơn 2 cột
                    String image = cursor.getColumnCount() > 2 ? cursor.getString(2) : null;

                    categoryList.add(new CategoryModel(id, name, image));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return categoryList;
    }


    // 🌟 Thêm tour mới
    public void addTour(String name, String destination, double price, int duration, String imagePath, int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TOUR_NAME, name);
        values.put(COLUMN_TOUR_DESTINATION, destination);
        values.put(COLUMN_TOUR_PRICE, price);
        values.put(COLUMN_TOUR_DURATION, duration);
        values.put(COLUMN_TOUR_IMAGE, imagePath);
        values.put(COLUMN_TOUR_CATEGORY_ID, categoryId);

        db.insert(TABLE_TOURS, null, values);
        db.close();
    }

    // 🌟 Lấy tất cả tour
    public ArrayList<TourModel> getAllTours() {
        ArrayList<TourModel> tourList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn để lấy thông tin tour cùng với tên category
        String query = "SELECT t.id, t.name, t.destination, t.price, t.duration, t.image, c.name AS categoryName " +
                "FROM tours t " +
                "LEFT JOIN categories c ON t.category_id = c.id";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String destination = cursor.getString(2);
                double price = cursor.getDouble(3);
                int duration = cursor.getInt(4);
                String image = cursor.getString(5);
                String categoryName = cursor.getString(6); // Lấy tên category

                tourList.add(new TourModel(id, name, destination, price, duration, image, categoryName));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return tourList;
    }


    public long addUser(String name, String email, String phone, String password, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PHONE, phone);
        values.put(COLUMN_USER_PASSWORD, password);
        values.put(COLUMN_USER_IMAGE, imagePath); // Lưu đường dẫn ảnh

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result;
    }
    public int checkUserLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_USER_PASSWORD + " FROM " + TABLE_USERS +
                " WHERE " + COLUMN_USER_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        if (cursor != null && cursor.moveToFirst()) {
            String storedPassword = cursor.getString(0);
            cursor.close();
            db.close();
            if (storedPassword.equals(password)) {
                return 1; // Đăng nhập thành công
            } else {
                return 0; // Sai mật khẩu
            }
        } else {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
            return -1; // Tài khoản không tồn tại
        }
    }
}
