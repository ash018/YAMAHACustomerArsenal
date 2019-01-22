package com.aci.yamaha.yamahacustomerarsenal.model;

/**
 * Created by aburasel on 11/5/2017.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceResponse {

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("services")
    @Expose
    private List<Service> services = null;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public ServiceResponse withSuccess(Integer success) {
        this.success = success;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ServiceResponse withMessage(String message) {
        this.message = message;
        return this;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public ServiceResponse withServices(List<Service> services) {
        this.services = services;
        return this;
    }

}
