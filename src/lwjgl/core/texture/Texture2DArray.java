package lwjgl.core.texture;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import lwjgl.core.Context;
import lwjgl.core.GL;
import lwjgl.core.GLObjectTracker;
import lwjgl.core.framebuffer.FBOAttachable;
import lwjgl.core.framebuffer.values.FBOAttachment;
import lwjgl.core.texture.values.DepthStencilMode;
import lwjgl.core.texture.values.MagnifyFilter;
import lwjgl.core.texture.values.MinifyFilter;
import lwjgl.core.texture.values.Swizzle;
import lwjgl.core.texture.values.TextureComparison;
import lwjgl.core.texture.values.TextureFormat;
import lwjgl.core.texture.values.TextureWrap;
import lwjgl.core.values.DataType;
import lwjgl.debug.Logging;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL42;
import org.lwjgl.opengl.GL43;

public class Texture2DArray extends Texture implements FBOAttachable {
	
	private static final GLObjectTracker<Texture2DArray> tracker = new GLObjectTracker<Texture2DArray>();
	private static final BindTracker curr = new BindTracker();
	
	public final static String target = "2D Texture Array";
	
	private Texture2DArray(String name) {
		super(name);
	}
	
	public static Texture2DArray create(String name) {
		if (tracker.contains(name)) {
			Logging.globjError(Texture2DArray.class, name, "Cannot create", "Already exists");
			return null;
		}
		Texture2DArray tex = new Texture2DArray(name);
		if (tex.id == 0) {
			Logging.globjError(Texture2DArray.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		tracker.add(tex);
		return tex;
	}
	
	public static Texture2DArray get(String name) {
		return tracker.get(name);
	}
	
	protected static Texture2DArray get(int id) {
		return tracker.get(id);
	}
	
	public int target() {
		return GL30.GL_TEXTURE_2D_ARRAY;
	}
	
	protected static void bind(int tex) {
		curr.update(tex);
		if (tex == curr.last())
			return;
		GL11.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, tex);
	}
	
	public static void bind(String name) {
		Texture2DArray t = get(name);
		if (t == null) {
			Logging.globjError(Texture2DArray.class, name, "Cannot bind", "Does not exist");
			return;
		}
		t.bind();
	}
	
	public void bind() {
		bind(id);
	}
	
	protected void unbind() {
		bind(curr.revert());
	}
	
	public void destroy() {
		if (curr.value() == id)
			bind(0);
		GL11.glDeleteTextures(id);
		tracker.remove(this);
	}
	
	public static void destroy(String name) {
		Texture2DArray tex = tracker.get(name);
		if (tex != null)
			tex.destroy();
		else
			Logging.globjError(Texture2DArray.class, name, "Cannot delete", "Does not exist");
	}
	
	protected void wrap(TextureWrap s, TextureWrap t, TextureWrap r) {
		GL11.glTexParameteri(target(), GL11.GL_TEXTURE_WRAP_S, s.value);
		GL11.glTexParameteri(target(), GL11.GL_TEXTURE_WRAP_T, t.value);
	}
	
	public void initializeTexture(int w, int h, int layers, int maps, TextureFormat texformat) {
		if (init) {
			Logging.globjError(Texture2DArray.class, name, "Cannot initialize", "Already initialized");
			return;
		}
		if (w < 0 || h < 0 || layers < 0) {
			Logging.globjError(Texture2DArray.class, name, "Cannot initialize", "Dimensions (" + w + " x " + h + " x " + layers + ") must be non-negative");
			return;
		}
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		int maxlayers = Context.intConst(GL30.GL_MAX_ARRAY_TEXTURE_LAYERS);
		if (w > max || h > max || layers > maxlayers) {
			Logging.globjError(Texture2DArray.class, name, "Cannot initialize", "Dimensions (" + w + " x " + h + " x " + layers
					+ ") too large. Device only supports textures up to (" + max + " x " + max + " x " + maxlayers + ")");
			return;
		}
		maps = Math.max(1, maps);
		bind();
		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage3D(target(), maps, texformat.value, w, h, layers);
			init = true;
		}
		else {
			for (int i = 0; i < maps; i++) {
				GL12.glTexImage3D(target(), i, texformat.value, w, h, layers, 0, texformat.base, DataType.UBYTE.value, (ByteBuffer) null);
				w = Math.max(1, w / 2);
				h = Math.max(1, h / 2);
			}
		}
		unbind();
	}
	
	/**
	 * Sets the texel data in specified rectangle of mipmap level. Texture needs
	 * to be initialized with
	 * {@link #initializeTexture(int, int, int, TextureFormat)}. Rectangle must
	 * be within the bounds of the texture. [GL_TEXTURE_BASE_LEVEL + map].
	 */
	public void setData(int x, int y, int w, int h, int layeri, int layerf, int map, ImageFormat format, DataType type, ByteBuffer data) {
		bind();
		GL12.glTexSubImage3D(target(), map, x, y, layeri, w, h, layerf, format.value, type.value, data);
		unbind();
	}
	
	/**************************************************/
	/****************** FBOAttachable *****************/
	/**************************************************/
	/**
	 * @param level
	 *            mipmap level.
	 * @param layer
	 *            texture index.
	 */
	@Override
	public void attachToFBO(FBOAttachment attachment, int level, int layer) {
		GL30.glFramebufferTextureLayer(GL30.GL_DRAW_FRAMEBUFFER, attachment.value, id, level, layer);
	}
	/**************************************************/
	
	@Override
	public String[] status() {
		if (id == 0)
			return new String[] { Logging.logText("Texture2DArray:", "Texture does not exist.", 0) };
		GL.flushErrors();
		
		bind();
		MinifyFilter min = MinifyFilter.get(GL11.glGetTexParameteri(target(), GL11.GL_TEXTURE_MIN_FILTER));
		MagnifyFilter mag = MagnifyFilter.get(GL11.glGetTexParameteri(target(), GL11.GL_TEXTURE_MAG_FILTER));
		float lodmin = GL11.glGetTexParameterf(target(), GL12.GL_TEXTURE_MIN_LOD);
		float lodmax = GL11.glGetTexParameterf(target(), GL12.GL_TEXTURE_MAX_LOD);
		float lodbias = GL11.glGetTexParameterf(target(), GL14.GL_TEXTURE_LOD_BIAS);
		int mipmin = GL11.glGetTexParameteri(target(), GL12.GL_TEXTURE_BASE_LEVEL);
		int mipmax = GL11.glGetTexParameteri(target(), GL12.GL_TEXTURE_MAX_LEVEL);
		Swizzle r = Swizzle.get(GL11.glGetTexParameteri(target(), GL33.GL_TEXTURE_SWIZZLE_R));
		Swizzle g = Swizzle.get(GL11.glGetTexParameteri(target(), GL33.GL_TEXTURE_SWIZZLE_G));
		Swizzle b = Swizzle.get(GL11.glGetTexParameteri(target(), GL33.GL_TEXTURE_SWIZZLE_B));
		Swizzle a = Swizzle.get(GL11.glGetTexParameteri(target(), GL33.GL_TEXTURE_SWIZZLE_A));
		TextureWrap swrap = TextureWrap.get(GL11.glGetTexParameteri(target(), GL11.GL_TEXTURE_WRAP_S));
		TextureWrap twrap = TextureWrap.get(GL11.glGetTexParameteri(target(), GL11.GL_TEXTURE_WRAP_T));
		FloatBuffer borderColor = BufferUtils.createFloatBuffer(4);
		DepthStencilMode dsmode = DepthStencilMode.get(GL11.glGetTexParameteri(target(), GL43.GL_DEPTH_STENCIL_TEXTURE_MODE));
		int w = GL11.glGetTexLevelParameteri(target(), mipmin, GL11.GL_TEXTURE_WIDTH);
		int h = GL11.glGetTexLevelParameteri(target(), mipmin, GL11.GL_TEXTURE_HEIGHT);
		int d = GL11.glGetTexLevelParameteri(target(), mipmin, GL12.GL_TEXTURE_DEPTH);
		int comparemode = GL11.glGetTexParameteri(target(), GL14.GL_TEXTURE_COMPARE_MODE);
		TextureComparison comparefunc = TextureComparison.get(GL11.glGetTexParameteri(target(), GL14.GL_TEXTURE_COMPARE_FUNC));
		TextureFormat format = TextureFormat.get(GL11.glGetTexLevelParameteri(target(), mipmin, GL11.GL_TEXTURE_INTERNAL_FORMAT));
		GL11.glGetTexParameter(target(), GL11.GL_TEXTURE_BORDER_COLOR, borderColor);
		unbind();
		
		List<String> status = new ArrayList<String>();
		List<String> errors = GL.readErrorsToList();
		for (String error : errors)
			status.add(Logging.logText("ERROR:", error, 0));
		status.add(Logging.logText("Texture1D:", String.format("%s\t%d x %d px\t%d layers", name, w, h, d), 0));
		status.add(Logging.logText(String.format("%-16s:\t%s", "Target", target), 1));
		status.add(Logging.logText(String.format("%-16s:\t%s", "Format", format == null ? "Unrecognized Format" : format), 1));
		status.add(Logging.logText(String.format("%-16s:\t%s", "Minify Filter", min), 1));
		status.add(Logging.logText(String.format("%-16s:\t%s", "Magnify Filter", mag), 1));
		status.add(Logging.logText(String.format("%-16s:\t[%.3f, %.3f] + %.3f", "LOD Range", lodmin, lodmax, lodbias), 1));
		if (min.mipmaps)
			status.add(Logging.logText(String.format("%-16s:\t%d - %d", "Mipmap Range", mipmin, mipmax), 1));
		status.add(Logging.logText(String.format("%-16s:\t[%s, %s, %s, %s]", "Swizzle", r, g, b, a), 1));
		status.add(Logging.logText(String.format("%-16s:\t%s %s", "Wrap Mode", swrap, twrap), 1));
		status.add(Logging.logText(
				String.format("%-16s:\t[%.3f, %.3f, %.3f, %.3f]", "Border Color", borderColor.get(), borderColor.get(), borderColor.get(), borderColor.get()),
				1));
		if (format.depth) {
			TextureComparison func = comparemode == GL11.GL_NONE ? TextureComparison.NONE : comparefunc;
			status.add(Logging.logText(String.format("%-24s:\t%s", "Texture Compare Function", func), 1));
			if (format.stencil)
				status.add(Logging.logText(String.format("%-16s:\t%s", "Depth/Stencil Mode", dsmode), 1));
		}
		return status.toArray(new String[status.size()]);
	}
	
}
