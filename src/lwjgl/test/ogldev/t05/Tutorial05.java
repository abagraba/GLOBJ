package lwjgl.test.ogldev.t05;

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

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Tutorial05 extends RenderCommand {
	
	VBO vbo;
	Program prog;
	int t;
	
	@Override
	public void init() {
		float[] vertices = new float[] { -1, -1, 0, 1, -1, 0, 0, 1, 0 };
		vbo = StaticVBO.create("Test VBO", VBOTarget.ARRAY, vertices);
		GL11.glClearColor(0, 0, 0, 0);
		
		Shader vert = null;
		Shader frag = null;
		try {
			vert = Shaders.createShader("Vert", ShaderType.VERTEX, getClass().getResourceAsStream("shader.vs"));
			frag = Shaders.createShader("Frag", ShaderType.FRAGMENT, getClass().getResourceAsStream("shader.fs"));
			vert.debugQuery();
			frag.debugQuery();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		prog = Programs.createProgram("Test", vert, frag);
		
		Shaders.destroyShader(vert);
		Shaders.destroyShader(frag);
		
		prog.debugQuery();
	}
	
	@Override
	public void uninit() {
		vbo.destroy();
		vbo = null;
	}
	
	@Override
	public void render() {
		prog.bind();
		
		GL20.glUniform1f(prog.uniformLocation("gScale"), (float) Math.sin(0.01 * t++));
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL20.glEnableVertexAttribArray(0);
		
		vbo.bind();
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
		vbo.bindNone();
		GL20.glDisableVertexAttribArray(0);
		
		prog.bindNone();
	}
	
	public static void main(String[] args) {
		GL.setTarget(new Tutorial05());
		try {
			GL.startGL();
		} catch (LWJGLException e) {
			e.printStackTrace();
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
			}
		}
	}
	
}
