package lwjgl.test.set2;


import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import control.ControlManager;
import globj.core.RenderCommand;
import globj.core.Window;
import globj.objects.bufferobjects.StaticVBO;
import globj.objects.bufferobjects.VBO;
import globj.objects.bufferobjects.values.VBOTarget;
import lwjgl.test.misc.TestControlSet;



/**
 * Test Target to test functionality of Vertex Buffer Objects. <br/>
 * <br/>
 * For more information, visit
 * <a href="http://www.ozone3d.net/tutorials/opengl_vbo.php" >http://www.ozone3d.net/tutorials/opengl_vbo.php</a>
 *
 */
public class VertexArrayVBOTestTarget extends RenderCommand {
	
	private static float	theta	= 0;
	private static float	rps		= (float) (2 * Math.PI) / 6;
	
	private VBO vertices;
	
	private int				numTri	= 100;
	private static Window	w;
	
	@Override
	public void init() {
		float dt = (float) (Math.PI / numTri);
		
		FloatBuffer v = BufferUtils.createFloatBuffer(15 * numTri);
		
		for (int i = 0; i < numTri; i++) {
			v.put(new float[] {	(float) Math.cos(theta + i * 2 * dt), (float) Math.sin(theta + i * 2 * dt), 0, 0, 1, (float) Math.cos(theta + i * 2 * dt + dt),
								(float) Math.sin(theta + i * 2 * dt + dt), 0, 1, 0, (float) (0.1f * Math.cos(theta + i * 2 * dt + dt * 0.5f)),
								(float) (0.1f * Math.sin(theta + i * 2 * dt + dt * 0.5f)), 1, 0, 0 });
		}
		v.flip();
		vertices = StaticVBO.create("Vertex Data", VBOTarget.ARRAY, v);
		vertices.bind();
		GL11.glVertexPointer(2, GL11.GL_FLOAT, 5 * 4, 0 * 4);
		GL11.glColorPointer(3, GL11.GL_FLOAT, 5 * 4, 2 * 4);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		float aspect = 2 * w.aspectRatio();
		GL11.glOrtho(-aspect, aspect, -2, 2, -1, 1);
		
	}
	
	@Override
	public void uninit() {
		vertices.destroy();
	}
	
	@Override
	public void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		
		GL11.glRotatef(theta, 0, 0, 1);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, numTri * 3);
		
		GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
	}
	
	public static void main(String[] args) {
		w = new Window();
		w.setTarget(new VertexArrayVBOTestTarget());
		w.start();
		
		ControlManager.attach(w, new TestControlSet());
	}
	
	@Override
	public void input() {
		theta += rps * TestControlSet.LR.position() * Window.deltaTime();
		if (TestControlSet.ESC.state())
			Window.close();
	}
	
}
