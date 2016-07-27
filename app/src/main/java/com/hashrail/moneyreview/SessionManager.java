package com.hashrail.moneyreview;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SessionManager {

    // Shared preferences file name

    public static final String PREF_PROOF_NAME = "PREF_PROOF_NAME";
    public static final String KEY_CURRENCY = "currency";


    // Shared Preferences
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();
    public Editor editor;
    // Shared Preferences
    SharedPreferences pref;
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_PROOF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }


    public void setCurrency(String currency) {


        editor.putString(KEY_CURRENCY, currency);

        // commit changes
        editor.commit();

        //  Log.d(TAG, "User login session modified!");
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name

        user.put(KEY_CURRENCY, pref.getString(KEY_CURRENCY, "Rs."));

        return user;

    }


}
