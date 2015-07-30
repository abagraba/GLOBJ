package globj.objects.textures;


import globj.core.GL;
import globj.core.V4f;
import globj.objects.BindableGLObject;
import globj.objects.textures.values.DepthStencilMode;
import globj.objects.textures.values.MagnifyFilter;
import globj.objects.textures.values.MinifyFilter;
import globj.objects.textures.values.Swizzle;
import globj.objects.textures.values.TextureComparison;
import globj.objects.textures.values.TextureFormat;
import globj.objects.textures.values.TextureTarget;

import java.nio.IntBuffer;

import lwjgl.debug.GLDebug;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;



@NonNullByDefault
public abstract class GLTexture extends BindableGLObject {
	
	protected MinifyFilter			minFilter	= MinifyFilter.NEAREST_MIPMAP_LINEAR;
	protected MagnifyFilter			magFilter	= MagnifyFilter.LINEAR;
	
	protected float					lodMin		= -1000f;
	protected float					lodMax		= 1000f;
	protected float					lodBias		= 0f;
	
	protected Swizzle				swizzleR	= Swizzle.R;
	protected Swizzle				swizzleG	= Swizzle.G;
	protected Swizzle				swizzleB	= Swizzle.B;
	protected Swizzle				swizzleA	= Swizzle.A;
	
	protected V4f					border		= new V4f(0, 0, 0, 0);
	
	protected DepthStencilMode		dsmode		= DepthStencilMode.DEPTH;
	
	protected TextureComparison		comparison	= TextureComparison.NONE;
	
	protected final TextureFormat	texformat;
	protected final TextureTarget	target;
	
	
	protected GLTexture(String name, TextureFormat texformat, TextureTarget target) {
		super(name, GL11.glGenTextures());
		this.texformat = texformat;
		this.target = target;
	}
	
	/*
	 * TODO : Textures Texture Buffers 2D Multisample & Arrays
	 * https://www.opengl.org/registry/specs/ARB/texture_multisample.txt Gen mipmaps Texture views Initialize texture
	 * with gltexstorage fallback to glteximage
	 */
	
	public void genMipmaps() {
		bind();
		GL30.glGenerateMipmap(target.value);
		undobind();
	}
	
	/**
	 * Sets the level of detail bounds for the texture.
	 * 
	 * @param min
	 *            minimum LOD.
	 * @param max
	 *            maximum LOD.
	 * @param bias
	 *            LOD bias.
	 */
	public void setLOD(float min, float max, float bias) {
		bind();
		GL11.glTexParameterf(target.value, GL12.GL_TEXTURE_MIN_LOD, lodMin = min);
		GL11.glTexParameterf(target.value, GL12.GL_TEXTURE_MAX_LOD, lodMax = max);
		GL11.glTexParameterf(target.value, GL14.GL_TEXTURE_LOD_BIAS, lodBias = bias);
		undobind();
	}
	
	/**
	 * Sets the minification and magnification filters for the texture.
	 * 
	 * @param min
	 *            minification filter.
	 * @param mag
	 *            magnification filter.
	 */
	public void setFilter(MinifyFilter min, MagnifyFilter mag) {
		bind();
		GL11.glTexParameteri(target.value, GL11.GL_TEXTURE_MIN_FILTER, (minFilter = min).value);
		GL11.glTexParameteri(target.value, GL11.GL_TEXTURE_MAG_FILTER, (magFilter = mag).value);
		undobind();
	}
	
	/**
	 * Sets the component swizzle for the texture.
	 * 
	 * @param r
	 *            the first component.
	 * @param g
	 *            the second component.
	 * @param b
	 *            the third component.
	 * @param a
	 *            the fourth component.
	 */
	public void setSwizzle(Swizzle r, Swizzle g, Swizzle b, Swizzle a) {
		bind();
		IntBuffer swizzle = BufferUtils.createIntBuffer(4);
		swizzle.put(new int[] { (swizzleR = r).value, (swizzleG = g).value, (swizzleB = b).value, (swizzleA = a).value }).flip();
		GL11.glTexParameter(target.value, GL11.GL_TEXTURE_BORDER_COLOR, swizzle);
		undobind();
	}
	
	/**
	 * Sets the color of the border.
	 * 
	 * @param r
	 *            red component.
	 * @param g
	 *            green component.
	 * @param b
	 *            blue component.
	 * @param a
	 *            alpha component.
	 */
	public void setBorderColor(float r, float g, float b, float a) {
		bind();
		GL11.glTexParameter(target.value, GL11.GL_TEXTURE_BORDER_COLOR, (border = new V4f(r, g, b, a)).asBuffer());
		undobind();
	}
	
	/**
	 * Sets the the depth/stencil mode.
	 * 
	 * @param func
	 *            depth/stencil mode.
	 */
	public void setDepthStencilMode(DepthStencilMode mode) {
		if (GL.versionCheck(4, 3)) {
			bind();
			GL11.glTexParameteri(target.value, GL43.GL_DEPTH_STENCIL_TEXTURE_MODE, (dsmode = mode).value);
			undobind();
		}
	}
	
	/**
	 * Sets the comparison mode for the texture. Only useful if the texture has a depth component.
	 * 
	 * @param func
	 *            depth comparison function.
	 */
	public void setDepthComparisonMode(TextureComparison func) {
		bind();
		GL11.glTexParameteri(target.value, GL14.GL_TEXTURE_COMPARE_MODE, (comparison = func).mode);
		GL11.glTexParameteri(target.value, GL14.GL_TEXTURE_COMPARE_FUNC, comparison.func);
		undobind();
	}
	
	/**************************************************/
	/********************** Bind **********************/
	/**************************************************/
	
	@Override
	protected void bindOP(int id) {
		bind();
		GL11.glBindTexture(target.value, id);
		undobind();
	}
	
	@Override
	protected void destroyOP() {
		bind();
		GL11.glDeleteTextures(id);
		undobind();
	}
	
	/**************************************************/
	/***************** Helper Methods *****************/
	/**************************************************/
	
	protected static int levels(int level) {
		int x = level;
		if (x <= 0)
			return 0;
		int log = 0;
		if ((x & 0xffff0000) != 0) {
			x >>>= 16;
			log = 16;
		}
		if ((x & 0xff00) != 0) {
			x >>>= 8;
			log += 8;
		}
		if ((x & 0xf0) != 0) {
			x >>>= 4;
			log += 4;
		}
		if ((x & 0xc) != 0) {
			x >>>= 2;
			log += 2;
		}
		return log + (x >>> 1);
	}
	
	protected static boolean checkBounds(int[] args, int[] limits, GLTexture tex) {
		for (int i = 0; i < args.length; i++)
			if (args[i] < 0) {
				GLDebug.glObjError(tex, "Cannot initialize", "Dimensions " + dimensions(args) + " must be non-negative.");
				return false;
			}
		for (int i = 0; i < args.length; i++)
			if (args[i] > limits[i]) {
				GLDebug.glObjError(tex, "Cannot initialize", "Dimensions " + dimensions(args) + " too large. Device only supports textures up to "
																+ dimensions(limits) + ".");
				return false;
			}
		return true;
	}
	
	protected static void setMipmaps(TextureTarget target, int base, int max) {
		if (base != 0)
			GL11.glTexParameteri(target.value, GL12.GL_TEXTURE_BASE_LEVEL, base);
		GL11.glTexParameteri(target.value, GL12.GL_TEXTURE_MAX_LEVEL, max);
		
	}
	
	private static String dimensions(int[] values) {
		if (values.length == 0)
			return "()";
		String s = "(" + values[0];
		for (int i = 1; i < values.length; i++)
			s += ", " + values[i];
		return s + ")";
	}
	
	/**************************************************/
	/********************** Debug *********************/
	/**************************************************/
	
	@Override
	public void debugQuery() {
		GLDebug.writef(GLDebug.ATTRIB_STRING, "Minification Filter", minFilter);
		GLDebug.writef(GLDebug.ATTRIB_STRING, "Magnification Filter", magFilter);
		
		GLDebug.writef(GLDebug.ATTRIB + "[%4f, %4f] + %4f", "LOD Range", lodMin, lodMax, lodBias);
		
		GLDebug.writef(GLDebug.ATTRIB_STRING + "\t%s\t%s\t%s", "Texture Swizzle", swizzleR, swizzleG, swizzleB, swizzleA);
		
		GLDebug.writef(GLDebug.ATTRIB_STRING, "Border Color", border);
		
		GLDebug.writef(GLDebug.ATTRIB_STRING, "Depth Stencil Mode", dsmode);
		GLDebug.writef(GLDebug.ATTRIB_STRING, "Depth Comparison Mode", comparison);
	}
}
