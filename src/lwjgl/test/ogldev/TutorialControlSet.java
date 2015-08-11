package lwjgl.test.ogldev;


import org.lwjgl.glfw.GLFW;

import control.ControlSet;
import control.KeyboardAxis;
import control.KeyboardPress;
import control.KeyboardToggle;



public class TutorialControlSet extends ControlSet {
	
	public static final KeyboardAxis	WS	= new KeyboardAxis("Up-Down Axis", GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_W, 0.05f);
	public static final KeyboardAxis	AD	= new KeyboardAxis("Left-Right Axis", GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_D, 0.05f);
	public static final KeyboardAxis	QE	= new KeyboardAxis("Rotation Axis", GLFW.GLFW_KEY_Q, GLFW.GLFW_KEY_E, 0.05f);
	
	public static final KeyboardAxis	UD	= new KeyboardAxis("Up-Down Axis", GLFW.GLFW_KEY_DOWN, GLFW.GLFW_KEY_UP, 0.5f);
	public static final KeyboardAxis	LR	= new KeyboardAxis("Left-Right Axis", GLFW.GLFW_KEY_LEFT, GLFW.GLFW_KEY_RIGHT, 0.5f);
	
	public static final KeyboardToggle	SPACE	= new KeyboardToggle("Space", GLFW.GLFW_KEY_SPACE);
	public static final KeyboardPress	LCTRL	= new KeyboardPress("Left Control", GLFW.GLFW_KEY_LEFT_CONTROL);
	public static final KeyboardPress	LSHIFT	= new KeyboardPress("Left Shift", GLFW.GLFW_KEY_LEFT_SHIFT);
	public static final KeyboardPress	LALT	= new KeyboardPress("Left Alt", GLFW.GLFW_KEY_LEFT_ALT);
	
	public static final KeyboardPress	ESC		= new KeyboardPress("Escape", GLFW.GLFW_KEY_ESCAPE);
	public static final KeyboardPress	FULLSCR	= new KeyboardPress("Full Screen", GLFW.GLFW_KEY_F11);
	
	public TutorialControlSet() {
		register(WS);
		register(AD);
		register(QE);
		
		register(UD);
		register(LR);
		
		register(SPACE);
		register(LCTRL);
		register(LSHIFT);
		register(LALT);
		
		register(ESC);
		register(FULLSCR);
	}
	
}
