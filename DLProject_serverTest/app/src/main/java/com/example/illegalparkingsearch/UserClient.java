package com.example.illegalparkingsearch;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.illegalparkingsearch.data.User;
import com.google.gson.Gson;

public class UserClient {

    private static final String USER_KEY = "user";

    public static User getUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE);
        return new Gson().fromJson(sharedPreferences.getString(USER_KEY, null), User.class);
    }

    public static void savelUser(Context context, String userText) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_KEY, userText);
        editor.commit();
    }

}
