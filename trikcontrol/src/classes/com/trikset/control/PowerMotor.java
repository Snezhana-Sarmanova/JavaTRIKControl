package com.trikset.control;

public class PowerMotor{
	
	private I2cCommunicator communicator;
	private int i2cCommandNumber;
	private boolean invert;
	private int currentPower;
	
	public PowerMotor(I2cCommunicator communicator, int i2cCommandNumber, boolean invert){
		this.communicator = communicator;
		this.i2cCommandNumber = i2cCommandNumber;
		this.invert = invert;
		this.currentPower = 0;
	}

	public void powerOff(){
		setPower(0);
	}

	public void setPower(int power)
	{
		if (power > 100) {
			power = 100;
		} else if (power < -100) {
			power = -100;
		}

		currentPower = power;

		power = invert ? -power : power;

		int[] command = new int[2];
		command[0] = (i2cCommandNumber & 0xFF);
		command[1] = (power & 0xFF);
		
		communicator.send(command);
	}

	public int power(){
		return currentPower;
	}
}
