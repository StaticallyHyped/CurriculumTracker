package com.example.curriculumtracker;

import java.io.Serializable;
import java.util.Arrays;

public class Assessment implements Serializable {

    private long mId;
    private String mTitle;
    private String mStart;
    private String mType;
    private String [] mNote;

    public Assessment(long mId, String mTitle, String mStart, String type) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mStart = mStart;
        this.mType = type;

    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getStart() {
        return mStart;
    }

    public void setStart(String mStart) {
        this.mStart = mStart;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }


    @Override
    public String toString() {
        return "Assessment{" +
                "mId=" + mId +
                ", mTitle='" + mTitle + '\'' +
                ", mStart='" + mStart + '\'' +
                ", mType='" + mType +
                '}';

    }
}
