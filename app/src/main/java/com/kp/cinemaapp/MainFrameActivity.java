package com.kp.cinemaapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.kp.cinemaapp.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainFrameActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

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

    private SwipeRefreshLayout mainRefreshLayout;
    private SwipeRefreshLayout cinemasRefreshLayout;
    private SwipeRefreshLayout ticketsRefreshLayout;
    private SwipeRefreshLayout profileRefreshLayout;

    private ConstraintLayout mainConstraintLayout;
    private ConstraintLayout cinemasConstraintLayout;
    private ConstraintLayout ticketsConstraintLayout;
    private ConstraintLayout profileConstraintLayout;

    private ConstraintLayout CLAuthToReg;
    private ConstraintLayout CLAuth;
    private ConstraintLayout CLReg;

    //unauthorizedProfileConstraintLayout

    private ConstraintLayout CLUnauthorizedProfile;
    private ConstraintLayout CLAuthorizedProfile;

    private EditText textEmail;
    private EditText textPassword;

    private EditText textEmailReg;
    private EditText textPasswordReg;
    private EditText textPasswordReg2;

    //Cinema
    private ListView listViewCinema;
    private ArrayList<Cinema> listCinema;
    private CinemaListAdapter cinemaAdapter;

    //Genre
    RecyclerView rvGenre;
    LinearLayoutManager genreLayoutManager;
    private ArrayList<Genre> listGenre;
    GenreRvAdapter genreRvAdapter;
    public int globalGenrePosition;

    //Movie
    RecyclerView rvMovie;
    LinearLayoutManager movieLayoutManager;
    private ArrayList<Movie> listMovie;
    MovieRvAdapter movieRvAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_frame);

        Init();

        SetBottomNavigationSelectedListener();

        List<String> genresForMovie = new ArrayList<String>();
        genresForMovie.add("Комедия");


        Movie movie = new Movie(MovieDataBase.getKey(), GenerateID(), "ЁЛКИ 9", "О фильме:\n" +
                "За считанные часы до праздника все с замиранием сердца надеются на чудо. " +
                "Вся деревня Глухарево готовится с размахом встречать зарубежную кинозвезду. " +
                "В Тюмени домохозяйке и будущей блогерше необходимо выиграть пари с мужем. " +
                "В Екатеринбурге влюбленный ролевик разрывается между семейным счастьем и эпическими приключениями. " +
                "В Санкт-Петербурге девушка вместе с долгожданным предложением руки и сердца узнает, что ее жених был мошенником. " +
                "Как всегда, судьбы героев неожиданно переплетутся и каждому достанется своя частичка новогоднего волшебства.",
                "Виктория Агалакова,Евгений Кулик,Федор Добронравов, " +
                        "Алексей Серебряков, Максим Лагашкин, Елена Валюшкина, " +
                        "Григорий Калинин, Александр Баширов, Елена Захарова, " +
                        "Софья Присс, Ростислав Бершауэр, Валерия Человечкова, " +
                        "Михаил Орлов, Наталия Валькович, Юлия Макарова, " +
                        "Андрей Никульский", "1 час 30 минут", "путь вставить сюда", genresForMovie);

        /*MovieDataBase.push().setValue(movie).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });*/

        updateMain();

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


        //Инициализируем адаптеры для ListView и RecyclerView

        //Cinema
        listViewCinema = findViewById(R.id.listViewCinema);
        listCinema = new ArrayList<Cinema>();
        cinemaAdapter = new CinemaListAdapter(MainFrameActivity.this, listCinema);
        listViewCinema.setAdapter(cinemaAdapter);
        listViewCinema.setSelector(android.R.color.transparent);//Не кликабельный


        //Genre
        listGenre = new ArrayList<Genre>();
        rvGenre = findViewById(R.id.RecyclerViewGenre);

        //Movie
        listMovie = new ArrayList<Movie>();
        rvMovie = findViewById(R.id.RecyclerViewMovie);

        //Tickets


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

    //--MainStart

    public void updateMain(){
        //--GenreStart
        if(listGenre.isEmpty()){
            mainRefreshLayout.setRefreshing(true);
        }
        ValueEventListener mainGenreListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(listGenre.size() > 0){listGenre.clear();}
                Genre AllGenre = new Genre("0", "0", "Все");
                listGenre.add(AllGenre);
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    //Заполняем массив жанров
                    Genre genre = ds.getValue(Genre.class);
                    assert genre != null;
                    listGenre.add(genre);
                }
                UpdateGenreList();
                mainRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainFrameActivity.this,
                        "Проверьте интернет соединение!",
                        Toast.LENGTH_SHORT).show();
                mainRefreshLayout.setRefreshing(false);
            }
        };

        GenreDataBase.removeEventListener(mainGenreListener);
        GenreDataBase.addValueEventListener(mainGenreListener);

        //--GenreEnd
        if(listMovie.isEmpty()){
            mainRefreshLayout.setRefreshing(true);
        }

        ValueEventListener mainMovieListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(listMovie.size() > 0){listMovie.clear();}
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    //Заполняем массив жанров
                    Movie movie = ds.getValue(Movie.class);
                    assert movie != null;
                    listMovie.add(movie);
                }
                UpdateMovieList();
                mainRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainFrameActivity.this,
                        "Проверьте интернет соединение!",
                        Toast.LENGTH_SHORT).show();
                mainRefreshLayout.setRefreshing(false);
            }
        };

        MovieDataBase.removeEventListener(mainMovieListener);
        MovieDataBase.addValueEventListener(mainMovieListener);
    }

    public void UpdateGenreList(){
        genreLayoutManager = new LinearLayoutManager(MainFrameActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        genreRvAdapter = new GenreRvAdapter(listGenre);
        rvGenre.setLayoutManager(genreLayoutManager);
        rvGenre.setAdapter(genreRvAdapter);
    }

    public void UpdateMovieList(){
        movieLayoutManager = new LinearLayoutManager(MainFrameActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        movieRvAdapter = new MovieRvAdapter(listMovie);
        rvMovie.setLayoutManager(movieLayoutManager);
        rvMovie.setAdapter(movieRvAdapter);
    }

    //--GenreRVAdapterStart
    class GenreRvAdapter extends RecyclerView.Adapter<GenreRvAdapter.GenreHolder> {
        ArrayList<Genre> data;

        public GenreRvAdapter(ArrayList<Genre> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public GenreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainFrameActivity.this).inflate(R.layout.item_list_genre, parent, false);
            return new GenreHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GenreHolder holder, int position) {
            if(position == globalGenrePosition){
                holder.tvGenreTitle.setTextColor(Color.parseColor("#727CBA"));
            }
            holder.tvGenreTitle.setText(data.get(position).Name);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class GenreHolder extends RecyclerView.ViewHolder {
            TextView tvGenreTitle;

            public GenreHolder(@NonNull View itemView) {
                super(itemView);
                tvGenreTitle = itemView.findViewById(R.id.tvGenreName);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        globalGenrePosition=getAdapterPosition();
                        UpdateGenreList();
                    }
                });
            }
        }

    }
    //--GenreRVAdapterEnd

    //--MovieRVAdapterStart
    class MovieRvAdapter extends RecyclerView.Adapter<MovieRvAdapter.MovieHolder> {
        ArrayList<Movie> data;

        public MovieRvAdapter(ArrayList<Movie> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainFrameActivity.this).inflate(R.layout.item_list_movie, parent, false);
            return new MovieHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
            if(data.get(position).movieImagePath != null){
                Picasso.get().load(data.get(position).movieImagePath).into(holder.tvMovieImage);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MovieHolder extends RecyclerView.ViewHolder {
            ImageView tvMovieImage;
            public MovieHolder(@NonNull View itemView) {
                super(itemView);
                tvMovieImage = itemView.findViewById(R.id.roundedMovieImageView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Constants.openMovie = listMovie.get(getAdapterPosition());
                        Intent openMovieIntent = new Intent(MainFrameActivity.this, ViewMovieActivity.class);
                        startActivity(openMovieIntent);
                    }
                });
            }
        }

    }
    //--MovieRVAdapterEnd

    //--MainEnd

    //--CinemasStart
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

    //--CinemasEnd


    //--TicketsStart

    public void updateTickets(){



        ticketsRefreshLayout.setRefreshing(false);
    }

    //--TicketsEnd

    //--ProfileStart
    public void updateProfile(){

        if(mAuth.getCurrentUser() == null){
            hideKeyboard();
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
        profileRefreshLayout.setRefreshing(true);
        ValueEventListener userValueEventListener = new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Пытаемся найти пользователя в БД
                ArrayList<User> userArrayList = new ArrayList<User>();
                for(DataSnapshot ds2 : dataSnapshot.getChildren()){
                    User user = ds2.getValue(User.class);
                    assert user != null;
                    if(user.Uid.equals(mAuth.getUid().toString())){
                        //Заполнение всех полей данными о пользователе
                        TextView tvProfileUserName = findViewById(R.id.textViewNameUserProfile);
                        tvProfileUserName.setMovementMethod(new ScrollingMovementMethod());
                        tvProfileUserName.setHorizontallyScrolling(true);

                        TextView tvProfileUserPhone = findViewById(R.id.textViewPhoneUserProfile);
                        tvProfileUserPhone.setMovementMethod(new ScrollingMovementMethod());
                        tvProfileUserPhone.setHorizontallyScrolling(true);

                        TextView tvProfileUserEmail = findViewById(R.id.textViewEmailUserProfile);
                        tvProfileUserEmail.setMovementMethod(new ScrollingMovementMethod());
                        tvProfileUserEmail.setHorizontallyScrolling(true);

                        CircleImageView profilePicture = findViewById(R.id.profile_image);

                        if(user.FirstName != null){
                            tvProfileUserName.setText(user.FirstName);
                        }
                        if(user.LastName != null){

                            tvProfileUserName.setText(user.LastName);
                        }
                        if(user.FirstName != null && user.LastName != null){
                            tvProfileUserName.setText(user.FirstName + " "
                                    + user.LastName);
                        }
                        if(user.Phone != null){
                            tvProfileUserPhone.setText(user.Phone);
                        }
                        if(mAuth.getCurrentUser() != null){
                            if(mAuth.getCurrentUser().getEmail() != null){
                                tvProfileUserEmail.setText(mAuth.getCurrentUser().getEmail());
                            }
                        }
                        if(user.ProfileImagePath != null){
                            Picasso.get().load(user.ProfileImagePath).into(profilePicture);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                profileRefreshLayout.setRefreshing(false);
            }
        };

        UserDataBase.addValueEventListener(userValueEventListener);
    }

    public void toEditProfile(View view){
        Intent editProfile = new Intent(MainFrameActivity.this, EditProfileActivity.class);
        startActivity(editProfile);
        updateProfile();
    }

    public void toSupportService(View view){
        Intent supportService = new Intent(MainFrameActivity.this, SupportServiceActivity.class);
        startActivity(supportService);
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
                        hideKeyboard();
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
                                        User newUser = new User(UserDataBase.getKey(),user.getUid(), null, null, null, null, null);
                                        UserDataBase.push().setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    hideKeyboard();
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

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //--RegistrationAuthorizationEnd


    //--BottomNavigationStart
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

    //--BottomNavigationEnd

    //--HelpMethodsStart
    private String GenerateID(){
        Calendar rightNow = Calendar.getInstance();
        long offset = rightNow.get(Calendar.ZONE_OFFSET) +
                rightNow.get(Calendar.DST_OFFSET);
        long sinceMidnight = (rightNow.getTimeInMillis() + offset) %
                (24 * 60 * 60 * 1000);
        Random rnd = new Random();
        return sinceMidnight + "" + rnd.nextInt() + "" + rnd.nextInt();
    }
    //--HelpMethodsEnd
}
