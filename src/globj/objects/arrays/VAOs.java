package globj.objects.arrays;

import globj.objects.GLObjectTracker;
import lwjgl.debug.GLDebug;

public class VAOs {
	
	private static GLObjectTracker<VAO> tracker = new GLObjectTracker<VAO>();
	
	public static VAO createVAO(String name) {
		VAO vao = VAO.create(name);
		if (tracker.contains(name)) {
			GLDebug.globjError(VAO.class, name, "Cannot create", "Already exists");
			return null;
		}
		tracker.add(vao);
		return vao;
	}
	
	public static VAO getVAO(String name) {
		return tracker.get(name);
	}
	
	public static VAO destroyVAO(String name) {
		return destroyVAO(getVAO(name));
	}

	public static VAO destroyVAO(VAO vao) {
		if (vao != null) {
			vao.destroy();
			tracker.remove(vao);
		}
		return null;
	}
	
}
