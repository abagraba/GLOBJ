package lwjgl.test.ogldev;


import globj.camera.OrthographicCamera;
import globj.camera.PerspectiveCamera;
import globj.camera.Screen;
import globj.core.DataType;
import globj.core.GL;
import globj.core.SceneCommand;
import globj.core.V4f;
import globj.core.utils.Transform;
import globj.core.utils.UnitQuaternion;
import globj.core.utils.V3f;
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
import org.lwjgl.util.vector.Matrix4f;

import control.ControlManager;



public class Tutorial12 extends SceneCommand {
	
	private static float	ar			= 1.8f;
	
	private VBO				axis;
	private VBO				iaxis;
	private VBO				vbo;
	private VBO				ibo;
	private Program			prog;
	private Transform		transform	= new Transform();
	private float			fov			= 90;
	
	private boolean			toggle;
	
	
	public Tutorial12() {
		super(ortho(new Transform()), Screen.left);
	}
	
	private static PerspectiveCamera persp(Transform transform) {
		return new PerspectiveCamera(transform, new V4f(1, 1, 0, 0), 0.1f, 20, 90);
	}
	
	private static OrthographicCamera ortho(Transform transform) {
		return new OrthographicCamera(transform, new V4f(0, 1, 1, 0), ar, 1, -10, 10);
	}
	
	@Override
	public boolean load() {
		ControlManager.select(new TutorialControlSet());
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		float length = 0.1f;
		axis = StaticVBO.create("Axis", VBOTarget.ARRAY, new float[] { 0, length, length, length, 0, length, length, length, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0,
				0, 0 });
		iaxis = StaticVBO.create("Axis Indices", VBOTarget.ELEMENT_ARRAY, new int[] { 0, 5, 1, 0, 2, 4, 2, 1, 3, 1, 0, 2, 6, 0, 4, 6, 5, 0, 6, 1, 5, 6, 3, 1,
				6, 2, 3, 6, 4, 2 });
		
		float size = 0.707f;
		vbo = StaticVBO.create("Test VBO", VBOTarget.ARRAY, new float[] { -size, -size, 0, -size, size, 0.5f, size, size, 0, size, -size, 0.5f });
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
		
		GL11.glClearColor(1, 0, 1, 0);
		return true;
	}
	
	@Override
	public void unload() {
		axis.destroy();
		iaxis.destroy();
		vbo.destroy();
		ibo.destroy();
		prog.destroy();
	}
	
	@Override
	public void draw(Matrix4f viewMatrix, Matrix4f projectionMatrix) {
		prog.bind();
		
		GLDebug.write("Model");
		GLDebug.indent();
		GLDebug.write(transform.getModelMatrix());
		GLDebug.unindent();
		GLDebug.write("View");
		GLDebug.indent();
		GLDebug.write(viewMatrix);
		GLDebug.unindent();
		GLDebug.write("Projection");
		GLDebug.indent();
		GLDebug.write(projectionMatrix);
		GLDebug.unindent();
		
		Programs.current().setUniform("mMatrix", transform.getModelMatrix(), false);
		Programs.current().setUniform("vMatrix", viewMatrix, false);
		Programs.current().setUniform("pMatrix", projectionMatrix, false);
		
		VAO.defaultVAO.attachBuffer(0, vbo, new VBOFormat(3, DataType.FLOAT, 0, 0));
		
		ibo.bind();
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawElements(GL11.GL_TRIANGLES, 12, GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		ibo.bindNone();
		
		Programs.current().setUniform("mMatrix", Matrix4f.setIdentity(new Matrix4f()), false);
		
		VAO.defaultVAO.attachBuffer(0, axis, new VBOFormat(3, DataType.FLOAT, 0, 0));
		
		iaxis.bind();
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawElements(GL11.GL_TRIANGLES, 30, GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		iaxis.bindNone();
		
		prog.bindNone();
	}
	
	public static void main(String[] args) {
		GL.setTarget(new Tutorial12());
		try {
			GL.startGL();
		}
		catch (LWJGLException e) {
			GLDebug.logException(e);
		}
	}
	
	@Override
	public void input() {
		Transform target = TutorialControlSet.SPACE.state() ? camera.transform : transform;
		
		target.translateBy(new V3f(TutorialControlSet.AD.position(), TutorialControlSet.WS.position(), 0));
		target.rotateBy(UnitQuaternion.rotation(new V3f(0, 0, 1), TutorialControlSet.QE.position()));
		
		if (camera instanceof PerspectiveCamera) {
			PerspectiveCamera camx = (PerspectiveCamera) camera;
			fov = Math.max(30, Math.min(120, fov + TutorialControlSet.UD.position()));
			camx.setFOV(fov);
		}
		
		if (TutorialControlSet.LCTRL.state()) {
			toggle = !toggle;
			camera = toggle ? persp(camera.transform) : ortho(camera.transform);
		}
		
		if (TutorialControlSet.FULLSCR.state())
			GL.toggleFS();
		if (TutorialControlSet.ESC.state())
			GL.close();
	}
}
