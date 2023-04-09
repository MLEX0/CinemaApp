package com.kp.cinemaapp.model;

public class Cinema {
    public String cinemaID, generatedCinemaID, CinemaName, imagePath, Address;

    public Cinema() {
    }

    public Cinema(String cinemaID, String generatedCinemaID, String cinemaName, String imagePath, String address) {
        this.cinemaID = cinemaID;
        this.generatedCinemaID = generatedCinemaID;
        this.CinemaName = cinemaName;
        this.imagePath = imagePath;
        this.Address = address;
    }
}
