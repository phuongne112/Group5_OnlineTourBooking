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
    private static final int DATABASE_VERSION = 6;
    // Bảng Roles
    private static final String TABLE_ROLES = "roles";
    private static final String COLUMN_ROLE_ID = "id";
    private static final String COLUMN_ROLE_NAME = "role_name"; // Admin, User, Employee, Guide

    // Bảng Cities
    private static final String TABLE_CITIES = "cities";
    private static final String COLUMN_CITY_ID = "id";
    private static final String COLUMN_CITY_NAME = "name";
    // Bảng Users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PHONE = "phone";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_IMAGE = "image";
    private static final String COLUMN_USER_ROLE_ID = "role_id"; // Liên kết với Roles

    // Bảng Categories
    private static final String TABLE_CATEGORIES = "categories";
    private static final String COLUMN_CATEGORY_ID = "id";
    private static final String COLUMN_CATEGORY_NAME = "name";
    private static final String COLUMN_CATEGORY_IMAGE = "image";
    private static final String COLUMN_CATEGORY_DESCRIPTION = "description"; // Mô tả danh mục

    // Bảng Tours
    private static final String TABLE_TOURS = "tours";
    private static final String COLUMN_TOUR_ID = "id";
    private static final String COLUMN_TOUR_NAME = "name";
    private static final String COLUMN_TOUR_DESTINATION = "destination";
    private static final String COLUMN_TOUR_CITY_ID = "city_id";
    private static final String COLUMN_TOUR_PRICE = "price";
    private static final String COLUMN_TOUR_DURATION = "duration";
    private static final String COLUMN_TOUR_IMAGE = "image";
    private static final String COLUMN_TOUR_CATEGORY_ID = "category_id";
    private static final String COLUMN_TOUR_GUIDE_ID = "guide_id";  // Cột mới để lưu ID người hướng dẫn


    // Bảng Bookings
    private static final String TABLE_BOOKINGS = "bookings";
    private static final String COLUMN_BOOKING_ID = "id";
    private static final String COLUMN_BOOKING_USER_ID = "user_id";
    private static final String COLUMN_BOOKING_TOUR_ID = "tour_id";
    private static final String COLUMN_BOOKING_DATE = "booking_date";
    private static final String COLUMN_BOOKING_TOTAL_PRICE = "total_price";
    private static final String COLUMN_BOOKING_STATUS = "status"; // Pending, Confirmed, Cancelled
    // Bảng Booking Passengers (Danh sách hành khách)
    private static final String TABLE_BOOKING_PASSENGERS = "booking_passengers";
    private static final String COLUMN_PASSENGER_ID = "id";
    private static final String COLUMN_PASSENGER_BOOKING_ID = "booking_id";
    private static final String COLUMN_PASSENGER_TYPE = "type"; // adult, child, infant, baby
    private static final String COLUMN_PASSENGER_NAME = "name";
    private static final String COLUMN_PASSENGER_AGE = "age";
    // Bảng Contacts
    private static final String TABLE_CONTACTS = "contacts";
    private static final String COLUMN_CONTACT_ID = "contact_id";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_MESSAGE = "message";

    // Bảng Payments (Thanh toán)
    private static final String TABLE_PAYMENTS = "payments";
    private static final String COLUMN_PAYMENT_ID = "id";
    private static final String COLUMN_PAYMENT_BOOKING_ID = "booking_id";
    private static final String COLUMN_PAYMENT_AMOUNT = "amount";
    private static final String COLUMN_PAYMENT_DATE = "payment_date";
    private static final String COLUMN_PAYMENT_STATUS = "status"; // Pending, Completed, Failed

    // Bảng Help Center
    private static final String TABLE_HELP_CENTER = "help_center";
    private static final String COLUMN_HELP_ID = "id";
    private static final String COLUMN_HELP_USER_ID = "user_id";
    private static final String COLUMN_HELP_QUESTION = "question";
    private static final String COLUMN_HELP_ANSWER = "answer";
    // Bảng Favorites (Danh sách yêu thích của người dùng)
    private static final String TABLE_FAVORITES = "favorites";
    private static final String COLUMN_FAVORITE_ID = "id";
    private static final String COLUMN_FAVORITE_USER_ID = "user_id"; // Liên kết với Users
    private static final String COLUMN_FAVORITE_TOUR_ID = "tour_id"; // Liên kết với Tours


    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Trong phương thức onCreate(), thêm đoạn sau để tạo bảng
        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + " ("
                + COLUMN_FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FAVORITE_USER_ID + " INTEGER, "
                + COLUMN_FAVORITE_TOUR_ID + " INTEGER, "
                + "FOREIGN KEY(" + COLUMN_FAVORITE_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "), "
                + "FOREIGN KEY(" + COLUMN_FAVORITE_TOUR_ID + ") REFERENCES " + TABLE_TOURS + "(" + COLUMN_TOUR_ID + "))";
        db.execSQL(CREATE_FAVORITES_TABLE);

        db.execSQL("CREATE TABLE " + TABLE_ROLES + " (" +
                COLUMN_ROLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ROLE_NAME + " TEXT UNIQUE)");

        db.execSQL("CREATE TABLE " + TABLE_CITIES + " (" +
                COLUMN_CITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CITY_NAME + " TEXT UNIQUE)");

        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_NAME + " TEXT, " +
                COLUMN_USER_EMAIL + " TEXT UNIQUE, " +
                COLUMN_USER_PASSWORD + " TEXT, " +
                COLUMN_USER_PHONE + " TEXT, " +
                COLUMN_USER_IMAGE + " TEXT, " +
                COLUMN_USER_ROLE_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_USER_ROLE_ID + ") REFERENCES roles(id))");

        // Tạo bảng Categories
        db.execSQL("CREATE TABLE " + TABLE_CATEGORIES + " (" +
                COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORY_NAME + " TEXT UNIQUE, " +
                COLUMN_CATEGORY_IMAGE + " TEXT, " +
                COLUMN_CATEGORY_DESCRIPTION + " TEXT)"); // ✅ Cột mô tả danh mục

        db.execSQL("CREATE TABLE " + TABLE_TOURS + " (" +
                COLUMN_TOUR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TOUR_NAME + " TEXT, " +
                COLUMN_TOUR_DESTINATION + " TEXT, " +
                COLUMN_TOUR_CITY_ID + " INTEGER, " +
                COLUMN_TOUR_PRICE + " REAL, " +
                COLUMN_TOUR_DURATION + " INTEGER, " +
                COLUMN_TOUR_IMAGE + " TEXT, " +
                COLUMN_TOUR_CATEGORY_ID + " INTEGER, " +
                COLUMN_TOUR_GUIDE_ID + " INTEGER, " + // Added guide_id
                "FOREIGN KEY(" + COLUMN_TOUR_CITY_ID + ") REFERENCES cities(id), " +
                "FOREIGN KEY(" + COLUMN_TOUR_CATEGORY_ID + ") REFERENCES categories(id), " +
                "FOREIGN KEY(" + COLUMN_TOUR_GUIDE_ID + ") REFERENCES users(id))"); // Foreign key to users for the guide


        // Tạo bảng bookings
        db.execSQL("CREATE TABLE " + TABLE_BOOKINGS + " (" +
                COLUMN_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BOOKING_USER_ID + " INTEGER, " +
                COLUMN_BOOKING_TOUR_ID + " INTEGER, " +
                COLUMN_BOOKING_DATE + " TEXT, " +
                COLUMN_BOOKING_TOTAL_PRICE + " REAL, " +
                COLUMN_BOOKING_STATUS + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_BOOKING_USER_ID + ") REFERENCES users(id), " +
                "FOREIGN KEY(" + COLUMN_BOOKING_TOUR_ID + ") REFERENCES tours(id))");

        // Tạo bảng booking_passengers
        db.execSQL("CREATE TABLE " + TABLE_BOOKING_PASSENGERS + " (" +
                COLUMN_PASSENGER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PASSENGER_BOOKING_ID + " INTEGER, " +
                COLUMN_PASSENGER_TYPE + " TEXT, " +
                COLUMN_PASSENGER_NAME + " TEXT, " +
                COLUMN_PASSENGER_AGE + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_PASSENGER_BOOKING_ID + ") REFERENCES bookings(id))");

        // Tạo bảng payments
        db.execSQL("CREATE TABLE " + TABLE_PAYMENTS + " (" +
                COLUMN_PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PAYMENT_BOOKING_ID + " INTEGER, " +
                COLUMN_PAYMENT_AMOUNT + " REAL, " +
                COLUMN_PAYMENT_DATE + " TEXT, " +
                COLUMN_PAYMENT_STATUS + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_PAYMENT_BOOKING_ID + ") REFERENCES bookings(id))");
        // Tạo bảng contacts
        String createContactsTable = "CREATE TABLE " + TABLE_CONTACTS + " (" +
                COLUMN_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FULL_NAME + " TEXT NOT NULL, " +
                COLUMN_EMAIL + " TEXT NOT NULL, " +
                COLUMN_MESSAGE + " TEXT NOT NULL, " +
                "user_id INTEGER, " +
                "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE" +
                ");";
        db.execSQL(createContactsTable);
        db.execSQL("CREATE TABLE " + TABLE_HELP_CENTER + " (" +
                COLUMN_HELP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_HELP_USER_ID + " INTEGER, " +
                COLUMN_HELP_QUESTION + " TEXT, " +
                COLUMN_HELP_ANSWER + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_HELP_USER_ID + ") REFERENCES users(id))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOURS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKING_PASSENGERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HELP_CENTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROLES);
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
    public void addTour(String name, String destination, int cityId, double price, int duration, String imagePath, int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TOUR_NAME, name);
        values.put(COLUMN_TOUR_DESTINATION, destination); // 🔹 Thêm destination
        values.put(COLUMN_TOUR_PRICE, price);
        values.put(COLUMN_TOUR_DURATION, duration);
        values.put(COLUMN_TOUR_IMAGE, imagePath);
        values.put(COLUMN_TOUR_CATEGORY_ID, categoryId);
        values.put("city_id", cityId); // 🔹 Giữ city_id để đảm bảo liên kết

        db.insert(TABLE_TOURS, null, values);
        db.close();
    }


    // 🌟 Lấy tất cả tour
    public ArrayList<TourModel> getAllTours() {
        ArrayList<TourModel> tourList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn lấy tour, category, và city
        String query = "SELECT t.id, t.name, t.destination, t.price, t.duration, t.image, c.name AS categoryName, ci.name AS cityName " +
                "FROM tours t " +
                "LEFT JOIN categories c ON t.category_id = c.id " +
                "LEFT JOIN cities ci ON t.city_id = ci.id";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String destination = cursor.getString(2);
                double price = cursor.getDouble(3);
                int duration = cursor.getInt(4);
                String image = cursor.getString(5);
                String categoryName = cursor.getString(6);
                String cityName = cursor.getString(7);

                tourList.add(new TourModel(id, name, destination, price, duration, image, categoryName, cityName));
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
    public long addCity(String cityName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CITY_NAME, cityName);
        long cityId = db.insert(TABLE_CITIES, null, values);
        db.close();
        return cityId;
    }
    public ArrayList<String> getAllCities() {
        ArrayList<String> cityList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT name FROM cities"; // Giả sử bảng thành phố tên là "cities"
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String cityName = cursor.getString(0);
                cityList.add(cityName);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return cityList;
    }





}
