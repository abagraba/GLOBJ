package lwjgl.core.texture;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lwjgl.core.Context;
import lwjgl.core.GL;
import lwjgl.core.GLObject;
import lwjgl.debug.Logging;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL43;

public class Texture3D extends GLObject {
	
	protected static final HashMap<String, Texture3D> texname = new HashMap<String, Texture3D>();
	protected static final HashMap<Integer, Texture3D> texid = new HashMap<Integer, Texture3D>();
	protected static final HashMap<Texture3DTarget, Integer> current = new HashMap<Texture3DTarget, Integer>();
	protected static final HashMap<Texture3DTarget, Integer> last = new HashMap<Texture3DTarget, Integer>();
	
	private final int tex;
	private final Texture3DTarget target;
	
	private Texture3D(String name, Texture3DTarget target) {
		super(name);
		tex = GL11.glGenTextures();
		this.target = target;
	}
	
	public static Texture3D create(String name, Texture3DTarget target) {
		if (texname.containsKey(name)) {
			Logging.glError("Cannot create Texture3D. Texture3D [" + name + "] already exists.", null);
			return null;
		}
		Texture3D tex = new Texture3D(name, target);
		if (tex.tex == 0) {
			Logging.glError("Cannot create Texture3D. No ID could be allocated for Texture3D [" + name + "].", null);
			return null;
		}
		texname.put(tex.name, tex);
		texid.put(tex.tex, tex);
		return tex;
	}
	
	public static Texture3D get(String name) {
		return texname.get(name);
	}
	
	protected static Texture3D get(int id) {
		return texid.get(id);
	}
	
	protected static void bind(int tex, Texture3DTarget target) {
		int c = current.containsKey(target) ? current.get(target) : 0;
		if (tex == c) {
			last.put(target, c);
			return;
		}
		GL11.glBindTexture(target.value, tex);
		last.put(target, c);
		current.put(target, tex);
	}
	
	public static void bind(String name) {
		Texture3D t = get(name);
		if (t == null) {
			Logging.glError("Cannot bind Texture3D [" + name + "]. Does not exist.", null);
			return;
		}
		t.bind();
	}
	
	public void bind() {
		bind(tex, target);
	}
	
	private static void bindLast(Texture3DTarget target) {
		int l = last.containsKey(target) ? last.get(target) : 0;
		bind(l, target);
	}
	
	public static void destroy(String name) {
		if (!texname.containsKey(name)) {
			Logging.glWarning("Cannot delete Texture3D. Texture3D [" + name + "] does not exist.");
			return;
		}
		Texture3D tex = get(name);
		if (current.get(tex.target) == tex.tex)
			bind(0, tex.target);
		GL11.glDeleteTextures(tex.tex);
		texname.remove(tex.name);
		texid.remove(tex.tex);
	}
	
	public void setLOD(float min, float max, float bias) {
		bind();
		GL11.glTexParameterf(target.value, GL12.GL_TEXTURE_MIN_LOD, min);
		GL11.glTexParameterf(target.value, GL12.GL_TEXTURE_MAX_LOD, max);
		GL11.glTexParameterf(target.value, GL14.GL_TEXTURE_LOD_BIAS, bias);
		bindLast(target);
	}
	
	public void setFilter(MinifyFilter min, MagnifyFilter mag) {
		bind();
		GL11.glTexParameteri(target.value, GL11.GL_TEXTURE_MIN_FILTER, min.value);
		GL11.glTexParameteri(target.value, GL11.GL_TEXTURE_MAG_FILTER, mag.value);
		bindLast(target);
	}
	
	public void setMipMapRange(int base, int max) {
		bind();
		GL11.glTexParameteri(target.value, GL12.GL_TEXTURE_BASE_LEVEL, base);
		GL11.glTexParameteri(target.value, GL12.GL_TEXTURE_MAX_LEVEL, max);
		bindLast(target);
	}
	
	public void setSwizzle(Swizzle r, Swizzle g, Swizzle b, Swizzle a) {
		bind();
		IntBuffer swizzle = BufferUtils.createIntBuffer(4);
		swizzle.put(new int[] { r.value, g.value, b.value, a.value }).flip();
		GL11.glTexParameter(target.value, GL33.GL_TEXTURE_SWIZZLE_RGBA, swizzle);
		bindLast(target);
	}
	
	public void setWrap(TextureWrap s, TextureWrap t, TextureWrap r) {
		bind();
		GL11.glTexParameteri(target.value, GL11.GL_TEXTURE_WRAP_S, s.value);
		GL11.glTexParameteri(target.value, GL11.GL_TEXTURE_WRAP_T, t.value);
		GL11.glTexParameteri(target.value, GL12.GL_TEXTURE_WRAP_R, r.value);
		bindLast(target);
	}
	
	public void setBorderColor(float r, float g, float b, float a) {
		bind();
		FloatBuffer color = BufferUtils.createFloatBuffer(4);
		color.put(new float[] { r, g, b, a }).flip();
		GL11.glTexParameter(target.value, GL11.GL_TEXTURE_BORDER_COLOR, color);
		bindLast(target);
	}
	
	public void setDepthComparisonMode(TextureComparison mode) {
		bind();
		GL11.glTexParameteri(target.value, GL14.GL_TEXTURE_COMPARE_MODE, mode.mode);
		GL11.glTexParameteri(target.value, GL14.GL_TEXTURE_COMPARE_FUNC, mode.func);
		bindLast(target);
	}
	
	public void setData(Texture3DDataTarget dataTarget, int w, int h, int d, int lod, TextureFormat texformat,
			ImageFormat format, ImageDataType type, ByteBuffer data) {
		if (w < 0 || h < 0) {
			Logging.glError("Cannot set data of Texture3D [" + name + "] with dimensions (" + w + "," + h
					+ "). Dimensions must be non-negative.", this);
			return;
		}
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		if (w > max || h > max) {
			Logging.glError("Cannot set data of Texture3D [" + name + "] with dimensions (" + w + "," + h
					+ "). Device only supports textures up to (" + max + "," + max + ").", this);
			return;
		}
		if (dataTarget.parent != target) {
			Logging.glError("Invalid Data Target. " + dataTarget + " is not a valid data target for " + target + ".",
					this);
			return;
		}
		bind();
		GL12.glTexImage3D(dataTarget.value, lod, texformat.value, w, h, d, 0, format.value, type.value, data);
		bindLast(target);
	}
	
	@Override
	public String[] status() {
		if (tex == 0)
			return new String[] { Logging.logText("Texture3D:", "Texture does not exist.", 0) };
		GL.flushErrors();
		
		bind();
		MinifyFilter min = MinifyFilter.get(GL11.glGetTexParameteri(target.value, GL11.GL_TEXTURE_MIN_FILTER));
		MagnifyFilter mag = MagnifyFilter.get(GL11.glGetTexParameteri(target.value, GL11.GL_TEXTURE_MAG_FILTER));
		float lodmin = GL11.glGetTexParameterf(target.value, GL12.GL_TEXTURE_MIN_LOD);
		float lodmax = GL11.glGetTexParameterf(target.value, GL12.GL_TEXTURE_MAX_LOD);
		float lodbias = GL11.glGetTexParameterf(target.value, GL14.GL_TEXTURE_LOD_BIAS);
		int mipmin = GL11.glGetTexParameteri(target.value, GL12.GL_TEXTURE_BASE_LEVEL);
		int mipmax = GL11.glGetTexParameteri(target.value, GL12.GL_TEXTURE_MAX_LEVEL);
		Swizzle r = Swizzle.get(GL11.glGetTexParameteri(target.value, GL33.GL_TEXTURE_SWIZZLE_R));
		Swizzle g = Swizzle.get(GL11.glGetTexParameteri(target.value, GL33.GL_TEXTURE_SWIZZLE_G));
		Swizzle b = Swizzle.get(GL11.glGetTexParameteri(target.value, GL33.GL_TEXTURE_SWIZZLE_B));
		Swizzle a = Swizzle.get(GL11.glGetTexParameteri(target.value, GL33.GL_TEXTURE_SWIZZLE_A));
		TextureWrap swrap = TextureWrap.get(GL11.glGetTexParameteri(target.value, GL11.GL_TEXTURE_WRAP_S));
		TextureWrap twrap = TextureWrap.get(GL11.glGetTexParameteri(target.value, GL11.GL_TEXTURE_WRAP_T));
		TextureWrap rwrap = TextureWrap.get(GL11.glGetTexParameteri(target.value, GL12.GL_TEXTURE_WRAP_R));
		FloatBuffer borderColor = BufferUtils.createFloatBuffer(4);
		DepthStencilMode dsmode = DepthStencilMode.get(GL11.glGetTexParameteri(target.value,
				GL43.GL_DEPTH_STENCIL_TEXTURE_MODE));
		int w = GL11.glGetTexLevelParameteri(target.value, mipmin, GL11.GL_TEXTURE_WIDTH);
		int h = GL11.glGetTexLevelParameteri(target.value, mipmin, GL11.GL_TEXTURE_HEIGHT);
		int d = GL11.glGetTexLevelParameteri(target.value, mipmin, GL12.GL_TEXTURE_DEPTH);
		TextureFormat format = TextureFormat.get(GL11.glGetTexLevelParameteri(target.value, mipmin,
				GL11.GL_TEXTURE_INTERNAL_FORMAT));
		GL11.glGetTexParameter(target.value, GL11.GL_TEXTURE_BORDER_COLOR, borderColor);
		bindLast(target);
		
		List<String> status = new ArrayList<String>();
		List<String> errors = GL.readErrorsToList();
		for (String error : errors)
			status.add(Logging.logText("ERROR:", error, 0));
		status.add(Logging.logText("Texture3D:", String.format("%s [%d x %d x %d]", name, w, h, d), 0));
		status.add(Logging.logText(String.format("%-16s:\t%s", "Target", target), 1));
		status.add(Logging.logText(
				String.format("%-16s:\t%s", "Format", format == null ? "Unrecognized Format" : format), 1));
		status.add(Logging.logText(String.format("%-16s:\t%s", "Minify Filter", min), 1));
		status.add(Logging.logText(String.format("%-16s:\t%s", "Magnify Filter", mag), 1));
		status.add(Logging.logText(String.format("%-16s:\t[%.3f, %.3f] + %.3f", "LOD Range", lodmin, lodmax, lodbias),
				1));
		if (min.mipmaps)
			status.add(Logging.logText(String.format("%-16s:\t%d - %d", "Mipmap Range", mipmin, mipmax), 1));
		status.add(Logging.logText(String.format("%-16s:\t[%s, %s, %s, %s]", "Swizzle", r, g, b, a), 1));
		status.add(Logging.logText(String.format("%-16s:\t%s, %s, %s", "Wrap Mode", swrap, twrap, rwrap), 1));
		status.add(Logging.logText(String.format("%-16s:\t[%.3f, %.3f, %.3f, %.3f]", "Border Color", borderColor.get(),
				borderColor.get(), borderColor.get(), borderColor.get()), 1));
		status.add(Logging.logText(String.format("%-16s:\t%s", "Depth/Stencil Mode", dsmode), 1));
		return status.toArray(new String[status.size()]);
	}
	
}
