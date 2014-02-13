package com.PP.wifilocalizerlib.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.PP.wifilocalizerlib.data.FileOpAPI;
import com.PP.wifilocalizerlibrary.R;

public class VoiceActionsDialog extends DialogFragment {
			
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.confirm_clear_data_message)
        	   .setTitle(R.string.confirm_clear_data)
               .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	        		@Override
					public void onClick(DialogInterface dialog, int id) {
	        			
	        			//clear data
	        			FileOpAPI.clearData();
		            }
	        		
        	})
           .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int id) {}
            });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}