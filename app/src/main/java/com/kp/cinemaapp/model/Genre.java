package com.kp.cinemaapp.model;

public class Genre {

    public String genreID, generatedGenreID, Name;

    public Genre() {
    }

    public Genre(String genreID, String generatedGenreID, String name) {
        this.genreID = genreID;
        this.generatedGenreID = generatedGenreID;
        this.Name = name;
    }
}
