package com.mikelearning.projecttest.model;

public class EmergencyOffice {


    public String office_email;
    public String firebaseToken;
    public String office_organization;
    public String state;
    public String office_password;
    public String office_phone_num;
    public String user_type;



    public EmergencyOffice(){

    }

    public EmergencyOffice(String user_type,String office_email, String office_organization, String state, String office_password, String office_phone_num,  String firebaseToken) {
        this.user_type = user_type;
        this.office_organization = office_organization;
        this.state = state;
        this.office_phone_num = office_phone_num;
        this.office_email = office_email;
        this.office_password = office_password;
        this.firebaseToken = firebaseToken;
    }


    public String getOffice_email() {
        return office_email;
    }

    public void setOffice_email(String office_email) {
        this.office_email = office_email;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public String getOffice_organization() {
        return office_organization;
    }

    public void setOffice_organization(String office_organization) {
        this.office_organization = office_organization;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOffice_password() {
        return office_password;
    }

    public void setOffice_password(String office_password) {
        this.office_password = office_password;
    }

    public String getOffice_phone_num() {
        return office_phone_num;
    }

    public void setOffice_phone_num(String office_phone_num) {
        this.office_phone_num = office_phone_num;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }


}
