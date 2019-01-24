package com.lollykrown.rccgdc.model;

public class User {

    private String username;
    private String userEmail;
    private String ppUurl;
    private String mobileNo;

    public User(String username, String userEmail, String ppUurl, String mobileNo) {
        this.username = username;
        this.userEmail = userEmail;
        this.ppUurl = ppUurl;
        this.mobileNo = mobileNo;
    }

    public User(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPpUurl() {
        return ppUurl;
    }

    public void setPpUurl(String ppUurl) {
        this.ppUurl = ppUurl;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
