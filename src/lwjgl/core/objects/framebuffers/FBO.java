package lwjgl.core.objects.framebuffers;

import lwjgl.core.Context;
import lwjgl.core.GL;
import lwjgl.core.objects.BindTracker;
import lwjgl.core.objects.BindableGLObject;
import lwjgl.core.objects.framebuffers.values.FBOAttachment;
import lwjgl.core.objects.framebuffers.values.FBOError;
import lwjgl.core.objects.textures.GLTexture;
import lwjgl.core.objects.textures.Textures;
import lwjgl.core.objects.textures.values.CubemapTarget;
import lwjgl.core.values.DataType;
import lwjgl.debug.Logging;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;

public class FBO extends BindableGLObject {
	
	private static BindTracker draw = new BindTracker();
	private static BindTracker read = new BindTracker();
	
	private FBO(String name) {
		super(name, GL30.glGenFramebuffers());
	}
	
	/**************************************************/
	
	protected static FBO create(String name) {
		FBO fbo = new FBO(name);
		if (fbo.id == 0) {
			Logging.glError("Cannot create Framebuffer Object. No ID could be allocated for Framebuffer Object [" + name + "].", null);
			return null;
		}
		return fbo;
	}
	
	protected void destroy() {
		boolean d = draw.value() == id;
		boolean r = read.value() == id;
		if (d && r)
			bind(0);
		else {
			if (d)
				bindDraw(0);
			if (r)
				bindRead(0);
		}
		GL30.glDeleteFramebuffers(id);
	}
	
	/**************************************************/
	
	private static void bind(int fbo) {
		if (fbo == draw.value() && fbo == read.value()) {
			draw.update(fbo);
			read.update(fbo);
			return;
		}
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
		draw.update(fbo);
		read.update(fbo);
	}
	
	public void bind() {
		bind(id);
	}
	
	protected void undobind() {
		bind(draw.last());
	}
	
	@Override
	public void bindNone() {
		bind(0);
	}
	
	protected static void bindDraw(int fbo) {
		if (fbo == draw.value()) {
			draw.update(fbo);
			return;
		}
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fbo);
		draw.update(fbo);
	}
	
	public void bindDraw() {
		bindDraw(id);
	}
	
	protected void unbindDraw() {
		bindDraw(draw.last());
	}
	
	protected static void bindRead(int fbo) {
		if (fbo == read.value()) {
			read.update(fbo);
			return;
		}
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fbo);
		read.update(fbo);
	}
	
	public void bindRead() {
		bindRead(id);
	}
	
	protected void unbindRead() {
		bindRead(read.last());
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
	
	@Override
	public void debug() {
		GL.flushErrors();
		Logging.setPad(24);
		
		Logging.writeOut(Logging.fixedString("FBO:") + name);
		Logging.indent();
		
		FBOError err = FBOError.get(GL30.glCheckFramebufferStatus(GL30.GL_DRAW_FRAMEBUFFER));
		if (err != FBOError.NONE) {
			Logging.writeOut(Logging.fixedString("Framebuffer incomplete:") + err);
			Logging.unindent();
			return;
		}
		int w = 0, h = 0, l = 0, s = 0, f = 0;
		if (GL.versionCheck(4, 3)) {
			w = GL43.glGetFramebufferParameteri(GL30.GL_DRAW_FRAMEBUFFER, GL43.GL_FRAMEBUFFER_DEFAULT_WIDTH);
			h = GL43.glGetFramebufferParameteri(GL30.GL_DRAW_FRAMEBUFFER, GL43.GL_FRAMEBUFFER_DEFAULT_HEIGHT);
			l = GL43.glGetFramebufferParameteri(GL30.GL_DRAW_FRAMEBUFFER, GL43.GL_FRAMEBUFFER_DEFAULT_LAYERS);
			s = GL43.glGetFramebufferParameteri(GL30.GL_DRAW_FRAMEBUFFER, GL43.GL_FRAMEBUFFER_DEFAULT_SAMPLES);
			f = GL43.glGetFramebufferParameteri(GL30.GL_DRAW_FRAMEBUFFER, GL43.GL_FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS);
			
			Logging.writeOut(Logging.fixedString("Default Size:") + String.format("(%d x %d) x %d", w, h, l));
			Logging.writeOut(Logging.fixedString("Default Sample:") + s);
			Logging.writeOut(Logging.fixedString("Default Sample Locations:") + f);
			Logging.writeOut("");
		}
		
		for (FBOAttachment attach : FBOAttachment.values())
			if (attach != FBOAttachment.DEPTH_STENCIL)
				debugBindingStatus(attach);
		
		Logging.unindent();
		Logging.unsetPad();
		GL.flushErrors();
	}
	
	private void debugBindingStatus(FBOAttachment attach) {
		int ca = Context.intConst(GL30.GL_MAX_COLOR_ATTACHMENTS);
		if (attach.colorindex >= ca)
			return;
		int type = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value, GL30.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE);
		if (type == GL11.GL_NONE)
			return;
		Logging.writeOut(attach.toString() + ":");
		switch (type) {
			case GL11.GL_TEXTURE:
				GLTexture tex = Textures.getTexture(GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value,
						GL30.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME));
				int level = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value, GL30.GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL);
				int layer = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value, GL30.GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER);
				Logging.indent();
				Logging.writeOut(Logging.fixedString("Texture:") + tex.name);
				Logging.indent();
				Logging.writeOut(Logging.fixedString("Layer:") + layer);
				Logging.writeOut(Logging.fixedString("Mipmap Level:") + level);
				Logging.indent(-2);
				return;
			case GL30.GL_FRAMEBUFFER_DEFAULT:
				Logging.indent();
				Logging.writeOut(Logging.fixedString("Default Framebuffer:"));
				Logging.unindent();
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
				Logging.indent();
				Logging.writeOut(Logging.fixedString("Renderbuffer") + rbo.name);
				Logging.indent();
				Logging.writeOut(Logging.fixedString("RGBA Size") + String.format("(%d, %d, %d, %d)", r, g, b, a));
				Logging.writeOut(Logging.fixedString("Depth Size") + d);
				Logging.writeOut(Logging.fixedString("Stencil Size") + s);
				Logging.writeOut(Logging.fixedString("Format") + f);
				Logging.indent(-2);
				return;
		}
	}
	
}
