package globj.core;


import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;

import control.Control;
import control.ControlManager;
import lwjgl.debug.GLDebug;



public class Window {
	
	private static boolean logFPS = false;
	
	private static Object			targetLock	= new Object();
	private static RenderCommand	current;
	
	protected static final ThreadLocal<Window>	currentWindow	= new ThreadLocal<Window>();
	protected long								window;
	protected long								monitor;
	
	private long	lastRender		= System.nanoTime();
	private long	currentRender	= System.nanoTime();
	
	protected String	title;
	protected int		width;
	protected int		height;
	protected boolean	decorated;
	
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
	
	public float aspectRatio() {
		IntBuffer wBuff = BufferUtils.createIntBuffer(1);
		IntBuffer hBuff = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowSize(window, wBuff, hBuff);
		int h = hBuff.get();
		return h == 0 ? wBuff.get() == 0 ? 0 : Float.MAX_VALUE : (float) wBuff.get() / h;
	}
	
	public void setTarget(RenderCommand target) {
		synchronized (targetLock) {
			this.target = target;
		}
	}
	
	public void start() {
		new Thread(new WindowRenderer(this), "LWJGL Render Thread [" + title + "]").start();
	}
	
	private void renderLoop() {
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
		// Display.setFullscreen(!Display.isFullscreen())
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
	
	public void render() {
		renderLoop();
		currentWindow().lastRender = currentWindow().currentRender;
		currentWindow().currentRender = System.nanoTime();
		if (logFPS)
			GLDebug.write(1000000000f / (currentWindow().currentRender - currentWindow().lastRender) + " fps");
	}
	
	public static Window currentWindow() {
		return currentWindow.get();
	}
	
	public static float deltaTime() {
		return (currentWindow().currentRender - currentWindow().lastRender) * 0.000001f;
	}
	
	public static void close() {
		GLFW.glfwSetWindowShouldClose(currentWindow().window, GL11.GL_TRUE);
	}
	
}
