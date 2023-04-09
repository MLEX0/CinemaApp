package com.kp.cinemaapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kp.cinemaapp.adapters.CinemaListAdapter;
import com.kp.cinemaapp.model.Cinema;
import com.kp.cinemaapp.model.Genre;
import com.kp.cinemaapp.model.Movie;
import com.kp.cinemaapp.model.Schedule;
import com.kp.cinemaapp.model.User;

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

    ConstraintLayout CLAuthToReg;
    ConstraintLayout CLAuth;
    ConstraintLayout CLReg;

    //unauthorizedProfileConstraintLayout

    ConstraintLayout CLUnauthorizedProfile;
    ConstraintLayout CLAuthorizedProfile;

    private EditText textEmail;
    private EditText textPassword;

    private EditText textEmailReg;
    private EditText textPasswordReg;
    private EditText textPasswordReg2;

    private ListView listViewCinema;
    private ArrayList<Cinema> listCinema;
    CinemaListAdapter cinemaAdapter;

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

        //регистрация и авторизация в профиле

        CLUnauthorizedProfile = findViewById(R.id.unauthorizedProfileConstraintLayout);
        CLAuthorizedProfile = findViewById(R.id.authorizedProfileConstraintLayout);

        CLAuthToReg = findViewById(R.id.clAuthOrReg);
        CLAuth = findViewById(R.id.clAuth);
        CLReg = findViewById(R.id.clRegistration);

        textEmail = findViewById(R.id.editTextTextEmailAddress);
        textPassword = findViewById(R.id.editTextTextPassword);
        textEmailReg = findViewById(R.id.editTextTextEmailAddressREG);
        textPasswordReg = findViewById(R.id.editTextTextPassWordREG);
        textPasswordReg2 = findViewById(R.id.editTextTextRepeatPassWordREG);


        //Инициализируем адаптеры для ListView
        listViewCinema = findViewById(R.id.listViewCinema);
        listCinema = new ArrayList<Cinema>();
        cinemaAdapter = new CinemaListAdapter(MainFrameActivity.this, listCinema);
        listViewCinema.setAdapter(cinemaAdapter);
        listViewCinema.setSelector(android.R.color.transparent);//Не кликабельный

        //Refresh Layout
        mainRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.mainRefreshLayout);
        mainRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateMain();
            }
        });

        cinemasRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.cinemasRefreshLayout);
        cinemasRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cinemasRefreshLayout.setRefreshing(true);
                updateCinemas();
            }
        });

        ticketsRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.ticketsRefreshLayout);
        ticketsRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateTickets();
            }
        });

        profileRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.profileRefreshLayout);
        profileRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                profileRefreshLayout.setRefreshing(true);
                updateProfile();
            }
        });

    }


    public void updateMain(){


        mainRefreshLayout.setRefreshing(false);
    }

    public void updateCinemas(){
        if(listCinema.isEmpty()){
            cinemasRefreshLayout.setRefreshing(true);
        }
        ValueEventListener cinemavListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(listCinema.size() > 0){listCinema.clear();}
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    //Заполняем массив кинотеатров
                    Cinema cinema = ds.getValue(Cinema.class);
                    assert cinema != null;
                    listCinema.add(cinema);
                }
                cinemaAdapter.notifyDataSetChanged();
                cinemasRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainFrameActivity.this,
                        "Проверьте интернет соединение!",
                        Toast.LENGTH_SHORT).show();
                cinemasRefreshLayout.setRefreshing(false);
            }
        };

        CinemaDataBase.removeEventListener(cinemavListener);
        CinemaDataBase.addValueEventListener(cinemavListener);
        cinemaAdapter.notifyDataSetChanged();
    }

    public void updateTickets(){



        ticketsRefreshLayout.setRefreshing(false);
    }


    //--ProfileStart
    public void updateProfile(){

        if(mAuth.getCurrentUser() == null){
            CLUnauthorizedProfile.setVisibility(View.VISIBLE);
            CLAuthorizedProfile.setVisibility(View.GONE);
            backToStartAuth();
        }
        else {
            CLUnauthorizedProfile.setVisibility(View.GONE);
            CLAuthorizedProfile.setVisibility(View.VISIBLE);
            updateAuthProfile();
        }

        profileRefreshLayout.setRefreshing(false);
    }

    public void updateAuthProfile(){

    }

    public void signOutOnClick(View view){
        mAuth.signOut();
        updateProfile();
    }

    //--ProfileEnd

    //--RegistrationAuthorizationStart

    public void backToStartAuth(){
        CLAuthToReg.setVisibility(View.VISIBLE);
        CLAuth.setVisibility(View.GONE);
        CLReg.setVisibility(View.GONE);
        clearFields();
    }

    public void toAuthOnClick(View view){
        CLAuthToReg.setVisibility(View.GONE);
        CLAuth.setVisibility(View.VISIBLE);
        CLReg.setVisibility(View.GONE);
    }

    public void toRegOnClick(View view){
        CLAuthToReg.setVisibility(View.GONE);
        CLAuth.setVisibility(View.GONE);
        CLReg.setVisibility(View.VISIBLE);
    }

    public void clearFields(){
        textEmail.setText("");
        textPassword.setText("");;
        textEmailReg.setText("");;
        textPasswordReg.setText("");;
        textPasswordReg2.setText("");;
    }

    public void showPassOnClick(View view){
        View eyeClose = findViewById(R.id.imageViewEnterPasswordEye_close);
        View eyeOpen = findViewById(R.id.imageViewEnterPasswordEye_open);
        EditText pswBox = findViewById(R.id.editTextTextPassword);
        pswBox.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        eyeClose.setVisibility(View.GONE);
        eyeOpen.setVisibility(View.VISIBLE);
    }

    public void hidePassOnClick(View view){
        View eyeClose = findViewById(R.id.imageViewEnterPasswordEye_close);
        View eyeOpen = findViewById(R.id.imageViewEnterPasswordEye_open);
        EditText pswBox = findViewById(R.id.editTextTextPassword);
        pswBox.setTransformationMethod(PasswordTransformationMethod.getInstance());
        eyeClose.setVisibility(View.VISIBLE);
        eyeOpen.setVisibility(View.GONE);
    }

    public void authOnClick(View view){
        String Email = textEmail.getText().toString();
        String Password = textPassword.getText().toString();

        if(Email.isEmpty()) {

            Toast.makeText(MainFrameActivity.this,
                    "Поле 'Email' не может быть пустым!", Toast.LENGTH_LONG).show();

        }
        else if (Password.isEmpty()) {
            Toast.makeText(MainFrameActivity.this,
                    "Поле 'Пароль' не может быть пустым!", Toast.LENGTH_LONG).show();
        } else {
            cinemasRefreshLayout.setRefreshing(true);
            mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()) {
                        Toast.makeText(MainFrameActivity.this,
                                "Email или пароль введены неверно!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainFrameActivity.this,
                                "Авторизация успешна!", Toast.LENGTH_LONG).show();

                        //Переход на новое окно при успешной авторизации
                        cinemasRefreshLayout.setRefreshing(false);
                        clearFields();
                        updateProfile();
                    }
                }
            });
        }
    }

    public void registrationClick(View view){
        String EmailReg = textEmailReg.getText().toString();
        String PasswordReg = textPasswordReg.getText().toString();
        String PasswordReg2 = textPasswordReg2.getText().toString();

        if(EmailReg.isEmpty()) {
            Toast.makeText(MainFrameActivity.this,
                    "Поле 'Email' не может быть пустым!", Toast.LENGTH_LONG).show();
        } else if (PasswordReg.isEmpty()) {
            Toast.makeText(MainFrameActivity.this,
                    "Поле 'Пароль' не может быть пустым!", Toast.LENGTH_LONG).show();

        } else if (!PasswordReg.equals(PasswordReg2)) {
            Toast.makeText(MainFrameActivity.this,
                    "Пароли не совпадают!", Toast.LENGTH_LONG).show();

        } else {
            cinemasRefreshLayout.setRefreshing(true);
            mAuth.createUserWithEmailAndPassword(EmailReg, PasswordReg).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(MainFrameActivity.this,
                                "Регистрация провалена!", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(MainFrameActivity.this,
                                "Регистрация успешна!", Toast.LENGTH_LONG).show();

                        mAuth.signInWithEmailAndPassword(EmailReg, PasswordReg).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if(user != null)
                                    {
                                        User newUser = new User(UserDataBase.getKey(),user.getUid(), null, null, null, null);
                                        UserDataBase.push().setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    //Переход на новое окно при успешной регистрации
                                                    cinemasRefreshLayout.setRefreshing(false);
                                                    updateProfile();
                                                    clearFields();
                                                }
                                            }
                                        });

                                    }
                                }
                            }
                        });

                    }
                }
            });
        }
    }

    //--RegistrationAuthorizationEnd

    private void SetBottomNavigationSelectedListener() {

        BottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Постраничная навигация при нажатии на кнопку меню
                switch(item.getItemId()) {
                    case R.id.nav_main:
                        showMainLayout();
                        updateMain();
                        break;
                    case R.id.nav_cinemas:
                        showCinemasLayout();
                        updateCinemas();
                        break;
                    case R.id.nav_tickets:
                        showTicketsLayout();
                        updateTickets();
                        break;
                    case R.id.nav_profile:
                        showProfileLayout();
                        updateProfile();
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
