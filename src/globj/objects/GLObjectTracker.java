package globj.objects;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;



@NonNullByDefault
public class GLObjectTracker<T extends GLObject> implements Iterable<T> {
	
	private final Map<String, T>	name	= new HashMap<String, T>();
	private final Map<Integer, T>	id		= new HashMap<Integer, T>();
	
	public void add(T t) {
		name.put(t.name, t);
		id.put(t.id, t);
	}
	
	public void remove(T t) {
		name.remove(t.name);
		id.remove(t.id);
	}
	
	@Nullable
	public T get(String name) {
		return this.name.get(name);
	}
	
	@Nullable
	public T get(int id) {
		return this.id.get(id);
	}
	
	public boolean contains(String name) {
		return this.name.containsKey(name);
	}
	
	public boolean contains(int id) {
		return this.id.containsKey(id);
	}
	
	@Override
	public Iterator<T> iterator() {
		return id.values().iterator();
	}
	
}
