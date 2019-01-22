package com.aci.yamaha.yamahacustomerarsenal.model;

/**
 * Created by aburasel on 9/24/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponse {

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("locationid")
    @Expose
    private String locationid;
    @SerializedName("contactno")
    @Expose
    private String contactno;
    @SerializedName("usertypeid")
    @Expose
    private String usertypeid;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("dealer_info")
    @Expose
    private List<DealerInfo> dealerInfo = null;
    @SerializedName("location_info")
    @Expose
    private List<LocationInfo> locationInfo = null;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public LoginResponse withSuccess(Integer success) {
        this.success = success;
        return this;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public LoginResponse withUserid(String userid) {
        this.userid = userid;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LoginResponse withUsername(String username) {
        this.username = username;
        return this;
    }

    public String getLocationid() {
        return locationid;
    }

    public void setLocationid(String locationid) {
        this.locationid = locationid;
    }

    public LoginResponse withLocationid(String locationid) {
        this.locationid = locationid;
        return this;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public LoginResponse withContactno(String contactno) {
        this.contactno = contactno;
        return this;
    }

    public String getUsertypeid() {
        return usertypeid;
    }

    public void setUsertypeid(String usertypeid) {
        this.usertypeid = usertypeid;
    }

    public LoginResponse withUsertypeid(String usertypeid) {
        this.usertypeid = usertypeid;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoginResponse withMessage(String message) {
        this.message = message;
        return this;
    }

    public List<DealerInfo> getDealerInfo() {
        return dealerInfo;
    }

    public void setDealerInfo(List<DealerInfo> dealerInfo) {
        this.dealerInfo = dealerInfo;
    }

    public LoginResponse withDealerInfo(List<DealerInfo> dealerInfo) {
        this.dealerInfo = dealerInfo;
        return this;
    }

    public List<LocationInfo> getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(List<LocationInfo> locationInfo) {
        this.locationInfo = locationInfo;
    }

    public LoginResponse withLocationInfo(List<LocationInfo> locationInfo) {
        this.locationInfo = locationInfo;
        return this;
    }

}
