package lwjgl.core;

public abstract class GLObject {

	public final String name;
	public final int id;
	
	public GLObject(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public abstract void bind();
	public abstract void bindNone();
	protected abstract void undobind();
	
	public abstract String[] status();

	public String toString(){
		return name;
	}
	
}
