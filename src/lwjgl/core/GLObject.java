package lwjgl.core;

public abstract class GLObject {

	public final String name;
	
	public GLObject(String name) {
		this.name = name;
	}
	
	public abstract String[] status();
	
}
