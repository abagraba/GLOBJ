package globj.objects.textures;

import globj.core.Context;
import globj.core.GL;
import globj.objects.BindTracker;
import globj.objects.framebuffers.FBOAttachable;
import globj.objects.framebuffers.values.FBOAttachment;
import globj.objects.textures.values.CubemapTarget;
import globj.objects.textures.values.ImageFormat;
import globj.objects.textures.values.TextureFormat;
import globj.objects.textures.values.TextureTarget;

import java.nio.ByteBuffer;

import lwjgl.core.values.DataType;
import lwjgl.debug.Logging;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL42;

public final class TextureCubemapArray extends GLTexture2D implements FBOAttachable {
	
	private static final BindTracker curr = new BindTracker();
	
	public final static TextureTarget target = TextureTarget.TEXTURE_CUBEMAP_ARRAY;
	
	private int s, layers, basemap, maxmap;
	
	private TextureCubemapArray(String name, TextureFormat texformat) {
		super(name, texformat, target);
	}
	
	protected static TextureCubemapArray create(String name, TextureFormat texformat, int s, int cubemaps, int mipmaps) {
		return create(name, texformat, s, cubemaps, 0, mipmaps - 1);
	}
	
	protected static TextureCubemapArray create(String name, TextureFormat texformat, int s, int cubemaps, int basemap, int maxmap) {
		TextureCubemapArray tex = new TextureCubemapArray(name, texformat);
		if (tex.id == 0) {
			Logging.globjError(TextureCubemapArray.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		
		if (maxmap < basemap)
			maxmap = basemap;
		int bmap = Math.max(0, Math.min(basemap, levels(s)));
		int mmap = Math.max(0, Math.min(maxmap, levels(s)));
		
		int max = Context.intConst(GL13.GL_MAX_CUBE_MAP_TEXTURE_SIZE);
		int maxlayers = Context.intConst(GL30.GL_MAX_ARRAY_TEXTURE_LAYERS);
		if (!checkBounds(new int[] { s, s, cubemaps }, new int[] { max, max, maxlayers / 6 }, tex))
			return null;
		
		tex.s = s;
		tex.layers = cubemaps;
		tex.basemap = basemap;
		tex.maxmap = maxmap;
		
		tex.bind();
		if (bmap != 0)
			GL11.glTexParameteri(target.value, GL12.GL_TEXTURE_BASE_LEVEL, bmap);
		GL11.glTexParameteri(target.value, GL12.GL_TEXTURE_MAX_LEVEL, mmap);
		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage3D(target.value, mmap + 1, texformat.value, s, s, cubemaps * 6);
		}
		else {
			s = Math.max(1, s >> bmap);
			for (int i = bmap; i <= mmap; i++) {
				GL12.glTexImage3D(target.value, i, texformat.value, s, s, cubemaps * 6, 0, texformat.base, DataType.UBYTE.value, (ByteBuffer) null);
				s = Math.max(1, s / 2);
			}
		}
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
		GL11.glBindTexture(GL40.GL_TEXTURE_CUBE_MAP_ARRAY, tex);
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
	
	/*
	 * XXX When is this core? Latest 4.4?
	 */
	public void makeSeamless(boolean seamless) {
		if (GL.versionCheck(4, 4)) {
			bind();
			GL11.glTexParameteri(target.value, GL32.GL_TEXTURE_CUBE_MAP_SEAMLESS, seamless ? 1 : 0);
			undobind();
		}
		else
			Logging.glWarning("Cannot use per texture seamless cubemaps. Version 4.4 required.");
	}
	
	/**
	 * Sets the texel data in specified rectangle of mipmap level. Texture needs
	 * to be initialized with
	 * {@link #initializeTexture(int, int, int, TextureFormat)}. Rectangle must
	 * be within the bounds of the texture. [GL_TEXTURE_BASE_LEVEL + map].
	 */
	
	// TODO make work for cubemaps
	public void setData(int x, int y, int w, int h, int map, ImageFormat format, DataType type, ByteBuffer data) {
		bind();
		GL11.glTexSubImage2D(target.value, map, x, y, w, h, format.value, type.value, data);
		undobind();
	}
	
	/**************************************************/
	/****************** FBOAttachable *****************/
	/**************************************************/
	/**
	 * @param level
	 *            mipmap level.
	 * @param layer
	 *            cubemap index and face. Use 6 * cubemap index +
	 *            {@link CubemapTarget#layer}.
	 */
	@Override
	public void attachToFBO(FBOAttachment attachment, int level, int layer) {
		GL30.glFramebufferTexture2D(GL30.GL_DRAW_FRAMEBUFFER, attachment.value, target.value, id, level);
	}
	
	/**************************************************/
	
	@Override
	public void debug() {
		GL.flushErrors();
		Logging.setPad(32);
		
		Logging.writeOut(Logging.fixedString(target + ":") + String.format("%s\t(%d x %d) x %d", name, s, s, layers));
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
		Logging.writeOut(tWrap);
		
		Logging.unindent();
		
		Logging.unsetPad();
		GL.flushErrors();
	}
}
