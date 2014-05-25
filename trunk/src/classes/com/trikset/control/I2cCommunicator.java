package com.trikset.control;

public class I2cCommunicator{

	private String devicePath;
	private int deviceId;
	private int deviceFileDescriptor;
	private static final Object MUTEX = new Object();
		
	private synchronized static native void send0(char[] data, int deviceFileDescriptor);
	private synchronized static native int read0(char[] data, int deviceFileDescriptor);
	private synchronized static native int connect0(String devicePath, int deviceId);
	private synchronized static native void disconnect0(int deviceFileDescriptor);

	public I2cCommunicator(String devicePath, int deviceId) {
		this.devicePath = devicePath;
		this.deviceId = deviceId;		
	}
	
	public void connect(){
		deviceFileDescriptor = connect0(devicePath,deviceId);
	}
	
	public void disconnect(){
		synchronized (MUTEX) {
			disconnect0(deviceFileDescriptor);
		}
	}

	public void send(char[] data){
		synchronized (MUTEX) {
			send0(data, deviceFileDescriptor);
		}
	}
	
	public int read(char[] data){
		synchronized (MUTEX) {
			int value = read0(data, deviceFileDescriptor);
			return value;
		}
	}	
}
