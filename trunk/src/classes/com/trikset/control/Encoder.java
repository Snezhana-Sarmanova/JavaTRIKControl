package com.trikset.control;

public class Encoder{
	
	private final double parToRad = 0.03272492;

	private I2cCommunicator communicator;
	private int i2cCommandNumber;

	public Encoder(I2cCommunicator communicator, int i2cCommandNumber){
		this.communicator = communicator;
		this.i2cCommandNumber = i2cCommandNumber;
	}

	public void reset()
	{
		char[] command = new char[2];
		command[0] = (char)(i2cCommandNumber & 0xFF);
		command[1] = (char)(0x00);

		communicator.send(command);
	}

	public double read(){
		char[] command = new char[2];
		command[0] = (char)(i2cCommandNumber & 0xFF);
		int data = communicator.read(command);

		return parToRad * data;
	}
}
