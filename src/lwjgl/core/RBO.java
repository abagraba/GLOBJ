package lwjgl.core;

import java.util.HashMap;

import lwjgl.debug.Logging;

import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class RBO extends GLObject {

	protected static final HashMap<String, RBO> rboname = new HashMap<String, RBO>();
	protected static final HashMap<Integer, RBO> rboid = new HashMap<Integer, RBO>();
	protected static int currentRBO = 0;
	private static int lastRBO = 0;

	protected final int rbo;

	private RBO(String name) {
		super(name);
		rbo = GL30.glGenRenderbuffers();
	}

	public static RBO create(String name, int w, int h, int format) {
		if (rboname.containsKey(name)) {
			Logging.glError(
					"Cannot create Renderbuffer Object. Renderbuffer Object ["
							+ name + "] already exists.", null);
			return null;
		}
		RBO rbo = new RBO(name);
		if (rbo.rbo == 0) {
			Logging.glError(
					"Cannot create Renderbuffer Object. Renderbuffer Object ["
							+ name + "] already exists.", null);
			return null;
		}

		GL.flushErrors();
		bind(rbo.rbo);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, format, w, h);
		bind(lastRBO);
		if (checkError())
			return null;

		rboname.put(rbo.name, rbo);
		rboid.put(rbo.rbo, rbo);
		return rbo;
	}

	private static boolean checkError() {
		int err = GL.nextError();
		while (err != GL11.GL_NO_ERROR) {
			switch (err) {
			case GL11.GL_INVALID_ENUM:
				Logging.glError(
						"Cannot create Renderbuffer Object. Invalid format.",
						null);
				return true;
			case GL11.GL_INVALID_VALUE:
				Logging.glError(
						"Cannot create Renderbuffer Object. Renderbuffer Object too large. Max Size is "
								+ ContextValues
										.intConst(GL30.GL_MAX_RENDERBUFFER_SIZE)
								+ " pixels.", null);
				return true;
			case GL11.GL_OUT_OF_MEMORY:
				Logging.glError(
						"Cannot create Renderbuffer Object. Out of Memory.",
						null);
				return true;
			}
			err = GL.nextError();
		}
		return false;
	}

	public static void destroy(String name) {
		if (!rboname.containsKey(name)) {
			Logging.glWarning("Cannot delete Renderbuffer Object. Renderbuffer Object ["
					+ name + "] does not exist.");
			return;
		}
		RBO rbo = rboname.get(name);
		if (currentRBO == rbo.rbo)
			bind(0);
		GL30.glDeleteRenderbuffers(rbo.rbo);
		rboname.remove(rbo.name);
		rboid.remove(rbo.rbo);
	}

	public static RBO get(String name) {
		return rboname.get(name);
	}

	protected static void bind(int rbo) {
		if (rbo == currentRBO)
			return;
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, rbo);
		lastRBO = currentRBO;
		currentRBO = rbo;
	}

	public static void bind(String name) {
		get(name).bind();
	}

	public void bind() {
		bind(rbo);
	}

	@Override
	public String status() {
		if (rbo == 0)
			return "-\tRenderbuffer does not exist.";
		GL.flushErrors();
		bind();
		int w = ARBFramebufferObject.glGetRenderbufferParameteri(
				GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_WIDTH);
		int h = ARBFramebufferObject.glGetRenderbufferParameteri(
				GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_HEIGHT);
		int r = ARBFramebufferObject.glGetRenderbufferParameteri(
				GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_RED_SIZE);
		int g = ARBFramebufferObject.glGetRenderbufferParameteri(
				GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_GREEN_SIZE);
		int b = ARBFramebufferObject.glGetRenderbufferParameteri(
				GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_BLUE_SIZE);
		int a = ARBFramebufferObject.glGetRenderbufferParameteri(
				GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_ALPHA_SIZE);
		int d = ARBFramebufferObject.glGetRenderbufferParameteri(
				GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_DEPTH_SIZE);
		int s = ARBFramebufferObject.glGetRenderbufferParameteri(
				GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_STENCIL_SIZE);
		int n = ARBFramebufferObject.glGetRenderbufferParameteri(
				GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_SAMPLES);
		int f = ARBFramebufferObject.glGetRenderbufferParameteri(
				GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_INTERNAL_FORMAT);
		bind(lastRBO);
		String err = GL.readErrors();
		String out = "-\tRenderbuffer:\t" + name + "\n-\t" + w + "x" + h
				+ " Renderbuffer Object. " + n + " samples.\n-"
				+ "\tRGBA:       [" + r + "," + g + "," + b + "," + a
				+ "]\n-\tDepth:      " + d + "\n-\tStencil:    " + s
				+ "\n-\tFormat:     " + f;
		if (err == null)
			return out;
		return "ERRORS:\n" + err + out;
	}
}
