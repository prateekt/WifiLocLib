package com.PP.wifilocalizerlib.Activities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.PP.wifilocalizerlib.data.FileOpAPI;
import com.PP.wifilocalizerlib.data.Parser;
import com.PP.wifilocalizerlib.data.ParserResult;
import com.PP.wifilocalizerlib.math.MathUtil;
import com.PP.wifilocalizerlib.math.Model;
import com.PP.wifilocalizerlib.math.StatPair;

public class ModelBuildingActivity extends Activity {
	
	   //GUI font sizes
  	   public static final int TITLE_SIZE = 18;
  	   public static final int LABEL_SIZE = 10;
  	   public static final int ELEM_SIZE = 10;
  	   public static final int CB_PER_ROW = 3;
  	   
  	   //file names for model and actual Model object
  	   protected Model model = null;
  	   
  	   //dynamic button
  	   protected Button buildButton;
  	    
  	   //dynamic view
  	   protected View currentView = null; 
  	   protected CheckBox[] checkBoxes = null;
  	   protected ParserResult pResult = null;
  	   
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
		   
	    	//theme
	    	this.setTheme(android.R.style.Theme_Black);        
		   
		    //super
	        super.onCreate(savedInstanceState);
	        
			//set up file system
			FileOpAPI.init();
	        
	        //init model (or at least attempt to) if need to
			if(model==null) {
		        model = FileOpAPI.readModel(FileOpAPI.MODEL_FILE);
			}
	        
	        //init dynamic button
	        buildButton = new Button(this);
	        buildButton.setText("Build Model");
	        buildButton.setOnClickListener(new View.OnClickListener() {
	            @Override
				public void onClick(View view) {
	            	
	            	//figure out which check boxes have been clicked
	            	List<String> checkboxesChecked = new ArrayList<String>();
	            	for(int x=0; x < checkBoxes.length; x++) {
	            		if(checkBoxes[x].isChecked()) {
	            			String apName = checkBoxes[x].getText().toString();
		            		checkboxesChecked.add(apName);
	            		}
	            	}
	            	
	            	//if nothing checked, return.
	            	if(checkboxesChecked.isEmpty()) {
	        			Toast t = Toast.makeText(view.getContext(), "No access points selected. Please select some access points.",Toast.LENGTH_SHORT);
	        			t.setGravity(Gravity.CENTER, 0, 0);
	        			t.show();	            		
	            		return;
	            	}
	            	
	            	//get APs to keep
	            	Map<String,Integer> aps = pResult.getApMap();
	            	int[] apsToKeep = new int[checkboxesChecked.size()];
	            	for(int x=0; x < checkboxesChecked.size(); x++) {
	            		int apId = aps.get(checkboxesChecked.get(x));
	            		apsToKeep[x] = apId;
	            	}
	            		            	
	            	//build model and serialize to file
	            	model = new Model(pResult,apsToKeep);
	            	FileOpAPI.writeModel(model,FileOpAPI.MODEL_FILE);
	            	
	            	//say model built
        			Toast t = Toast.makeText(view.getContext(), "Model successfully built.",Toast.LENGTH_LONG);
        			t.setGravity(Gravity.CENTER, 0, 0);
        			t.show();	            		
        			
	            }
	        });	        
	   }
	   
	   /**
	    * Gets called when activity gets focus.
	    */
	   @Override
	   public void onResume() {
		   
		   //super
		   super.onResume();
		   
		   //read the file
		   List<String> fileLines = FileOpAPI.readFile(FileOpAPI.LOG_FILE);
		   
		   //parse its contents
		   ParserResult r = Parser.parse(fileLines);		   
		   if(r==null || r.getNumAPs()==0 || r.getNumLocs()==0) {
				Toast t = Toast.makeText(this, "No data available. You must first collect data to build models.",Toast.LENGTH_SHORT);
				t.setGravity(Gravity.CENTER, 0, 0);
				t.show();			   
			   return; //easy exit
		   }
		   
		   //compute statistics
		   StatPair stat = MathUtil.computeStats(r);
		   
		   //make necessary layouts
		   String[] rows = r.getApNameSet();
		   String[] cols = r.getLocNameSet();
		   double[][] means = stat.getMeans();
		   double[][] stds = stat.getStds();
	       TableLayout table = makeTable(rows,cols,means,stds);
	       TableLayout cbs = makeCheckboxLayout(rows,CB_PER_ROW);
	       
	       //remove old view (if exists)
	       if(currentView!=null) {
	    	   ViewGroup parent = (ViewGroup) currentView.getParent();
	    	   parent.removeAllViews();
	    	   parent = (ViewGroup) buildButton.getParent();
	    	   parent.removeAllViews();
	       }
	       
	       //add new view
	       currentView = renderLayout(table,cbs);
	       setContentView(currentView);
	       pResult = r;
	   }
	   
	   /*
	    * Used to render the layout
	    */
	   public LinearLayout renderLayout(TableLayout table, TableLayout cbs) {
		   
		   //prepare
		   LinearLayout rtn = new LinearLayout(this);
		   rtn.setOrientation(LinearLayout.VERTICAL);
		   ScrollView v = new ScrollView(this);
		   v.addView(table);
		   TextView cbLabel = new TextView(this);
		   cbLabel.setText("\n\nSelect Access Points to keep: ");
		   ScrollView v2 = new ScrollView(this);
		   v2.addView(cbs);
		   
		   //add
		   LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                   LayoutParams.MATCH_PARENT,
                   0, 0.5f);
		   v.setLayoutParams(param1);
		   rtn.addView(v);		   
		   LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                   LayoutParams.MATCH_PARENT,
                   0, 0.1f);		   
		   cbLabel.setLayoutParams(param2);
		   rtn.addView(cbLabel);
		   LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(
                   LayoutParams.MATCH_PARENT,
                   0, 0.3f);		   
		   v2.setLayoutParams(param3);
		   rtn.addView(v2);
		   LinearLayout.LayoutParams param4 = new LinearLayout.LayoutParams(
                   LayoutParams.MATCH_PARENT,
                   0, 0.1f);		   		   
		   buildButton.setLayoutParams(param4);
		   rtn.addView(buildButton);
		   return rtn;
	   }
	   	   
	   /*
	    * Make the data table
	    */
	   public TableLayout makeTable(String[] rowLabels, String[] colLabels, double[][] means, double[][] stds) {
		   
		    //init table
	        TableLayout table = new TableLayout(this);
	        table.setStretchAllColumns(true);
	        table.setShrinkAllColumns(true);
	        
	        //title row
	        TableRow rowTitle = new TableRow(this);
	        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);
	        TextView title = new TextView(this);
	        title.setText("Median/Std Signal Strength");	        
	        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, TITLE_SIZE);
	        title.setGravity(Gravity.CENTER_HORIZONTAL);
	        title.setTypeface(Typeface.SERIF, Typeface.BOLD);
	        TableRow.LayoutParams params = new TableRow.LayoutParams();
	        params.span = 6;
	        rowTitle.addView(title, params);
	        table.addView(rowTitle);
	        
	        //AP label row	        
	        TableRow apLabelRow = new TableRow(this);
	        TextView empty = new TextView(this);
	        empty.setTextSize(TypedValue.COMPLEX_UNIT_DIP, LABEL_SIZE);
	        empty.setGravity(Gravity.CENTER_HORIZONTAL);
	        apLabelRow.addView(empty);
	        for(int x=0; x < colLabels.length; x++) {
	        	TextView apLabel = new TextView(this);
	        	apLabel.setText(colLabels[x]);
		        apLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, LABEL_SIZE);
		        apLabel.setGravity(Gravity.CENTER_HORIZONTAL);
		        apLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);	        	
		        apLabelRow.addView(apLabel);
	        }
	        table.addView(apLabelRow);
	        
	        //location rows	        
	        for(int x=0; x < rowLabels.length; x++) {
	        	TableRow locRow = new TableRow(this);
	        	TextView locLabel = new TextView(this);
	        	locLabel.setText(rowLabels[x]);
		        locLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, LABEL_SIZE);
		        locLabel.setGravity(Gravity.CENTER_HORIZONTAL);
		        locLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);	        	
		        locRow.addView(locLabel);
		        for(int y=0; y < means[x].length; y++) {
		        	TextView mean = new TextView(this);
		        	DecimalFormat df = new DecimalFormat("##.00");
		        	if(df.format(means[x][y]).indexOf("NaN")>-1) {
			        	mean.setText("Median: N/A\nStd: N/A");		        	
		        	}
		        	else {
			        	mean.setText("Median: "+df.format(means[x][y]) + "\nStd: " + df.format(stds[x][y]));		        	
		        	}
			        mean.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ELEM_SIZE);
			        mean.setGravity(Gravity.CENTER_HORIZONTAL);	
			        mean.setTypeface(Typeface.SERIF, Typeface.NORMAL);	        				        
		        	locRow.addView(mean);
		        }
		        table.addView(locRow);
	        }
	        return table;
	   }
	   
	   /*
	    * Layout the checkboxes
	    */
	   public TableLayout makeCheckboxLayout(String[] APs, int rowSize) {
		   
		   //parent layout
		   TableLayout rtn = new TableLayout(this);
		   
		   //make rows
		   TableRow cRow = new TableRow(this);
		   int cnt=0;
		   checkBoxes = new CheckBox[APs.length];
		   for(int x=0; x < APs.length; x++) {
			   
			   //make checkbox
			   CheckBox cb = new CheckBox(this);
			   cb.setText(APs[x]);
			   cb.setTextSize(ELEM_SIZE);
			   checkBoxes[x] = cb;
			   
			   //add old row reinit new row
			   if(cnt == rowSize) {
				   cnt =0;
				   rtn.addView(cRow);
				   cRow = new TableRow(this);
			   }
			   
			   //add cb
			   cRow.addView(cb);
			   cnt++;
			   
			   //add last row
			   if(x==(APs.length-1)) {
				   rtn.addView(cRow);
			   }
		   }
		   return rtn;
	   }
}