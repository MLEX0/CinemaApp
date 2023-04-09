package com.kp.cinemaapp.model;

public class HallRowPlace {
    public String hallRowPlaceID, generatedHallRowPlaceID, hallID, row, place;

    public HallRowPlace() {
    }

    public HallRowPlace(String hallRowPlaceID, String generatedHallRowPlaceID, String hallID, String row, String place) {
        this.hallRowPlaceID = hallRowPlaceID;
        this.generatedHallRowPlaceID = generatedHallRowPlaceID;
        this.hallID = hallID;
        this.row = row;
        this.place = place;
    }
}
