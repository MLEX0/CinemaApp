package com.kp.cinemaapp.model;

public class Ticket {
    public String ticketID, generatedTicketID, scheduleID, userUID, hallRowPlaceID;

    public Ticket() {
    }

    public Ticket(String ticketID, String generatedTicketID, String scheduleID, String userUID, String hallRowPlaceID) {
        this.ticketID = ticketID;
        this.generatedTicketID = generatedTicketID;
        this.scheduleID = scheduleID;
        this.userUID = userUID;
        this.hallRowPlaceID = hallRowPlaceID;
    }
}
