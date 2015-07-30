package control;


import org.lwjgl.input.Keyboard;



public class KeyboardPress implements ControlState {
	
	private boolean			lastValue;
	private boolean			changed;
	
	private final String	name;
	private final int		key;
	
	
	public KeyboardPress(String name, int key) {
		this.name = name;
		this.key = key;
	}
	
	@Override
	public String name() {
		return name;
	}
	
	@Override
	public boolean state() {
		return changed;
	}
	
	@Override
	public void update() {
		boolean down = Keyboard.isKeyDown(key);
		changed = down && !lastValue;
		lastValue = down;
	}
	
}
