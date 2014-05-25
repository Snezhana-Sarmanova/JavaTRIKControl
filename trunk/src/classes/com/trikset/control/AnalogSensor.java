package com.trikset.control;

import java.lang.Math;

public class AnalogSensor{

	private final int analogMax = 1024;  //for all analog sensors
	private final int analogMin = 0;  //for all analog sensors
	private I2cCommunicator communicator;
	private int i2cCommandNumber;


	public AnalogSensor(I2cCommunicator communicator, int i2cCommandNumber){
		this.communicator = communicator;
		this.i2cCommandNumber = i2cCommandNumber;
	}	
	
	public int read(){
		char[] command = new char[1];
		command[0] = (char)(i2cCommandNumber & 0xFF);
		int value = communicator.read(command);

		value = Math.min(value, analogMax);
		value = Math.max(value, analogMin);

		double scale = 100.0 / ((double)(analogMax - analogMin));

		value = (int)((value - analogMin) * scale);
		return value;
	}
}
