package com.example.muntako;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

@SuppressLint("CommitPrefEdits")
public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;
	
	// Editor for Shared preferences
	Editor editor;
	
	// Context
	Context _context;
	
	// Shared pref mode
	int PRIVATE_MODE = 0;
	
	// nama sharepreference
	private static final String PREF_NAME = "Sesi";
	
	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";
	public static final String KEY_NAME = "nama";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_PASS = "password";
	public static final String KEY_TTL = "ttl";
	public static final String KEY_NOTEL = "notel";
	public static final String KEY_ID = "id";
	public static final String KEY_ACKEY = "ackey";
	public static final String KEY_JEKEL = "jekel";
	
	// Constructor
	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	/**
	 * Create login session
	 * */
	public void createLoginSession(String email, String password,String nama, String ttl, String notel, String id, String ackey, String jekel){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);
		editor.putString(KEY_EMAIL, email);
		editor.putString(KEY_PASS, password);
		editor.putString(KEY_TTL, ttl);
		editor.putString(KEY_NOTEL, notel);
		editor.putString(KEY_NAME, nama);
		editor.putString(KEY_ID, id);
		editor.putString(KEY_ACKEY, ackey);
		editor.putString(KEY_JEKEL, jekel);
		editor.commit();
	}	
	
	/**
	 * Check login method wil check user login status
	 * If false it will redirect user to login page
	 * Else won't do anything
	 * */

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<>();
		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
		user.put(KEY_PASS, pref.getString(KEY_PASS, null));
		user.put(KEY_TTL, pref.getString(KEY_TTL, null));
		user.put(KEY_NOTEL, pref.getString(KEY_NOTEL, null));
		user.put(KEY_NAME, pref.getString(KEY_NAME, null));
		user.put(KEY_ID, pref.getString(KEY_ID, null));
		user.put(KEY_ACKEY, pref.getString(KEY_ACKEY, null));
		user.put(KEY_JEKEL, pref.getString(KEY_JEKEL, null));
		return user;
	}

	/**
	 * Clear session details
	 * */
	public void logoutUser(){
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();
	}
	
	public boolean isLoggedIn(){
		return pref.getBoolean(IS_LOGIN, false);
	}
}
