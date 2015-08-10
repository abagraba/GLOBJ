package globj.objects.textures;


import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;

import globj.core.Context;
import globj.core.GL;
import globj.core.utils.ImageUtil;
import globj.objects.BindTracker;
import globj.objects.framebuffers.FBOAttachable;
import globj.objects.framebuffers.values.FBOAttachment;
import globj.objects.textures.values.ImageDataType;
import globj.objects.textures.values.ImageFormat;
import globj.objects.textures.values.TextureFormat;
import globj.objects.textures.values.TextureTarget;
import lwjgl.debug.GLDebug;



@NonNullByDefault
public final class TextureRectangle extends GLTexture2D implements FBOAttachable {
	
	private int w, h;
	
	private TextureRectangle(String name, TextureFormat texformat) {
		super(name, texformat, TextureTarget.TEXTURE_RECTANGLE);
	}
	
	@Nullable
	protected static TextureRectangle create(String name, TextureFormat texformat, int width, int height) {
		TextureRectangle tex = build(name, texformat, width, height);
		if (tex == null)
			return null;
		tex.undobind();
		return tex;
	}
	
	@Nullable
	protected static TextureRectangle create(String name, BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		TextureFormat texformat = TextureFormat.RGBA8;
		TextureRectangle tex = build(name, texformat, width, height);
		if (tex == null)
			return null;
		// TODO BGRA more efficient?
		GL11.glTexSubImage2D(tex.target.value(), 0, 0, 0, width, height, ImageFormat.RGBA.value(), ImageDataType.UBYTE.value(), ImageUtil.imageRGBAData(image));
		tex.undobind();
		return tex;
	}
	
	@Nullable
	private static TextureRectangle build(String name, TextureFormat texformat, int width, int height) {
		TextureRectangle tex = new TextureRectangle(name, texformat);
		if (tex.id == 0) {
			GLDebug.glObjError(TextureRectangle.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		if (!checkBounds(new int[] { width, height }, new int[] { max, max }, tex))
			return null;
			
		tex.w = width;
		tex.h = height;
		
		tex.bind();
		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage2D(tex.target.value(), 0, texformat.value(), width, height);
		}
		else {
			GL11.glTexImage2D(tex.target.value(), 0, texformat.value(), width, height, 0, texformat.base(), ImageDataType.UBYTE.value(), (ByteBuffer) null);
		}
		return tex;
	}
	
	/**************************************************/
	
	/**
	 * Sets the texel data in specified rectangle of mipmap level. Texture needs to be initialized with
	 * {@link #initializeTexture(int, int, int, TextureFormat)}. Rectangle must be within the bounds of the texture.
	 * [GL_TEXTURE_BASE_LEVEL + map].
	 */
	public void setData(int x, int y, int w, int h, ImageFormat format, ImageDataType type, ByteBuffer data) {
		bind();
		GL11.glTexSubImage2D(target.value(), 0, x, y, w, h, format.value(), type.value(), data);
		undobind();
	}
	
	/**************************************************
	 ********************** Bind **********************
	 **************************************************/
	
	private static final BindTracker bindTracker = new BindTracker();
	
	@Override
	protected BindTracker bindingTracker() {
		return bindTracker;
	}
	
	/**************************************************
	 ****************** FBOAttachable *****************
	 **************************************************/
	/**
	 * @param level
	 *            unused.
	 * @param layer
	 *            unused.
	 */
	@Override
	public void attachToFBO(FBOAttachment attachment, int level, int layer) {
		GL30.glFramebufferTexture2D(GL30.GL_DRAW_FRAMEBUFFER, attachment.value(), target.value(), id, 0);
	}
	
	/**************************************************
	 ********************** Debug *********************
	 **************************************************/
	@Override
	public void debug() {
		GLDebug.writef(GLDebug.ATTRIB_STRING + "\t(%d x %d)", target + ":", name, w, h);
		//#formatter:off
		GLDebug.indent();
			GLDebug.writef(GLDebug.ATTRIB_STRING, "Texture Format:", texformat);
			super.debug();
		GLDebug.unindent();
		//#formatter:on
	}
	
	@Override
	public void debugQuery() {
		GLDebug.flushErrors();
		GLDebug.writef(GLDebug.ATTRIB_STRING+ "\t(%d x %d)", target + ":", name, GL11.glGetTexLevelParameteri(target.value(), 0, GL11.GL_TEXTURE_WIDTH),
						GL11.glGetTexParameteri(target.value(), GL11.GL_TEXTURE_HEIGHT));
		//#formatter:off
		GLDebug.indent();
			GLDebug.writef(GLDebug.ATTRIB_STRING, "Texture Format:", TextureFormat.get(GL11.glGetTexLevelParameteri(target.value(), 0, GL11.GL_TEXTURE_INTERNAL_FORMAT)));
			super.debugQuery();
		GLDebug.unindent();
		//#formatter:on
		GLDebug.flushErrors();
	}
	
}
