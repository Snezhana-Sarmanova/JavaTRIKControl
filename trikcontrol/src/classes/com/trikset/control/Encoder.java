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
		int[] command = new int[2];
		command[0] = (i2cCommandNumber & 0xFF);
		command[1] = (0x00);

		communicator.send(command);
	}

	public double read(){
		int[] command = new int[2];
		command[0] = (i2cCommandNumber & 0xFF);
		int data = communicator.read(command);

		return parToRad * data;
	}
}
