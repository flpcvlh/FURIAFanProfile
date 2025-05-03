package com.furiagg.fanprofile.model;

import java.util.Date;

public class FanEvent {
    private String name;
    private Date date;
    private String location;
    private String eventType;
    private boolean attended;
    private String details;

    public FanEvent() {
    }

    public FanEvent(String name, Date date, String location, String eventType, boolean attended) {
        this.name = name;
        this.date = date;
        this.location = location;
        this.eventType = eventType;
        this.attended = attended;
    }

    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}