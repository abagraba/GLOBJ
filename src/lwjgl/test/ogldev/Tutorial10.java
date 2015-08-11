package lwjgl.test.ogldev;


import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import control.ControlManager;
import globj.core.DataType;
import globj.core.RenderCommand;
import globj.core.Window;
import globj.objects.arrays.VAO;
import globj.objects.arrays.VBOFormat;
import globj.objects.bufferobjects.StaticVBO;
import globj.objects.bufferobjects.VBO;
import globj.objects.bufferobjects.values.VBOTarget;
import globj.objects.shaders.Program;
import globj.objects.shaders.Programs;
import globj.objects.shaders.Shader;
import globj.objects.shaders.ShaderType;
import globj.objects.shaders.Shaders;
import lwjgl.debug.GLDebug;



public class Tutorial10 extends RenderCommand {
	
	private VBO		vbo;
	private VBO		ibo;
	private Shader	vert;
	private Shader	frag;
	private Program	prog;
	
	private float t;
	
	@Override
	public void init() {
		float[] vertices = new float[] { -0.6122f, -0.707f, -0.3535f, 0, -0.707f, 0.707f, 0.6122f, -0.707f, -0.3535f, 0, 1, 0 };
		int[] indices = new int[] { 0, 3, 1, 1, 3, 2, 2, 3, 0, 0, 1, 2 };
		
		vbo = StaticVBO.create("Test VBO", VBOTarget.ARRAY, vertices);
		ibo = StaticVBO.create("Test IBO", VBOTarget.ELEMENT_ARRAY, indices);
		
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
		float s = (float) Math.sin(t);
		float c = (float) Math.cos(t);
		t += 0.01f;
		FloatBuffer mat = BufferUtils.createFloatBuffer(16);
		mat.put(new float[] { c, 0, -s, 0, 0, 1, 0, 0, s, 0, c, 0, 0, 0, 0, 1 }).flip();
		
		Programs.current().setUniform("gWorld", mat, true);
		
		GL11.glClearColor(0, 0, 0, 0);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		VAO.defaultVAO.attachBuffer(0, vbo, new VBOFormat(3, DataType.FLOAT, 0, 0));
		
		ibo.bind();
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawElements(GL11.GL_TRIANGLES, 12, GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		ibo.bindNone();
		
	}
	
	public static void main(String[] args) {
		Window w = new Window();
		w.setTarget(new Tutorial10());
		w.start();
		ControlManager.select(w, new TutorialControlSet());
		
	}
	
	@Override
	public void input() {
		if (TutorialControlSet.FULLSCR.state())
			Window.toggleFS();
		if (TutorialControlSet.ESC.state())
			Window.close();
	}
	
}
