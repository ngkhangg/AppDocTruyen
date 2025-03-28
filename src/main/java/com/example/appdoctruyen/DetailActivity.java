package com.example.appdoctruyen;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;


public class DetailActivity extends AppCompatActivity {

    private ImageView imgTruyen;
    private TextView txtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imgTruyen = findViewById(R.id.imgTruyen);
        txtDescription = findViewById(R.id.txvDescription);

        // Lấy thông tin truyện được truyền từ MainActivity
        String linkAnh = getIntent().getStringExtra("linkAnh");
        String description = getIntent().getStringExtra("description");

        // Set ảnh và mô tả cho ImageView và TextView
        Picasso.get().load(linkAnh).into(imgTruyen);
        txtDescription.setText(description);
    }
}
