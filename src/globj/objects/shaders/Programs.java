package globj.objects.shaders;


import lwjgl.debug.GLDebug;
import globj.objects.GLObjectTracker;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;



@NonNullByDefault
public class Programs {
	
	private static final GLObjectTracker<Program>	tracker	= new GLObjectTracker<Program>();
	
	
	private Programs() {
	}
	
	@Nullable
	public static Program createProgram(String name, Shader... shaders) {
		if (tracker.contains(name)) {
			GLDebug.glObjError(Program.class, name, "Cannot create", "Already exists");
			return null;
		}
		Program p = Program.create(name, shaders);
		if (p != null)
			tracker.add(p);
		return p;
	}
	
	@Nullable
	public static Program createProgram(String name, String... shaders) {
		if (tracker.contains(name)) {
			GLDebug.glObjError(Program.class, name, "Cannot create", "Already exists");
			return null;
		}
		Program p = Program.create(name, shaders);
		if (p != null)
			tracker.add(p);
		return p;
	}
	
	@Nullable
	public static Program getProgram(String name) {
		return tracker.get(name);
	}
	
	@Nullable
	public static Program current() {
		return tracker.get(Program.bindTracker.value());
	}
	
}
