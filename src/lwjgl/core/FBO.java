package lwjgl.core;

import java.util.HashMap;

import lwjgl.debug.Logging;

import org.lwjgl.opengl.GL30;

public class FBO extends GLObject {

	protected static final HashMap<String, FBO> fboname = new HashMap<String, FBO>();
	protected static final HashMap<Integer, FBO> fboid = new HashMap<Integer, FBO>();
	protected static int currentFBO = 0;
	private static int lastFBO = 0;
	
	protected final int fbo;

	private FBO(String name) {
		super(name);
		fbo = GL30.glGenFramebuffers();
	}
	
	public static FBO create(String name){
		if (fboname.containsKey(name)){
			Logging.glError("Cannot create Framebuffer Object. Framebuffer Object ["
					+ name + "] already exists.", null);
			return null;
		}
		FBO fbo = new FBO(name);
		if (fbo.fbo == 0){
			Logging.glError("Cannot create Framebuffer Object. Framebuffer Object ["
					+ name + "] already exists.", null);
			return null;
		}
		fboname.put(fbo.name, fbo);
		fboid.put(fbo.fbo, fbo);
		return fbo;
	}

	public static void destroy(String name) {
		if (!fboname.containsKey(name)){
			Logging.glWarning("Cannot delete Framebuffer Object. Framebuffer Object ["
					+ name + "] does not exist.");
			return;
		}
		FBO fbo = fboname.get(name);
		GL30.glDeleteFramebuffers(fbo.fbo);
		fboname.remove(fbo.name);
		fboid.remove(fbo.fbo);
	}
	
	public static FBO get(String name) {
		return fboname.get(name);
	}

	protected static void bind(int fbo) {
		if (fbo == currentFBO)
			return;
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
		lastFBO = currentFBO;
		currentFBO = fbo;
	}

	public static void bind(String name) {
		get(name).bind();
	}

	public void bind() {
		bind(fbo);
	}
	
	/**
	 * 
	 * Be cautious about deleting the attached RBO/Texture. If the FBO is bound, deleted RBO/Textures are automatically detached. However, non-active FBOs will not.
	 * @param rbo
	 */
/*	XXX public void attach(RBO rbo, int point){
		bind();
		int i = rbo == null ? 0 : rbo.rbo;
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, point, GL30.GL_RENDERBUFFER, i);
		bind(lastFBO);
	}*/
	/**
	 * 
	 * Be cautious about deleting the attached RBO/Texture. If the FBO is bound, deleted RBO/Textures are automatically detached. However, non-active FBOs will not.
	 * @param rbo
	 */
	public void attach(RBO rbo, int point){
		bind();
		int i = rbo == null ? 0 : rbo.rbo;
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, point, GL30.GL_RENDERBUFFER, i);
		bind(lastFBO);
	}
	

	@Override
	public String status() {
		return null;
	}
}
