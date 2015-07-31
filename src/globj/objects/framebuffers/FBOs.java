package globj.objects.framebuffers;


import globj.core.Context;
import globj.objects.GLObjectTracker;

import org.lwjgl.opengl.GL30;

import static lwjgl.debug.GLDebug.*;



public class FBOs {
	
	private static GLObjectTracker<FBO> tracker = new GLObjectTracker<FBO>();
	
	private FBOs() {
	}
	
	public static FBO createFBO(String name) {
		if (tracker.contains(name)) {
			glError("Cannot create Framebuffer Object. Framebuffer Object [" + name + "] already exists.", null);
			return null;
		}
		FBO fbo = FBO.create(name);
		if (fbo != null)
			tracker.add(fbo);
		return fbo;
	}
	
	public static FBO getFBO(String name) {
		return tracker.get(name);
	}
	
	public static FBO destroyFBO(FBO fbo) {
		if (fbo != null) {
			tracker.remove(fbo);
			fbo.destroy();
		}
		return null;
	}
	
	public static FBO destroyFBO(String name) {
		if (!tracker.contains(name)) {
			glObjError(FBO.class, name, "Cannot destroy", "Does not exist");
			return null;
		}
		return destroyFBO(getFBO(name));
	}
	
	public static void constants() {
		flushErrors();
		//	#formatter:off
		write("FBO Constants:");
		indent();
			writef(ATTRIB_INT, "Max Color Attachments:", Context.intConst(GL30.GL_MAX_COLOR_ATTACHMENTS));
			int rs = Context.intConst(GL30.GL_MAX_RENDERBUFFER_SIZE);
			writef(ATTRIB + "%d x %d", "Max Renderbuffer Size:", rs, rs);
		unindent();
		//	#formatter:on
		flushErrors();
	}
	
}
