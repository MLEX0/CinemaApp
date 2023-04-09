package com.kp.cinemaapp.model;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class Schedule {
    public String scheduleID, generatedScheduleID, movieID, hallID;
    public String date;
    public String time;
    public String cost;

    public Schedule() {
    }

    public Schedule(String scheduleID, String generatedScheduleID, String movieID, String hallID, String date, String time, String cost) {
        this.scheduleID = scheduleID;
        this.generatedScheduleID = generatedScheduleID;
        this.movieID = movieID;
        this.hallID = hallID;
        this.date = date;
        this.time = time;
        this.cost = cost;
    }
}
