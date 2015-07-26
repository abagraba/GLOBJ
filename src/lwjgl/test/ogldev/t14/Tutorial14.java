package lwjgl.test.ogldev.t14;

import globj.camera.PerspectiveCamera;
import globj.camera.Screen;
import globj.core.GL;
import globj.core.SceneCommand;
import globj.core.utils.Transform;
import globj.core.utils.UnitQuaternion;
import globj.core.utils.V3f;
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

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

public class Tutorial14 extends SceneCommand {
	
	public Tutorial14() {
		super(new PerspectiveCamera(0.1f, 20, 90), Screen.left);
	}
	
	VBO vbo;
	VBO ibo;
	Program prog;
	Transform transform = new Transform();
	float fov = 90;
	
	@Override
	public boolean load() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		float f = 0.707f;
		vbo = StaticVBO.create("Test VBO", VBOTarget.ARRAY, new float[] { -f, -f, 0, -f, f, 0.5f, f, f, 0, f, -f, 0.5f });
		ibo = StaticVBO.create("Test IBO", VBOTarget.ELEMENT_ARRAY, new int[] { 0, 3, 1, 1, 3, 2, 2, 3, 0, 0, 1, 2 });
		
		GL11.glClearColor(0, 0.5f, 0.5f, 0);
		Shader vert = null;
		Shader frag = null;
		InputStream vin;
		InputStream fin;
		try {
			vin = new FileInputStream("src/lwjgl/test/ogldev/t12/shader.vs");
			fin = new FileInputStream("src/lwjgl/test/ogldev/t12/shader.fs");
			vert = Shaders.createShader("Vert", ShaderType.VERTEX, vin);
			frag = Shaders.createShader("Frag", ShaderType.FRAGMENT, fin);
			vin.close();
			fin.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		prog = Programs.createProgram("Test", vert, frag);
		Shaders.destroyShader(vert);
		Shaders.destroyShader(frag);
		prog.debugQuery();
		
		transform.setPosition(0, 0, 1f);
		transform.setScale(0.33f);
		return true;
	}
	
	@Override
	public void unload() {
		vbo.destroy();
		ibo.destroy();
		prog.destroy();
	}
	
	@Override
	public void draw(Matrix4f viewMatrix, Matrix4f projectionMatrix) {
		prog.bind();
		
		prog.setUniform("mMatrix", transform.getModelMatrix(), false);
		prog.setUniform("vMatrix", viewMatrix, false);
		prog.setUniform("pMatrix", projectionMatrix, false);
		
		vbo.bind();
		ibo.bind();
		GL20.glEnableVertexAttribArray(0);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL11.glDrawElements(GL11.GL_TRIANGLES, 12, GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		ibo.bindNone();
		vbo.bindNone();
		
		prog.bindNone();
		
	}
	
	boolean w, a, s, d, e, q;
	boolean up, down, space;
	
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
				case Keyboard.KEY_W:
					w = Keyboard.getEventKeyState();
					break;
				case Keyboard.KEY_A:
					a = Keyboard.getEventKeyState();
					break;
				case Keyboard.KEY_S:
					s = Keyboard.getEventKeyState();
					break;
				case Keyboard.KEY_D:
					d = Keyboard.getEventKeyState();
					break;
				case Keyboard.KEY_E:
					e = Keyboard.getEventKeyState();
					break;
				case Keyboard.KEY_Q:
					q = Keyboard.getEventKeyState();
					break;
				case Keyboard.KEY_UP:
					up = Keyboard.getEventKeyState();
					break;
				case Keyboard.KEY_DOWN:
					down = Keyboard.getEventKeyState();
					break;
				case Keyboard.KEY_SPACE:
					if (Keyboard.getEventKeyState())
						space = !space;
					break;
			}
		}
		//
		
		float moveSpeed = 0.05f;
		float rotSpeed = 0.05f;
		
		V3f v = new V3f((d ? moveSpeed : 0) - (a ? moveSpeed : 0), (w ? moveSpeed : 0) - (s ? moveSpeed : 0), 0);
		UnitQuaternion r = UnitQuaternion.rotation(new V3f(0, 0, 1), -(e ? rotSpeed : 0) + (q ? rotSpeed : 0));
		
		if (space) {
			camera.transform.translateBy(v);
			camera.transform.rotateBy(r);
		}
		else {
			transform.translateBy(v);
			transform.rotateBy(r);
		}
		if (camera instanceof PerspectiveCamera) {
			PerspectiveCamera camx = (PerspectiveCamera) camera;
			fov = Math.max(30, Math.min(120, fov + (down ? 1 : 0) - (up ? 1 : 0)));
			camx.setFOV(fov);
		}
		
	}
	
	public static void main(String[] args) {
		GL.setTarget(new Tutorial14());
		try {
			GL.startGL();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
}
