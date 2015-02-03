package globj.objects.textures;

import globj.core.Context;
import globj.core.GL;
import globj.objects.BindTracker;
import globj.objects.framebuffers.FBOAttachable;
import globj.objects.framebuffers.values.FBOAttachment;
import globj.objects.textures.values.ImageFormat;
import globj.objects.textures.values.TextureFormat;
import globj.objects.textures.values.TextureTarget;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import lwjgl.core.utils.ImageUtil;
import lwjgl.core.values.DataType;
import lwjgl.debug.Logging;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL42;

public final class TextureRectangle extends GLTexture2D implements FBOAttachable {
	
	private static final BindTracker curr = new BindTracker();
	
	public final static TextureTarget target = TextureTarget.TEXTURE_RECTANGLE;
	
	private int w, h;
	
	private TextureRectangle(String name, TextureFormat texformat) {
		super(name, texformat, target);
	}
	
	protected static TextureRectangle create(String name, TextureFormat texformat, int w, int h) {
		TextureRectangle tex = new TextureRectangle(name, texformat);
		if (tex.id == 0) {
			Logging.globjError(TextureRectangle.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		if (!checkBounds(new int[] { w, h }, new int[] { max, max }, tex))
			return null;
		
		tex.w = w;
		tex.h = h;
		
		tex.bind();
		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage2D(target.value, 0, texformat.value, w, h);
		}
		else {
			GL11.glTexImage2D(target.value, 0, texformat.value, w, h, 0, texformat.base, DataType.UBYTE.value, (ByteBuffer) null);
		}
		tex.undobind();
		return tex;
	}
	
	protected static TextureRectangle create(String name, BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();
		TextureFormat texformat = TextureFormat.RGBA8;
		TextureRectangle tex = new TextureRectangle(name, texformat);
		if (tex.id == 0) {
			Logging.globjError(TextureRectangle.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		if (!checkBounds(new int[] { w, h }, new int[] { max, max }, tex))
			return null;
		
		tex.w = w;
		tex.h = h;
		
		tex.bind();
		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage2D(target.value, 0, texformat.value, w, h);
		}
		else {
			GL11.glTexImage2D(target.value, 0, texformat.value, w, h, 0, texformat.base, DataType.UBYTE.value, (ByteBuffer) null);
		}
		// TODO BGRA more efficient?
		GL11.glTexSubImage2D(target.value, 0, 0, 0, w, h, ImageFormat.RGBA.value, DataType.UBYTE.value, ImageUtil.imageRGBAData(image));
		tex.undobind();
		return tex;
	}
	
	/**************************************************/
	/********************** Bind **********************/
	/**************************************************/
	
	private static void bind(int tex) {
		curr.update(tex);
		if (tex == curr.last())
			return;
		GL11.glBindTexture(GL31.GL_TEXTURE_RECTANGLE, tex);
	}
	
	public void bind() {
		bind(id);
	}
	
	public void bindNone() {
		bind(0);
	}
	
	protected void undobind() {
		bind(curr.revert());
	}
	
	protected void destroy() {
		if (curr.value() == id)
			bindNone();
		GL11.glDeleteTextures(id);
	}
	
	/**************************************************/
	
	/**
	 * Sets the texel data in specified rectangle of mipmap level. Texture needs
	 * to be initialized with
	 * {@link #initializeTexture(int, int, int, TextureFormat)}. Rectangle must
	 * be within the bounds of the texture. [GL_TEXTURE_BASE_LEVEL + map].
	 */
	public void setData(int x, int y, int w, int h, ImageFormat format, DataType type, ByteBuffer data) {
		bind();
		GL11.glTexSubImage2D(target.value, 0, x, y, w, h, format.value, type.value, data);
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
		GL30.glFramebufferTexture2D(GL30.GL_DRAW_FRAMEBUFFER, attachment.value, target.value, id, 0);
	}
	
	/**************************************************/
	
	@Override
	public void debug() {
		GL.flushErrors();
		Logging.setPad(32);
		
		Logging.writeOut(Logging.fixedString(target + ":") + String.format("%s\t(%d x %d)", name, w, h));
		Logging.indent();
		
		Logging.writeOut(Logging.fixedString("Texture Format:") + texformat);
		
		Logging.writeOut(minFilter);
		Logging.writeOut(magFilter);
		
		boolean tb = lodMin.resolved() && lodMax.resolved() && lodBias.resolved();
		String ts = Logging.fixedString("LOD Range:") + String.format("[%4f, %4f] + %4f", lodMin.value(), lodMax.value(), lodBias.value());
		if (!tb)
			ts += "\tUnresolved:\t" + String.format("[%4f, %4f] + %4f", lodMin.state(), lodMax.state(), lodBias.state());
		Logging.writeOut(ts);
		
		tb = swizzleR.resolved() && swizzleG.resolved() && swizzleB.resolved() && swizzleA.resolved();
		ts = Logging.fixedString("Texture Swizzle:")
				+ String.format("[%s, %s, %s, %s]", swizzleR.value(), swizzleG.value(), swizzleB.value(), swizzleA.value());
		if (!tb)
			ts += "\tUnresolved:\t" + String.format("[%s, %s, %s, %s]", swizzleR.state(), swizzleG.state(), swizzleB.state(), swizzleA.state());
		Logging.writeOut(ts);
		
		Logging.writeOut(border);
		Logging.writeOut(sWrap);
		Logging.writeOut(tWrap);
		
		Logging.unindent();
		
		Logging.unsetPad();
		GL.flushErrors();
	}
	
}
