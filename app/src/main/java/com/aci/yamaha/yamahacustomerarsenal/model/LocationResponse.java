package com.aci.yamaha.yamahacustomerarsenal.model;

/**
 * Created by aburasel on 11/20/2017.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationResponse {

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("location_info")
    @Expose
    private List<LocationInfo> locationInfo = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public LocationResponse withSuccess(Integer success) {
        this.success = success;
        return this;
    }

    public List<LocationInfo> getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(List<LocationInfo> locationInfo) {
        this.locationInfo = locationInfo;
    }

    public LocationResponse withLocationInfo(List<LocationInfo> locationInfo) {
        this.locationInfo = locationInfo;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocationResponse withMessage(String message) {
        this.message = message;
        return this;
    }

}