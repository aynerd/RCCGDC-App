package com.lollykrown.rccgdc.model;

public class Bulletin {

    // String resource for event title
    private String mMonth;

    private String mBullImgUrl;

    public Bulletin(String bullImgUrl, String bullMonth) {
        mBullImgUrl = bullImgUrl;
        mMonth = bullMonth;
    }

    public String getBullImgUrl() {
        return mBullImgUrl;
    }

    public String getBullMonth() {
        return mMonth;
    }

}