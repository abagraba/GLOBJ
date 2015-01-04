package lwjgl.core;

import java.util.HashMap;

public class GLObjectTracker<T extends GLObject> {
	
	private final HashMap<String, T> name = new HashMap<String, T>();
	private final HashMap<Integer, T> id = new HashMap<Integer, T>();
	
	public void add(T t) {
		name.put(t.name, t);
		id.put(t.id, t);
	}
	
	public void remove(T t) {
		name.remove(t.name);
		id.remove(t.id);
	}
	
	public T get(String name){
		return this.name.get(name);
	}

	public T get(int id){
		return this.id.get(id);
	}

	public boolean contains(String name){
		return this.name.containsKey(name);
	}

	public boolean contains(int id){
		return this.id.containsKey(id);
	}

}
