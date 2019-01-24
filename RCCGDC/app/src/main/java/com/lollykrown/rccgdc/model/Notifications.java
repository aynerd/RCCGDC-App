package com.lollykrown.rccgdc.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "notifications")
public class Notifications {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private String titlee;
    private String datee;
    private String timee;
    private int isRead;

    @ColumnInfo(name = "image_url")
    private String url;


    public Notifications(@NonNull int id, String titlee, String datee, String timee, String url, int isRead ) {
        this.id = id;
        this.titlee = titlee;
        this.datee = datee;
        this.timee = timee;
        this.url = url;
        this.isRead = isRead;
    }

    @Ignore
    public Notifications(String titlee, String datee, String timee, String url, int isRead) {
        this.titlee = titlee;
        this.datee = datee;
        this.timee = timee;
        this.url = url;
        this.isRead = isRead;
    }

    @Ignore
    public Notifications() {
    }

    @NonNull
    public int getId() {
        return id;
    }
    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getTitlee() {
        return titlee;
    }

    public void setTitlee(String titlee) {
        this.titlee = titlee;
    }

    public String getDatee() {
        return datee;
    }

    public void setDatee(String datee) {
        this.datee = datee;
    }

    public String getTimee() {
        return timee;
    }

    public void setTimee(String timee) {
        this.timee = timee;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

}

