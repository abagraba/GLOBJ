package control;

public class ControlManager {
	
	private static ControlSet	controlSet;
	
	
	private ControlManager() {
	}
	
	public static void select(ControlSet controlSet) {
		ControlManager.controlSet = controlSet;
	}
	
	public static ControlSet current() {
		return controlSet;
	}
	
}
