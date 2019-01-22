package com.aci.yamaha.yamahacustomerarsenal.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {
    private static String E_PREF = "";
    private SharedPreferences spref;
    private Editor editor;
    private String IMEI;

    public AppPreferences(Context context) {
        E_PREF = context.getPackageName() + ".YamahaCustomerArsenal";

        spref = context.getSharedPreferences(
                E_PREF, Context.MODE_PRIVATE);
    }

    public boolean getLoggedIn() {
        return spref.getBoolean("LOGGED_IN", false);
    }

    public void setLoggedIn(boolean logedin) {
        editor = spref.edit();
        editor.putBoolean("LOGGED_IN", logedin);
        editor.commit();
    }

    public String getUserId() {
        return spref.getString("USERID", "");
    }

    public void setUserId(String uid) {
        editor = spref.edit();
        editor.putString("USERID", uid);
        editor.commit();
    }

    public String getImei() {
        return spref.getString("IMEI", "");
    }

    public void setImei(String imei) {
        editor = spref.edit();
        editor.putString("IMEI", imei);
        editor.commit();
    }

    public String getLatitude() {
        return spref.getString("LATITUDE", "0");
    }

    public void setLatitude(String latitude) {
        editor = spref.edit();
        editor.putString("LATITUDE", latitude);
        editor.commit();
    }

    public String getLongitude() {
        return spref.getString("LONGITUDE", "0");
    }

    public void setLongitude(String longitude) {
        editor = spref.edit();
        editor.putString("LONGITUDE", longitude);
        editor.commit();
    }

    public String getAddress() {
        return spref.getString("ADDRESS", "");
    }

    public void setAddress(String address) {
        editor = spref.edit();
        editor.putString("ADDRESS", address);
        editor.commit();
    }

    public String getMobileNo() {
        return spref.getString("MOBILE_NO", "");
    }

    public void setMobileNo(String mobileNo) {
        editor = spref.edit();
        editor.putString("MOBILE_NO", mobileNo);
        editor.commit();
    }

    public boolean isBasicInfoSubmitted() {
        return spref.getBoolean("BASIC_INFO", false);
    }

    public void setBasicInfoSubmitted(boolean synced) {
        editor = spref.edit();
        editor.putBoolean("BASIC_INFO", synced);
        editor.commit();
    }

    public String getUsertName() {
        return spref.getString("USER_NAME", "");
    }

    public void setUsertName(String un) {
        editor = spref.edit();
        editor.putString("USER_NAME", un);
        editor.commit();
    }

    public String getLocationId() {
        return spref.getString("LOCATION_ID", "");
    }

    public void setLocationId(String un) {
        editor = spref.edit();
        editor.putString("LOCATION_ID", un);
        editor.commit();
    }

    public String getContactNo() {
        return spref.getString("CONTACT_NO", "");
    }

    public void setContactNo(String un) {
        editor = spref.edit();
        editor.putString("CONTACT_NO", un);
        editor.commit();
    }

    public String getUserTypeId() {
        return spref.getString("USER_TYPE_ID", "");
    }

    public void setUserTypeId(String un) {
        editor = spref.edit();
        editor.putString("USER_TYPE_ID", un);
        editor.commit();
    }

    public int getPendingServiceCount() {
        return spref.getInt("PENDING_SERVICE", 0);
    }

    public void setPendingServiceCount(int count) {
        editor = spref.edit();
        editor.putInt("PENDING_SERVICE", count);
        editor.commit();
    }

    public int getReceivedServiceCount() {
        return spref.getInt("RECEIVED_SERVICE", 0);
    }

    public void setReceivedServiceCount(int count) {
        editor = spref.edit();
        editor.putInt("RECEIVED_SERVICE", count);
        editor.commit();
    }

    public int getTotalServiceCount() {
        return spref.getInt("TOTAL_SERVICE", 0);
    }

    public void setTotalServiceCount(int count) {
        editor = spref.edit();
        editor.putInt("TOTAL_SERVICE", count);
        editor.commit();
    }

    public boolean isLocationDataSynced() {
        return spref.getBoolean("LOCATION_SYNCED", false);
    }

    public void setLocationDataSynced(boolean synced) {
        editor = spref.edit();
        editor.putBoolean("LOCATION_SYNCED", synced);
        editor.commit();
    }
}
