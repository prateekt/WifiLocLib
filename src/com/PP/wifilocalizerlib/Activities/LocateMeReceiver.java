package com.PP.wifilocalizerlib.Activities;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.view.Gravity;
import android.widget.Toast;

import com.PP.wifilocalizerlib.math.MathUtil;

public class LocateMeReceiver extends BroadcastReceiver {

   //my sending process
   protected LocateMeActivity parent;
   
   //my buffer for scan results
   public static List<List<ScanResult>> buffer = null;
   
   /**
    * Ctr
    * @param wifiDemo
    */
   public LocateMeReceiver(LocateMeActivity parent) {
	    super();
	    this.parent = parent;
   }
	
	@Override
	public void onReceive(Context c, Intent i) {
								
		//get scan results
	    List<ScanResult> results = parent.wifi.getScanResults();
		    
	    //if no results, easy exit.
	    if(results.size()==0) {
	    	
	    	//show message
			Toast t = Toast.makeText(c, "No Wi-Fi access points available.",Toast.LENGTH_SHORT);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();		
			
			//return
			return;
	    }
	    
	    //put result into buffer
	    buffer = new ArrayList<List<ScanResult>>();
	    buffer.add(results);
		    			    
	    //run finger-printing
	    String label = null;
	    if(parent.model!=null) {
		    label = MathUtil.fingerprint(parent.model,buffer);
		    label = label.substring(0,label.indexOf("_"));
	    }
		    
	    //call callback
	    parent.onLocationChanged(label);
	    
	}	
}