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

public class Texture1D extends Texture implements FBOAttachable {
	
	private static final GLObjectTracker<Texture1D> tracker = new GLObjectTracker<Texture1D>();
	private static final BindTracker curr = new BindTracker();
	
	public final static String target = "1D Texture";
	
	private Texture1D(String name) {
		super(name);
	}
	
	public static Texture1D create(String name) {
		if (tracker.contains(name)) {
			Logging.globjError(Texture1D.class, name, "Cannot create", "Already exists");
			return null;
		}
		Texture1D tex = new Texture1D(name);
		if (tex.id == 0) {
			Logging.globjError(Texture1D.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		tracker.add(tex);
		return tex;
	}
	
	public static Texture1D get(String name) {
		return tracker.get(name);
	}
	
	protected static Texture1D get(int id) {
		return tracker.get(id);
	}
	
	public int target() {
		return GL11.GL_TEXTURE_1D;
	}
	
	protected static void bind(int tex) {
		curr.update(tex);
		if (tex == curr.last())
			return;
		GL11.glBindTexture(GL11.GL_TEXTURE_1D, tex);
	}
	
	public static void bind(String name) {
		Texture1D t = get(name);
		if (t == null) {
			Logging.globjError(Texture1D.class, name, "Cannot bind", "Does not exist");
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
		Texture1D tex = tracker.get(name);
		if (tex != null)
			tex.destroy();
		else
			Logging.globjError(Texture1D.class, name, "Cannot delete", "Does not exist");
	}
	
	protected void wrap(TextureWrap s, TextureWrap t, TextureWrap r) {
		GL11.glTexParameteri(target(), GL11.GL_TEXTURE_WRAP_S, s.value);
	}
	
	public void initializeTexture(int w, int maps, TextureFormat texformat) {
		if (init) {
			Logging.globjError(Texture1D.class, name, "Cannot initialize", "Already initialized");
			return;
		}
		if (w < 0) {
			Logging.globjError(Texture1D.class, name, "Cannot initialize", "Dimensions (" + w + ") must be non-negative");
			return;
		}
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		if (w > max) {
			Logging.globjError(Texture1D.class, name, "Cannot initialize", "Dimensions (" + w + ") too large. Device only supports textures up to (" + max
					+ ")");
			return;
		}
		maps = Math.max(1, maps);
		bind();
		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage1D(target(), maps, texformat.value, w);
			init = true;
		}
		else {
			for (int i = 0; i < maps; i++) {
				GL11.glTexImage1D(target(), i, texformat.value, w, 0, texformat.base, DataType.UBYTE.value, (ByteBuffer) null);
				w = Math.max(1, w / 2);
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
	public void setData(int x, int w, int map, ImageFormat format, DataType type, ByteBuffer data) {
		bind();
		GL11.glTexSubImage1D(target(), map, x, w, format.value, type.value, data);
		unbind();
	}
	
	/**************************************************/
	/****************** FBOAttachable *****************/
	/**************************************************/
	
	/**
	 * @param level
	 *            mipmap level.
	 * @param layer
	 *            unused.
	 */
	@Override
	public void attachToFBO(FBOAttachment attachment, int level, int layer) {
		GL30.glFramebufferTexture1D(GL30.GL_DRAW_FRAMEBUFFER, attachment.value, target(), id, level);
	}
	
	/**************************************************/
	
	@Override
	public String[] status() {
		if (id == 0)
			return new String[] { Logging.logText("Texture1D:", "Texture does not exist.", 0) };
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
		FloatBuffer borderColor = BufferUtils.createFloatBuffer(4);
		DepthStencilMode dsmode = DepthStencilMode.get(GL11.glGetTexParameteri(target(), GL43.GL_DEPTH_STENCIL_TEXTURE_MODE));
		int w = GL11.glGetTexLevelParameteri(target(), mipmin, GL11.GL_TEXTURE_WIDTH);
		int comparemode = GL11.glGetTexParameteri(target(), GL14.GL_TEXTURE_COMPARE_MODE);
		TextureComparison comparefunc = TextureComparison.get(GL11.glGetTexParameteri(target(), GL14.GL_TEXTURE_COMPARE_FUNC));
		TextureFormat format = TextureFormat.get(GL11.glGetTexLevelParameteri(target(), mipmin, GL11.GL_TEXTURE_INTERNAL_FORMAT));
		GL11.glGetTexParameter(target(), GL11.GL_TEXTURE_BORDER_COLOR, borderColor);
		unbind();
		
		List<String> status = new ArrayList<String>();
		List<String> errors = GL.readErrorsToList();
		for (String error : errors)
			status.add(Logging.logText("ERROR:", error, 0));
		status.add(Logging.logText("Texture1D:", String.format("%s\t%d px", name, w), 0));
		status.add(Logging.logText(String.format("%-16s:\t%s", "Target", target), 1));
		status.add(Logging.logText(String.format("%-16s:\t%s", "Format", format == null ? "Unrecognized Format" : format), 1));
		status.add(Logging.logText(String.format("%-16s:\t%s", "Minify Filter", min), 1));
		status.add(Logging.logText(String.format("%-16s:\t%s", "Magnify Filter", mag), 1));
		status.add(Logging.logText(String.format("%-16s:\t[%.3f, %.3f] + %.3f", "LOD Range", lodmin, lodmax, lodbias), 1));
		if (min.mipmaps)
			status.add(Logging.logText(String.format("%-16s:\t%d - %d", "Mipmap Range", mipmin, mipmax), 1));
		status.add(Logging.logText(String.format("%-16s:\t[%s, %s, %s, %s]", "Swizzle", r, g, b, a), 1));
		status.add(Logging.logText(String.format("%-16s:\t%s", "Wrap Mode", swrap), 1));
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
