package com.aci.yamaha.yamahacustomerarsenal.model;

/**
 * Created by aburasel on 10/10/2017.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationInfo {

    @SerializedName("LocationID")
    @Expose
    private String locationID;
    @SerializedName("LocationName")
    @Expose
    private String locationName;

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public LocationInfo(String locationID, String locationName) {
        this.locationID = locationID;
        this.locationName = locationName;
    }

    public LocationInfo withLocationID(String locationID) {
        this.locationID = locationID;
        return this;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public LocationInfo withLocationName(String locationName) {
        this.locationName = locationName;
        return this;
    }

    @Override
    public String toString() {
        return getLocationName();
    }
}