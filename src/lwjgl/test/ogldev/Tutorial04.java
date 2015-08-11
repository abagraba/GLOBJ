package lwjgl.test.ogldev;


import java.io.IOException;

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



public class Tutorial04 extends RenderCommand {
	
	private VBO		vbo;
	private Shader	vert;
	private Shader	frag;
	private Program	prog;
	
	@Override
	public void init() {
		float[] vertices = new float[] { -1, -1, 0, 1, -1, 0, 0, 1, 0 };
		
		vbo = StaticVBO.create("Test VBO", VBOTarget.ARRAY, vertices);
		
		try {
			vert = Shaders.createShader("Vert", ShaderType.VERTEX, getClass().getResourceAsStream("shaders/tut04.vs"));
			frag = Shaders.createShader("Frag", ShaderType.FRAGMENT, getClass().getResourceAsStream("shaders/tut04.fs"));
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
		GL11.glClearColor(0, 0, 0, 0);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		VAO.defaultVAO.attachBuffer(0, vbo, new VBOFormat(3, DataType.FLOAT, 0, 0));
		
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
		GL20.glDisableVertexAttribArray(0);
	}
	
	public static void main(String[] args) {
		Window w = new Window();
		w.setTarget(new Tutorial04());
		w.start();
		ControlManager.attach(w, new TutorialControlSet());
		
	}
	
	@Override
	public void input() {
		if (TutorialControlSet.FULLSCR.state())
			Window.toggleFS();
		if (TutorialControlSet.ESC.state())
			Window.close();
	}
	
}
