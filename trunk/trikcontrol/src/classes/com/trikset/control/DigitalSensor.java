package com.trikset.control;

import java.lang.Math;

public class DigitalSensor{

	public int digitalMax;  
	public int digitalMin;
	public String deviceFile;

	public DigitalSensor(int min, int max, String devFile){
		this.digitalMax = max;
		this.digitalMin = min;
		this.deviceFile = devFile;
	}	
	
	private synchronized static native int read0(String deviceFile);
	
	public synchronized int read(){
		int value = 0;
		value = read0(deviceFile);
		
		if (digitalMax == digitalMin) {
			return digitalMin;
		}		

		value = Math.min(value, digitalMax);
		value = Math.max(value, digitalMin);

		double scale = 100.0 / ((double)(digitalMax - digitalMin));

		value = (int)((value - digitalMin) * scale);
		return value;
	}
}
