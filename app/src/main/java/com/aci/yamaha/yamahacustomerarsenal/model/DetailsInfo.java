package com.aci.yamaha.yamahacustomerarsenal.model;

/**
 * Created by aburasel on 10/29/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailsInfo {

    @SerializedName("MasterCode")
    @Expose
    private String masterCode;
    @SerializedName("ProductCode")
    @Expose
    private String productCode;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("ChassisNo")
    @Expose
    private String chassisNo;
    @SerializedName("EngineNo")
    @Expose
    private String engineNo;
    @SerializedName("Color")
    @Expose
    private String color;

    public String getMasterCode() {
        return masterCode;
    }

    public void setMasterCode(String masterCode) {
        this.masterCode = masterCode;
    }

    public DetailsInfo withMasterCode(String masterCode) {
        this.masterCode = masterCode;
        return this;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public DetailsInfo withProductCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public DetailsInfo withProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public String getChassisNo() {
        return chassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
    }

    public DetailsInfo withChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
        return this;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public DetailsInfo withEngineNo(String engineNo) {
        this.engineNo = engineNo;
        return this;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public DetailsInfo withColor(String color) {
        this.color = color;
        return this;
    }

}