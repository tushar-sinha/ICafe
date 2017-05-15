package com.example.rshikkal.icafe.Models;

/**
 * Created by rshikkal on 5/13/2017.
 */

public class User {

    String username,useremail, mobile,cube;

    public User(String username, String useremail, String mobile, String cube) {
        this.username = username;
        this.useremail = useremail;
        this.mobile = mobile;
        this.cube = cube;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCube() {
        return cube;
    }

    public void setCube(String cube) {
        this.cube = cube;
    }

}
