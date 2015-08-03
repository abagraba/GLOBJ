package lwjgl.test.ogldev;


import globj.core.DataType;
import globj.core.GL;
import globj.core.RenderCommand;
import globj.math.Transform;
import globj.math.UnitQuaternion;
import globj.math.Vector3f;
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

import java.io.IOException;

import lwjgl.debug.GLDebug;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import control.ControlManager;



public class Tutorial11 extends RenderCommand {
	
	private VBO		vbo;
	private VBO		ibo;
	private Shader	vert;
	private Shader	frag;
	private Program	prog;
	
	private Transform trans = new Transform();
	
	@Override
	public void init() {
		float[] vertices = new float[] { -0.6122f, -0.707f, -0.3535f, 0, -0.707f, 0.707f, 0.6122f, -0.707f, -0.3535f, 0, 1, 0 };
		int[] indices = new int[] { 0, 3, 1, 1, 3, 2, 2, 3, 0, 0, 1, 2 };
		
		ControlManager.select(new TutorialControlSet());
		vbo = StaticVBO.create("Test VBO", VBOTarget.ARRAY, vertices);
		ibo = StaticVBO.create("Test IBO", VBOTarget.ELEMENT_ARRAY, indices);
		
		try {
			vert = Shaders.createShader("Vert", ShaderType.VERTEX, getClass().getResourceAsStream("shaders/tut09.vs"));
			frag = Shaders.createShader("Frag", ShaderType.FRAGMENT, getClass().getResourceAsStream("shaders/tut09.fs"));
		}
		catch (IOException ex) {
			GLDebug.logException(ex);
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
		Programs.current().setUniform("gWorld", trans.getModelMatrix(), false);
		
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
		GL.setTarget(new Tutorial11());
		try {
			GL.startGL();
		}
		catch (LWJGLException e) {
			GLDebug.logException(e);
		}
	}
	
	@Override
	public void input() {
		trans.translateBy(new Vector3f(TutorialControlSet.AD.position(), TutorialControlSet.WS.position(), 0));
		trans.rotateBy(UnitQuaternion.rotation(new Vector3f(0, 0, 1), -TutorialControlSet.QE.position()));
		if (TutorialControlSet.FULLSCR.state())
			GL.toggleFS();
		if (TutorialControlSet.ESC.state())
			GL.close();
	}
	
}
