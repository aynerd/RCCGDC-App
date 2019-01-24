package com.lollykrown.rccgdc.model;

public class Events {

    private String url;
    private String eventTitle;
    private String eventDate;
    private String eventTime;

    public Events(String eventDate, String eventTime, String eventTitle, String url) {
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventTitle = eventTitle;
        this.url = url;
    }

    public Events(){
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }



}
