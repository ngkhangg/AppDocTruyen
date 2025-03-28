package com.example.appdoctruyen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appdoctruyen.Adapter.TruyenTranhAdapter;
import com.example.appdoctruyen.Object.TruyenTranh;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    GridView gdvDanhSachTruyen;
    TruyenTranhAdapter adapter;
    ArrayList<TruyenTranh> truyenTranhArrayList;
    ArrayList<TruyenTranh> allTruyenTranh;
    EditText edtTimKiem;
    SharedPreferences sharedPreferences;
    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        anhXa();
        setUp();
        setClick();
    }

    private void init() {
        truyenTranhArrayList = new ArrayList<>();
        allTruyenTranh = new ArrayList<>();
        sharedPreferences = getSharedPreferences("TruyenData", MODE_PRIVATE);
        loadTruyenData();

        // If no data exists, add some dummy data
        if (truyenTranhArrayList.isEmpty()) {
            truyenTranhArrayList.add(new TruyenTranh("Đại Quản Gia Là Ma Hoàng", "Chap 622", "https://s1.fstscnmw.org/dai-quan-gia-la-ma-hoang/thumbnail/dqg.jpg", "Description..."));
        }

        allTruyenTranh.addAll(truyenTranhArrayList);
    }

    private void anhXa() {
        edtTimKiem = findViewById(R.id.edtTimKiem);
        gdvDanhSachTruyen = findViewById(R.id.gdvDanhSachTruyen);
 
        adapter = new TruyenTranhAdapter(MainActivity.this, truyenTranhArrayList);
        gdvDanhSachTruyen.setAdapter(adapter);
    }

    private void setUp() {
        gdvDanhSachTruyen.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            TruyenTranh selectedTruyen = truyenTranhArrayList.get(position);
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("linkAnh", selectedTruyen.getLinkAnh());
            intent.putExtra("description", selectedTruyen.getDescription());
            startActivity(intent);
        });
    }

    // Set click listeners
    private void setClick() {
        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần thực hiện hành động gì ở đây
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Không cần thực hiện hành động gì khi văn bản đang thay đổi
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchQuery = edtTimKiem.getText().toString().toUpperCase();

                // Xóa dữ liệu cũ trong danh sách truyenTranhArrayList
                if (searchQuery.isEmpty()) {
                    adapter.updateData(allTruyenTranh);  // Nếu không có từ khóa tìm kiếm, hiển thị tất cả
                } else {
                    ArrayList<TruyenTranh> filteredList = new ArrayList<>();
                    for (TruyenTranh truyen : allTruyenTranh) {
                        if (truyen.getTenTruyen().toUpperCase().contains(searchQuery)) {
                            filteredList.add(truyen);
                        }
                    }
                    adapter.updateData(filteredList);  // Cập nhật danh sách với dữ liệu tìm kiếm
                }
            }
        });
    }


    public void showMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_truyentranh, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_add) {
                Toast.makeText(MainActivity.this, "Thêm được chọn", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, 1);
                return true;
            } else if (item.getItemId() == R.id.action_edit) {
                Toast.makeText(MainActivity.this, "Sửa được chọn", Toast.LENGTH_SHORT).show();
                if (selectedPosition >= 0) {
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    TruyenTranh truyenToEdit = truyenTranhArrayList.get(selectedPosition);
                    intent.putExtra("truyen", truyenToEdit);
                    intent.putExtra("position", selectedPosition);
                    startActivityForResult(intent, 2);
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng chọn một truyện để sửa!", Toast.LENGTH_SHORT).show();
                }
                return true;
            } else if (item.getItemId() == R.id.action_delete) {
                Toast.makeText(MainActivity.this, "Xóa được chọn", Toast.LENGTH_SHORT).show();
                if (selectedPosition >= 0) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Bạn có chắc chắn muốn xóa truyện này?")
                            .setCancelable(false)
                            .setPositiveButton("Có", (dialog, id) -> deleteTruyen())
                            .setNegativeButton("Không", null)
                            .show();
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng chọn một truyện để xóa!", Toast.LENGTH_SHORT).show();
                }
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            TruyenTranh newTruyen = (TruyenTranh) data.getSerializableExtra("newTruyen");
            truyenTranhArrayList.add(newTruyen);  // Add to the main list
            adapter.notifyDataSetChanged();
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            TruyenTranh updatedTruyen = (TruyenTranh) data.getSerializableExtra("updatedTruyen");
            int position = data.getIntExtra("position", -1);
            if (position >= 0) {
                truyenTranhArrayList.set(position, updatedTruyen);  // Update the item
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void deleteTruyen() {
        if (selectedPosition >= 0) {
            truyenTranhArrayList.remove(selectedPosition);
            allTruyenTranh.remove(selectedPosition);
            saveTruyenData();
            adapter.notifyDataSetChanged();
            selectedPosition = -1;
            Toast.makeText(MainActivity.this, "Truyện đã được xóa", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveTruyenData() {
        Gson gson = new Gson();
        String json = gson.toJson(truyenTranhArrayList);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("truyenList", json);
        editor.apply();
    }

    private void loadTruyenData() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("truyenList", "[]");
        TruyenTranh[] truyenArray = gson.fromJson(json, TruyenTranh[].class);

        truyenTranhArrayList.clear();
        allTruyenTranh.clear();

        if (truyenArray != null) {
            for (TruyenTranh truyen : truyenArray) {
                truyenTranhArrayList.add(truyen);
            }
            allTruyenTranh.addAll(truyenTranhArrayList);
        }
    }

    @Override
    public void onBackPressed() {
        saveTruyenData();
        super.onBackPressed();
    }
}
