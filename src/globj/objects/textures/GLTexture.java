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

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import lwjgl.core.states.State;
import lwjgl.debug.GLDebug;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL43;



public abstract class GLTexture extends BindableGLObject {
	
	protected final State<MinifyFilter>			minFilter	= new State<MinifyFilter>("Minification Filter", MinifyFilter.NEAREST_MIPMAP_LINEAR);
	protected final State<MagnifyFilter>		magFilter	= new State<MagnifyFilter>("Magnification Filter", MagnifyFilter.LINEAR);
	
	protected final State<Float>				lodMin		= new State<Float>("Minimum LOD", -1000f);
	protected final State<Float>				lodMax		= new State<Float>("Maximum LOD", 1000f);
	protected final State<Float>				lodBias		= new State<Float>("LOD Bias", 0f);
	
	protected final State<Swizzle>				swizzleR	= new State<Swizzle>("Swizzle Red", Swizzle.R);
	protected final State<Swizzle>				swizzleG	= new State<Swizzle>("Swizzle Green", Swizzle.G);
	protected final State<Swizzle>				swizzleB	= new State<Swizzle>("Swizzle Blue", Swizzle.B);
	protected final State<Swizzle>				swizzleA	= new State<Swizzle>("Swizzle Alpha", Swizzle.A);
	
	protected final State<V4f>					border		= new State<V4f>("Border", new V4f(0, 0, 0, 0));
	
	protected final State<DepthStencilMode>		dsmode		= new State<DepthStencilMode>("Depth Stencil Mode", DepthStencilMode.DEPTH);
	
	protected final State<TextureComparison>	comparison	= new State<TextureComparison>("Depth Comparison Mode", TextureComparison.NONE);
	
	protected final TextureFormat				texformat;
	protected final TextureTarget				target;
	
	
	protected GLTexture(String name, TextureFormat texformat, TextureTarget target) {
		super(name, GL11.glGenTextures());
		this.texformat = texformat;
		this.target = target;
	}
	
	public void update() {
		bind();
		resolveStates();
		undobind();
	}
	
	protected void resolveStates() {
		if (!minFilter.resolved()) {
			GL11.glTexParameteri(target.value, GL11.GL_TEXTURE_MIN_FILTER, minFilter.state().value);
			minFilter.resolve();
		}
		if (!magFilter.resolved()) {
			GL11.glTexParameteri(target.value, GL11.GL_TEXTURE_MAG_FILTER, magFilter.state().value);
			magFilter.resolve();
		}
		if (!lodMin.resolved()) {
			GL11.glTexParameterf(target.value, GL12.GL_TEXTURE_MIN_LOD, lodMin.state());
			lodMin.resolve();
		}
		if (!lodMax.resolved()) {
			GL11.glTexParameterf(target.value, GL12.GL_TEXTURE_MAX_LOD, lodMax.state());
			lodMax.resolve();
		}
		if (!lodBias.resolved()) {
			GL11.glTexParameterf(target.value, GL14.GL_TEXTURE_LOD_BIAS, lodBias.state());
			lodBias.resolve();
		}
		if (!swizzleR.resolved() || !swizzleG.resolved() || !swizzleB.resolved() || !swizzleA.resolved()) {
			IntBuffer swizzle = BufferUtils.createIntBuffer(4);
			swizzle.put(new int[] { swizzleR.state().value, swizzleG.state().value, swizzleB.state().value, swizzleA.state().value }).flip();
			GL11.glTexParameter(target.value, GL11.GL_TEXTURE_BORDER_COLOR, swizzle);
			swizzleR.resolve();
			swizzleG.resolve();
			swizzleB.resolve();
			swizzleA.resolve();
		}
		if (!border.resolved()) {
			FloatBuffer color = BufferUtils.createFloatBuffer(4);
			V4f c = border.state();
			color.put(new float[] { c.x, c.y, c.z, c.w }).flip();
			GL11.glTexParameter(target.value, GL11.GL_TEXTURE_BORDER_COLOR, color);
			border.resolve();
		}
		if (!dsmode.resolved() && GL.versionCheck(4, 3)) {
			GL11.glTexParameteri(target.value, GL43.GL_DEPTH_STENCIL_TEXTURE_MODE, dsmode.state().value);
			dsmode.resolve();
		}
		if (!comparison.resolved()) {
			GL11.glTexParameteri(target.value, GL14.GL_TEXTURE_COMPARE_MODE, comparison.state().mode);
			GL11.glTexParameteri(target.value, GL14.GL_TEXTURE_COMPARE_FUNC, comparison.state().func);
			comparison.resolve();
		}
	}
	
	/*
	 * TODO : Textures Texture Buffers 2D Multisample & Arrays
	 * https://www.opengl.org/registry/specs/ARB/texture_multisample.txt Gen mipmaps Texture views Initialize texture
	 * with gltexstorage fallback to glteximage
	 */
	
	public void genMipmaps() {
		// GL30.glGenerateMipmap(target());
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
		lodMin.setState(min);
		lodMax.setState(max);
		lodBias.setState(bias);
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
		minFilter.setState(min);
		magFilter.setState(mag);
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
		swizzleR.setState(r);
		swizzleG.setState(g);
		swizzleB.setState(b);
		swizzleA.setState(a);
		/*
		 * IntBuffer swizzle = BufferUtils.createIntBuffer(4); swizzle.put(new int[] { r.value, g.value, b.value,
		 * a.value }).flip(); GL11.glTexParameter(target(), GL33.GL_TEXTURE_SWIZZLE_RGBA, swizzle);
		 */
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
		border.setState(new V4f(r, g, b, a));
	}
	
	/**
	 * Sets the the depth/stencil mode.
	 * 
	 * @param func
	 *            depth/stencil mode.
	 */
	public void setDepthStencilMode(DepthStencilMode dsmode) {
		this.dsmode.setState(dsmode);
	}
	
	/**
	 * Sets the comparison mode for the texture. Only useful if the texture has a depth component.
	 * 
	 * @param func
	 *            depth comparison function.
	 */
	public void setDepthComparisonMode(TextureComparison func) {
		comparison.setState(func);
	}
	
	/**************************************************/
	/********************** Bind **********************/
	/**************************************************/
	
	@Override
	protected void bindOP(int id) {
		GL11.glBindTexture(target.value, id);
	}
	
	@Override
	protected void destroyOP() {
		GL11.glDeleteTextures(id);
	}
	
	@Override
	public void destroy() {
		super.destroy();
	}
	
	/**************************************************/
	/***************** Helper Methods *****************/
	/**************************************************/
	
	protected static int levels(int x) {
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
}
