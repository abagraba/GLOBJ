package lwjgl.core.objects.textures;

import java.nio.ByteBuffer;

import lwjgl.core.Context;
import lwjgl.core.GL;
import lwjgl.core.objects.GLObjectTracker;
import lwjgl.core.objects.framebuffers.FBOAttachable;
import lwjgl.core.objects.framebuffers.values.FBOAttachment;
import lwjgl.core.objects.textures.values.CubemapTarget;
import lwjgl.core.objects.textures.values.TextureFormat;
import lwjgl.core.objects.textures.values.TextureTarget;
import lwjgl.core.values.DataType;
import lwjgl.debug.Logging;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL42;

public class TextureCubemap extends GLTexture2D implements FBOAttachable {
	
	private static final GLObjectTracker<TextureCubemap> tracker = new GLObjectTracker<TextureCubemap>();
	private static final BindTracker curr = new BindTracker();
	
	public final static TextureTarget target = TextureTarget.TEXTURE_CUBEMAP;
	
	private int s, basemap, maxmap;
	
	private TextureCubemap(String name, TextureFormat texformat) {
		super(name, texformat, target);
	}
	
	public static TextureCubemap create(String name, TextureFormat texformat, int s, int mipmaps) {
		return create(name, texformat, s, 0, mipmaps - 1);
	}
	
	public static TextureCubemap create(String name, TextureFormat texformat, int s, int basemap, int maxmap) {
		if (tracker.contains(name)) {
			Logging.globjError(TextureCubemap.class, name, "Cannot create", "Already exists");
			return null;
		}
		TextureCubemap tex = new TextureCubemap(name, texformat);
		if (tex.id == 0) {
			Logging.globjError(TextureCubemap.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		
		if (maxmap < basemap)
			maxmap = basemap;
		int bmap = Math.max(0, Math.min(basemap, levels(s)));
		int mmap = Math.max(0, Math.min(maxmap, levels(s)));
		
		int max = Context.intConst(GL13.GL_MAX_CUBE_MAP_TEXTURE_SIZE);
		if (!checkBounds(new int[] { s, s }, new int[] { max, max }, tex))
			return null;

		tex.s = s;
		tex.basemap = basemap;
		tex.maxmap = maxmap;
		
		tex.bind();
		if (bmap != 0)
			GL11.glTexParameteri(target.value, GL12.GL_TEXTURE_BASE_LEVEL, bmap);
		GL11.glTexParameteri(target.value, GL12.GL_TEXTURE_MAX_LEVEL, mmap);
		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage2D(target.value, mmap + 1, texformat.value, s, s);
		}
		else {
			s = Math.max(1, s >> bmap);
			for (int i = bmap; i <= mmap; i++) {
				for (CubemapTarget target : CubemapTarget.values())
					GL11.glTexImage2D(target.value, i, texformat.value, s, s, 0, texformat.base, DataType.UBYTE.value, (ByteBuffer) null);
				s = Math.max(1, s / 2);
			}
		}
		tex.undobind();
		tracker.add(tex);
		return tex;
	}

	
	public static TextureCubemap get(String name) {
		return tracker.get(name);
	}
	
	protected static TextureCubemap get(int id) {
		return tracker.get(id);
	}
	
	public int target() {
		return GL13.GL_TEXTURE_CUBE_MAP;
	}
	
	/**************************************************/
	/********************** Bind **********************/
	/**************************************************/
	
	private static void bind(int tex) {
		curr.update(tex);
		if (tex == curr.last())
			return;
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, tex);
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
		tracker.remove(this);
	}
	
	/**************************************************/
	
	
	/*
	 * XXX When is this core? Latest 4.4?
	 */
	public void makeSeamless(boolean seamless) {
		if (GL.versionCheck(4, 4)) {
			bind();
			GL11.glTexParameteri(target(), GL32.GL_TEXTURE_CUBE_MAP_SEAMLESS, seamless ? 1 : 0);
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
	public void setData(int x, int y, int w, int h, int map, ImageFormat format, DataType type, ByteBuffer data) {
		bind();
		GL11.glTexSubImage2D(target(), map, x, y, w, h, format.value, type.value, data);
		undobind();
	}
	
	/**************************************************/
	/****************** FBOAttachable *****************/
	/**************************************************/
	/**
	 * @param level
	 *            mipmap level.
	 * @param layer
	 *            cubemap face. Use {@link CubemapTarget#layer}.
	 */
	@Override
	public void attachToFBO(FBOAttachment attachment, int level, int layer) {
		GL30.glFramebufferTexture2D(GL30.GL_DRAW_FRAMEBUFFER, attachment.value, target(), id, level);
	}
	
	/**************************************************/
	
	@Override
	public void debug() {
		GL.flushErrors();
		Logging.setPad(32);
		
		Logging.writeOut(Logging.fixedString(target + ":") + String.format("%s\t(%d x %d)", name, s, s));
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
	}
	
}
