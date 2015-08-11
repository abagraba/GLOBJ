package control;


import java.util.ArrayList;
import java.util.List;



public class ControlSet {
	
	private final List<Control> controls = new ArrayList<Control>();
	
	public void register(Control control) {
		controls.add(control);
	}
	
	public void unregister(Control control) {
		controls.remove(control);
	}
	
	public List<Control> controls() {
		return controls;
	}
	
	public void setWindow(long window) {
		for (Control control : controls) {
			control.setWindow(window);
		}
	}
	
}
