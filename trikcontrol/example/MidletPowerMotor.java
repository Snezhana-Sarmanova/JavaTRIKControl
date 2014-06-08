import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import com.trikset.control.*; 

public class MidletPowerMotor extends MIDlet {

  public MidletPowerMotor(){
	super();
  }

  public void startApp(){	  
 	String i2cstr = "/dev/i2c-2";
	double valueEncoder; 
		
	I2cCommunicator i2c = new I2cCommunicator(i2cstr, 0x48);	
	i2c.connect();
		
	Encoder enc = new Encoder(i2c, 0x33);		
	enc.reset();
	PowerMotor pw = new PowerMotor(i2c, 0x16, false);
		
	pw.setPower(-40);

	try{	
		Thread.sleep(2000);
	}catch(Exception e){}
	
	pw.setPower(70);
			
	try {
   	 Thread.sleep(2000);
	} catch (Exception ex) {}

	valueEncoder = enc.read();	
		
	pw.powerOff();			
	i2c.disconnect();
	 
  	destroyApp(true);  	  
  }

  public void pauseApp(){}
  
  public void destroyApp(boolean unconditional){
	notifyDestroyed();
  }
}

