package lwjgl.core.texture;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
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

public class Texture2D extends GLObject {
	
	protected static final HashMap<String, Texture2D> texname = new HashMap<String, Texture2D>();
	protected static final HashMap<Integer, Texture2D> texid = new HashMap<Integer, Texture2D>();
	protected static final HashMap<Texture2DTarget, Integer> current = new HashMap<Texture2DTarget, Integer>();
	protected static final HashMap<Texture2DTarget, Integer> last = new HashMap<Texture2DTarget, Integer>();
	
	private final int tex;
	private final Texture2DTarget target;
	
	private static Color clear = new Color(0f, 0f, 0f, 0f);
	@SuppressWarnings("unused")
	private static ColorModel model = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] {
			8, 8, 8, 0 }, false, false, ComponentColorModel.OPAQUE, DataBuffer.TYPE_BYTE);
	
	private static ColorModel modelA = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] {
			8, 8, 8, 8 }, true, false, ComponentColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);
	
	private Texture2D(String name, Texture2DTarget target) {
		super(name);
		tex = GL11.glGenTextures();
		this.target = target;
	}
	
	public static Texture2D create(String name, Texture2DTarget target) {
		if (texname.containsKey(name)) {
			Logging.glError("Cannot create Texture2D. Texture2D [" + name + "] already exists.", null);
			return null;
		}
		Texture2D tex = new Texture2D(name, target);
		if (tex.tex == 0) {
			Logging.glError("Cannot create Texture2D. No ID could be allocated for Texture2D [" + name + "].", null);
			return null;
		}
		texname.put(tex.name, tex);
		texid.put(tex.tex, tex);
		return tex;
	}
	
	public static Texture2D get(String name) {
		return texname.get(name);
	}
	
	protected static Texture2D get(int id) {
		return texid.get(id);
	}
	
	protected static void bind(int tex, Texture2DTarget target) {
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
		Texture2D t = get(name);
		if (t == null) {
			Logging.glError("Cannot bind Texture2D [" + name + "]. Does not exist.", null);
			return;
		}
		t.bind();
	}
	
	public void bind() {
		bind(tex, target);
	}
	
	private static void bindLast(Texture2DTarget target) {
		int l = last.containsKey(target) ? last.get(target) : 0;
		bind(l, target);
	}
	
	public static void destroy(String name) {
		if (!texname.containsKey(name)) {
			Logging.glWarning("Cannot delete Texture2D. Texture2D [" + name + "] does not exist.");
			return;
		}
		Texture2D tex = get(name);
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
	
	public void setWrap(TextureWrap s, TextureWrap t) {
		bind();
		GL11.glTexParameteri(target.value, GL11.GL_TEXTURE_WRAP_S, s.value);
		GL11.glTexParameteri(target.value, GL11.GL_TEXTURE_WRAP_T, t.value);
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
	
	public void setData(Texture2DDataTarget dataTarget, int w, int h, int lod, TextureFormat texformat,
			ImageFormat format, ImageDataType type, ByteBuffer data) {
		if (w < 0 || h < 0) {
			Logging.glError("Cannot set data of Texture2D [" + name + "] with dimensions (" + w + "," + h
					+ "). Dimensions must be non-negative.", this);
			return;
		}
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		if (w > max || h > max) {
			Logging.glError("Cannot set data of Texture2D [" + name + "] with dimensions (" + w + "," + h
					+ "). Device only supports textures up to (" + max + "," + max + ").", this);
			return;
		}
		if (dataTarget.parent != target) {
			Logging.glError("Invalid Data Target. " + dataTarget + " is not a valid data target for " + target + ".",
					this);
			return;
		}
		bind();
		GL11.glTexImage2D(dataTarget.value, lod, texformat.value, w, h, 0, format.value, type.value, data);
		bindLast(target);
	}
	
	public void setData(Texture2DDataTarget dataTarget, int x, int y, int w, int h, int lod, ImageFormat format,
			ImageDataType type, ByteBuffer data) {
		if (w < 0 || h < 0) {
			Logging.glError("Cannot set data of Texture2D [" + name + "] with dimensions (" + w + "," + h
					+ "). Dimensions must be non-negative.", this);
			return;
		}
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		if (w > max || h > max) {
			Logging.glError("Cannot set data of Texture2D [" + name + "] with dimensions (" + w + "," + h
					+ "). Device only supports textures up to (" + max + "," + max + ").", this);
			return;
		}
		if (dataTarget.parent != target) {
			Logging.glError("Invalid Data Target. " + dataTarget + " is not a valid data target for " + target + ".",
					this);
			return;
		}
		bind();
		GL11.glTexSubImage2D(dataTarget.value, lod, x, y, w, h, format.value, type.value, data);
		bindLast(target);
	}
	
	public void setData(Texture2DDataTarget dataTarget, BufferedImage src, int lod, TextureFormat texformat) {
		setData(dataTarget, src.getWidth(), src.getHeight(), lod, texformat, ImageFormat.RGBA, ImageDataType.UBYTE,
				imageDataTransparent(src));
	}
	
	public static ByteBuffer imageDataTransparent(BufferedImage image) {
		WritableRaster raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, image.getWidth(),
				image.getHeight(), 4, null);
		@SuppressWarnings("rawtypes")
		BufferedImage formatted = new BufferedImage(modelA, raster, false, new Hashtable());
		
		Graphics g = formatted.getGraphics();
		g.setColor(clear);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		g.drawImage(image, 0, 0, null);
		
		byte[] data = ((DataBufferByte) formatted.getRaster().getDataBuffer()).getData();
		ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
		buffer.order(ByteOrder.nativeOrder());
		buffer.put(data, 0, data.length).flip();
		return buffer;
	}
	
	@Override
	public String[] status() {
		if (tex == 0)
			return new String[] { Logging.logText("Texture2D:", "Texture does not exist.", 0) };
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
		FloatBuffer borderColor = BufferUtils.createFloatBuffer(4);
		DepthStencilMode dsmode = DepthStencilMode.get(GL11.glGetTexParameteri(target.value,
				GL43.GL_DEPTH_STENCIL_TEXTURE_MODE));
		int w = GL11.glGetTexLevelParameteri(target.value, mipmin, GL11.GL_TEXTURE_WIDTH);
		int h = GL11.glGetTexLevelParameteri(target.value, mipmin, GL11.GL_TEXTURE_HEIGHT);
		int comparemode = GL11.glGetTexParameteri(target.value, GL14.GL_TEXTURE_COMPARE_MODE);
		TextureComparison comparefunc = TextureComparison.get(GL11.glGetTexParameteri(target.value,
				GL14.GL_TEXTURE_COMPARE_FUNC));
		TextureFormat format = TextureFormat.get(GL11.glGetTexLevelParameteri(target.value, mipmin,
				GL11.GL_TEXTURE_INTERNAL_FORMAT));
		GL11.glGetTexParameter(target.value, GL11.GL_TEXTURE_BORDER_COLOR, borderColor);
		bindLast(target);
		
		List<String> status = new ArrayList<String>();
		List<String> errors = GL.readErrorsToList();
		for (String error : errors)
			status.add(Logging.logText("ERROR:", error, 0));
		status.add(Logging.logText("Texture2D:", String.format("%s [%d x %d]", name, w, h), 0));
		status.add(Logging.logText(String.format("%-24s:\t%s", "Target", target), 1));
		status.add(Logging.logText(
				String.format("%-24s:\t%s", "Format", format == null ? "Unrecognized Format" : format), 1));
		status.add(Logging.logText(String.format("%-24s:\t%s", "Minify Filter", min), 1));
		status.add(Logging.logText(String.format("%-24s:\t%s", "Magnify Filter", mag), 1));
		status.add(Logging.logText(String.format("%-24s:\t[%.3f, %.3f] + %.3f", "LOD Range", lodmin, lodmax, lodbias),
				1));
		if (min.mipmaps)
			status.add(Logging.logText(String.format("%-24s:\t%d - %d", "Mipmap Range", mipmin, mipmax), 1));
		status.add(Logging.logText(String.format("%-24s:\t[%s, %s, %s, %s]", "Swizzle", r, g, b, a), 1));
		status.add(Logging.logText(String.format("%-24s:\t%s, %s", "Wrap Mode", swrap, twrap), 1));
		status.add(Logging.logText(String.format("%-24s:\t[%.3f, %.3f, %.3f, %.3f]", "Border Color", borderColor.get(),
				borderColor.get(), borderColor.get(), borderColor.get()), 1));
		if (format.depth){
			status.add(Logging.logText(String.format("%-24s:\t%s", "Depth/Stencil Mode", dsmode), 1));
			TextureComparison func = comparemode == GL11.GL_NONE ? TextureComparison.NONE : comparefunc;
			status.add(Logging.logText(String.format("%-24s:\t%s", "Texture Compare Function", func), 1));
		}
		return status.toArray(new String[status.size()]);
	}
	
}
