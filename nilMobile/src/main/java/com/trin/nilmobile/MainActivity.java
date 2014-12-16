/*
 * Copyright (C) 2014 3IN Consulting Group 
 * Created by: 3IN Consulting - Mehmet Saylica
 * Date: September 2014
 */
package com.trin.nilmobile;

import com.google.android.gcm.GCMRegistrar;
import com.trin.nilmobile.reader.DrawerActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends Activity {
	String regDATA = "RegistrationData";

	SharedPreferences sharedpreferences;

	private static final String regKey = "registered";
	boolean isRegistered;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_initial);
		// Check if regid already presents
		// Get GCM registration id
		boolean isregistered = getFromSP(regKey);

		final String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {

			if (isregistered == true) {
				// Skips registration.
				Intent i = new Intent(MainActivity.this, DrawerActivity.class);
				startActivity(i);
				finish();
			}else{
			// Registration is not present, register now with GCM
			Intent i = new Intent(MainActivity.this, InitialActivity.class);
			startActivity(i);
			finish();}
			// Device is already registered on GCM
		} else if (GCMRegistrar.isRegisteredOnServer(this)) {
			// Skips registration.
			Intent i = new Intent(MainActivity.this, DrawerActivity.class);
			startActivity(i);
			finish();
		}

	}

	// Retrieves User Registry state from sharedpreferences
	private boolean getFromSP(String key) {
		SharedPreferences sp = getApplicationContext().getSharedPreferences(
				regDATA, android.content.Context.MODE_PRIVATE);
		return sp.getBoolean(key, false);
	}
}