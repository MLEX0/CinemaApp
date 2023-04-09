package com.kp.cinemaapp.model;

public class Hall {
    public String hallID, generatedHallID, ImagePath;

    public Hall() {
    }

    public Hall(String hallID, String generatedHallID, String imagePath) {
        this.hallID = hallID;
        this.generatedHallID = generatedHallID;
        this.ImagePath = imagePath;
    }
}
