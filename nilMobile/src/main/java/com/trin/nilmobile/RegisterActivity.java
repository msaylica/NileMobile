package com.trin.nilmobile;

import static com.trin.nilmobile.CommonUtilities.SENDER_ID;
import static com.trin.nilmobile.CommonUtilities.SERVER_URL;

import com.trin.nilmobile.R;
import com.trin.nilmobile.AlertDialogManager;
import com.trin.nilmobile.ConnectionDetector;
import com.trin.nilmobile.GcmActivity;
import com.trin.nilmobile.RegisterActivity;
import com.trin.nilmobile.reader.DrawerActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class RegisterActivity extends Activity {

	String DATA = "userData";
	String regDATA = "RegistrationData";
	SharedPreferences sharedpreferences;
	private static final String occupationKey = "occupationKey";
	private static final String regKey = "registered";
	private static final String campusKey = "campusKey";

	// alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();
	ConnectionDetector cd;

	// UI elements
	EditText txtFName;
	EditText txtLName;
	TextView lblGradeSpnr;
	Spinner spinGrade;

	EditText txtEmail;

	EditText txtChildName;
	EditText txtSpecify;
	Button btnRegister;
	TextView txtSkip;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("");
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(
					RegisterActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection and re run the application",
					false);
			// stop executing code by return
			return;
		}
		// Check if GCM configuration is set
		if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
				|| SENDER_ID.length() == 0) {
			// GCM sender id / server url is missing
			alert.showAlertDialog(RegisterActivity.this,
					"Configuration Error!",
					"Please set your Server URL and GCM Sender ID", false);
			// stop executing code by return
			return;
		}

		txtFName = (EditText) findViewById(R.id.txtFname);
		txtLName = (EditText) findViewById(R.id.txtLname);

		lblGradeSpnr = (TextView) findViewById(R.id.lblGrade);
		spinGrade = (Spinner) findViewById(R.id.gradeSpinner);

		txtEmail = (EditText) findViewById(R.id.txtEmail);

		txtChildName = (EditText) findViewById(R.id.txtChildName);
		txtSpecify = (EditText) findViewById(R.id.txtSpecify);
		btnRegister = (Button) findViewById(R.id.btnRegister);

		txtSkip = (TextView) findViewById(R.id.txtSkip);

		String occupation = getInfo(occupationKey);

		if (occupation.equals("Student")) {
			lblGradeSpnr.setVisibility(View.VISIBLE);
			spinGrade.setVisibility(View.VISIBLE);
		} else if (occupation.equals("Parent")) {
			lblGradeSpnr.setVisibility(View.GONE);
			spinGrade.setVisibility(View.GONE);
			txtChildName.setVisibility(View.VISIBLE);
			txtSpecify.setVisibility(View.GONE);
		} else if (occupation.equals("Staff")) {
			txtSpecify.setVisibility(View.GONE);
			lblGradeSpnr.setVisibility(View.GONE);
			spinGrade.setVisibility(View.GONE);
			txtChildName.setVisibility(View.GONE);
		} else if (occupation.equals("Other")) {
			txtSpecify.setVisibility(View.VISIBLE);
			lblGradeSpnr.setVisibility(View.GONE);
			spinGrade.setVisibility(View.GONE);
			txtChildName.setVisibility(View.GONE);
		} else {
			lblGradeSpnr.setVisibility(View.GONE);
			spinGrade.setVisibility(View.GONE);
			txtChildName.setVisibility(View.GONE);
		}

		/*
		 * Click event on Register button
		 */
		btnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Read EditText dat
				String fname = txtFName.getText().toString();
				String lname = txtLName.getText().toString();
				String occupation = getInfo(occupationKey);
				String campus = getInfo(campusKey);
				String grade = String.valueOf(spinGrade.getSelectedItem());
				String childName = txtChildName.getText().toString();
				String specifics = txtSpecify.getText().toString();
				String email = txtEmail.getText().toString();

				// Check if user filled the form
				if (fname.trim().length() > 0 && lname.trim().length() > 0) {
					// Launch Main Activity
					Intent i = new Intent(getApplicationContext(),
							GcmActivity.class);
					// Registering user on our server
					// Sending registraiton details to MainActivity
					i.putExtra("fname", fname);
					i.putExtra("lname", lname);
					i.putExtra("email", email);
					i.putExtra("campus", campus);
					i.putExtra("occupation", occupation);
					i.putExtra("childName", childName);
					i.putExtra("specifics", specifics);
					i.putExtra("grade", grade);
					startActivity(i);
					finish();
				} else {
					// user doen't filled that data
					// ask him to fill the form
					alert.showAlertDialog(RegisterActivity.this,
							"Registration Error!", "Please enter your details",
							false);
				}
			}
		});

		txtSkip.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				storeRegistrationState(regKey, true);
				Intent i = new Intent(RegisterActivity.this,
						DrawerActivity.class);
				startActivity(i);
				finish();
			}
		});
	}

	// Retrieves User occupation
	private String getInfo(String key) {
		SharedPreferences sp = getApplicationContext().getSharedPreferences(
				DATA, android.content.Context.MODE_PRIVATE);
		return sp.getString(key, "");
	}

	// Store User registry state
	private void storeRegistrationState(String key, boolean value) {
		SharedPreferences sp = getApplicationContext().getSharedPreferences(
				regDATA, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle action bar actions click
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
