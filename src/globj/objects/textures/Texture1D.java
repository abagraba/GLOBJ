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
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;



@NonNullByDefault
public final class Texture1D extends GLTexture1D implements FBOAttachable {
	
	private int	w, basemap, maxmap;
	
	
	private Texture1D(String name, TextureFormat texformat) {
		super(name, texformat, TextureTarget.TEXTURE_1D);
	}
	
	@Nullable
	protected static Texture1D create(String name, TextureFormat texformat, int w, int mipmaps) {
		return create(name, texformat, w, 0, mipmaps - 1);
	}
	
	@Nullable
	protected static Texture1D create(String name, TextureFormat texformat, int w, int basemap, int maxmap) {
		Texture1D tex = new Texture1D(name, texformat);
		if (tex.id == 0) {
			GLDebug.glObjError(Texture1D.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		if (!checkBounds(new int[] { w }, new int[] { max }, tex))
			return null;
		
		tex.w = w;
		tex.basemap = Math.min(Math.max(0, basemap), levels(w));
		tex.maxmap = Math.min(Math.max(tex.basemap, maxmap), levels(w));
		
		tex.bind();
		setMipmaps(tex.target, tex.basemap, tex.maxmap);
		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage1D(tex.target.value, tex.maxmap + 1, texformat.value, w);
		}
		else {
			w = Math.max(1, w >> tex.basemap);
			for (int i = tex.basemap; i <= tex.maxmap; i++) {
				GL11.glTexImage1D(tex.target.value, i, texformat.value, w, 0, texformat.base, ImageDataType.UBYTE.value, (ByteBuffer) null);
				w = Math.max(1, w / 2);
			}
		}
		tex.undobind();
		return tex;
	}
	
	
	/**************************************************/
	/********************** Bind **********************/
	/**************************************************/
	
	private static final BindTracker	bindTracker	= new BindTracker();
	
	
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
	public void setData(int x, int w, int map, ImageFormat format, ImageDataType type, ByteBuffer data) {
		bind();
		GL11.glTexSubImage1D(target.value, map, x, w, format.value, type.value, data);
		undobind();
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
		GL30.glFramebufferTexture1D(GL30.GL_DRAW_FRAMEBUFFER, attachment.value, target.value, id, level);
	}
	
	/**************************************************/
	
	@Override
	public void debugQuery() {
		GLDebug.flushErrors();
		
		GLDebug.writef(GLDebug.ATTRIB_STRING + "\t(%d)", target, name, w);
		GLDebug.indent();
		
		GLDebug.writef(GLDebug.ATTRIB_STRING, "Texture Format", texformat);
		
		GLDebug.writef(GLDebug.ATTRIB_STRING, "Wrapping Mode", sWrap);
		
		if (minFilter.mipmaps && maxmap > 0)
			GLDebug.writef(GLDebug.ATTRIB + "[%d, %d]", "Mipmap Range", basemap, maxmap);
		
		super.debugQuery();
		
		GLDebug.unindent();
		
		GLDebug.flushErrors();
	}
}
