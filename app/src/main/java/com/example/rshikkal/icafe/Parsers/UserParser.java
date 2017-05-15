package com.example.rshikkal.icafe.Parsers;

import com.example.rshikkal.icafe.Models.User;

import org.json.JSONObject;

/**
 * Created by rshikkal on 5/13/2017.
 */

public class UserParser {

    String username = "", useremail = "", mobile = "", cube = "";

    public User parse(JSONObject object){
        User user = null;
        try {
            if (object.has("username")) {
                username = object.getString("username");
            }
            if (object.has("email")) {
                useremail = object.getString("email");
            }
            if (object.has("mobile")) {
                mobile = object.getString("mobile");
            }
            if (object.has("cube")) {
                cube = object.getString("cube");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        user = new User(username, useremail, mobile, cube);
        return  user;
    }
}
