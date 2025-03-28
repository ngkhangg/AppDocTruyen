package com.example.appdoctruyen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appdoctruyen.Object.TruyenTranh;

public class EditActivity extends AppCompatActivity {

    private EditText edtTenTruyen, edtChapter, edtLinkAnh, edtDescription;
    private TruyenTranh truyenToEdit;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Initialize EditText fields
        edtTenTruyen = findViewById(R.id.edtTenTruyen);
        edtChapter = findViewById(R.id.edtChap);
        edtLinkAnh = findViewById(R.id.edtLinkAnh);
        edtDescription = findViewById(R.id.edtDescription);

        // Get the TruyenTranh object and position passed from the previous activity
        Intent intent = getIntent();
        truyenToEdit = (TruyenTranh) intent.getSerializableExtra("truyen");
        position = intent.getIntExtra("position", -1);

        // If the TruyenTranh object is not null, populate the fields with the current data
        if (truyenToEdit != null) {
            edtTenTruyen.setText(truyenToEdit.getTenTruyen());
            edtChapter.setText(truyenToEdit.getTenChap());
            edtLinkAnh.setText(truyenToEdit.getLinkAnh());
            edtDescription.setText(truyenToEdit.getDescription());
        }
    }

    // Method to handle the save button click
    public void onSave(View view) {
        // Update the TruyenTranh object with new values from the EditText fields
        truyenToEdit.setTenTruyen(edtTenTruyen.getText().toString());
        truyenToEdit.setTenChap(edtChapter.getText().toString());
        truyenToEdit.setLinkAnh(edtLinkAnh.getText().toString());
        truyenToEdit.setDescription(edtDescription.getText().toString());

        // Return the updated TruyenTranh object and position back to MainActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedTruyen", truyenToEdit);
        resultIntent.putExtra("position", position);
        setResult(RESULT_OK, resultIntent);

        // Show a toast to confirm save
        Toast.makeText(this, "Lưu thành công!", Toast.LENGTH_SHORT).show();

        // Finish the activity and return to the previous screen
        finish();
    }

    // Phương thức kiểm tra xem có thay đổi nào trong dữ liệu không
    private boolean isDataChanged() {
        // Sử dụng trim để loại bỏ khoảng trắng dư thừa trước khi so sánh
        return !truyenToEdit.getTenTruyen().trim().equals(edtTenTruyen.getText().toString().trim()) ||
                !truyenToEdit.getTenChap().trim().equals(edtChapter.getText().toString().trim()) ||
                !truyenToEdit.getLinkAnh().trim().equals(edtLinkAnh.getText().toString().trim()) ||
                !truyenToEdit.getDescription().trim().equals(edtDescription.getText().toString().trim());
    }

    public void onBack(View view) {
        if (!isDataChanged()) {
            finish();  // Nếu không có thay đổi, quay lại mà không thông báo
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Bạn chưa lưu thay đổi, bạn có muốn quay lại không?")
                    .setCancelable(false)
                    .setPositiveButton("Có", (dialog, id) -> finish()) // Quay lại mà không lưu
                    .setNegativeButton("Không", null) // Không làm gì nếu người dùng chọn "Không"
                    .show();
        }
    }
}
