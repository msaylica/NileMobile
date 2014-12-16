/*
 * Copyright (C) 2014 3IN Consulting Group 
 * 
 *      http://3in.ca
 */
package com.trin.nilmobile.reader;

import com.trin.nilmobile.R;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

/*
 * Display Contact page 
 * */
public class ContactUsFragment extends Fragment {

	CampusUtilities camp = new CampusUtilities();
	int locNum = 1;
	String url, phone;
	View rootView;
	String DATA = "userData";
	SharedPreferences sharedpreferences;
	private static final String campusKey = "campusKey";
	ImageButton mapButton;
	Button btnTell1;
	Button btnTell2;
	Button btnEmail;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		if (getCampus(campusKey).equals("Bluehaven")) {
			locNum = 2;
		} else
			locNum = 1;

		switch (locNum) {
		case 1:
			rootView = inflater.inflate(R.layout.plunket_contact, container,
					false);
			break;
		case 2:
			rootView = inflater.inflate(R.layout.bhaven_contact, container,
					false);

			break;

		}

		mapButton = (ImageButton) rootView.findViewById(R.id.mapButton);
		mapButton.setOnClickListener(new OnClickListener() {
			// Display Directions to Campus when clicked
			@Override
			public void onClick(View view) {
				switch (locNum) {
				case 1:
					url = "http://maps.google.com/maps?daddr=135+plunkett+rd";
					break;
				case 2:
					url = "http://maps.google.com/maps?daddr=5+Blue+Haven+Crst";
					break;

				}
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse(url));
				startActivity(intent);
			}
		});

		btnTell1 = (Button) rootView.findViewById(R.id.btnTell1);
		btnTell1.setOnClickListener(new OnClickListener() {
			// Call campus on click
			@Override
			public void onClick(View view) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				switch (locNum) {
				case 1:
					phone = "tel:14162850115";
					break;
				case 2:
					phone = "tel:16474445267";

				}
				callIntent.setData(Uri.parse(phone));
				startActivity(callIntent);
			}
		});

		btnTell2 = (Button) rootView.findViewById(R.id.btnTell2);
		btnTell2.setOnClickListener(new OnClickListener() {
			// Call campus on click
			@Override
			public void onClick(View view) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:14162859551"));
				startActivity(callIntent);
			}
		});

		btnEmail = (Button) rootView.findViewById(R.id.btnEmail);
		btnEmail.setOnClickListener(new OnClickListener() {
			// Email for information on click
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain"); // send email as plain text
				intent.putExtra(Intent.EXTRA_EMAIL,
						new String[] { "info@nileacademy.ca" });
				intent.putExtra(Intent.EXTRA_SUBJECT, "Information Request");
				intent.putExtra(Intent.EXTRA_TEXT, "");
				startActivity(Intent.createChooser(intent, ""));
			}
		});
		return rootView;
	}

	// shareIt
	protected void shareIt() {
		// TODO Auto-generated method stub
		// sharing implementation here
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = "https://play.google.com/store/apps/details?id=com.trin.nilmobile";
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				"Nile Academy");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}

	// Retrieves campus
	private String getCampus(String key) {
		SharedPreferences sp = this.getActivity().getSharedPreferences(DATA,
				android.content.Context.MODE_PRIVATE);
		return sp.getString(key, "");
	}
}
