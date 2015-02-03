package globj.objects.framebuffers;

import globj.core.Context;
import globj.core.GL;
import globj.objects.GLObjectTracker;

import org.lwjgl.opengl.GL30;

import lwjgl.debug.Logging;

public class FBOs {
	
	private static GLObjectTracker<FBO> tracker = new GLObjectTracker<FBO>();
	
	public static FBO createFBO(String name) {
		if (tracker.contains(name)) {
			Logging.glError("Cannot create Framebuffer Object. Framebuffer Object [" + name + "] already exists.", null);
			return null;
		}
		FBO fbo = FBO.create(name);
		if (fbo != null)
			tracker.add(fbo);
		return fbo;
	}
	
	public static FBO getFBO(String name){
		return tracker.get(name);
	}
	
	public static void destroyFBO(String name){
		FBO fbo = getFBO(name);
		if (fbo != null)
			fbo.destroy();
	}
	
	public static void constants() {
		GL.flushErrors();
		int rs = Context.intConst(GL30.GL_MAX_RENDERBUFFER_SIZE);
		int ca = Context.intConst(GL30.GL_MAX_COLOR_ATTACHMENTS);
		Logging.writeOut("FBO Constants:");
		Logging.indent();
		Logging.writeOut(Logging.fixedString("Max Color Attachments") + ca);
		Logging.writeOut(Logging.fixedString("Max Renderbuffer Size") + String.format("%d x %d", rs, rs));
		Logging.unindent();
		GL.flushErrors();
	}
	
}
