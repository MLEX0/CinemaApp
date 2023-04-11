package com.kp.cinemaapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kp.cinemaapp.model.Movie;

public class ViewMovieActivity extends AppCompatActivity {

    private Movie OpenMovie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_view_movie);
        if(Constants.openMovie == null){
            finish();
        }
        else {
            this.OpenMovie = Constants.openMovie;
            Toast.makeText(ViewMovieActivity.this,
                    OpenMovie.Name,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.openMovie = null;
    }
}
