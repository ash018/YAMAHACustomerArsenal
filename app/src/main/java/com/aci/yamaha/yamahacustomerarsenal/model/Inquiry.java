package com.aci.yamaha.yamahacustomerarsenal.model;

/**
 * Created by aburasel on 11/5/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Inquiry {

    @SerializedName("InquiryID")
    @Expose
    private String inquiryID;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("EntryDate")
    @Expose
    private String entryDate;

    public String getInquiryID() {
        return inquiryID;
    }

    public void setInquiryID(String inquiryID) {
        this.inquiryID = inquiryID;
    }

    public Inquiry withInquiryID(String inquiryID) {
        this.inquiryID = inquiryID;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Inquiry withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Inquiry withMessage(String message) {
        this.message = message;
        return this;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Inquiry withUserID(String userID) {
        this.userID = userID;
        return this;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public Inquiry withEntryDate(String entryDate) {
        this.entryDate = entryDate;
        return this;
    }

}