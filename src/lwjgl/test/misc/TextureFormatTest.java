package lwjgl.test.misc;


import org.lwjgl.opengl.GL11;

import control.ControlManager;
import globj.core.RenderCommand;
import globj.core.Window;
import globj.objects.textures.Textures;
import globj.objects.textures.values.TextureFormat;
import lwjgl.debug.GLDebug;
import lwjgl.debug.Timer;



/**
 * Test Target to test functionality of Vertex Buffer Objects. <br/>
 * <br/>
 * For more information, visit
 * <a href="http://www.ozone3d.net/tutorials/opengl_vbo.php" >http://www.ozone3d.net/tutorials/opengl_vbo.php</a>
 *
 */
public class TextureFormatTest extends RenderCommand {
	
	@Override
	public void init() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		for (int i = -1024; i < 1024; i++) {
			Textures.createTexture2D("Test" + i, TextureFormat.RGBA8, 1024, 1024, 1);
			Textures.destroyTexture2D("Test" + i);
		}
		
		TextureFormat[] t = TextureFormat.values();
		for (int i = 0; i < t.length; i++) {
			TextureFormat format = t[i];
			int j = -1024;
			for (; j < 0; j++)
				Textures.createTexture2D("Test" + j + format, format, 256, 256, 1);
			Timer.DEBUG.mark();
			for (; j < 1024; j++)
				Textures.createTexture2D("Test" + j + format, format, 256, 256, 1);
			Timer.DEBUG.measure("Loading Texture " + format + ":", 1024);
			GLDebug.write("\n");
			for (j = -1024; j < 1024; j++)
				Textures.destroyTexture2D("Test" + j + format);
			GLDebug.flushErrors();
		}
		
	}
	
	public static void main(String[] args) {
		Window w = new Window();
		w.setTarget(new TextureFormatTest());
		w.start();
		ControlManager.select(w, new TestControlSet());
	}
	
	@Override
	public void input() {
		if (TestControlSet.ESC.state())
			Window.close();
		if (TestControlSet.F11.state())
			Window.toggleFS();
	}
	
	@Override
	public void uninit() {
		//
	}
	
	@Override
	public void render() {
		//
	}
	
}
