package globj.objects.framebuffers;

import globj.core.Context;
import globj.core.GL;
import globj.objects.GLObjectTracker;

import org.lwjgl.opengl.GL30;

import lwjgl.debug.GLDebug;

public class FBOs {
	
	private static GLObjectTracker<FBO> tracker = new GLObjectTracker<FBO>();
	
	public static FBO createFBO(String name) {
		if (tracker.contains(name)) {
			GLDebug.glError("Cannot create Framebuffer Object. Framebuffer Object [" + name + "] already exists.", null);
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
			GLDebug.glObjError(FBO.class, name, "Cannot destroy", "Does not exist");
			return null;
		}
		return destroyFBO(getFBO(name));
	}
	
	public static void constants() {
		GL.flushErrors();
		int rs = Context.intConst(GL30.GL_MAX_RENDERBUFFER_SIZE);
		int ca = Context.intConst(GL30.GL_MAX_COLOR_ATTACHMENTS);
		GLDebug.write("FBO Constants:");
		GLDebug.indent();
		GLDebug.write(GLDebug.fixedString("Max Color Attachments") + ca);
		GLDebug.write(GLDebug.fixedString("Max Renderbuffer Size") + String.format("%d x %d", rs, rs));
		GLDebug.unindent();
		GL.flushErrors();
	}
	
}
