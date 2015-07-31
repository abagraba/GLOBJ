package globj.objects.framebuffers;


import globj.core.Context;
import globj.core.GL;
import globj.objects.BindTracker;
import globj.objects.BindableGLObject;
import globj.objects.framebuffers.values.FBOAttachment;
import globj.objects.framebuffers.values.FBOError;
import globj.objects.textures.GLTexture;
import globj.objects.textures.Textures;
import globj.objects.textures.values.CubemapTarget;
import globj.objects.textures.values.ImageDataType;
import lwjgl.debug.GLDebug;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;



public class FBO extends BindableGLObject {
	
	private FBO(String name) {
		super(name, GL30.glGenFramebuffers());
	}
	
	/**************************************************/
	
	public static FBO create(String name) {
		FBO fbo = new FBO(name);
		if (fbo.id == 0) {
			GLDebug.glError("Cannot create Framebuffer Object. No ID could be allocated for Framebuffer Object [" + name + "].", null);
			return null;
		}
		return fbo;
	}
	
	/**************************************************/
	
	
	/********************** Bind **********************/
	/**************************************************/
	
	private static final BindTracker bindTracker = new BindTracker();
	
	
	@Override
	protected BindTracker bindingTracker() {
		return bindTracker;
	}
	
	@Override
	protected void bindOP(int id) {
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, id);
	}
	
	@Override
	protected void destroyOP() {
		GL30.glDeleteFramebuffers(id);
	}
	
	@Override
	public void destroy() {
		super.destroy();
	}
	
	/**************************************************/
	
	/*
	 * Bind textures to fbos? bind function in texture? Separate texture to one target per class?
	 */
	/**
	 * Be cautious about deleting the attached RBO/Texture. If the FBO is bound, deleted RBO/Textures are automatically
	 * detached. However, non-active FBOs will not.
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
	 *            &nbsp;&nbsp;&nbsp;TextureCubemap: cubemap face. Use {@link CubemapTarget#layer}.<br/>
	 *            &nbsp;&nbsp;&nbsp;TextureCubemapArray: cubemap index and face. Use 6 * cubemap index +
	 *            {@link CubemapTarget#layer}.
	 */
	public void attach(FBOAttachable att, FBOAttachment attach, int level, int layer) {
		bind();
		if (att == null)
			GL30.glFramebufferTextureLayer(GL30.GL_DRAW_FRAMEBUFFER, attach.value(), 0, 0, 0);
		else
			att.attachToFBO(attach, level, layer);
		undobind();
	}
	
	@Override
	public void debugQuery() {
		GLDebug.flushErrors();
		GLDebug.setPad(24);
		
		GLDebug.write(GLDebug.fixedString("FBO:") + name);
		GLDebug.indent();
		bind();
		
		FBOError err = FBOError.get(GL30.glCheckFramebufferStatus(GL30.GL_DRAW_FRAMEBUFFER));
		if (err != FBOError.NONE) {
			GLDebug.write(GLDebug.fixedString("Framebuffer incomplete:") + err);
			GLDebug.unindent();
			return;
		}
		int count = 0;
		for (FBOAttachment attach : FBOAttachment.values())
			if (attach != FBOAttachment.DEPTH_STENCIL)
				if (debugBindingStatus(attach))
					count++;
					
		int w = 0, h = 0, l = 0, s = 0, f = 0;
		if (count == 0 && GL.versionCheck(4, 3)) {
			w = GL43.glGetFramebufferParameteri(GL30.GL_DRAW_FRAMEBUFFER, GL43.GL_FRAMEBUFFER_DEFAULT_WIDTH);
			h = GL43.glGetFramebufferParameteri(GL30.GL_DRAW_FRAMEBUFFER, GL43.GL_FRAMEBUFFER_DEFAULT_HEIGHT);
			l = GL43.glGetFramebufferParameteri(GL30.GL_DRAW_FRAMEBUFFER, GL43.GL_FRAMEBUFFER_DEFAULT_LAYERS);
			s = GL43.glGetFramebufferParameteri(GL30.GL_DRAW_FRAMEBUFFER, GL43.GL_FRAMEBUFFER_DEFAULT_SAMPLES);
			f = GL43.glGetFramebufferParameteri(GL30.GL_DRAW_FRAMEBUFFER, GL43.GL_FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS);
			
			GLDebug.write(GLDebug.fixedString("Default Size:") + String.format("(%d x %d) x %d", w, h, l));
			GLDebug.write(GLDebug.fixedString("Default Sample:") + s);
			GLDebug.write(GLDebug.fixedString("Default Sample Locations:") + f);
			GLDebug.write("");
		}
		
		undobind();
		GLDebug.unindent();
		GLDebug.unsetPad();
		GLDebug.flushErrors();
	}
	
	private static boolean debugBindingStatus(FBOAttachment attach) {
		int ca = Context.intConst(GL30.GL_MAX_COLOR_ATTACHMENTS);
		if (attach.colorIndex() >= ca)
			return true;
		int type = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value(), GL30.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE);
		if (type == GL11.GL_NONE)
			return false;
		GLDebug.write(attach.toString() + ":");
		switch (type) {
			case GL11.GL_TEXTURE:
				GLTexture tex = Textures.getTexture(GL30.glGetFramebufferAttachmentParameteri(	GL30.GL_DRAW_FRAMEBUFFER, attach.value(),
																								GL30.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME));
				GLDebug.indent();
				if (tex == null) {
					GLDebug.write(GLDebug.fixedString("Texture:") + "none");
				}
				else {
					int level =
							GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value(), GL30.GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL);
					int layer =
							GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value(), GL30.GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER);
					GLDebug.write(GLDebug.fixedString("Texture:") + tex.name);
					GLDebug.indent();
					GLDebug.write(GLDebug.fixedString("Layer:") + layer);
					GLDebug.write(GLDebug.fixedString("Mipmap Level:") + level);
					GLDebug.indent(-1);
				}
				GLDebug.indent(-1);
				return true;
			case GL30.GL_FRAMEBUFFER_DEFAULT:
				GLDebug.indent();
				GLDebug.write(GLDebug.fixedString("Default Framebuffer"));
				GLDebug.unindent();
				return true;
			case GL30.GL_RENDERBUFFER:
				RBO rbo = RBO
						.get(GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value(), GL30.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME));
				int r = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value(), GL30.GL_FRAMEBUFFER_ATTACHMENT_RED_SIZE);
				int g = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value(), GL30.GL_FRAMEBUFFER_ATTACHMENT_GREEN_SIZE);
				int b = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value(), GL30.GL_FRAMEBUFFER_ATTACHMENT_BLUE_SIZE);
				int a = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value(), GL30.GL_FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE);
				int d = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value(), GL30.GL_FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE);
				int s = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value(), GL30.GL_FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE);
				ImageDataType f = ImageDataType.get(GL30.glGetFramebufferAttachmentParameteri(	GL30.GL_DRAW_FRAMEBUFFER, attach.value(),
																								GL30.GL_FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE));
				// TODO sRGB
				GLDebug.indent();
				GLDebug.write(GLDebug.fixedString("Renderbuffer") + rbo.name);
				GLDebug.indent();
				GLDebug.write(GLDebug.fixedString("RGBA Size") + String.format("(%d, %d, %d, %d)", r, g, b, a));
				GLDebug.write(GLDebug.fixedString("Depth Size") + d);
				GLDebug.write(GLDebug.fixedString("Stencil Size") + s);
				GLDebug.write(GLDebug.fixedString("Format") + f);
				GLDebug.indent(-2);
				return true;
		}
		return false;
	}
}
