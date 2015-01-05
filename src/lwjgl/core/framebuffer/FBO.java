package lwjgl.core.framebuffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lwjgl.core.Context;
import lwjgl.core.GL;
import lwjgl.core.GLObject;
import lwjgl.core.framebuffer.values.FBOAttachment;
import lwjgl.core.texture.Texture1D;
import lwjgl.core.texture.Texture2D;
import lwjgl.core.texture.values.CubemapTarget;
import lwjgl.core.texture.values.Texture1DTarget;
import lwjgl.core.texture.values.Texture2DTarget;
import lwjgl.debug.Logging;

import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GL30;

public class FBO extends GLObject {
	
	protected static final HashMap<String, FBO> fboname = new HashMap<String, FBO>();
	protected static final HashMap<Integer, FBO> fboid = new HashMap<Integer, FBO>();
	protected static int currentFBOD = 0;
	protected static int currentFBOR = 0;
	private static int lastFBOD = 0;
	private static int lastFBOR = 0;
	
	private FBO(String name) {
		super(name, GL30.glGenFramebuffers());
	}
	
	/**************************************************/
	
	public static FBO create(String name) {
		if (fboname.containsKey(name)) {
			Logging.glError("Cannot create Framebuffer Object. Framebuffer Object [" + name + "] already exists.", null);
			return null;
		}
		FBO fbo = new FBO(name);
		if (fbo.id == 0) {
			Logging.glError("Cannot create Framebuffer Object. No ID could be allocated for Framebuffer Object [" + name + "].", null);
			return null;
		}
		fboname.put(fbo.name, fbo);
		fboid.put(fbo.id, fbo);
		return fbo;
	}
	
	public static void destroy(String name) {
		if (!fboname.containsKey(name)) {
			Logging.glWarning("Cannot delete Framebuffer Object. Framebuffer Object [" + name + "] does not exist.");
			return;
		}
		FBO fbo = fboname.get(name);
		boolean d = currentFBOD == fbo.id;
		boolean r = currentFBOR == fbo.id;
		if (d && r)
			bind(0);
		else {
			if (d)
				bindDraw(0);
			if (r)
				bindRead(0);
		}
		GL30.glDeleteFramebuffers(fbo.id);
		fboname.remove(fbo.name);
		fboid.remove(fbo.id);
	}
	
	public static FBO get(String name) {
		return fboname.get(name);
	}
	
	protected static FBO get(int id) {
		return fboid.get(id);
	}
	
	/**************************************************/
	
	static void bind(int fbo) {
		if (fbo == currentFBOD && fbo == currentFBOR) {
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
		bind(id);
	}
	
	protected void unbind() {
		bind(lastFBOD);
	}
	
	protected static void bindDraw(int fbo) {
		if (fbo == currentFBOD) {
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
		bindDraw(id);
	}
	
	protected void unbindDraw() {
		bindDraw(lastFBOD);
	}
	
	protected static void bindRead(int fbo) {
		if (fbo == currentFBOR) {
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
		bindRead(id);
	}
	
	protected void unbindRead() {
		bindRead(lastFBOR);
	}
	
	/**************************************************/
	
	/*
	 * 
	 * Bind textures to fbos? bind function in texture? Separate texture to one
	 * target per class?
	 */
	/**
	 * Be cautious about deleting the attached RBO/Texture. If the FBO is bound,
	 * deleted RBO/Textures are automatically detached. However, non-active FBOs
	 * will not.
	 * 
	 * @param level
	 *            Texture1D: mipmap level.<br/>
	 *            &nbsp;&nbsp;&nbsp;Texture1DArray: mipmap level.<br/>
	 *            &nbsp;&nbsp;&nbsp;Texture2D: mipmap level.<br/>
	 *            &nbsp;&nbsp;&nbsp;Texture2DArray: mipmap level.<br/>
	 *            &nbsp;&nbsp;&nbsp;Texture3D: mipmap level.<br/>
	 *            &nbsp;&nbsp;&nbsp;TextureRectangle: unused.<br/>
	 *            &nbsp;&nbsp;&nbsp;TextureCubemap: mipmap level.<br/>
	 *            &nbsp;&nbsp;&nbsp;TextureCubemapArray: mipmap level.<br/>
	 * 
	 * @param layer
	 *            Texture1D: unused.<br/>
	 *            &nbsp;&nbsp;&nbsp;Texture1DArray: texture index.<br/>
	 *            &nbsp;&nbsp;&nbsp;Texture2D: unused.<br/>
	 *            &nbsp;&nbsp;&nbsp;Texture2DArray: texture index.<br/>
	 *            &nbsp;&nbsp;&nbsp;Texture3D: z-offset.<br/>
	 *            &nbsp;&nbsp;&nbsp;TextureRectangle: unused.<br/>
	 *            &nbsp;&nbsp;&nbsp;TextureCubemap: cubemap face. Use
	 *            {@link CubemapTarget#layer}.<br/>
	 *            &nbsp;&nbsp;&nbsp;TextureCubemapArray: cubemap index and face.
	 *            Use 6 * cubemap index + {@link CubemapTarget#layer}.<br/>
	 */
	public void attach(FBOAttachable att, FBOAttachment attach, int level, int layer) {
		bindDraw();
		if (att == null)
			GL30.glFramebufferTextureLayer(GL30.GL_DRAW_FRAMEBUFFER, attach.value, 0, 0, 0);
		else
			att.attachToFBO(attach, level, layer);
		unbindDraw();
	}
	
	private void attach(RBO rbo, int point) {
		bind();
		int i = rbo == null ? 0 : rbo.id;
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, point, GL30.GL_RENDERBUFFER, i);
		unbind();
	}
	
	public static String[] constants() {
		GL.flushErrors();
		int rs = Context.intConst(GL30.GL_MAX_RENDERBUFFER_SIZE);
		int ca = Context.intConst(GL30.GL_MAX_COLOR_ATTACHMENTS);
		List<String> status = new ArrayList<String>();
		List<String> errors = GL.readErrorsToList();
		for (String error : errors)
			status.add(Logging.logText("ERROR:", error, 0));
		status.add(Logging.logText("FBO Constants:", "", 0));
		status.add(Logging.logText(String.format("%-12s:\t%d", "Max Color Attachments", ca), 1));
		status.add(Logging.logText(String.format("%-12s:\t%d", "Max Renderbuffer Size", rs), 1));
		return status.toArray(new String[status.size()]);
	}
	
	@Override
	public String[] status() {
		/*
		 * if (rbo == 0) return "-\tRenderbuffer does not exist.";
		 * GL.flushErrors(); bind(); int w =
		 * ARBFramebufferObject.glGetRenderbufferParameteri(
		 * GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_WIDTH); int h =
		 * ARBFramebufferObject.glGetRenderbufferParameteri(
		 * GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_HEIGHT); int r =
		 * ARBFramebufferObject.glGetRenderbufferParameteri(
		 * GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_RED_SIZE); int g =
		 * ARBFramebufferObject.glGetRenderbufferParameteri(
		 * GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_GREEN_SIZE); int b =
		 * ARBFramebufferObject.glGetRenderbufferParameteri(
		 * GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_BLUE_SIZE); int a =
		 * ARBFramebufferObject.glGetRenderbufferParameteri(
		 * GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_ALPHA_SIZE); int d =
		 * ARBFramebufferObject.glGetRenderbufferParameteri(
		 * GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_DEPTH_SIZE); int s =
		 * ARBFramebufferObject.glGetRenderbufferParameteri(
		 * GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_STENCIL_SIZE); int n =
		 * ARBFramebufferObject.glGetRenderbufferParameteri(
		 * GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_SAMPLES); int f =
		 * ARBFramebufferObject.glGetRenderbufferParameteri(
		 * GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_INTERNAL_FORMAT);
		 * bindLast(); String err = GL.readErrors(); String out = String.format(
		 * "-\t%-12s:\t%s\n-\t%dx%d Renderbuffer Object. %d samples.\n-\t%-12s:\t[%d, %d, %d, %d]\n-\t%-12s:\t%d\n-\t%-12s:\t%d\n-\t%-12s:\t%d"
		 * , new Object[]{"Renderbuffer", name, w, h, n, "RGBA", r, g, b, a,
		 * "Depth:", d, "Stencil", s, "Format", f}); if (err == null) return
		 * out; return "ERRORS:\n" + err + out;
		 */
		return null;
	}
}
