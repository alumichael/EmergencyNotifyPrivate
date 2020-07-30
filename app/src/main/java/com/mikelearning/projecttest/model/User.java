package com.mikelearning.projecttest.model;

public class User {


    public String email;
    public String firebaseToken;
    public String firstname;
    public String lastname;
    public String address;
    public String password;
    public String phone_num;
    public String next_of_kin_phone_num;
    public String user_type;



    public User(){

    }

    public User(String user_type,String firstname, String lastname, String address, String phone_num, String email, String password, String next_of_kin_phone_num, String firebaseToken) {
        this.user_type = user_type;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.phone_num = phone_num;
        this.email = email;
        this.password = password;
        this.next_of_kin_phone_num = next_of_kin_phone_num;
        this.firebaseToken = firebaseToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getNext_of_kin_phone_num() {
        return next_of_kin_phone_num;
    }

    public void setNext_of_kin_phone_num(String next_of_kin_phone_num) {
        this.next_of_kin_phone_num = next_of_kin_phone_num;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }





}
