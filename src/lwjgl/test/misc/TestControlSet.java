package lwjgl.test.misc;


import org.lwjgl.input.Keyboard;

import control.ControlSet;
import control.KeyboardAxis;
import control.KeyboardPress;
import control.KeyboardToggle;



public class TestControlSet extends ControlSet {
	public static final KeyboardAxis	UD		= new KeyboardAxis("Up-Down Axis", Keyboard.KEY_DOWN, Keyboard.KEY_UP, 0.001f);
	public static final KeyboardAxis	LR		= new KeyboardAxis("Left-Right Axis", Keyboard.KEY_LEFT, Keyboard.KEY_RIGHT, 0.001f);
	
	public static final KeyboardToggle	SPACE	= new KeyboardToggle("Space", Keyboard.KEY_SPACE);
	
	public static final KeyboardPress	ESC		= new KeyboardPress("Escape", Keyboard.KEY_ESCAPE);
	public static final KeyboardPress	F11		= new KeyboardPress("Fullscreen", Keyboard.KEY_F11);
	
	
	public TestControlSet() {
		register(UD);
		register(LR);
		register(ESC);
		register(F11);
		register(SPACE);
	}
}