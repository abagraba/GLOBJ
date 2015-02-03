package globj.objects.shaders;

import globj.core.Context;
import globj.core.GL;
import globj.objects.BindTracker;
import globj.objects.BindableGLObject;

import java.nio.FloatBuffer;
import java.util.HashMap;

import lwjgl.debug.GLDebug;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL42;
import org.lwjgl.util.vector.Matrix4f;

public class Program extends BindableGLObject {
	
	private final HashMap<String, Integer> uniforms = new HashMap<String, Integer>();
	
	private Shader[] shaders;
	
	private Program(String name) {
		super(name, GL20.glCreateProgram());
	}
	
	public static Program create(String name, Shader[] shaders) {
		Program prog = new Program(name);
		if (prog.id == 0) {
			GLDebug.globjError(Program.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		prog.setShaders(shaders);
		GL20.glLinkProgram(prog.id);
		GL20.glValidateProgram(prog.id);
		prog.shaders = shaders;
		return prog;
	}
	
	public static Program create(String name, String[] shaders) {
		Shader[] s = new Shader[shaders.length];
		for (int i = 0; i < shaders.length; i++) {
			s[i] = Shaders.getShader(shaders[i]);
			if (s[i] == null) {
				GLDebug.globjError(Program.class, name, "Cannot create program", "Cannot find shader [" + shaders[i] + "]");
				return null;
			}
		}
		return create(name, s);
	}
	
	private void setShaders(Shader[] shaders) {
		for (Shader shader : shaders)
			GL20.glAttachShader(id, shader.id);
		GL20.glLinkProgram(id);
	}
	
	/**************************************************/
	/********************** Bind **********************/
	/**************************************************/
	
	protected static final BindTracker bindTracker = new BindTracker();
	
	@Override
	protected BindTracker bindingTracker() {
		return bindTracker;
	}
	
	@Override
	protected void bindOP(int id) {
		GL20.glUseProgram(0);
	}
	
	@Override
	protected void destroyOP() {
		GL20.glDeleteProgram(id);
	}
	
	/**************************************************/
	public void setUniform(String uniform, Matrix4f mat, boolean transpose) {
		int uni = uniformLocation(uniform);
		if (uni == -1) {
			GLDebug.globjError(this, "Could not set uniform for ", "Could not locate uniform [" + uniform + "]");
			return;
		}
		FloatBuffer buff = BufferUtils.createFloatBuffer(16);
		mat.storeTranspose(buff);
		buff.flip();
		GL20.glUniformMatrix4(uni, transpose, buff);
	}
	
	public int uniformLocation(String uniform) {
		if (!uniforms.containsKey(uniform))
			uniforms.put(uniform, GL20.glGetUniformLocation(id, uniform));
		return uniforms.get(uniform);
	}
	
	/**************************************************/
	
	private String[] getErrors() {
		if (GL20.glGetProgrami(id, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
			int length = GL20.glGetProgrami(id, GL20.GL_INFO_LOG_LENGTH);
			return GL20.glGetProgramInfoLog(id, length).split("\n");
		}
		if (GL20.glGetProgrami(id, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
			int length = GL20.glGetProgrami(id, GL20.GL_INFO_LOG_LENGTH);
			return GL20.glGetProgramInfoLog(id, length).split("\n");
		}
		return null;
	}
	
	public void debug() {
		GL.flushErrors();
		GLDebug.setPad(32);
		GLDebug.write(GLDebug.fixedString("Program:") + name);
		GLDebug.indent();
		
		String[] errors = getErrors();
		if (errors != null) {
			for (String error : errors)
				GLDebug.write(error);
			GLDebug.unindent();
			return;
		}
		
		boolean geo = false;
		
		GLDebug.write(GLDebug.fixedString("Shaders:") + GL20.glGetProgrami(id, GL20.GL_ATTACHED_SHADERS));
		GLDebug.indent();
		for (Shader shader : this.shaders) {
			GLDebug.write(shader.name);
			if (shader.type == ShaderType.GEOMETRY)
				geo = true;
		}
		GLDebug.unindent();
		
		int geomax = 0, geoin = 0, geoout = 0;
		if (geo) {
			geomax = GL20.glGetProgrami(id, GL32.GL_GEOMETRY_VERTICES_OUT);
			geoin = GL20.glGetProgrami(id, GL32.GL_GEOMETRY_INPUT_TYPE);
			geoout = GL20.glGetProgrami(id, GL32.GL_GEOMETRY_OUTPUT_TYPE);
		}
		
		GLDebug.write(GLDebug.fixedString("Attributes:")
				+ String.format("%d (%d)", GL20.glGetProgrami(id, GL20.GL_ACTIVE_ATTRIBUTES), Context.intConst(GL20.GL_MAX_VERTEX_ATTRIBS)));
		GLDebug.write(GLDebug.fixedString("Uniforms:") + GL20.glGetProgrami(id, GL20.GL_ACTIVE_UNIFORMS));
		if (GL.versionCheck(4, 2))
			GLDebug.write(GLDebug.fixedString("Atomic Counter Buffers:") + GL20.glGetProgrami(id, GL42.GL_ACTIVE_ATOMIC_COUNTER_BUFFERS));
		
		GLDebug.write(GLDebug.fixedString("Transform Feedback Mode:") + GL20.glGetProgrami(id, GL30.GL_TRANSFORM_FEEDBACK_BUFFER_MODE));
		GLDebug.write(GLDebug.fixedString("Transform Feedback Varyings:") + GL20.glGetProgrami(id, GL30.GL_TRANSFORM_FEEDBACK_VARYINGS));
		
		if (geo) {
			GLDebug.write(GLDebug.fixedString("Geometry I/O:") + geoin + " -> Geometry Shader -> " + geoout);
			GLDebug.write(GLDebug.fixedString("Geometry Shader Max Vertices:") + geomax);
		}
		GLDebug.unindent();
		GLDebug.unsetPad();
		GL.flushErrors();
	}
	
}
