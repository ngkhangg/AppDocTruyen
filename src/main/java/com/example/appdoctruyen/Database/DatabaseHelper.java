package com.example.appdoctruyen.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AppDocTruyenDB";
    private static final int DATABASE_VERSION = 1;

    // Tên bảng và cột cho bảng người dùng
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    // Tên bảng và cột cho bảng truyện
    private static final String TABLE_TRUYEN = "truyen";
    private static final String COLUMN_TEN_TRUYEN = "ten_truyen";
    private static final String COLUMN_CHAP = "chap";
    private static final String COLUMN_LINK_ANH = "link_anh";
    private static final String COLUMN_DESCRIPTION = "description";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng người dùng
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // Tạo bảng truyện
        String CREATE_TRUYEN_TABLE = "CREATE TABLE " + TABLE_TRUYEN + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TEN_TRUYEN + " TEXT, " +
                COLUMN_CHAP + " TEXT, " +
                COLUMN_LINK_ANH + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT)";
        db.execSQL(CREATE_TRUYEN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRUYEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Thêm người dùng
    public boolean addUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;  // Trả về true nếu thêm thành công
    }

    // Cập nhật truyện
    public boolean updateStory(int storyId, String tenTruyen, String chap, String linkAnh, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TEN_TRUYEN, tenTruyen);
        values.put(COLUMN_CHAP, chap);
        values.put(COLUMN_LINK_ANH, linkAnh);
        values.put(COLUMN_DESCRIPTION, description);

        int rowsAffected = db.update(TABLE_TRUYEN, values, COLUMN_ID + " = ?", new String[]{String.valueOf(storyId)});
        db.close();
        return rowsAffected > 0;
    }

    // Kiểm tra đăng nhập người dùng
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null);

        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }

    // Thêm truyện mới
    public boolean addTruyen(String tenTruyen, String chap, String linkAnh, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEN_TRUYEN, tenTruyen);
        values.put(COLUMN_CHAP, chap);
        values.put(COLUMN_LINK_ANH, linkAnh);
        values.put(COLUMN_DESCRIPTION, description);

        long result = db.insert(TABLE_TRUYEN, null, values);
        db.close();
        return result != -1;  // Trả về true nếu thêm thành công
    }

    // Tìm kiếm truyện theo từ khóa
    public ArrayList<String> searchTruyen(String keyword) {
        ArrayList<String> resultList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_TEN_TRUYEN + " FROM " + TABLE_TRUYEN +
                " WHERE " + COLUMN_TEN_TRUYEN + " LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + keyword + "%"});

        if (cursor.moveToFirst()) {
            do {
                String tenTruyen = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_TRUYEN));
                resultList.add(tenTruyen);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return resultList;
    }
}
