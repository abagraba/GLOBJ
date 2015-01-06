package lwjgl.core.framebuffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lwjgl.core.Context;
import lwjgl.core.GL;
import lwjgl.core.GLObject;
import lwjgl.core.framebuffer.values.FBOAttachment;
import lwjgl.core.framebuffer.values.FBOError;
import lwjgl.core.texture.Texture;
import lwjgl.core.texture.values.CubemapTarget;
import lwjgl.core.values.DataType;
import lwjgl.debug.Logging;

import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;

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
		GL.flushErrors();
		bindDraw();
		List<String> status = new ArrayList<String>();
		FBOError err = FBOError.get(GL30.glCheckFramebufferStatus(GL30.GL_DRAW_FRAMEBUFFER));
		if (err != FBOError.NONE) {
			unbindDraw();
			status.add(Logging.logText("FBO:", name, 0));
			status.add(Logging.logText("Framebuffer incomplete: " + err, 1));
			return status.toArray(new String[status.size()]);
		}
		
		int w = 0, h = 0, l = 0, s = 0, f = 0;
		if (GL.versionCheck(4, 3)) {
			w = GL43.glGetFramebufferParameteri(GL30.GL_DRAW_FRAMEBUFFER, GL43.GL_FRAMEBUFFER_DEFAULT_WIDTH);
			h = GL43.glGetFramebufferParameteri(GL30.GL_DRAW_FRAMEBUFFER, GL43.GL_FRAMEBUFFER_DEFAULT_HEIGHT);
			l = GL43.glGetFramebufferParameteri(GL30.GL_DRAW_FRAMEBUFFER, GL43.GL_FRAMEBUFFER_DEFAULT_LAYERS);
			s = GL43.glGetFramebufferParameteri(GL30.GL_DRAW_FRAMEBUFFER, GL43.GL_FRAMEBUFFER_DEFAULT_SAMPLES);
			f = GL43.glGetFramebufferParameteri(GL30.GL_DRAW_FRAMEBUFFER, GL43.GL_FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS);
		}
		status.add(Logging.logText("FBO:", name, 0));
		for (FBOAttachment attach : FBOAttachment.values())
			if (attach != FBOAttachment.DEPTH_STENCIL)
				statusBinding(status, attach);
		unbindDraw();
		if (GL.versionCheck(4, 3)) {
			status.add(Logging.logText(String.format("%-32s:\t%d x %d \t%d layers", "Default Dimensions", w, h, l), 1));
			status.add(Logging.logText(String.format("%-32s:\t%d", "Default Samples", s), 1));
			status.add(Logging.logText(String.format("%-32s:\t%d", "Default Fixed Sample Locations", f), 1));
		}
		List<String> errors = GL.readErrorsToList();
		for (String error : errors)
			status.add(Logging.logText("ERROR:", error, 0));
		return status.toArray(new String[status.size()]);
	}
	
	private void statusBinding(List<String> status, FBOAttachment attach) {
		int ca = Context.intConst(GL30.GL_MAX_COLOR_ATTACHMENTS);
		if (attach.colorindex >= ca)
			return;
		int type = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value, GL30.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE);
		switch (type) {
			case GL11.GL_NONE:
				return;
			case GL11.GL_TEXTURE:
				Texture tex = Texture.getTexture(GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value,
						GL30.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME));
				int level = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value, GL30.GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL);
				int layer = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value, GL30.GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER);
				status.add(Logging.logText(attach.toString(), 1));
				status.add(Logging.logText(String.format("%-16s:\t%s", "Texture", tex), 2));
				status.add(Logging.logText(String.format("%-16s:\t%d", "Layer", layer), 3));
				status.add(Logging.logText(String.format("%-16s:\t%d", "Mipmap Level", level), 3));
				return;
			case GL30.GL_FRAMEBUFFER_DEFAULT:
				status.add(Logging.logText(attach.toString(), 1));
				status.add(Logging.logText("Default Framebuffer", 2));
				return;
			case GL30.GL_RENDERBUFFER:
				RBO rbo = RBO
						.get(GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value, GL30.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME));
				int r = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value, GL30.GL_FRAMEBUFFER_ATTACHMENT_RED_SIZE);
				int g = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value, GL30.GL_FRAMEBUFFER_ATTACHMENT_GREEN_SIZE);
				int b = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value, GL30.GL_FRAMEBUFFER_ATTACHMENT_BLUE_SIZE);
				int a = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value, GL30.GL_FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE);
				int d = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value, GL30.GL_FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE);
				int s = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value, GL30.GL_FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE);
				DataType f = DataType.get(GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value,
						GL30.GL_FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE));
				// TODO sRGB
				status.add(Logging.logText(attach.toString(), 1));
				status.add(Logging.logText(String.format("%-16s:\t%s", "Renderbuffer", rbo), 2));
				status.add(Logging.logText(String.format("%-16s:\t(%d, %d, %d, %d)", "RGBA Size", r, g, b, a), 3));
				status.add(Logging.logText(String.format("%-16s:\t%d", "Depth Size", d), 3));
				status.add(Logging.logText(String.format("%-16s:\t%d", "Stencil Size", s), 3));
				status.add(Logging.logText(String.format("%-16s:\t%s", "Data Format", f), 3));
				return;
		}
	}
	
}
