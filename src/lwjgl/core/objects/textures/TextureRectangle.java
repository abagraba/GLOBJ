package lwjgl.core.objects.textures;

import java.nio.ByteBuffer; 

import lwjgl.core.Context;
import lwjgl.core.GL;
import lwjgl.core.objects.GLObjectTracker;
import lwjgl.core.objects.framebuffers.FBOAttachable;
import lwjgl.core.objects.framebuffers.values.FBOAttachment;
import lwjgl.core.objects.textures.values.TextureFormat;
import lwjgl.core.objects.textures.values.TextureTarget;
import lwjgl.core.values.DataType;
import lwjgl.debug.Logging;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL42;

public class TextureRectangle extends GLTexture2D implements FBOAttachable {
	
	private static final GLObjectTracker<TextureRectangle> tracker = new GLObjectTracker<TextureRectangle>();
	private static final BindTracker curr = new BindTracker();
	
	public final static TextureTarget target = TextureTarget.TEXTURE_RECTANGLE;
	
	private int w, h;

	private TextureRectangle(String name, TextureFormat texformat) {
		super(name, texformat, target);
	}
	
	public static TextureRectangle create(String name, TextureFormat texformat, int w, int h) {
		if (tracker.contains(name)) {
			Logging.globjError(TextureRectangle.class, name, "Cannot create", "Already exists");
			return null;
		}
		TextureRectangle tex = new TextureRectangle(name, texformat);
		if (tex.id == 0) {
			Logging.globjError(TextureRectangle.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		
		int max = Context.intConst(GL11.GL_MAX_TEXTURE_SIZE);
		if (!checkBounds(new int[] { w, h }, new int[] { max, max }, tex))
			return null;
		
		tex.w = w;
		tex.h = h;
		
		tex.bind();
		if (GL.versionCheck(4, 2)) {
			GL42.glTexStorage2D(target.value, 0, texformat.value, w, h);
		}
		else {
			GL11.glTexImage2D(target.value, 0, texformat.value, w, h, 0, texformat.base, DataType.UBYTE.value, (ByteBuffer) null);
		}
		tex.undobind();
		tracker.add(tex);
		return tex;
	}
	
	public static TextureRectangle get(String name) {
		return tracker.get(name);
	}
	
	protected static TextureRectangle get(int id) {
		return tracker.get(id);
	}
	
	public int target() {
		return GL31.GL_TEXTURE_RECTANGLE;
	}
	
	/**************************************************/
	/********************** Bind **********************/
	/**************************************************/
	
	private static void bind(int tex) {
		curr.update(tex);
		if (tex == curr.last())
			return;
		GL11.glBindTexture(GL31.GL_TEXTURE_RECTANGLE, tex);
	}
	
	public void bind() {
		bind(id);
	}
	
	public void bindNone() {
		bind(0);
	}
	
	protected void undobind() {
		bind(curr.revert());
	}
	
	public void destroy() {
		if (curr.value() == id)
			bindNone();
		GL11.glDeleteTextures(id);
		tracker.remove(this);
	}
	
	/**************************************************/
	
	/**
	 * Sets the texel data in specified rectangle of mipmap level. Texture needs
	 * to be initialized with
	 * {@link #initializeTexture(int, int, int, TextureFormat)}. Rectangle must
	 * be within the bounds of the texture. [GL_TEXTURE_BASE_LEVEL + map].
	 */
	public void setData(int x, int y, int w, int h, ImageFormat format, DataType type, ByteBuffer data) {
		bind();
		GL11.glTexSubImage2D(target(), 0, x, y, w, h, format.value, type.value, data);
		undobind();
	}
	
	/**************************************************/
	/****************** FBOAttachable *****************/
	/**************************************************/
	/**
	 * @param level
	 *            unused.
	 * @param layer
	 *            unused.
	 */
	@Override
	public void attachToFBO(FBOAttachment attachment, int level, int layer) {
		GL30.glFramebufferTexture2D(GL30.GL_DRAW_FRAMEBUFFER, attachment.value, target(), id, 0);
	}
	
	/**************************************************/
	
	@Override
	public void debug() {
		GL.flushErrors();
		Logging.setPad(32);
		
		Logging.writeOut(Logging.fixedString(target + ":") + String.format("%s\t(%d x %d)", name, w, h));
		Logging.indent();
		
		Logging.writeOut(Logging.fixedString("Texture Format:") + texformat);
		
		Logging.writeOut(minFilter);
		Logging.writeOut(magFilter);
		
		boolean tb = lodMin.resolved() && lodMax.resolved() && lodBias.resolved();
		String ts = Logging.fixedString("LOD Range:") + String.format("[%4f, %4f] + %4f", lodMin.value(), lodMax.value(), lodBias.value());
		if (!tb)
			ts += "\tUnresolved:\t" + String.format("[%4f, %4f] + %4f", lodMin.state(), lodMax.state(), lodBias.state());
		Logging.writeOut(ts);
		
		tb = swizzleR.resolved() && swizzleG.resolved() && swizzleB.resolved() && swizzleA.resolved();
		ts = Logging.fixedString("Texture Swizzle:")
				+ String.format("[%s, %s, %s, %s]", swizzleR.value(), swizzleG.value(), swizzleB.value(), swizzleA.value());
		if (!tb)
			ts += "\tUnresolved:\t" + String.format("[%s, %s, %s, %s]", swizzleR.state(), swizzleG.state(), swizzleB.state(), swizzleA.state());
		Logging.writeOut(ts);
		
		Logging.writeOut(border);
		Logging.writeOut(sWrap);
		Logging.writeOut(tWrap);
		
		Logging.unindent();
		
		Logging.unsetPad();
	}
	
}
