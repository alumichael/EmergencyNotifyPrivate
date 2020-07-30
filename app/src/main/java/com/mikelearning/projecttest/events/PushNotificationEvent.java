package com.mikelearning.projecttest.events;

public class PushNotificationEvent {
    private String email;
    private String fcmToken;
    private String firstname;
    private String lastname;
    private String next_of_kin_num;
    private String message;
    private String title;


    public PushNotificationEvent(String firstname, String lastname, String next_of_kin_num, String title, String message, String email, String fcmToken) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.next_of_kin_num = next_of_kin_num;
        this.title = title;
        this.message = message;
        this.email = email;
        this.fcmToken = fcmToken;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }



    public String getNext_of_kin_num() {
        return next_of_kin_num;
    }

    public void setNext_of_kin_num(String next_of_kin_num) {
        this.next_of_kin_num = next_of_kin_num;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getFcmToken() {
        return this.fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
