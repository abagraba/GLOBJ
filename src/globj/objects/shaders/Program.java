package globj.objects.shaders;

import globj.core.Context;
import globj.core.GL;
import globj.objects.BindTracker;
import globj.objects.BindableGLObject;

import java.nio.FloatBuffer;
import java.util.HashMap;

import lwjgl.debug.Logging;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL42;
import org.lwjgl.util.vector.Matrix4f;

public class Program extends BindableGLObject {
	
	protected static final BindTracker bind = new BindTracker();
	private final HashMap<String, Integer> uniforms = new HashMap<String, Integer>();
	
	private Shader[] shaders;
	
	private Program(String name) {
		super(name, GL20.glCreateProgram());
	}
	
	public static Program create(String name, Shader[] shaders) {
		Program prog = new Program(name);
		if (prog.id == 0) {
			Logging.globjError(Program.class, name, "Cannot create", "No ID could be allocated");
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
				Logging.globjError(Program.class, name, "Cannot create program", "Cannot find shader [" + shaders[i] + "]");
				return null;
			}
		}
		return create(name, s);
	}
	
	protected void setShaders(Shader[] shaders) {
		for (Shader shader : shaders)
			GL20.glAttachShader(id, shader.id);
		GL20.glLinkProgram(id);
	}
	
	/**************************************************/
	
	public void setUniform(String uniform, Matrix4f mat, boolean transpose) {
		int uni = uniformLocation(uniform);
		if (uni == -1) {
			Logging.globjError(this, "Could not set uniform for ", "Could not locate uniform [" + uniform + "]");
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
		Logging.setPad(32);
		Logging.writeOut(Logging.fixedString("Program:") + name);
		Logging.indent();
		
		String[] errors = getErrors();
		if (errors != null) {
			for (String error : errors)
				Logging.writeOut(error);
			Logging.unindent();
			return;
		}
		
		boolean geo = false;
		
		Logging.writeOut(Logging.fixedString("Shaders:") + GL20.glGetProgrami(id, GL20.GL_ATTACHED_SHADERS));
		Logging.indent();
		for (Shader shader : this.shaders) {
			Logging.writeOut(shader.name);
			if (shader.type == ShaderType.GEOMETRY)
				geo = true;
		}
		Logging.unindent();
		
		int geomax = 0, geoin = 0, geoout = 0;
		if (geo) {
			geomax = GL20.glGetProgrami(id, GL32.GL_GEOMETRY_VERTICES_OUT);
			geoin = GL20.glGetProgrami(id, GL32.GL_GEOMETRY_INPUT_TYPE);
			geoout = GL20.glGetProgrami(id, GL32.GL_GEOMETRY_OUTPUT_TYPE);
		}
		
		Logging.writeOut(Logging.fixedString("Attributes:")
				+ String.format("%d (%d)", GL20.glGetProgrami(id, GL20.GL_ACTIVE_ATTRIBUTES), Context.intConst(GL20.GL_MAX_VERTEX_ATTRIBS)));
		Logging.writeOut(Logging.fixedString("Uniforms:") + GL20.glGetProgrami(id, GL20.GL_ACTIVE_UNIFORMS));
		if (GL.versionCheck(4, 2))
			Logging.writeOut(Logging.fixedString("Atomic Counter Buffers:") + GL20.glGetProgrami(id, GL42.GL_ACTIVE_ATOMIC_COUNTER_BUFFERS));
		
		Logging.writeOut(Logging.fixedString("Transform Feedback Mode:") + GL20.glGetProgrami(id, GL30.GL_TRANSFORM_FEEDBACK_BUFFER_MODE));
		Logging.writeOut(Logging.fixedString("Transform Feedback Varyings:") + GL20.glGetProgrami(id, GL30.GL_TRANSFORM_FEEDBACK_VARYINGS));
		
		if (geo) {
			Logging.writeOut(Logging.fixedString("Geometry I/O:") + geoin + " -> Geometry Shader -> " + geoout);
			Logging.writeOut(Logging.fixedString("Geometry Shader Max Vertices:") + geomax);
		}
		Logging.unindent();
		Logging.unsetPad();
		GL.flushErrors();
	}
	
	@Override
	public void bind() {
		bind.update(id);
		if (bind.last() != id)
			GL20.glUseProgram(id);
	}
	
	@Override
	public void bindNone() {
		bind.update(0);
		if (bind.last() != 0)
			GL20.glUseProgram(0);
	}
	
	@Override
	protected void undobind() {
		GL20.glUseProgram(bind.revert());
	}
	
}
