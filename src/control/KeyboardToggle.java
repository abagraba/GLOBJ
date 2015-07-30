package control;


import org.lwjgl.input.Keyboard;



public class KeyboardToggle implements ControlState {
	
	private boolean			lastValue;
	private boolean			state;
	
	private final String	name;
	private final int		key;
	
	
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
		boolean down = Keyboard.isKeyDown(key);
		if (down && !lastValue)
			state = !state;
		lastValue = down;
	}
	
}
