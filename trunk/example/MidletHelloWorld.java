import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class Hello extends MIDlet{
  private Form form;
  private Display display;

  public Hello(){
 	 super();
  }

  public void startApp(){
  	form = new Form("Hello World");
  	String msg = "Hello World!!!!!!!";
  	form.append(msg);
  	display = Display.getDisplay(this);
 	display.setCurrent(form);

	destroyApp(true);
  }

  public void pauseApp(){}
  
  public void destroyApp(boolean unconditional){
 	notifyDestroyed();
  }
}
