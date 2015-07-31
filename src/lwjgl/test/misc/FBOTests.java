package lwjgl.test.misc;


import globj.core.GL;
import globj.core.RenderCommand;
import globj.objects.arrays.VAO;
import globj.objects.arrays.VBOFormat;
import globj.objects.bufferobjects.DynamicFloatVBO;
import globj.objects.bufferobjects.VBOTarget;
import globj.objects.framebuffers.FBO;
import globj.objects.framebuffers.FBOs;
import globj.objects.framebuffers.values.FBOAttachment;
import globj.objects.textures.Texture2D;
import globj.objects.textures.Textures;
import globj.objects.textures.values.MagnifyFilter;
import globj.objects.textures.values.MinifyFilter;
import globj.objects.textures.values.TextureFormat;
import globj.objects.textures.values.TextureWrapMode;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;

import control.ControlManager;
import lwjgl.debug.GLDebug;



public class FBOTests extends RenderCommand {
	
	DynamicFloatVBO	rectvbo;
	DynamicFloatVBO	quadvbo;
	FBO				fbo;
	Texture2D		tex;
	Texture2D		c0;
	float[]			rect	= new float[] { 0, 0, 0, 1, //
			0, 1, 0, 0, //
			1, 1, 1, 0, //
			1, 1, 1, 0, //
			1, 0, 1, 1, //
			0, 0, 0, 1		};
	float[]			quad	= new float[] { 0, 0, 0, 0, //
			0, 1, 0, 1, //
			1, 1, 1, 1, //
			1, 1, 1, 1, //
			1, 0, 1, 0, //
			0, 0, 0, 0		};
	
	
	@Override
	public void init() {
		ControlManager.select(new TestControlSet());
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		FBOs.constants();
		
		File file = new File("src/lwjgl/test/misc/Untitled.png");
		
		rectvbo = DynamicFloatVBO.create("Rect", VBOTarget.ARRAY);
		quadvbo = DynamicFloatVBO.create("Quad", VBOTarget.ARRAY);
		
		fbo = FBO.create("Test FBO");
		try {
			tex = Textures.createTexture2D("Tex", ImageIO.read(file), 1);
			tex.setFilter(MinifyFilter.NEAREST, MagnifyFilter.NEAREST);
			tex.setWrap(TextureWrapMode.CLAMP_EDGE, TextureWrapMode.CLAMP_EDGE);
		}
		catch (IOException e) {
			GLDebug.logException(e);
		}
		
		GLDebug.debug(tex);
		
		rectvbo.bind();
		rectvbo.write(rect);
		rectvbo.undobind();
		
		quadvbo.bind();
		quadvbo.write(quad);
		quadvbo.undobind();
		
		rectvbo.debugContents();
		quadvbo.debugContents();
		
		c0 = Textures.createTexture2D("Color 0", TextureFormat.RGBA8, 1024, 1024, 0, 0);
		Texture2D d = Textures.createTexture2D("Depth", TextureFormat.D16, 1024, 1024, 0, 0);
		
		fbo.attach(c0, FBOAttachment.COLOR0, 0, 0);
		GLDebug.debug(fbo);
		fbo.attach(d, FBOAttachment.DEPTH, 0, 0);
		GLDebug.debug(fbo);
	}
	
	@Override
	public void uninit() {
		rectvbo.destroy();
		quadvbo.destroy();
		
		c0.destroy();
		tex.destroy();
		
		fbo.destroy();
	}
	
	@Override
	public void input() {
		if (TestControlSet.ESC.state())
			GL.close();
		if (TestControlSet.F11.state())
			GL.toggleFS();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void render() {
		fbo.bind();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(-0.1, 1.1, 1.1, -0.1, -1.1, 1.1);
		
		GL11.glViewport(0, 0, 1024, 1024);
		
		GL11.glClearColor(0, 0, 1, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		tex.bind();
		
		VAO.defaultVAO.attachVertexBuffer(quadvbo, VBOFormat.FLOAT_2_4_0);
		VAO.defaultVAO.attachTexCoordBuffer(quadvbo, VBOFormat.FLOAT_2_4_2);
		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 2 * 3);
		
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		
		tex.undobind();
		
		// **GL30**.**glBlitFramebuffer(0, 0, 1024, 1024, 0, 0, 1024, 1024,
		// **GL11**.**GL_COLOR_BUFFER_BIT, GL11.GL_NEAREST);
		fbo.undobind();
		
		c0.bind();
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(-0.1, 1.1, -0.1, 1.1, -1.1, 1.1);
		
		GL11.glViewport(0, 0, 1920, 1080);
		
		GL11.glClearColor(0, 0.5f, 1, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		VAO.defaultVAO.attachVertexBuffer(quadvbo, VBOFormat.FLOAT_2_4_0);
		VAO.defaultVAO.attachTexCoordBuffer(quadvbo, VBOFormat.FLOAT_2_4_2);
		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 2 * 3);
		
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		
		c0.bindNone();
		
	}
	
	public static void main(String[] args) {
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
		GL.setTarget(new FBOTests());
		try {
			GL.startGL();
		}
		catch (LWJGLException e) {
			GLDebug.logException(e);
		}
	}
	
}
