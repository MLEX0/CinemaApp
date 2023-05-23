package com.kp.cinemaapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kp.cinemaapp.model.Cinema;
import com.kp.cinemaapp.model.Movie;
import com.kp.cinemaapp.model.Ticket;


public class ViewTicketActivity extends AppCompatActivity {

    TextView textViewTicketMovieTitle;
    TextView textViewTicketMovieDateAndTime;
    TextView textViewTicketMovieHall;
    TextView textViewTicketMovieRow;
    TextView textViewTicketMoviePlace;

    private FirebaseDatabase CinemaAppDB;
    private DatabaseReference MovieDataBase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ticket);

        if(Constants.openTicket == null){
            finish();
        }

        CinemaAppDB = FirebaseDatabase.getInstance("https://cinemaapp-80774-default-rtdb.firebaseio.com/");

        MovieDataBase = CinemaAppDB.getReference(Constants.MOVIE_KEY);

        Ticket ticket = Constants.openTicket;

        textViewTicketMovieTitle = findViewById(R.id.textViewTicketMovieTitle);
        textViewTicketMovieDateAndTime = findViewById(R.id.textViewTicketMovieDateAndTime);
        textViewTicketMovieHall = findViewById(R.id.textViewTicketMovieHall);
        textViewTicketMovieRow = findViewById(R.id.textViewTicketMovieRow);
        textViewTicketMoviePlace = findViewById(R.id.textViewTicketMoviePlace);

        MovieDataBase.orderByChild("generatedMovieID")
                .equalTo(ticket.getThisSchedule().movieID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            //Заполняем массив кинотеатров
                            Movie movie = ds.getValue(Movie.class);
                            assert movie != null;
                            textViewTicketMovieTitle.setText(movie.Name);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        textViewTicketMovieDateAndTime.setText(ticket.getThisSchedule().date.replace('-', '.')
                + " " + ticket.getThisSchedule().time);
        textViewTicketMovieHall.setText(ticket.getThisSchedule().hall.hallNumber);
        textViewTicketMovieRow.setText(ticket.ticketPlace.row);
        textViewTicketMoviePlace.setText(ticket.ticketPlace.place);


    }

    public void cancelOnClick(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.openTicket = null;
    }
}
