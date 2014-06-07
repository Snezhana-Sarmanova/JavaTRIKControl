package com.trikset.control;

import java.util.Arrays;
import java.util.Random;
import java.io.*;

public class Sensor3d implements Runnable {

    private int[] array;
    private boolean running = true;
    private Thread thread;
    private String eventFile;
    
    public Sensor3d(String eventFile) {
	this.eventFile = eventFile;
	thread = new Thread(this);
	thread.start();
    }     
      
    public void run(){
		while(running){							
			try{	
				array = readFile();
				Thread.sleep(400);
			}catch(IOException e){								
				running = false;
				System.err.println(e.getMessage());
			}catch (InterruptedException ex) {
                System.err.println("InterruptedException");
            }			
		}
	}  

    private synchronized int[] readFile() throws IOException {		
		int[] a = read(eventFile); 
		if (a == null || (a[0] == 0 && a[1] == 0 && a[2] == 0))  { 
			throw new IOException("File descriptor of sensor not exist");			
		} 
		return a;
    }
    
    public void disconnect(){
		running = false;
	}
    public int[] getData(){
		while(array == null){System.out.println("in while");}
		return array;
	}
    
     /*return vector (x,y,z) for sensor*/
    private synchronized static native int[] read(String eventFile);
    
}
