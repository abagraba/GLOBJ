package lwjgl.test;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import lwjgl.core.GL;
import lwjgl.core.RenderTarget;

public class TestTarget extends RenderTarget {

	private static float theta = 0;
	private static float rps = (float) (2 * Math.PI) / 6;

	@Override
	public void init() {
	}

	@Override
	public void uninit() {
	}

	@Override
	public void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glBegin(GL11.GL_TRIANGLES);

		int numTri = 100;
		float dt = (float) (Math.PI / numTri);
		
		for (int i = 0; i < numTri; i++){
			GL11.glColor3f(0, 0, 1);
			GL11.glVertex2d(Math.cos(theta + i * 2 * dt), Math.sin(theta + i * 2 * dt));
			GL11.glColor3f(0, 1, 0);
			GL11.glVertex2d(Math.cos(theta + i * 2 * dt + dt), Math.sin(theta + i * 2 * dt + dt));
			GL11.glColor3f(1, 0, 0);
//			GL11.glVertex2d(0, 0);
			GL11.glVertex2d(0.1f * Math.cos(theta + i * 2 * dt + dt * 0.5f), 0.1f * Math.sin(theta + i * 2 * dt + dt * 0.5f));
//			GL11.glVertex2d(Math.cos(theta + i * 2 * dt + 2 * dt), Math.sin(theta + i * 2 * dt + 2 * dt));
		}

		GL11.glEnd();
	}

	public static void main(String[] args) {
			GL.setTarget(new TestTarget());
		try {
			GL.startGL();
		} catch (LWJGLException e) {
			e.printStackTrace();
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
			}
		}
		if (l)
			theta += rps * 0.001f * GL.deltaTime();
		if (r)
			theta -= rps * 0.001f * GL.deltaTime();
	}

}
