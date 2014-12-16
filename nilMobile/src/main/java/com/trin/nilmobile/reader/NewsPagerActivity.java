package com.trin.nilmobile.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.trin.nilmobile.R;
import com.trin.nilmobile.imageloader.BaseActivity;
import com.trin.nilmobile.imageloader.ImageGridActivity;
import com.trin.nilmobile.imageloader.ImagePagerActivity;
import com.trin.nilmobile.imageloader.Constants.Extra;
import com.trin.nilmobile.model.FeedItem;
import com.trin.nilmobile.serializer.ObjectSerializer;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressWarnings("deprecation")
public class NewsPagerActivity extends BaseActivity {

	private static final String STATE_POSITION = "STATE_POSITION";
	// private FeedItem feed;
	DisplayImageOptions options;
	private ProgressDialog pd;
	ViewPager pager;
	private ArrayList<FeedItem> feedList = null;
	private String URL;
	String[] imageUrls;
	String FeedDATA = "feedData";
	SharedPreferences sharedpreferences;
	private FeedItem currentFeed;
	private static final String feedKey = "FeedKey";
	View feedLayout;
	public Object ImageButton;
	public int pagerPosition;
	private String[] postUrls;
	protected List<String> postUrlList;
	static String shareURL="";
	static String shareTitle;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_news_pager);

		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("");

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		Bundle bundle = getIntent().getExtras();
		assert bundle != null;
		
		postUrls = bundle.getStringArray("posturls");	
		pagerPosition = bundle.getInt("position", 0);
		// load feed data from preference
		SharedPreferences prefs = getSharedPreferences(FeedDATA,
				Context.MODE_PRIVATE);

		try {
			feedList = (ArrayList<FeedItem>) ObjectSerializer.deserialize(prefs
					.getString(feedKey, ObjectSerializer
							.serialize(new ArrayList<FeedItem>())));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		currentFeed = feedList.get(pagerPosition);
		// FeedItem newsItem = (FeedItem) listData.get(position);
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new NewsPagerAdapter(postUrls));
		
		pager.setCurrentItem(pagerPosition);
		pager.setOffscreenPageLimit(0);
	}

	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, pager.getCurrentItem());
	}

	private class NewsPagerAdapter extends PagerAdapter {

		private String[] posts;
		private LayoutInflater inflater;
		

		NewsPagerAdapter(String[] posts) {
			this.posts = posts;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return posts.length;

		}

		@Override
		public Object instantiateItem(ViewGroup view, final int position) {
			feedLayout = inflater.inflate(R.layout.activity_feed_details, view,
					false);

			// assert feedLayout != null;

			currentFeed = feedList.get(position);
			ImageView thumb = (ImageView) feedLayout
					.findViewById(R.id.featuredImg);
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(currentFeed.getPostThumb(), thumb, options);

			TextView title = (TextView) feedLayout.findViewById(R.id.title);
			final String postTitle = currentFeed.getTitle();
			Html.fromHtml(postTitle);
			title.setText(Html.fromHtml(postTitle, null, null));
			shareTitle = postTitle;
			
			TextView date = (TextView) feedLayout.findViewById(R.id.date);
			date.setText(currentFeed.getDate());
			
			TextView content = (TextView) feedLayout.findViewById(R.id.content);
			Html.fromHtml(currentFeed.getContent());
			content.setText(Html.fromHtml(currentFeed.getContent(), null, null));
			
			final String postURL = currentFeed.getUrl();
			//shareURL= postURL;
			
			view.addView(feedLayout, 0);

			ImageButton imagesBtn = (ImageButton) feedLayout
					.findViewById(R.id.imagesBtn);
			imagesBtn.setOnClickListener(new OnClickListener() {
			
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(NewsPagerActivity.this,
							ImageGridActivity.class);
					
					intent.putExtra("postURL", postURL);
					Log.e("URL", "Post URL is: " + postURL);
					startActivity(intent);

				}

			});
			
			ImageButton shareBtn =(ImageButton) feedLayout.findViewById(R.id.action_share);
			shareBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT,
						postTitle + "\n" + postURL);
				sendIntent.setType("text/plain");
				startActivity(Intent.createChooser(sendIntent, "Share using")); 
			}
			});
			
			return feedLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.news_pager_menu, menu);
		return true;
	}

	
	
	/*
	 * DELTE THIS !! AFTER FIX
	 * SHARES THE NEXT COMING UP NEWS
	 */
	//share content	
			private void shareContent() { 
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT,
						shareTitle + "\n" + shareURL);
				sendIntent.setType("text/plain");
				startActivity(Intent.createChooser(sendIntent, "Share using")); 

			}
			

}