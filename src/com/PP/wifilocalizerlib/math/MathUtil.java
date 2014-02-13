package com.PP.wifilocalizerlib.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.net.wifi.ScanResult;

import com.PP.wifilocalizerlib.data.ParserResult;


public class MathUtil {
	
	/*
	 * Returns means and stds for display in table.
	 */
	public static StatPair computeStats(ParserResult res) {
		
		//maintain sums and number of readings support
		int numAPs = res.getNumAPs();
		int numLocs = res.getNumLocs();
		double[][] sums = new double[numAPs][numLocs];
		double[][] numberOfReadings = new double[numAPs][numLocs];
		double[][] sumsOfSquares = new double[numAPs][numLocs];
		double[][] medians = new double[numAPs][numLocs];
		
		//go through data and update
		for(int x=0; x < res.getRssi().length; x++) {
			int trueLocOfReading = res.getLocIDs()[x];
			for(int y=0; y < res.getRssi()[x].length;y++) {
				if(res.getRssi()[x][y]!=0) {
					sums[y][trueLocOfReading] = sums[y][trueLocOfReading] + res.getRssi()[x][y];
					sumsOfSquares[y][trueLocOfReading] = sumsOfSquares[y][trueLocOfReading] + Math.pow(res.getRssi()[x][y],2);
					numberOfReadings[y][trueLocOfReading]++;
				}
			}			
		}
		
		//compute medians 
		for(int x=0; x < res.getNumAPs(); x++) {
			for(int y=0; y < res.getNumLocs(); y++){
				List<Double> data = new ArrayList<Double>();
				for(int z=0; z < res.getRssi().length; z++) {
					int trueLocOfReading = res.getLocIDs()[z];
					if(trueLocOfReading==y && res.getRssi()[z][x]!=0) {
						data.add(res.getRssi()[z][x]);
					}
				}
				medians[x][y] = MathUtil.median(data);
			}
		}
			
		//take average and std
		double[][] means = new double[numAPs][numLocs];
		double[][] stds = new double[numAPs][numLocs];				
		for(int x=0; x < numAPs; x++) {
			for(int y=0; y < numLocs; y++) {
				means[x][y] = sums[x][y] / numberOfReadings[x][y];
				stds[x][y] = Math.sqrt((sumsOfSquares[x][y] / numberOfReadings[x][y]) - Math.pow(means[x][y],2));
			}
		}		
		
		//return
		StatPair rtn = new StatPair(medians,stds);
		return rtn;		
	}
	
	/**
	 * Compute median values
	 * @param values
	 * @return
	 */
	public static double median(List<Double> values)
	{
		//bad value if empty
		if(values.isEmpty()) {
			return Double.NaN;
		}
		
		//sort
		Collections.sort(values);
				
		//return median value
	    if (values.size() % 2 == 1)
			return values.get((values.size()+1)/2-1);
	    else
	    {
			double lower = values.get(values.size()/2-1);
			double upper = values.get(values.size()/2);		 
			return (lower + upper) / 2.0;
	    }	
	}
	
	/**
	 * Finger printing algorithm
	 * @param model -- learned model
	 * @param aps -- list of APs in current observation
	 * @param obsRSSI -- RSSI signal strengths of current observation
	 * @return
	 */
	public static String fingerprint(Model m, List<List<ScanResult>> results) {
		
		//get rssi table and locs
		double[][] rssi = m.getRssi();
		String[] locs = m.getLocs();
		int numAPs = m.getNumAPs();
		String[] apNameSet = m.getApNameSet();
		
		//create rssi vector
		double[] rssiVect = new double[numAPs];		
		for(int x=0; x < numAPs; x++) {
			
			//get data for particular ap
			List<Double> data = new ArrayList<Double>();
			String ap = apNameSet[x];

			//iterate through scan results and see where 
			for(int y=0; y < results.size(); y++) {
				List<ScanResult> scan = results.get(y);
				for(int z=0; z < scan.size(); z++) {
					ScanResult res = scan.get(z);
					String apCurrent = res.SSID;
					double obsRSSI = res.level;
					if(apCurrent.equalsIgnoreCase(ap)) {
						data.add(obsRSSI);
						break; //speed up - ap only happens once in scan result
					}
				}
			}
			
			//store median in vector
			rssiVect[x] = MathUtil.median(data);
		}
		
		//least squares
		double minErr = Double.MAX_VALUE;
		int bestIndex = -1;
		for(int x=0; x < rssi.length; x++) {
			double cErr = 0;
			for(int y=0; y < rssi[x].length; y++) {
				cErr = cErr + Math.pow((rssi[x][y]-rssiVect[y]),2);
			}
			if(cErr < minErr) {
				minErr = cErr;
				bestIndex = x;
			}
		}
		
		//return best index
		if(bestIndex==-1) {
			return null; //should never happen
		}
		else {
			return locs[bestIndex]; //return best label
		}
	}
}
