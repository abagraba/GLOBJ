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

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import lwjgl.core.utils.ImageUtil;
import lwjgl.debug.GLDebug;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;

public final class Texture1DArray extends GLTexture1D implements FBOAttachable {
	
	private int w, layers, basemap, maxmap;
	
	private Texture1DArray(String name, TextureFormat texformat) {
		super(name, texformat, TextureTarget.TEXTURE_1D_ARRAY);
	}
	
	protected static Texture1DArray create(String name, TextureFormat texformat, int w, int layers, int basemap, int maxmap) {
		Texture1DArray tex = new Texture1DArray(name, texformat);
		if (tex.id == 0) {
			GLDebug.globjError(Texture1DArray.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		int maxlayers = Context.intConst(GL30.GL_MAX_ARRAY_TEXTURE_LAYERS);
		if (!checkBounds(new int[] { w, layers }, new int[] { max, maxlayers }, tex))
			return null;
		
		tex.w = w;
		tex.layers = layers;
		tex.basemap = Math.min(Math.max(0, basemap), levels(w));
		tex.maxmap = Math.min(Math.max(tex.basemap, maxmap), levels(w));
				
		tex.bind();
		setMipmaps(tex.target, tex.basemap, tex.maxmap);

		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage2D(tex.target.value, tex.maxmap + 1, texformat.value, w, layers);
		}
		else {
			w = Math.max(1, w >> tex.basemap);
			for (int i = tex.basemap; i <= tex.maxmap; i++) {
				GL11.glTexImage2D(tex.target.value, i, texformat.value, w, layers, 0, texformat.base, ImageDataType.UBYTE.value, (ByteBuffer) null);
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
			GLDebug.globjError(Texture1DArray.class, name, "Cannot create", "No ID could be allocated");
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
		GL11.glTexParameteri(tex.target.value, GL12.GL_TEXTURE_MAX_LEVEL, maps - 1);
		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage2D(tex.target.value, maps, texformat.value, w, layers);
		}
		else {
			for (int i = 0; i <= maps; i++) {
				GL11.glTexImage2D(tex.target.value, i, texformat.value, w, layers, 0, texformat.base, ImageDataType.UBYTE.value, (ByteBuffer) null);
				w = Math.max(1, w / 2);
			}
		}
		GL11.glTexSubImage2D(tex.target.value, 0, 0, 0, w, layers, ImageFormat.RGBA.value, ImageDataType.UBYTE.value, ImageUtil.imageRGBAData(image));
		if (mipmaps > 1)
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
	public void setData(int x, int w, int layeri, int layerf, int map, ImageFormat format, ImageDataType type, ByteBuffer data) {
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
		GLDebug.setPad(32);
		
		GLDebug.write(GLDebug.fixedString(target + ":") + String.format("%s\t(%d) x %d", name, w, layers));
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
		
		GLDebug.unindent();
		
		GLDebug.unsetPad();
		GL.flushErrors();
	}
	
}