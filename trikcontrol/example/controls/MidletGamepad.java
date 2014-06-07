import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

import com.trikset.control.*; 

public class MidletGamepad extends MIDlet implements IGamepad{

  public MidletGamepad(){
	super();
  }

  public void startApp(){
	  int port = 4444;
	  Gamepad gp = new Gamepad(port, this);	
	  gp.connect();  
	  
	  while(true){}  	  
  }

  public void pauseApp(){}
  
  public void destroyApp(boolean unconditional){
	notifyDestroyed();
  }
  
  //@Override
  public void padUp(int pad){
	  System.out.println("Pad = " + pad);
  }
  
  //@Override
  public void wheel(int percent){
	  System.out.println("percent = " + percent);
  }
  
  //@Override
  public void pad(int pad, int x, int y){
	  System.out.println("Pad = " + pad + " x=" + x + " y=" + y);
  }
  
  //@Override
  public void button(int button, int pressed){
	  System.out.println("button = " + button + " pressed=" + pressed);
  }  
}

