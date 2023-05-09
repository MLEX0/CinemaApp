package com.kp.cinemaapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kp.cinemaapp.model.Genre;
import com.kp.cinemaapp.model.Movie;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class ViewMovieActivity extends AppCompatActivity {

    private Movie OpenMovie;
    ImageView imageViewMovieImage;
    ScrollView movieScrollView;

    TextView TextViewMovieName;

    RecyclerView rvGenre;
    LinearLayoutManager genreLayoutManager;
    private ArrayList<Genre> listGenre;
    MovieGenreRvAdapter genreRvAdapter;

    TextView TextViewAboutMovie;
    TextView TextViewAboutActors;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_view_movie);
        if(Constants.openMovie == null){
            finish();
        }
        else {
            this.OpenMovie = Constants.openMovie;
            Init();
            //Костыльный костыль
            View MovieView50Percent = (View) findViewById(R.id.movieView50Percent);
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            MovieView50Percent.setLayoutParams(new LinearLayout.LayoutParams(width, (height * 50)/100));

            //Получение фото на фон
            Picasso.get().load(OpenMovie.movieImagePath).into(imageViewMovieImage);

            TextViewMovieName.setText(this.OpenMovie.Name);
            TextViewAboutMovie.setText(this.OpenMovie.Description);
            TextViewAboutActors.setText(this.OpenMovie.Actors);

            int padding = getResources().getDisplayMetrics().heightPixels / 2;
            movieScrollView.scrollTo(padding, 0);
            movieScrollView.setClipToPadding(false);

            int i = 0;
            for (String genre:this.OpenMovie.GenresID ) {
                listGenre.add(new Genre(String.valueOf(i), String.valueOf(i), genre));
                i++;
            }

            genreLayoutManager  = new LinearLayoutManager(ViewMovieActivity.this,
                    LinearLayoutManager.HORIZONTAL, false);
            genreRvAdapter = new ViewMovieActivity.MovieGenreRvAdapter(listGenre);
            rvGenre.setLayoutManager(genreLayoutManager);
            rvGenre.setAdapter(genreRvAdapter);
            rvGenre.setNestedScrollingEnabled(false);
        }
    }

    private void Init(){
        imageViewMovieImage = findViewById(R.id.imageViewMovieBackground);
        movieScrollView = findViewById(R.id.movieScrollView);

        TextViewMovieName = findViewById(R.id.textViewMovieName);

        listGenre = new ArrayList<Genre>();
        rvGenre = findViewById(R.id.RecyclerViewMovieGenre);

        TextViewAboutMovie = findViewById(R.id.textViewAboutMovie);
        TextViewAboutActors = findViewById(R.id.textViewAboutActors);
    }

    //--MovieGenreRVAdapterStart
    class MovieGenreRvAdapter extends RecyclerView.Adapter<MovieGenreRvAdapter.MovieGenreHolder> {
        ArrayList<Genre> data;

        public MovieGenreRvAdapter(ArrayList<Genre> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public MovieGenreRvAdapter.MovieGenreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ViewMovieActivity.this).inflate(R.layout.item_list_genre, parent, false);
            return new MovieGenreRvAdapter.MovieGenreHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MovieGenreRvAdapter.MovieGenreHolder holder, int position) {
            holder.setIsRecyclable(false);
            holder.tvGenreTitle.setText(data.get(position).Name);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MovieGenreHolder extends RecyclerView.ViewHolder {
            TextView tvGenreTitle;
            public MovieGenreHolder(@NonNull View itemView) {
                super(itemView);
                tvGenreTitle = itemView.findViewById(R.id.tvGenreName);
            }
        }
    }
    //--MovieGenreRVAdapterEnd

    public void toMovieSchedule(View view){
        Intent openMovieIntent = new Intent(ViewMovieActivity.this, MovieScheduleActivity.class);
        startActivity(openMovieIntent);
        finish();
    }

    /*public class CustomGridLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomGridLayoutManager(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
        }
        @Override
        public boolean canScrollHorizontally() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollHorizontally();
        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.openMovie = null;
    }
    public void cancelOnClick(View view) {
        finish();
    }
}
