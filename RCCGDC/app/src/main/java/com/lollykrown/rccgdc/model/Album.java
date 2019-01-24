package com.lollykrown.rccgdc.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Album implements Parcelable {

    private String mUrl;
    private String mTitle;
    private int mTotal;

    public Album(String url, String title, int total) {
        mUrl = url;
        mTitle = title;
        mTotal = total;
    }

    protected Album(Parcel in) {
        mUrl = in.readString();
        mTitle = in.readString();
        mTotal = in.readInt();
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
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

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int total) {
        mTotal = total;
    }

    public static  Album[] getAlbums() {

        return new Album[]{
                new Album("https://firebasestorage.googleapis.com/v0/b/rccgdc-app.appspot.com/o/images%2Fpics%2Fworkers_hangout%2Fone.jpg?alt=media&token=cec8290d-7d79-4b04-a32f-e6661f21a04b", "Workers Hangout", 26),
                new Album("https://firebasestorage.googleapis.com/v0/b/rccgdc-app.appspot.com/o/images%2Fpics%2Fdinner%2Fold_school.jpg?alt=media&token=16e3fa2d-0199-47eb-af22-732cbc799b8d", "Old School Dinner", 14),
                new Album("https://firebasestorage.googleapis.com/v0/b/rccgdc-app.appspot.com/o/images%2Fpics%2Fdinner%2Fold_school.jpg?alt=media&token=16e3fa2d-0199-47eb-af22-732cbc799b8d", "July Annointing Service", 32),
                new Album("https://firebasestorage.googleapis.com/v0/b/rccgdc-app.appspot.com/o/images%2Fpics%2Fdinner%2Fold_school.jpg?alt=media&token=16e3fa2d-0199-47eb-af22-732cbc799b8d", "Career Service", 54),
                new Album("https://firebasestorage.googleapis.com/v0/b/rccgdc-app.appspot.com/o/images%2Fpics%2Fdinner%2Fold_school.jpg?alt=media&token=16e3fa2d-0199-47eb-af22-732cbc799b8d", "Workers Hangout", 77),
                new Album("https://firebasestorage.googleapis.com/v0/b/rccgdc-app.appspot.com/o/images%2Fpics%2Fdinner%2Fold_school.jpg?alt=media&token=16e3fa2d-0199-47eb-af22-732cbc799b8dz2019", "Sports Weekend", 59),
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
        parcel.writeInt(mTotal);
    }
}