package com.example.blackjackgame.model;

public class UserHelper {

    String fullname;
    String username;

    String password;


    public UserHelper(String fullname, String username,String password) {
        this.fullname = fullname;
        this.username = username;

        this.password = password;

    }

    public UserHelper() {

    }

    public String getFullName() {
        return fullname;
    }

    public void setFullName(String fullname) {
        this.fullname = fullname;
    }
//    public String getHistory() {
//        return history;
//    }

//    public void setHistory(String history) {
//        this.history = history;
//    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

//    public String getUserEmail() {
//        return useremail;
//    }

//    public void setUserEmail(String useremail) {
//        this.useremail = useremail;
//    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
