package lwjgl.test.misc;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import lwjgl.core.GL;
import lwjgl.core.RenderTarget;
import lwjgl.core.VBO;
import lwjgl.core.VBOTarget;
import lwjgl.core.texture.Texture2D;
import lwjgl.core.texture.values.MagnifyFilter;
import lwjgl.core.texture.values.MinifyFilter;
import lwjgl.debug.Logging;
import lwjgl.debug.Timer;

/**
 * Test Target to test functionality of Vertex Buffer Objects. <br/>
 * <br/>
 * For more information, visit <a
 * href="http://www.ozone3d.net/tutorials/opengl_vbo.php"
 * >http://www.ozone3d.net/tutorials/opengl_vbo.php</a>
 *
 */
public class TextureTestTarget extends RenderTarget {
	
	private static float theta = 0;
	private static float phi = 0;
	
	private static float rps = (float) (2 * Math.PI) / 6;
	
	private VBO vertices, tcoords;
	
	@Override
	public void init() {
		
		FloatBuffer v = BufferUtils.createFloatBuffer(12);
		FloatBuffer t = BufferUtils.createFloatBuffer(12);

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		try {
			Texture2D tex = Texture2D.create("Test");
			tex.setFilter(MinifyFilter.NEAREST, MagnifyFilter.NEAREST);
			Timer.debug.mark();
			tex.setDataRGBA(ImageIO.read(new File("src/lwjgl/test/misc/Untitled.png")), 0);
			Timer.debug.measure("Load Texture:");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		v.put(new float[] { -1, -1, -1, 1, 1, 1, 1, -1, 1, 1, -1, -1 }).flip();
		t.put(new float[] { 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0 }).flip();
		
		Logging.logObject(Texture2D.get("Test"));
		
		vertices = VBO.create("A", VBOTarget.ARRAY);
		vertices.bufferData(v);
		vertices.bind();
		GL11.glVertexPointer(2, GL11.GL_FLOAT, 0, 0);
		
		tcoords = VBO.create("B", VBOTarget.ARRAY);
		tcoords.bufferData(t);
		tcoords.bind();
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		float aspect = 2 * (float) Display.getWidth() / Display.getHeight();
		GL11.glOrtho(-aspect, aspect, -2, 2, -1, 1);
		
	}
	
	@Override
	public void uninit() {
		vertices.destroy();
		tcoords.destroy();
	}
	
	@Override
	public void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		
		Texture2D.get("Test").bind();
		
		GL11.glRotatef(theta, phi, 0, 1);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
		
		
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
	}
	
	public static void main(String[] args) {
		GL.setTarget(new TextureTestTarget());
		try {
			GL.startGL();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	private boolean l, r, u, d;
	
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
				case Keyboard.KEY_UP:
					u = Keyboard.getEventKeyState();
					break;
				case Keyboard.KEY_DOWN:
					d = Keyboard.getEventKeyState();
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
		if (u)
			phi += rps * 0.001f * GL.deltaTime();
		if (d)
			phi += rps * 0.001f * GL.deltaTime();
	}
	
}
