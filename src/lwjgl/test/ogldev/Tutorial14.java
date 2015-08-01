package lwjgl.test.ogldev;


import globj.camera.PerspectiveCamera;
import globj.camera.Screen;
import globj.core.DataType;
import globj.core.GL;
import globj.core.SceneCommand;
import globj.math.Matrix4x4f;
import globj.math.Transform;
import globj.math.UnitQuaternion;
import globj.math.Vector3f;
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



public class Tutorial14 extends SceneCommand {
	
	private VBO			vbo;
	private VBO			ibo;
	private Program		prog;
	private Transform	transform	= new Transform();
	private float		fov			= 90;
	
	public Tutorial14() {
		super(new PerspectiveCamera(0.1f, 20, 90), Screen.left);
	}
	
	@Override
	public boolean load() {
		ControlManager.select(new TutorialControlSet());
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		float f = 0.707f;
		vbo = StaticVBO.create("Test VBO", VBOTarget.ARRAY, new float[] { -f, -f, 0, -f, f, 0.5f, f, f, 0, f, -f, 0.5f });
		ibo = StaticVBO.create("Test IBO", VBOTarget.ELEMENT_ARRAY, new int[] { 0, 3, 1, 1, 3, 2, 2, 3, 0, 0, 1, 2 });
		
		try {
			Shader vert = Shaders.createShader("Vert", ShaderType.VERTEX, getClass().getResourceAsStream("shaders/tut12.vs"));
			Shader frag = Shaders.createShader("Frag", ShaderType.FRAGMENT, getClass().getResourceAsStream("shaders/tut12.fs"));
			prog = Programs.createProgram("Test", vert, frag);
			vert.destroy();
			frag.destroy();
		}
		catch (IOException ex) {
			GLDebug.logException(ex);
		}
		prog.debugQuery();
		
		transform.setPosition(0, 0, 1f);
		transform.setScale(0.33f);
		
		GL11.glClearColor(0, 0.5f, 0.5f, 0);
		return true;
	}
	
	@Override
	public void unload() {
		vbo.destroy();
		ibo.destroy();
		prog.destroy();
	}
	
	@Override
	public void draw(Matrix4x4f viewMatrix, Matrix4x4f projectionMatrix) {
		prog.bind();
		
		Programs.current().setUniform("mMatrix", transform.getModelMatrix(), false);
		Programs.current().setUniform("vMatrix", viewMatrix, false);
		Programs.current().setUniform("pMatrix", projectionMatrix, false);
		
		VAO.defaultVAO.attachBuffer(0, vbo, new VBOFormat(3, DataType.FLOAT, 0, 0));
		
		ibo.bind();
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawElements(GL11.GL_TRIANGLES, 12, GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		ibo.bindNone();
		
		prog.bindNone();
		
	}
	
	@Override
	public void input() {
		
		Transform target = TutorialControlSet.SPACE.state() ? camera.transform() : transform;
		
		target.translateBy(new Vector3f(TutorialControlSet.AD.position(), TutorialControlSet.WS.position(), 0));
		target.rotateBy(UnitQuaternion.rotation(new Vector3f(0, 0, 1), TutorialControlSet.QE.position()));
		
		if (camera instanceof PerspectiveCamera) {
			PerspectiveCamera camx = (PerspectiveCamera) camera;
			fov = Math.max(30, Math.min(120, fov + TutorialControlSet.UD.position()));
			camx.setFOV(fov);
		}
		
		if (TutorialControlSet.FULLSCR.state())
			GL.toggleFS();
		if (TutorialControlSet.ESC.state())
			GL.close();
	}
	
	public static void main(String[] args) {
		GL.setTarget(new Tutorial14());
		try {
			GL.startGL();
		}
		catch (LWJGLException e) {
			GLDebug.logException(e);
		}
	}
	
}
