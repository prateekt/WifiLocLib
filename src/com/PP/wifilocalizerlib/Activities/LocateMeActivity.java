package com.PP.wifilocalizerlib.Activities;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.PP.wifilocalizerlib.data.FileOpAPI;
import com.PP.wifilocalizerlib.math.Model;
import com.PP.wifilocalizerlibrary.R;

public abstract class LocateMeActivity extends BaseWifiActivity {
	
	//fields
	protected BroadcastReceiver receiver;
    protected Model model;
	
	/**
	 * On Create
	 */
	public void onCreate(Bundle savedInstanceState) {
		
		//super
		super.onCreate(savedInstanceState);
		
		//init model
		model = FileOpAPI.readModel(FileOpAPI.MODEL_FILE);
		
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }   
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	    	
        // Handle menu item selection
    	if(item.getItemId()==R.id.wifi_localizer_settings_menu_item) {
    		
    		//go to preference activity
    		Intent i = new Intent(this, WifiLocalizerPrefActivity.class);
    		i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    		startActivity(i);												
        	return true;
    		
    	}
    	
    	return true;
    }


	public void locateMe() {
		//starts scan
		wifi.startScan();
	}
	
	@Override
	public BroadcastReceiver getReceiver() {
		if(receiver==null) {
			receiver = new LocateMeReceiver(this);
		}
		return receiver;
	}
	
	
	public abstract void onLocationChanged(String location);
	
}