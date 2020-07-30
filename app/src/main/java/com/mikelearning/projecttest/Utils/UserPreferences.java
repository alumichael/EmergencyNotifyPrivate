package com.mikelearning.projecttest.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.mikelearning.projecttest.Constant;

public class UserPreferences {
    private SharedPreferences sharedPreferences;
    private Editor editor;
    private Context _context;

    @SuppressLint({"CommitPrefEdits"})
    public UserPreferences(Context _context) {
        this._context = _context;
        sharedPreferences = _context.getSharedPreferences(Constant.USER_PREF, Constant.PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

   

    public void setUserLogged(boolean usLg) {
        editor.putBoolean(Constant.IS_USER_LOGGED, usLg);
        editor.commit();
    }

    public boolean isUserLogged() {
        return sharedPreferences.getBoolean(Constant.IS_USER_LOGGED, false);
    }
    

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(Constant.IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return sharedPreferences.getBoolean(Constant.IS_FIRST_TIME_LAUNCH, true);
    }

    public void setSentSuccess(boolean isSentSuccess) {
        editor.putBoolean(Constant.IS_SENT_SUCCESS, isSentSuccess);
        editor.commit();
    }

    public boolean isSentSuccess() {
        return sharedPreferences.getBoolean(Constant.IS_SENT_SUCCESS, false);
    }

    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }


    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }
    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }


    public void setUserType(String usertype) {
        editor.putString("usertype", usertype);
        editor.apply();
    }

    public String getUserType() {
        return sharedPreferences.getString("usertype", "");
    }





    public void setFirstName(String firstName) {
        editor.putString("firstName", firstName);
        editor.apply();
    }

    public String getFirstname() {
        return sharedPreferences.getString("firstName", "");
    }

    public void setLastName(String lastname) {
        editor.putString("lastname", lastname);
        editor.apply();
    }

    public String getLastname() {
        return sharedPreferences.getString("lastname", "");
    }

    public void setPersonalNum(String personalNum) {
        editor.putString("personalNum", personalNum);
        editor.apply();
    }

    public String getPersonalNum() {
        return sharedPreferences.getString("personalNum", "");
    }

    public void setNextKinPhoneNum(String nextKinPhoneNum) {
        editor.putString("nextKinPhoneNum", nextKinPhoneNum);
        editor.apply();
    }

    public String getNextKinPhoneNum() {
        return sharedPreferences.getString("nextKinPhoneNum", "");
    }


}
