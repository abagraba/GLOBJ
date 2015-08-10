package lwjgl.test.set1;


import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import globj.core.GL;
import globj.core.RenderCommand;
import lwjgl.debug.GLDebug;



public class ImmediateTestTarget extends RenderCommand {
	
	private static float	phi		= 0;
	private static float	theta	= 0;
	private static float	rps		= (float) (2 * Math.PI) / 6;
	
	@Override
	public void init() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		float aspect = 2 * (float) Display.getWidth() / Display.getHeight();
		GL11.glOrtho(-aspect, aspect, -2, 2, -1, 1);
	}
	
	@Override
	public void uninit() {
		//
	}
	
	@Override
	public void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glBegin(GL11.GL_TRIANGLES);
		
		int numTri = 100;
		float dt = (float) (Math.PI / numTri);
		phi += theta * Math.PI / 180;
		for (int i = 0; i < numTri; i++) {
			GL11.glColor3f(0, 0, 1);
			GL11.glVertex2d(Math.cos(phi + i * 2 * dt), Math.sin(phi + i * 2 * dt));
			GL11.glColor3f(0, 1, 0);
			GL11.glVertex2d(Math.cos(phi + i * 2 * dt + dt), Math.sin(phi + i * 2 * dt + dt));
			GL11.glColor3f(1, 0, 0);
			GL11.glVertex2d(0.1f * Math.cos(phi + i * 2 * dt + dt * 0.5f), 0.1f * Math.sin(phi + i * 2 * dt + dt * 0.5f));
		}
		
		GL11.glEnd();
	}
	
	public static void main(String[] args) {
		GL.setTarget(new ImmediateTestTarget());
		try {
			GL.startGL();
		}
		catch (LWJGLException e) {
			GLDebug.logException(e);
		}
	}
	
	private boolean l, r;
	
	@Override
	public void input() {
		while (Keyboard.next()) {
			switch (Keyboard.getEventKey()) {
				case Keyboard.KEY_LEFT:
					l = Keyboard.getEventKeyState();
					break;
				case Keyboard.KEY_RIGHT:
					r = Keyboard.getEventKeyState();
					break;
				case Keyboard.KEY_ESCAPE:
					GL.close();
					break;
			}
		}
		
		if (l)
			theta += rps * 0.001f * GL.deltaTime();
		if (r)
			theta -= rps * 0.001f * GL.deltaTime();
	}
	
}
