package com.example.pokelearn;

public class Users {
    String userId;
    String userName;
    String userEmail;
    String userPassword;
    String userImgUrl;
//    String deviceToken;
//    Long online;

    public Users(){

    }

    public Users(String userId, String userName, String userEmail, String userPassword, String userImgUrl) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userImgUrl = userImgUrl;
//        this.deviceToken = deviceToken;
//        this.online = online;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserImgUrl() { return userImgUrl;}

//    public String getDeviceToken(){return deviceToken;}
//
//    public Long getOnline(){return online;}
}
