package com.trikset.control;

import java.io.*;
import javax.microedition.io.*;

public class Gamepad implements Runnable {

	private int port;
    private SocketConnection sc;
    private ServerSocketConnection scn;
    private IGamepad ig;
	
	public Gamepad(int port, IGamepad ig){
		this.port = port;	
		this.ig = ig;	
	}
	
	public void connect(){				
		Thread t = new Thread(this);
		System.out.println("Create thread");
		t.start();
	}
	
	public void run(){
		try {
		  scn = (ServerSocketConnection) Connector.open("socket://:"+port);
		  System.out.println("Connection!...");
		  SocketConnection sc = null;
		  while (true) {
			System.out.println("Waiting...");
			sc = (SocketConnection) scn.acceptAndOpen();
			System.out.println("New connection");

		    InputStream dis = sc.openDataInputStream();
			int ch = 0; 
			StringBuffer sb = new StringBuffer();
			int i = 0;
			
			while(i++ < 8) { 
				ch = dis.read();
				sb.append((char)ch);
			} 
			String result=sb.toString();
			parse(result);
		
			sc.close();
		  }
		} catch (Exception e) {		  
		}
	}
	
	public void parse(String message){
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
