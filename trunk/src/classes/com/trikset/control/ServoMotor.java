package com.trikset.control;	

public class ServoMotor{
	
	private final int Min;
	private final int Max;
	private final int Zero;
	private final int Stop;
	private final String file;
	private int Period;
	private final boolean Invert;	
	private int CurrentPower;
	private int CurrentDutyPercent;
	private int Frequency;
	
	private native void init0(int period,  String file);
	private native void setPower0(int power, String file);
	private native void powerOff0(String file);
	
	public ServoMotor(int min, int max, int zero, int stop, String file, int period, boolean invert){
		this.Min = min;
		this.Max = max;
		this.Zero = zero;
		this.Stop = stop;
		this.file = file;
		this.Period = period;
		this.Invert = invert;
		this.CurrentPower = 0;
		this.CurrentDutyPercent = 0;
		this.Frequency = 1000000000 / Period;
		
		init0(period, file);
	}
	
	public void setPower(int power){
		
		if (power > 100) {
			power = 100;
		} else if (power < -100) {
			power = -100;
		}

		CurrentPower = power;

		power = Invert ? -power : power;

		int range = power <= 0 ? Zero - Min : Max - Zero;
		double powerFactor = (double) (range) / 100;
		int duty = (int)(Zero + power * powerFactor);	
			
		CurrentDutyPercent = 100 * duty / Period;	
		
		setPower0(duty, file);
	}
	
	public int power(){
		return CurrentPower;
	}
	
	public int frequency(){
		return Frequency;
	}
	
	public int duty(){
		return CurrentDutyPercent;
	}
	public void powerOff(){
		powerOff0(file);			
		CurrentPower = 0;
	}	
}
