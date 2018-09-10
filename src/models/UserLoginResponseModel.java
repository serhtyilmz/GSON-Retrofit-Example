package com.yourapp.name.models;

public class UserLoginResponseModel {
    public int result;
    public String message;
    public int usertype;

    public UserLoginResponseModel(int result, String message, int usertype) {
        this.result = result;
        this.message = message;
        this.usertype = usertype;
    }
}
