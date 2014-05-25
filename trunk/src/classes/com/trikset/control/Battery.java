package com.trikset.control;

public class Battery{

	private I2cCommunicator communicator;
	
	public Battery(I2cCommunicator communicator){
		this.communicator = communicator;
	}

	public double readVoltage(){
		char[] command = new char[1];
		command[0] = (char)(0x26);

		int parrot = communicator.read(command);

		return ((double)(parrot) / 1023.0) * 3.3 * (7.15 + 2.37) / 2.37;
	}
}
