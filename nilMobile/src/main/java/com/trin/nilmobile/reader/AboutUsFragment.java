/*
 * Copyright (C) 2014 3IN Consulting Group 
 * 
 *      http://3in.ca
 */
package com.trin.nilmobile.reader;

import com.trin.nilmobile.R;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AboutUsFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		View rootView = inflater.inflate(R.layout.activity_aboutus, container,
				false);

		
		return rootView;
	}

	// share App
	protected void shareIt() {
		// sharing implementation here
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = "https://play.google.com/store/apps/details?id=com.trin.nilmobile";
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				"Nile Academy");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}
}
