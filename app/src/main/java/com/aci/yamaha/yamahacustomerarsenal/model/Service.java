package com.aci.yamaha.yamahacustomerarsenal.model;

/**
 * Created by aburasel on 10/25/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Service {

    @SerializedName("TrackID")
    @Expose
    private String trackID;
    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("CategoryID")
    @Expose
    private String categoryID;
    @SerializedName("LocationID")
    @Expose
    private String locationID;
    @SerializedName("UserIDDealer")
    @Expose
    private String userIDDealer;
    @SerializedName("Model")
    @Expose
    private String model;
    @SerializedName("EngineNo")
    @Expose
    private String engineNo;
    @SerializedName("ChassisNo")
    @Expose
    private String chassisNo;
    @SerializedName("Subject")
    @Expose
    private String subject;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("EntryDate")
    @Expose
    private String entryDate;
    @SerializedName("EditDate")
    @Expose
    private Object editDate;
    @SerializedName("StatusID")
    @Expose
    private String statusID;
    @SerializedName("ReplyMessage")
    @Expose
    private String replyMessage;
    @SerializedName("Rating")
    @Expose
    private String rating;

    public String getTrackID() {
        return trackID;
    }

    public void setTrackID(String trackID) {
        this.trackID = trackID;
    }

    public Service withTrackID(String trackID) {
        this.trackID = trackID;
        return this;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Service withUserID(String userID) {
        this.userID = userID;
        return this;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public Service withCategoryID(String categoryID) {
        this.categoryID = categoryID;
        return this;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public Service withLocationID(String locationID) {
        this.locationID = locationID;
        return this;
    }

    public String getUserIDDealer() {
        return userIDDealer;
    }

    public void setUserIDDealer(String userIDDealer) {
        this.userIDDealer = userIDDealer;
    }

    public Service withUserIDDealer(String userIDDealer) {
        this.userIDDealer = userIDDealer;
        return this;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Service withModel(String model) {
        this.model = model;
        return this;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public Service withEngineNo(String engineNo) {
        this.engineNo = engineNo;
        return this;
    }

    public String getChassisNo() {
        return chassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
    }

    public Service withChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Service withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Service withMessage(String message) {
        this.message = message;
        return this;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public Service withEntryDate(String entryDate) {
        this.entryDate = entryDate;
        return this;
    }

    public Object getEditDate() {
        return editDate;
    }

    public void setEditDate(Object editDate) {
        this.editDate = editDate;
    }

    public Service withEditDate(Object editDate) {
        this.editDate = editDate;
        return this;
    }

    public String getStatusID() {
        return statusID;
    }

    public void setStatusID(String statusID) {
        this.statusID = statusID;
    }

    public Service withStatusID(String statusID) {
        this.statusID = statusID;
        return this;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }

    public Service withReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
        return this;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Service withRating(String rating) {
        this.rating = rating;
        return this;
    }

}