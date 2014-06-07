import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import java.io.*;

import com.trikset.control.*; 

public class MidletSensor3d extends MIDlet {

  public MidletSensor3d(){
		super();
  }

  public void startApp(){	  
	  getData();	  
  	  destroyApp(true);  	  
  }

  public void getData(){
		String ef = "/dev/input/by-path/platform-spi_davinci.1-event";       
        Sensor3d sensor = new Sensor3d(ef);

        int[] arrayData = sensor.getData();	
        sensor.disconnect();

		System.out.println("X = " + arrayData[0]);
		System.out.println("Y = " + arrayData[1]);
		System.out.println("Z = " + arrayData[2]); 
  }

  public void pauseApp(){}
  
  public void destroyApp(boolean unconditional){
		notifyDestroyed();
  }
}
