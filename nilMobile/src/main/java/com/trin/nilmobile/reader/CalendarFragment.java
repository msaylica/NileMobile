/*
 * Copyright (C) 2014 3IN Consulting Group 
 * 
 *      http://3in.ca
 */
package com.trin.nilmobile.reader;

import com.trin.nilmobile.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/*
 * Displays calendar in a webview
 * 
 * */
public class CalendarFragment extends Fragment {

	public static final String TAG = "Calendar";
	WebView webview;
	String share_url;
	String DATA = "userData";
	private static final String campusKey = "campusKey";
	public ProgressDialog pd;
	public AlertDialog alertDialog;
	String page_url;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		View rootView = inflater.inflate(R.layout.webview, container, false);

		if (getInfo(campusKey).equals("Plunkett")) {
			page_url = "http://nileacademy.ca/nilemobile/plunkett_calendar.html";
		} else if (getInfo(campusKey).equals("Bluehaven")) {
			page_url = "http://nileacademy.ca/nilemobile/bh_high_calendar.html";
		} else {
			page_url = "http://nileacademy.ca/nilemobile/plunkett_calendar.html";
		}
		alertDialog = new AlertDialog.Builder(this.getActivity()).create();

		pd = ProgressDialog.show(this.getActivity(),
				getString(R.string.calendar_desc), getString(R.string.loading));

		share_url = page_url;
		webview = (WebView) rootView.findViewById(R.id.webpage);

		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setUseWideViewPort(true);
		webview.getSettings().setBuiltInZoomControls(true);
		final Activity activity = this.getActivity();
		webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {

				activity.setProgress(progress * 1000);
			}
		});
		webview.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(activity, "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.i(TAG, "Loading Calendar...");
				view.loadUrl(url);
				return true;
			}

			public void onPageFinished(WebView view, String url) {
				Log.i(TAG, "Finished loading URL: " + url);
				if (pd.isShowing()) {
					pd.dismiss();
				}
			}
		});

		webview.loadUrl(page_url);
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

	// Retrieves Campus
	private String getInfo(String key) {
		SharedPreferences sp = this.getActivity().getSharedPreferences(DATA,
				android.content.Context.MODE_PRIVATE);
		return sp.getString(key, "");
	}
}