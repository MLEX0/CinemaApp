package com.kp.cinemaapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kp.cinemaapp.model.Genre;
import com.kp.cinemaapp.model.Movie;
import com.kp.cinemaapp.model.Schedule;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainFrameActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;

    private FirebaseDatabase  CinemaAppDB;
    private DatabaseReference UserDataBase;
    private DatabaseReference MovieDataBase;
    private DatabaseReference CinemaDataBase;
    private DatabaseReference GenreDataBase;
    private DatabaseReference ScheduleDataBase;
    private DatabaseReference HallDataBase;
    private DatabaseReference HallRowPlaceDataBase;
    private DatabaseReference TicketDataBase;

    private BottomNavigationView BottomNav;

    SwipeRefreshLayout mainRefreshLayout;
    SwipeRefreshLayout cinemasRefreshLayout;
    SwipeRefreshLayout ticketsRefreshLayout;
    SwipeRefreshLayout profileRefreshLayout;

    ConstraintLayout mainConstraintLayout;
    ConstraintLayout cinemasConstraintLayout;
    ConstraintLayout ticketsConstraintLayout;
    ConstraintLayout profileConstraintLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_frame);

        Init();

        SetBottomNavigationSelectedListener();
    }

    private void Init(){

        mAuth = FirebaseAuth.getInstance();

        BottomNav = findViewById(R.id.bottom_navigation);

        CinemaAppDB = FirebaseDatabase.getInstance("https://cinemaapp-80774-default-rtdb.firebaseio.com/");

        UserDataBase = CinemaAppDB.getReference(Constants.USER_KEY);
        MovieDataBase = CinemaAppDB.getReference(Constants.MOVIE_KEY);
        CinemaDataBase = CinemaAppDB.getReference(Constants.CINEMA_KEY);
        GenreDataBase = CinemaAppDB.getReference(Constants.GENRE_KEY);
        ScheduleDataBase = CinemaAppDB.getReference(Constants.SCHEDULE_KEY);
        HallDataBase = CinemaAppDB.getReference(Constants.HALL_KEY);
        HallRowPlaceDataBase = CinemaAppDB.getReference(Constants.HALL_ROW_PLACE_KEY);
        TicketDataBase = CinemaAppDB.getReference(Constants.TICKET_KEY);

        mainConstraintLayout = findViewById(R.id.mainConstraintLayout);
        cinemasConstraintLayout = findViewById(R.id.cinemasConstraintLayout);
        ticketsConstraintLayout = findViewById(R.id.ticketsConstraintLayout);
        profileConstraintLayout = findViewById(R.id.profileConstraintLayout);

        //Refresh Layout
        mainRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.mainRefreshLayout);
        mainRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mainRefreshLayout.setRefreshing(false);
            }
        });

        cinemasRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.cinemasRefreshLayout);
        cinemasRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cinemasRefreshLayout.setRefreshing(false);
            }
        });

        ticketsRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.ticketsRefreshLayout);
        ticketsRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ticketsRefreshLayout.setRefreshing(false);
            }
        });

        profileRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.profileRefreshLayout);
        profileRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                profileRefreshLayout.setRefreshing(false);
            }
        });

    }




    private void SetBottomNavigationSelectedListener() {

        BottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Постраничная навигация при нажатии на кнопку меню
                switch(item.getItemId()) {
                    case R.id.nav_main:
                        showMainLayout();
                        break;
                    case R.id.nav_cinemas:
                        showCinemasLayout();;
                        break;
                    case R.id.nav_tickets:
                        showTicketsLayout();
                        break;
                    case R.id.nav_profile:
                        showProfileLayout();
                        break;
                }
                return true;
            }
        });
    }

    public void showMainLayout() {
        mainConstraintLayout.setVisibility(View.VISIBLE);
        cinemasConstraintLayout.setVisibility(View.GONE);
        ticketsConstraintLayout.setVisibility(View.GONE);
        profileConstraintLayout.setVisibility(View.GONE);
    }
    public void showCinemasLayout() {
        mainConstraintLayout.setVisibility(View.GONE);
        cinemasConstraintLayout.setVisibility(View.VISIBLE);
        ticketsConstraintLayout.setVisibility(View.GONE);
        profileConstraintLayout.setVisibility(View.GONE);
    }
    public void showTicketsLayout() {
        mainConstraintLayout.setVisibility(View.GONE);
        cinemasConstraintLayout.setVisibility(View.GONE);
        ticketsConstraintLayout.setVisibility(View.VISIBLE);
        profileConstraintLayout.setVisibility(View.GONE);
    }
    public void showProfileLayout() {
        mainConstraintLayout.setVisibility(View.GONE);
        cinemasConstraintLayout.setVisibility(View.GONE);
        ticketsConstraintLayout.setVisibility(View.GONE);
        profileConstraintLayout.setVisibility(View.VISIBLE);
    }

    private String GenerateID(){
        Calendar rightNow = Calendar.getInstance();
        long offset = rightNow.get(Calendar.ZONE_OFFSET) +
                rightNow.get(Calendar.DST_OFFSET);
        long sinceMidnight = (rightNow.getTimeInMillis() + offset) %
                (24 * 60 * 60 * 1000);
        Random rnd = new Random();
        return sinceMidnight + "" + rnd.nextInt() + "" + rnd.nextInt();
    }
}
