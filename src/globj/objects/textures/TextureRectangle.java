package globj.objects.textures;


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

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import lwjgl.debug.GLDebug;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;



@NonNullByDefault
public final class TextureRectangle extends GLTexture2D implements FBOAttachable {
	
	private int w, h;
	
	
	private TextureRectangle(String name, TextureFormat texformat) {
		super(name, texformat, TextureTarget.TEXTURE_RECTANGLE);
	}
	
	@Nullable
	protected static TextureRectangle create(String name, TextureFormat texformat, int w, int h) {
		TextureRectangle tex = new TextureRectangle(name, texformat);
		if (tex.id == 0) {
			GLDebug.glObjError(TextureRectangle.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		if (!checkBounds(new int[] { w, h }, new int[] { max, max }, tex))
			return null;
			
		tex.w = w;
		tex.h = h;
		
		tex.bind();
		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage2D(tex.target.value(), 0, texformat.value(), w, h);
		}
		else {
			GL11.glTexImage2D(tex.target.value(), 0, texformat.value(), w, h, 0, texformat.base(), ImageDataType.UBYTE.value(), (ByteBuffer) null);
		}
		tex.undobind();
		return tex;
	}
	
	@Nullable
	protected static TextureRectangle create(String name, BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();
		TextureFormat texformat = TextureFormat.RGBA8;
		TextureRectangle tex = new TextureRectangle(name, texformat);
		if (tex.id == 0) {
			GLDebug.glObjError(TextureRectangle.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		if (!checkBounds(new int[] { w, h }, new int[] { max, max }, tex))
			return null;
			
		tex.w = w;
		tex.h = h;
		
		tex.bind();
		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage2D(tex.target.value(), 0, texformat.value(), w, h);
		}
		else {
			GL11.glTexImage2D(tex.target.value(), 0, texformat.value(), w, h, 0, texformat.base(), ImageDataType.UBYTE.value(), (ByteBuffer) null);
		}
		// TODO BGRA more efficient?
		GL11.glTexSubImage2D(tex.target.value(), 0, 0, 0, w, h, ImageFormat.RGBA.value(), ImageDataType.UBYTE.value(), ImageUtil.imageRGBAData(image));
		tex.undobind();
		return tex;
	}
	
	/**************************************************/
	
	
	/********************** Bind **********************/
	/**************************************************/
	
	private static final BindTracker bindTracker = new BindTracker();
	
	
	@Override
	protected BindTracker bindingTracker() {
		return bindTracker;
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
	
	/**************************************************/
	/****************** FBOAttachable *****************/
	
	/**************************************************/
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
	
	/**************************************************/
	
	/********************** Debug *********************/
	/**************************************************/
	
	@Override
	public void debugQuery() {
		GLDebug.flushErrors();
		
		GLDebug.writef(GLDebug.ATTRIB_STRING + "\t(%d x %d)", target, name, w, h);
		GLDebug.indent();
		
		GLDebug.writef(GLDebug.ATTRIB_STRING, "Texture Format", texformat);
		
		GLDebug.writef(GLDebug.ATTRIB_STRING, "Wrapping Mode [S]", sWrap);
		GLDebug.writef(GLDebug.ATTRIB_STRING, "Wrapping Mode [T]", tWrap);
		
		super.debugQuery();
		
		GLDebug.unindent();
		
		GLDebug.flushErrors();
	}
	
}
