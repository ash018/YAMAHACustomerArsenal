package com.aci.yamaha.yamahacustomerarsenal.model;

/**
 * Created by aburasel on 10/29/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChassisValidationResponse {

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("details_info")
    @Expose
    private List<DetailsInfo> detailsInfo = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public ChassisValidationResponse withSuccess(Integer success) {
        this.success = success;
        return this;
    }

    public List<DetailsInfo> getDetailsInfo() {
        return detailsInfo;
    }

    public void setDetailsInfo(List<DetailsInfo> detailsInfo) {
        this.detailsInfo = detailsInfo;
    }

    public ChassisValidationResponse withDetailsInfo(List<DetailsInfo> detailsInfo) {
        this.detailsInfo = detailsInfo;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChassisValidationResponse withMessage(String message) {
        this.message = message;
        return this;
    }

}