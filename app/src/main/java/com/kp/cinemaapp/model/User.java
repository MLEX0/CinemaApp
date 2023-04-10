package com.kp.cinemaapp.model;

public class User {

    public String userID, Uid, FirstName, Patronymic, LastName, Phone, ProfileImagePath;

    public User(){
    }

    public User(String userID, String uid, String firstName, String patronymic, String lastName, String phone, String profileImagePath) {
        this.userID = userID;
        this.Uid = uid;
        this.FirstName = firstName;
        this.Patronymic = patronymic;
        this.LastName = lastName;
        this.Phone = phone;
        this.ProfileImagePath = profileImagePath;
    }
}
