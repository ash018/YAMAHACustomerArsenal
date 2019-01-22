package com.aci.yamaha.yamahacustomerarsenal.model;

/**
 * Created by aburasel on 10/24/2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceStatResponse {

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("New")
    @Expose
    private Integer _new;
    @SerializedName("Done")
    @Expose
    private Integer done;
    @SerializedName("Total")
    @Expose
    private Integer total;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public ServiceStatResponse withSuccess(Integer success) {
        this.success = success;
        return this;
    }

    public Integer getNew() {
        return _new;
    }

    public void setNew(Integer _new) {
        this._new = _new;
    }

    public ServiceStatResponse withNew(Integer _new) {
        this._new = _new;
        return this;
    }

    public Integer getDone() {
        return done;
    }

    public void setDone(Integer done) {
        this.done = done;
    }

    public ServiceStatResponse withDone(Integer done) {
        this.done = done;
        return this;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public ServiceStatResponse withTotal(Integer total) {
        this.total = total;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ServiceStatResponse withMessage(String message) {
        this.message = message;
        return this;
    }

}