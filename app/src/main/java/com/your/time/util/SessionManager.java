package com.your.time.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.your.time.activity.LoginActivity;
import com.your.time.bean.User;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;
     
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
     
    // Shared pref mode
    int PRIVATE_MODE = 0;
     
    // Sharedpref file name
    private static final String PREF_NAME = "YourTimePref";
     
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
     
    // name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
     
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // User login id (make variable public to access from outside)
    public static final String KEY_USERNAME = "username";

    // User mobile (make variable public to access from outside)
    public static final String KEY_MOBILE = "mobile";

    // Is User an ISP (make variable public to access from outside)
    public static final String KEY_IS_ISP = "isISP";

    // If User is an ISP (make variable public to access from outside)
    public static final String KEY_ISP_ID = "ISP_ID";
    private String KEY_ISP_TYPE = "ISP_TYPE";
    private String KEY_ROLE = "ROLE";

    public SessionManager(Context context){
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(Context context,String name, String email,String username, String mobile,boolean isISP,String ispId,String role){

        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
         
        // Storing name in pref
        editor.putString(KEY_NAME, name);
         
        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // Storing username in pref
        editor.putString(KEY_USERNAME,username);

        // Storing mobile in pref
        editor.putString(KEY_MOBILE,mobile);

        // Storing "is user an ISP" in pref
        editor.putBoolean(KEY_IS_ISP,isISP);

        // Storing ISP ID in pref
        editor.putString(KEY_ISP_ID,ispId);

        // Storing User Role in pref
        editor.putString(KEY_ROLE,role);

        // commit changes
        editor.commit();
    }   
     
    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(Context _context){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             
            // Staring Login Activity
            _context.startActivity(i);
        }
         
    }
     
    public String getUsername(){
        return pref.getString(KEY_USERNAME,null);
    }
     
    /**
     * Get stored session data
     * */
    public User getUserDetails(){
        User user = new User();
        user.setFirstname(pref.getString(KEY_NAME, null));
        user.setUsername(pref.getString(KEY_USERNAME,null));
        user.setServiceProvider(pref.getBoolean(KEY_IS_ISP,false));
        user.setServiceProviderId(pref.getString(KEY_ISP_ID,null));
        user.setServiceProviderTye(pref.getString(KEY_ISP_TYPE,null));
        user.setPhonenumber(pref.getString(KEY_MOBILE,null));
        user.setEmail(pref.getString(KEY_EMAIL,null));
        user.setRole(pref.getString(KEY_ROLE,null));
        return user;
    }
     
    /**
     * Clear session details
     * */
    public void logoutUser(Context _context){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
         
        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
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
        return pref.getBoolean(IS_LOGIN, false);
    }
}