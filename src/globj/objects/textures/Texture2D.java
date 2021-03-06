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
public final class Texture2D extends GLTexture2D implements FBOAttachable {
	
	private int w, h, basemap, maxmap;
	
	private Texture2D(String name, TextureFormat texformat) {
		super(name, texformat, TextureTarget.TEXTURE_2D);
	}
	
	@Nullable
	protected static Texture2D create(String name, TextureFormat texformat, int width, int height, int mipmaps) {
		return create(name, texformat, width, height, 0, mipmaps - 1);
	}
	
	@Nullable
	protected static Texture2D create(String name, TextureFormat texformat, int width, int height, int basemap, int maxmap) {
		Texture2D tex = new Texture2D(name, texformat);
		if (tex.id == 0) {
			GLDebug.glObjError(Texture2D.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		if (!checkBounds(new int[] { width, height }, new int[] { max, max }, tex))
			return null;
			
		tex.w = width;
		tex.h = height;
		tex.basemap = Math.min(Math.max(0, basemap), levels(Math.max(width, height)));
		tex.maxmap = Math.min(Math.max(tex.basemap, maxmap), levels(Math.max(width, height)));
		
		tex.bind();
		setMipmaps(tex.target, tex.basemap, tex.maxmap);
		if (Window.versionCheck(4, 2)) {
			GL42.glTexStorage2D(tex.target.value(), tex.maxmap + 1, texformat.value(), width, height);
		}
		else {
			int w = Math.max(1, width >> tex.basemap);
			int h = Math.max(1, height >> tex.basemap);
			for (int i = tex.basemap; i <= tex.maxmap; i++) {
				GL11.glTexImage2D(tex.target.value(), i, texformat.value(), w, h, 0, texformat.base(), ImageDataType.UBYTE.value(), (ByteBuffer) null);
				w = Math.max(1, w / 2);
				h = Math.max(1, h / 2);
			}
		}
		tex.undobind();
		return tex;
	}
	
	@Nullable
	protected static Texture2D create(String name, BufferedImage image, int mipmaps) {
		int w = image.getWidth();
		int h = image.getHeight();
		TextureFormat texformat = TextureFormat.RGBA8;
		Texture2D tex = new Texture2D(name, texformat);
		if (tex.id == 0) {
			GLDebug.glObjError(Texture2D.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		int maps = Math.max(1, Math.min(mipmaps, levels(Math.max(tex.w, tex.h)) + 1));
		if (mipmaps > maps)
			writef("Mipmaps clamped to %d for Texture %s", maps, name);
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		if (!checkBounds(new int[] { tex.w, tex.h }, new int[] { max, max }, tex))
			return null;
			
		tex.w = w;
		tex.h = h;
		tex.basemap = 0;
		tex.maxmap = maps - 1;
		
		tex.bind();
		setMipmaps(tex.target, tex.basemap, tex.maxmap);
		if (Window.versionCheck(4, 2)) {
			GL42.glTexStorage2D(tex.target.value(), maps, texformat.value(), w, h);
		}
		else {
			for (int i = 0; i < maps; i++) {
				GL11.glTexImage2D(tex.target.value(), i, texformat.value(), w, h, 0, texformat.base(), ImageDataType.UBYTE.value(), (ByteBuffer) null);
				w = Math.max(1, w / 2);
				h = Math.max(1, h / 2);
			}
		}
		GL11.glTexSubImage2D(tex.target.value(), 0, 0, 0, w, h, ImageFormat.RGBA.value(), ImageDataType.UBYTE.value(), ImageUtil.imageRGBAData(image));
		if (maps > 1)
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
	 * Sets the texel data in specified rectangle of mipmap level. Texture needs to be initialized with
	 * {@link #initializeTexture(int, int, int, TextureFormat)}. Rectangle must be within the bounds of the texture.
	 * [GL_TEXTURE_BASE_LEVEL + map].
	 */
	public void setData(int x, int y, int w, int h, int map, ImageFormat format, ImageDataType type, ByteBuffer data) {
		bind();
		GL11.glTexSubImage2D(target.value(), map, x, y, w, h, format.value(), type.value(), data);
		undobind();
	}
	
	public void setDataRGBA(BufferedImage src, int maps) {
		int width = src.getWidth();
		int height = src.getHeight();
		bind();
		GL11.glTexSubImage2D(target.value(), 0, 0, 0, width, height, ImageFormat.RGBA.value(), ImageDataType.UBYTE.value(), ImageUtil.imageRGBAData(src));
		undobind();
		return;
	}
	
	/**************************************************
	 ****************** FBOAttachable *****************
	 **************************************************/
	
	/**
	 * @param level
	 *            mipmap level.
	 * @param layer
	 *            unused.
	 */
	@Override
	public void attachToFBO(FBOAttachment attachment, int level, int layer) {
		GL30.glFramebufferTexture2D(GL30.GL_DRAW_FRAMEBUFFER, attachment.value(), target.value(), id, level);
	}
	
	/**************************************************
	 ********************** Debug *********************
	 **************************************************/
	
	@Override
	public void debug() {
		GLDebug.writef(GLDebug.ATTRIB_STRING + "\t(%d) x %d", target + ":", name, w, h);
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
		GLDebug.writef(GLDebug.ATTRIB_STRING+ "\t(%d x %d)", target + ":", name, GL11.glGetTexLevelParameteri(target.value(), 0, GL11.GL_TEXTURE_WIDTH),
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
