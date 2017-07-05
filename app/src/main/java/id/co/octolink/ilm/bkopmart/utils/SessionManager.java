package id.co.octolink.ilm.bkopmart.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import id.co.octolink.ilm.bkopmart.ui.login.LoginActivity;

/**
 * Created by E.R.D on 4/2/2016.
 */
public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "BKOPMART_ERD";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public static final String KEY_CUSTOMER_ID = "customer_id";
    public static final String KEY_CUSTOMER_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_BIRTH_DATE = "birth_date";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_CREATE_AT = "create_at";
    public static final String KEY_RECORD_STATUS = "record_status";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String custID, String custName, String email, String password,
                                   String birthdate, String gender, String avatar, String createAt,
                                   String recordStatus){
        editor.putBoolean(KEY_IS_LOGGEDIN, true);

        editor.putString(KEY_CUSTOMER_ID, custID);
        editor.putString(KEY_CUSTOMER_NAME, custName);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_BIRTH_DATE, birthdate);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_AVATAR, avatar);
        editor.putString(KEY_CREATE_AT, createAt);
        editor.putString(KEY_RECORD_STATUS, recordStatus);

        editor.commit();
    }

    public void createChangePicture(String pic_url){
        editor.putBoolean(KEY_IS_LOGGEDIN, true);

        editor.putString(KEY_AVATAR, pic_url);

        editor.commit();
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.commit();
    }

    public HashMap<String, String> getLoginDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_CUSTOMER_ID, pref.getString(KEY_CUSTOMER_ID, null));
        user.put(KEY_CUSTOMER_NAME, pref.getString(KEY_CUSTOMER_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        user.put(KEY_BIRTH_DATE, pref.getString(KEY_BIRTH_DATE, null));
        user.put(KEY_GENDER, pref.getString(KEY_GENDER, null));
        user.put(KEY_AVATAR, pref.getString(KEY_AVATAR, null));
        user.put(KEY_CREATE_AT, pref.getString(KEY_CREATE_AT, null));
        user.put(KEY_RECORD_STATUS, pref.getString(KEY_RECORD_STATUS, null));

        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        _context.startActivity(i);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

}
