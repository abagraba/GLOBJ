package globj.core;


import lwjgl.debug.GLDebug;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.PixelFormat;
import control.Control;
import control.ControlManager;



public class GL {
	
	private static boolean			logFPS			= false;
	
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
				Thread.sleep(16);
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
				initDisplay(pf, attribs);
				while (!Display.isCloseRequested() && !stop) {
					lastRender = currentRender;
					currentRender = System.nanoTime();
					renderLoop();
					renderTime = System.nanoTime() - currentRender;
					if (logFPS)
						GLDebug.write(1000000000f / renderTime + " fps");
				}
				destroyDisplay();
			}
		}.start();
	}
	
	private static void initDisplay(final PixelFormat pf, final ContextAttribs attribs) {
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
	}
	
	protected static void destroyDisplay() {
		synchronized (displayLock) {
			Display.destroy();
			display = false;
			stop = false;
		}
	}
	
	private static void renderLoop() {
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
		if (ControlManager.current() != null)
			for (Control c : ControlManager.current().controls)
				c.update();
		current = next;
		current.input();
		current.render();
		GLDebug.flushErrors();
		Display.update();
	}
	
	public static void toggleFS() {
		try {
			Display.setFullscreen(!Display.isFullscreen());
		}
		catch (LWJGLException e) {
			GLDebug.logException(e);
		}
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
}
