package globj.core;


import java.nio.ByteBuffer;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.system.MemoryUtil;



public final class WindowRenderer implements Runnable {
	
	private Window window;
	
	protected WindowRenderer(Window window) {
		this.window = window;
	}
	
	@Override
	public void run() {
		constructWindow();
		
		GLFW.glfwMakeContextCurrent(window.window);
		GLContext.createFromCurrent();
		GL.createCapabilities(false);
		
		GLFW.glfwSwapInterval(1);
		GLFW.glfwShowWindow(window.window);
		
		while (GLFW.glfwWindowShouldClose(window.window) == GL11.GL_FALSE) {
			window.render();
			GLFW.glfwSwapBuffers(window.window);
			GLFW.glfwPollEvents();
		}
		GLFW.glfwDestroyWindow(window.window);
	}
	
	private void constructWindow() {
		Window.currentWindow.set(window);
		GLFW.glfwSetErrorCallback(Callbacks.errorCallbackPrint(System.err));
		if (GLFW.glfwInit() != GL11.GL_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");
		GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, window.decorated ? GL11.GL_TRUE : GL11.GL_FALSE);
		window.window = GLFW.glfwCreateWindow(window.width, window.height, window.title, MemoryUtil.NULL, MemoryUtil.NULL);
		if (window.window == MemoryUtil.NULL)
			throw new OpenGLException("Failed to create the GLFW window.window");
		window.monitor = GLFW.glfwGetPrimaryMonitor();
		ByteBuffer vidmode = GLFW.glfwGetVideoMode(window.monitor);
		GLFW.glfwSetWindowPos(window.window, (GLFWvidmode.width(vidmode) - window.width) / 2, (GLFWvidmode.height(vidmode) - window.height) / 2);
	}
	
}
