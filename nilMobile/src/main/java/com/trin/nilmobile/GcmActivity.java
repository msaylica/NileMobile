package com.trin.nilmobile;

import static com.trin.nilmobile.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.trin.nilmobile.CommonUtilities.EXTRA_MESSAGE;
import static com.trin.nilmobile.CommonUtilities.SENDER_ID;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.trin.nilmobile.R;
import com.trin.nilmobile.AlertDialogManager;
import com.trin.nilmobile.ConnectionDetector;
import com.trin.nilmobile.GcmActivity;
import com.trin.nilmobile.ServerUtilities;
import com.trin.nilmobile.WakeLocker;
import com.trin.nilmobile.reader.DrawerActivity;

public class GcmActivity extends Activity {
	// label to display gcm messages
	TextView lblMessage;

	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;

	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();

	// Connection detector
	ConnectionDetector cd;

	public static String fname;
	public static String lname;
	public static String email;
	public static String campus;
	public static String occupation;
	public static String childName;
	public static String specifics;
	public static String grade;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(GcmActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);

			return;
		}

		// Getting name, email from intent
		Intent i = getIntent();

		fname = i.getStringExtra("fname");
		lname = i.getStringExtra("lname");
		email = i.getStringExtra("email");
		campus = i.getStringExtra("campus");
		occupation = i.getStringExtra("occupation");
		childName = i.getStringExtra("childName");
		specifics = i.getStringExtra("specifics");
		grade = i.getStringExtra("grade");

		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		GCMRegistrar.checkManifest(this);

		lblMessage = (TextView) findViewById(R.id.lblMessage);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));

		// Get GCM registration id
		final String regId = GCMRegistrar.getRegistrationId(this);

		// Check if regid already presents
		if (regId.equals("")) {
			// Registration is not present, register now with GCM
			GCMRegistrar.register(this, SENDER_ID);
		} else {
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.
				Toast.makeText(getApplicationContext(),
						"Already registered with GCM", Toast.LENGTH_LONG)
						.show();
			} else {
				// Try to register again, but not in the UI thread.

				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						// Register on our server
						// On server creates a new user
						ServerUtilities.register(context, fname, lname, email,
								campus, occupation, childName, specifics, grade,
								regId);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}

		Intent intent = new Intent(GcmActivity.this, DrawerActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 * */

			// Showing received message
			// lblMessage.append(newMessage + "\n");
			// Toast.makeText(getApplicationContext(), "New Message: " +
			// newMessage, Toast.LENGTH_LONG).show();

			// Releasing wake lock
			WakeLocker.release();
		}
	};

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

}