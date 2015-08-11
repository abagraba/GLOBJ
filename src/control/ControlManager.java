package control;


import globj.core.Window;



public class ControlManager {
	
	private static ControlSet controlSet;
	
	private ControlManager() {
	}
	
	public static void select(Window window, ControlSet controlSet) {
		ControlManager.controlSet = controlSet;
		controlSet.setWindow(window.window());
	}
	
	public static ControlSet current() {
		return controlSet;
	}
	
}
