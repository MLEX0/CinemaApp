package com.kp.cinemaapp.model;

public class HallRowPlace {
    public String hallRowPlaceID, generatedHallRowPlaceID, hallID, row, place;
    public boolean isBusy;

    public HallRowPlace() {
    }

    public HallRowPlace(String hallRowPlaceID, String generatedHallRowPlaceID, String hallID, String row, String place, Boolean isBusy) {
        this.hallRowPlaceID = hallRowPlaceID;
        this.generatedHallRowPlaceID = generatedHallRowPlaceID;
        this.hallID = hallID;
        this.row = row;
        this.place = place;
        this.isBusy = isBusy;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
