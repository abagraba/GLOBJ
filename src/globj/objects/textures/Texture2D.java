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

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;

public final class Texture2D extends GLTexture2D implements FBOAttachable {
	
	private int w, h, basemap, maxmap;
	
	private Texture2D(String name, TextureFormat texformat) {
		super(name, texformat, TextureTarget.TEXTURE_2D);
	}
	
	protected static Texture2D create(String name, TextureFormat texformat, int w, int h, int basemap, int maxmap) {
		Texture2D tex = new Texture2D(name, texformat);
		if (tex.id == 0) {
			GLDebug.globjError(Texture2D.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		if (!checkBounds(new int[] { w, h }, new int[] { max, max }, tex))
			return null;
		
		tex.w = w;
		tex.h = h;
		tex.basemap = Math.min(Math.max(0, basemap), levels(Math.max(w, h)));
		tex.maxmap = Math.min(Math.max(tex.basemap, maxmap), levels(Math.max(w, h)));
	
		tex.bind();
		setMipmaps(tex.target, tex.basemap, tex.maxmap);
		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage2D(tex.target.value, tex.maxmap + 1, texformat.value, w, h);
		}
		else {
			w = Math.max(1, w >> tex.basemap);
			h = Math.max(1, h >> tex.basemap);
			for (int i = tex.basemap; i <= tex.maxmap; i++) {
				GL11.glTexImage2D(tex.target.value, i, texformat.value, w, h, 0, texformat.base, ImageDataType.UBYTE.value, (ByteBuffer) null);
				w = Math.max(1, w / 2);
				h = Math.max(1, h / 2);
			}
		}
		tex.undobind();
		return tex;
	}
	
	protected static Texture2D create(String name, BufferedImage image, int mipmaps) {
		int w = image.getWidth();
		int h = image.getHeight();
		TextureFormat texformat = TextureFormat.RGBA8;
		Texture2D tex = new Texture2D(name, texformat);
		if (tex.id == 0) {
			GLDebug.globjError(Texture2D.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		// TODO warn if clamped?
		int maps = Math.max(1, Math.min(mipmaps, levels(Math.max(tex.w, tex.h)) + 1));
		
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		if (!checkBounds(new int[] { tex.w, tex.h }, new int[] { max, max }, tex))
			return null;
		
		tex.w = w;
		tex.h = h;
		tex.basemap = 0;
		tex.maxmap = maps - 1;
		
		tex.bind();
		setMipmaps(tex.target, tex.basemap, tex.maxmap);
		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage2D(tex.target.value, maps, texformat.value, w, h);
		}
		else {
			for (int i = 0; i < maps; i++) {
				GL11.glTexImage2D(tex.target.value, i, texformat.value, w, h, 0, texformat.base, ImageDataType.UBYTE.value, (ByteBuffer) null);
				w = Math.max(1, w / 2);
				h = Math.max(1, h / 2);
			}
		}
		GL11.glTexSubImage2D(tex.target.value, 0, 0, 0, w, h, ImageFormat.RGBA.value, ImageDataType.UBYTE.value, ImageUtil.imageRGBAData(image));
		if (maps > 1)
			GL30.glGenerateMipmap(tex.target.value);
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
	 * Sets the texel data in specified rectangle of mipmap level. Texture needs
	 * to be initialized with
	 * {@link #initializeTexture(int, int, int, TextureFormat)}. Rectangle must
	 * be within the bounds of the texture. [GL_TEXTURE_BASE_LEVEL + map].
	 */
	public void setData(int x, int y, int w, int h, int map, ImageFormat format, ImageDataType type, ByteBuffer data) {
		bind();
		GL11.glTexSubImage2D(target.value, map, x, y, w, h, format.value, type.value, data);
		undobind();
	}
	
	public void setDataRGBA(BufferedImage src, int maps) {
		int w = src.getWidth();
		int h = src.getHeight();
		bind();
		GL11.glTexSubImage2D(target.value, 0, 0, 0, w, h, ImageFormat.RGBA.value, ImageDataType.UBYTE.value, ImageUtil.imageRGBAData(src));
		undobind();
		return;
	}
	
	/**************************************************/
	/****************** FBOAttachable *****************/
	/**************************************************/
	/**
	 * @param level
	 *            mipmap level.
	 * @param layer
	 *            unused.
	 */
	@Override
	public void attachToFBO(FBOAttachment attachment, int level, int layer) {
		GL30.glFramebufferTexture2D(GL30.GL_DRAW_FRAMEBUFFER, attachment.value, target.value, id, level);
	}
	
	/**************************************************/
	
	@Override
	public void debug() {
		GL.flushErrors();
		GLDebug.setPad(32);
		
		GLDebug.write(GLDebug.fixedString(target + ":") + String.format("%s\t(%d x %d)", name, w, h));
		GLDebug.indent();
		
		GLDebug.write(GLDebug.fixedString("Texture Format:") + texformat);
		
		GLDebug.write(minFilter);
		GLDebug.write(magFilter);
		
		boolean tb = lodMin.resolved() && lodMax.resolved() && lodBias.resolved();
		String ts = GLDebug.fixedString("LOD Range:") + String.format("[%4f, %4f] + %4f", lodMin.value(), lodMax.value(), lodBias.value());
		if (!tb)
			ts += "\tUnresolved:\t" + String.format("[%4f, %4f] + %4f", lodMin.state(), lodMax.state(), lodBias.state());
		GLDebug.write(ts);
		
		if (minFilter.value().mipmaps && maxmap > 0)
			GLDebug.write(GLDebug.fixedString("Mipmap Range:") + String.format("[%d, %d]", basemap, maxmap));
		
		tb = swizzleR.resolved() && swizzleG.resolved() && swizzleB.resolved() && swizzleA.resolved();
		ts = GLDebug.fixedString("Texture Swizzle:")
				+ String.format("[%s, %s, %s, %s]", swizzleR.value(), swizzleG.value(), swizzleB.value(), swizzleA.value());
		if (!tb)
			ts += "\tUnresolved:\t" + String.format("[%s, %s, %s, %s]", swizzleR.state(), swizzleG.state(), swizzleB.state(), swizzleA.state());
		GLDebug.write(ts);
		
		GLDebug.write(border);
		GLDebug.write(sWrap);
		GLDebug.write(tWrap);
		
		GLDebug.unindent();
		
		GLDebug.unsetPad();
		GL.flushErrors();
	}
}
