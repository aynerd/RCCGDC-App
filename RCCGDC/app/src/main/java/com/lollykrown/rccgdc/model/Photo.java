package com.lollykrown.rccgdc.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {

    private String mUrl;
    private String mTitle;

    public Photo(String url, String title) {
        mUrl = url;
        mTitle = title;
    }

    protected Photo(Parcel in) {
        mUrl = in.readString();
        mTitle = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public static  Photo[] getSpacePhotos() {

        return new Photo[]{
                new Photo("https://i.imgur.com/mgGINEa.jpg", "Workers Hangout"),
                new Photo("https://firebasestorage.googleapis.com/v0/b/rccgdc-app.appspot.com/o/images%2Fpics%2Fdinner%2Fold_school.jpg?alt=media&token=16e3fa2d-0199-47eb-af22-732cbc799b8d", "Workers Hang"),
                new Photo("https://i.imgur.com/KmuHbl4.jpg", "Workers Hang"),
                new Photo("https://i.imgur.com/E4DR8Ww.jpg", "Workers Han"),
                new Photo("https://i.imgur.com/yI30eZd.jpg", "Workers H"),
                new Photo("https://i.imgur.com/stfATQi.jpg", "Workers"),
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mUrl);
        parcel.writeString(mTitle);
    }
}