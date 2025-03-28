package com.example.appdoctruyen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.example.appdoctruyen.Object.TruyenTranh;
import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    private EditText edtTenTruyen, edtChap, edtLinkAnh, edtDescription;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Ánh xạ các View
        edtTenTruyen = findViewById(R.id.edtTenTruyen);
        edtChap = findViewById(R.id.edtChap);
        edtLinkAnh = findViewById(R.id.edtLinkAnh);
        edtDescription = findViewById(R.id.edtDescription);

        sharedPreferences = getSharedPreferences("TruyenData", MODE_PRIVATE);  // Khởi tạo SharedPreferences
    }

    // Hàm xử lý khi người dùng nhấn nút Lưu
    public void onSave(View view) {
        // Lấy dữ liệu từ các EditText
        String tenTruyen = edtTenTruyen.getText().toString().trim();
        String chap = edtChap.getText().toString().trim();
        String linkAnh = edtLinkAnh.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        // Kiểm tra xem các trường có rỗng không
        if (tenTruyen.isEmpty() || chap.isEmpty() || linkAnh.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else {
            // Tạo đối tượng TruyenTranh mới
            TruyenTranh newTruyen = new TruyenTranh(tenTruyen, chap, linkAnh, description);

            // Lấy danh sách truyện đã lưu từ SharedPreferences
            ArrayList<TruyenTranh> truyenList = getTruyenListFromPreferences();

            // Thêm truyện mới vào danh sách
            truyenList.add(newTruyen);

            // Lưu danh sách truyện mới vào SharedPreferences
            saveTruyenListToPreferences(truyenList);

            // Trả lại dữ liệu cho MainActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("newTruyen", newTruyen);
            setResult(RESULT_OK, resultIntent);
1
            Toast.makeText(this, "Thêm truyện thành công!", Toast.LENGTH_SHORT).show();
            finish(); // Trở về màn hình chính
        }
    }

    // Hàm lấy danh sách truyện từ SharedPreferences
    private ArrayList<TruyenTranh> getTruyenListFromPreferences() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("truyenList", "[]");
        TruyenTranh[] truyenArray = gson.fromJson(json, TruyenTranh[].class);
        ArrayList<TruyenTranh> truyenList = new ArrayList<>();
        for (TruyenTranh truyen : truyenArray) {
            truyenList.add(truyen);
        }
        return truyenList;
    }

    // Hàm lưu danh sách truyện vào SharedPreferences
    private void saveTruyenListToPreferences(ArrayList<TruyenTranh> truyenList) {
        Gson gson = new Gson();
        String json = gson.toJson(truyenList);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("truyenList", json);
        editor.apply();
    }

}
