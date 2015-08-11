package control;


import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import globj.core.Window;



public class KeyboardAxis implements ControlAxis {
	
	private final String	name;
	private final int		lowKey, highKey;
	private float			min			= -1;
	private float			mid			= 0;
	private float			max			= 1;
	private float			sensitivity	= 1;
	private float			position	= 0;
	
	private Window window;
	
	public KeyboardAxis(String name, int lowKey, int highKey, float sensitivity) {
		this.name = name;
		this.lowKey = lowKey;
		this.highKey = highKey;
		this.sensitivity = sensitivity;
	}
	
	public KeyboardAxis(String name, int lowKey, int highKey) {
		this(name, lowKey, highKey, 1);
	}
	
	@Override
	public String name() {
		return name;
	}
	
	@Override
	public float position() {
		return position * sensitivity;
	}
	
	@Override
	public float min() {
		return min;
	}
	
	@Override
	public float max() {
		return max;
	}
	
	@Override
	public void setRange(float min, float mid, float max) {
		this.min = min;
		this.max = max;
		this.mid = mid;
	}
	
	@Override
	public void setSensitivity(float sensitivity) {
		this.sensitivity = sensitivity;
	}
	
	@Override
	public void update() {
		boolean low = GLFW.glfwGetKey(window.window(), lowKey) != GL11.GL_FALSE;
		boolean high = GLFW.glfwGetKey(window.window(), highKey) != GL11.GL_FALSE;
		if (low)
			position = high ? mid : min;
		else
			position = high ? max : mid;
	}
	
	@Override
	public void setWindow(Window window) {
		this.window = window;
	}
}
