package globj.objects;

public abstract class GLObject {
	
	protected final String	name;
	protected final int		id;
	
	public GLObject(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	/**
	 * @return the name of this GLObject
	 */
	public String name() {
		return name;
	}
	
	/**
	 * @return the id of this GLObject
	 */
	public int id() {
		return id;
	}
	
	public abstract void debug();
	
	public abstract void debugQuery();
	
	@Override
	public String toString() {
		return name;
	}
	
}
