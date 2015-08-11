package globj.objects.textures;


import static lwjgl.debug.GLDebug.writef;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;

import globj.core.Context;
import globj.core.Window;
import globj.core.utils.ImageUtil;
import globj.objects.BindTracker;
import globj.objects.framebuffers.FBOAttachable;
import globj.objects.framebuffers.values.FBOAttachment;
import globj.objects.textures.values.ImageDataType;
import globj.objects.textures.values.ImageFormat;
import globj.objects.textures.values.TextureFormat;
import globj.objects.textures.values.TextureTarget;
import lwjgl.debug.GLDebug;



@NonNullByDefault
public final class Texture1DArray extends GLTexture1D implements FBOAttachable {
	
	private int w, layers, basemap, maxmap;
	
	private Texture1DArray(String name, TextureFormat texformat) {
		super(name, texformat, TextureTarget.TEXTURE_1D_ARRAY);
	}
	
	@Nullable
	protected static Texture1DArray create(String name, TextureFormat texformat, int width, int layers, int mipmaps) {
		return create(name, texformat, width, layers, 0, mipmaps - 1);
	}
	
	@Nullable
	protected static Texture1DArray create(String name, TextureFormat texformat, int width, int layers, int basemap, int maxmap) {
		Texture1DArray tex = new Texture1DArray(name, texformat);
		if (tex.id == 0) {
			GLDebug.glObjError(Texture1DArray.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		int maxlayers = Context.intConst(GL30.GL_MAX_ARRAY_TEXTURE_LAYERS);
		if (!checkBounds(new int[] { width, layers }, new int[] { max, maxlayers }, tex))
			return null;
			
		tex.w = width;
		tex.layers = layers;
		tex.basemap = Math.min(Math.max(0, basemap), levels(width));
		tex.maxmap = Math.min(Math.max(tex.basemap, maxmap), levels(width));
		
		tex.bind();
		setMipmaps(tex.target, tex.basemap, tex.maxmap);
		
		if (Window.versionCheck(4, 2)) {
			GL42.glTexStorage2D(tex.target.value(), tex.maxmap + 1, texformat.value(), width, layers);
		}
		else {
			int w = Math.max(1, width >> tex.basemap);
			for (int i = tex.basemap; i <= tex.maxmap; i++) {
				GL11.glTexImage2D(tex.target.value(), i, texformat.value(), w, layers, 0, texformat.base(), ImageDataType.UBYTE.value(), (ByteBuffer) null);
				w = Math.max(1, w / 2);
			}
		}
		tex.undobind();
		return tex;
	}
	
	@Nullable
	protected static Texture1DArray create(String name, BufferedImage image, int mipmaps) {
		int w = image.getWidth();
		int layers = image.getHeight();
		TextureFormat texformat = TextureFormat.RGBA8;
		Texture1DArray tex = new Texture1DArray(name, texformat);
		if (tex.id == 0) {
			GLDebug.glObjError(Texture1DArray.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		int maps = Math.max(1, Math.min(mipmaps, levels(tex.w)));
		if (mipmaps > maps)
			writef("Mipmaps clamped to %d for Texture %s", maps, name);
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		int maxlayers = Context.intConst(GL30.GL_MAX_ARRAY_TEXTURE_LAYERS);
		if (!checkBounds(new int[] { w, layers }, new int[] { max, maxlayers }, tex))
			return null;
			
		tex.w = w;
		tex.layers = layers;
		tex.basemap = 0;
		tex.maxmap = maps - 1;
		
		tex.bind();
		GL11.glTexParameteri(tex.target.value(), GL12.GL_TEXTURE_MAX_LEVEL, maps - 1);
		if (Window.versionCheck(4, 2)) {
			GL42.glTexStorage2D(tex.target.value(), maps, texformat.value(), w, layers);
		}
		else {
			for (int i = 0; i <= maps; i++) {
				GL11.glTexImage2D(tex.target.value(), i, texformat.value(), w, layers, 0, texformat.base(), ImageDataType.UBYTE.value(), (ByteBuffer) null);
				w = Math.max(1, w / 2);
			}
		}
		GL11.glTexSubImage2D(tex.target.value(), 0, 0, 0, w, layers, ImageFormat.RGBA.value(), ImageDataType.UBYTE.value(), ImageUtil.imageRGBAData(image));
		if (mipmaps > 1)
			GL30.glGenerateMipmap(tex.target.value());
		tex.undobind();
		return tex;
	}
	
	/**************************************************
	 ********************** Bind **********************
	 **************************************************/
	
	private static final BindTracker bindTracker = new BindTracker();
	
	@Override
	protected BindTracker bindingTracker() {
		return bindTracker;
	}
	
	/**************************************************/
	
	/**
	 * Sets the texel data in specified rectangle of mipmap level. Rectangle must be within the bounds of the texture.
	 * [GL_TEXTURE_BASE_LEVEL + map].
	 */
	public void setData(int x, int w, int layeri, int layerf, int map, ImageFormat format, ImageDataType type, ByteBuffer data) {
		bind();
		GL11.glTexSubImage2D(target.value(), map, x, layeri, w, layerf, format.value(), type.value(), data);
		undobind();
	}
	
	/**************************************************
	 ****************** FBOAttachable *****************
	 **************************************************/
	
	/**
	 * @param level
	 *            mipmap level.
	 * @param layer
	 *            texture index.
	 */
	@Override
	public void attachToFBO(FBOAttachment attachment, int level, int layer) {
		GL30.glFramebufferTextureLayer(GL30.GL_DRAW_FRAMEBUFFER, attachment.value(), id, level, layer);
	}
	
	/**************************************************
	 ********************** Debug *********************
	 **************************************************/
	
	@Override
	public void debug() {
		GLDebug.writef(GLDebug.ATTRIB_STRING + "\t(%d) x %d", target + ":", name, w, layers);
		//#formatter:off
		GLDebug.indent();
			GLDebug.writef(GLDebug.ATTRIB_STRING, "Texture Format:", texformat);
			if (minFilter.mipmaps() && maxmap > 0)
				GLDebug.writef(GLDebug.ATTRIB + "[%d, %d]", "Mipmap Range:", basemap, maxmap);
			super.debug();
		GLDebug.unindent();
		//#formatter:on
		
	}
	
	@Override
	public void debugQuery() {
		GLDebug.flushErrors();
		GLDebug.writef(GLDebug.ATTRIB_STRING+ "\t(%d) x %d", target + ":", name, GL11.glGetTexLevelParameteri(target.value(), 0, GL11.GL_TEXTURE_WIDTH),
						GL11.glGetTexParameteri(target.value(), GL11.GL_TEXTURE_HEIGHT));
		//#formatter:off
		GLDebug.indent();
			GLDebug.writef(GLDebug.ATTRIB_STRING, "Texture Format:", TextureFormat.get(GL11.glGetTexLevelParameteri(target.value(), 0, GL11.GL_TEXTURE_INTERNAL_FORMAT)));
			if (minFilter.mipmaps() && maxmap > 0)
				GLDebug.writef(GLDebug.ATTRIB + "[%d, %d]", "Mipmap Range:", GL11.glGetTexParameteri(target.value(), GL12.GL_TEXTURE_BASE_LEVEL), 
				               GL11.glGetTexParameteri(target.value(), GL12.GL_TEXTURE_MAX_LEVEL));
			super.debugQuery();
		GLDebug.unindent();
		//#formatter:on
		GLDebug.flushErrors();
	}
	
}
