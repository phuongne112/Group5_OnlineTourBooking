package com.example.group5_onlinetourbookingsystem.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.group5_onlinetourbookingsystem.models.TourModel;

import java.util.ArrayList;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "tourbooking.db";
    private static final int DATABASE_VERSION = 3; // Cập nhật version để thay đổi DB

    // Bảng Users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PHONE = "phone";
    private static final String COLUMN_USER_IMAGE = "image";

    // Bảng Categories (Danh mục tour)
    private static final String TABLE_CATEGORIES = "categories";
    private static final String COLUMN_CATEGORY_ID = "id";
    private static final String COLUMN_CATEGORY_NAME = "name";

    // Bảng Tours
    private static final String TABLE_TOURS = "tours";
    private static final String COLUMN_TOUR_ID = "id";
    private static final String COLUMN_TOUR_NAME = "name";
    private static final String COLUMN_TOUR_DESTINATION = "destination";
    private static final String COLUMN_TOUR_PRICE = "price";
    private static final String COLUMN_TOUR_DURATION = "duration";
    private static final String COLUMN_TOUR_IMAGE = "image";
    private static final String COLUMN_TOUR_CATEGORY_ID = "category_id";
    private static final String COLUMN_TOUR_CATEGORY_NAME = "category_name"; // Cột mới

    // Bảng Bookings
    private static final String TABLE_BOOKINGS = "bookings";
    private static final String COLUMN_BOOKING_ID = "id";
    private static final String COLUMN_BOOKING_USER_ID = "user_id";
    private static final String COLUMN_BOOKING_TOUR_ID = "tour_id";
    private static final String COLUMN_BOOKING_DATE = "booking_date";
    private static final String COLUMN_BOOKING_STATUS = "status";

    // Tạo bảng Users
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_NAME + " TEXT, " +
                    COLUMN_USER_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_USER_PHONE + " TEXT, " +
                    COLUMN_USER_IMAGE + " TEXT)";

    // Tạo bảng Categories
    private static final String CREATE_TABLE_CATEGORIES =
            "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                    COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CATEGORY_NAME + " TEXT UNIQUE)";

    // Tạo bảng Tours
    private static final String CREATE_TABLE_TOURS =
            "CREATE TABLE " + TABLE_TOURS + " (" +
                    COLUMN_TOUR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TOUR_NAME + " TEXT, " +
                    COLUMN_TOUR_DESTINATION + " TEXT, " +
                    COLUMN_TOUR_PRICE + " REAL, " +
                    COLUMN_TOUR_DURATION + " INTEGER, " +
                    COLUMN_TOUR_IMAGE + " TEXT, " +
                    COLUMN_TOUR_CATEGORY_ID + " INTEGER, " +
                    COLUMN_TOUR_CATEGORY_NAME + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_TOUR_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + COLUMN_CATEGORY_ID + "))";

    // Tạo bảng Bookings
    private static final String CREATE_TABLE_BOOKINGS =
            "CREATE TABLE " + TABLE_BOOKINGS + " (" +
                    COLUMN_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_BOOKING_USER_ID + " INTEGER, " +
                    COLUMN_BOOKING_TOUR_ID + " INTEGER, " +
                    COLUMN_BOOKING_DATE + " TEXT, " +
                    COLUMN_BOOKING_STATUS + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_BOOKING_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "), " +
                    "FOREIGN KEY(" + COLUMN_BOOKING_TOUR_ID + ") REFERENCES " + TABLE_TOURS + "(" + COLUMN_TOUR_ID + "))";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_TOURS);
        db.execSQL(CREATE_TABLE_BOOKINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_TOURS + " ADD COLUMN " + COLUMN_TOUR_CATEGORY_ID + " INTEGER");
            db.execSQL(CREATE_TABLE_CATEGORIES);
        }
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + TABLE_TOURS + " ADD COLUMN " + COLUMN_TOUR_CATEGORY_NAME + " TEXT");
        }
    }

    // Thêm danh mục
    public void addCategory(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, name);

        db.insert(TABLE_CATEGORIES, null, values);
        db.close();
    }

    // Lấy tất cả danh mục
    public ArrayList<String> getAllCategories() {
        ArrayList<String> categoryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CATEGORIES, null);

        if (cursor.moveToFirst()) {
            do {
                categoryList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categoryList;
    }

    // Thêm tour
    public void addTour(String name, String destination, double price, int duration, String imagePath, int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Lấy tên danh mục dựa trên categoryId
        String categoryName = "";
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_CATEGORY_NAME + " FROM " + TABLE_CATEGORIES + " WHERE " + COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(categoryId)});
        if (cursor.moveToFirst()) {
            categoryName = cursor.getString(0);
        }
        cursor.close();

        values.put(COLUMN_TOUR_NAME, name);
        values.put(COLUMN_TOUR_DESTINATION, destination);
        values.put(COLUMN_TOUR_PRICE, price);
        values.put(COLUMN_TOUR_DURATION, duration);
        values.put(COLUMN_TOUR_IMAGE, imagePath);
        values.put(COLUMN_TOUR_CATEGORY_ID, categoryId);
        values.put("category_name", categoryName); // Thêm tên danh mục vào bảng tours

        long result = db.insert(TABLE_TOURS, null, values);
        db.close();

        if (result == -1) {
            Toast.makeText(context, "Thêm tour thất bại!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Thêm tour thành công!", Toast.LENGTH_SHORT).show();
        }
    }



    // Lấy tất cả tour
    public ArrayList<TourModel> getAllTours() {
        ArrayList<TourModel> tourList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn lấy dữ liệu từ bảng tours, kết hợp với bảng categories
        String query = "SELECT t.id, t.name, t.destination, t.price, t.duration, t.image, c.name " +
                "FROM " + TABLE_TOURS + " t " +
                "LEFT JOIN " + TABLE_CATEGORIES + " c " +
                "ON t." + COLUMN_TOUR_CATEGORY_ID + " = c." + COLUMN_CATEGORY_ID;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String destination = cursor.getString(2);
                double price = cursor.getDouble(3);
                int duration = cursor.getInt(4);
                String image = cursor.getString(5);
                String category = cursor.getString(6); // Lấy tên danh mục

                // Thêm tour vào danh sách
                tourList.add(new TourModel(id, name, destination, price, duration, image, category));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return tourList;
    }
    public void addTourWithCategory(String name, String destination, double price, int duration, String imagePath, String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Bước 1: Kiểm tra xem danh mục đã tồn tại chưa
        int categoryId = -1;
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_CATEGORY_ID + " FROM " + TABLE_CATEGORIES +
                        " WHERE " + COLUMN_CATEGORY_NAME + " = ?",
                new String[]{categoryName});

        if (cursor.moveToFirst()) {
            categoryId = cursor.getInt(0); // Lấy category_id nếu đã tồn tại
        } else {
            // Nếu chưa có, thêm danh mục vào bảng categories
            ContentValues categoryValues = new ContentValues();
            categoryValues.put(COLUMN_CATEGORY_NAME, categoryName);
            long newCategoryId = db.insert(TABLE_CATEGORIES, null, categoryValues);
            if (newCategoryId != -1) {
                categoryId = (int) newCategoryId; // Lấy ID danh mục mới
            }
        }
        cursor.close();

        // Nếu có lỗi khi thêm danh mục
        if (categoryId == -1) {
            Toast.makeText(context, "Lỗi khi thêm danh mục!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Bước 2: Thêm tour với category_id và category_name
        ContentValues tourValues = new ContentValues();
        tourValues.put(COLUMN_TOUR_NAME, name);
        tourValues.put(COLUMN_TOUR_DESTINATION, destination);
        tourValues.put(COLUMN_TOUR_PRICE, price);
        tourValues.put(COLUMN_TOUR_DURATION, duration);
        tourValues.put(COLUMN_TOUR_IMAGE, imagePath);
        tourValues.put(COLUMN_TOUR_CATEGORY_ID, categoryId);
        tourValues.put("category_name", categoryName); // Lưu luôn tên danh mục

        long result = db.insert(TABLE_TOURS, null, tourValues);
        db.close();

        if (result == -1) {
            Toast.makeText(context, "Thêm tour thất bại!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Thêm tour thành công!", Toast.LENGTH_SHORT).show();
        }
    }

}
