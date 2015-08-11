package control;


import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import globj.core.Window;



public class KeyboardToggle implements ControlState {
	
	private boolean	lastValue;
	private boolean	state;
	
	private final String	name;
	private final int		key;
	private Window			window;
	
	public KeyboardToggle(String name, int key) {
		this.name = name;
		this.key = key;
	}
	
	@Override
	public String name() {
		return name;
	}
	
	@Override
	public boolean state() {
		return state;
	}
	
	@Override
	public void update() {
		boolean down = GLFW.glfwGetKey(window.window(), key) != GL11.GL_FALSE;
		if (down && !lastValue)
			state = !state;
		lastValue = down;
	}
	
	@Override
	public void setWindow(Window window) {
		this.window = window;
	}
}
