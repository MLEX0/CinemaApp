package com.kp.cinemaapp.model;

public class Ticket {
    public String ticketID, generatedTicketID, scheduleID, userUID;

    public HallRowPlace ticketPlace;

    public Ticket() {
    }

    public Ticket(String ticketID, String generatedTicketID, String scheduleID, String userUID, HallRowPlace ticketPlace) {
        this.ticketID = ticketID;
        this.generatedTicketID = generatedTicketID;
        this.scheduleID = scheduleID;
        this.userUID = userUID;
        this.ticketPlace = ticketPlace;
    }
}
