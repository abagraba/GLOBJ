package lwjgl.core;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class FBO implements GLObject {

	private static final HashMap<String, FBO> fbos = new HashMap<String, FBO>();

	private final int fbo;
	private final String name;

	public FBO(String name) {
		if (fbos.containsKey(name))
			invalidError("Cannot create Framebuffer Object. Framebuffer Object ["
					+ name + "] already exists.");
		this.name = name;
		fbo = GL30.glGenFramebuffers();
	}

	public void destroy() {
		invalidWarning("Framebuffer Object [" + name
				+ "] has already been deleted.");
		GL30.glDeleteFramebuffers(fbo);
	}

	private void invalidWarning(String error) {
		if (fbo == 0)
			System.err.println("WARNING:    " + error);
	}

	private void invalidError(String error) {
		if (fbo == 0) {
			try {
				throw new GLException(error, this);
			} catch (GLException e) {
				System.err.println("ERROR:      " + error);
				e.printStackTrace();
			}
		}
	}

	@Override
	public String status() {
		return null;
	}
}
