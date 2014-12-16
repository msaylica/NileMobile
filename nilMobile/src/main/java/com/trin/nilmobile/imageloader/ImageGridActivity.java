/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.trin.nilmobile.imageloader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.trin.nilmobile.R;
import com.trin.nilmobile.imageloader.Constants.Extra;
import com.trin.nilmobile.reader.CampusUtilities;
import com.trin.nilmobile.reader.NewsPagerActivity;

//import com.trin.nilmobile.reader.NewsPagerActivity.ImageGalleryAdapter;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImageGridActivity extends AbsListViewBaseActivity {
	//Pop-up Menu button 
	ImageButton pop_menu;
	private ProgressDialog pd;
	//static CampusUtilities camp = new CampusUtilities();
	String[] imageUrls;//= camp.getImageURLS();

	DisplayImageOptions options;

	private String URL;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_grid);
		
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("");
		
		Bundle bundle = getIntent().getExtras();
		assert bundle != null;
		
		URL =bundle.getString("postURL");
		Log.e("URL", "pst url is " + URL);
		
		
		 new Parse().execute(URL);
		options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_stub)
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_error)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();

		
		
	}

	private void startImagePagerActivity(int position) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		intent.putExtra(Extra.IMAGES, imageUrls);
		intent.putExtra(Extra.IMAGE_POSITION, position);
		startActivity(intent);
	}

	public class ImageAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return imageUrls.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ImageView imageView;
			if (convertView == null) {
				imageView = (ImageView) getLayoutInflater().inflate(R.layout.item_grid_image, parent, false);
			} else {
				imageView = (ImageView) convertView;
			}

			imageLoader.displayImage(imageUrls[position], imageView, options);

			return imageView;
		}
	}
	
			//shareIt 	  
			protected void shareIt() {
			// TODO Auto-generated method stub
			//sharing implementation here
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				String shareBody = "https://play.google.com/store/apps/details?id=com.trin.ahfmobile";
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Nile Academy");
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
				startActivity(Intent.createChooser(sharingIntent, "Share via"));
			}
	
	@Override
	public void onBackPressed() {
		
	    finish();
	}
	
	public class Parse extends AsyncTask<String, Integer, List<String>> {
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(ImageGridActivity.this);
			pd.setMessage("Loading Content, please wait!");
			pd.show();
		}

		@Override
		protected List<String> doInBackground(String... urls) {
			List<String> where = new ArrayList<String>();
			try {
				Document doc = Jsoup.connect(URL).timeout(4000).get();
				Elements img = doc.select("a[rel*=lightbox]");
				for (org.jsoup.nodes.Element src : img) {
					String imgSrc = src.attr("href");

					System.out.println(imgSrc);

					where.add(imgSrc);

				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			imageUrls = new String[where.size()];
			where.toArray(imageUrls);
			return where;
		}

		@Override
		protected void onPostExecute(List<String> where) {

			listView = (GridView) findViewById(R.id.gridview);
			((GridView) listView).setAdapter(new ImageAdapter());
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					startImagePagerActivity(position);
				}
			});

			pd.dismiss();
		}
	}
	
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
		case R.id.action_share:
		//	shareContent();
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}

