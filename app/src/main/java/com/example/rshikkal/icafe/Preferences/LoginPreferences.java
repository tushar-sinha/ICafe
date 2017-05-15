package com.example.rshikkal.icafe.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.StrictMode;

import com.example.rshikkal.icafe.Models.User;

import org.json.JSONException;

/**
 * Created by rshikkal on 5/13/2017.
 */

public class LoginPreferences {

    private static final String USERTABLE = "usertable";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String USERNAME = "username";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String MOBILE = "mobile";
    private static final String CUBE = "cube";

    SharedPreferences preferences;
    Context context;

    public LoginPreferences(Context context){
        preferences = context.getSharedPreferences(USERTABLE,Context.MODE_PRIVATE);
        this.context = context;
    }


    public boolean isLoggedIn(){
        return preferences.getBoolean(IS_LOGGED_IN, false);
    }

    public User getUser() {
        String username = preferences.getString(USERNAME, "");
        String email = preferences.getString(EMAIL,"");
        String mobile = preferences.getString(MOBILE,"");
        String cube = preferences.getString(CUBE,"");
        return new User(username,email,mobile,cube);
    }


    public void loginUser(User user) throws JSONException {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(USERNAME, user.getUsername());
        editor.putString(EMAIL, user.getUseremail());
        editor.putString(MOBILE, user.getMobile());
        editor.putString(CUBE, user.getCube());
        editor.apply();
    }

    public void logoutUser(){
        if(preferences.getBoolean(IS_LOGGED_IN, false)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            editor.putBoolean(IS_LOGGED_IN, false);
            editor.apply();
        }
    }

}
