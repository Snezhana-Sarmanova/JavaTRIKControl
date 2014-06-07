package com.trikset.control;

public interface IGamepad {
	
	public void padUp(int pad);
	public void wheel(int percent);
	public void pad(int pad, int x, int y);
	public void button(int button, int pressed);	
}
