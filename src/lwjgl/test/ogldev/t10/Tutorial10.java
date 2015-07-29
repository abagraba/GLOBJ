package lwjgl.test.ogldev.t10;


import globj.core.GL;
import globj.core.RenderCommand;
import globj.objects.bufferobjects.StaticVBO;
import globj.objects.bufferobjects.VBO;
import globj.objects.bufferobjects.VBOTarget;
import globj.objects.shaders.Program;
import globj.objects.shaders.Programs;
import globj.objects.shaders.Shader;
import globj.objects.shaders.ShaderType;
import globj.objects.shaders.Shaders;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;

import lwjgl.debug.GLDebug;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;



public class Tutorial10 extends RenderCommand {
	
	VBO		vbo;
	VBO		ibo;
	Shader	vert;
	Shader	frag;
	Program	prog;
	int		t;
	
	
	@Override
	public void init() {
		vbo = StaticVBO.create(	"Test VBO", VBOTarget.ARRAY,
								new float[] { -0.6122f, -0.707f, -0.3535f, 0, -0.707f, 0.707f, 0.6122f, -0.707f, -0.3535f, 0, 1, 0 });
		ibo = StaticVBO.create("Test IBO", VBOTarget.ELEMENT_ARRAY, new int[] { 0, 3, 1, 1, 3, 2, 2, 3, 0, 0, 1, 2 });
		
		InputStream vin;
		InputStream fin;
		try {
			vin = new FileInputStream("src/lwjgl/test/ogldev/t09/shader.vs");
			fin = new FileInputStream("src/lwjgl/test/ogldev/t09/shader.fs");
			vert = Shaders.createShader("Vert", ShaderType.VERTEX, vin);
			frag = Shaders.createShader("Frag", ShaderType.FRAGMENT, fin);
		}
		catch (IOException e) {
			GLDebug.logException(e);
		}
		vert.debugQuery();
		frag.debugQuery();
		
		prog = Programs.createProgram("Test", vert, frag);
		
		prog.debugQuery();
		prog.bind();
	}
	
	@Override
	public void uninit() {
		vbo.destroy();
	}
	
	@Override
	public void render() {
		float s = (float) Math.sin(0.01 * t++);
		float c = (float) Math.cos(0.01 * t++);
		FloatBuffer mat = BufferUtils.createFloatBuffer(16);
		mat.put(new float[] { c, 0, -s, 0, 0, 1, 0, 0, s, 0, c, 0, 0, 0, 0, 1 }).flip();
		GL20.glUniformMatrix4(prog.uniformLocation("gWorld"), true, mat);
		
		GL11.glClearColor(0, 0, 0, 0);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		GL20.glEnableVertexAttribArray(0);
		vbo.bind();
		ibo.bind();
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL11.glDrawElements(GL11.GL_TRIANGLES, 12, GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		ibo.bindNone();
		vbo.bindNone();
		
	}
	
	public static void main(String[] args) {
		GL.setTarget(new Tutorial10());
		try {
			GL.startGL();
		}
		catch (LWJGLException e) {
			GLDebug.logException(e);
		}
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
				default:
			}
		}
	}
	
}
