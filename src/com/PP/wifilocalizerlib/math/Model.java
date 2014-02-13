package com.PP.wifilocalizerlib.math;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.PP.wifilocalizerlib.data.ParserResult;

public class Model implements Serializable {

	/**
	 * Table of rssi values for reading by AP
	 */
	protected double[][] rssi;
	
	/**
	 * Actual locations data was collected
	 */
	protected String[] locs;
	
	/**
	 * Location ids instead of strings
	 */
	protected int[] locIDs;
	
	/**
	 * Mapping of AP --> id
	 */
	protected Map<String,Integer> apMap;
	
	/**
	 * Mapping of location to location ids
	 */
	protected Map<String, Integer> locMap;

	/*
	 * AP name set (in order of ID)
	 */
	protected String[] apNameSet;
	
	/*
	 * Loc name set (in order of ID)
	 */
	protected String[] locNameSet;
	
	/*
	 * Number of APs
	 */
	protected int numAPs;
	
	/*
	 * Number of Locations
	 */
	protected int numLocs;

	/**
	 * Build model
	 * @param pResult
	 * @param apsToKeep
	 * @return
	 */
	public Model(ParserResult pResult, int[] apsToKeep) {
		
		//ap name set and  new ap map
		apNameSet = new String[apsToKeep.length];
		apMap = new HashMap<String,Integer>();
		for(int x=0; x < apsToKeep.length; x++) {
			String apName = pResult.getApNameSet()[apsToKeep[x]];
			apNameSet[x] = apName;
			apMap.put(apName, x);
		}
		numAPs = apsToKeep.length;
		
		//loc name set and loc map
		locNameSet = pResult.getLocNameSet();
		locMap = pResult.getLocMap();
		numLocs = pResult.getNumLocs();
		
		//make model
		rssi = new double[numLocs][numAPs];
		locs = new String[numLocs];
		locIDs = new int[numLocs];
		for(int x=0; x < numLocs; x++) {
			for(int y=0; y < numAPs; y++) {
				
				//get relevant data in original table
				List<Double> relData = new ArrayList<Double>();
				int numReadingsOld = pResult.getRssi().length;
				int[] locIDsOld = pResult.getLocIDs();
				for(int z=0; z < numReadingsOld; z++) {
					if(locIDsOld[z]==x) {
						relData.add(pResult.getRssi()[z][apsToKeep[y]]);
					}
				}
				
				//take median
				rssi[x][y] = MathUtil.median(relData);
				
			}
			locs[x] = locNameSet[x];
			locIDs[x] = x;
		}
	}	
	
	/**
	 * @return the rssi
	 */
	public double[][] getRssi() {
		return rssi;
	}

	/**
	 * @param rssi the rssi to set
	 */
	public void setRssi(double[][] rssi) {
		this.rssi = rssi;
	}

	/**
	 * @return the locs
	 */
	public String[] getLocs() {
		return locs;
	}

	/**
	 * @param locs the locs to set
	 */
	public void setLocs(String[] locs) {
		this.locs = locs;
	}

	/**
	 * @return the locIDs
	 */
	public int[] getLocIDs() {
		return locIDs;
	}

	/**
	 * @param locIDs the locIDs to set
	 */
	public void setLocIDs(int[] locIDs) {
		this.locIDs = locIDs;
	}

	/**
	 * @return the apMap
	 */
	public Map<String, Integer> getApMap() {
		return apMap;
	}

	/**
	 * @param apMap the apMap to set
	 */
	public void setApMap(Map<String, Integer> apMap) {
		this.apMap = apMap;
	}

	/**
	 * @return the locMap
	 */
	public Map<String, Integer> getLocMap() {
		return locMap;
	}

	/**
	 * @param locMap the locMap to set
	 */
	public void setLocMap(Map<String, Integer> locMap) {
		this.locMap = locMap;
	}

	/**
	 * @return the apNameSet
	 */
	public String[] getApNameSet() {
		return apNameSet;
	}

	/**
	 * @param apNameSet the apNameSet to set
	 */
	public void setApNameSet(String[] apNameSet) {
		this.apNameSet = apNameSet;
	}

	/**
	 * @return the locNameSet
	 */
	public String[] getLocNameSet() {
		return locNameSet;
	}

	/**
	 * @param locNameSet the locNameSet to set
	 */
	public void setLocNameSet(String[] locNameSet) {
		this.locNameSet = locNameSet;
	}

	/**
	 * @return the numAPs
	 */
	public int getNumAPs() {
		return numAPs;
	}

	/**
	 * @param numAPs the numAPs to set
	 */
	public void setNumAPs(int numAPs) {
		this.numAPs = numAPs;
	}

	/**
	 * @return the numLocs
	 */
	public int getNumLocs() {
		return numLocs;
	}

	/**
	 * @param numLocs the numLocs to set
	 */
	public void setNumLocs(int numLocs) {
		this.numLocs = numLocs;
	}	
}
