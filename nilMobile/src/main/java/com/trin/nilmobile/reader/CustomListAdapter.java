package com.trin.nilmobile.reader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.trin.nilmobile.R;
import com.trin.nilmobile.model.FeedItem;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {
	DisplayImageOptions options;
	private ArrayList<FeedItem> listData;

	private LayoutInflater layoutInflater;

	public CustomListAdapter(Context context, ArrayList<FeedItem> listData) {
		
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(10))
		.build();
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
			holder = new ViewHolder();
			holder.headlineView = (TextView) convertView.findViewById(R.id.title);
			holder.reportedDateView = (TextView) convertView.findViewById(R.id.date);
			holder.shortContentView = (TextView) convertView.findViewById(R.id.content);
			holder.imageView = (ImageView) convertView.findViewById(R.id.thumbImage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		FeedItem newsItem = (FeedItem) listData.get(position);
		holder.headlineView.setText(newsItem.getTitle());
		holder.reportedDateView.setText(newsItem.getDate());
		Html.fromHtml(newsItem.getContent());
		holder.shortContentView.setText(Html.fromHtml(newsItem.getContent()));

		if (holder.imageView != null) {
			//new ImageDownloaderTask(holder.imageView).execute(newsItem.getAttachmentUrl());
			
			//String imageUrls = newsItem.getAttachmentUrl();
			String imageUrls = newsItem.getPostThumb();
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(imageUrls, holder.imageView,
					options, animateFirstListener);
		}

		return convertView;
	}

	static class ViewHolder {
		TextView headlineView;
		TextView reportedDateView;
		TextView shortContentView;
		ImageView imageView;
	}
	
	
	public void onBackPressed() {
		AnimateFirstDisplayListener.displayedImages.clear();
		//super.onBackPressed();
	}
	
	private static class AnimateFirstDisplayListener extends
	SimpleImageLoadingListener {
		static final List<String> displayedImages = Collections
		.synchronizedList(new LinkedList<String>());
	}
	
	/*
	private void onLoadingComplete(String imageUri, View view,
			Bitmap loadedImage) {
		if (loadedImage != null) {
			ImageView imageView = (ImageView) view;
			boolean firstDisplay = !displayedImages.contains(imageUri);
			if (firstDisplay) {
				FadeInBitmapDisplayer.animate(imageView, 500);
				displayedImages.add(imageUri);
			}
		}
	} */
}
