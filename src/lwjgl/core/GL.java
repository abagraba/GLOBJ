package lwjgl.core;

import java.util.ArrayList;
import java.util.List;

import lwjgl.debug.Logging;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.opengl.XRandR.Screen;
import org.lwjgl.util.glu.GLU;

public class GL {
	
	public volatile static boolean logFPS = false;
	public volatile static boolean debug = true;
	
	public volatile static long renderTime = 0;
	public volatile static int fps = 60;
	
	private volatile static boolean stop = false;
	
	private static Object displayLock = new Object();
	private volatile static boolean display = false;
	
	private static Object targetLock = new Object();
	private static RenderCommand target;
	private static RenderCommand current;
	private static long lastRender = System.nanoTime();
	private static long currentRender = System.nanoTime();
	
	public static void setTarget(RenderCommand target) {
		synchronized (targetLock) {
			GL.target = target;
		}
	}
	
	public static void waitForStart() {
		while (!display)
			;
	}
	
	public static void startGL() throws LWJGLException {
		startGL(new PixelFormat(), new ContextAttribs());
	}
	
	public static void startGL(final PixelFormat pf, final ContextAttribs attribs) throws LWJGLException {
		new Thread("LWJGL Render Thread") {
			public void run() {
				synchronized (displayLock) {
					if (display) {
						System.err.println("LWJGL Display cannot be initiated: Already Running.");
						return;
					}
					try {
						Display.create(pf, attribs);
					} catch (LWJGLException e) {
						e.printStackTrace();
					}
					display = true;
				}
				while (!Display.isCloseRequested() && !stop) {
					lastRender = currentRender;
					currentRender = System.nanoTime();
					Display.sync(fps);
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
					current = next;
					current.input();
					current.render();
					flushErrors();
					Display.update();
					renderTime = System.nanoTime() - currentRender;
					if (logFPS)
						System.out.println(1000000000f / renderTime + " fps");
				}
				synchronized (displayLock) {
					Display.destroy();
					display = false;
					stop = false;
				}
			}
		}.start();
	}
	
	public static void toggleFS(){
		try {
			Display.setFullscreen(!Display.isFullscreen());
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public static void flushErrors() {
		int err = GL11.glGetError();
		while (err != GL11.GL_NO_ERROR) {
			if (debug)
				System.err.println(GLU.gluErrorString(err));
			err = GL11.glGetError();
		}
	}
	public static void flushErrorsOut() {
		int err = GL11.glGetError();
		while (err != GL11.GL_NO_ERROR) {
			if (debug)
				System.out.println(GLU.gluErrorString(err));
			err = GL11.glGetError();
		}
	}
	
	public static String readErrors() {
		int err = GL11.glGetError();
		if (err == GL11.GL_NO_ERROR)
			return null;
		String s = "";
		while (err != GL11.GL_NO_ERROR) {
			s += GLU.gluErrorString(err) + '\n';
			err = GL11.glGetError();
		}
		return s;
	}
	
	public static List<String> readErrorsToList() {
		ArrayList<String> errors = new ArrayList<String>();
		int err = GL11.glGetError();
		if (err == GL11.GL_NO_ERROR)
			return errors;
		while (err != GL11.GL_NO_ERROR) {
			errors.add(GLU.gluErrorString(err));
			err = GL11.glGetError();
		}
		return errors;
	}
	
	public static int nextError() {
		return GL11.glGetError();
	}
	
	public static float deltaTime() {
		return (currentRender - lastRender) * 0.000001f;
	}
	
	public static void close() {
		stop = true;
	}
	
	public static boolean versionCheck(int major, int minor){
		if (Context.intConst(GL30.GL_MAJOR_VERSION) > major)
			return true;
		if (Context.intConst(GL30.GL_MAJOR_VERSION) < major)
			return false;
		return Context.intConst(GL30.GL_MINOR_VERSION) >= minor;
	}
	
	public static String[] status(){
		GL.flushErrors();
		String vendor = GL11.glGetString(GL11.GL_VENDOR);
		String renderer = GL11.glGetString(GL11.GL_RENDERER);
		int major = Context.intConst(GL30.GL_MAJOR_VERSION);
		int minor = Context.intConst(GL30.GL_MINOR_VERSION);
		int glsl = Context.intConst(GL43.GL_NUM_SHADING_LANGUAGE_VERSIONS);
		int extnum = Context.intConst(GL30.GL_NUM_EXTENSIONS);
		String[] glsls = new String[glsl];
		for (int i = 0; i < glsl; i++)
			glsls[i] = GL30.glGetStringi(GL20.GL_SHADING_LANGUAGE_VERSION, i);
		String[] exts = new String[extnum];
		for (int i = 0; i < extnum; i++)
			exts[i] = GL30.glGetStringi(GL11.GL_EXTENSIONS, i);
		List<String> status = new ArrayList<String>();
		List<String> errors = GL.readErrorsToList();
		for (String error : errors)
			status.add(Logging.logText("ERROR:", error, 0));
		status.add(Logging.logText("OpenGL Context Status:", "", 0));
		status.add(Logging.logText(String.format("%-16s:\t%s", "Vendor", vendor), 1));
		status.add(Logging.logText(String.format("%-16s:\t%s", "Renderer", renderer), 1));
		status.add(Logging.logText(String.format("%-16s:\t%d.%d", "Version", major, minor), 1));
		status.add(Logging.logText("Supported GLSL Versions", 1));
		for (int i = 0; i < glsls.length; i++)
			status.add(Logging.logText(String.format("%-16s:\t%s", "", glsls[i]), 1));
		status.add(Logging.logText("Supported Extensions", 1));
		for (int i = 0; i < exts.length; i++)
			status.add(Logging.logText(String.format("%-16s:\t%s", "", exts[i]), 1));
		return status.toArray(new String[status.size()]);
	}
}
