package com.PP.wifilocalizerlib.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
	
	//file separator
	public static String FILE_SEP = ",";
	
	public static ParserResult parse(List<String> fileLines) {
		
		//nothing to return case
		if(fileLines.size()==0) {
			return null;
		}
		
		//figure out number of readings
		String lastReading = fileLines.get(fileLines.size()-1);
		String[] toks = lastReading.split(FILE_SEP);
		int numReadings = Integer.parseInt(toks[2]);
		
		//figure out set of APs and set of locations
		Map<String,Integer> apMap = new HashMap<String,Integer>();
		Map<String,Integer> locMap = new HashMap<String,Integer>();
		int locID=0;
		int apID=0;
		for(int x=0; x < fileLines.size(); x++) {
			toks = fileLines.get(x).split(FILE_SEP);
			String ap = toks[0];
			if(!apMap.containsKey(ap)) {
				apMap.put(ap, apID);
				apID++;
			}
			String loc = toks[3];
			if(!locMap.containsKey(loc)) {
				locMap.put(loc, locID);
				locID++;
			}
		}
		int numAPs = apID;
				
		//init table and locs for full read
		double[][] rssi = new double[numReadings][numAPs];
		String[] locs = new String[numReadings];
		int[] locIDs = new int[numReadings];
		
		//read
		for(int x=0; x < fileLines.size(); x++) {
			
			//parse
			toks = fileLines.get(x).split(FILE_SEP);
			int readingNumber = Integer.parseInt(toks[2]);
			String loc = toks[3];
			String apName =toks[0];
			int idOfAP = apMap.get(apName);
			double rssiValue = Double.parseDouble(toks[1]);
			
			//store
			rssi[readingNumber-1][idOfAP] = rssiValue;
			locs[readingNumber-1] = loc;
			locIDs[readingNumber-1] = locMap.get(loc);
		}
				
		//return
		ParserResult rtn = new ParserResult(rssi,locs,locIDs,apMap,locMap);
		return rtn;
	}	
}
