package com.PP.wifilocalizerlib.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.PP.wifilocalizerlib.dialog.VoiceActionsDialog;
import com.PP.wifilocalizerlibrary.R;


public class WifiLocalizerPrefActivity extends PreferenceActivity {
	
	//on create
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
    	//theme
    	this.setTheme(android.R.style.Theme_Black);        
    	
    	//create
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment(this)).commit();
    }
    
    //fragment for preferences
    public static class MyPreferenceFragment extends PreferenceFragment
    {
    	protected Activity parent;
    	
    	public MyPreferenceFragment(Activity parent) {
    		this.parent = parent;
    	}

        @Override
        public void onCreate(final Bundle savedInstanceState)
        {        	
        	//create
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.wifi_pref);
            
            //set up collect data preference
            final Preference collectDataPreference = getPreferenceScreen().findPreference("collect_data_pref");
            if(collectDataPreference!=null) {
	            collectDataPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	                @Override
					public boolean onPreferenceClick(Preference preference) {
	                	
	                	//start new activity
	            		Intent i = new Intent(getActivity(), DataCollectionActivity.class);
	            		i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	            		startActivity(i);												
	                	return true;
	                }
	            });            
            }
            
            //set up train model preference
            final Preference trainModelPreference = getPreferenceScreen().findPreference("train_model_pref");
            if(trainModelPreference!=null) {
	            trainModelPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	                @Override
					public boolean onPreferenceClick(Preference preference) {
	                	
	                	//start new activity
	            		Intent i = new Intent(getActivity(), ModelBuildingActivity.class);
	            		i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	            		startActivity(i);												
	                	return true;
	                }
	            });            
            }

            //set up clear data preference
            final Preference clearDataPreference = getPreferenceScreen().findPreference("clear_data_pref");
            if(clearDataPreference!=null) {
	            clearDataPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	                @Override
					public boolean onPreferenceClick(Preference preference) {
	                	//show dialog
	                	VoiceActionsDialog d = new VoiceActionsDialog();
	                	d.show(parent.getFragmentManager(), "LOZ");
	                	return true;
	                }
	            });            
            }
            
        }
    }
}