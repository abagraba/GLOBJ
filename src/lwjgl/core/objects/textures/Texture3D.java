package lwjgl.core.objects.textures;

import java.nio.ByteBuffer; 

import lwjgl.core.Context;
import lwjgl.core.GL;
import lwjgl.core.GLObjectTracker;
import lwjgl.core.objects.framebuffers.FBOAttachable;
import lwjgl.core.objects.framebuffers.values.FBOAttachment;
import lwjgl.core.objects.textures.values.TextureFormat;
import lwjgl.core.objects.textures.values.TextureTarget;
import lwjgl.core.values.DataType;
import lwjgl.debug.Logging;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;

public class Texture3D extends GLTexture3D implements FBOAttachable {
	
	private static final GLObjectTracker<Texture3D> tracker = new GLObjectTracker<Texture3D>();
	private static final BindTracker curr = new BindTracker();
	
	public final static TextureTarget target = TextureTarget.TEXTURE_3D;
	
	private int w, h, d, basemap, maxmap;

	private Texture3D(String name, TextureFormat texformat) {
		super(name, texformat, target);
	}
	
	public static Texture3D create(String name, TextureFormat texformat, int w, int h, int d, int mipmaps) {
		return create(name, texformat, w, h, d, 0, mipmaps - 1);
	}
	
	public static Texture3D create(String name, TextureFormat texformat, int w, int h, int d, int basemap, int maxmap) {
		if (tracker.contains(name)) {
			Logging.globjError(Texture3D.class, name, "Cannot create", "Already exists");
			return null;
		}
		Texture3D tex = new Texture3D(name, texformat);
		if (tex.id == 0) {
			Logging.globjError(Texture3D.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		
		if (maxmap < basemap)
			maxmap = basemap;
		int bmap = Math.max(0, Math.min(basemap, levels(Math.max(w, Math.max(h, d)))));
		int mmap = Math.max(0, Math.min(maxmap, levels(Math.max(w, Math.max(h, d)))));
		
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		if (!checkBounds(new int[] { w, h, d }, new int[] { max, max, max }, tex))
			return null;

		tex.w = w;
		tex.h = h;
		tex.d = d;
		tex.basemap = basemap;
		tex.maxmap = maxmap;

		tex.bind();
		if (bmap != 0)
			GL11.glTexParameteri(target.value, GL12.GL_TEXTURE_BASE_LEVEL, bmap);
		GL11.glTexParameteri(target.value, GL12.GL_TEXTURE_MAX_LEVEL, mmap);
		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage3D(target.value, mmap + 1, texformat.value, w, h, d);
		}
		else {
			w = Math.max(1, w >> bmap);
			h = Math.max(1, h >> bmap);
			d = Math.max(1, d >> bmap);
			for (int i = bmap; i <= mmap; i++) {
				GL12.glTexImage3D(target.value, i, texformat.value, w, h, d, 0, texformat.base, DataType.UBYTE.value, (ByteBuffer) null);
				w = Math.max(1, w / 2);
				h = Math.max(1, h / 2);
				d = Math.max(1, d / 2);
			}
		}
		tex.undobind();
		tracker.add(tex);
		return tex;
	}
	
	public static Texture3D get(String name) {
		return tracker.get(name);
	}
	
	protected static Texture3D get(int id) {
		return tracker.get(id);
	}
	
	public int target() {
		return GL12.GL_TEXTURE_3D;
	}
	
	/**************************************************/
	/********************** Bind **********************/
	/**************************************************/
	
	private static void bind(int tex) {
		curr.update(tex);
		if (tex == curr.last())
			return;
		GL11.glBindTexture(GL12.GL_TEXTURE_3D, tex);
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
	
	
	/**
	 * Sets the texel data in specified rectangle of mipmap level. Texture needs
	 * to be initialized with
	 * {@link #initializeTexture(int, int, int, TextureFormat)}. Rectangle must
	 * be within the bounds of the texture. [GL_TEXTURE_BASE_LEVEL + map].
	 */
	public void setData(int x, int y, int z, int w, int h, int d, int map, ImageFormat format, DataType type, ByteBuffer data) {
		bind();
		GL12.glTexSubImage3D(target(), map, x, y, z, w, h, d, format.value, type.value, data);
		undobind();
	}
	
	/**************************************************/
	/****************** FBOAttachable *****************/
	/**************************************************/
	/**
	 * @param level
	 *            mipmap level.
	 * @param layer
	 *            z offset.
	 */
	@Override
	public void attachToFBO(FBOAttachment attachment, int level, int layer) {
		GL30.glFramebufferTexture3D(GL30.GL_DRAW_FRAMEBUFFER, attachment.value, target(), id, level, layer);
	}
	
	/**************************************************/
	
	@Override
	public void debug() {
		GL.flushErrors();
		Logging.setPad(32);
		
		Logging.writeOut(Logging.fixedString(target + ":") + String.format("%s\t(%d x %d x $d)", name, w, h, d));
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
		Logging.writeOut(rWrap);
		
		Logging.unindent();
		
		Logging.unsetPad();	}
	
}
