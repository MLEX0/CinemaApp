package com.kp.cinemaapp.model;

import java.util.ArrayList;
import java.util.List;

public class Hall {
    public String hallID, hallNumber, generatedHallID, ImagePath, allPlaceCount;
    public ArrayList<HallRowPlace> hallRowPlaces;

    public Hall() {
    }

    public Hall(String hallID, String generatedHallID, String hallNumber, String imagePath, ArrayList<HallRowPlace> hallRowPlaces) {
        this.hallID = hallID;
        this.generatedHallID = generatedHallID;
        this.hallNumber = hallNumber;
        this.ImagePath = imagePath;
        this.hallRowPlaces = hallRowPlaces;
        this.allPlaceCount = String.valueOf(hallRowPlaces.size());
    }
}
