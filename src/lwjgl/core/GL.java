package lwjgl.core;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

public class GL {

	public volatile static boolean logFPS = false;

	public volatile static long renderTime = 0;
	public volatile static int fps = 60;

	private volatile static boolean stop = false;

	private static Object displayLock = new Object();
	private volatile static boolean display = false;

	private static Object targetLock = new Object();
	private static RenderTarget target;
	private static RenderTarget current;
	private static long lastRender = System.nanoTime();
	private static long currentRender = System.nanoTime();

	public static void setTarget(RenderTarget target) {
		synchronized (targetLock) {
			GL.target = target;
		}
	}

	public static void startGL() throws LWJGLException {
		startGL(new PixelFormat(), new ContextAttribs());
	}

	public static void startGL(final PixelFormat pf,
			final ContextAttribs attribs) throws LWJGLException {
		new Thread("LWJGL Render Thread") {
			public void run() {
				synchronized (displayLock) {
					if (display) {
						System.err
								.println("LWJGL Display cannot be initiated: Already Running.");
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
					RenderTarget next;
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
					int err = GL11.glGetError();
					while (err != GL11.GL_NO_ERROR) {
						System.err.println(GLU.gluErrorString(err));
						err = GL11.glGetError();
					}
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
	
	public static float deltaTime(){
		return (currentRender - lastRender) * 0.000001f;
	}

	public static void close() {
		stop = true;
	}

}
