package com.zyght.trayectoseguro.session;


import android.content.Context;
import android.content.SharedPreferences;

import com.zyght.trayectoseguro.entity.User;


public class Session {



    private static final String PREFS_NAME = "Session";
    private static final String TOKEN_NAME = "token";
    private static Session ourInstance = new Session();

    private User user;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static Session getInstance() {
        return ourInstance;
    }

    private Session() {
    }

    public String accessToken = "";

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken, Context context) {
        this.accessToken = accessToken;
        if(context != null)
            save(context);
    }

    public void restore(Context context){
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        accessToken  = settings.getString(TOKEN_NAME, "");
    }

    private void save(Context context){
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(TOKEN_NAME, accessToken);
        editor.commit();
    }


}
