package lwjgl.core.objects.textures;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import lwjgl.core.Context;
import lwjgl.core.GL;
import lwjgl.core.objects.GLObjectTracker;
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

public class Texture2D extends GLTexture2D implements FBOAttachable {
	
	private static final GLObjectTracker<Texture2D> tracker = new GLObjectTracker<Texture2D>();
	private static final BindTracker curr = new BindTracker();
	
	public final static TextureTarget target = TextureTarget.TEXTURE_2D;
	
	private int w, h, basemap, maxmap;

	private Texture2D(String name, TextureFormat texformat) {
		super(name, texformat, target);
	}
	
	public static Texture2D create(String name, TextureFormat texformat, int w, int h, int mipmaps) {
		return create(name, texformat, w, h, 0, mipmaps - 1);
	}
	
	public static Texture2D create(String name, TextureFormat texformat, int w, int h, int basemap, int maxmap) {
		if (tracker.contains(name)) {
			Logging.globjError(Texture2D.class, name, "Cannot create", "Already exists");
			return null;
		}
		Texture2D tex = new Texture2D(name, texformat);
		if (tex.id == 0) {
			Logging.globjError(Texture2D.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		
		if (maxmap < basemap)
			maxmap = basemap;
		int bmap = Math.max(0, Math.min(basemap, levels(Math.max(w, h))));
		int mmap = Math.max(0, Math.min(maxmap, levels(Math.max(w, h))));
		
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		if (!checkBounds(new int[] { w, h }, new int[] { max, max }, tex))
			return null;

		tex.w = w;
		tex.h = h;
		tex.basemap = basemap;
		tex.maxmap = maxmap;
		
		tex.bind();
		if (bmap != 0)
			GL11.glTexParameteri(target.value, GL12.GL_TEXTURE_BASE_LEVEL, bmap);
		GL11.glTexParameteri(target.value, GL12.GL_TEXTURE_MAX_LEVEL, mmap);
		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage2D(target.value, mmap + 1, texformat.value, w, h);
		}
		else {
			w = Math.max(1, w >> bmap);
			h = Math.max(1, h >> bmap);
			for (int i = bmap; i <= mmap; i++) {
				GL11.glTexImage2D(target.value, i, texformat.value, w, h, 0, texformat.base, DataType.UBYTE.value, (ByteBuffer) null);
				w = Math.max(1, w / 2);
				h = Math.max(1, h / 2);
			}
		}
		tex.undobind();
		tracker.add(tex);
		return tex;
	}
	
	public static Texture2D create(String name, BufferedImage image, int mipmaps) {
		int w = image.getWidth();
		int h = image.getHeight();
		if (tracker.contains(name)) {
			Logging.globjError(Texture2D.class, name, "Cannot create", "Already exists");
			return null;
		}
		TextureFormat texformat = TextureFormat.RGBA8;
		Texture2D tex = new Texture2D(name, texformat);
		if (tex.id == 0) {
			Logging.globjError(Texture2D.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		// TODO warn if clamped?
		int maps = Math.max(1, Math.min(mipmaps, levels(Math.max(tex.w, tex.h))));

		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		if (!checkBounds(new int[] { tex.w, tex.h }, new int[] { max, max }, tex))
			return null;

		tex.w = w;
		tex.h = h;
		tex.basemap = 0;
		tex.maxmap = mipmaps - 1;

		tex.bind();
		GL11.glTexParameteri(target.value, GL12.GL_TEXTURE_MAX_LEVEL, maps - 1);
		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage2D(target.value, maps, texformat.value, w, h);
		}
		else {
			for (int i = 0; i < maps; i++) {
				GL11.glTexImage2D(target.value, i, texformat.value, w, h, 0, texformat.base, DataType.UBYTE.value, (ByteBuffer) null);
				w = Math.max(1, w / 2);
				h = Math.max(1, h / 2);
			}
		}
		GL11.glTexSubImage2D(target.value, 0, 0, 0, w, h, ImageFormat.RGBA.value, DataType.UBYTE.value, ImageUtil.imageRGBAData(image));
		if(mipmaps > 1)
			GL30.glGenerateMipmap(target.value);
		tex.undobind();
		tracker.add(tex);
		return tex;
	}
	
	public static Texture2D get(String name) {
		return tracker.get(name);
	}
	
	protected static Texture2D get(int id) {
		return tracker.get(id);
	}
	
	/**************************************************/
	/********************** Bind **********************/
	/**************************************************/
	
	private static void bind(int tex) {
		curr.update(tex);
		if (tex == curr.last())
			return;
		GL11.glBindTexture(target.value, tex);
	}
	
	public void bind() {
		bind(id);
	}
	
	public void bindNone() {
		bind(0);
	}
	
	protected void undobind() {
		if (curr.last() != curr.value())
			GL11.glBindTexture(target.value, curr.revert());
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
	public void setData(int x, int y, int w, int h, int map, ImageFormat format, DataType type, ByteBuffer data) {
		bind();
		GL11.glTexSubImage2D(target.value, map, x, y, w, h, format.value, type.value, data);
		undobind();
	}
	
	public void setDataRGBA(BufferedImage src, int maps) {
		int w = src.getWidth();
		int h = src.getHeight();
		bind();
		GL11.glTexSubImage2D(target.value, 0, 0, 0, w, h, ImageFormat.RGBA.value, DataType.UBYTE.value, ImageUtil.imageRGBAData(src));
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
