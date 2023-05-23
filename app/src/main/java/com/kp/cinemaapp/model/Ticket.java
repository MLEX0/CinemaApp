package com.kp.cinemaapp.model;

public class Ticket {
    public String ticketID, generatedTicketID, scheduleID, userUID;

    public HallRowPlace ticketPlace;

    private Schedule thisSchedule;

    public Ticket() {
    }

    public Ticket(String ticketID, String generatedTicketID, String scheduleID, String userUID, HallRowPlace ticketPlace) {
        this.ticketID = ticketID;
        this.generatedTicketID = generatedTicketID;
        this.scheduleID = scheduleID;
        this.userUID = userUID;
        this.ticketPlace = ticketPlace;
    }

    public Schedule getThisSchedule() {
        return thisSchedule;
    }

    public void setThisSchedule(Schedule thisSchedule) {
        this.thisSchedule = thisSchedule;
    }


}
