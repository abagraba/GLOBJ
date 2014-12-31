package lwjgl.test.misc;

import java.awt.image.BufferedImage;
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
import lwjgl.core.texture.Texture2D;
import lwjgl.core.texture.TextureFormat;
import lwjgl.core.texture.values.MagnifyFilter;
import lwjgl.core.texture.values.MinifyFilter;
import lwjgl.core.texture.values.Texture2DTarget;
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
public class TextureFormatTestTarget extends RenderTarget {
	
	private static float rps = (float) (2 * Math.PI) / 6;
	
	
	@Override
	public void init() {
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		TextureFormat[] t = TextureFormat.values();
		for (int i = 0; i < t.length; i++) {
			TextureFormat format = t[i];
			System.out.println();
			System.out.println(format);
			Texture2D tex = Texture2D.create("Test" + format, Texture2DTarget.TEXTURE_2D);
			tex.setFilter(MinifyFilter.NEAREST, MagnifyFilter.NEAREST);
			Timer.debug.mark();
			tex.initializeTexture(256, 256, 1, format);
			Timer.debug.measure("Load Texture " + format + ":");
		}
		
	}
	
	@Override
	public void uninit() {
	}
	
	@Override
	public void render() {
	}
	
	public static void main(String[] args) {
		GL.setTarget(new TextureFormatTestTarget());
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
				case Keyboard.KEY_ESCAPE:
					GL.close();
					break;
			}
		}
		
	}
	
}
