package lwjgl.core.objects;

public class BindTracker {
	
	private int current = 0;
	private int last = 0;
	
	public int value(){
		return current;
	}
	public int last(){
		return last;
	}
	
	public void update(int newValue){
		last = current;
		current = newValue;
	}
	
	public int revert(){
		return current = last;
	}
}
