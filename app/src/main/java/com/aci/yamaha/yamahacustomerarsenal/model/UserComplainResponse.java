package com.aci.yamaha.yamahacustomerarsenal.model;

/**
 * Created by aburasel on 11/5/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserComplainResponse {

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("complain")
    @Expose
    private List<Complain> complain = null;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public UserComplainResponse withSuccess(Integer success) {
        this.success = success;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserComplainResponse withMessage(String message) {
        this.message = message;
        return this;
    }

    public List<Complain> getComplain() {
        return complain;
    }

    public void setComplain(List<Complain> complain) {
        this.complain = complain;
    }

    public UserComplainResponse withComplain(List<Complain> complain) {
        this.complain = complain;
        return this;
    }

}
