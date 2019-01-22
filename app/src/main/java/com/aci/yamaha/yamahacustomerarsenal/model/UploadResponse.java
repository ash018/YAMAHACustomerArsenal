package com.aci.yamaha.yamahacustomerarsenal.model;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class UploadResponse {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("value")
    @Expose
    private String value;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public UploadResponse withResult(String result) {
        this.result = result;
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public UploadResponse withValue(String value) {
        this.value = value;
        return this;
    }

}
