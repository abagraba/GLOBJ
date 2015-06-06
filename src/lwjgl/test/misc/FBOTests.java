package lwjgl.test.misc;

import globj.core.GL;
import globj.core.RenderCommand;
import globj.objects.bufferobjects.DynamicFloatVBO;
import globj.objects.bufferobjects.VBOTarget;
import globj.objects.framebuffers.FBO;
import globj.objects.framebuffers.values.FBOAttachment;
import globj.objects.textures.Texture2D;
import globj.objects.textures.Textures;
import globj.objects.textures.values.MagnifyFilter;
import globj.objects.textures.values.MinifyFilter;
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
	
	DynamicFloatVBO vbo;
	FBO fbo;
	Texture2D tex;
	Texture2D c0;
	float[] rect = new float[] { 0, 0, 0, 1, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1 };
	
	@Override
	public void init() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		// Logging.logInfo(FBO.constants());
		
		File file = new File("src/lwjgl/test/misc/Untitled.png");
		vbo = DynamicFloatVBO.create("Test", VBOTarget.ARRAY);
		fbo = FBO.create("Test FBO");
		try {
			tex = Textures.createTexture2D("Tex", ImageIO.read(file), 1);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		tex.setFilter(MinifyFilter.NEAREST, MagnifyFilter.NEAREST);
		tex.setWrap(TextureWrap.CLAMP_EDGE, TextureWrap.CLAMP_EDGE);
		tex.update();
		
		GLDebug.debug(tex);
		
		vbo.bind();
		vbo.write(rect);
		vbo.debugContents();
		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glVertexPointer(2, GL11.GL_FLOAT, 4 * 4, 0 * 4);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 4 * 4, 2 * 4);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		vbo.undobind();
		
		c0 = Textures.createTexture2D("Color 0", TextureFormat.RGBA8, 1024, 1024, 0, 0);
		Texture2D d = Textures.createTexture2D("Depth", TextureFormat.D16, 1024, 1024, 0, 0);
		
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
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(-0.1, 1.1, -0.1, 1.1, -1.1, 1.1);
		
		GL11.glViewport(0, 0, 1024, 1024);
		
		GL11.glClearColor(1, 0, 0, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 2 * 3);
		
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		
		vbo.undobind();
		tex.bindNone();
		
		// FBO.bindDraw(null);
		// GL30.glBlitFramebuffer(0, 0, 1024, 1024, 0, 0, 1024, 1024,
		// GL11.GL_COLOR_BUFFER_BIT, GL11.GL_NEAREST);
		fbo.undobind();
		
		c0.bind();
		vbo.bind();
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(-0.1, 1.1, -0.1, 1.1, -1.1, 1.1);

		GL11.glViewport(0, 0, 1920, 1080);

		GL11.glClearColor(0, 0.5f, 1, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 2 * 3);
		
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		
		vbo.undobind();
		c0.bindNone();
	}
	
}
