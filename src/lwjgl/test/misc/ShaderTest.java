package lwjgl.test.misc;


import globj.core.DataType;
import globj.core.GL;
import globj.core.RenderCommand;
import globj.objects.arrays.VAO;
import globj.objects.arrays.VBOFormat;
import globj.objects.bufferobjects.StaticVBO;
import globj.objects.bufferobjects.VBO;
import globj.objects.bufferobjects.VBOTarget;
import globj.objects.shaders.Program;
import globj.objects.shaders.Programs;
import globj.objects.shaders.Shader;
import globj.objects.shaders.ShaderType;
import globj.objects.shaders.Shaders;

import java.io.IOException;

import lwjgl.debug.GLDebug;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import control.ControlManager;



public class ShaderTest extends RenderCommand {
	
	private VBO		vbo;
	private Program	prog;
	
	
	@Override
	public void init() {
		ControlManager.select(new TestControlSet());
		
		float[] vertices = new float[] { -1, -1, 0, 1, -1, 0, 0, 1, 0 };
		vbo = StaticVBO.create("Test VBO", VBOTarget.ARRAY, vertices);
		
		try {
			Shader vert = Shaders.createShader("Vert", ShaderType.VERTEX, getClass().getResourceAsStream("shader.vs"));
			Shader frag = Shaders.createShader("Frag", ShaderType.FRAGMENT, getClass().getResourceAsStream("shader.fs"));
			prog = Programs.createProgram("Test", vert, frag);
			vert.debugQuery();
			frag.debugQuery();
			Shaders.destroyShader(vert);
			Shaders.destroyShader(frag);
		}
		catch (IOException e) {
			GLDebug.logException(e);
		}
		
		prog.debugQuery();
		
	}
	
	@Override
	public void uninit() {
		prog.destroy();
		vbo.destroy();
	}
	
	@Override
	public void render() {
		prog.bind();
		GL11.glClearColor(0, 0, 0, 0);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		VAO.defaultVAO.attachBuffer(0, vbo, new VBOFormat(3, DataType.FLOAT, 0, 0));
		
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
		GL20.glDisableVertexAttribArray(0);
		
		prog.bindNone();
	}
	
	public static void main(String[] args) {
		GL.setTarget(new ShaderTest());
		try {
			GL.startGL();
		}
		catch (LWJGLException e) {
			GLDebug.logException(e);
		}
	}
	
	@Override
	public void input() {
		if (TestControlSet.ESC.state())
			GL.close();
		if (TestControlSet.F11.state())
			GL.toggleFS();
	}
	
}
