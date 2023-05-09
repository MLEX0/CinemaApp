package com.kp.cinemaapp;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kp.cinemaapp.model.Genre;
import com.kp.cinemaapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieScheduleActivity extends AppCompatActivity {

    private Movie OpenMovie;
    private FirebaseAuth mAuth;
    ImageView imageViewScheduleImage;

    private FirebaseDatabase  CinemaAppDB;

    private DatabaseReference ScheduleDataBase;
    private DatabaseReference HallDataBase;
    private DatabaseReference HallRowPlaceDataBase;
    private DatabaseReference TicketDataBase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_schedule);
        this.OpenMovie = Constants.openMovie;
        if(this.OpenMovie == null){
            finish();
        } else {
            this.OpenMovie = Constants.openMovie;
            Init();

            //Получение фото на фон
            Picasso.get().load(OpenMovie.movieImagePath).into(imageViewScheduleImage);
        }
    }

    private void Init(){
        mAuth = FirebaseAuth.getInstance();
        CinemaAppDB = FirebaseDatabase.getInstance("https://cinemaapp-80774-default-rtdb.firebaseio.com/");

        ScheduleDataBase = CinemaAppDB.getReference(Constants.SCHEDULE_KEY);
        HallDataBase = CinemaAppDB.getReference(Constants.HALL_KEY);
        HallRowPlaceDataBase = CinemaAppDB.getReference(Constants.HALL_ROW_PLACE_KEY);
        TicketDataBase = CinemaAppDB.getReference(Constants.TICKET_KEY);

        imageViewScheduleImage = findViewById(R.id.imageViewScheduleBackground);
    }
}
