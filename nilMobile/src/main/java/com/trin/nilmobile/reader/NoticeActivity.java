/*
 * Copyright (C) 2014 3IN Consulting Group 
 * 
 *      http://3in.ca
 */
package com.trin.nilmobile.reader;

import com.trin.nilmobile.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/*Displays Important Notifications Using Webview*/
public class NoticeActivity extends Activity {


	public static final String TAG = null;
	WebView webview;
	String share_url;
	public ProgressDialog pd;
	public AlertDialog alertDialog;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		CampusUtilities camp = new CampusUtilities();
		String page_url = camp.getNoticeURL();
		
		 alertDialog = new AlertDialog.Builder(this).create();
		 pd = ProgressDialog.show(NoticeActivity.this, getString(R.string.wait), getString(R.string.loading));
		 
		 share_url = page_url;
			webview = (WebView) findViewById(R.id.webpage);
			
			webview.getSettings().setJavaScriptEnabled(true);
			webview.getSettings().setUseWideViewPort(true);
			webview.getSettings().setBuiltInZoomControls(true);
			 final Activity activity = this;
			 webview.setWebChromeClient(new WebChromeClient() {
			   public void onProgressChanged(WebView view, int progress) {
			     
			     activity.setProgress(progress * 1000);
			   }
			 });
			 webview.setWebViewClient(new WebViewClient() {
			   public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			     Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
			   }
			   public boolean shouldOverrideUrlLoading(WebView view, String url) {
			        Log.i(TAG,"Loading Notices...");
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

		    

		
	}


	//Share App
	private void shareIt() {
		// sharing implementation here
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = share_url;
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				"Nile Academy");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}
	
	//Options Menu
			@Override
			public boolean onCreateOptionsMenu(Menu menu) {
				getMenuInflater().inflate(R.menu.feed_menu, menu);
				return true;
			}

			@Override
			public boolean onOptionsItemSelected(MenuItem item) {
				
				// Handle action bar actions click
				switch (item.getItemId()) {
				case android.R.id.home:
					finish();
					return true;
				case R.id.action_refresh:
					finish();
					startActivity(getIntent());
					return true;
				
				
				default:
					return super.onOptionsItemSelected(item);
				}
			}


}
