package com.trikset.control;

public class Led {
	
	private String mRedDeviceFile;
	private String mGreenDeviceFile;
	private int mOn;
	private int moff;

	public Led(String redDeviceFile, String greenDeviceFile, int on, int off) {
		mRedDeviceFile = redDeviceFile;
		mGreenDeviceFile = greenDeviceFile;
		mOn = on;
		moff = off;
	}

	public void disconnect()
	{
		red();
	}

	public void red()
	{
		off();

		String command = String.valueOf(mOn);		
		write0(mRedDeviceFile, command);
	}

	public void green()
	{
		off();

		String command = String.valueOf(mOn);
		write0(mGreenDeviceFile, command);
	}

	public void orange()
	{
		String command = String.valueOf(mOn);

		write0(mRedDeviceFile, command);
		write0(mGreenDeviceFile, command);		
	}

	public void off()
	{
		String command = String.valueOf(moff);

		write0(mRedDeviceFile, command);
		write0(mGreenDeviceFile, command);	
	}
	
	private synchronized static native void write0(String file, String command);
}
