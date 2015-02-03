package lwjgl.test.misc;

import globj.core.GL;
import globj.core.RenderCommand;
import globj.objects.bufferobjects.VBO;
import globj.objects.bufferobjects.VBOTarget;
import globj.objects.framebuffers.FBO;
import globj.objects.framebuffers.values.FBOAttachment;
import globj.objects.textures.Texture2D;
import globj.objects.textures.values.TextureFormat;
import globj.objects.textures.values.TextureWrap;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import lwjgl.debug.GLDebug;

public class FBOTests extends RenderCommand {
	
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
	Texture2D c0;
	
	@Override
	public void init() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		// Logging.logInfo(FBO.constants());
		
		File file = new File("src/lwjgl/test/misc/Untitled.png");
		vbo = VBO.create("Test", VBOTarget.ARRAY);
		fbo = FBO.create("Test FBO");
		try {
			tex = Texture2D.create("Tex", ImageIO.read(file), 1);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		tex.setWrap(TextureWrap.CLAMP_EDGE, TextureWrap.CLAMP_EDGE);
		tex.update();
		GLDebug.debug(tex);
		
		try {
			tex.setDataRGBA(ImageIO.read(file), 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		GLDebug.debug(tex);
		
		vbo.bufferData(new float[] { 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0 });
		
		vbo.bind();
		GL11.glVertexPointer(2, GL11.GL_FLOAT, 4 * 4, 0 * 4);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 4 * 4, 2 * 4);
		VBO.unbind(VBOTarget.ARRAY);
		
		c0 = Texture2D.create("Color 0", TextureFormat.RGBA8, 1024, 1024, 1);
		Texture2D d = Texture2D.create("Depth", TextureFormat.D16, 1024, 1024, 1);
		
		GLDebug.debug(fbo);
		fbo.attach(c0, FBOAttachment.COLOR0, 0, 0);
		GLDebug.debug(fbo);
		fbo.attach(d, FBOAttachment.DEPTH, 0, 0);
		GLDebug.debug(fbo);
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
		
		GL11.glOrtho(-0.1, 1.1, -0.1, 1.1, -0.1, 1.1);
		
		GL11.glClearColor(1, 0, 0, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 2 * 3);
		
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		
		VBO.unbind(VBOTarget.ARRAY);
		tex.bindNone();
		
		// FBO.bindDraw(null);
		// GL30.glBlitFramebuffer(0, 0, 1024, 1024, 0, 0, 1024, 1024,
		// GL11.GL_COLOR_BUFFER_BIT, GL11.GL_NEAREST);
		FBO.bind(null);
		
		c0.bind();
		vbo.bind();
		
		
		GL11.glOrtho(-0.1, 1.1, -0.1, 1.1, -0.1, 1.1);

		GL11.glClearColor(0, 1, 1, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 2 * 3);
		
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		
		VBO.unbind(VBOTarget.ARRAY);
		c0.bindNone();
		
	}
	
}
