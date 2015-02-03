package lwjgl.test.ogldev.t14;

import globj.camera.PerspectiveCamera;
import globj.camera.Screen;
import globj.core.GL;
import globj.core.SceneCommand;
import globj.objects.bufferobjects.VBO;
import globj.objects.bufferobjects.VBOTarget;
import globj.objects.bufferobjects.VBOs;
import globj.objects.shaders.Program;
import globj.objects.shaders.Programs;
import globj.objects.shaders.Shader;
import globj.objects.shaders.ShaderType;
import globj.objects.shaders.Shaders;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import lwjgl.core.utils.Transform;
import lwjgl.core.utils.UnitQuaternion;
import lwjgl.core.utils.V3f;

public class Tutorial14 extends SceneCommand {
	
	public Tutorial14() {
		super(new PerspectiveCamera(new Transform(new V3f(0, 0, -10f), new UnitQuaternion(0, 0, 0, 1)), 0.1f, 20, 90), Screen.left);
	}
	
	VBO vbo;
	VBO ibo;
	Program prog;
	Transform trans = new Transform();
	float fov = 90;
	
	@Override
	public boolean load() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		vbo = VBOs.createVBO("Test VBO", VBOTarget.ARRAY);
		float f = 0.707f;
		vbo.bufferData(new float[] { -f, -f, 0, -f, f, 0.5f, f, f, 0, f, -f, 0.5f });
		ibo = VBOs.createVBO("Test IBO", VBOTarget.ELEMENT_ARRAY);
		ibo.bufferData(new int[] { 0, 3, 1, 1, 3, 2, 2, 3, 0, 0, 1, 2 });
		GL11.glClearColor(0, 0, 0, 0);
		
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
		prog.debug();
		
		trans.setPosition(0, 0, 1f);
		trans.setScale(0.33f);
		return true;
	}
	
	@Override
	public void unload() {
		vbo = VBOs.destroyVBO(vbo);
		ibo = VBOs.destroyVBO(ibo);
		prog = Programs.destroyProgram(prog);
	}
	
	@Override
	public void draw(float[] vpMatrix) {
		prog.bind();
		
		FloatBuffer mmat = BufferUtils.createFloatBuffer(16);
		mmat.put(trans.getModelMatrix()).flip();
		GL20.glUniformMatrix4(prog.uniformLocation("mMatrix"), false, mmat);
		
		FloatBuffer vpmat = BufferUtils.createFloatBuffer(16);
		vpmat.put(vpMatrix).flip();
		GL20.glUniformMatrix4(prog.uniformLocation("vpMatrix"), false, vpmat);
		
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
	boolean up, down;
	
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
			}
		}
		V3f v = new V3f((d ? 0.01f : 0) - (a ? 0.01f : 0), (w ? 0.01f : 0) - (s ? 0.01f : 0), 0);
		UnitQuaternion r = UnitQuaternion.rotation(new V3f(0, 0, 1), -(e ? 0.01f : 0) + (q ? 0.01f : 0));
		PerspectiveCamera cam = (PerspectiveCamera)camera;
		
		cam.transform.translateBy(v);
		cam.transform.rotateBy(r);
		
		fov = Math.max(30, Math.min(120, fov + (down?1:0) - (up?1:0)));
		cam.setFOV(fov);
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
