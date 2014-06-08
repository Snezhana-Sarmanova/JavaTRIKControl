package car;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

import com.trikset.control.*; 

public class MidletGamepad extends MIDlet implements IGamepad{

	public boolean flag = true;
	public int port = 4444;
	public Car car;
	public Gamepad gp;
	public int currentAngle;
	public int maxAngle = 60;
	
	public MidletGamepad(){
		super();
	}

	public void startApp(){
	
		car = new Car();
		car.createCar();
		currentAngle = 0;
		
		gp = new Gamepad(port, this);	
		gp.connect(); 
			
		flag = true;	

		while(flag){}
		destroyApp(true);
	}

	public void pauseApp(){}
	  
	public void destroyApp(boolean unconditional){		
		car.destroyCar();
		gp.disconnect();		
		notifyDestroyed();
	}
	  
/* -------------------------------------------------*/ 

	public void padUp(int pad){
		
		if(pad == 2){
			car.stopMotors();
			car.stopServoMotors();		
		}
	}
	  
	public void wheel(int percent){
		System.out.println("percent = " + percent);
	}

	public void pad(int pad, int x, int y){
		
		if (pad == 1){		 
            		if (y>=35 && x>=-45 && x<=45){
               			 //ехать прямо
				car.setAngle(0);
           	 	}
            		if (y>-45 && y<45 && x<-35){
            	    		//ехать влево
				if(currentAngle >= -maxAngle){
					currentAngle -= 20;
					car.setAngle(currentAngle);
				}
            		}
			if (y>-45 && y<45 && x>35){
                		//ехать вправо
				if(currentAngle <= maxAngle){
					currentAngle += 20;
					car.setAngle(currentAngle);
				}
            		}
           	 	if (y<=-35 && x<=45 && x>=-45){
                		//ехать назад
				car.setAngle(0);
            		}
        	}
		
		if (pad == 2){		 
            		car.drive(y);
        	}
	}
	  
	public void button(int button, int pressed){
		
		switch(button){
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				System.out.println("Gamepad stoped");
				flag = false;
		}
	}  
}
