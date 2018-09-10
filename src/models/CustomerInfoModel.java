package com.yourapp.name.models;

public class CustomerInfoModel {
    public int usertype;
    public String email;
    public String password;

    public CustomerInfoModel(int usertype, String email, String password){
        this.usertype  = usertype;
        this.email = email;
        this.password = password;
    }
}
