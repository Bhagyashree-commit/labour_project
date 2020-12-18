package com.example.labourmangement.DatabaseHelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.labourmangement.Admin.UpdateMain;

import java.util.HashMap;

public class SessionForArch {
    // LogCat tag
    private static String TAG = com.example.labourmangement.DatabaseHelper.SessionForArch.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "LabourManagementArchitecht";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public static final String KEY_EMAIL = "email";

    public static final String KEY_NAME = "name";

    public static final String KEY_ROLE = "role";

    public static final String KEY_REFNAME= "ref_name";

    public static final String KEY_REFCODE= "ref_code";





    public SessionForArch(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createUserLoginSession( String email,String name,String role,String ref_name, String ref_code){
        // Storing login value as TRUE
        // editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_ROLE,role);
        editor.putString(KEY_REFCODE,ref_code);
        editor.putString(KEY_REFNAME,ref_name);

        // commit changes
        editor.commit();
    }


    public void setLogin(boolean isLoggedIn,String email) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        editor.putString(KEY_EMAIL,email);
        // editor.putString(KEY_ACCESS,access_token);

        // commit changes
        editor.commit();

        // Log.d(TAG, "User login session modified!");
    }



    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, UpdateMain.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    //=====
    public SessionForArch commit() {
        editor.commit();
        return this;
    }

    //=====
    public SessionForArch set(String key, String value) {

        editor.putString(key, value);
        return this;
    }

    //=====
    public String get(String key) {
        return pref.getString(key, "");
    }


    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();


        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_ROLE,pref.getString(KEY_ROLE,null));
        user.put(KEY_REFCODE,pref.getString(KEY_REFCODE,null));
        user.put(KEY_REFNAME,pref.getString(KEY_REFNAME,null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, UpdateMain.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }




}
