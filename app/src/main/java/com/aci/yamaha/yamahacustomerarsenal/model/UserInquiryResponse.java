package com.aci.yamaha.yamahacustomerarsenal.model;

/**
 * Created by aburasel on 11/5/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserInquiryResponse {

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("inquiry")
    @Expose
    private List<Inquiry> inquiry = null;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public UserInquiryResponse withSuccess(Integer success) {
        this.success = success;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserInquiryResponse withMessage(String message) {
        this.message = message;
        return this;
    }

    public List<Inquiry> getInquiry() {
        return inquiry;
    }

    public void setInquiry(List<Inquiry> inquiry) {
        this.inquiry = inquiry;
    }

    public UserInquiryResponse withInquiry(List<Inquiry> inquiry) {
        this.inquiry = inquiry;
        return this;
    }

}
