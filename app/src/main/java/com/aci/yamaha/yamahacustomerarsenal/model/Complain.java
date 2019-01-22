package com.aci.yamaha.yamahacustomerarsenal.model;

/**
 * Created by aburasel on 11/5/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Complain {

    @SerializedName("ComplainID")
    @Expose
    private String complainID;
    @SerializedName("Category")
    @Expose
    private String category;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("EntryDate")
    @Expose
    private String entryDate;

    public String getComplainID() {
        return complainID;
    }

    public void setComplainID(String complainID) {
        this.complainID = complainID;
    }

    public Complain withComplainID(String complainID) {
        this.complainID = complainID;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Complain withCategory(String category) {
        this.category = category;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Complain withMessage(String message) {
        this.message = message;
        return this;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Complain withUserID(String userID) {
        this.userID = userID;
        return this;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public Complain withEntryDate(String entryDate) {
        this.entryDate = entryDate;
        return this;
    }

}
