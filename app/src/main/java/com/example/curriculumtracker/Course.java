package com.example.curriculumtracker;

import java.io.Serializable;

public class Course implements Serializable {

    private long mId;
    private String mTitle;
    private String mType;
    private String mStart;
    private String mEnd;
    private String mStatus;
    private String mNote;
    private String mMentorName;
    private String mMentorPhone;
    private String mMentorEmail;

    public Course(long mId, String mTitle, String mType, String mStart, String mEnd, String mStatus, String mNote, String mMentorName, String mMentorPhone, String mMentorEmail) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mType = mType;
        this.mStart = mStart;
        this.mEnd = mEnd;
        this.mStatus = mStatus;
        this.mNote = mNote;
        this.mMentorName = mMentorName;
        this.mMentorPhone = mMentorPhone;
        this.mMentorEmail = mMentorEmail;
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

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public String getStart() {
        return mStart;
    }

    public void setStart(String mStart) {
        this.mStart = mStart;
    }

    public String getEnd() {
        return mEnd;
    }

    public void setEnd(String mEnd) {
        this.mEnd = mEnd;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getMentorName() {
        return mMentorName;
    }

    public void setMentorName(String mMentorName) {
        this.mMentorName = mMentorName;
    }

    public String getMentorPhone() {
        return mMentorPhone;
    }

    public void setMentorPhone(String mMentorPhone) {
        this.mMentorPhone = mMentorPhone;
    }

    public String getMentorEmail() {
        return mMentorEmail;
    }

    public void setMentorEmail(String mMentorEmail) {
        this.mMentorEmail = mMentorEmail;
    }

    @Override
    public String toString() {
        return "Course{" +
                "mId=" + mId +
                ", mTitle='" + mTitle + '\'' +
                ", mType='" + mType + '\'' +
                ", mStart='" + mStart + '\'' +
                ", mEnd='" + mEnd + '\'' +
                ", mStatus='" + mStatus + '\'' +
                ", mMentorName='" + mMentorName + '\'' +
                ", mMentorPhone='" + mMentorPhone + '\'' +
                ", mMentorEmail='" + mMentorEmail + '\'' +
                '}';
    }
}
