package globj.objects.shaders;


import static lwjgl.debug.GLDebug.ATTRIB;
import static lwjgl.debug.GLDebug.ATTRIB_INT;
import static lwjgl.debug.GLDebug.ATTRIB_L_STRING;
import static lwjgl.debug.GLDebug.ATTRIB_STRING;
import static lwjgl.debug.GLDebug.flushErrors;
import static lwjgl.debug.GLDebug.glObjError;
import static lwjgl.debug.GLDebug.indent;
import static lwjgl.debug.GLDebug.separator;
import static lwjgl.debug.GLDebug.unindent;
import static lwjgl.debug.GLDebug.write;
import static lwjgl.debug.GLDebug.writef;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL42;
import org.lwjgl.opengl.GL43;

import globj.core.Context;
import globj.core.GL;
import globj.math.Matrix4x4f;
import globj.objects.BindTracker;
import globj.objects.BindableGLObject;
import globj.objects.GLObjectTracker;



@NonNullByDefault
public class Program extends BindableGLObject {
	
	private GLObjectTracker<ShaderUniformBlock>	blocks		= new GLObjectTracker<ShaderUniformBlock>();
	private GLObjectTracker<ShaderUniform>		uniforms	= new GLObjectTracker<ShaderUniform>();
	private GLObjectTracker<ShaderInput>		inputs		= new GLObjectTracker<ShaderInput>();
	
	private Set<ShaderType>	types	= new TreeSet<ShaderType>();
	private List<Shader>	shaders	= new ArrayList<Shader>();
	
	@Nullable
	private String[] errors;
	
	private Program(String name) {
		super(name, GL20.glCreateProgram());
	}
	
	@Nullable
	public static Program create(String name, Shader[] shaders) {
		Program prog = new Program(name);
		if (prog.id == 0) {
			glObjError(Program.class, name, "Cannot create", "No ID could be allocated");
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
	
	@Nullable
	public static Program create(String name, String[] shaders) {
		Shader[] s = new Shader[shaders.length];
		for (int i = 0; i < shaders.length; i++) {
			s[i] = Shaders.getShader(shaders[i]);
			if (s[i] == null) {
				glObjError(Program.class, name, "Cannot create program", "Cannot find shader [" + shaders[i] + "]");
				return null;
			}
		}
		return create(name, s);
	}
	
	private void setShaders(Shader[] shaders) {
		for (Shader shader : shaders)
			GL20.glAttachShader(id, shader.id());
		GL20.glLinkProgram(id);
	}
	
	/**************************************************
	 ********************** Bind **********************
	 **************************************************/
	
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
	
	public void setUniform(String uniform, Matrix4x4f mat, boolean transpose) {
		int uni = uniformLocation(uniform);
		if (uni == -1) {
			glObjError(this, "Could not set uniform for ", "Could not locate uniform [" + uniform + "]");
			return;
		}
		GL20.glUniformMatrix4(uni, transpose, mat.toBuffer());
	}
	
	public void setUniform(String uniform, FloatBuffer mat, boolean transpose) {
		int uni = uniformLocation(uniform);
		if (uni == -1) {
			glObjError(this, "Could not set uniform for ", "Could not locate uniform [" + uniform + "]");
			return;
		}
		if (mat.remaining() < 16)
			write("Could not set Uniform to data in buffer. Buffer does not contain 16 values.");
		else
			GL20.glUniformMatrix4(uni, transpose, mat);
	}
	
	public int uniformLocation(String uniform) {
		ShaderUniform u = uniforms.get(uniform);
		if (u != null)
			return u.location();
		writef("Could not locate shader uniform %s.", uniform);
		return -1;
	}
	
	/**************************************************/
	
	private void fillAttributes() {
		int activeAttributes;
		if (GL.versionCheck(4, 3))
			activeAttributes = GL43.glGetProgramInterfacei(id, GL43.GL_PROGRAM_INPUT, GL43.GL_ACTIVE_RESOURCES);
		else
			activeAttributes = GL20.glGetProgrami(id, GL20.GL_ACTIVE_ATTRIBUTES);
		for (int i = 0; i < activeAttributes; i++)
			inputs.add(ShaderInput.buildInput(id, i));
	}
	
	private void fillUniforms() {
		int activeUniforms;
		if (GL.versionCheck(4, 3))
			activeUniforms = GL43.glGetProgramInterfacei(id, GL43.GL_UNIFORM, GL43.GL_ACTIVE_RESOURCES);
		else
			activeUniforms = GL20.glGetProgrami(id, GL20.GL_ACTIVE_UNIFORMS);
		for (int i = 0; i < activeUniforms; i++) {
			ShaderUniform uniform = ShaderUniform.buildUniform(id, i);
			this.uniforms.add(uniform);
			if (uniform.block() != -1) {
				ShaderUniformBlock block = blocks.get(uniform.block());
				if (block == null) {
					block = ShaderUniformBlock.buildUniformBlock(id, uniform.block());
					blocks.add(block);
				}
				block.uniforms().add(uniform);
			}
		}
	}
	
	/**************************************************
	 ********************** Debug *********************
	 **************************************************/
	
	@Override
	public void debug() {
//		#formatter:off
		writef(ATTRIB_STRING, "Program:", name);
		indent();
			if (debugErrors()){
				unindent();
				return;
			}
		
			debugShaders();
			write("");
			debugAttributes();
			
			if (!GL.versionCheck(4, 3))
			write("May be inaccurate due to OpenGL version below 4.3");
			
			debugUniforms();

		unindent();
//		#formatter:on
	}
	
	private void debugShaders() {
		writef(ATTRIB_INT, "%s attached shaders.", shaders.size());
		indent();
		for (Shader shader : this.shaders)
			write(shader);
		unindent();
	}
	
	private void debugAttributes() {
		writef(ATTRIB, types.contains(ShaderType.VERTEX) ? "Attributes:" : "Program Inputs:");
		indent();
		writef(ATTRIB_STRING, "Max Attributes:", Context.intConst(GL20.GL_MAX_VERTEX_ATTRIBS));
		write(inputs);
		unindent();
	}
	
	private void debugUniforms() {
		for (ShaderType type : types) {
			writef(ATTRIB, type + ":");
			indent();
			writef(ATTRIB, "Uniforms:");
			indent();
			writef(ATTRIB_INT, "Max Uniforms:", Context.intConst(type.maxUniforms()));
			for (ShaderUniform uniform : uniforms)
				if (uniform.block() == -1)
					write(uniform);
			for (ShaderUniformBlock block : blocks) {
				if (block.shaders.contains(type)) {
					write(block);
					for (ShaderUniform uniform : block.uniforms())
						write(uniform);
				}
			}
			unindent(2);
		}
	}
	
	@Override
	public void debugQuery() {
		//	#formatter:off
		flushErrors();
		
		writef(ATTRIB_STRING, "Program:", name);
		indent();
			if (debugErrors()){
				unindent();
				return;
			}
			if (GL20.glGetProgrami(id, GL20.GL_DELETE_STATUS) != GL11.GL_FALSE)
				write("FLAGGED FOR DELETION");
			debugQueryShaders();
			write("");
			debugQueryAttributes();

			
			if (!GL.versionCheck(4, 3))
			write("May be inaccurate due to OpenGL version below 4.3");
			
			debugQueryUniforms();
			
			if (GL.versionCheck(4, 2))
			writef(ATTRIB_INT, "Atomic Counter Buffers:", GL20.glGetProgrami(id, GL42.GL_ACTIVE_ATOMIC_COUNTER_BUFFERS));

			writef(ATTRIB_INT, "Transform Feedback Mode:", GL20.glGetProgrami(id, GL30.GL_TRANSFORM_FEEDBACK_BUFFER_MODE));
			writef(ATTRIB_INT, "Transform Feedback Varyings:", GL20.glGetProgrami(id, GL30.GL_TRANSFORM_FEEDBACK_VARYINGS));

			if (types.contains(ShaderType.GEOMETRY)) {
			int in = GL20.glGetProgrami(id, GL32.GL_GEOMETRY_INPUT_TYPE);
			int out =GL20.glGetProgrami(id, GL32.GL_GEOMETRY_OUTPUT_TYPE);
			writef(ATTRIB + "%d -> Geometry Shader -> %d", "Geometry I/O:", in, out);
			writef(ATTRIB_INT, "Geometry Shader Max Vertices:", GL20.glGetProgrami(id, GL32.GL_GEOMETRY_VERTICES_OUT));
			}
		unindent();
		flushErrors();
		//	#formatter:on
	}
	
	private void debugQueryShaders() {
		int shadernum = GL20.glGetProgrami(id, GL20.GL_ATTACHED_SHADERS);
		writef(ATTRIB_INT, "%s attached shaders.", shadernum);
		IntBuffer shaders = BufferUtils.createIntBuffer(shadernum);
		GL20.glGetAttachedShaders(id, null, shaders);
		indent();
		for (int i = 0; i < shadernum; i++) {
			Shader shader = Shaders.getShader(shaders.get());
			write(shader == null ? "Shader Object not found" : shader);
		}
		unindent();
	}
	
	private void debugQueryAttributes() {
		writef(ATTRIB, types.contains(ShaderType.VERTEX) ? "Attributes:" : "Program Inputs:");
		indent();
		writef(ATTRIB_STRING, "Max Attributes:", Context.intConst(GL20.GL_MAX_VERTEX_ATTRIBS));
		int maxLength = GL20.glGetProgrami(id, GL20.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH);
		int attribs = GL20.glGetProgrami(id, GL20.GL_ACTIVE_ATTRIBUTES);
		IntBuffer length = BufferUtils.createIntBuffer(1);
		IntBuffer size = BufferUtils.createIntBuffer(1);
		IntBuffer typeID = BufferUtils.createIntBuffer(1);
		ByteBuffer nameBuffer = BufferUtils.createByteBuffer(maxLength);
		for (int i = 0; i < attribs; i++) {
			GL20.glGetActiveAttrib(id, i, length, size, typeID, nameBuffer);
			ShaderUniformType type = ShaderUniformType.get(typeID.get());
			String name = new String(nameBuffer.array(), 0, length.get());
			writef(ATTRIB_STRING + " (%d bytes)", type == null ? "Unrecognized type:" : type + ":", name, size.get());
		}
		unindent();
	}
	
	private void debugQueryUniforms() {
		writef(ATTRIB, "Uniforms:");
		indent();
		for (ShaderType type : types) {
			writef(ATTRIB_L_STRING, "Max " + type + " Attributes:", Context.intConst(type.maxUniforms()));
		}
		int maxLength = GL20.glGetProgrami(id, GL20.GL_ACTIVE_UNIFORM_MAX_LENGTH);
		int unifs = GL20.glGetProgrami(id, GL20.GL_ACTIVE_UNIFORMS);
		IntBuffer length = BufferUtils.createIntBuffer(1);
		IntBuffer size = BufferUtils.createIntBuffer(1);
		IntBuffer typeID = BufferUtils.createIntBuffer(1);
		ByteBuffer nameBuffer = BufferUtils.createByteBuffer(maxLength);
		for (int i = 0; i < unifs; i++) {
			GL20.glGetActiveUniform(id, i, length, size, typeID, nameBuffer);
			ShaderUniformType type = ShaderUniformType.get(typeID.get());
			String name = new String(nameBuffer.array(), 0, length.get());
			writef(ATTRIB_STRING + " (%d bytes)", type == null ? "Unrecognized type:" : type + ":", name, size.get());
		}
		
		for (ShaderType type : types) {
			writef(ATTRIB, type + ":");
			indent();
			writef(ATTRIB, "Uniforms:");
			indent();
			writef(ATTRIB_INT, "Max Uniforms:", Context.intConst(type.maxUniforms()));
			// TODO Proper uniform queries
			unindent(2);
			separator();
		}
		unindent();
	}
	
	public static void debugContext() {
		for (ShaderType type : ShaderType.values()) {
			writef(ATTRIB, type + ":");
			indent();
			writef(ATTRIB_INT, "Max Uniforms:", Context.intConst(type.maxUniforms()));
			unindent();
		}
	}
	
	private boolean debugErrors() {
		if (errors != null) {
			for (String error : errors)
				write(error);
			return true;
		}
		return false;
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
	
	@Override
	public String toString() {
		return name;
	}
	
}
