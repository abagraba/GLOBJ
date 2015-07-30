package globj.objects.textures;

import globj.core.Context;
import globj.core.GL;
import globj.objects.BindTracker;
import globj.objects.framebuffers.FBOAttachable;
import globj.objects.framebuffers.values.FBOAttachment;
import globj.objects.textures.values.ImageDataType;
import globj.objects.textures.values.ImageFormat;
import globj.objects.textures.values.TextureFormat;
import globj.objects.textures.values.TextureTarget;

import java.nio.ByteBuffer;

import lwjgl.debug.GLDebug;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;

public final class Texture3D extends GLTexture3D implements FBOAttachable {
	
	private int w, h, d, basemap, maxmap;
	
	private Texture3D(String name, TextureFormat texformat) {
		super(name, texformat, TextureTarget.TEXTURE_3D);
	}
	
	protected static Texture3D create(String name, TextureFormat texformat, int w, int h, int d, int mipmaps) {
		return create(name, texformat, w, h, d, 0, mipmaps - 1);
	}
	
	protected static Texture3D create(String name, TextureFormat texformat, int w, int h, int d, int basemap, int maxmap) {
		Texture3D tex = new Texture3D(name, texformat);
		if (tex.id == 0) {
			GLDebug.glObjError(Texture3D.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		if (!checkBounds(new int[] { w, h, d }, new int[] { max, max, max }, tex))
			return null;
		
		tex.w = w;
		tex.h = h;
		tex.d = d;
		tex.basemap = Math.min(Math.max(0, basemap), levels(Math.max(w, Math.max(h, d))));
		tex.maxmap = Math.min(Math.max(tex.basemap, maxmap), levels(Math.max(w, Math.max(h, d))));
		
		tex.bind();
		setMipmaps(tex.target, tex.basemap, tex.maxmap);
		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage3D(tex.target.value, tex.maxmap + 1, texformat.value, w, h, d);
		}
		else {
			w = Math.max(1, w >> tex.basemap);
			h = Math.max(1, h >> tex.basemap);
			d = Math.max(1, d >> tex.basemap);
			for (int i = tex.basemap; i <= tex.maxmap; i++) {
				GL12.glTexImage3D(tex.target.value, i, texformat.value, w, h, d, 0, texformat.base, ImageDataType.UBYTE.value, (ByteBuffer) null);
				w = Math.max(1, w / 2);
				h = Math.max(1, h / 2);
				d = Math.max(1, d / 2);
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
	
	/**
	 * Sets the texel data in specified rectangle of mipmap level. Texture needs
	 * to be initialized with
	 * {@link #initializeTexture(int, int, int, TextureFormat)}. Rectangle must
	 * be within the bounds of the texture. [GL_TEXTURE_BASE_LEVEL + map].
	 */
	public void setData(int x, int y, int z, int w, int h, int d, int map, ImageFormat format, ImageDataType type, ByteBuffer data) {
		bind();
		GL12.glTexSubImage3D(target.value, map, x, y, z, w, h, d, format.value, type.value, data);
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
		GL30.glFramebufferTexture3D(GL30.GL_DRAW_FRAMEBUFFER, attachment.value, target.value, id, level, layer);
	}
	
	/**************************************************/
	
	@Override
	public void debugQuery() {
		GLDebug.flushErrors();
		GLDebug.setPad(32);
		
		GLDebug.write(GLDebug.fixedString(target + ":") + String.format("%s\t(%d x %d x $d)", name, w, h, d));
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
		GLDebug.write(rWrap);
		
		GLDebug.unindent();
		
		GLDebug.unsetPad();
		GLDebug.flushErrors();
	}
	
}
