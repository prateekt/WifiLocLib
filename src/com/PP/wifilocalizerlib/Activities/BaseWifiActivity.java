package com.PP.wifilocalizerlib.Activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.PP.wifilocalizerlib.data.FileOpAPI;

public abstract class BaseWifiActivity extends Activity {
	
	//fields
	protected WifiManager wifi;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		//set up file system
		FileOpAPI.init();
		
		// Setup WiFi
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifi.setWifiEnabled(true);
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//REGISTER
		if(getReceiver()!=null) {
			registerReceiver(getReceiver(), new IntentFilter(
					WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));		
		}
	}
	
	//callback for exit wifi
	@Override
	public void onStop() {
		super.onStop();
		try {
			if(getReceiver()!=null) {
				unregisterReceiver(getReceiver());
			}
		}
		catch(Exception e) {}
	}
		
	//callback for receiver
	public abstract BroadcastReceiver getReceiver();
	
}
