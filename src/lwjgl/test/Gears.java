/*
 * Copyright LWJGL. All rights reserved. License terms: http://lwjgl.org/license.php
 */
package lwjgl.test;


import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_G;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_DEBUG_CONTEXT;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetInputMode;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowMonitor;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.system.libffi.Closure;



/** The Gears demo implemented using GLFW. */
public class Gears extends AbstractGears {
	
	private GLFWErrorCallback	errorfun;
	private GLFWKeyCallback		keyfun;
	
	private Closure debugProc;
	
	private long window;
	
	private Boolean toggleMode;
	
	public static void main(String[] args) {
		new Gears().execute();
	}
	
	@Override
	protected void init() {
		glfwSetErrorCallback(errorfun = errorCallbackPrint(System.err));
		if (glfwInit() != GL11.GL_TRUE)
			throw new IllegalStateException("Unable to initialize glfw");
			
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GL_TRUE);
		
		keyfun = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (action != GLFW_RELEASE)
					return;
					
				switch (key) {
					case GLFW_KEY_ESCAPE:
						glfwSetWindowShouldClose(window, GL_TRUE);
						break;
					case GLFW_KEY_F:
						if (glfwGetWindowMonitor(window) == NULL)
							toggleMode = true;
						break;
					case GLFW_KEY_W:
						if (glfwGetWindowMonitor(window) != NULL)
							toggleMode = false;
						break;
					case GLFW_KEY_G:
						glfwSetInputMode(	window, GLFW_CURSOR,
											glfwGetInputMode(window, GLFW_CURSOR) == GLFW_CURSOR_NORMAL ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
						break;
				}
			}
		};
		
		createWindow(false);
	}
	
	private void createWindow(boolean fullscreen) {
		int WIDTH = 300;
		int HEIGHT = 300;
		
		long monitor = glfwGetPrimaryMonitor();
		GLFWvidmode vidmode = new GLFWvidmode(glfwGetVideoMode(monitor));
		
		long window = fullscreen	? glfwCreateWindow(vidmode.getWidth(), vidmode.getHeight(), "GLFW Gears Demo", monitor, this.window)
									: glfwCreateWindow(WIDTH, HEIGHT, "GLFW Gears Demo", NULL, this.window);
									
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");
			
		// Destroy old window
		if (this.window != NULL) {
			glfwSetInputMode(this.window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
			glfwDestroyWindow(this.window);
			if (debugProc != null)
				debugProc.release();
		}
		
		glfwSetKeyCallback(window, keyfun);
		
		if (fullscreen)
			glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		else {
			// Center window
			glfwSetWindowPos(window, (vidmode.getWidth() - WIDTH) / 2, (vidmode.getHeight() - HEIGHT) / 2);
		}
		
		glfwMakeContextCurrent(window);
		
		GLContext.createFromCurrent();
		GL.createCapabilities(false);
		// debugProc = GLUtil.setupDebugMessageCallback();
		
		glfwSwapInterval(1);
		glfwShowWindow(window);
		
		this.window = window;
	}
	
	@Override
	protected void loop() {
		long lastUpdate = System.currentTimeMillis();
		int frames = 0;
		
		while (glfwWindowShouldClose(window) == GL_FALSE) {
			glfwPollEvents();
			
			if (toggleMode != null) {
				// Toggle between windowed and fullscreen modes
				createWindow(toggleMode);
				initGLState();
				toggleMode = null;
			}
			
			renderLoop();
			
			glfwSwapBuffers(window);
			
			frames++;
			
			long time = System.currentTimeMillis();
			int UPDATE_EVERY = 5; // seconds
			if (UPDATE_EVERY * 1000L <= time - lastUpdate) {
				lastUpdate = time;
				
				System.out.printf("%d frames in %d seconds = %.2f fps\n", frames, UPDATE_EVERY, (frames / (float) UPDATE_EVERY));
				frames = 0;
			}
		}
		
		glfwDestroyWindow(window);
	}
	
	@Override
	protected void destroy() {
		if (debugProc != null)
			debugProc.release();
		keyfun.release();
		glfwTerminate();
		errorfun.release();
	}
	
}