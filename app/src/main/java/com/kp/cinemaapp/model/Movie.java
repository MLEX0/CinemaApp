package com.kp.cinemaapp.model;

import java.lang.reflect.Array;
import java.util.List;

public class Movie {

    public String movieID, generatedMovieID, Name, Description, Actors, Length, movieImagePath;

    public List<String> GenresID;

    public Movie() {
    }

    public Movie(String movieID, String generatedMovieID, String name, String description,
                 String actors, String length, String movieImagePath, List<String> genres) {
        this.movieID = movieID;
        this.generatedMovieID = generatedMovieID;
        this.Name = name;
        this.Description = description;
        this.Actors = actors;
        this.Length = length;
        this.movieImagePath = movieImagePath;
        this.GenresID = genres;
    }
}
