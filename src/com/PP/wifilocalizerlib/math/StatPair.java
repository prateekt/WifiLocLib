package com.PP.wifilocalizerlib.math;

public class StatPair {
	
	protected double[][] means;
	protected double[][] stds;
	
	public StatPair(double[][] means, double[][] stds) {
		this.means = means;
		this.stds = stds;
	}

	/**
	 * @return the means
	 */
	public double[][] getMeans() {
		return means;
	}

	/**
	 * @param means the means to set
	 */
	public void setMeans(double[][] means) {
		this.means = means;
	}

	/**
	 * @return the stds
	 */
	public double[][] getStds() {
		return stds;
	}

	/**
	 * @param stds the stds to set
	 */
	public void setStds(double[][] stds) {
		this.stds = stds;
	}
	
	

}
