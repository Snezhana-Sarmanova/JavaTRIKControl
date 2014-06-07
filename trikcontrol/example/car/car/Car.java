package car;

import com.trikset.control.*; 

public class Car {
		
	private String i2cstr = "/dev/i2c-2";
	private I2cCommunicator i2c;
	
	private int FrontLeftMotorName = 0x17; //JM3
	private int FrontRightMotorName = 0x16; //M1
	private int BackLeftMotorName = 0x15; //JM2
	private int BackRightMotorName = 0x14; //JM1
	
	public PowerMotor FrontLeftMotor;
	public PowerMotor FrontRightMotor;
	public PowerMotor BackLeftMotor;
	public PowerMotor BackRightMotor;

	public int currentPower;
	
	private String FrontLeftServoMotorName = "/sys/class/pwm/ehrpwm.1:1";
	private String FrontRightServoMotorName = "/sys/class/pwm/ecap.1";
	private String BackLeftServoMotorName = "";
	private String BackRightServoMotorName = "";

	public ServoMotor FrontLeftServoMotor;
	public ServoMotor FrontRightServoMotor;
	public ServoMotor BackLeftServoMotor;
	public ServoMotor BackRightServoMotor;
	
	public void createCar(){
		
		i2c = new I2cCommunicator(i2cstr, 0x48);	
		i2c.connect();
		
		FrontLeftMotor = new PowerMotor(i2c, FrontLeftMotorName, false);
		FrontRightMotor = new PowerMotor(i2c, FrontRightMotorName, false);
		BackLeftMotor = new PowerMotor(i2c, BackLeftMotorName, false);
		BackRightMotor = new PowerMotor(i2c, BackRightMotorName, false);		
		
		currentPower = 0;
		
		FrontLeftServoMotor = new ServoMotor(1200000, 1800000, 1500000, 0, FrontLeftServoMotorName, 20000000, false);
		FrontRightServoMotor = new ServoMotor(1200000, 1800000, 1500000, 0, FrontRightServoMotorName, 20000000, true);
		BackLeftServoMotor = new ServoMotor(1200000, 1800000, 1500000, 0, BackLeftServoMotorName, 20000000, false);
		BackRightServoMotor = new ServoMotor(1200000, 1800000, 1500000, 0, BackRightServoMotorName, 20000000, true);
		
	}
	
	public void drive(int power){
		currentPower = power;
	
		FrontLeftMotor.setPower(power);
		FrontRightMotor.setPower(power);
		BackLeftMotor.setPower(power);
		BackRightMotor.setPower(power);
	}
	
	public void setAngle(int angle){
		FrontLeftServoMotor.setPower(angle);
		FrontRightServoMotor.setPower(angle);
	}
	
	public void stopMotors(){
		currentPower = 0;
		FrontLeftMotor.powerOff();
		FrontRightMotor.powerOff();
		BackLeftMotor.powerOff();
		BackRightMotor.powerOff();
	}
	
	public void stopServoMotors(){
		FrontLeftServoMotor.powerOff();
		FrontRightServoMotor.powerOff();
		BackLeftServoMotor.powerOff();
		BackRightServoMotor.powerOff();
	}

	public void destroyCar(){
		stopMotors();
		stopServoMotors();
		i2c.disconnect();
	}

}
