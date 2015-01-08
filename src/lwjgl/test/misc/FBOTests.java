package lwjgl.test.misc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
	Texture2D tex;
	
	@Override
	public void init() {
//		Logging.logInfo(FBO.constants());
		
		File file = new File("src/lwjgl/test/misc/Untitled.png");
		vbo = VBO.create("Test", VBOTarget.ARRAY);
		fbo = FBO.create("Test FBO");
		tex = Texture2D.create("Tex");
		Logging.logObject(tex);
		
		try {
			tex.setDataRGBA(ImageIO.read(file), 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logging.logObject(tex);

		vbo.bufferData(new float[] { 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0 });
		
		vbo.bind();
		GL11.glVertexPointer(2, GL11.GL_FLOAT, 4 * 4, 0 * 4);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 4 * 4, 2 * 4);
		VBO.unbind(VBOTarget.ARRAY);
		
		Texture2D c0 = Texture2D.create("Color 0");
		Texture2D d = Texture2D.create("Depth");
		
		Logging.logObject(fbo);
		fbo.attach(c0, FBOAttachment.COLOR0, 0, 0);
		Logging.logObject(fbo);
		c0.initializeTexture(16, 16, 1, TextureFormat.RGBA8);
		Logging.logObject(fbo);
		fbo.attach(d, FBOAttachment.DEPTH, 0, 0);
		Logging.logObject(fbo);
		d.initializeTexture(16, 16, 1, TextureFormat.D16);
		Logging.logObject(fbo);
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
		fbo.bind();
		tex.bind();
		vbo.bind();
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 2 * 3);
		
		GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		VBO.unbind(VBOTarget.ARRAY);
		Texture2D.bind(null);
		FBO.bind(null);
	}
	
}
