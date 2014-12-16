package com.trin.nilmobile.reader;

import com.trin.nilmobile.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

public class PreferencesFragment extends Fragment {
	String DATA = "userData";
	SharedPreferences sharedpreferences;
	private static final String occupationKey = "occupationKey";
	private static final String campusKey = "campusKey";
	Spinner campus;
	Spinner occupation;
	TextView lblGradeSpnr;
	Spinner spinGrade;
	EditText txtChildName;
	public PreferencesFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Set campus selection preference
		View rootView = inflater.inflate(R.layout.preferences_fragment,
				container, false);
		campus = (Spinner) rootView.findViewById(R.id.campusSpinner);
		occupation = (Spinner) rootView.findViewById(R.id.occupationSpinner);
		String savedCampus =(getInfo("campusKey"));
		if(savedCampus.equals("Please Select")){
			campus.setSelection(0);
		}else if(savedCampus.equals("Plunkett")){
			campus.setSelection(1);
		}else if (savedCampus.equals("Bluehaven")){
			campus.setSelection(2);
		}else {
			campus.setSelection(3);
		}
		
		campus.setOnItemSelectedListener(new OnItemSelectedListener() {

			

			 @Override
	            public void onItemSelected(AdapterView<?> arg0, View arg1,
	                    int arg2, long arg3) {
				
				String campusValue = String.valueOf(campus.getSelectedItem());
				storeInfo(campusKey, campusValue);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		//Set occupation 
		String savedOccupation =(getInfo("occupationKey"));
		if(savedOccupation.equals("Please Select")){
			occupation.setSelection(0);
		}else if(savedOccupation.equals("Student")){
			occupation.setSelection(1);
		}else if (savedOccupation.equals("Parent")){
			occupation.setSelection(2);
		}else if (savedOccupation.equals("Staff")){
			occupation.setSelection(3);
		}else {
			occupation.setSelection(4);
		}
		
		occupation.setOnItemSelectedListener(new OnItemSelectedListener() {

			
			 @Override
	            public void onItemSelected(AdapterView<?> arg0, View arg1,
	                    int arg2, long arg3) {
				// TODO Auto-generated method stub
				String occupationValue = String.valueOf(occupation.getSelectedItem());
				storeInfo(occupationKey, occupationValue);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return rootView;

	}
	 
	
	// Store User registry state
	private void storeInfo(String key, String value) {
		SharedPreferences sp = this.getActivity().getSharedPreferences(
				DATA, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	// Retrieves User occupation
	private String getInfo(String key) {
		SharedPreferences sp = this.getActivity().getSharedPreferences(
				DATA, android.content.Context.MODE_PRIVATE);
		return sp.getString(key, "");
	}
}
