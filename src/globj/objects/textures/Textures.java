package globj.objects.textures;


import static lwjgl.debug.GLDebug.ATTRIB;
import static lwjgl.debug.GLDebug.ATTRIB_INT;
import static lwjgl.debug.GLDebug.ATTRIB_STRING;
import static lwjgl.debug.GLDebug.flushErrors;
import static lwjgl.debug.GLDebug.glObjError;
import static lwjgl.debug.GLDebug.indent;
import static lwjgl.debug.GLDebug.unindent;
import static lwjgl.debug.GLDebug.write;
import static lwjgl.debug.GLDebug.writef;

import java.awt.image.BufferedImage;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL40;

import globj.core.Context;
import globj.objects.GLObjectTracker;
import globj.objects.textures.values.TextureFormat;



public class Textures {
	
	protected static final GLObjectTracker<GLTexture> tracker = new GLObjectTracker<GLTexture>();
	
	private static final String	T_1D	= "Texture_1D:";
	private static final String	T_2D	= "Texture_2D:";
	private static final String	T_3D	= "Texture_3D:";
	private static final String	T_1D_A	= "Texture_1DArray:";
	private static final String	T_2D_A	= "Texture_2DArray:";
	private static final String	T_C		= "Texture_Cubemap:";
	private static final String	T_C_A	= "Texture_CubemapArray:";
	private static final String	T_R		= "Texture_Rectangle:";
	
	/**************************************************/
	
	private Textures() {
	}
	
	public static Texture1D createTexture1D(String name, TextureFormat format, int w, int mipmaps) {
		return createTexture1D(name, format, w, 0, mipmaps - 1);
	}
	
	public static Texture1D createTexture1D(String name, TextureFormat format, int w, int basemap, int maxmap) {
		if (tracker.contains(T_1D + name)) {
			glObjError(Texture1D.class, name, "Cannot create", "Already exists");
			return null;
		}
		Texture1D tex = Texture1D.create(T_1D + name, format, w, basemap, maxmap);
		tracker.add(tex);
		return tex;
	}
	
	public static Texture2D createTexture2D(String name, TextureFormat format, int w, int h, int mipmaps) {
		return createTexture2D(name, format, w, h, 0, mipmaps - 1);
	}
	
	public static Texture2D createTexture2D(String name, TextureFormat format, int w, int h, int basemap, int maxmap) {
		if (tracker.contains(T_2D + name)) {
			glObjError(Texture2D.class, name, "Cannot create", "Already exists");
			return null;
		}
		Texture2D tex = Texture2D.create(T_2D + name, format, w, h, basemap, maxmap);
		tracker.add(tex);
		return tex;
	}
	
	public static Texture3D createTexture3D(String name, TextureFormat format, int w, int h, int d, int mipmaps) {
		return createTexture3D(name, format, w, h, d, 0, mipmaps - 1);
	}
	
	public static Texture3D createTexture3D(String name, TextureFormat format, int w, int h, int d, int basemap, int maxmap) {
		if (tracker.contains(T_3D + name)) {
			glObjError(Texture3D.class, name, "Cannot create", "Already exists");
			return null;
		}
		Texture3D tex = Texture3D.create(T_3D + name, format, w, h, d, basemap, maxmap);
		tracker.add(tex);
		return tex;
	}
	
	public static Texture1DArray createTexture1DArray(String name, TextureFormat format, int w, int layers, int mipmaps) {
		return createTexture1DArray(name, format, w, layers, 0, mipmaps - 1);
	}
	
	public static Texture1DArray createTexture1DArray(String name, TextureFormat format, int w, int layers, int basemap, int maxmap) {
		if (tracker.contains(T_1D_A + name)) {
			glObjError(Texture1DArray.class, name, "Cannot create", "Already exists");
			return null;
		}
		Texture1DArray tex = Texture1DArray.create(T_1D_A + name, format, w, layers, basemap, maxmap);
		tracker.add(tex);
		return tex;
	}
	
	public static Texture2DArray createTexture2DArray(String name, TextureFormat format, int w, int h, int layers, int mipmaps) {
		return createTexture2DArray(name, format, w, h, layers, 0, mipmaps - 1);
	}
	
	public static Texture2DArray createTexture2DArray(String name, TextureFormat format, int w, int h, int layers, int basemap, int maxmap) {
		if (tracker.contains(T_2D_A + name)) {
			glObjError(Texture2DArray.class, name, "Cannot create", "Already exists");
			return null;
		}
		Texture2DArray tex = Texture2DArray.create(T_2D_A + name, format, w, h, layers, basemap, maxmap);
		tracker.add(tex);
		return tex;
	}
	
	public static TextureCubemap createTextureCubemap(String name, TextureFormat format, int s, int mipmaps) {
		return createTextureCubemap(name, format, s, 0, mipmaps - 1);
	}
	
	public static TextureCubemap createTextureCubemap(String name, TextureFormat format, int s, int basemap, int maxmap) {
		if (tracker.contains(T_C + name)) {
			glObjError(TextureCubemap.class, name, "Cannot create", "Already exists");
			return null;
		}
		TextureCubemap tex = TextureCubemap.create(T_C + name, format, s, basemap, maxmap);
		tracker.add(tex);
		return tex;
	}
	
	public static TextureCubemapArray createTextureCubemapArray(String name, TextureFormat format, int s, int layers, int mipmaps) {
		return createTextureCubemapArray(name, format, s, layers, 0, mipmaps - 1);
	}
	
	public static TextureCubemapArray createTextureCubemapArray(String name, TextureFormat format, int s, int layers, int basemap, int maxmap) {
		if (tracker.contains(T_C_A + name)) {
			glObjError(TextureCubemapArray.class, name, "Cannot create", "Already exists");
			return null;
		}
		TextureCubemapArray tex = TextureCubemapArray.create(T_C_A + name, format, s, layers, basemap, maxmap);
		tracker.add(tex);
		return tex;
	}
	
	public static TextureRectangle createTextureRectangle(String name, TextureFormat format, int w, int h) {
		if (tracker.contains(T_R + name)) {
			glObjError(TextureRectangle.class, name, "Cannot create", "Already exists");
			return null;
		}
		TextureRectangle tex = TextureRectangle.create(T_R + name, format, w, h);
		tracker.add(tex);
		return tex;
	}
	
	/**************************************************/
	
	public static Texture2D createTexture2D(String name, BufferedImage image, int mipmaps) {
		if (tracker.contains(T_R + name)) {
			glObjError(Texture2D.class, name, "Cannot create", "Already exists");
			return null;
		}
		Texture2D tex = Texture2D.create(T_2D + name, image, mipmaps);
		tracker.add(tex);
		return tex;
	}
	
	public static Texture1DArray createTexture1DArray(String name, BufferedImage image, int mipmaps) {
		if (tracker.contains(T_R + name)) {
			glObjError(Texture1DArray.class, name, "Cannot create", "Already exists");
			return null;
		}
		Texture1DArray tex = Texture1DArray.create(T_1D_A + name, image, mipmaps);
		tracker.add(tex);
		return tex;
	}
	
	public static TextureRectangle createTextureRectangle(String name, BufferedImage image, int mipmaps) {
		TextureRectangle tex = TextureRectangle.create(T_R + name, image);
		if (tracker.contains(T_R + name)) {
			glObjError(TextureRectangle.class, name, "Cannot create", "Already exists");
			return null;
		}
		tracker.add(tex);
		return tex;
	}
	
	/**************************************************/
	
	public static Texture1D getTexture1D(String name) {
		return (Texture1D) tracker.get(T_1D + name);
	}
	
	public static Texture2D getTexture2D(String name) {
		return (Texture2D) tracker.get(T_2D + name);
	}
	
	public static Texture3D getTexture3D(String name) {
		return (Texture3D) tracker.get(T_3D + name);
	}
	
	public static Texture1DArray getTexture1DArray(String name) {
		return (Texture1DArray) tracker.get(T_1D_A + name);
	}
	
	public static Texture2DArray getTexture2DArray(String name) {
		return (Texture2DArray) tracker.get(T_2D_A + name);
	}
	
	public static TextureCubemap getTextureCubemap(String name) {
		return (TextureCubemap) tracker.get(T_C + name);
	}
	
	public static TextureCubemapArray getTextureCubemapArray(String name) {
		return (TextureCubemapArray) tracker.get(T_C_A + name);
	}
	
	public static TextureRectangle getTextureRectangle(String name) {
		return (TextureRectangle) tracker.get(T_R + name);
	}
	
	/**************************************************/
	
	public static GLTexture getTexture(int id) {
		return tracker.get(id);
	}
	
	public static Texture1D getTexture1D(int id) {
		return (Texture1D) tracker.get(id);
	}
	
	public static Texture2D getTexture2D(int id) {
		return (Texture2D) tracker.get(id);
	}
	
	public static Texture3D getTexture3D(int id) {
		return (Texture3D) tracker.get(id);
	}
	
	public static Texture1DArray getTexture1DArray(int id) {
		return (Texture1DArray) tracker.get(id);
	}
	
	public static Texture2DArray getTexture2DArray(int id) {
		return (Texture2DArray) tracker.get(id);
	}
	
	public static TextureCubemap getTextureCubemap(int id) {
		return (TextureCubemap) tracker.get(id);
	}
	
	public static TextureCubemapArray getTextureCubemapArray(int id) {
		return (TextureCubemapArray) tracker.get(id);
	}
	
	public static TextureRectangle getTextureRectangle(int id) {
		return (TextureRectangle) tracker.get(id);
	}
	
	/**************************************************/
	
	private static void destroy(String name) {
		GLTexture tex = tracker.get(name);
		if (tex != null) {
			tex.destroy();
			tracker.remove(tex);
		}
	}
	
	public static void destroyTexture1D(String name) {
		destroy(T_1D + name);
	}
	
	public static void destroyTexture2D(String name) {
		destroy(T_2D + name);
	}
	
	public static void destroyTexture3D(String name) {
		destroy(T_3D + name);
	}
	
	public static void destroyTexture1DArray(String name) {
		destroy(T_1D_A + name);
	}
	
	public static void destroyTexture2DArray(String name) {
		destroy(T_2D_A + name);
	}
	
	public static void destroyTextureCubemap(String name) {
		destroy(T_C + name);
	}
	
	public static void destroyTextureCubemapArray(String name) {
		destroy(T_C_A + name);
	}
	
	public static void destroyTextureRectangle(String name) {
		destroy(T_R + name);
	}
	
	/**************************************************/
	
	public static void constants() {
		String texels = "%d x %d texels";
		flushErrors();
//		#formatter:off		
		write("Texture Constants:");
		indent();
			int maxSize = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
			writef(ATTRIB + texels, "Max Texture Size:", maxSize, maxSize);
			writef(ATTRIB + "%d layers", "Max Texture Array Layers:", Context.intConst(GL30.GL_MAX_ARRAY_TEXTURE_LAYERS));
			int maxCube = Context.intConst(GL13.GL_MAX_CUBE_MAP_TEXTURE_SIZE);
			writef(ATTRIB + texels, "Max Cubemap Size:", maxCube, maxCube);
			int maxRect = Context.intConst(GL31.GL_MAX_RECTANGLE_TEXTURE_SIZE);
			writef(ATTRIB + texels, "Max Rectangle Size:", maxRect, maxRect);
			writef(ATTRIB_INT, "Max Buffer Size:", Context.intConst(GL31.GL_MAX_TEXTURE_BUFFER_SIZE));
			writef(ATTRIB_INT, "Max LOD Bias:", Context.floatConst(GL14.GL_MAX_TEXTURE_LOD_BIAS));
			writef(ATTRIB_INT, "Max Texture Units:", Context.intConst(GL20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS));
		unindent();
//		#formatter:on		
		flushErrors();
	}
	
	public static void states() {
		flushErrors();
		//	#formatter:off		
		// Texture2D texture2dm =
		// Texture2D.get(Context.intValue(GL32.GL_TEXTURE_BINDING_2D_MULTISAMPLE))
		// Texture2D texture2dmarr =
		// Texture2D.get(Context.intValue(GL32.GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY))
		// Texture2D texturebuffer =
		// Texture2D.get(Context.intValue(GL31.GL_TEXTURE_BINDING_BUFFER))
		
		write("Texture States:");
		indent();
			write("Texture Bindings:");
			indent();
				Texture1D texture1d = Textures.getTexture1D(Context.intValue(GL11.GL_TEXTURE_BINDING_1D));
				writef(ATTRIB_STRING, "Texture 1D Binding:", texture1d == null ? "None" : texture1d);

				Texture2D texture2d = Textures.getTexture2D(Context.intValue(GL11.GL_TEXTURE_BINDING_2D));
				writef(ATTRIB_STRING, "Texture 2D Binding:", texture2d == null ? "None" : texture2d);

				Texture3D texture3d = Textures.getTexture3D(Context.intValue(GL12.GL_TEXTURE_BINDING_3D));
				writef(ATTRIB_STRING, "Texture 3D Binding:", texture3d == null ? "None" : texture3d);

				Texture1DArray texture1darr = Textures.getTexture1DArray(Context.intValue(GL30.GL_TEXTURE_BINDING_1D_ARRAY));
				writef(ATTRIB_STRING, "Texture 1D Array Binding:", texture1darr == null ? "None" : texture1darr);
				
				Texture2DArray texture2darr = Textures.getTexture2DArray(Context.intValue(GL30.GL_TEXTURE_BINDING_2D_ARRAY));
				writef(ATTRIB_STRING, "Texture 2D Array Binding:", texture2darr == null ? "None" : texture2darr);
		
				TextureRectangle texturerect = Textures.getTextureRectangle(Context.intValue(GL31.GL_TEXTURE_BINDING_RECTANGLE));
				writef(ATTRIB_STRING, "Texture Rectangle Binding:", texturerect == null ? "None" : texturerect);
		
				TextureCubemap texturecube = Textures.getTextureCubemap(Context.intValue(GL13.GL_TEXTURE_BINDING_CUBE_MAP));
				writef(ATTRIB_STRING, "Texture Cubemap Binding:", texturecube == null ? "None" : texturecube);
		
				TextureCubemapArray texturecubearr = Textures.getTextureCubemapArray(Context.intValue(GL40.GL_TEXTURE_BINDING_CUBE_MAP_ARRAY));
				writef(ATTRIB_STRING, "Texture Cubemap Array Binding:", texturecubearr == null ? "None" : texturecubearr);
			unindent();
			writef(ATTRIB_INT, "Active Texture Unit:", (Context.intValue(GL13.GL_ACTIVE_TEXTURE) - GL13.GL_TEXTURE0));
			writef(ATTRIB_INT, "Active Sampler Unit:", Context.intValue(GL33.GL_SAMPLER_BINDING));
		unindent();
		//	#formatter:on		
		flushErrors();
	}
	
}
