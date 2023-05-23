package com.kp.cinemaapp.model;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class Schedule {
    public String scheduleID, generatedScheduleID, movieID;
    public String date;
    public String time;
    public String cost;

    public Hall hall;

    public Schedule() {
    }

    public Schedule(String scheduleID, String generatedScheduleID, String movieID, Hall hall, String date, String time, String cost) {
        this.scheduleID = scheduleID;
        this.generatedScheduleID = generatedScheduleID;
        this.movieID = movieID;
        this.hall = hall;
        this.date = date;
        this.time = time;
        this.cost = cost;
    }
}
