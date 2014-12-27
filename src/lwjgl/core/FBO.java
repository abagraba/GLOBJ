package lwjgl.core;

import java.util.HashMap;

import lwjgl.debug.Logging;

import org.lwjgl.opengl.GL30;

public class FBO extends GLObject {

	protected static final HashMap<String, FBO> fboname = new HashMap<String, FBO>();
	protected static final HashMap<Integer, FBO> fboid = new HashMap<Integer, FBO>();
	protected static int currentFBOD = 0;
	protected static int currentFBOR = 0;
	private static int lastFBOD = 0;
	private static int lastFBOR = 0;
	
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
			Logging.glError("Cannot create Framebuffer Object. No ID could be allocated for Framebuffer Object ["
					+ name + "].", null);
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
		//XXX
		if (currentFBOD == fbo.fbo)
			bindDraw(0);
		if (currentFBOR == fbo.fbo)
			bindRead(0);
		GL30.glDeleteFramebuffers(fbo.fbo);
		fboname.remove(fbo.name);
		fboid.remove(fbo.fbo);
	}
	
	public static FBO get(String name) {
		return fboname.get(name);
	}

	protected static FBO get(int id) {
		return fboid.get(id);
	}
	
	protected static void bind(int fbo) {
		if (fbo == currentFBOD && fbo == currentFBOR){
			lastFBOD = lastFBOR = fbo;
			return;
		}
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
		lastFBOD = currentFBOD;
		lastFBOR = currentFBOR;
		currentFBOD = fbo;
		currentFBOR = fbo;
	}

	public static void bind(String name) {
		FBO f = get(name);
		if (f == null) {
			Logging.glError("Cannot bind FBO [" + name + "]. Does not exist.", null);
			return;
		}
		f.bind();
	}

	public void bind() {
		bind(fbo);
	}

	protected static void bindDraw(int fbo) {
		if (fbo == currentFBOD){
			lastFBOD = fbo;
			return;
		}
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fbo);
		lastFBOD = currentFBOD;
		currentFBOD = fbo;
	}

	public static void bindDraw(String name) {
		FBO f = get(name);
		if (f == null) {
			Logging.glError("Cannot bind FBO [" + name + "]. Does not exist.", null);
			return;
		}
		f.bindDraw();
	}

	public void bindDraw() {
		bindDraw(fbo);
	}

	protected static void bindRead(int fbo) {
		if (fbo == currentFBOR){
			lastFBOR = fbo;
			return;
		}
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fbo);
		lastFBOR = currentFBOR;
		currentFBOR = fbo;
	}

	public static void bindRead(String name) {
		FBO f = get(name);
		if (f == null) {
			Logging.glError("Cannot bind FBO [" + name + "]. Does not exist.", null);
			return;
		}
		f.bindRead();
	}

	public void bindRead() {
		bindRead(fbo);
	}

	private static void bindLast(){
		if (lastFBOD == lastFBOR)
			bind(lastFBOD);
		else{
			bindDraw(lastFBOD);
			bindRead(lastFBOR);
		}
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
		bindLast();
	}
	

	@Override
	public String[] status() {
	/*	if (rbo == 0)
			return "-\tRenderbuffer does not exist.";
		GL.flushErrors();
		bind();
		int w = ARBFramebufferObject.glGetRenderbufferParameteri(
				GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_WIDTH);
		int h = ARBFramebufferObject.glGetRenderbufferParameteri(
				GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_HEIGHT);
		int r = ARBFramebufferObject.glGetRenderbufferParameteri(
				GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_RED_SIZE);
		int g = ARBFramebufferObject.glGetRenderbufferParameteri(
				GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_GREEN_SIZE);
		int b = ARBFramebufferObject.glGetRenderbufferParameteri(
				GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_BLUE_SIZE);
		int a = ARBFramebufferObject.glGetRenderbufferParameteri(
				GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_ALPHA_SIZE);
		int d = ARBFramebufferObject.glGetRenderbufferParameteri(
				GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_DEPTH_SIZE);
		int s = ARBFramebufferObject.glGetRenderbufferParameteri(
				GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_STENCIL_SIZE);
		int n = ARBFramebufferObject.glGetRenderbufferParameteri(
				GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_SAMPLES);
		int f = ARBFramebufferObject.glGetRenderbufferParameteri(
				GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_INTERNAL_FORMAT);
		bindLast();
		String err = GL.readErrors();
		String out = String.format("-\t%-12s:\t%s\n-\t%dx%d Renderbuffer Object. %d samples.\n-\t%-12s:\t[%d, %d, %d, %d]\n-\t%-12s:\t%d\n-\t%-12s:\t%d\n-\t%-12s:\t%d", new Object[]{"Renderbuffer", name, w, h, n, "RGBA", r, g, b, a, "Depth:", d, "Stencil", s, "Format", f});
		if (err == null)
			return out;
		return "ERRORS:\n" + err + out;*/
		return null;
	}}
