package com.PP.wifilocalizerlib.Activities;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.PP.wifilocalizerlibrary.R;

public class DataCollectionActivity extends BaseWifiActivity 
	implements OnClickListener, OnItemSelectedListener {
	
	//receiver
	protected DataCollectionReceiver receiver;
		
	//book-keeping
	protected int cReadingNumber;	
	protected boolean CLICKED = false;			
	
	//gui components and user input
	protected AutoCompleteTextView locText;
	protected Button buttonScan;
	protected int numReadingsPerClick;
	protected String orientation;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		//theme
    	this.setTheme(android.R.style.Theme_Black);        		
		super.onCreate(savedInstanceState);
		
		// Setup UI
		setContentView(R.layout.data_collection_layout);		
		buttonScan = (Button) findViewById(R.id.buttonScan);
		buttonScan.setOnClickListener(this);
		locText = (AutoCompleteTextView) findViewById(R.id.locField);
		Spinner numReadingsSpinner = (Spinner) findViewById(R.id.numReadings);
		numReadingsSpinner.setOnItemSelectedListener(this);	
		Spinner orientationSpinner = (Spinner) findViewById(R.id.orientation);
		orientationSpinner.setOnItemSelectedListener(this);
		
	}	
	
	//button call back
	public void onClick(View view) {
		if (view.getId() == R.id.buttonScan && !CLICKED) {
			
			//if location tag is empty, easy exit.
			if(locText.getText().toString().trim().equals("")) {
				Toast t = Toast.makeText(this, "Location tag is empty. Please enter a location tag.",Toast.LENGTH_SHORT);
				t.setGravity(Gravity.CENTER, 0, 0);
				t.show();				
				return;
			}
			
			//display scan message
			Toast t = Toast.makeText(this, "Doing Wifi-Scan..",Toast.LENGTH_SHORT);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
			
			//reset flags
			cReadingNumber = 1;
			CLICKED = true;			
			
			//start first scan
			wifi.startScan();
		}
	}
	
	//num readings sel callback
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		if(parent.getId()==R.id.numReadings) {
			String selection = (String) parent.getItemAtPosition(pos);
			int selInt = Integer.parseInt(selection);
			numReadingsPerClick = selInt;
		}
		else if(parent.getId()==R.id.orientation) {
			orientation = (String) parent.getItemAtPosition(pos);
		}
	}
	
	@Override
	public BroadcastReceiver getReceiver() {
		if(receiver==null) {
			receiver = new DataCollectionReceiver(this);
		}
		return receiver;
	}
	
	//unused
	public void onNothingSelected(AdapterView<?> arg0) {}
	
}