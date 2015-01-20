package lwjgl.core.objects.bufferobjects;

import lwjgl.core.objects.GLObjectTracker;
import lwjgl.debug.Logging;

public class VBOs {

	private static GLObjectTracker<VBO> tracker = new GLObjectTracker<VBO>(); 
	
	public static VBO createVBO(String name, VBOTarget target){
		VBO vbo = VBO.create(name, target);
		if (tracker.contains(name)) {
			Logging.globjError(VBO.class, name, "Cannot create", "Already exists");
			return null;
		}
		tracker.add(vbo);
		return vbo;
	}

	public static VBO getVBO(String name){
		return tracker.get(name);
	}

	public static void destroyVBO(String name){
		VBO vbo = getVBO(name);
		if (vbo != null)
			vbo.destroy();
	}

	
}
