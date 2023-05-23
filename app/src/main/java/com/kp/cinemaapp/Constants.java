package com.kp.cinemaapp;

import com.kp.cinemaapp.model.Movie;
import com.kp.cinemaapp.model.Ticket;

import java.util.ArrayList;

public class Constants {
    public final static String USER_KEY = "User";
    public final static String CINEMA_KEY = "Cinema";
    public final static String MOVIE_KEY = "Movie";
    public final static String GENRE_KEY = "Genre";
    public final static String SCHEDULE_KEY = "Schedule";
    public final static String HALL_KEY = "Hall";
    public final static String HALL_ROW_PLACE_KEY = "HallRowPlace";
    public final static String TICKET_KEY = "Ticket";
    public static Movie openMovie;
    public static ArrayList<Ticket> selectedTickets;

    public static Ticket openTicket;

}
