package control;

public interface ControlAxis extends Control {
	
	public float position();
	
	public float min();
	
	public float max();
	
	public void setRange(float min, float mid, float max);
	
	public void setSensitivity(float sensitivity);
	
}
