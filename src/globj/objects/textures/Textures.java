package globj.objects.textures;

import globj.core.Context;
import globj.core.GL;
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

import lwjgl.debug.Logging;

public class Textures {
	
	private static final GLObjectTracker<GLTexture> tracker = new GLObjectTracker<GLTexture>();
	
	private static final String t1d = "Texture1D_";
	private static final String t2d = "Texture2D_";
	private static final String t3d = "Texture3D_";
	private static final String t1da = "Texture1DArray_";
	private static final String t2da = "Texture2DArray_";
	private static final String tc = "TextureCubemap_";
	private static final String tca = "TextureCubemapArray_";
	private static final String tr = "TextureRectangle_";
	
	/**************************************************/
	
	public static Texture1D createTexture1D(String name, TextureFormat format, int w, int mipmaps) {
		return createTexture1D(name, format, w, 0, mipmaps - 1);
	}
	
	public static Texture1D createTexture1D(String name, TextureFormat format, int w, int basemap, int maxmap) {
		Texture1D tex = Texture1D.create(t1d + name, format, w, basemap, maxmap);
		if (tracker.contains(t1d + name)) {
			Logging.globjError(Texture1D.class, name, "Cannot create", "Already exists");
			return null;
		}
		tracker.add(tex);
		return tex;
	}
	
	public static Texture2D createTexture2D(String name, TextureFormat format, int w, int h, int mipmaps) {
		return createTexture2D(name, format, w, h, 0, mipmaps - 1);
	}
	
	public static Texture2D createTexture2D(String name, TextureFormat format, int w, int h, int basemap, int maxmap) {
		Texture2D tex = Texture2D.create(t2d + name, format, w, h, basemap, maxmap);
		if (tracker.contains(t2d + name)) {
			Logging.globjError(Texture2D.class, name, "Cannot create", "Already exists");
			return null;
		}
		tracker.add(tex);
		return tex;
	}
	
	public static Texture3D createTexture3D(String name, TextureFormat format, int w, int h, int d, int mipmaps) {
		return createTexture3D(name, format, w, h, d, 0, mipmaps - 1);
	}
	
	public static Texture3D createTexture3D(String name, TextureFormat format, int w, int h, int d, int basemap, int maxmap) {
		Texture3D tex = Texture3D.create(t3d + name, format, w, h, d, basemap, maxmap);
		if (tracker.contains(t3d + name)) {
			Logging.globjError(Texture3D.class, name, "Cannot create", "Already exists");
			return null;
		}
		tracker.add(tex);
		return tex;
	}
	
	public static Texture1DArray createTexture1DArray(String name, TextureFormat format, int w, int layers, int mipmaps) {
		return createTexture1DArray(name, format, w, layers, 0, mipmaps - 1);
	}
	
	public static Texture1DArray createTexture1DArray(String name, TextureFormat format, int w, int layers, int basemap, int maxmap) {
		Texture1DArray tex = Texture1DArray.create(t1da + name, format, w, layers, basemap, maxmap);
		if (tracker.contains(t1da + name)) {
			Logging.globjError(Texture1DArray.class, name, "Cannot create", "Already exists");
			return null;
		}
		tracker.add(tex);
		return tex;
	}
	
	public static Texture2DArray createTexture2DArray(String name, TextureFormat format, int w, int h, int layers, int mipmaps) {
		return createTexture2DArray(name, format, w, h, layers, 0, mipmaps - 1);
	}
	
	public static Texture2DArray createTexture2DArray(String name, TextureFormat format, int w, int h, int layers, int basemap, int maxmap) {
		Texture2DArray tex = Texture2DArray.create(t2da + name, format, w, h, layers, basemap, maxmap);
		if (tracker.contains(t2da + name)) {
			Logging.globjError(Texture2DArray.class, name, "Cannot create", "Already exists");
			return null;
		}
		tracker.add(tex);
		return tex;
	}
	
	public static TextureCubemap createTextureCubemap(String name, TextureFormat format, int s, int mipmaps) {
		return createTextureCubemap(name, format, s, 0, mipmaps - 1);
	}
	
	public static TextureCubemap createTextureCubemap(String name, TextureFormat format, int s, int basemap, int maxmap) {
		TextureCubemap tex = TextureCubemap.create(tc + name, format, s, basemap, maxmap);
		if (tracker.contains(tc + name)) {
			Logging.globjError(TextureCubemap.class, name, "Cannot create", "Already exists");
			return null;
		}
		tracker.add(tex);
		return tex;
	}
	
	public static TextureCubemapArray createTextureCubemapArray(String name, TextureFormat format, int s, int layers, int mipmaps) {
		return createTextureCubemapArray(name, format, s, layers, 0, mipmaps - 1);
	}
	
	public static TextureCubemapArray createTextureCubemapArray(String name, TextureFormat format, int s, int layers, int basemap, int maxmap) {
		TextureCubemapArray tex = TextureCubemapArray.create(tca + name, format, s, layers, basemap, maxmap);
		if (tracker.contains(tca + name)) {
			Logging.globjError(TextureCubemapArray.class, name, "Cannot create", "Already exists");
			return null;
		}
		tracker.add(tex);
		return tex;
	}
	
	public static TextureRectangle createTextureRectangle(String name, TextureFormat format, int w, int h) {
		TextureRectangle tex = TextureRectangle.create(tr + name, format, w, h);
		if (tracker.contains(tr + name)) {
			Logging.globjError(TextureRectangle.class, name, "Cannot create", "Already exists");
			return null;
		}
		tracker.add(tex);
		return tex;
	}
	
	/**************************************************/
	
	public static Texture2D createTexture2D(String name, BufferedImage image, int mipmaps) {
		Texture2D tex = Texture2D.create(t2d + name, image, mipmaps);
		if (tracker.contains(tr + name)) {
			Logging.globjError(TextureRectangle.class, name, "Cannot create", "Already exists");
			return null;
		}
		tracker.add(tex);
		return tex;
	}
	
	public static Texture1DArray createTexture1DArray(String name, BufferedImage image, int mipmaps) {
		Texture1DArray tex = Texture1DArray.create(t1da + name, image, mipmaps);
		if (tracker.contains(tr + name)) {
			Logging.globjError(TextureRectangle.class, name, "Cannot create", "Already exists");
			return null;
		}
		tracker.add(tex);
		return tex;
	}
	
	public static TextureRectangle createTextureRectangle(String name, BufferedImage image, int mipmaps) {
		TextureRectangle tex = TextureRectangle.create(tr + name, image);
		if (tracker.contains(tr + name)) {
			Logging.globjError(TextureRectangle.class, name, "Cannot create", "Already exists");
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
	
	public static void destroyTexture1D(String name) {
		Texture1D tex = (Texture1D) tracker.get(t1d + name);
		if (tex != null)
			tex.destroy();
	}
	
	public static void destroyTexture2D(String name) {
		Texture2D tex = (Texture2D) tracker.get(t2d + name);
		if (tex != null)
			tex.destroy();
	}
	
	public static void destroyTexture3D(String name) {
		Texture3D tex = (Texture3D) tracker.get(t3d + name);
		if (tex != null)
			tex.destroy();
	}
	
	public static void destroyTexture1DArray(String name) {
		Texture1DArray tex = (Texture1DArray) tracker.get(t1da + name);
		if (tex != null)
			tex.destroy();
	}
	
	public static void destroyTexture2DArray(String name) {
		Texture2DArray tex = (Texture2DArray) tracker.get(t2da + name);
		if (tex != null)
			tex.destroy();
	}
	
	public static void destroyTextureCubemap(String name) {
		TextureCubemap tex = (TextureCubemap) tracker.get(tc + name);
		if (tex != null)
			tex.destroy();
	}
	
	public static void destroyTextureCubemapArray(String name) {
		TextureCubemapArray tex = (TextureCubemapArray) tracker.get(tca + name);
		if (tex != null)
			tex.destroy();
	}
	
	public static void destroyTextureRectangle(String name) {
		TextureRectangle tex = (TextureRectangle) tracker.get(tr + name);
		if (tex != null)
			tex.destroy();
	}
	
	/**************************************************/
	
	public static void constants() {
		GL.flushErrors();
		int maxSize = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		int maxLayers = Context.intConst(GL30.GL_MAX_ARRAY_TEXTURE_LAYERS);
		int maxCube = Context.intConst(GL13.GL_MAX_CUBE_MAP_TEXTURE_SIZE);
		int maxRect = Context.intConst(GL31.GL_MAX_RECTANGLE_TEXTURE_SIZE);
		int maxBuffer = Context.intConst(GL31.GL_MAX_TEXTURE_BUFFER_SIZE);
		float lodbias = Context.floatConst(GL14.GL_MAX_TEXTURE_LOD_BIAS);
		int maxTexture = Context.intConst(GL20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS);
		
		Logging.setPad(32);
		Logging.writeOut("Texture Constants:");
		Logging.indent();
		Logging.writeOut(Logging.fixedString("Max Texture Size:") + String.format("%d x %d texels", maxSize, maxSize));
		Logging.writeOut(Logging.fixedString("Max Texture Array Layers:") + String.format("%d layers", maxLayers));
		Logging.writeOut(Logging.fixedString("Max Cubemap Size:") + String.format("%d x %d texels", maxCube, maxCube));
		Logging.writeOut(Logging.fixedString("Max Rectangle Size:") + String.format("%d x %d texels", maxRect, maxRect));
		Logging.writeOut(Logging.fixedString("Max Buffer Size:") + String.format("%d texels", maxBuffer));
		Logging.writeOut(Logging.fixedString("Max LOD Bias:") + String.format("%d texels", lodbias));
		Logging.writeOut(Logging.fixedString("Max Texture Units:") + String.format("%d units", maxTexture));
		Logging.unindent();
		Logging.unsetPad();
		GL.flushErrors();
	}
	
	public static void states() {
		GL.flushErrors();
		Texture1D texture1d = Textures.getTexture1D(Context.intValue(GL11.GL_TEXTURE_BINDING_1D));
		Texture1DArray texture1darr = Textures.getTexture1DArray(Context.intValue(GL30.GL_TEXTURE_BINDING_1D_ARRAY));
		Texture2D texture2d = Textures.getTexture2D(Context.intValue(GL11.GL_TEXTURE_BINDING_2D));
		Texture2DArray texture2darr = Textures.getTexture2DArray(Context.intValue(GL30.GL_TEXTURE_BINDING_2D_ARRAY));
		Texture3D texture3d = Textures.getTexture3D(Context.intValue(GL12.GL_TEXTURE_BINDING_3D));
		TextureRectangle texturerect = Textures.getTextureRectangle(Context.intValue(GL31.GL_TEXTURE_BINDING_RECTANGLE));
		TextureCubemap texturecube = Textures.getTextureCubemap(Context.intValue(GL13.GL_TEXTURE_BINDING_CUBE_MAP));
		TextureCubemapArray texturecubearr = Textures.getTextureCubemapArray(Context.intValue(GL40.GL_TEXTURE_BINDING_CUBE_MAP_ARRAY));
		// Texture2D texture2dm =
		// Texture2D.get(Context.intValue(GL32.GL_TEXTURE_BINDING_2D_MULTISAMPLE));
		// Texture2D texture2dmarr =
		// Texture2D.get(Context.intValue(GL32.GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY));
		// Texture2D texturebuffer =
		// Texture2D.get(Context.intValue(GL31.GL_TEXTURE_BINDING_BUFFER));
		
		int activeTexture = Context.intValue(GL13.GL_ACTIVE_TEXTURE) - GL13.GL_TEXTURE0;
		int activeSampler = Context.intValue(GL33.GL_SAMPLER_BINDING);

		Logging.setPad(32);
		Logging.writeOut("Texture States:");
		Logging.indent();
		Logging.writeOut("Texture Bindings:");
		Logging.indent();
		Logging.writeOut(Logging.fixedString("Texture 1D Binding:") + texture1d == null ? "None" : texture1d.name);
		Logging.writeOut(Logging.fixedString("Texture 2D Binding:") + texture2d == null ? "None" : texture2d.name);
		Logging.writeOut(Logging.fixedString("Texture 3D Binding:") + texture3d == null ? "None" : texture3d.name);
		Logging.writeOut(Logging.fixedString("Texture 1D Array Binding:") + texture1darr == null ? "None" : texture1darr.name);
		Logging.writeOut(Logging.fixedString("Texture 2D Array Binding:") + texture2darr == null ? "None" : texture2darr.name);
		Logging.writeOut(Logging.fixedString("Texture Rectangle Binding:") + texturerect == null ? "None" : texturerect.name);
		Logging.writeOut(Logging.fixedString("Texture Cubemap Binding:") + texturecube== null ? "None" : texturecube.name);
		Logging.writeOut(Logging.fixedString("Texture Cubemap Array Binding:") + texturecubearr== null ? "None" : texturecubearr.name);
		Logging.unindent();
		Logging.writeOut(Logging.fixedString("Active Texture Unit:") + activeTexture);
		Logging.writeOut(Logging.fixedString("Active Sampler Unit:") + activeSampler);
		Logging.unindent();
		Logging.unsetPad();
		GL.flushErrors();
	}

	
}
