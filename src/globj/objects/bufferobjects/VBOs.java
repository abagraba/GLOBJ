package globj.objects.bufferobjects;

import globj.objects.GLObjectTracker;
import lwjgl.debug.GLDebug;

public class VBOs {
	
	private static GLObjectTracker<VBO> tracker = new GLObjectTracker<VBO>();
	
	protected static boolean registerVBO(VBO vbo) {
		if (tracker.contains(vbo.name)) {
			GLDebug.glObjError(VBO.class, vbo.name, "Cannot create", "Already exists");
			return false;
		}
		tracker.add(vbo);
		return true;
	}
	
	protected static boolean unregisterVBO(VBO vbo) {
		if (!tracker.contains(vbo.name))
			return false;
		tracker.remove(vbo);
		return true;
	}
	
	public static VBO getVBO(String name) {
		return tracker.get(name);
	}
	
}
