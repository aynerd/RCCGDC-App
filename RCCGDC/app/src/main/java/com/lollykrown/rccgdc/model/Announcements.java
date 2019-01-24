package com.lollykrown.rccgdc.model;

public class Announcements {

    // Image resource ID for the event
    private String url;

    public Announcements(String url) {
        this.url = url;
    }

    public Announcements(){
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
