package com.kp.cinemaapp.model;

public class User {

    public String userID, Uid, FirstName, Patronymic, LastName, Phone;

    public User(){
    }

    public User(String userid, String uid, String firstName, String patronymic, String lastName, String phone) {
        this.userID = userid;
        this.Uid = uid;
        this.FirstName = firstName;
        this.Patronymic = patronymic;
        this.LastName = lastName;
        this.Phone = phone;
    }
}
