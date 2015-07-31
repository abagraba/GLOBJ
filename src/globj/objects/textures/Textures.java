package globj.objects.textures;


import globj.core.Context;
import globj.objects.GLObjectTracker;
import globj.objects.textures.values.TextureFormat;

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

import static lwjgl.debug.GLDebug.*;



public class Textures {
	
	protected static final GLObjectTracker<GLTexture> tracker = new GLObjectTracker<GLTexture>();
	
	private static final String	t1d		= "Texture_1D:";
	private static final String	t2d		= "Texture_2D:";
	private static final String	t3d		= "Texture_3D:";
	private static final String	t1da	= "Texture_1DArray:";
	private static final String	t2da	= "Texture_2DArray:";
	private static final String	tc		= "Texture_Cubemap:";
	private static final String	tca		= "Texture_CubemapArray:";
	private static final String	tr		= "Texture_Rectangle:";
	
	/**************************************************/
	
	private Textures() {
	}
	
	public static Texture1D createTexture1D(String name, TextureFormat format, int w, int mipmaps) {
		return createTexture1D(name, format, w, 0, mipmaps - 1);
	}
	
	public static Texture1D createTexture1D(String name, TextureFormat format, int w, int basemap, int maxmap) {
		if (tracker.contains(t1d + name)) {
			glObjError(Texture1D.class, name, "Cannot create", "Already exists");
			return null;
		}
		Texture1D tex = Texture1D.create(t1d + name, format, w, basemap, maxmap);
		tracker.add(tex);
		return tex;
	}
	
	public static Texture2D createTexture2D(String name, TextureFormat format, int w, int h, int mipmaps) {
		return createTexture2D(name, format, w, h, 0, mipmaps - 1);
	}
	
	public static Texture2D createTexture2D(String name, TextureFormat format, int w, int h, int basemap, int maxmap) {
		if (tracker.contains(t2d + name)) {
			glObjError(Texture2D.class, name, "Cannot create", "Already exists");
			return null;
		}
		Texture2D tex = Texture2D.create(t2d + name, format, w, h, basemap, maxmap);
		tracker.add(tex);
		return tex;
	}
	
	public static Texture3D createTexture3D(String name, TextureFormat format, int w, int h, int d, int mipmaps) {
		return createTexture3D(name, format, w, h, d, 0, mipmaps - 1);
	}
	
	public static Texture3D createTexture3D(String name, TextureFormat format, int w, int h, int d, int basemap, int maxmap) {
		if (tracker.contains(t3d + name)) {
			glObjError(Texture3D.class, name, "Cannot create", "Already exists");
			return null;
		}
		Texture3D tex = Texture3D.create(t3d + name, format, w, h, d, basemap, maxmap);
		tracker.add(tex);
		return tex;
	}
	
	public static Texture1DArray createTexture1DArray(String name, TextureFormat format, int w, int layers, int mipmaps) {
		return createTexture1DArray(name, format, w, layers, 0, mipmaps - 1);
	}
	
	public static Texture1DArray createTexture1DArray(String name, TextureFormat format, int w, int layers, int basemap, int maxmap) {
		if (tracker.contains(t1da + name)) {
			glObjError(Texture1DArray.class, name, "Cannot create", "Already exists");
			return null;
		}
		Texture1DArray tex = Texture1DArray.create(t1da + name, format, w, layers, basemap, maxmap);
		tracker.add(tex);
		return tex;
	}
	
	public static Texture2DArray createTexture2DArray(String name, TextureFormat format, int w, int h, int layers, int mipmaps) {
		return createTexture2DArray(name, format, w, h, layers, 0, mipmaps - 1);
	}
	
	public static Texture2DArray createTexture2DArray(String name, TextureFormat format, int w, int h, int layers, int basemap, int maxmap) {
		if (tracker.contains(t2da + name)) {
			glObjError(Texture2DArray.class, name, "Cannot create", "Already exists");
			return null;
		}
		Texture2DArray tex = Texture2DArray.create(t2da + name, format, w, h, layers, basemap, maxmap);
		tracker.add(tex);
		return tex;
	}
	
	public static TextureCubemap createTextureCubemap(String name, TextureFormat format, int s, int mipmaps) {
		return createTextureCubemap(name, format, s, 0, mipmaps - 1);
	}
	
	public static TextureCubemap createTextureCubemap(String name, TextureFormat format, int s, int basemap, int maxmap) {
		if (tracker.contains(tc + name)) {
			glObjError(TextureCubemap.class, name, "Cannot create", "Already exists");
			return null;
		}
		TextureCubemap tex = TextureCubemap.create(tc + name, format, s, basemap, maxmap);
		tracker.add(tex);
		return tex;
	}
	
	public static TextureCubemapArray createTextureCubemapArray(String name, TextureFormat format, int s, int layers, int mipmaps) {
		return createTextureCubemapArray(name, format, s, layers, 0, mipmaps - 1);
	}
	
	public static TextureCubemapArray createTextureCubemapArray(String name, TextureFormat format, int s, int layers, int basemap, int maxmap) {
		if (tracker.contains(tca + name)) {
			glObjError(TextureCubemapArray.class, name, "Cannot create", "Already exists");
			return null;
		}
		TextureCubemapArray tex = TextureCubemapArray.create(tca + name, format, s, layers, basemap, maxmap);
		tracker.add(tex);
		return tex;
	}
	
	public static TextureRectangle createTextureRectangle(String name, TextureFormat format, int w, int h) {
		if (tracker.contains(tr + name)) {
			glObjError(TextureRectangle.class, name, "Cannot create", "Already exists");
			return null;
		}
		TextureRectangle tex = TextureRectangle.create(tr + name, format, w, h);
		tracker.add(tex);
		return tex;
	}
	
	/**************************************************/
	
	public static Texture2D createTexture2D(String name, BufferedImage image, int mipmaps) {
		if (tracker.contains(tr + name)) {
			glObjError(Texture2D.class, name, "Cannot create", "Already exists");
			return null;
		}
		Texture2D tex = Texture2D.create(t2d + name, image, mipmaps);
		tracker.add(tex);
		return tex;
	}
	
	public static Texture1DArray createTexture1DArray(String name, BufferedImage image, int mipmaps) {
		if (tracker.contains(tr + name)) {
			glObjError(Texture1DArray.class, name, "Cannot create", "Already exists");
			return null;
		}
		Texture1DArray tex = Texture1DArray.create(t1da + name, image, mipmaps);
		tracker.add(tex);
		return tex;
	}
	
	public static TextureRectangle createTextureRectangle(String name, BufferedImage image, int mipmaps) {
		TextureRectangle tex = TextureRectangle.create(tr + name, image);
		if (tracker.contains(tr + name)) {
			glObjError(TextureRectangle.class, name, "Cannot create", "Already exists");
			return null;
		}
		tracker.add(tex);
		return tex;
	}
	
	/**************************************************/
	
	public static Texture1D getTexture1D(String name) {
		return (Texture1D) tracker.get(t1d + name);
	}
	
	public static Texture2D getTexture2D(String name) {
		return (Texture2D) tracker.get(t2d + name);
	}
	
	public static Texture3D getTexture3D(String name) {
		return (Texture3D) tracker.get(t3d + name);
	}
	
	public static Texture1DArray getTexture1DArray(String name) {
		return (Texture1DArray) tracker.get(t1da + name);
	}
	
	public static Texture2DArray getTexture2DArray(String name) {
		return (Texture2DArray) tracker.get(t2da + name);
	}
	
	public static TextureCubemap getTextureCubemap(String name) {
		return (TextureCubemap) tracker.get(tc + name);
	}
	
	public static TextureCubemapArray getTextureCubemapArray(String name) {
		return (TextureCubemapArray) tracker.get(tca + name);
	}
	
	public static TextureRectangle getTextureRectangle(String name) {
		return (TextureRectangle) tracker.get(tr + name);
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
		destroy(t1d + name);
	}
	
	public static void destroyTexture2D(String name) {
		destroy(t2d + name);
	}
	
	public static void destroyTexture3D(String name) {
		destroy(t3d + name);
	}
	
	public static void destroyTexture1DArray(String name) {
		destroy(t1da + name);
	}
	
	public static void destroyTexture2DArray(String name) {
		destroy(t2da + name);
	}
	
	public static void destroyTextureCubemap(String name) {
		destroy(tc + name);
	}
	
	public static void destroyTextureCubemapArray(String name) {
		destroy(tca + name);
	}
	
	public static void destroyTextureRectangle(String name) {
		destroy(tr + name);
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
