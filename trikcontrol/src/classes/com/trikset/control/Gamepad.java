package com.trikset.control;

import java.io.*;
import javax.microedition.io.*;

public class Gamepad implements Runnable {

	private int port;
    	private SocketConnection sc;
    	private ServerSocketConnection scn;
    	private IGamepad ig;
	private boolean flag;
	
	public Gamepad(int port, IGamepad ig){
		this.port = port;	
		this.ig = ig;	
	}
	
	public void connect(){	
		flag = true;			
		Thread t = new Thread(this);
		System.out.println("Create thread");
		t.start();
	}
	public void disconnect(){				
		flag = false;
	}
	
	public void run(){
		try {
		  scn = (ServerSocketConnection) Connector.open("socket://:"+port);
		  System.out.println("Connection!...");
		  SocketConnection sc = null;

		  while (flag) {
			System.out.println("Waiting...");
			sc = (SocketConnection) scn.acceptAndOpen();
			System.out.println("New connection");

		    	InputStream dis = sc.openDataInputStream();
			
			int ch; 
			StringBuffer sb;
			
			while(flag) {
				ch = 0;
				sb = new StringBuffer();

				while((ch = dis.read()) != '\n'){		 
					sb.append((char)ch);
				}

				String result = sb.toString();
				parse(result);
			} 

			sc.close();
			dis.close();
		  }
		} catch (Exception e) {		  
		}
	}
	
	private void parse(String message){
		String[] cmd = String.split(message, " ");		
		String commandName = cmd[0];

		if (commandName.equals("pad")){
			int padId = Integer.parseInt(cmd[1]);
			if (cmd[2].equals("up")) {
				ig.padUp(padId);
			} else {
				int x = Integer.parseInt(cmd[2]);
				int y = Integer.parseInt(cmd[3]);
				ig.pad(padId, x, y);
			}
		} else if (commandName.equals("btn")) {
			int buttonCode = Integer.parseInt(cmd[1]);
			ig.button(buttonCode, 1);
		} else if (commandName.equals("wheel")) {
			int perc = Integer.parseInt(cmd[1]);
			ig.wheel(perc);
		} else {
			System.out.println("Unknown command");
		}
	}
}
