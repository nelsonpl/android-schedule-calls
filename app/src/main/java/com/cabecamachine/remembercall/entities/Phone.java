package com.cabecamachine.remembercall.entities;

public class Phone {

    private long id;
    private long contactId;
    private String phoneNumber;
    private String label;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public String getNumber() {
        return phoneNumber;
    }

    public void setNumber(String phone) {
        this.phoneNumber = phone;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
