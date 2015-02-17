package globj.objects.shaders;

import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL43;

import globj.core.GL;
import globj.core.utils.LWJGLBuffers;
import globj.objects.GLObject;
import lwjgl.debug.GLDebug;

public class ShaderInput extends GLObject {
	
	public int location;
	public ShaderUniformType type;
	public int array;
	
	protected ArrayList<ShaderType> shaders = new ArrayList<ShaderType>();
	
	private ShaderInput(int program, int index, boolean legacy) {
		super(name31(program, index), index);
		this.location = GL20.glGetAttribLocation(program, name);
		this.type = ShaderUniformType.get(GL20.glGetActiveAttribType(program, index));
		this.array = GL20.glGetActiveAttribType(program, index);
	}
	
	private ShaderInput(int program, int index) {
		super(name43(program, index), index);
		IntBuffer res = getResource(program, new int[] { GL43.GL_LOCATION, GL43.GL_ARRAY_SIZE, GL43.GL_TYPE }, index, 3);
		location = res.get();
		array = res.get();
		type = ShaderUniformType.get(res.get());
	}
	
	/**************************************************/
	
	public static ShaderInput buildInput(int program, int index) {
		if (GL.versionCheck(4, 3))
			return new ShaderInput(program, index);
		else
			return new ShaderInput(program, index, false);
	}
	
	private static IntBuffer getResource(int program, int[] args, int index, int results) {
		IntBuffer req = LWJGLBuffers.intBuffer(args);
		IntBuffer res = LWJGLBuffers.intBuffer(results);
		GL43.glGetProgramResource(program, GL43.GL_PROGRAM_INPUT, index, req, null, res);
		return res;
	}
	
	private static int getResource(int program, int[] args, int index) {
		return getResource(program, args, index, 1).get();
	}
	
	/**************************************************/
	
	private static String name31(int program, int index) {
		int inputNameSize = GL20.glGetProgrami(index, GL20.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH);
		return GL20.glGetActiveAttrib(program, index, inputNameSize);
	}
	
	private static String name43(int program, int index) {
		int length = getResource(program, new int[] { GL43.GL_NAME_LENGTH }, index);
		return GL43.glGetProgramResourceName(program, GL43.GL_PROGRAM_INPUT, index, length);
	}
	
	/**************************************************/
	
	private String type() {
		if (array > 1)
			return type + " [" + array + "]";
		return type.toString();
	}
	
	/**************************************************/
	
	@Override
	public void debug() {
		GLDebug.write(this);
	}
	
	@Override
	public String toString() {
		if (location != -1)
			return GLDebug.fixedString(String.format("[%d]\t%s", location, name)) + type();
		return GLDebug.fixedString(String.format("\t%s", name)) + type();
	}
	
}
