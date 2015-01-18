package lwjgl.test.misc;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import lwjgl.core.GL;
import lwjgl.core.RenderCommand;
import lwjgl.core.objects.textures.Texture2D;
import lwjgl.core.objects.textures.values.MagnifyFilter;
import lwjgl.core.objects.textures.values.MinifyFilter;
import lwjgl.core.objects.textures.values.TextureFormat;
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
			Texture2D tex = Texture2D.create("Test" + format);
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
