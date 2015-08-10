package lwjgl.test.misc;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import control.ControlManager;
import globj.core.DataType;
import globj.core.GL;
import globj.core.RenderCommand;
import globj.objects.arrays.VAO;
import globj.objects.arrays.VBOFormat;
import globj.objects.bufferobjects.StaticVBO;
import globj.objects.bufferobjects.VBO;
import globj.objects.bufferobjects.values.VBOTarget;
import globj.objects.textures.Texture2D;
import globj.objects.textures.Textures;
import globj.objects.textures.values.MagnifyFilter;
import globj.objects.textures.values.MinifyFilter;
import lwjgl.debug.GLDebug;
import lwjgl.debug.Timer;



/**
 * Test Target to test functionality of Vertex Buffer Objects. <br/>
 * <br/>
 * For more information, visit
 * <a href="http://www.ozone3d.net/tutorials/opengl_vbo.php" >http://www.ozone3d.net/tutorials/opengl_vbo.php</a>
 *
 */
public class TextureTest extends RenderCommand {
	
	private static float	theta	= 0;
	private static float	phi		= 0;
	
	private static float rps = (float) (2 * Math.PI) / 6;
	
	private VBO vertices, tcoords;
	
	@Override
	public void init() {
		ControlManager.select(new TestControlSet());
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		Texture2D tex = null;
		try {
			BufferedImage img = ImageIO.read(new File("src/lwjgl/test/misc/Untitled.png"));
			Timer.DEBUG.mark();
			tex = Textures.createTexture2D("Test", img, 1);
			tex.setFilter(MinifyFilter.NEAREST, MagnifyFilter.NEAREST);
			Timer.DEBUG.measure("Load Texture:");
			GLDebug.debug(tex);
		}
		catch (IOException e) {
			GLDebug.logException(e);
		}
		
		float[] v = new float[] { -1, -1, -1, 1, 1, 1, 1, -1, 1, 1, -1, -1 };
		float[] t = new float[] { 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 1 };
		
		vertices = StaticVBO.create("A", VBOTarget.ARRAY, v);
		tcoords = StaticVBO.create("B", VBOTarget.ARRAY, t);
		
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
	
	@SuppressWarnings("deprecation")
	@Override
	public void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glRotatef(theta, phi, 0, 1);
		Textures.getTexture2D("Test").bind();
		
		VAO.defaultVAO.attachVertexBuffer(vertices, new VBOFormat(2, DataType.FLOAT, 0, 0));
		VAO.defaultVAO.attachTexCoordBuffer(tcoords, new VBOFormat(2, DataType.FLOAT, 0, 0));
		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		
	}
	
	public static void main(String[] args) {
		GL.setTarget(new TextureTest());
		try {
			GL.startGL();
		}
		catch (LWJGLException e) {
			GLDebug.logException(e);
		}
	}
	
	@Override
	public void input() {
		theta += rps * GL.deltaTime() * TestControlSet.LR.position();
		phi += rps * GL.deltaTime() * TestControlSet.UD.position();
		
		if (TestControlSet.ESC.state())
			GL.close();
		if (TestControlSet.F11.state())
			GL.toggleFS();
	}
	
}
