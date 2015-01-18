package lwjgl.core.objects;

public abstract class GLObject {

	public final String name;
	public final int id;
	
	public GLObject(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public abstract void debug();

	public String toString(){
		return name;
	}
	
}
