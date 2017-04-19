package com.example.android.newsguardian;

/**
 * Created by Nanda on 16/11/16.
 */

public class Article {

    private String mTitle;
    private String mSection;
    private String mDate;
    private String mUrl;

    public Article(String mTitle, String mSection, String mDate, String mUrl) {
        this.mTitle = mTitle;
        this.mSection = mSection;
        this.mDate = mDate;
        this.mUrl = mUrl;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmSection() {
        return mSection;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmUrl() {
        return mUrl;
    }
}

