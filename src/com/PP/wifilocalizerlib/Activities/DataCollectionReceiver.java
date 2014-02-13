package com.PP.wifilocalizerlib.Activities;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.ScanResult;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.Toast;

import com.PP.wifilocalizerlib.data.FileOpAPI;
import com.PP.wifilocalizerlib.data.Parser;

public class DataCollectionReceiver extends BroadcastReceiver {
  
  //my sending process
  protected DataCollectionActivity dataCollActivity;
  
  //global reading ID
  protected int readingID;
  
  /*
   * Ctr
   */
  public DataCollectionReceiver(DataCollectionActivity dataCollActivity) {
    super();
    readingID = PreferenceManager.getDefaultSharedPreferences(dataCollActivity.getApplicationContext()).getInt("READING_ID", 0);
    this.dataCollActivity = dataCollActivity;
  }
  
  //SSID,RSSI,READING,loc_tag
  
  /*
   * Receiver callback
   */
  @SuppressLint({ "NewApi", "NewApi" })
  @Override
  public void onReceive(Context c, Intent intent) {

	  if(dataCollActivity.CLICKED) {
		  
		//get scan results
	    List<ScanResult> results = dataCollActivity.wifi.getScanResults();
	    
	    //if no scan results, return.
	    if(results.size()==0) {
	    	
	    	//error message
			Toast t = Toast.makeText(dataCollActivity, "No Wi-Fi access points available.",Toast.LENGTH_SHORT);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
			
			//set clicked false
			dataCollActivity.CLICKED = false;
			
			//return
	    	return; //easy exit
	    }
	    
	    //parse scan results
	    String str = "";
	    String orientationTag = "";
	    if(dataCollActivity.orientation.equalsIgnoreCase("North")) {
	    	orientationTag = "N";
	    }
	    else if(dataCollActivity.orientation.equalsIgnoreCase("South")) {
	    	orientationTag = "S";
	    }
	    else if(dataCollActivity.orientation.equalsIgnoreCase("West")) {
	    	orientationTag = "W";
	    }
	    else if(dataCollActivity.orientation.equalsIgnoreCase("East")) {
	    	orientationTag = "E";
	    }
  	    String locTag = dataCollActivity.locText.getText().toString() + "_" + orientationTag;
	    for (ScanResult result : results) {
	    	str = str + result.SSID + Parser.FILE_SEP + result.level + Parser.FILE_SEP + readingID + Parser.FILE_SEP + locTag + "\n";
	    }
	
	    //inc reading id
	    readingID++;
	    
	    //show message 
	    Toast t = Toast.makeText(dataCollActivity, "Reading number " + dataCollActivity.cReadingNumber + ":\n" +
	    		str, Toast.LENGTH_LONG);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
				
		//commit to file and update data model
	    FileOpAPI.commitToFile(str,FileOpAPI.LOG_FILE);
 	    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(dataCollActivity.getApplicationContext());
 	    Editor e =sharedPrefs.edit();
 	    e.putInt("READING_ID", readingID);
 	    e.commit();
	    
	    //update number of readings and redo scan until we have current number of readings.
	    if(dataCollActivity.cReadingNumber < dataCollActivity.numReadingsPerClick) {
		    dataCollActivity.cReadingNumber++;
		    dataCollActivity.wifi.startScan();
	    }
	    else {
	    	dataCollActivity.CLICKED = false;
	    }
	    
	  }
	}
}