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
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;

public final class Texture1DArray extends GLTexture1D implements FBOAttachable {
	
	private static final BindTracker curr = new BindTracker();
	
	public final static TextureTarget target = TextureTarget.TEXTURE_1D_ARRAY;
	
	private int w, layers, basemap, maxmap;
	
	private Texture1DArray(String name, TextureFormat texformat) {
		super(name, texformat, target);
	}
	
	protected static Texture1DArray create(String name, TextureFormat texformat, int w, int layers, int basemap, int maxmap) {
		Texture1DArray tex = new Texture1DArray(name, texformat);
		if (tex.id == 0) {
			Logging.globjError(Texture1DArray.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		if (maxmap < basemap)
			maxmap = basemap;
		int bmap = Math.max(0, Math.min(basemap, levels(w)));
		int mmap = Math.max(0, Math.min(maxmap, levels(w)));
		
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		int maxlayers = Context.intConst(GL30.GL_MAX_ARRAY_TEXTURE_LAYERS);
		if (!checkBounds(new int[] { w, layers }, new int[] { max, maxlayers }, tex))
			return null;
		
		tex.w = w;
		tex.layers = layers;
		tex.basemap = basemap;
		tex.maxmap = maxmap;
		
		tex.bind();
		if (bmap != 0)
			GL11.glTexParameteri(target.value, GL12.GL_TEXTURE_BASE_LEVEL, bmap);
		GL11.glTexParameteri(target.value, GL12.GL_TEXTURE_MAX_LEVEL, mmap);
		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage2D(target.value, mmap + 1, texformat.value, w, layers);
		}
		else {
			w = Math.max(1, w >> bmap);
			for (int i = bmap; i <= mmap; i++) {
				GL11.glTexImage2D(target.value, i, texformat.value, w, layers, 0, texformat.base, DataType.UBYTE.value, (ByteBuffer) null);
				w = Math.max(1, w / 2);
			}
		}
		tex.undobind();
		return tex;
	}
	
	protected static Texture1DArray create(String name, BufferedImage image, int mipmaps) {
		int w = image.getWidth();
		int layers = image.getHeight();
		TextureFormat texformat = TextureFormat.RGBA8;
		Texture1DArray tex = new Texture1DArray(name, texformat);
		if (tex.id == 0) {
			Logging.globjError(Texture1DArray.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		int maps = Math.max(1, Math.min(mipmaps, levels(tex.w)));
		
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		int maxlayers = Context.intConst(GL30.GL_MAX_ARRAY_TEXTURE_LAYERS);
		if (!checkBounds(new int[] { w, layers }, new int[] { max, maxlayers }, tex))
			return null;
		
		tex.w = w;
		tex.layers = layers;
		tex.basemap = 0;
		tex.maxmap = maps - 1;
		
		tex.bind();
		GL11.glTexParameteri(target.value, GL12.GL_TEXTURE_MAX_LEVEL, maps - 1);
		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage2D(target.value, maps, texformat.value, w, layers);
		}
		else {
			for (int i = 0; i <= maps; i++) {
				GL11.glTexImage2D(target.value, i, texformat.value, w, layers, 0, texformat.base, DataType.UBYTE.value, (ByteBuffer) null);
				w = Math.max(1, w / 2);
			}
		}
		GL11.glTexSubImage2D(target.value, 0, 0, 0, w, layers, ImageFormat.RGBA.value, DataType.UBYTE.value, ImageUtil.imageRGBAData(image));
		if (mipmaps > 1)
			GL30.glGenerateMipmap(target.value);
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
		GL11.glBindTexture(GL30.GL_TEXTURE_1D_ARRAY, tex);
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
	
	public void destroy() {
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
	public void setData(int x, int w, int layeri, int layerf, int map, ImageFormat format, DataType type, ByteBuffer data) {
		bind();
		GL11.glTexSubImage2D(target.value, map, x, layeri, w, layerf, format.value, type.value, data);
		undobind();
	}
	
	/**************************************************/
	/****************** FBOAttachable *****************/
	/**************************************************/
	/**
	 * @param level
	 *            mipmap level.
	 * @param layer
	 *            texture index.
	 */
	@Override
	public void attachToFBO(FBOAttachment attachment, int level, int layer) {
		GL30.glFramebufferTextureLayer(GL30.GL_DRAW_FRAMEBUFFER, attachment.value, id, level, layer);
	}
	
	/**************************************************/
	
	@Override
	public void debug() {
		GL.flushErrors();
		Logging.setPad(32);
		
		Logging.writeOut(Logging.fixedString(target + ":") + String.format("%s\t(%d) x %d", name, w, layers));
		Logging.indent();
		
		Logging.writeOut(Logging.fixedString("Texture Format:") + texformat);
		
		Logging.writeOut(minFilter);
		Logging.writeOut(magFilter);
		
		boolean tb = lodMin.resolved() && lodMax.resolved() && lodBias.resolved();
		String ts = Logging.fixedString("LOD Range:") + String.format("[%4f, %4f] + %4f", lodMin.value(), lodMax.value(), lodBias.value());
		if (!tb)
			ts += "\tUnresolved:\t" + String.format("[%4f, %4f] + %4f", lodMin.state(), lodMax.state(), lodBias.state());
		Logging.writeOut(ts);
		
		if (minFilter.value().mipmaps && maxmap > 0)
			Logging.writeOut(Logging.fixedString("Mipmap Range:") + String.format("[%d, %d]", basemap, maxmap));
		
		tb = swizzleR.resolved() && swizzleG.resolved() && swizzleB.resolved() && swizzleA.resolved();
		ts = Logging.fixedString("Texture Swizzle:")
				+ String.format("[%s, %s, %s, %s]", swizzleR.value(), swizzleG.value(), swizzleB.value(), swizzleA.value());
		if (!tb)
			ts += "\tUnresolved:\t" + String.format("[%s, %s, %s, %s]", swizzleR.state(), swizzleG.state(), swizzleB.state(), swizzleA.state());
		Logging.writeOut(ts);
		
		Logging.writeOut(border);
		Logging.writeOut(sWrap);
		
		Logging.unindent();
		
		Logging.unsetPad();
		GL.flushErrors();
	}
	
}
