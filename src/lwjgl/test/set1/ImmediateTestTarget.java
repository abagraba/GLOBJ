package lwjgl.test.set1;


import org.lwjgl.opengl.GL11;

import control.ControlManager;
import globj.core.RenderCommand;
import globj.core.Window;
import lwjgl.test.misc.TestControlSet;



public class ImmediateTestTarget extends RenderCommand {
	
	private static float	phi		= 0;
	private static float	theta	= 0;
	private static float	rps		= (float) (2 * Math.PI) / 6;
	
	private static Window w;
	
	@Override
	public void init() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		float aspect = 2 * w.aspectRatio();
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
		w = new Window();
		w.setTarget(new ImmediateTestTarget());
		w.start();
		ControlManager.attach(w, new TestControlSet());
	}
	
	@Override
	public void input() {
		
		theta += rps * TestControlSet.LR.position() * w.deltaTime();
		if (TestControlSet.ESC.state())
			Window.close();
	}
	
}
