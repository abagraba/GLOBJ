package lwjgl.core;

public abstract class GLObject {

	protected final String name;
	
	public GLObject(String name) {
		this.name = name;
	}
	
	public abstract String status();
	
}
