package com.kp.cinemaapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kp.cinemaapp.adapters.BuyTicketListAdapter;
import com.kp.cinemaapp.adapters.SpinnerBuyTicketAdapter;
import com.kp.cinemaapp.model.Genre;
import com.kp.cinemaapp.model.Hall;
import com.kp.cinemaapp.model.HallRowPlace;
import com.kp.cinemaapp.model.Movie;
import com.kp.cinemaapp.model.MovieDate;
import com.kp.cinemaapp.model.Schedule;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MovieScheduleActivity extends AppCompatActivity {

    private Movie OpenMovie;
    private FirebaseAuth mAuth;
    ImageView imageViewScheduleImage;

    private FirebaseDatabase  CinemaAppDB;

    private DatabaseReference ScheduleDataBase;
    private DatabaseReference HallDataBase;
    private DatabaseReference HallRowPlaceDataBase;
    private DatabaseReference TicketDataBase;

    private TextView movieName;
    private TextView scheduleInfo;

    private SwipeRefreshLayout scheduleRefreshLayout;

    private ConstraintLayout ConstraintLayoutBuyUI;
    private ConstraintLayout ConstraintLayoutTicketsView;

    //Date
    RecyclerView rvDate;
    LinearLayoutManager dateLayoutManager;
    private ArrayList<MovieDate> movieDates;
    MovieScheduleActivity.MovieDateAdapter dateRvAdapter;
    private int globalDatePosition;

    //Time
    RecyclerView rvTime;
    LinearLayoutManager timeLayoutManager;
    private ArrayList<Schedule> movieTime;
    MovieScheduleActivity.MovieTimeAdapter timeRvAdapter;
    private int globalTimePosition;

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

            ArrayList<HallRowPlace> hallRowPlaces = new ArrayList<>();

            String hallID = GenerateID();

            hallRowPlaces.add(new HallRowPlace(HallRowPlaceDataBase.getKey(), GenerateID(), hallID, "1", "1"));
            hallRowPlaces.add(new HallRowPlace(HallRowPlaceDataBase.getKey(), GenerateID(), hallID, "1", "2"));
            hallRowPlaces.add(new HallRowPlace(HallRowPlaceDataBase.getKey(), GenerateID(), hallID, "1", "3"));
            hallRowPlaces.add(new HallRowPlace(HallRowPlaceDataBase.getKey(), GenerateID(), hallID, "1", "4"));

            hallRowPlaces.add(new HallRowPlace(HallRowPlaceDataBase.getKey(), GenerateID(), hallID, "2", "1"));
            hallRowPlaces.add(new HallRowPlace(HallRowPlaceDataBase.getKey(), GenerateID(), hallID, "2", "2"));
            hallRowPlaces.add(new HallRowPlace(HallRowPlaceDataBase.getKey(), GenerateID(), hallID, "2", "3"));
            hallRowPlaces.add(new HallRowPlace(HallRowPlaceDataBase.getKey(), GenerateID(), hallID, "2", "4"));
            hallRowPlaces.add(new HallRowPlace(HallRowPlaceDataBase.getKey(), GenerateID(), hallID, "2", "5"));

            hallRowPlaces.add(new HallRowPlace(HallRowPlaceDataBase.getKey(), GenerateID(), hallID, "3", "1"));
            hallRowPlaces.add(new HallRowPlace(HallRowPlaceDataBase.getKey(), GenerateID(), hallID, "3", "2"));
            hallRowPlaces.add(new HallRowPlace(HallRowPlaceDataBase.getKey(), GenerateID(), hallID, "3", "3"));
            hallRowPlaces.add(new HallRowPlace(HallRowPlaceDataBase.getKey(), GenerateID(), hallID, "3", "4"));
            hallRowPlaces.add(new HallRowPlace(HallRowPlaceDataBase.getKey(), GenerateID(), hallID, "3", "5"));

            hallRowPlaces.add(new HallRowPlace(HallRowPlaceDataBase.getKey(), GenerateID(), hallID, "4", "1"));
            hallRowPlaces.add(new HallRowPlace(HallRowPlaceDataBase.getKey(), GenerateID(), hallID, "4", "2"));
            hallRowPlaces.add(new HallRowPlace(HallRowPlaceDataBase.getKey(), GenerateID(), hallID, "4", "3"));
            hallRowPlaces.add(new HallRowPlace(HallRowPlaceDataBase.getKey(), GenerateID(), hallID, "4", "4"));
            hallRowPlaces.add(new HallRowPlace(HallRowPlaceDataBase.getKey(), GenerateID(), hallID, "4", "5"));

            Hall hall = new Hall(HallDataBase.getKey(), hallID, "3", "imagePath",
                    hallRowPlaces);

            /*HallDataBase.push().setValue(hall).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });*/

            Schedule schedule = new Schedule(ScheduleDataBase.getKey(), GenerateID(), "48227305-753321073-1091710138", "502797301540546072-579375682", "22-5-2023", "15:00", "450");

            /*ScheduleDataBase.push().setValue(schedule).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }});*/


            //Получение фото на фон
            Picasso.get().load(OpenMovie.movieImagePath).into(imageViewScheduleImage);

            movieName.setText(OpenMovie.Name);

            scheduleInfo.setText("Выберите время сеанса");

            getScheduleDates();
            UpdateScheduleTimeFromDatabase();

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

        movieName = findViewById(R.id.textViewScheduleMovieName);

        scheduleInfo = findViewById(R.id.textViewScheduleInfo);

        //Refresh Layout
        scheduleRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.scheduleRefreshLayout);
        scheduleRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UpdateScheduleTimeFromDatabase();
            }
        });

        ConstraintLayoutBuyUI = findViewById(R.id.ConstraintLayoutBuyUI);
        ConstraintLayoutTicketsView = findViewById(R.id.ConstraintLayoutTicketsView);

        //Date
        movieDates = new ArrayList<>();
        rvDate = findViewById(R.id.recyclerViewScheduleDate);
        globalDatePosition = 0;

        //Time
        movieTime = new ArrayList<>();
        rvTime = findViewById(R.id.recyclerViewScheduleTime);
        globalTimePosition = 0;
    }


    private void getScheduleDates(){
        // Получаем текущую дату и время
        Calendar calendar = Calendar.getInstance();

        // Заполняем ArrayList данными на две недели, начиная с текущей даты
        for (int i = 0; i < 14; i++) {
            String id = Integer.toString(i + 1);
            String dayNumber = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
            String dayOfWeekName = getRussianDayOfWeekName(calendar.get(Calendar.DAY_OF_WEEK));
            String dateInFormat = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH))
                    + "-" + Integer.toString(calendar.get(Calendar.MONTH) + 1) + "-"
                    + Integer.toString(calendar.get(Calendar.YEAR));
            MovieDate movieDate = new MovieDate(id, dayNumber, dayOfWeekName, dateInFormat);
            movieDates.add(movieDate);
            // Переходим к следующей дате
            calendar.add(Calendar.DATE, 1);
        }
        for (MovieDate date: movieDates) {
            Log.i("MovieDates", date.fullDate);
        }
        globalDatePosition = 0;
        UpdateScheduleDateList();

    }


    private void UpdateScheduleDateList() {
        dateLayoutManager = new LinearLayoutManager(MovieScheduleActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        dateLayoutManager.setSmoothScrollbarEnabled(false);
        dateLayoutManager.scrollToPosition(globalDatePosition);
        dateRvAdapter = new MovieScheduleActivity.MovieDateAdapter(movieDates);
        rvDate.setLayoutManager(dateLayoutManager);
        rvDate.setAdapter(dateRvAdapter);
    }

    private void UpdateScheduleTimeFromDatabase() {
        //--TimeStart
        if(movieTime.isEmpty()){
            scheduleRefreshLayout.setRefreshing(true);
        }
        ValueEventListener mainGenreListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(movieTime.size() > 0){movieTime.clear();}

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    //Заполняем массив жанров
                    Schedule schedule = ds.getValue(Schedule.class);
                    assert schedule != null;
                    if(schedule.movieID.equals(OpenMovie.generatedMovieID)){
                        if(schedule.date.equals(movieDates.get(globalDatePosition).fullDate)){
                            if(isTimeNotPassed(schedule.time, schedule.date)) {
                                movieTime.add(schedule);
                            }
                        }
                    }
                }
                if(movieTime.size() == 0){
                    Toast.makeText(MovieScheduleActivity.this,
                            "Не нашлось доступного времени на выбранную дату." +
                                    "\nПроверьте интернет соединение",
                            Toast.LENGTH_SHORT).show();
                    scheduleRefreshLayout.setRefreshing(false);
                    HideBuyUI();
                } else {
                    UpdateTicketListView();
                }
                globalTimePosition = 0;
                UpdateScheduleTimeList();
                scheduleRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MovieScheduleActivity.this,
                        "Проверьте интернет соединение!",
                        Toast.LENGTH_SHORT).show();
                scheduleRefreshLayout.setRefreshing(false);
            }
        };

        ScheduleDataBase.removeEventListener(mainGenreListener);
        ScheduleDataBase.addValueEventListener(mainGenreListener);
        //--TimeEnd
        UpdateScheduleTimeList();
    }

    public static boolean isTimeNotPassed(String inputTime, String inputDate) {
        boolean result = false;
        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentTime.get(Calendar.MINUTE);

        String currentDate = Integer.toString(currentTime.get(Calendar.DAY_OF_MONTH))
                + "-" + Integer.toString(currentTime.get(Calendar.MONTH) + 1) + "-"
                + Integer.toString(currentTime.get(Calendar.YEAR));

        if(!currentDate.equals(inputDate)){
            return true;
        }

        // Преобразуем строку в формате "hh:mm" в целочисленные значения времени
        String[] input = inputTime.split(":");
        int inputHour = Integer.parseInt(input[0]);
        int inputMinute = Integer.parseInt(input[1]);

        // Сравниваем текущее время и введенное время
        if (currentHour < inputHour) {
            result = true;
        } else if (currentHour == inputHour && currentMinute < inputMinute) {
            result = true;
        }
        return result;
    }



    private void UpdateScheduleTimeList() {
        timeLayoutManager = new LinearLayoutManager(MovieScheduleActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        timeLayoutManager.setSmoothScrollbarEnabled(false);
        timeLayoutManager.scrollToPosition(globalDatePosition);
        timeRvAdapter = new MovieScheduleActivity.MovieTimeAdapter(movieTime);
        rvTime.setLayoutManager(timeLayoutManager);
        rvTime.setAdapter(timeRvAdapter);
    }


    // Метод получения названия дня недели на русском языке
    private static String getRussianDayOfWeekName(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return "Пн";
            case Calendar.TUESDAY:
                return "Вт";
            case Calendar.WEDNESDAY:
                return "Ср";
            case Calendar.THURSDAY:
                return "Чт";
            case Calendar.FRIDAY:
                return "Пт";
            case Calendar.SATURDAY:
                return "Сб";
            case Calendar.SUNDAY:
                return "Вс";
            default:
                return "";
        }
    }

    private void HideBuyUI(){
        ConstraintLayoutBuyUI.setVisibility(View.GONE);
        ConstraintLayoutTicketsView.setVisibility(View.GONE);
    }

    private void ShowBuyUI(){
        ConstraintLayoutBuyUI.setVisibility(View.VISIBLE);
        ConstraintLayoutTicketsView.setVisibility(View.VISIBLE);
    }

    private void UpdateTicketListView(){
        Schedule selectedSchedule = movieTime.get(globalTimePosition);
        assert selectedSchedule != null;
        if(movieTime.size() != 0){
            ShowBuyUI();
        }
        else {
            HideBuyUI();
        }
    }

    //--DateRVAdapterStart
    class MovieDateAdapter extends RecyclerView.Adapter<MovieScheduleActivity.MovieDateAdapter.DateHolder> {
        ArrayList<MovieDate> data;

        public MovieDateAdapter(ArrayList<MovieDate> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public DateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MovieScheduleActivity.this).inflate(R.layout.item_list_date, parent, false);
            return new DateHolder(view);
        }


        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull  MovieScheduleActivity.MovieDateAdapter.DateHolder holder, int position) {
            holder.dateBackground.setBackground(null);
            if(position == globalDatePosition){
                holder.dateBackground.setBackground(getResources().getDrawable(R.drawable.item_date_background_highlighted));
            } else {
                holder.dateBackground.setBackground(getResources().getDrawable(R.drawable.item_date_background));
            }
            holder.tvScheduleDayOfWeek.setText(data.get(position).DayOfWeekName);
            holder.tvScheduleDayOfMonth.setText(data.get(position).DayNumber);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class DateHolder extends RecyclerView.ViewHolder {
            TextView tvScheduleDayOfWeek;
            TextView tvScheduleDayOfMonth;
            ImageView dateBackground;

            public DateHolder(@NonNull View itemView) {
                super(itemView);
                dateBackground = itemView.findViewById(R.id.date_background);
                tvScheduleDayOfWeek = itemView.findViewById(R.id.textViewScheduleDayOfWeek);
                tvScheduleDayOfMonth = itemView.findViewById(R.id.textViewScheduleDayOfMonth);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        globalDatePosition=getAdapterPosition();

                        UpdateScheduleDateList();
                        UpdateScheduleTimeFromDatabase();
                    }
                });
            }
        }

    }
    //--DateRVAdapterEnd


    //--TimeRVAdapterStart
    class MovieTimeAdapter extends RecyclerView.Adapter<MovieScheduleActivity.MovieTimeAdapter.TimeHolder> {
        ArrayList<Schedule> data;

        public MovieTimeAdapter(ArrayList<Schedule> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public TimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MovieScheduleActivity.this).inflate(R.layout.item_list_time, parent, false);
            return new TimeHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull  MovieScheduleActivity.MovieTimeAdapter.TimeHolder holder, int position) {
            if(position == globalTimePosition){
                holder.TimeCardView.setCardBackgroundColor(Color.parseColor("#697BEA"));
            }
            holder.tvTime.setText(data.get(position).time);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class TimeHolder extends RecyclerView.ViewHolder {
            TextView tvTime;
            CardView TimeCardView;

            public TimeHolder(@NonNull View itemView) {
                super(itemView);
                TimeCardView = itemView.findViewById(R.id.TimeCardView);
                tvTime = itemView.findViewById(R.id.tvMovieTime);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        globalTimePosition=getAdapterPosition();

                        UpdateScheduleTimeList();
                        UpdateTicketListView();
                    }
                });
            }
        }

    }
    //--TimeRVAdapterEnd

    public void cancelOnClick(View view) {
        finish();
    }



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
