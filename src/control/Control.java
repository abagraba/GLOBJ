package control;

import globj.core.Window;

public interface Control {
	
	public String name();
	
	public void update();
	
	public void setWindow(Window window);
}
