package globj.core;


import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.system.MemoryUtil;

import control.Control;
import control.ControlManager;
import lwjgl.debug.GLDebug;



public class Window {
	
	private static boolean logFPS = false;
	
	private static long	renderTime	= 0;
	private static int	fps			= 60;
	
	private static volatile boolean stop = false;
	
	private static Object			displayLock	= new Object();
	private static volatile boolean	display		= false;
	
	private static Object			targetLock		= new Object();
	private static RenderCommand	current;
	private static long				lastRender		= System.nanoTime();
	private static long				currentRender	= System.nanoTime();
	
	private GLFWErrorCallback	errorCallback;
	private GLFWKeyCallback		keyCallback;
	
	private long	window;
	private long	monitor;
	
	private String	title;
	private int		width;
	private int		height;
	private boolean	decorated;
	
	private RenderCommand target;
	
	public Window() {
		this("LWJGL Window", 800, 600);
	}
	
	public Window(String title, int width, int height) {
		this(title, width, height, true);
	}
	
	public Window(String title, int width, int height, boolean decorated) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.decorated = decorated;
	}
	
	private void construct() {
		GLFW.glfwSetErrorCallback(errorCallback = Callbacks.errorCallbackPrint(System.err));
		if (GLFW.glfwInit() != GL11.GL_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");
		GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, decorated ? GL11.GL_TRUE : GL11.GL_FALSE);
		window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
		if (window == MemoryUtil.NULL)
			throw new RuntimeException("Failed to create the GLFW window");
			
		GLFW.glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
					GLFW.glfwSetWindowShouldClose(window, GL11.GL_TRUE); // We will detect this in our rendering loop
			}
		});
		monitor = GLFW.glfwGetPrimaryMonitor();
		ByteBuffer vidmode = GLFW.glfwGetVideoMode(monitor);
		GLFW.glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - width) / 2, (GLFWvidmode.height(vidmode) - height) / 2);
	}
	
	public float aspectRatio() {
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowSize(window, w, h);
		int height = h.get();
		return height == 0 ? (w.get() == 0 ? 0 : Float.MAX_VALUE) : (float) w.get() / height;
	}
	
	public void setTarget(RenderCommand target) {
		synchronized (targetLock) {
			this.target = target;
		}
	}
	
	public void start() {
		
		new Thread("LWJGL Render Thread") {
			@Override
			public void run() {
				construct();
				GLFW.glfwMakeContextCurrent(window);
				GLContext.createFromCurrent();
				GL.createCapabilities(false);
				
				GLFW.glfwSwapInterval(1);
				GLFW.glfwShowWindow(window);
				
				while (GLFW.glfwWindowShouldClose(window) == GL11.GL_FALSE) {
					lastRender = currentRender;
					currentRender = System.nanoTime();
					renderLoop();
					renderTime = System.nanoTime() - currentRender;
					if (logFPS)
						GLDebug.write(1000000000f / renderTime + " fps");
					GLFW.glfwSwapBuffers(window);
					GLFW.glfwPollEvents();
				}
				GLFW.glfwDestroyWindow(window);
				keyCallback.release();
			}
		}.start();
	}
	
	private void renderLoop() {
		// Display.sync(fps);
		RenderCommand next;
		synchronized (targetLock) {
			next = target;
		}
		if (current != next) {
			if (current != null)
				current.uninit();
			if (next != null)
				next.init();
		}
		if (ControlManager.current() != null)
			for (Control c : ControlManager.current().controls())
				c.update();
		current = next;
		current.input();
		current.render();
		GLDebug.flushErrors();
	}
	
	public static void toggleFS() {
		// Display.setFullscreen(!Display.isFullscreen());
	}
	
	// TODO ThreadLocal?
	public static float deltaTime() {
		return (currentRender - lastRender) * 0.000001f;
	}
	
	public static void close() {
		stop = true;
	}
	
	public static boolean versionCheck(int major, int minor) {
		if (Context.intConst(GL30.GL_MAJOR_VERSION) > major)
			return true;
		if (Context.intConst(GL30.GL_MAJOR_VERSION) < major)
			return false;
		return Context.intConst(GL30.GL_MINOR_VERSION) >= minor;
	}
	
	public static void debugQuery() {
		
		GLDebug.flushErrors();
		GLDebug.write("GL Context Information:");
		GLDebug.indent();
		GLDebug.writef(GLDebug.ATTRIB_STRING, "Vendor", GL11.glGetString(GL11.GL_VENDOR));
		GLDebug.writef(GLDebug.ATTRIB_STRING, "Renderer", GL11.glGetString(GL11.GL_RENDERER));
		GLDebug.writef(GLDebug.ATTRIB + "%d.%d", "Version", Context.intConst(GL30.GL_MAJOR_VERSION), Context.intConst(GL30.GL_MINOR_VERSION));
		GLDebug.writef(GLDebug.ATTRIB, "Supported GLSL Versions");
		GLDebug.indent();
		int glsl = Context.intConst(GL43.GL_NUM_SHADING_LANGUAGE_VERSIONS);
		String[] glsls = new String[glsl];
		for (int i = 0; i < glsl; i++)
			glsls[i] = GL30.glGetStringi(GL20.GL_SHADING_LANGUAGE_VERSION, i);
		for (int i = 0; i < glsls.length; i++)
			GLDebug.write(glsls[i]);
		GLDebug.unindent();
		GLDebug.writef(GLDebug.ATTRIB, "Supported Extensions");
		GLDebug.indent();
		int ext = Context.intConst(GL30.GL_NUM_EXTENSIONS);
		String[] exts = new String[ext];
		for (int i = 0; i < ext; i++)
			exts[i] = GL30.glGetStringi(GL11.GL_EXTENSIONS, i);
		for (int i = 0; i < exts.length; i++)
			GLDebug.write(exts[i]);
		GLDebug.unindent(2);
		GLDebug.flushErrors();
	}
	
	public long window() {
		return window;
	}
	
}
