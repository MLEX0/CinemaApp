package com.kp.cinemaapp.model;

public class MovieDate {
    public String id, DayNumber, DayOfWeekName, fullDate;

    public MovieDate() {
    }

    public MovieDate(String id, String dayNumber, String dayOfWeekName, String fullDate) {
        this.id = id;
        this.DayNumber = dayNumber;
        this.DayOfWeekName = dayOfWeekName;
        this.fullDate = fullDate;
    }
}
