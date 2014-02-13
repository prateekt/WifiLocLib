package com.PP.wifilocalizerlib.data;

import java.util.Map;

public class ParserResult {
	
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
	 * Number of APs
	 */
	protected int numAPs;
	
	/*
	 * Number of Locations
	 */
	protected int numLocs;
	
	/*
	 * AP name set (in order of ID)
	 */
	protected String[] apNameSet;
	
	/*
	 * Loc name set (in order of ID)
	 */
	protected String[] locNameSet;
	
	/**
	 * Ctr
	 * @param rssi
	 * @param locs
	 * @param apMap
	 */
	public ParserResult(double[][] rssi, String[] locs, int[] locIDs, Map<String,Integer> apMap, Map<String,Integer> locMap) {
		this.rssi = rssi;
		this.locs = locs;
		this.locIDs = locIDs;
		this.apMap = apMap;
		this.locMap = locMap;
		
		//compute numbers
		numAPs = apMap.keySet().size();
		numLocs = locMap.keySet().size();
		
		//compute ap name set
		apNameSet = new String[numAPs];
		for(String ap : apMap.keySet()) {
			apNameSet[apMap.get(ap)] = ap;
		}
		
		//compute loc name set
		locNameSet = new String[numLocs];
		for(String loc : locMap.keySet()) {
			locNameSet[locMap.get(loc)] = loc;
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
}
