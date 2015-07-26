package globj.objects;

public abstract class GLObject {
	
	public final String name;
	public final int id;
	
	public GLObject(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public abstract void debugQuery();
	
	@Override
	public String toString() {
		return name;
	}
	
}
