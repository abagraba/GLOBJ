package lwjgl.test.set3;


import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import globj.core.RenderCommand;
import globj.core.Window;
import globj.objects.bufferobjects.DynamicFloatVBO;
import globj.objects.bufferobjects.values.VBOTarget;



/**
 * Test Target to test functionality of Vertex Buffer Objects. <br/>
 * <br/>
 * For more information, visit
 * <a href="http://www.ozone3d.net/tutorials/opengl_vbo.php" >http://www.ozone3d.net/tutorials/opengl_vbo.php</a>
 *
 */
public class InterleavedVBOTestTarget extends RenderCommand {
	
	private static float	theta	= 0;
	private static float	rps		= (float) (2 * Math.PI) / 6;
	
	private DynamicFloatVBO vertices, colors;
	
	@Override
	public void init() {
		int numTri = 100;
		float dt = (float) (Math.PI / numTri);
		
		FloatBuffer v = BufferUtils.createFloatBuffer(6 * numTri);
		FloatBuffer c = BufferUtils.createFloatBuffer(9 * numTri);
		
		for (int i = 0; i < numTri; i++) {
			v.put(new float[] {	(float) Math.cos(theta + i * 2 * dt), (float) Math.sin(theta + i * 2 * dt), (float) Math.cos(theta + i * 2 * dt + dt),
								(float) Math.sin(theta + i * 2 * dt + dt), (float) (0.1f * Math.cos(theta + i * 2 * dt + dt * 0.5f)),
								(float) (0.1f * Math.sin(theta + i * 2 * dt + dt * 0.5f)) });
			c.put(new float[] { 0, 0, 1, 0, 1, 0, 1, 0, 0 });
		}
		v.flip();
		vertices = DynamicFloatVBO.create("Vertices", VBOTarget.ARRAY);
		vertices.write(v);
		GL11.glVertexPointer(2, GL11.GL_FLOAT, 0, 0);
		
		c.flip();
		colors = DynamicFloatVBO.create("Colors", VBOTarget.ARRAY);
		colors.write(c);
		GL11.glColorPointer(3, GL11.GL_FLOAT, 0, 0);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		float aspect = 2 * (float) Display.getWidth() / Display.getHeight();
		GL11.glOrtho(-aspect, aspect, -2, 2, -1, 1);
		
	}
	
	@Override
	public void uninit() {
		vertices.destroy();
		colors.destroy();
	}
	
	@Override
	public void render() {
		
		int numTri = 100;
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		
		GL11.glRotatef(theta, 0, 0, 1);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, numTri * 3);
		
		GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
	}
	
	public static void main(String[] args) {
		Window w = new Window();
		w.setTarget(new InterleavedVBOTestTarget());
		w.start();
		
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
					Window.close();
					break;
			}
		}
		
		if (l)
			theta += rps * 0.001f * Window.deltaTime();
		if (r)
			theta -= rps * 0.001f * Window.deltaTime();
	}
	
}
