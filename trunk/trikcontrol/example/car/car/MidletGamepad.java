package car;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

import com.trikset.control.*; 

public class MidletGamepad extends MIDlet implements IGamepad{

	public boolean flag = true;
	public int port = 4444;
	public Car car;
	public Gamepad gp;
	
	public MidletGamepad(){
		super();
	}

	public void startApp(){
	
		car = new Car();
		car.createCar();
		
		gp = new Gamepad(port, this);	
		gp.connect();  
		
		destroyApp(true);
	}

	public void pauseApp(){}
	  
	public void destroyApp(boolean unconditional){
		gp.disconnect();
		car.destroyCar();		
		notifyDestroyed();
	}
	  
/* -------------------------------------------------*/ 

	public void padUp(int pad){
	    System.out.println("Pad = " + pad);
		
		if(pad == 2){
			car.stopMotors();
			car.stopServoMotors();
		}
	}
	  
	public void wheel(int percent){
		System.out.println("percent = " + percent);
	}

	public int currentAngle = 0;
	public int maxAngle = 40;
	
	public void pad(int pad, int x, int y){
		System.out.println("Pad = " + pad + " x=" + x + " y=" + y);
		
		 if (pad == 1){		 
            if (y>=35 && x>=-45 && x<=45){
                //ехать прямо
				car.setAngle(0);
            }
            if (y>-45 && y<45 && x<-35){
                //ехать влево
				if(currentAngle >= -maxAngle){
					car.setAngle(currentAngle - 15);
				}
            }
			if (y>-45 && y<45 && x>35){
                //ехать вправо
				if(currentAngle <= maxAngle){
					car.setAngle(currentAngle + 15);
				}
            }
            if (y<=-35 && x<=45 && x>=-45){
                //ехать назад
				car.setAngle(0);
            }
        }
	}
	  
	public void button(int button, int pressed){
		System.out.println("button = " + button + " pressed=" + pressed);
		
		switch(pressed){
			case 1:
				car.drive(20);
				break;
			case 2:
				car.drive(30);
				break;
			case 3:
				car.drive(40);
				break;
			case 4:
				car.drive(50);
				break;
			case 5:
				destroyApp(true);
				break;
		}
	}  
}