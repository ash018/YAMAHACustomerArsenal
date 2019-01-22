package com.aci.yamaha.yamahacustomerarsenal.model;

/**
 * Created by aburasel on 10/11/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpgradeResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("latestVersion")
    @Expose
    private String latestVersion;
    @SerializedName("appURI")
    @Expose
    private String appURI;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public UpgradeResponse withSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public UpgradeResponse withLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
        return this;
    }

    public String getAppURI() {
        return appURI;
    }

    public void setAppURI(String appURI) {
        this.appURI = appURI;
    }

    public UpgradeResponse withAppURI(String appURI) {
        this.appURI = appURI;
        return this;
    }

}