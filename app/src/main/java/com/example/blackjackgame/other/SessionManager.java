package com.example.blackjackgame.other;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    //variables
    SharedPreferences usersSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_FULLNAME = "fullName";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_USERNAME = "username";

    public SessionManager(Context _context) {
        this.context = _context;
        this.usersSession = context.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        this.editor = usersSession.edit();
    }


    public void createLoginSession(String fullName, String username, String password, String email) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_FULLNAME, fullName);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USERNAME, username);

        editor.commit();

    }

    public HashMap<String, String> getUserDetailFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();
        userData.put(KEY_USERNAME, usersSession.getString(KEY_USERNAME, null));
        userData.put(KEY_FULLNAME, usersSession.getString(KEY_FULLNAME, null));
        userData.put(KEY_PASSWORD, usersSession.getString(KEY_PASSWORD, null));
        userData.put(KEY_USER_EMAIL, usersSession.getString(KEY_USER_EMAIL, null));

        return userData;

    }

    public boolean checkLogin() {

        if (usersSession.getBoolean(IS_LOGIN, false)) {
            return true;
        } else {
            return false;
        }
    }


    public void logoutUserFromSession() {
        editor.clear();
        editor.commit();
    }

}

