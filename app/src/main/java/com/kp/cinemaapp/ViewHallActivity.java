package com.kp.cinemaapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ViewHallActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hall);

        Bundle arguments = getIntent().getExtras();
        String imagePathHall = arguments.get("hall_image_path").toString();

        ImageView imageViewHallScheme = findViewById(R.id.imageViewHallScheme);
        Picasso.get().load(imagePathHall).into(imageViewHallScheme);

    }

    public void cancelOnClick(View view) {
        finish();
    }
}
