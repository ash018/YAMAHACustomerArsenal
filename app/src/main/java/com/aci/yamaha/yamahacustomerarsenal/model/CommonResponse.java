package com.aci.yamaha.yamahacustomerarsenal.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aburasel on 10/18/2017.
 */

public class CommonResponse {

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public CommonResponse withSuccess(Integer success) {
        this.success = success;
        return this;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CommonResponse withMessage(String message) {
        this.message = message;
        return this;
    }

}
