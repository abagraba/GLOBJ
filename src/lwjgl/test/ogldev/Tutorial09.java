package lwjgl.test.ogldev;


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
import java.nio.FloatBuffer;

import lwjgl.debug.GLDebug;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import control.ControlManager;



public class Tutorial09 extends RenderCommand {
	
	private VBO		vbo;
	private Shader	vert;
	private Shader	frag;
	private Program	prog;
	private int		t;
	
	
	@Override
	public void init() {
		float[] data = new float[] { -0.707f, -0.707f, 0, 0.707f, -0.707f, 0, 0, 1, 0 };
		
		ControlManager.select(new TutorialControlSet());
		vbo = StaticVBO.create("Test VBO", VBOTarget.ARRAY, data);
		
		try {
			vert = Shaders.createShader("Vert", ShaderType.VERTEX, getClass().getResourceAsStream("shaders/tut09.vs"));
			frag = Shaders.createShader("Frag", ShaderType.FRAGMENT, getClass().getResourceAsStream("shaders/tut09.fs"));
		}
		catch (IOException e) {
			GLDebug.logException(e);
		}
		vert.debugQuery();
		frag.debugQuery();
		
		prog = Programs.createProgram("Test", vert, frag);
		
		vert.destroy();
		frag.destroy();
		
		prog.debugQuery();
		prog.bind();
	}
	
	@Override
	public void uninit() {
		prog.destroy();
		vbo.destroy();
	}
	
	@Override
	public void render() {
		float s = (float) Math.sin(0.01 * t++);
		FloatBuffer mat = BufferUtils.createFloatBuffer(16);
		mat.put(new float[] { s, 0, 0, 0, 0, s, 0, 0, 0, 0, s, 0, 0, 0, 0, 1 }).flip();
		Programs.current().setUniform("gWorld", mat, true);
		
		GL11.glClearColor(0, 0, 0, 0);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		VAO.defaultVAO.attachBuffer(0, vbo, new VBOFormat(3, DataType.FLOAT, 0, 0));
		
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
		GL20.glDisableVertexAttribArray(0);
	}
	
	public static void main(String[] args) {
		GL.setTarget(new Tutorial09());
		try {
			GL.startGL();
		}
		catch (LWJGLException e) {
			GLDebug.logException(e);
		}
	}
	
	@Override
	public void input() {
		if (TutorialControlSet.FULLSCR.state())
			GL.toggleFS();
		if (TutorialControlSet.ESC.state())
			GL.close();
	}
	
}
