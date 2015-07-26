package globj.objects.shaders;

import globj.core.Context;
import globj.core.GL;
import globj.objects.BindTracker;
import globj.objects.BindableGLObject;
import globj.objects.GLObjectTracker;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import lwjgl.debug.GLDebug;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL42;
import org.lwjgl.opengl.GL43;
import org.lwjgl.util.vector.Matrix4f;

public class Program extends BindableGLObject {
	
	private GLObjectTracker<ShaderUniformBlock> blocks = new GLObjectTracker<ShaderUniformBlock>();
	private GLObjectTracker<ShaderUniform> uniforms = new GLObjectTracker<ShaderUniform>();
	private GLObjectTracker<ShaderInput> inputs = new GLObjectTracker<ShaderInput>();
	
	private Set<ShaderType> types = new TreeSet<ShaderType>();
	private ArrayList<Shader> shaders = new ArrayList<Shader>();
	
	private String[] errors = null;
	
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
		prog.getErrors();
		for (Shader shader : shaders) {
			prog.shaders.add(shader);
			prog.types.add(shader.type);
		}
		prog.bind();
		prog.fillAttributes();
		prog.fillUniforms();
		prog.undobind();
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
		GL20.glUseProgram(id);
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
		return uniforms.get(uniform).location;
	}
	
	/**************************************************/
	
	@Override
	public void debugQuery() {
		GL.flushErrors();
		GLDebug.setPad(32);
		GLDebug.write(GLDebug.fixedString("Program:") + name);
		GLDebug.indent();
		
		if (errors != null) {
			for (String error : errors)
				GLDebug.write(error);
			GLDebug.unindent();
			return;
		}
		
		GLDebug.write(GLDebug.fixedString("Shaders:") + GL20.glGetProgrami(id, GL20.GL_ATTACHED_SHADERS));
		GLDebug.indent();
		for (Shader shader : this.shaders)
			GLDebug.write(shader);
		GLDebug.unindent();
		
		GLDebug.separator();
		
		GLDebug.write((types.contains(ShaderType.VERTEX) ? "Attributes:" : "Program Inputs:") + "\tMax: " + Context.intConst(GL20.GL_MAX_VERTEX_ATTRIBS));
		GLDebug.indent();
		printAttributes();
		GLDebug.unindent();
		if (!GL.versionCheck(4, 3))
			GLDebug.write("May be inaccurate due to OpenGL version below 4.3");
		
		GLDebug.separator();
		for (ShaderType type : types) {
			GLDebug.write(type + ":");
			GLDebug.indent();
			GLDebug.write("Uniforms:\tMax: " + Context.intConst(type.maxuni));
			GLDebug.indent();
			printUniforms(type);
			GLDebug.unindent();
			GLDebug.unindent();
			GLDebug.separator();
		}
		
		if (GL.versionCheck(4, 2))
			GLDebug.write(GLDebug.fixedString("Atomic Counter Buffers:") + GL20.glGetProgrami(id, GL42.GL_ACTIVE_ATOMIC_COUNTER_BUFFERS));
		
		GLDebug.write(GLDebug.fixedString("Transform Feedback Mode:") + GL20.glGetProgrami(id, GL30.GL_TRANSFORM_FEEDBACK_BUFFER_MODE));
		GLDebug.write(GLDebug.fixedString("Transform Feedback Varyings:") + GL20.glGetProgrami(id, GL30.GL_TRANSFORM_FEEDBACK_VARYINGS));
		
		if (types.contains(ShaderType.GEOMETRY)) {
			int geomax = GL20.glGetProgrami(id, GL32.GL_GEOMETRY_VERTICES_OUT);
			GLDebug.write(GLDebug.fixedString("Geometry I/O:") + GL20.glGetProgrami(id, GL32.GL_GEOMETRY_INPUT_TYPE) + " -> Geometry Shader -> "
					+ GL20.glGetProgrami(id, GL32.GL_GEOMETRY_OUTPUT_TYPE));
			GLDebug.write(GLDebug.fixedString("Geometry Shader Max Vertices:") + geomax);
		}
		GLDebug.unindent();
		GLDebug.unsetPad();
		GL.flushErrors();
	}
	
	/**************************************************/
	
	private void getErrors() {
		if (GL20.glGetProgrami(id, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
			int length = GL20.glGetProgrami(id, GL20.GL_INFO_LOG_LENGTH);
			errors = GL20.glGetProgramInfoLog(id, length).split("\n");
		}
		if (GL20.glGetProgrami(id, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
			int length = GL20.glGetProgrami(id, GL20.GL_INFO_LOG_LENGTH);
			errors = GL20.glGetProgramInfoLog(id, length).split("\n");
		}
	}
	
	private void fillAttributes() {
		int attributes;
		if (GL.versionCheck(4, 3))
			attributes = GL43.glGetProgramInterfacei(id, GL43.GL_PROGRAM_INPUT, GL43.GL_ACTIVE_RESOURCES);
		else
			attributes = GL20.glGetProgrami(id, GL20.GL_ACTIVE_ATTRIBUTES);
		for (int i = 0; i < attributes; i++)
			inputs.add(ShaderInput.buildInput(id, i));
	}
	
	private void fillUniforms() {
		int uniforms;
		if (GL.versionCheck(4, 3))
			uniforms = GL43.glGetProgramInterfacei(id, GL43.GL_UNIFORM, GL43.GL_ACTIVE_RESOURCES);
		else
			uniforms = GL20.glGetProgrami(id, GL20.GL_ACTIVE_UNIFORMS);
		for (int i = 0; i < uniforms; i++) {
			ShaderUniform uniform = ShaderUniform.buildUniform(id, i);
			this.uniforms.add(uniform);
			if (uniform.block != -1) {
				ShaderUniformBlock block = blocks.get(uniform.block);
				if (block == null) {
					block = ShaderUniformBlock.buildUniformBlock(id, uniform.block);
					blocks.add(block);
				}
				block.uniforms.add(uniform);
			}
		}
	}
	
	private void printUniforms(ShaderType type) {
		for (ShaderUniform uniform : uniforms)
			if (uniform.block == -1)
				GLDebug.write(uniform);
		for (ShaderUniformBlock block : blocks) {
			if (block.shaders.contains(type)) {
				GLDebug.write(block);
				for (ShaderUniform uniform : block.uniforms)
					GLDebug.write(uniform);
			}
		}
	}
	
	private void printAttributes() {
		for (ShaderInput input : inputs)
			GLDebug.write(input);
	}
	
}
