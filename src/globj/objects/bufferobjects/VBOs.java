package globj.objects.bufferobjects;

import globj.objects.GLObjectTracker;
import lwjgl.debug.GLDebug;

public class VBOs {
	
	private static GLObjectTracker<VBO> tracker = new GLObjectTracker<VBO>();
	
	public static VBO createVBO(String name, VBOTarget target) {
		VBO vbo = VBO.create(name, target);
		if (tracker.contains(name)) {
			GLDebug.globjError(VBO.class, name, "Cannot create", "Already exists");
			return null;
		}
		tracker.add(vbo);
		return vbo;
	}
	
	public static VBO getVBO(String name) {
		return tracker.get(name);
	}
	
	public static VBO destroyVBO(VBO vbo) {
		if (vbo != null) {
			vbo.destroy();
			tracker.remove(vbo);
		}
		return null;
	}
	
	public static VBO destroyVBO(String name) {
		return destroyVBO(getVBO(name));
	}
	
}
