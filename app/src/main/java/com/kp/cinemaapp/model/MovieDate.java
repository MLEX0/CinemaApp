package com.kp.cinemaapp.model;

public class MovieDate {
    String id, DayNumber, DayOfWeekName;

    public MovieDate() {
    }

    public MovieDate(String id, String dayNumber, String dayOfWeekName) {
        this.id = id;
        this.DayNumber = dayNumber;
        this.DayOfWeekName = dayOfWeekName;
    }
}
