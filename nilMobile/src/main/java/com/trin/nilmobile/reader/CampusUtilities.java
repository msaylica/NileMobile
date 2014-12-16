package com.trin.nilmobile.reader;

import java.util.List;



public class CampusUtilities {
	public static  String campusURL;
	public static  String postImgURL;
	public static String databaseURL;
	public static int galleryConst;
	public static String galleryUrls;
	public static String calendarURL;
	public static String noticeURL;
	private static List<String> iUrls;
	private static String[] urlArray;
	private static int num;
	
	/**
	 * @return  campusURL
	 */
	
	public String getCampusURL() {
		
		return campusURL;		
	}
	/**
	 * @param campusURL the campusURL to set
	 */
	public void setCampusURL(String campURL){
		
		CampusUtilities.campusURL=campURL;
	}
	
	/**
	 * @return  postImgURL
	 */
	
	public String getPostImgURL() {
		
		return postImgURL;		
	}
	/**
	 * @param postImgURL the postImgURL to set
	 */
	public void setPostImgURL(String postImgURL){
		
		CampusUtilities.postImgURL=postImgURL;
	}
	
	
	/**
	 * @return the databaseURL
	 */
	
	public String getDatabaseURL() {	
		return databaseURL;		
	}
	/**
	 * @param campusURL the campusURL to set
	 */
	public void setDatabaseURL(String dataURL){
		
		CampusUtilities.databaseURL=dataURL;
	}
	
	public String getGalleryURLS(){
		return galleryUrls;
		
	}
	
	public void setGalleryURLS(String string){
		CampusUtilities.galleryUrls = string;
	}

	public String getCalendarURL() {
		
		return calendarURL;	
	}
	/**
	 * @param campusURL the campusURL to set
	 */
	public void setCalendarURL(String calURL){
		
		CampusUtilities.calendarURL=calURL;
	}
	
	public String getNoticeURL() {
		
		return noticeURL;	
	}
	/**
	 * @param noticeURL the noticeURL to set
	 */
	public void setNoticeURL(String noticeURL){
		
		CampusUtilities.noticeURL=noticeURL;
	}
	
		
	public String[] getImageURLS(){
	    urlArray = new String[ iUrls.size() ];
		iUrls.toArray( urlArray );
		
		return urlArray;
	}
	public void setImageURLS(List<String> where) {
		// TODO Auto-generated method stub
		CampusUtilities.iUrls = where;
	}
	
	public int getLocationNum(){
		return num;
	}
	public void setLocationNum(int i) {
		CampusUtilities.num=i;
	}
}
