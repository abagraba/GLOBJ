package lwjgl.test.ogldev;


import org.lwjgl.input.Keyboard;

import control.ControlSet;
import control.KeyboardAxis;
import control.KeyboardPress;
import control.KeyboardToggle;



public class TutorialControlSet extends ControlSet {
	
	public static final KeyboardAxis	WS		= new KeyboardAxis("Up-Down Axis", Keyboard.KEY_S, Keyboard.KEY_W, 0.05f);
	public static final KeyboardAxis	AD		= new KeyboardAxis("Left-Right Axis", Keyboard.KEY_A, Keyboard.KEY_D, 0.05f);
	public static final KeyboardAxis	QE		= new KeyboardAxis("Rotation Axis", Keyboard.KEY_Q, Keyboard.KEY_E, 0.05f);
	
	public static final KeyboardAxis	UD		= new KeyboardAxis("Up-Down Axis", Keyboard.KEY_DOWN, Keyboard.KEY_UP, 0.5f);
	public static final KeyboardAxis	LR		= new KeyboardAxis("Left-Right Axis", Keyboard.KEY_LEFT, Keyboard.KEY_RIGHT, 0.5f);
	
	public static final KeyboardToggle	SPACE	= new KeyboardToggle("Space", Keyboard.KEY_SPACE);
	public static final KeyboardPress	LCTRL	= new KeyboardPress("Left Control", Keyboard.KEY_LCONTROL);
	public static final KeyboardPress	LSHIFT	= new KeyboardPress("Left Shift", Keyboard.KEY_LSHIFT);
	public static final KeyboardPress	LALT	= new KeyboardPress("Left Alt", Keyboard.KEY_LMETA);
	
	public static final KeyboardPress	ESC		= new KeyboardPress("Escape", Keyboard.KEY_ESCAPE);
	public static final KeyboardPress	FULLSCR	= new KeyboardPress("Full Screen", Keyboard.KEY_F11);
	
	
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
