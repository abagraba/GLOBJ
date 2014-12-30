package lwjgl.core.texture;

import java.util.ArrayList;
import java.util.List;

import lwjgl.core.Context;
import lwjgl.core.GL;
import lwjgl.debug.Logging;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL40;

public class Texture {
	
	/* TODO : Textures
	 * 
	 * Texture Buffers
	 * 
	 * 2D Multisample & Arrays
	 * https://www.opengl.org/registry/specs/ARB/texture_multisample.txt
	 * 
	 * Gen mipmaps
	 * 
	 * Texture views
	 * 
	 * gltexstorage
	 * 
	 */
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
	
	public static String[] status() {
		GL.flushErrors();
		Texture1D texture1d = Texture1D.get(Context.intValue(GL11.GL_TEXTURE_BINDING_1D));
		Texture2D texture1darr = Texture2D.get(Context.intValue(GL30.GL_TEXTURE_BINDING_1D_ARRAY));
		Texture2D texture2d = Texture2D.get(Context.intValue(GL11.GL_TEXTURE_BINDING_2D));
		Texture3D texture2darr = Texture3D.get(Context.intValue(GL30.GL_TEXTURE_BINDING_2D_ARRAY));
		Texture3D texture3d = Texture3D.get(Context.intValue(GL12.GL_TEXTURE_BINDING_3D));
		//Texture2D texturebuffer = Texture2D.get(Context.intValue(GL31.GL_TEXTURE_BINDING_BUFFER));
		Texture2D texturerect = Texture2D.get(Context.intValue(GL31.GL_TEXTURE_BINDING_RECTANGLE));
		Texture2D texturecube = Texture2D.get(Context.intValue(GL13.GL_TEXTURE_BINDING_CUBE_MAP));
		Texture2D texturecubearr = Texture2D.get(Context.intValue(GL40.GL_TEXTURE_BINDING_CUBE_MAP_ARRAY));
		//Texture2D texture2dm = Texture2D.get(Context.intValue(GL32.GL_TEXTURE_BINDING_2D_MULTISAMPLE));
		//Texture2D texture2dmarr = Texture2D.get(Context.intValue(GL32.GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY));
		
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
//		status.add(Logging.logText(String.format("%-40s:\t%s", "Texture Buffer Binding", texturebuffer == null ? "None"
//				: texturebuffer.name), 1));
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
//		status.add(Logging.logText(String.format("%-40s:\t%s", "Texture 2D Multisample Binding",
//				texture2dm == null ? "None" : texture2dm.name), 1));
		// 2D Multisample Array
//		status.add(Logging.logText(String.format("%-40s:\t%s", "Texture 2D Multisample Array Binding",
//				texture2dmarr == null ? "None" : texture2dmarr.name), 1));
		
		status.add(Logging.logText(String.format("%-32s:\tUnit #%d", "Active Texture Unit", activeTexture), 0));
		status.add(Logging.logText(String.format("%-32s:\tUnit #%d", "Active Sampler Unit", activeSampler), 0));
		status.add(Logging.logText(String.format("%-32s:\t%s", "Seamless Cubemaps", seamlessCube ? "True" : "False"), 0));
		return status.toArray(new String[status.size()]);
	}
	
}
