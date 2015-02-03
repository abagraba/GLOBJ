package lwjgl.test.misc;

import globj.core.GL;
import globj.core.RenderCommand;
import globj.objects.textures.Texture2D;
import globj.objects.textures.values.TextureFormat;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import lwjgl.debug.Timer;

/**
 * Test Target to test functionality of Vertex Buffer Objects. <br/>
 * <br/>
 * For more information, visit <a
 * href="http://www.ozone3d.net/tutorials/opengl_vbo.php"
 * >http://www.ozone3d.net/tutorials/opengl_vbo.php</a>
 *
 */
public class TextureFormatTestTarget extends RenderCommand {
	
	@Override
	public void init() {
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		TextureFormat[] t = TextureFormat.values();
		for (int i = 0; i < t.length; i++) {
			TextureFormat format = t[i];
			System.out.println();
			System.out.println(format);
			for (int j = -16; j < 0; j++)
				Texture2D.create("Test" + j + format, format, 256, 256, 1);
			Timer.debug.mark();
			for (int j = 0; j < 256; j++)
				Texture2D.create("Test" + j + format, format, 256, 256, 1);
			Timer.debug.measure("Load Texture " + format + ":", 256);
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
	
	@Override
	public void input() {
		while (Keyboard.next()) {
			switch (Keyboard.getEventKey()) {
				case Keyboard.KEY_ESCAPE:
					GL.close();
					break;
			}
		}
		
	}
	
}
