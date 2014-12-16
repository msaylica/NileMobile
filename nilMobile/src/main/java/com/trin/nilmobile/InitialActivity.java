package com.trin.nilmobile;

import com.trin.nilmobile.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

public class InitialActivity extends Activity {
	String DATA = "userData";
	SharedPreferences sharedpreferences;
	private static final String campusKey = "campusKey";
	private static final String occupationKey = "occupationKey";
	TextView lblCampus1;
	Spinner spinCampus;
	Spinner spinOccupation;
	TextView btnNext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_initial);
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("");
		btnNext = (TextView) findViewById(R.id.nextBtn);
		spinCampus = (Spinner) findViewById(R.id.campusSpinner);
		spinOccupation = (Spinner) findViewById(R.id.occupationSpinner);

		/*
		 * Click event on Next button
		 */
		btnNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				String campus = String.valueOf(spinCampus.getSelectedItem());
				storePreferences(campusKey, campus);
				String occupation = String.valueOf(spinOccupation.getSelectedItem());
				storePreferences(occupationKey, occupation);
				Intent i = new Intent(InitialActivity.this,
						RegisterActivity.class);
				startActivity(i);
				//finish();
			}
		});

	}
	
	
	// Store user preferences
	private void storePreferences(String key, String value) {
		SharedPreferences sp = getApplicationContext().getSharedPreferences(
				DATA, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
}
