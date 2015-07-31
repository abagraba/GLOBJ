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

import static lwjgl.debug.GLDebug.*;

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
			glError("Cannot create Framebuffer Object. No ID could be allocated for Framebuffer Object [" + name + "].", null);
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
	
	/**************************************************/
	
	/*
	 * Bind textures to fbos? bind function in texture? Separate texture to one target per class?
	 */
	/**
	 * Be cautious about deleting the attached RBO/Texture. If the FBO is bound, deleted RBO/Textures are automatically
	 * detached. However, non-active FBOs will not automatically detach.
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
	
	/**************************************************
	 ********************** Debug *********************
	 **************************************************/
	
	@Override
	public void debugQuery() {
		flushErrors();
		bind();
		
		//#formatter:off
		writef(ATTRIB_STRING, "FBO:", name);
		indent();
			FBOError err = FBOError.get(GL30.glCheckFramebufferStatus(GL30.GL_DRAW_FRAMEBUFFER));
			if (err != FBOError.NONE) {
			writef(ATTRIB_STRING, "Framebuffer incomplete:", err);
			unindent();
			return;
			}
		
			int colorCount = 0;
			for (FBOAttachment attach : FBOAttachment.values())
				if (attach != FBOAttachment.DEPTH_STENCIL && debugBindingStatus(attach))
						colorCount++;
						
			if (colorCount == 0 && GL.versionCheck(4, 3)) {
			writef(ATTRIB_STRING + "(%d x %d) x %d", "Default Size:", name, GL43.glGetFramebufferParameteri(GL30.GL_DRAW_FRAMEBUFFER, GL43.GL_FRAMEBUFFER_DEFAULT_WIDTH), 
			               																	GL43.glGetFramebufferParameteri(GL30.GL_DRAW_FRAMEBUFFER, GL43.GL_FRAMEBUFFER_DEFAULT_HEIGHT), 
			               																	GL43.glGetFramebufferParameteri(GL30.GL_DRAW_FRAMEBUFFER, GL43.GL_FRAMEBUFFER_DEFAULT_LAYERS));
			writef(ATTRIB_INT, "Default Samples:", GL43.glGetFramebufferParameteri(GL30.GL_DRAW_FRAMEBUFFER, GL43.GL_FRAMEBUFFER_DEFAULT_SAMPLES));
			writef(ATTRIB_INT, "Default Sample Locations:", GL43.glGetFramebufferParameteri(GL30.GL_DRAW_FRAMEBUFFER, GL43.GL_FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS));
			}
		unindent();
		//#formatter:on
		undobind();
		flushErrors();
	}
	
	private static boolean debugBindingStatus(FBOAttachment attach) {
		int ca = Context.intConst(GL30.GL_MAX_COLOR_ATTACHMENTS);
		if (attach.colorIndex() >= ca) {
			writef("Color attachment index (%d) greater than allowed (%d)", attach.colorIndex(), ca);
			return true;
		}
		int type = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value(), GL30.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE);
		if (type == GL11.GL_NONE)
			return false;
			
		writef(ATTRIB, attach.attachmentName() + ":");
		switch (type) {
			case GL11.GL_TEXTURE:
				indent();
				debugBindingTexture(attach, Textures.getTexture(GL30.glGetFramebufferAttachmentParameteri(	GL30.GL_DRAW_FRAMEBUFFER, attach.value(),
																											GL30.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME)));
				unindent();
				break;
			case GL30.GL_FRAMEBUFFER_DEFAULT:
				indent();
				write("Default Framebuffer");
				unindent();
				break;
			case GL30.GL_RENDERBUFFER:
				indent();
				debugBindingRBO(attach, RBO
						.get(GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value(), GL30.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME)));
				unindent();
				break;
			default:
				return false;
		}
		return true;
	}
	
	private static void debugBindingTexture(FBOAttachment attach, GLTexture tex) {
		//#formatter:off
		if (tex == null){
			writef(ATTRIB_STRING, "Texture:", "none");
		}
		else {
			writef(ATTRIB_STRING, "Texture:", tex);
			indent();
				writef(	ATTRIB_INT, "Layer:",
								GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value(), GL30.GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER));
				writef(	ATTRIB_INT, "Mipmap Level:",
								GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value(), GL30.GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL));
			unindent();
		}
		//#formatter:on
	}
	
	private static void debugBindingRBO(FBOAttachment attach, RBO rbo) {
		//#formatter:off
		ImageDataType f = ImageDataType.get(GL30.glGetFramebufferAttachmentParameteri(	GL30.GL_DRAW_FRAMEBUFFER, attach.value(),
																						GL30.GL_FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE));
		// TODO sRGB?
		writef(ATTRIB_STRING, "Renderbuffer:", rbo);
		indent();
			writef(ATTRIB + "[%d, %d, %d, %d] bits", "RGBA Size:" , 
			       				GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value(), GL30.GL_FRAMEBUFFER_ATTACHMENT_RED_SIZE),
			       				GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value(), GL30.GL_FRAMEBUFFER_ATTACHMENT_GREEN_SIZE), 
			       				GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value(), GL30.GL_FRAMEBUFFER_ATTACHMENT_BLUE_SIZE), 
			       				GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value(), GL30.GL_FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE));
			writef(ATTRIB_INT, "Depth Size:", GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value(), GL30.GL_FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE)); 
			writef(ATTRIB_INT, "Stencil Size:", GL30.glGetFramebufferAttachmentParameteri(GL30.GL_DRAW_FRAMEBUFFER, attach.value(), GL30.GL_FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE)); 
			writef(ATTRIB_STRING, "Format:", f);
		unindent();
		//#formatter:on
	}
	
}
