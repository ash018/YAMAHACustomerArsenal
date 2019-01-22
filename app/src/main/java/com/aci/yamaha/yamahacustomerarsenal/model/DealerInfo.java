package com.aci.yamaha.yamahacustomerarsenal.model;

/**
 * Created by aburasel on 10/24/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DealerInfo {

    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("LocationID")
    @Expose
    private String locationID;
    @SerializedName("LocationName")
    @Expose
    private String locationName;
    @SerializedName("ShowroomName")
    @Expose
    private String showroomName;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("ContactNo")
    @Expose
    private String contactNo;
    @SerializedName("HotLine")
    @Expose
    private String hotLine;
    @SerializedName("Email")
    @Expose
    private String email;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public DealerInfo withUserID(String userID) {
        this.userID = userID;
        return this;
    }

    public DealerInfo(String userID, String locationID, String showroomName, String address) {
        this.userID = userID;
        this.locationID = locationID;
        this.showroomName = showroomName;
        this.address = address;
    }

    public DealerInfo(String userID, String userName, String locationID, String locationName, String showroomName,
                      String address, String contactNo, String hotLine, String email) {
        this.userID = userID;
        this.userName = userName;
        this.locationID = locationID;
        this.locationName = locationName;
        this.showroomName = showroomName;
        this.address = address;
        this.contactNo = contactNo;
        this.hotLine = hotLine;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public DealerInfo withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public DealerInfo withLocationID(String locationID) {
        this.locationID = locationID;
        return this;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public DealerInfo withLocationName(String locationName) {
        this.locationName = locationName;
        return this;
    }

    public String getShowroomName() {
        return showroomName;
    }

    public void setShowroomName(String showroomName) {
        this.showroomName = showroomName;
    }

    public DealerInfo withShowroomName(String showroomName) {
        this.showroomName = showroomName;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public DealerInfo withAddress(String address) {
        this.address = address;
        return this;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public DealerInfo withContactNo(String contactNo) {
        this.contactNo = contactNo;
        return this;
    }

    public String getHotLine() {
        return hotLine;
    }

    public void setHotLine(String hotLine) {
        this.hotLine = hotLine;
    }

    public DealerInfo withHotLine(String hotLine) {
        this.hotLine = hotLine;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DealerInfo withEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public String toString() {
        return getAddress() + " (" + getShowroomName() + ")";
    }
}