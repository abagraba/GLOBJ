package globj.core;


import java.util.ArrayList;
import java.util.List;

import lwjgl.debug.GLDebug;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;



public class GL {
	
	private static boolean			logFPS			= false;
	private static boolean			debug			= true;
	
	private static long				renderTime		= 0;
	private static int				fps				= 60;
	
	private static volatile boolean	stop			= false;
	
	private static Object			displayLock		= new Object();
	private static volatile boolean	display			= false;
	
	private static Object			targetLock		= new Object();
	private static RenderCommand	target;
	private static RenderCommand	current;
	private static long				lastRender		= System.nanoTime();
	private static long				currentRender	= System.nanoTime();
	
	
	private GL() {
	}
	
	public static void setTarget(RenderCommand target) {
		synchronized (targetLock) {
			GL.target = target;
		}
	}
	
	public static void waitForStart() {
		while (!display) {
			try {
				Thread.sleep(17);
			}
			catch (InterruptedException e) {
				GLDebug.logException(e);
			}
		}
	}
	
	public static void startGL() throws LWJGLException {
		startGL(new PixelFormat(), new ContextAttribs());
	}
	
	public static void startGL(final PixelFormat pf, final ContextAttribs attribs) throws LWJGLException {
		new Thread("LWJGL Render Thread") {
			@Override
			public void run() {
				synchronized (displayLock) {
					if (display) {
						GLDebug.glError("LWJGL Display cannot be initiated: Already Running.");
						return;
					}
					try {
						Display.create(pf, attribs);
					}
					catch (LWJGLException e) {
						GLDebug.logException(e);
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
						GLDebug.write(1000000000f / renderTime + " fps");
				}
				synchronized (displayLock) {
					Display.destroy();
					display = false;
					stop = false;
				}
			}
		}.start();
	}
	
	public static void toggleFS() {
		try {
			Display.setFullscreen(!Display.isFullscreen());
		}
		catch (LWJGLException e) {
			GLDebug.logException(e);
		}
	}
	
	public static void flushErrors() {
		int err = GL11.glGetError();
		while (err != GL11.GL_NO_ERROR) {
			if (debug)
				GLDebug.write(GLU.gluErrorString(err));
			err = GL11.glGetError();
		}
	}
	
	public static void flushErrorsV() {
		int err = GL11.glGetError();
		if (err == GL11.GL_NO_ERROR)
			GLDebug.write("No Error");
		while (err != GL11.GL_NO_ERROR) {
			if (debug)
				GLDebug.write(GLU.gluErrorString(err));
			err = GL11.glGetError();
		}
	}
	
	public static List<String> readErrorsToList() {
		List<String> errors = new ArrayList<String>();
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
	
	public static boolean versionCheck(int major, int minor) {
		if (Context.intConst(GL30.GL_MAJOR_VERSION) > major)
			return true;
		if (Context.intConst(GL30.GL_MAJOR_VERSION) < major)
			return false;
		return Context.intConst(GL30.GL_MINOR_VERSION) >= minor;
	}
	
	public static String[] status() {
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
			status.add(GLDebug.logMessage("ERROR:", error, 0));
		status.add(GLDebug.logMessage("OpenGL Context Status:", "", 0));
		status.add(GLDebug.logMessage(String.format("%-16s:\t%s", "Vendor", vendor), 1));
		status.add(GLDebug.logMessage(String.format("%-16s:\t%s", "Renderer", renderer), 1));
		status.add(GLDebug.logMessage(String.format("%-16s:\t%d.%d", "Version", major, minor), 1));
		status.add(GLDebug.logMessage("Supported GLSL Versions", 1));
		for (int i = 0; i < glsls.length; i++)
			status.add(GLDebug.logMessage(String.format("%-16s:\t%s", "", glsls[i]), 1));
		status.add(GLDebug.logMessage("Supported Extensions", 1));
		for (int i = 0; i < exts.length; i++)
			status.add(GLDebug.logMessage(String.format("%-16s:\t%s", "", exts[i]), 1));
		return status.toArray(new String[status.size()]);
	}
}
