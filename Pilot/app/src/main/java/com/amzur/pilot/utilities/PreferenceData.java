package com.amzur.pilot.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;

import android.provider.Settings;
import android.widget.Toast;

import android.util.Log;

import com.amzur.pilot.MyApplication;
import com.amzur.pilot.interfaces.ConformationListener;
import com.amzur.pilot.myretrofit.Listener;
import com.amzur.pilot.myretrofit.RetrofitService;
import com.facebook.login.LoginManager;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit.Call;

/**
 * Created by MRamesh on 09-05-2016.
 *
 */
public class PreferenceData {
    private static SharedPreferences preferences;
    public static final String SHARED_PREF="credentials";
    private static SharedPreferences.Editor editor;
    public static final String PREF_LOGIN="login";

    public static final String PREF_API_KEY="api_key";
    public static final String PREF_EMAIL="email";
    static final String TAG = "Splash Screen";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";


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


    public static void putEmail(Context context, String email_id)
    {
        if(preferences==null)
            preferences=context.getSharedPreferences(SHARED_PREF, 0);
        editor=preferences.edit();
        editor.putString(PREF_EMAIL, email_id);
        editor.apply();
    }
    /**
     * This method stores a value in the sharedPreferences with a specified key.
     * @param val value to be stored in the SharedPreferences.
     * @param key key for the value that is stored in the SharedPreference.
     */
    public static void putString( String key,String val) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, val);
        editor.apply();
    }

    public static String getString(String key)
    {
        if(preferences==null)
            preferences=getSharedPreferences();
        return preferences.getString(key,"hello");
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
    public static void signOut(final Activity activity){
        Utils.showConformationDialog(activity, "Sign out", "Do you want to Sign out?", new ConformationListener() {
            @Override
            public void conformed() {
                signOutFromSession(activity);
            }
        });


    }

    /**
     * Deletes the session from backend of this user and API_KEY get invalid now*/
    public static void signOutFromSession(final Activity activity){
        JSONObject logoutObject=new JSONObject();
        try {
            logoutObject.put("email", PreferenceData.getString(PreferenceData.PREF_EMAIL));
            logoutObject.put("deviceId", Settings.Secure.getString(MyApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID));
            logoutObject.put("deviceType", "ANDROID");
            logoutObject.put("deviceToken", PreferenceData.getRegistrationId(activity));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<ResponseBody> call= MyApplication.getSerivce().authLogout(PreferenceData.getString(PreferenceData.PREF_API_KEY),logoutObject.toString(), "application/json");
        call.enqueue(new Listener(new RetrofitService() {
            @Override
            public void onSuccess(String result, int pos, Throwable t) {
                if(pos==0)
                {
                    Toast.makeText(activity,"Successfully logged out",Toast.LENGTH_LONG).show();
                    PreferenceData.putLoginStatus(activity,false);
                    LoginManager.getInstance().logOut();
                }
            }
        },"singing out...",true,activity));

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

    /**
     * This method gets gcm registration id from the shared preferences.
     * @param con context of the application.
     * @return
     */
    public static String getRegistrationId(Context con) {
        if(preferences==null)
            preferences=con.getSharedPreferences(SHARED_PREF, 0);
        String registrationId = preferences.getString(PROPERTY_REG_ID, "");
        //Log.i(TAG, registrationId);
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = preferences.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(con);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }
    public static void storeRegistrationId(Context context, String regId) {
        if(preferences==null)
            preferences=context.getSharedPreferences(SHARED_PREF, 0);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


    public static synchronized void initPrefs(Context context) {
        if (preferences == null) {

            preferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        }
    }

    /**
     * This method returns the cost in us currency format.
     * @param val value to be converted into us currency format.
     * @return
     */
    public static String getUsCurrency(Object val)
    {
        NumberFormat format=NumberFormat.getCurrencyInstance(Locale.US);
        return format.format(val);
    }

}
