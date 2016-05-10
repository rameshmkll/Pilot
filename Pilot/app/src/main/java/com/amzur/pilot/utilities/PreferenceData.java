package com.amzur.pilot.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;

import com.facebook.login.LoginManager;

/**
 * Created by MRamesh on 09-05-2016.
 */
public class PreferenceData {
    private static SharedPreferences preferences;
    public static final String SHARED_PREF="credentials";
    private static SharedPreferences.Editor editor;
    public static final String PREF_LOGIN="login";

    /**
     * This method puts the login status as true after sign in and login status as false after sign out.
     * @param context context from which this method is called.
     * @param login_status true if  this method is called from signin and false if it called from signout.
     */
    public static void putLoginStatus(Context context, boolean login_status)
    {
        if(preferences==null)
            preferences=context.getSharedPreferences(SHARED_PREF, 0);
        editor=preferences.edit();
        editor.putBoolean(PREF_LOGIN, login_status);
        editor.apply();
    }

    /**
     * This method stores a value in the sharedPreferences with a specified key.
     * @param val value to be stored in the SharedPreferences.
     * @param key key for the value that is stored in the SharedPreference.
     */
    public static void putString( String val,String key) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, val);

        editor.apply();
    }

    /**
     * This method returns SharedPreferences object.
     * @return SharedPreferences object.
     */
    public static SharedPreferences getSharedPreferences()
    {
        if(preferences!=null)
            return preferences;
        else

            throw new RuntimeException(
                    "Prefs class not correctly instantiated please call Prefs.iniPrefs(context) in the Application class onCreate.");
    }

    /**
     * This method returns true if the login status is true in the shared preferences.
     * @param context from which this method is called.
     * @return login status as true or false.
     */
    public static boolean isLogin(Context context)
    {
        if(preferences==null)
            preferences=context.getSharedPreferences(SHARED_PREF, 0);
        return preferences.getBoolean(PREF_LOGIN, false);
    }

    /**
     * This method performs signout operation and puts login status as false in the SharedPreferences.
     * @param activity context of the activity from which this method is called.
     */
    public static void signOut(Activity activity){
        PreferenceData.putLoginStatus(activity,false);
        LoginManager.getInstance().logOut();
    }

    /**
     * This method returns the screen height.
     * @param activity context of the activity from which this method is called.
     * @return screen height
     */
    public static int getScreenHeight(Activity activity){
        Point size=new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.y;
    }

    /**
     * This method returns the width of the screen.
     * @param activity context of the activity from which this method is called.
     * @return screen width.
     */
    public static int getScreenWidth(Activity activity){
        Point size=new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }
}