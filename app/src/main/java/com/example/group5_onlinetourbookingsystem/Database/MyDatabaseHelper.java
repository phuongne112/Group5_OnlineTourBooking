    package com.example.group5_onlinetourbookingsystem.Database;
    
    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;
    import android.util.Log;
    import android.widget.Toast;
    
    import androidx.annotation.Nullable;
    import java.security.MessageDigest;
    import java.security.NoSuchAlgorithmException;
    import com.example.group5_onlinetourbookingsystem.models.BookingModel;
    import com.example.group5_onlinetourbookingsystem.models.CategoryModel;
    import com.example.group5_onlinetourbookingsystem.models.CityModel;
    import com.example.group5_onlinetourbookingsystem.models.TourModel;
    import com.example.group5_onlinetourbookingsystem.models.UserModel;

    import java.security.MessageDigest;
    import java.text.Normalizer;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Locale;
    import java.util.regex.Pattern;

    public class MyDatabaseHelper extends SQLiteOpenHelper {
        private Context context;
        private static final String DATABASE_NAME = "tourbooking.db";
        private static final int DATABASE_VERSION = 6;

        // Bảng Roles
        private static final String TABLE_ROLES = "roles";
        private static final String COLUMN_ROLE_ID = "id";
        private static final String COLUMN_ROLE_NAME = "role_name"; // Admin, User, Guide

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
        private static final String COLUMN_USER_BIRTH = "birth_date";
        private static final String COLUMN_USER_STATUS = "status"; // ✅ Thêm cột trạng thái user


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
        private static final String COLUMN_TOUR_DESCRIPTION = "description";
        private static final String COLUMN_TOUR_START_TIME = "start_time";
        // Bảng Tour Guides (Quản lý nhiều hướng dẫn viên trên một tour)
        private static final String TABLE_TOUR_GUIDES = "tour_guides";
        private static final String COLUMN_TOUR_GUIDE_ID = "id";
        private static final String COLUMN_TOUR_GUIDE_TOUR_ID = "tour_id";
        private static final String COLUMN_TOUR_GUIDE_USER_ID = "guide_id";
        // Bảng Tour Images
        private static final String TABLE_TOUR_IMAGES = "tour_images";
        private static final String COLUMN_TOUR_IMAGE_ID = "id";
        private static final String COLUMN_TOUR_IMAGE_TOUR_ID = "tour_id";
        private static final String COLUMN_TOUR_IMAGE_URL = "image_url";

        // Bảng Bookings
        private static final String TABLE_BOOKINGS = "bookings";
        private static final String COLUMN_BOOKING_ID = "id";
        private static final String COLUMN_BOOKING_USER_ID = "user_id";
        private static final String COLUMN_BOOKING_TOUR_ID = "tour_id";
        private static final String COLUMN_BOOKING_DATE = "booking_date";
        private static final String COLUMN_BOOKING_TOTAL_PRICE = "total_price";
        private static final String COLUMN_BOOKING_STATUS = "status";
        private static final String COLUMN_BOOKING_ADULT_COUNT = "adult_count";
        private static final String COLUMN_BOOKING_CHILD_COUNT = "child_count";
        private static final String COLUMN_BOOKING_NOTE = "note";
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
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FAVORITES + " ("
                    + COLUMN_FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_FAVORITE_USER_ID + " INTEGER, "
                    + COLUMN_FAVORITE_TOUR_ID + " INTEGER, "
                    + "FOREIGN KEY(" + COLUMN_FAVORITE_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "), "
                    + "FOREIGN KEY(" + COLUMN_FAVORITE_TOUR_ID + ") REFERENCES " + TABLE_TOURS + "(" + COLUMN_TOUR_ID + "))");


            if (!tableExists(db, TABLE_ROLES)) {
                db.execSQL("CREATE TABLE " + TABLE_ROLES + " (" +
                        COLUMN_ROLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_ROLE_NAME + " TEXT UNIQUE)");
            }


            db.execSQL("CREATE TABLE " + TABLE_CITIES + " (" +
                    COLUMN_CITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CITY_NAME + " TEXT UNIQUE)");
            // Tạo bảng Cities (đã sửa lại để tránh lỗi trùng lặp)
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CITIES + " (" +
                    COLUMN_CITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CITY_NAME + " TEXT UNIQUE)");

            db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_NAME + " TEXT, " +
                    COLUMN_USER_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_USER_PHONE + " TEXT, " +
                    COLUMN_USER_PASSWORD + " TEXT, " +
                    COLUMN_USER_BIRTH + " TEXT, " +
                    COLUMN_USER_IMAGE + " TEXT, " +
                    COLUMN_USER_ROLE_ID + " INTEGER, " +
                    "status TEXT DEFAULT 'active', " + // ✅ Đảm bảo có cột status
                    "FOREIGN KEY(" + COLUMN_USER_ROLE_ID + ") REFERENCES roles(id))"
            );







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
                    COLUMN_TOUR_DESCRIPTION + " TEXT, " +

                    COLUMN_TOUR_START_TIME + " TEXT, " + // 🔹 Thêm cột start_time
                    "FOREIGN KEY(" + COLUMN_TOUR_CITY_ID + ") REFERENCES cities(id), " +
                    "FOREIGN KEY(" + COLUMN_TOUR_CATEGORY_ID + ") REFERENCES categories(id)) ");
                     // Tạo bảng Tour Guides
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_TOUR_GUIDES + " (" +
                    COLUMN_TOUR_GUIDE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TOUR_GUIDE_TOUR_ID + " INTEGER, " +
                    COLUMN_TOUR_GUIDE_USER_ID + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_TOUR_GUIDE_TOUR_ID + ") REFERENCES tours(id), " +
                    "FOREIGN KEY(" + COLUMN_TOUR_GUIDE_USER_ID + ") REFERENCES users(id))");


            // Tạo bảng bookings
            db.execSQL("CREATE TABLE " + TABLE_BOOKINGS + " (" +
                    COLUMN_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_BOOKING_USER_ID + " INTEGER, " +
                    COLUMN_BOOKING_TOUR_ID + " INTEGER, " +
                    COLUMN_BOOKING_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
                    "time TEXT DEFAULT '00:00', " +  // ✅ Ensure time column exists
                    COLUMN_BOOKING_TOTAL_PRICE + " REAL, " +
                    COLUMN_BOOKING_STATUS + " TEXT, " +
                    COLUMN_BOOKING_ADULT_COUNT + " INTEGER, " +
                    COLUMN_BOOKING_CHILD_COUNT + " INTEGER, " +
                    COLUMN_BOOKING_NOTE + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_BOOKING_USER_ID + ") REFERENCES users(id), " +
                    "FOREIGN KEY(" + COLUMN_BOOKING_TOUR_ID + ") REFERENCES tours(id))");




            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_TOUR_IMAGES + " (" +
                    COLUMN_TOUR_IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TOUR_IMAGE_TOUR_ID + " INTEGER, " +
                    COLUMN_TOUR_IMAGE_URL + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_TOUR_IMAGE_TOUR_ID + ") REFERENCES " + TABLE_TOURS + "(" + COLUMN_TOUR_ID + "))");




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
            if (oldVersion < 6) { // Kiểm tra version để chỉ thêm cột nếu cần
                db.execSQL("ALTER TABLE users ADD COLUMN status TEXT DEFAULT 'active'");
            } else {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOURS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOUR_GUIDES);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOUR_IMAGES);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENTS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITIES);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_HELP_CENTER);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROLES);
                        onCreate(db); // Gọi lại để tạo bảng mới
            }
             {
                if (oldVersion < 6) {
                    db.execSQL("ALTER TABLE users ADD COLUMN status TEXT DEFAULT 'active'");
                }
                 if (oldVersion < 7) {
                     if (!columnExists(db, TABLE_BOOKINGS, "time")) {
                         db.execSQL("ALTER TABLE " + TABLE_BOOKINGS + " ADD COLUMN time TEXT DEFAULT '00:00'");
                     }
                 }

                 onCreate(db);
            }

        }
        private boolean columnExists(SQLiteDatabase db, String tableName, String columnName) {
            Cursor cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
            int columnIndex = cursor.getColumnIndex("name");

            if (cursor.moveToFirst()) {
                do {
                    String existingColumn = cursor.getString(columnIndex);
                    if (existingColumn.equalsIgnoreCase(columnName)) {
                        cursor.close();
                        return true; // ✅ Cột đã tồn tại
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
            return false; // ❌ Cột chưa tồn tại
        }

        private boolean tableExists(SQLiteDatabase db, String tableName) {
            Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
            boolean exists = (cursor.getCount() > 0);
            cursor.close();
            return exists;
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

            Cursor cursor = db.rawQuery("SELECT * FROM categories", null); // Kiểm tra tên bảng có chính xác không?

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

                    categoryList.add(new CategoryModel(id, name, image, description));
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return categoryList;
        }


        public ArrayList<TourModel> getToursByPage(int offset, int limit) {
            ArrayList<TourModel> tourList = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();

            String query = "SELECT t.id, t.name, t.destination, t.price, t.duration, t.image, t.description, " +
                    "c.id AS categoryId, c.name AS categoryName, t.city_id, ci.name AS cityName, t.start_time " + // ✅ Lấy cả city_id
                    "FROM tours t " +
                    "LEFT JOIN categories c ON t.category_id = c.id " +
                    "LEFT JOIN cities ci ON t.city_id = ci.id " +
                    "ORDER BY t.id " +
                    "LIMIT ? OFFSET ?";

            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(limit), String.valueOf(offset)});

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String destination = cursor.getString(2);
                    double price = cursor.getDouble(3);
                    int duration = cursor.getInt(4);
                    String image = cursor.getString(5);
                    String description = cursor.getString(6);
                    int categoryId = cursor.getInt(7);
                    String categoryName = cursor.getString(8);
                    int cityId = cursor.getInt(9); // ✅ Lấy city_id
                    String cityName = cursor.getString(10) != null ? cursor.getString(10) : "Không xác định"; // ✅ Tránh NULL
                    String startTime = cursor.getString(11);

                    tourList.add(new TourModel(id, name, destination, price, duration, image, description, categoryId, categoryName, cityId, cityName, startTime));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return tourList;
        }


        // 🌟 Thêm tour mới
        public int addTour(String name, String destination, int cityId, double price, int duration, String image,
                           int categoryId, String startTime, String description) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("destination", destination);
            values.put("city_id", cityId);
            values.put("price", price);
            values.put("duration", duration);
            values.put("image", image);
            values.put("category_id", categoryId);
            values.put("start_time", startTime);
            values.put("description", description);

            long tourId = db.insert("tours", null, values);
            return (int) tourId;
        }





        // 🌟 Lấy tất cả tour
        public ArrayList<TourModel> getAllTours() {
            ArrayList<TourModel> tourList = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();

            String query = "SELECT t.id, t.name, t.destination, t.price, t.duration, t.image, t.description, " +
                    "c.id AS categoryId, c.name AS categoryName, t.city_id, ci.name AS cityName, t.start_time " +
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
                    String description = cursor.getString(6);
                    int categoryId = cursor.getInt(7);
                    String categoryName = cursor.getString(8);
                    int cityId = cursor.getInt(9);
                    String cityName = cursor.getString(10) != null ? cursor.getString(10) : "Không xác định";
                    String startTime = cursor.getString(11);

                    tourList.add(new TourModel(id, name, destination, price, duration, image, description, categoryId, categoryName, cityId, cityName, startTime));
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return tourList;
        }







        public long addUser(String name, String email, String phone, String hashedPassword, String birthDate, String imagePath, int roleId) {
            SQLiteDatabase db = null;
            long result = -1;

            try {
                db = this.getWritableDatabase();

                if (isUserExists(email)) {
                    Log.e("DB_ERROR", "Email đã tồn tại: " + email);
                    return -1;
                }

                ContentValues values = new ContentValues();
                values.put(COLUMN_USER_NAME, name);
                values.put(COLUMN_USER_EMAIL, email);
                values.put(COLUMN_USER_PHONE, phone);
                values.put(COLUMN_USER_PASSWORD, hashedPassword);
                values.put(COLUMN_USER_BIRTH, birthDate);
                values.put(COLUMN_USER_IMAGE, imagePath);
                values.put(COLUMN_USER_ROLE_ID, roleId);
                values.put("status", "active"); // ✅ Thêm trạng thái mặc định

                result = db.insert(TABLE_USERS, null, values);

                if (result == -1) {
                    Log.e("DB_ERROR", "Thêm user thất bại!");
                } else {
                    Log.d("DB_SUCCESS", "Thêm user thành công với ID: " + result);
                }
            } catch (Exception e) {
                Log.e("DB_EXCEPTION", "Lỗi khi thêm user: " + e.getMessage());
            } finally {
                if (db != null && db.isOpen()) {
                    db.close();
                }
            }

            return result;
        }









        public int checkUserLogin(String email, String hashedPassword) {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT " + COLUMN_USER_PASSWORD + ", " + COLUMN_USER_STATUS +
                    " FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_EMAIL + " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{email});

            if (cursor != null && cursor.moveToFirst()) {
                String storedPassword = cursor.getString(0);
                String status = cursor.getString(1); // 🔥 Lấy trạng thái tài khoản
                cursor.close();
                db.close();

                // ✅ Kiểm tra trạng thái tài khoản
                if (status.equalsIgnoreCase("banned")) {
                    return -2; // ❌ Tài khoản bị cấm
                }

                // ✅ So sánh mật khẩu đã mã hóa
                if (storedPassword.equals(hashedPassword)) {
                    return 1; // ✅ Đăng nhập thành công
                } else {
                    return 0; // ❌ Sai mật khẩu
                }
            } else {
                if (cursor != null) {
                    cursor.close();
                }
                db.close();
                return -1; // ❌ Tài khoản không tồn tại
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
        public String getUserPhoneByEmail(String email) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT phone FROM users WHERE email = ?", new String[]{email});
            if (cursor.moveToFirst()) {
                return cursor.getString(0);
            }
            cursor.close();
            return "";
        }
        public String getUserPhoneById(int userId) {
            SQLiteDatabase db = this.getReadableDatabase();
            String phone = null;

            Cursor cursor = db.rawQuery("SELECT " + COLUMN_USER_PHONE + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_ID + " = ?",
                    new String[]{String.valueOf(userId)});

            if (cursor.moveToFirst()) {
                phone = cursor.getString(0);
            }

            cursor.close();
            db.close();
            return phone;
        }


        public TourModel getTourById(int id) {
            SQLiteDatabase db = this.getReadableDatabase();
            TourModel tour = null;

            String query = "SELECT t.id, t.name, t.destination, t.description, t.price, t.duration, t.image, " +
                    "t.city_id, ci.name AS cityName, " +  // ✅ Lấy cả city_id và cityName
                    "c.id AS categoryId, c.name AS categoryName, t.start_time " +
                    "FROM tours t " +
                    "LEFT JOIN categories c ON t.category_id = c.id " +
                    "LEFT JOIN cities ci ON t.city_id = ci.id " +
                    "WHERE t.id = ?";

            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

            if (cursor.moveToFirst()) {
                int tourId = cursor.getInt(0);
                String name = cursor.getString(1);
                String destination = cursor.getString(2);
                String description = cursor.getString(3);
                double price = cursor.getDouble(4);
                int duration = cursor.getInt(5);
                String image = cursor.getString(6);
                int cityId = cursor.getInt(7);  // ✅ Lấy city_id
                String cityName = cursor.getString(8); // ✅ Lấy cityName
                int categoryId = cursor.getInt(9);
                String categoryName = cursor.getString(10);
                String startTime = cursor.getString(11);

                // ✅ Tạo đối tượng TourModel với cityId và cityName
                tour = new TourModel(tourId, name, destination, price, duration, image, description, categoryId, categoryName, cityId, cityName, startTime);
            }

            cursor.close();
            db.close();
            return tour;
        }



        public long addBooking(int userId, int tourId, int adults, int children, String note, double totalPrice, String status) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("user_id", userId);
            values.put("tour_id", tourId);
            values.put("adult_count", adults);
            values.put("child_count", children);
            values.put("note", note);
            values.put("total_price", totalPrice);
            values.put("status", status); // ✅ Thêm trạng thái
            values.put("booking_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));

            long result = db.insert("bookings", null, values);
            db.close();
            return result;
        }





        public boolean isUserExists(String email) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT 1 FROM users WHERE email = ?", new String[]{email});
            boolean exists = cursor.getCount() > 0;
            cursor.close();
            return exists; // Không gọi db.close() ở đây!
        }



        // ✅ Hàm cập nhật mật khẩu mới
        public void updatePassword(String email, String newPassword) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("password", newPassword);
            db.update("users", values, "email=?", new String[]{email});
            db.close();
        }
        public UserModel getUserById(int userId) {
            SQLiteDatabase db = this.getReadableDatabase();
            UserModel user = null;

            String query = "SELECT id, name, email, phone, birth_date, status, role_id FROM users WHERE id = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            if (cursor.moveToFirst()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String email = cursor.getString(2);
                String phone = cursor.getString(3);
                String birthDate = cursor.getString(4);
                String status = cursor.getString(5);
                int roleId = cursor.getInt(6);
                Log.d("Database", "User found: ID=" + id + ", Name=" + name + ", Email=" + email);

                user = new UserModel(id, name, email, phone, birthDate , "active",roleId); // Mặc định trạng thái active

            } else {
                Log.e("Database", "Không tìm thấy user với ID: " + userId);
            }

            cursor.close();
            db.close();
            return user;
        }


        public ArrayList<UserModel> getAllUsers() {
            ArrayList<UserModel> userList = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();

            // 🔥 Chỉ lấy user có role_id khác 2 (loại bỏ Admin)
            String query = "SELECT id, name, email, phone, birth_date, status,role_id FROM users WHERE role_id != 2";
            Cursor cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String email = cursor.getString(2);
                    String phone = cursor.getString(3);
                    String birthDate = cursor.getString(4);
                    String status = cursor.getString(5);
                    int roleId = cursor.getInt(6);
                    userList.add(new UserModel(id, name, email, phone, birthDate, status,roleId));
                } while (cursor.moveToNext());

                cursor.close();
            } else {
                Log.e("DB_ERROR", "Không có dữ liệu user!");
            }

            db.close();
            return userList;
        }
        public void updateUserRole(int userId, int newRoleId) {
            SQLiteDatabase db = this.getWritableDatabase();

            try {
                ContentValues values = new ContentValues();
                values.put("role_id", newRoleId);
                db.update("users", values, "id = ?", new String[]{String.valueOf(userId)});

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (db.isOpen()) {
                    db.close();
                }
            }
        }
        public int getUserIdByEmail(String email) {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT id FROM users WHERE email = ?";
            Cursor cursor = db.rawQuery(query, new String[]{email});

            int userId = -1; // Nếu không tìm thấy user, giữ -1
            if (cursor.moveToFirst()) {
                userId = cursor.getInt(0);
            }
            cursor.close();
            db.close();

            Log.d("DB_USER_ID", "UserID tìm thấy: " + userId + " cho email: " + email);
            return userId;
        }



        public String getUserNameByEmail(String email) {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT name FROM users WHERE email = ?";
            Cursor cursor = db.rawQuery(query, new String[]{email});

            String userName = "Guest";
            if (cursor.moveToFirst()) {
                userName = cursor.getString(0);
            }
            cursor.close();
            db.close();
            return userName;
        }
        public boolean updateUser(int userId, String name, String phone, String birthDate, String imagePath) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_NAME, name);
            values.put(COLUMN_USER_PHONE, phone);
            values.put(COLUMN_USER_BIRTH, birthDate);
            values.put(COLUMN_USER_IMAGE, imagePath); // ✅ Lưu URI ảnh vào database

            int rowsAffected = db.update(TABLE_USERS, values, "id=?", new String[]{String.valueOf(userId)});
            db.close();

            return rowsAffected > 0;
        }


        public ArrayList<TourModel> searchTours(String query) {
            ArrayList<TourModel> tourList = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();

            String queryNoAccent = removeDiacritics(query);

            String sql = "SELECT t.id, t.name, t.destination, t.price, t.duration, t.image, t.description, " +
                    "c.id AS categoryId, c.name AS categoryName, t.city_id, ci.name AS cityName, t.start_time " +
                    "FROM tours t " +
                    "LEFT JOIN categories c ON t.category_id = c.id " +
                    "LEFT JOIN cities ci ON t.city_id = ci.id";

            Cursor cursor = db.rawQuery(sql, null);

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String destination = cursor.getString(2);
                    double price = cursor.getDouble(3);
                    int duration = cursor.getInt(4);
                    String image = cursor.getString(5);
                    String description = cursor.getString(6);
                    int categoryId = cursor.getInt(7);
                    String categoryName = cursor.getString(8);
                    int cityId = cursor.getInt(9);
                    String cityName = cursor.getString(10) != null ? cursor.getString(10) : "Không xác định";
                    String startTime = cursor.getString(11);

                    // Chuyển thành không dấu để tìm kiếm
                    String nameNoAccent = removeDiacritics(name);
                    String categoryNoAccent = removeDiacritics(categoryName);
                    String cityNoAccent = removeDiacritics(cityName);

                    if (nameNoAccent.contains(queryNoAccent) || categoryNoAccent.contains(queryNoAccent) || cityNoAccent.contains(queryNoAccent)) {
                        tourList.add(new TourModel(id, name, destination, price, duration, image, description, categoryId, categoryName, cityId, cityName, startTime));
                    }
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return tourList;
        }


        public void updateUserStatus(int userId, String newStatus) {
            SQLiteDatabase db = this.getWritableDatabase();

            try {
                // 🚨 Prevent Admin from being banned
                String roleQuery = "SELECT role_id FROM users WHERE id = ?";
                Cursor cursor = db.rawQuery(roleQuery, new String[]{String.valueOf(userId)});

                if (cursor.moveToFirst()) {
                    int roleId = cursor.getInt(0);
                    if (roleId == 2) { // If Admin, prevent banning
                        cursor.close();
                        return; // ❌ Stop Execution
                    }
                }
                cursor.close();

                ContentValues values = new ContentValues();
                values.put("status", newStatus);
                db.update("users", values, "id = ?", new String[]{String.valueOf(userId)});

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (db.isOpen()) { // ✅ Ensure we don't close an already closed database
                    db.close();
                }
            }
        }


        // Hàm chuyển Tiếng Việt có dấu thành không dấu
        public static String removeDiacritics(String text) {
            if (text == null) return "";
            String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
            return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
        }

        public HashMap<String, String> getUserDetails(int userId) {
            HashMap<String, String> user = new HashMap<>();
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT name, role, email, phone FROM users WHERE id = ?", new String[]{String.valueOf(userId)});

            if (cursor.moveToFirst()) {
                user.put("name", cursor.getString(0));
                user.put("role", cursor.getString(1));
                user.put("email", cursor.getString(2));
                user.put("phone", cursor.getString(3));
            }

            cursor.close();
            db.close();
            return user;
        }


        public long addPayment(long bookingId, double amount, String paymentDate, String status) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("booking_id", bookingId);
            values.put("amount", amount);
            values.put("payment_date", paymentDate); // Thêm ngày thanh toán
            values.put("status", status);

            long result = db.insert("payments", null, values);
            db.close();
            return result;
        }




        public void updateBookingStatus(long bookingId, String status) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("status", status);
            db.update("bookings", values, "id=?", new String[]{String.valueOf(bookingId)});
            db.close();
        }
        public Cursor getUserBookings(int userId) {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT b.id AS _id, t.name AS tour_name, b.status, b.booking_date " +
                    "FROM bookings b INNER JOIN tours t ON b.tour_id = t.id " +
                    "WHERE b.user_id = ?";
            return db.rawQuery(query, new String[]{String.valueOf(userId)});
        }

        public List<TourModel> getToursByCategory(int categoryId) {
            List<TourModel> tourList = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();

            String query = "SELECT t.id, t.name, t.destination, t.price, t.duration, t.image, " +
                    "t.description, c.id AS categoryId, c.name AS categoryName, t.city_id, ci.name AS cityName, t.start_time " +
                    "FROM tours t " +
                    "LEFT JOIN categories c ON t.category_id = c.id " +
                    "LEFT JOIN cities ci ON t.city_id = ci.id " +
                    "WHERE t.category_id = ?";

            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(categoryId)});

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String destination = cursor.getString(2);
                    double price = cursor.getDouble(3);
                    int duration = cursor.getInt(4);
                    String image = cursor.getString(5);
                    String description = cursor.getString(6);
                    int categoryID = cursor.getInt(7);
                    String categoryName = cursor.getString(8);
                    int cityId = cursor.getInt(9); // ✅ Lấy city_id
                    String cityName = cursor.getString(10) != null ? cursor.getString(10) : "Không xác định"; // ✅ Tránh NULL
                    String startTime = cursor.getString(11);

                    tourList.add(new TourModel(id, name, destination, price, duration, image, description, categoryID, categoryName, cityId, cityName, startTime));
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return tourList;
        }

        public boolean addRole(String roleName) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("role_name", roleName);

            long result = db.insert("roles", null, values);
            db.close();
            return result != -1;
        }
        public HashMap<String, Integer> getAllCitiesWithIds() {
            HashMap<String, Integer> cityMap = new HashMap<>();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT id, name FROM cities", null);

            if (cursor.moveToFirst()) {
                do {
                    cityMap.put(cursor.getString(1), cursor.getInt(0)); // cityName -> cityId
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return cityMap;
        }



        public ArrayList<String> getAllRoles() {
            ArrayList<String> roles = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT role_name FROM roles", null);
            if (cursor.moveToFirst()) {
                do {
                    roles.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return roles;
        }


        public int getUserRoleIdByEmail(String email) {
            int roleId = -1;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT " + COLUMN_USER_ROLE_ID + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_EMAIL + " = ?", new String[]{email});

            if (cursor.moveToFirst()) {
                roleId = cursor.getInt(0);
            }
            cursor.close();
            db.close();
            return roleId;
        }
        public int getTotalBookings() {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT COUNT(*) FROM bookings";
            Cursor cursor = db.rawQuery(query, null);
            int count = 0;
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
            db.close();
            return count;
        }

        // Truy vấn số Tour đã được đặt
        public int getBookedToursCount() {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT COUNT(DISTINCT tour_id) FROM bookings";
            Cursor cursor = db.rawQuery(query, null);
            int count = 0;
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
            db.close();
            return count;
        }

        // Truy vấn tổng số tiền đã thanh toán
        public double getTotalRevenue() {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT SUM(amount) FROM payments WHERE status = 'Completed'";
            Cursor cursor = db.rawQuery(query, null);
            double totalRevenue = 0;
            if (cursor.moveToFirst()) {
                totalRevenue = cursor.getDouble(0);
            }
            cursor.close();
            db.close();
            return totalRevenue;
        }
        public Cursor getAllBookingsWithTourInfo() {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT b.id AS _id, t.name AS tour_name, b.booking_date, " +
                    "b.adult_count, b.child_count, b.total_price, " +
                    "b.status AS booking_status, " +
                    "(SELECT status FROM payments WHERE booking_id = b.id) AS payment_status " +
                    "FROM bookings b " +
                    "JOIN tours t ON b.tour_id = t.id " +
                    "ORDER BY b.booking_date DESC";
            return db.rawQuery(query, null);
        }




        // 🛠 Cập nhật trạng thái booking
        public boolean updateBookingStatus(int bookingId, String newStatus) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("status", newStatus);

            int rowsAffected = db.update("bookings", values, "id = ?", new String[]{String.valueOf(bookingId)});
            db.close();
            return rowsAffected > 0;
        }
        public boolean updatePaymentStatus(int bookingId, String newStatus) {
            SQLiteDatabase db = this.getWritableDatabase();
            boolean success = false;

            try {
                ContentValues values = new ContentValues();
                values.put("status", newStatus);

                int rowsAffected = db.update("payments", values, "booking_id = ?", new String[]{String.valueOf(bookingId)});
                success = rowsAffected > 0;

                Log.d("DB_UPDATE", "Updated payment status for Booking ID: " + bookingId + " to " + newStatus);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("DB_ERROR", "Lỗi cập nhật thanh toán: " + e.getMessage());
            } finally {
                db.close();
            }

            return success;
        }

        public boolean isUserPasswordCorrect(int userId, String oldPassword) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT password FROM users WHERE id = ?", new String[]{String.valueOf(userId)});

            if (cursor.moveToFirst()) {
                String storedHashedPassword = cursor.getString(0); // Mật khẩu đã mã hóa trong DB
                cursor.close();
                db.close();

                Log.d("DEBUG", "Mật khẩu đã mã hóa trong DB: " + storedHashedPassword);
                Log.d("DEBUG", "Mật khẩu nhập vào (chưa mã hóa): " + oldPassword);

                // ✅ Sử dụng SHA-256 để kiểm tra mật khẩu
                String hashedInputPassword = hashPassword(oldPassword);

                Log.d("DEBUG", "Mật khẩu nhập vào (sau mã hóa): " + hashedInputPassword);

                return storedHashedPassword.equals(hashedInputPassword);
            }

            cursor.close();
            db.close();
            return false; // Nếu không tìm thấy user
        }
        public boolean changeUserPassword(int userId, String oldPassword, String newPassword) {
            if (!isUserPasswordCorrect(userId, oldPassword)) {
                Log.e("ERROR", "Mật khẩu cũ không đúng!");
                return false;
            }

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            // ✅ Hash the new password using SHA-256
            String hashedNewPassword = hashPassword(newPassword);

            values.put("password", hashedNewPassword);
            int rowsUpdated = db.update("users", values, "id=?", new String[]{String.valueOf(userId)});

            if (rowsUpdated > 0) {
                logPasswordChange(userId); // 🆕 Log password change event
                Log.d("SUCCESS", "Mật khẩu đã được cập nhật và mã hóa thành công!");
            }

            db.close();
            return rowsUpdated > 0;
        }



        private String hashPassword(String password) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(password.getBytes());
                StringBuilder hexString = new StringBuilder();
                for (byte b : hash) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) hexString.append('0');
                    hexString.append(hex);
                }
                return hexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }
        }
        private void logPasswordChange(int userId) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("user_id", userId);
            values.put("message", "Mật khẩu của bạn đã bị thay đổi lúc " + getCurrentDateTime());

            db.insert("user_notifications", null, values);
            db.close();
        }
        private String getCurrentDateTime() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return sdf.format(new Date());
        }
        public ArrayList<CityModel> getAllCitiesAdmin() {
            ArrayList<CityModel> cityList = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM cities", null);

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    cityList.add(new CityModel(id, name));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return cityList;
        }
        public CityModel getCityById(int cityId) {
            SQLiteDatabase db = this.getReadableDatabase();
            CityModel city = null;

            Cursor cursor = db.rawQuery("SELECT * FROM cities WHERE id = ?", new String[]{String.valueOf(cityId)});

            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                city = new CityModel(id, name);
            }

            cursor.close();
            db.close();
            return city;
        }
        public CategoryModel getCategoryById(int categoryId) {
            SQLiteDatabase db = this.getReadableDatabase();
            CategoryModel category = null;

            Cursor cursor = db.rawQuery("SELECT * FROM categories WHERE id = ?", new String[]{String.valueOf(categoryId)});

            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                category = new CategoryModel(id, name, image, description);
            }

            cursor.close();
            db.close();
            return category;
        }
        public void addToFavorites(int userId, int tourId) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("user_id", userId);
            values.put("tour_id", tourId);

            // ✅ Prevent duplicate favorites
            Cursor cursor = db.rawQuery("SELECT * FROM favorites WHERE user_id = ? AND tour_id = ?",
                    new String[]{String.valueOf(userId), String.valueOf(tourId)});

            if (cursor.getCount() == 0) {
                db.insert("favorites", null, values);
            }
            cursor.close();
            db.close();
        }

        public void removeFromFavorites(int userId, int tourId) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("favorites", "user_id = ? AND tour_id = ?",
                    new String[]{String.valueOf(userId), String.valueOf(tourId)});
            db.close();
        }

        public boolean isFavorite(int userId, int tourId) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM favorites WHERE user_id = ? AND tour_id = ?",
                    new String[]{String.valueOf(userId), String.valueOf(tourId)});

            boolean exists = cursor.getCount() > 0;
            cursor.close();
            db.close();
            return exists;
        }
        public ArrayList<TourModel> getFavoriteTours(int userId) {
            ArrayList<TourModel> favoriteTours = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();

            String query = "SELECT t.id, t.name, t.destination, t.price, t.duration, t.image, t.description, " +
                    "c.id AS categoryId, c.name AS categoryName, t.city_id, ci.name AS cityName, t.start_time " +
                    "FROM tours t " +
                    "JOIN favorites f ON t.id = f.tour_id " +
                    "LEFT JOIN categories c ON t.category_id = c.id " +
                    "LEFT JOIN cities ci ON t.city_id = ci.id " +
                    "WHERE f.user_id = ?";

            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String destination = cursor.getString(cursor.getColumnIndexOrThrow("destination"));
                    double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                    int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
                    String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("categoryId"));
                    String categoryName = cursor.getString(cursor.getColumnIndexOrThrow("categoryName"));
                    int cityId = cursor.getInt(cursor.getColumnIndexOrThrow("city_id"));
                    String cityName = cursor.getString(cursor.getColumnIndexOrThrow("cityName")) != null
                            ? cursor.getString(cursor.getColumnIndexOrThrow("cityName"))
                            : "Không xác định";
                    String startTime = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));

                    favoriteTours.add(new TourModel(id, name, destination, price, duration, image, description, categoryId, categoryName, cityId, cityName, startTime));
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return favoriteTours;
        }
        public void addTour(String name, String destination, int cityId, double price, int duration,
                            String imagePath, int categoryId, String start_time, String description, int guideId) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(COLUMN_TOUR_NAME, name);
            values.put(COLUMN_TOUR_DESTINATION, destination);
            values.put(COLUMN_TOUR_PRICE, price);
            values.put(COLUMN_TOUR_DURATION, duration);
            values.put(COLUMN_TOUR_IMAGE, imagePath);
            values.put(COLUMN_TOUR_CATEGORY_ID, categoryId);
            values.put(COLUMN_TOUR_CITY_ID, cityId);
            values.put(COLUMN_TOUR_START_TIME, start_time);
            values.put(COLUMN_TOUR_DESCRIPTION, description);

            db.insert(TABLE_TOURS, null, values);
            db.close();
        }

        public ArrayList<TourModel> getToursByGuide(int guideId) {
            ArrayList<TourModel> tourList = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();

            String query = "SELECT t.id, t.name, t.destination, t.price, t.duration, t.image, " +
                    "t.description, c.id AS categoryId, c.name AS categoryName, " +
                    "t.city_id, ci.name AS cityName, t.start_time " +
                    "FROM tours t " +
                    "JOIN tour_guides tg ON t.id = tg.tour_id " +  // ✅ Chỉ lấy Tour của Guide hợp lệ
                    "LEFT JOIN categories c ON t.category_id = c.id " +
                    "LEFT JOIN cities ci ON t.city_id = ci.id " +
                    "WHERE tg.guide_id = ?";

            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(guideId)});

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String destination = cursor.getString(cursor.getColumnIndexOrThrow("destination"));
                    double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                    int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
                    String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("categoryId"));
                    String categoryName = cursor.getString(cursor.getColumnIndexOrThrow("categoryName"));
                    int cityId = cursor.getInt(cursor.getColumnIndexOrThrow("city_id"));
                    String cityName = cursor.getString(cursor.getColumnIndexOrThrow("cityName"));
                    String startTime = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));

                    // ✅ Thêm vào danh sách tours
                    tourList.add(new TourModel(id, name, destination, price, duration, image, description, categoryId, categoryName, cityId, cityName, startTime));
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return tourList;
        }

        public ArrayList<BookingModel> getBookingsForGuideTour(int tourId, int guideId) {
            ArrayList<BookingModel> bookingList = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();

            String query = "SELECT b.id, b.booking_date, b.status, b.adult_count, b.child_count, t.name " +
                    "FROM bookings b " +
                    "JOIN tours t ON b.tour_id = t.id " +
                    "JOIN tour_guides tg ON t.id = tg.tour_id " +
                    "WHERE b.tour_id = ? AND tg.guide_id = ? AND b.status = 'Confirmed'";

            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(tourId), String.valueOf(guideId)});

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String date = cursor.getString(1);
                    String status = cursor.getString(2);
                    int adultCount = cursor.getInt(3);
                    int childCount = cursor.getInt(4);
                    String tourName = cursor.getString(5);

                    bookingList.add(new BookingModel(id, date, status, childCount, adultCount, tourName));
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return bookingList;
        }

        public boolean isUserGuide(int userId) {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT COUNT(*) FROM tour_guides WHERE guide_id = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            boolean isGuide = false;
            if (cursor.moveToFirst()) {
                isGuide = cursor.getInt(0) > 0;
            }

            cursor.close();
            db.close();
            return isGuide;
        }
        public boolean assignUserToTourGuide(int tourId, int userId) {
            SQLiteDatabase db = this.getWritableDatabase();

            // 🚨 Kiểm tra xem hướng dẫn viên đã tồn tại chưa
            String checkQuery = "SELECT COUNT(*) FROM tour_guides WHERE tour_id = ? AND guide_id = ?";
            Cursor cursor = db.rawQuery(checkQuery, new String[]{String.valueOf(tourId), String.valueOf(userId)});

            boolean alreadyExists = false;
            if (cursor.moveToFirst()) {
                alreadyExists = cursor.getInt(0) > 0;
            }
            cursor.close();

            if (alreadyExists) {
                Log.e("DB_ERROR", "User ID " + userId + " đã là hướng dẫn viên của Tour ID " + tourId);
                return false;
            }

            // 🚀 Nếu chưa có, thêm vào bảng `tour_guides`
            ContentValues values = new ContentValues();
            values.put("tour_id", tourId);
            values.put("guide_id", userId);

            long result = db.insert("tour_guides", null, values);
            db.close();

            return result != -1;
        }

        public Cursor getAllTourGuides() {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("SELECT id, name FROM users WHERE role_id = 3", null);
        }
        public void updateTourGuide(int tourId, int guideId) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM tour_guides WHERE tour_id=?", new String[]{String.valueOf(tourId)});
            if (guideId != -1) {
                ContentValues values = new ContentValues();
                values.put("tour_id", tourId);
                values.put("guide_id", guideId);
                db.insert("tour_guides", null, values);
            }
            db.close();
        }

        public List<String> getTourImages(int tourId) {
            List<String> imageUrls = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT image_url FROM tour_images WHERE tour_id = ?", new String[]{String.valueOf(tourId)});

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    imageUrls.add(cursor.getString(0));
                }
                cursor.close();
            }
            return imageUrls;
        }
        // 🟢 Hàm thêm nhiều ảnh vào bảng tour_images
        // ✅ Kiểm tra ảnh đã tồn tại chưa
        public boolean isImageExists(int tourId, String imageUrl) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM tour_images WHERE tour_id = ? AND image_url = ?",
                    new String[]{String.valueOf(tourId), imageUrl});

            boolean exists = cursor.getCount() > 0;
            cursor.close();
            db.close();
            return exists;
        }
        // ✅ Lưu ảnh trực tiếp vào bảng tour_images
        public void addTourImage(int tourId, String imageUrl) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("tour_id", tourId);
            values.put("image_url", imageUrl);
            db.insert("tour_images", null, values);
            db.close();
        }


    }


