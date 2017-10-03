package com.dp.uheadmaster.utilities;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by a.akl on 9/29/2016.
 */
public class SharedPrefManager {

    Context context;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    public SharedPrefManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(ConfigurationFile.ShardPref.SHARD_PREF_NAME, context.MODE_PRIVATE);
        editor = context.getSharedPreferences(ConfigurationFile.ShardPref.SHARD_PREF_NAME, context.MODE_PRIVATE).edit();


    }


    public void addStringToSharedPrederances(String title, String value) {
        try {
            editor.putString(title, value);
            editor.commit();

        } catch (Exception e) {
            System.out.println("Error /addStringToSharedPrederances / : "+e.getMessage());
        }
    }

    public String getStringFromSharedPrederances(String title) {
        String value = "";
        try {
            value = prefs.getString(title, "");

        } catch (Exception e) {
            System.out.println("Error /getStringFromSharedPrederances / : "+e.getMessage());
        }
        return value;
    }

    public void addIntegerToSharedPrederances(String title, int value) {
        try {
            editor.putInt(title, value);
            editor.commit();

        } catch (Exception e) {
            System.out.println("Error /addIntegerToSharedPrederances / : "+e.getMessage());
        }
    }

    public int getIntegerFromSharedPrederances(String title) {
        int value = 0;
        try {
            value = prefs.getInt(title, 0);

        } catch (Exception e) {
            System.out.println("Error /getIntegerFromSharedPrederances / : "+e.getMessage());
        }
        return value;


    }

    public void addBooleanToSharedPrederances(String title, Boolean value) {
        try {
            editor.putBoolean(title, value);
            editor.commit();

        } catch (Exception e) {
            System.out.println("Error /addBooleanToSharedPrederances / : "+e.getMessage());
        }
    }

    public Boolean getBooleanFromSharedPrederances(String title) {
        Boolean value = false;
        try {
            value = prefs.getBoolean(title, false);

        } catch (Exception e) {
            System.out.println("Error /getBooleanFromSharedPrederances / : "+e.getMessage());
        }
        return value;
    }

    public void clearToken()
    {
        try {
            editor.clear();
            editor.apply();

        } catch (Exception e) {
            System.out.println("Error /addBooleanToSharedPrederances / : "+e.getMessage());
        }
    }

}
