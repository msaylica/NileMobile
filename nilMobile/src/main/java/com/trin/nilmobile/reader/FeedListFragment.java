package com.trin.nilmobile.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.trin.nilmobile.R;
import com.trin.nilmobile.model.FeedItem;
import com.trin.nilmobile.serializer.ObjectSerializer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FeedListFragment extends Fragment {

	private ArrayList<FeedItem> feedList = null;
	private List<String> postUrl = null;
	private ListView feedListView = null;
	private ProgressDialog pd;
	protected ImageLoader imageLoader;
	String DATA = "userData";
	String FeedDATA = "feedData";
	SharedPreferences sharedpreferences;
	private static final String campusKey = "campusKey";
	private static final String feedKey = "FeedKey";
	public View rootView;
	String url;

	// protected String pUrl;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rootView = inflater.inflate(R.layout.activity_posts_list, container,
				false);
		// Initialize Image Loader
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this
				.getActivity()));

		if (getCampus(campusKey).equals("Please Select")
				|| getCampus(campusKey).equals("Other")) {
			storeCampus(campusKey, "Plunkett");
		}

		if (getCampus(campusKey).equals("Bluehaven")) {
			url = "http://nileacademy.ca/bluehaven/api/get_recent_posts";
		} else {
			url = "http://nileacademy.ca/plunkett/api/get_recent_posts";
		}
		new DownloadFilesTask().execute(url);

		return rootView;
	}

	public void updateList() {

		feedListView = (ListView) rootView.findViewById(R.id.custom_list);

		feedListView.setAdapter(new CustomListAdapter(this.getActivity(),
				feedList));
		feedListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				int postPosition = position;
				Object o = feedListView.getItemAtPosition(position);
				FeedItem newsData = (FeedItem) o;
				String[] pURL = postUrl.toArray(new String[postUrl.size()]);
				String selectedFeedUrl = pURL[position];
				Intent intent = new Intent(getActivity(),
						NewsPagerActivity.class);
				//intent.putExtra("feed", newsData);
				intent.putExtra("posturls", pURL);
			//	intent.putExtra("selectedPostUrl", selectedFeedUrl);
				intent.putExtra("position", postPosition);

				startActivity(intent);
			}
		});
	}

	/*
	 * Downloads feed in the background using AsyncTask
	 */
	private class DownloadFilesTask extends AsyncTask<String, Integer, Void> {

		@Override
		protected void onProgressUpdate(Integer... values) {
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(getActivity());
			pd.setMessage("loading news");
			pd.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			if (null != feedList) {
				updateList();
			}
			pd.dismiss();
		}

		@Override
		protected Void doInBackground(String... params) {
			String url = params[0];

			// getting JSON string from URL
			JSONObject json = getJSONFromUrl(url);

			// parsing json data
			parseJson(json);
			return null;
		}
	}

	public JSONObject getJSONFromUrl(String url) {
		InputStream is = null;
		JSONObject jObj = null;
		String json = null;

		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			json = sb.toString();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		return jObj;

	}

	/*
	 * parses json object
	 */
	public void parseJson(JSONObject json) {
		try {

			if (json.getString("status").equalsIgnoreCase("ok")) {
				JSONArray posts = json.getJSONArray("posts");
				postUrl = new ArrayList<String>();
				feedList = new ArrayList<FeedItem>();

				for (int i = 0; i < posts.length(); i++) {
					JSONObject post = (JSONObject) posts.getJSONObject(i);
					FeedItem item = new FeedItem();
					Spanned postTitle = (Html.fromHtml(post.getString("title"), null,
							null));
					String title = postTitle.toString();
					item.setTitle(title);
					item.setDate(post.getString("date"));
					item.setId(post.getString("id"));
					item.setUrl(post.getString("url"));
					item.setContent(post.getString("content"));

					JSONObject bigimg = post.getJSONObject("custom_fields");

					JSONArray thumb = bigimg.getJSONArray("bigimg");
					item.setPostThumb(thumb.getString(0));

					JSONArray attachments = post.getJSONArray("attachments");

					if (null != attachments && attachments.length() > 0) {
						JSONObject attachment = attachments.getJSONObject(0);
						if (attachment != null)
							item.setAttachmentUrl(attachment.getString("url"));
					}
					postUrl.add(post.getString("url"));
					feedList.add(item);

					// save the task list to preference
					storeFeedData(feedKey, feedList);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// share App
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

	// Store user preferences
	private void storeFeedData(String key, ArrayList<FeedItem> feedList2) {
		SharedPreferences sp = this.getActivity().getSharedPreferences(
				FeedDATA, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		try {
			editor.putString(feedKey, ObjectSerializer.serialize(feedList2));
		} catch (IOException e) {
			e.printStackTrace();
		}
		editor.commit();
	}

	// Retrieves Campus
	private String getCampus(String key) {
		SharedPreferences sp = this.getActivity().getSharedPreferences(DATA,
				android.content.Context.MODE_PRIVATE);
		return sp.getString(key, "");
	}

	// Store User campus
	private void storeCampus(String key, String value) {
		SharedPreferences sp = this.getActivity().getSharedPreferences(DATA,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
}
