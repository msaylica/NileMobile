package com.trin.nilmobile.model;

public class NavDrawerItem {
	
	private String title;
	private int icon;
	private String count = "0";
	// boolean to set visiblity of the counter
	private boolean isCounterVisible = false;
	
	public NavDrawerItem(){}

	//Create Navigation Drawer Item object
	public NavDrawerItem(String title, int icon){
		this.title = title;
		this.icon = icon;
	}
	
	//Create Navigation Drawer Item Object with counter
	public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count){
		this.title = title;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}
	
	//Get title of Navigation Drawer Item
	public String getTitle(){
		return this.title;
	}
	
	//Get icon of Navigation Drawer Item
	public int getIcon(){
		return this.icon;
	}
	
	//Get count of Navigation Drawer Items
	public String getCount(){
		return this.count;
	}
	
	//Get visibility of Navigation Drawer Item counter
	public boolean getCounterVisibility(){
		return this.isCounterVisible;
	}
	
	//Set title of Navigation Drawer Item
	public void setTitle(String title){
		this.title = title;
	}
	
	//Set icon of Navigation Drawer Item
	public void setIcon(int icon){
		this.icon = icon;
	}
	
	//Set count of Navigation Drawer Items
	public void setCount(String count){
		this.count = count;
	}
	
	//Set visibility of Navigation Drawer Item counter
	public void setCounterVisibility(boolean isCounterVisible){
		this.isCounterVisible = isCounterVisible;
	}
}
