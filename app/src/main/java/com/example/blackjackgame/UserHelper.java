package com.example.blackjackgame;

public class UserHelper {

    String fullname;
    String username;
    String useremail;
    String password;
    String history;

    public UserHelper(String fullname, String username, String useremail, String password,String history) {
        this.fullname = fullname;
        this.username = username;
        this.useremail = useremail;
        this.password = password;
        this.history = history;
    }

    public UserHelper() {

    }

    public String getFullName() {
        return fullname;
    }

    public void setFullName(String fullname) {
        this.fullname = fullname;
    }
    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return useremail;
    }

    public void setUserEmail(String useremail) {
        this.useremail = useremail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
