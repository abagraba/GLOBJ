package globj.objects.textures;


import java.nio.ByteBuffer;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;

import globj.core.Context;
import globj.core.GL;
import globj.objects.BindTracker;
import globj.objects.framebuffers.FBOAttachable;
import globj.objects.framebuffers.values.FBOAttachment;
import globj.objects.textures.values.ImageDataType;
import globj.objects.textures.values.ImageFormat;
import globj.objects.textures.values.TextureFormat;
import globj.objects.textures.values.TextureTarget;
import lwjgl.debug.GLDebug;



@NonNullByDefault
public final class Texture2DArray extends GLTexture2D implements FBOAttachable {
	
	private int w, h, layers, basemap, maxmap;
	
	private Texture2DArray(String name, TextureFormat texformat) {
		super(name, texformat, TextureTarget.TEXTURE_2D_ARRAY);
	}
	
	@Nullable
	protected static Texture2DArray create(String name, TextureFormat texformat, int width, int height, int layers, int mipmaps) {
		return create(name, texformat, width, height, layers, 0, mipmaps - 1);
	}
	
	@Nullable
	protected static Texture2DArray create(String name, TextureFormat texformat, int width, int height, int layers, int basemap, int maxmap) {
		Texture2DArray tex = new Texture2DArray(name, texformat);
		if (tex.id == 0) {
			GLDebug.glObjError(Texture2DArray.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		int maxlayers = Context.intConst(GL30.GL_MAX_ARRAY_TEXTURE_LAYERS);
		if (!checkBounds(new int[] { width, height, layers }, new int[] { max, max, maxlayers }, tex))
			return null;
			
		tex.w = width;
		tex.h = height;
		tex.layers = layers;
		tex.basemap = Math.min(Math.max(0, basemap), levels(Math.max(width, height)));
		tex.maxmap = Math.min(Math.max(tex.basemap, maxmap), levels(Math.max(width, height)));
		
		tex.bind();
		setMipmaps(tex.target, tex.basemap, tex.maxmap);
		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage3D(tex.target.value(), tex.maxmap + 1, texformat.value(), width, height, layers);
		}
		else {
			int w = Math.max(1, width >> tex.basemap);
			int h = Math.max(1, height >> tex.basemap);
			for (int i = tex.basemap; i <= tex.maxmap; i++) {
				GL12.glTexImage3D(tex.target.value(), i, texformat.value(), w, h, layers, 0, texformat.base(), ImageDataType.UBYTE.value(), (ByteBuffer) null);
				w = Math.max(1, w / 2);
				h = Math.max(1, h / 2);
			}
		}
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
	public void setData(int x, int y, int w, int h, int layeri, int layerf, int map, ImageFormat format, ImageDataType type, ByteBuffer data) {
		bind();
		GL12.glTexSubImage3D(target.value(), map, x, y, layeri, w, h, layerf, format.value(), type.value(), data);
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
		GLDebug.writef(GLDebug.ATTRIB_STRING + "\t(%d x %d) x %d", target + ":", name, w, h, layers);
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
		GLDebug.writef(GLDebug.ATTRIB_STRING+ "\t(%d x %d) x %d", target + ":", name, GL11.glGetTexLevelParameteri(target.value(), 0, GL11.GL_TEXTURE_WIDTH),
						GL11.glGetTexParameteri(target.value(), GL11.GL_TEXTURE_HEIGHT), GL11.glGetTexParameteri(target.value(), GL12.GL_TEXTURE_DEPTH));
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
