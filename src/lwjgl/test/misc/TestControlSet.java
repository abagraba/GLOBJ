package lwjgl.test.misc;


import org.lwjgl.glfw.GLFW;

import control.ControlSet;
import control.KeyboardAxis;
import control.KeyboardPress;
import control.KeyboardToggle;



public class TestControlSet extends ControlSet {
	public static final KeyboardAxis	UD	= new KeyboardAxis("Up-Down Axis", GLFW.GLFW_KEY_DOWN, GLFW.GLFW_KEY_UP, 0.001f);
	public static final KeyboardAxis	LR	= new KeyboardAxis("Left-Right Axis", GLFW.GLFW_KEY_LEFT, GLFW.GLFW_KEY_RIGHT, 0.001f);
	
	public static final KeyboardToggle SPACE = new KeyboardToggle("Space", GLFW.GLFW_KEY_SPACE);
	
	public static final KeyboardPress	ESC	= new KeyboardPress("Escape", GLFW.GLFW_KEY_ESCAPE);
	public static final KeyboardPress	F11	= new KeyboardPress("Fullscreen", GLFW.GLFW_KEY_F11);
	
	public TestControlSet() {
		register(UD);
		register(LR);
		register(ESC);
		register(F11);
		register(SPACE);
	}
}