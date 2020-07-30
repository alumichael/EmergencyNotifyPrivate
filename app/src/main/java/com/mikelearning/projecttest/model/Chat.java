package com.mikelearning.projecttest.model;

public class Chat {
    public String firstname;
    public String lastname;
    public String personal_contact;
    public String nextofkin_phone;
    public double latitude,longitude;
    public String date;
    public String message;
    public String receiver_type;
    public String senderEmail;
    public String timestamp;


    public Chat(){

    }

    public Chat(String firstname, String lastname, String personal_contact, String nextofkin_phone, double latitude,double longitude, String date, String message, String receiver_type, String senderEmail, String timestamp) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.personal_contact = personal_contact;
        this.nextofkin_phone = nextofkin_phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.message = message;
        this.receiver_type = receiver_type;
        this.senderEmail = senderEmail;
        this.timestamp = timestamp;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPersonal_contact() {
        return personal_contact;
    }

    public String getNextofkin_phone() {
        return nextofkin_phone;
    }


    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    public String getReceiver_type() {
        return receiver_type;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getTimestamp() {
        return timestamp;
    }




}
