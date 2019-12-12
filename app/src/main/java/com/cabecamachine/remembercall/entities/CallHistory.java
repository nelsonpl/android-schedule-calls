package com.cabecamachine.remembercall.entities;


public class CallHistory {
    private String ContactName;
    private String PhoneLabel;
    private String PhoneNumber;
    private long Date;

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getPhoneLabel() {
        return PhoneLabel;
    }

    public void setPhoneLabel(String phoneLabel) {
        PhoneLabel = phoneLabel;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public long getDate() {
        return Date;
    }

    public void setDate(long date) {
        Date = date;
    }
}
