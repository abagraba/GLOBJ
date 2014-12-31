package lwjgl.core.texture;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import lwjgl.core.Context;
import lwjgl.core.GL;
import lwjgl.core.GLObject;
import lwjgl.core.texture.values.MagnifyFilter;
import lwjgl.core.texture.values.MinifyFilter;
import lwjgl.core.texture.values.Swizzle;
import lwjgl.debug.Logging;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL40;

public abstract class Texture extends GLObject{
	
	/*
	 * TODO : Textures
	 * 
	 * Texture Buffers
	 * 
	 * 2D Multisample & Arrays
	 * https://www.opengl.org/registry/specs/ARB/texture_multisample.txt
	 * 
	 * Cubemaps
	 * 
	 * Gen mipmaps
	 * 
	 * Texture views
	 * 
	 * Initialize texture with gltexstorage fallback to glteximage
	 */
	
	protected boolean init = false;

	protected Texture(String name, int id){
		super(name, id);
	}
	
	public abstract void bind();
	protected abstract void unbind();
	
	protected abstract int target();
	
	public void genMipmaps(){
		bind();
		GL30.glGenerateMipmap(target());
		unbind();
	}
	
	public void setLOD(float min, float max, float bias) {
		bind();
		GL11.glTexParameterf(target(), GL12.GL_TEXTURE_MIN_LOD, min);
		GL11.glTexParameterf(target(), GL12.GL_TEXTURE_MAX_LOD, max);
		GL11.glTexParameterf(target(), GL14.GL_TEXTURE_LOD_BIAS, bias);
		unbind();
	}
	
	public void setFilter(MinifyFilter min, MagnifyFilter mag) {
		bind();
		GL11.glTexParameteri(target(), GL11.GL_TEXTURE_MIN_FILTER, min.value);
		GL11.glTexParameteri(target(), GL11.GL_TEXTURE_MAG_FILTER, mag.value);
		unbind();
	}
	
	public void setMipMapRange(int base, int max) {
		bind();
		GL11.glTexParameteri(target(), GL12.GL_TEXTURE_BASE_LEVEL, base);
		GL11.glTexParameteri(target(), GL12.GL_TEXTURE_MAX_LEVEL, max);
		unbind();
	}
	
	public void setSwizzle(Swizzle r, Swizzle g, Swizzle b, Swizzle a) {
		bind();
		IntBuffer swizzle = BufferUtils.createIntBuffer(4);
		swizzle.put(new int[] { r.value, g.value, b.value, a.value }).flip();
		GL11.glTexParameter(target(), GL33.GL_TEXTURE_SWIZZLE_RGBA, swizzle);
		unbind();
	}

	public void setBorderColor(float r, float g, float b, float a) {
		bind();
		FloatBuffer color = BufferUtils.createFloatBuffer(4);
		color.put(new float[] { r, g, b, a }).flip();
		GL11.glTexParameter(target(), GL11.GL_TEXTURE_BORDER_COLOR, color);
		unbind();
	}
	
	public void setDepthComparisonMode(TextureComparison mode) {
		bind();
		GL11.glTexParameteri(target(), GL14.GL_TEXTURE_COMPARE_MODE, mode.mode);
		GL11.glTexParameteri(target(), GL14.GL_TEXTURE_COMPARE_FUNC, mode.func);
		unbind();
	}
	
	public void setWrap(TextureWrap s, TextureWrap t, TextureWrap r) {
		bind();
		wrap(s, t, r);
		unbind();
	}
	
	protected abstract void wrap(TextureWrap s, TextureWrap t, TextureWrap r);

	
	public static String[] constants() {
		GL.flushErrors();
		int maxSize = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		int maxLayers = Context.intConst(GL30.GL_MAX_ARRAY_TEXTURE_LAYERS);
		int maxCube = Context.intConst(GL13.GL_MAX_CUBE_MAP_TEXTURE_SIZE);
		int maxRect = Context.intConst(GL31.GL_MAX_RECTANGLE_TEXTURE_SIZE);
		int maxBuffer = Context.intConst(GL31.GL_MAX_TEXTURE_BUFFER_SIZE);
		float lodbias = Context.floatConst(GL14.GL_MAX_TEXTURE_LOD_BIAS);
		int maxTexture = Context.intConst(GL20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS);
		
		List<String> status = new ArrayList<String>();
		List<String> errors = GL.readErrorsToList();
		for (String error : errors)
			status.add(Logging.logText("ERROR:", error, 0));
		status.add(Logging.logText("Texture Constants:", "", 0));
		status.add(Logging.logText(String.format("%-32s:\t%d x %d texels", "Max Texture Size", maxSize, maxSize), 0));
		status.add(Logging.logText(String.format("%-32s:\t%d layers", "Max Texture Array Layers", maxLayers), 0));
		status.add(Logging.logText(
				String.format("%-32s:\t%d x %d texels per side", "Max Cube Map Size", maxCube, maxCube), 0));
		status.add(Logging.logText(String.format("%-32s:\t%d x %d texels", "Max Rectangle Size", maxRect, maxRect), 0));
		status.add(Logging.logText(String.format("%-32s:\t%d texels", "Max Texture Buffer Size", maxBuffer), 0));
		status.add(Logging.logText(String.format("%-32s:\t%.3f", "Max LOD Bias", lodbias), 0));
		status.add(Logging.logText(String.format("%-32s:\t%d units", "Max Texture Units", maxTexture), 0));
		return status.toArray(new String[status.size()]);
	}
	
	public static String[] states() {
		GL.flushErrors();
		Texture1D texture1d = Texture1D.get(Context.intValue(GL11.GL_TEXTURE_BINDING_1D));
		Texture2D texture1darr = Texture2D.get(Context.intValue(GL30.GL_TEXTURE_BINDING_1D_ARRAY));
		Texture2D texture2d = Texture2D.get(Context.intValue(GL11.GL_TEXTURE_BINDING_2D));
		Texture3D texture2darr = Texture3D.get(Context.intValue(GL30.GL_TEXTURE_BINDING_2D_ARRAY));
		Texture3D texture3d = Texture3D.get(Context.intValue(GL12.GL_TEXTURE_BINDING_3D));
		// Texture2D texturebuffer =
		// Texture2D.get(Context.intValue(GL31.GL_TEXTURE_BINDING_BUFFER));
		Texture2D texturerect = Texture2D.get(Context.intValue(GL31.GL_TEXTURE_BINDING_RECTANGLE));
		Texture2D texturecube = Texture2D.get(Context.intValue(GL13.GL_TEXTURE_BINDING_CUBE_MAP));
		Texture2D texturecubearr = Texture2D.get(Context.intValue(GL40.GL_TEXTURE_BINDING_CUBE_MAP_ARRAY));
		// Texture2D texture2dm =
		// Texture2D.get(Context.intValue(GL32.GL_TEXTURE_BINDING_2D_MULTISAMPLE));
		// Texture2D texture2dmarr =
		// Texture2D.get(Context.intValue(GL32.GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY));
		
		int activeTexture = Context.intValue(GL13.GL_ACTIVE_TEXTURE) - GL13.GL_TEXTURE0;
		int activeSampler = Context.intValue(GL33.GL_SAMPLER_BINDING);
		boolean seamlessCube = Context.boolValue(GL32.GL_TEXTURE_CUBE_MAP_SEAMLESS);
		
		List<String> status = new ArrayList<String>();
		List<String> errors = GL.readErrorsToList();
		for (String error : errors)
			status.add(Logging.logText("ERROR:", error, 0));
		status.add(Logging.logText("Texture States:", "", 0));
		
		status.add(Logging.logText("Texture Bindings:", 0));
		// 1D
		status.add(Logging.logText(
				String.format("%-40s:\t%s", "Texture 1D Binding", texture1d == null ? "None" : texture1d.name), 1));
		// 1D Array
		status.add(Logging.logText(String.format("%-40s:\t%s", "Texture 1D Array Binding",
				texture1darr == null ? "None" : texture1darr.name), 1));
		// 2D
		status.add(Logging.logText(
				String.format("%-40s:\t%s", "Texture 2D Binding", texture2d == null ? "None" : texture2d.name), 1));
		// 2D Array
		status.add(Logging.logText(String.format("%-40s:\t%s", "Texture 2D Array Binding",
				texture2darr == null ? "None" : texture2darr.name), 1));
		// 3D
		status.add(Logging.logText(
				String.format("%-40s:\t%s", "Texture 3D Binding", texture3d == null ? "None" : texture3d.name), 1));
		// Buffer
		// status.add(Logging.logText(String.format("%-40s:\t%s",
		// "Texture Buffer Binding", texturebuffer == null ? "None"
		// : texturebuffer.name), 1));
		// Rectangle
		status.add(Logging.logText(String.format("%-40s:\t%s", "Texture Rectangle Binding",
				texturerect == null ? "None" : texturerect.name), 1));
		// Cubemap
		status.add(Logging.logText(
				String.format("%-40s:\t%s", "Texture Cubemap Binding", texturecube == null ? "None" : texturecube.name),
				1));
		// Cubemap Array
		status.add(Logging.logText(String.format("%-40s:\t%s", "Texture Cubemap Array Binding",
				texturecubearr == null ? "None" : texturecubearr.name), 1));
		// 2D Multisample
		// status.add(Logging.logText(String.format("%-40s:\t%s",
		// "Texture 2D Multisample Binding",
		// texture2dm == null ? "None" : texture2dm.name), 1));
		// 2D Multisample Array
		// status.add(Logging.logText(String.format("%-40s:\t%s",
		// "Texture 2D Multisample Array Binding",
		// texture2dmarr == null ? "None" : texture2dmarr.name), 1));
		
		status.add(Logging.logText(String.format("%-32s:\tUnit #%d", "Active Texture Unit", activeTexture), 0));
		status.add(Logging.logText(String.format("%-32s:\tUnit #%d", "Active Sampler Unit", activeSampler), 0));
		status.add(Logging.logText(String.format("%-32s:\t%s", "Seamless Cubemaps", seamlessCube ? "True" : "False"), 0));
		return status.toArray(new String[status.size()]);
	}
	
}
