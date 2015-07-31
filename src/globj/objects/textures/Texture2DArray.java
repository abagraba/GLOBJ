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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;



@NonNullByDefault
public final class Texture2DArray extends GLTexture2D implements FBOAttachable {
	
	private int w, h, layers, basemap, maxmap;
	
	
	private Texture2DArray(String name, TextureFormat texformat) {
		super(name, texformat, TextureTarget.TEXTURE_2D_ARRAY);
	}
	
	@Nullable
	protected static Texture2DArray create(String name, TextureFormat texformat, int w, int h, int layers, int mipmaps) {
		return create(name, texformat, w, h, layers, 0, mipmaps - 1);
	}
	
	@Nullable
	protected static Texture2DArray create(String name, TextureFormat texformat, int w, int h, int layers, int basemap, int maxmap) {
		Texture2DArray tex = new Texture2DArray(name, texformat);
		if (tex.id == 0) {
			GLDebug.glObjError(Texture2DArray.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		int maxlayers = Context.intConst(GL30.GL_MAX_ARRAY_TEXTURE_LAYERS);
		if (!checkBounds(new int[] { w, h, layers }, new int[] { max, max, maxlayers }, tex))
			return null;
			
		tex.w = w;
		tex.h = h;
		tex.layers = layers;
		tex.basemap = Math.min(Math.max(0, basemap), levels(Math.max(w, h)));
		tex.maxmap = Math.min(Math.max(tex.basemap, maxmap), levels(Math.max(w, h)));
		
		tex.bind();
		setMipmaps(tex.target, tex.basemap, tex.maxmap);
		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage3D(tex.target.value(), tex.maxmap + 1, texformat.value(), w, h, layers);
		}
		else {
			w = Math.max(1, w >> tex.basemap);
			h = Math.max(1, h >> tex.basemap);
			for (int i = tex.basemap; i <= tex.maxmap; i++) {
				GL12.glTexImage3D(tex.target.value(), i, texformat.value(), w, h, layers, 0, texformat.base(), ImageDataType.UBYTE.value(), (ByteBuffer) null);
				w = Math.max(1, w / 2);
				h = Math.max(1, h / 2);
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
	 * Sets the texel data in specified rectangle of mipmap level. Texture needs to be initialized with
	 * {@link #initializeTexture(int, int, int, TextureFormat)}. Rectangle must be within the bounds of the texture.
	 * [GL_TEXTURE_BASE_LEVEL + map].
	 */
	public void setData(int x, int y, int w, int h, int layeri, int layerf, int map, ImageFormat format, ImageDataType type, ByteBuffer data) {
		bind();
		GL12.glTexSubImage3D(target.value(), map, x, y, layeri, w, h, layerf, format.value(), type.value(), data);
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
		GL30.glFramebufferTextureLayer(GL30.GL_DRAW_FRAMEBUFFER, attachment.value(), id, level, layer);
	}
	
	/**************************************************/
	
	/********************** Debug *********************/
	/**************************************************/
	
	@Override
	public void debugQuery() {
		GLDebug.flushErrors();
		
		GLDebug.writef(GLDebug.ATTRIB_STRING + "\t(%d x %d) x %d", target, name, w, h, layers);
		GLDebug.indent();
		
		GLDebug.writef(GLDebug.ATTRIB_STRING, "Texture Format", texformat);
		
		GLDebug.writef(GLDebug.ATTRIB_STRING, "Wrapping Mode [S]", sWrap);
		GLDebug.writef(GLDebug.ATTRIB_STRING, "Wrapping Mode [T]", tWrap);
		
		if (minFilter.mipmaps() && maxmap > 0)
			GLDebug.writef(GLDebug.ATTRIB + "[%d, %d]", "Mipmap Range", basemap, maxmap);
			
		super.debugQuery();
		
		GLDebug.unindent();
		
		GLDebug.flushErrors();
	}
	
}
