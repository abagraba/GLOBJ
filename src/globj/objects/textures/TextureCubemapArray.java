package globj.objects.textures;

import globj.core.Context;
import globj.core.GL;
import globj.objects.BindTracker;
import globj.objects.framebuffers.FBOAttachable;
import globj.objects.framebuffers.values.FBOAttachment;
import globj.objects.textures.values.CubemapTarget;
import globj.objects.textures.values.ImageDataType;
import globj.objects.textures.values.ImageFormat;
import globj.objects.textures.values.TextureFormat;
import globj.objects.textures.values.TextureTarget;

import java.nio.ByteBuffer;

import lwjgl.debug.GLDebug;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL42;

public final class TextureCubemapArray extends GLTexture2D implements FBOAttachable {
	
	private int s, layers, basemap, maxmap;
	
	private TextureCubemapArray(String name, TextureFormat texformat) {
		super(name, texformat, TextureTarget.TEXTURE_CUBEMAP_ARRAY);
	}
	
	protected static TextureCubemapArray create(String name, TextureFormat texformat, int s, int cubemaps, int mipmaps) {
		return create(name, texformat, s, cubemaps, 0, mipmaps - 1);
	}
	
	protected static TextureCubemapArray create(String name, TextureFormat texformat, int s, int cubemaps, int basemap, int maxmap) {
		TextureCubemapArray tex = new TextureCubemapArray(name, texformat);
		if (tex.id == 0) {
			GLDebug.glObjError(TextureCubemapArray.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		
		int max = Context.intConst(GL13.GL_MAX_CUBE_MAP_TEXTURE_SIZE);
		int maxlayers = Context.intConst(GL30.GL_MAX_ARRAY_TEXTURE_LAYERS);
		if (!checkBounds(new int[] { s, s, cubemaps }, new int[] { max, max, maxlayers / 6 }, tex))
			return null;
		
		tex.s = s;
		tex.layers = cubemaps;
		tex.basemap = Math.min(Math.max(0, basemap), levels(s));
		tex.maxmap = Math.min(Math.max(tex.basemap, maxmap), levels(s));
		
		tex.bind();
		setMipmaps(tex.target, tex.basemap, tex.maxmap);
		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage3D(tex.target.value, tex.maxmap + 1, texformat.value, s, s, cubemaps * 6);
		}
		else {
			s = Math.max(1, s >> tex.basemap);
			for (int i = tex.basemap; i <= tex.maxmap; i++) {
				GL12.glTexImage3D(tex.target.value, i, texformat.value, s, s, cubemaps * 6, 0, texformat.base, ImageDataType.UBYTE.value, (ByteBuffer) null);
				s = Math.max(1, s / 2);
			}
		}
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
			GLDebug.glWarning("Cannot use per texture seamless cubemaps. Version 4.4 required.");
	}
	
	/**
	 * Sets the texel data in specified rectangle of mipmap level. Texture needs
	 * to be initialized with
	 * {@link #initializeTexture(int, int, int, TextureFormat)}. Rectangle must
	 * be within the bounds of the texture. [GL_TEXTURE_BASE_LEVEL + map].
	 */
	
	// TODO make work for cubemaps
	public void setData(int x, int y, int w, int h, int map, ImageFormat format, ImageDataType type, ByteBuffer data) {
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
	public void debugQuery() {
		GLDebug.flushErrors();
		GLDebug.setPad(32);
		
		GLDebug.write(GLDebug.fixedString(target + ":") + String.format("%s\t(%d x %d) x %d", name, s, s, layers));
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
		GLDebug.flushErrors();
	}
}
