package globj.objects.shaders;

import globj.objects.GLObjectTracker;

import org.lwjgl.opengl.GL20;

import lwjgl.debug.GLDebug;

public class Programs {
	
	private static final GLObjectTracker<Program> tracker = new GLObjectTracker<Program>();
	
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
	
	public static Program destroyProgram(Program p) {
		if (p != null) {
			GL20.glDeleteProgram(p.id);
			tracker.remove(p);
		}
		return null;
	}
	
	public static Program destroyProgram(String name) {
		if (!tracker.contains(name)) {
			GLDebug.glObjError(Program.class, name, "Cannot destroy", "Does not exist");
			return null;
		}
		return destroyProgram(getProgram(name));
	}
	
	public static Program getProgram(String name) {
		return tracker.get(name);
	}
	
	public static Program current() {
		return tracker.get(Program.bindTracker.value());
	}
	
}
