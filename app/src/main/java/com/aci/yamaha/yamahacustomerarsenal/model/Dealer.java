package com.aci.yamaha.yamahacustomerarsenal.model;

/**
 * Created by aburasel on 10/19/2017.
 */

public class Dealer {
    int dealerId;
    String dealerName;
    String contactNumber;
    String showroomName;
    String dealerAddress;
    String dealerArea;
    String dealerHotLine;
    String dealerEmail;

    public Dealer(int dealerId, String dealerName, String contactNumber, String showroomName, String dealerAddress,
                  String dealerArea, String dealerHotLine, String dealerEmail) {
        this.dealerId = dealerId;
        this.dealerName = dealerName;
        this.contactNumber = contactNumber;
        this.showroomName = showroomName;
        this.dealerAddress = dealerAddress;
        this.dealerArea = dealerArea;
        this.dealerHotLine = dealerHotLine;
        this.dealerEmail = dealerEmail;
    }

    public int getDealerId() {
        return dealerId;
    }

    public void setDealerId(int dealerId) {
        this.dealerId = dealerId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getShowroomName() {
        return showroomName;
    }

    public void setShowroomName(String showroomName) {
        this.showroomName = showroomName;
    }

    public String getDealerAddress() {
        return dealerAddress;
    }

    public void setDealerAddress(String dealerAddress) {
        this.dealerAddress = dealerAddress;
    }

    public String getDealerArea() {
        return dealerArea;
    }

    public void setDealerArea(String dealerArea) {
        this.dealerArea = dealerArea;
    }

    public String getDealerHotLine() {
        return dealerHotLine;
    }

    public void setDealerHotLine(String dealerHotLine) {
        this.dealerHotLine = dealerHotLine;
    }

    public String getDealerEmail() {
        return dealerEmail;
    }

    public void setDealerEmail(String dealerEmail) {
        this.dealerEmail = dealerEmail;
    }

    @Override
    public String toString() {
        return getShowroomName() + " (" + getDealerAddress() + ")";
    }
}
