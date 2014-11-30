package com.dualion.power_strip.power_strip.model;


public class User {

    private String username;
    private String password;

    public User(){
        username = "";
        password = "";
    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUsernameValid(){
        if (username.isEmpty() || username.length() < 3)
            return false;
        return true;
    }

    public boolean isPasswordValid() {
        return true;
    }

}
