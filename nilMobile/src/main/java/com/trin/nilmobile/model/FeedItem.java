package com.trin.nilmobile.model;

import java.io.Serializable;

public class FeedItem implements Serializable {

	private String title;
	private String date;
	private String attachmentUrl;
	private String id;
	private String content;
	private String url;
	private String contentImageUrl;
	private String postThumb;

	// Get url of Feed Item
	public String getUrl() {
		return url;
	}

	// Set id of Feed Item
	public void setUrl(String url) {
		this.url = url;
	}

	// Get content of Feed Item
	public String getContent() {
		return content;
	}

	// Set content of Feed Item
	public void setContent(String content) {
		this.content = content;
	}

	// Get content image of Feed Item
	public String getContentImageUrl() {
		return contentImageUrl;
	}

	// set content image of Feed Item
	public void setContentImageUrl(String contentImageUrl) {
		this.contentImageUrl = contentImageUrl;
	}

	// Get id of Feed Item
	public String getId() {
		return id;
	}

	// Set id of Feed Item
	public void setId(String id) {
		this.id = id;
	}

	// Get title of Feed Item
	public String getTitle() {
		return title;
	}

	// Set title of Feed Item
	public void setTitle(String title) {
		this.title = title;
	}

	// Get date of Feed Item
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	// Get attachment Url of Feed Item
	public String getAttachmentUrl() {
		return attachmentUrl;
	}

	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}

	// Get thumb image of Feed Item
	public String getPostThumb() {
		return postThumb;
	}

	public void setPostThumb(String postThumb) {
		this.postThumb = postThumb;
	}

	@Override
	public String toString() {
		return "[ title=" + title + ", date=" + date + "]";
	}
}
