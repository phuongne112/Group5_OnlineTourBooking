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
    import com.example.group5_onlinetourbookingsystem.models.UserModel;

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
        private static final String COLUMN_USER_BIRTH = "birth_date";
    
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

        private static final String COLUMN_TOUR_GUIDE_ID = "guide_id";  // Cột mới để lưu ID người hướng dẫn
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
        private static final String COLUMN_BOOKING_CREATED_AT = "created_at";
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
                    COLUMN_USER_PHONE + " TEXT, " +
                    COLUMN_USER_PASSWORD + " TEXT, " +
                    COLUMN_USER_BIRTH + " TEXT, " +
                    COLUMN_USER_IMAGE + " TEXT, " +  // ➕ Cột lưu ảnh đại diện
                    COLUMN_USER_ROLE_ID + " INTEGER, " +
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
                    COLUMN_BOOKING_ADULT_COUNT + " INTEGER, " +
                    COLUMN_BOOKING_CHILD_COUNT + " INTEGER, " +
                    COLUMN_BOOKING_NOTE + " TEXT, " +
                    COLUMN_BOOKING_CREATED_AT + " TEXT DEFAULT CURRENT_TIMESTAMP, " +  // ✅ Thêm cột created_at với giá trị mặc định
                    "FOREIGN KEY(" + COLUMN_BOOKING_USER_ID + ") REFERENCES users(id), " +
                    "FOREIGN KEY(" + COLUMN_BOOKING_TOUR_ID + ") REFERENCES tours(id))");




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
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOUR_IMAGES);
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

            String query = "SELECT t.id, t.name, t.destination, t.price, t.duration, t.image, t.description, " +
                    "c.id AS categoryId, c.name AS categoryName, ci.name AS cityName " +
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
                    String description = cursor.getString(6); // Lấy description
                    int categoryId = cursor.getInt(7);
                    String categoryName = cursor.getString(8);
                    String cityName = cursor.getString(9);

                    tourList.add(new TourModel(id, name, destination, price, duration, image, description, categoryId, categoryName, cityName));
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return tourList;
        }






        public long addUser(String name, String email, String phone, String hashedPassword, String birthDate, String imagePath) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_NAME, name);
            values.put(COLUMN_USER_EMAIL, email);
            values.put(COLUMN_USER_PHONE, phone);
            values.put(COLUMN_USER_PASSWORD, hashedPassword); // ✅ Lưu mật khẩu đã mã hóa
            values.put(COLUMN_USER_BIRTH, birthDate);
            values.put(COLUMN_USER_IMAGE, imagePath);
    
            long result = db.insert(TABLE_USERS, null, values);
            db.close();
            return result;
        }
    
    
        public int checkUserLogin(String email, String hashedPassword) {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT " + COLUMN_USER_PASSWORD + " FROM " + TABLE_USERS +
                    " WHERE " + COLUMN_USER_EMAIL + " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{email});

            if (cursor != null && cursor.moveToFirst()) {
                String storedPassword = cursor.getString(0);
                cursor.close();
                db.close();

                // ✅ So sánh mật khẩu đã mã hóa
                if (storedPassword.equals(hashedPassword)) {
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
    
            String query = "SELECT t.id, t.name, t.destination,t.description , t.price, t.duration, t.image, c.id AS categoryId, c.name AS categoryName, ci.name AS cityName " +
                    "FROM tours t " +
                    "LEFT JOIN categories c ON t.category_id = c.id " +
                    "LEFT JOIN cities ci ON t.city_id = ci.id " +
                    "WHERE t.id = ?";
    
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
    
            if (cursor.moveToFirst()) {
                int tourId = cursor.getInt(0);
                String name = cursor.getString(1);
                String destination = cursor.getString(2);
                String description = cursor.getString(3); // ✅ Lấy đúng cột description
                double price = cursor.getDouble(4);
                int duration = cursor.getInt(5);
                String image = cursor.getString(6);
                int categoryId = cursor.getInt(7);
                String categoryName = cursor.getString(8);
                String cityName = cursor.getString(9);

                // Tạo đối tượng TourModel
                tour = new TourModel(tourId, name, destination, price, duration, image,description, categoryId, categoryName, cityName);
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
            values.put("created_at", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));

            long result = db.insert("bookings", null, values);
            db.close();
            return result;
        }





        public boolean isUserExists(String email) {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM users WHERE email = ?";
            Cursor cursor = db.rawQuery(query, new String[]{email});
            boolean exists = cursor.getCount() > 0;
            cursor.close();
            db.close();
            return exists;
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
    
            String query = "SELECT id, name, email, phone, password, birth_date, image FROM users WHERE id = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
    
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String email = cursor.getString(2);
                String phone = cursor.getString(3);
                String password = cursor.getString(4);
                String birthDate = cursor.getString(5);
                String image = cursor.getString(6);
    
                user = new UserModel(id, name, email, phone, password, birthDate, image);
            }
    
            cursor.close();
            db.close();
            return user;
        }
        public int getUserIdByEmail(String email) {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT id FROM users WHERE email = ?";
            Cursor cursor = db.rawQuery(query, new String[]{email});
    
            int userId = -1; // Nếu không tìm thấy user
            if (cursor.moveToFirst()) {
                userId = cursor.getInt(0);
            }
            cursor.close();
            db.close();
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
            values.put(COLUMN_USER_IMAGE, imagePath); // ✅ Cập nhật ảnh đại diện
    
            int rowsAffected = db.update(TABLE_USERS, values, "id=?", new String[]{String.valueOf(userId)});
            db.close();
            return rowsAffected > 0;
        }

        public ArrayList<TourModel> searchTours(String query) {
            ArrayList<TourModel> tourList = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();

            // Chuyển query thành không dấu để tìm kiếm
            String queryNoAccent = removeDiacritics(query);

            // Truy vấn dữ liệu từ bảng tours, categories, cities
            String sql = "SELECT t." + COLUMN_TOUR_ID + ", t." + COLUMN_TOUR_NAME +
                    ", t." + COLUMN_TOUR_DESTINATION + ", t." + COLUMN_TOUR_PRICE +
                    ", t." + COLUMN_TOUR_DURATION + ", t." + COLUMN_TOUR_IMAGE +
                    ", t." + COLUMN_TOUR_DESCRIPTION +  // Thêm description
                    ", c." + COLUMN_CATEGORY_ID + ", c." + COLUMN_CATEGORY_NAME +
                    ", ci." + COLUMN_CITY_NAME +  // Lấy cityName từ bảng cities
                    " FROM " + TABLE_TOURS + " t " +
                    "LEFT JOIN " + TABLE_CATEGORIES + " c ON t." + COLUMN_TOUR_CATEGORY_ID + " = c." + COLUMN_CATEGORY_ID + " " +
                    "LEFT JOIN " + TABLE_CITIES + " ci ON t." + COLUMN_TOUR_CITY_ID + " = ci." + COLUMN_CITY_ID;

            Cursor cursor = db.rawQuery(sql, null);

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String destination = cursor.getString(2);
                    double price = cursor.getDouble(3);
                    int duration = cursor.getInt(4);
                    String image = cursor.getString(5);
                    String description = cursor.getString(6); // Lấy description
                    int categoryId = cursor.getInt(7);
                    String categoryName = cursor.getString(8);
                    String cityName = cursor.getString(9);

                    // Xử lý null để tránh lỗi
                    if (name == null) name = "";
                    if (categoryName == null) categoryName = "";

                    // Chuyển thành không dấu để so sánh
                    String nameNoAccent = removeDiacritics(name);
                    String categoryNoAccent = removeDiacritics(categoryName);

                    // Kiểm tra nếu query khớp với name hoặc category
                    if (nameNoAccent.contains(queryNoAccent) || categoryNoAccent.contains(queryNoAccent)) {
                        tourList.add(new TourModel(id, name, destination, price, duration, image, description, categoryId, categoryName, cityName));
                    }
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return tourList;
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


        public void updateBookingStatus(long bookingId, String status) {  // Sửa int thành long
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("status", status);

            db.update("bookings", values, "id = ?", new String[]{String.valueOf(bookingId)});
            db.close();
        }






    }
