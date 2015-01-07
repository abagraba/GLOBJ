package lwjgl.test.misc;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import lwjgl.core.GL;
import lwjgl.core.RenderTarget;
import lwjgl.core.VBO;
import lwjgl.core.VBOTarget;
import lwjgl.core.framebuffer.FBO;
import lwjgl.core.framebuffer.RBO;
import lwjgl.core.framebuffer.values.FBOAttachment;
import lwjgl.core.texture.Texture2D;
import lwjgl.core.texture.values.TextureFormat;
import lwjgl.debug.Logging;

public class FBOTests extends RenderTarget {
	
	public static void main(String[] args) {
		GL.setTarget(new FBOTests());
		try {
			GL.startGL();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	VBO vbo;
	FBO fbo;
	@Override
	public void init() {
		
		vbo = VBO.create("Test", VBOTarget.ARRAY);
		
		Logging.logInfo(FBO.constants());
		
		Texture2D c0 = Texture2D.create("Color 0");
		Texture2D d = Texture2D.create("Depth");
		fbo = FBO.create("Test RBO");
		Logging.logObject(fbo);
		fbo.attach(c0, FBOAttachment.COLOR0, 0, 0);
		Logging.logObject(fbo);
		c0.initializeTexture(16, 16, 1, TextureFormat.RGBA8);
		Logging.logObject(fbo);
		fbo.attach(d, FBOAttachment.DEPTH, 0, 0);
		Logging.logObject(fbo);
		d.initializeTexture(16, 16, 1, TextureFormat.D16);
		Logging.logObject(fbo);
		FBO.bind(null);
	}
	
	@Override
	public void uninit() {
		
	}
	
	@Override
	public void input() {
		while (Keyboard.next()) {
			switch (Keyboard.getEventKey()) {
				case Keyboard.KEY_ESCAPE:
					GL.close();
					break;
				case Keyboard.KEY_F11:
					if (!Keyboard.getEventKeyState())
						GL.toggleFS();
					break;
			}
		}
	}
	
	@Override
	public void render() {
	}
	
}
