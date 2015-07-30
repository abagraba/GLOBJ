package control;


import java.util.ArrayList;
import java.util.List;



public class ControlSet {
	
	public final List<Control>	controls	= new ArrayList<Control>();
	
	
	public void register(Control control) {
		controls.add(control);
	}
	
}
