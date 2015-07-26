package globj.objects;

import lwjgl.debug.GLDebug;

public class BindTracker {
	
	private int current = 0;
	private int last = 0;
	
	public int value() {
		return current;
	}
	
	public int last() {
		return last;
	}
	
	public void update(int newValue) {
		last = current;
		current = newValue;
	}
	
	public int revert() {
		current = last;
		last = -1;
		if (current == -1) {
			GLDebug.glWarning("Attempted to revert object binding twice. May be due to double-binding the same object. Binding default instead.");
			return 0;
		}
		return current;
	}
	
	public boolean changed() {
		return last != current;
	}
	
	public void clear() {
		current = last = 0;
	}
}
