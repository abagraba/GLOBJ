package globj.objects.shaders;

import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL43;

import globj.core.GL;
import globj.core.utils.LWJGLBuffers;
import globj.objects.GLObject;
import lwjgl.debug.GLDebug;

public class ShaderUniform extends GLObject {
	
	public int location;
	public int block;
	public ShaderUniformType type;
	public int array;
	
	protected ArrayList<ShaderType> shaders = new ArrayList<ShaderType>();
	
	private ShaderUniform(int program, int index, boolean legacy) {
		super(name31(program, index), index);
		this.location = GL20.glGetUniformLocation(program, name);
		this.type = ShaderUniformType.get(GL31.glGetActiveUniformsi(program, index, GL31.GL_UNIFORM_TYPE));
		this.block = GL31.glGetActiveUniformsi(program, index, GL31.GL_UNIFORM_BLOCK_INDEX);
	}
	
	private ShaderUniform(int program, int index) {
		super(name43(program, index), index);
		IntBuffer res = getResource(program, new int[] { GL43.GL_BLOCK_INDEX, GL43.GL_LOCATION, GL43.GL_ARRAY_SIZE, GL43.GL_TYPE }, index, 4);
		block = res.get();
		location = res.get();
		array = res.get();
		type = ShaderUniformType.get(res.get());
	}
	
	/**************************************************/
	
	public static ShaderUniform buildUniform(int program, int index) {
		if (GL.versionCheck(4, 3))
			return new ShaderUniform(program, index);
		else
			return new ShaderUniform(program, index, false);
	}
	
	private static IntBuffer getResource(int program, int[] args, int index, int results) {
		IntBuffer req = LWJGLBuffers.intBuffer(args);
		IntBuffer res = LWJGLBuffers.intBuffer(results);
		GL43.glGetProgramResource(program, GL43.GL_UNIFORM, index, req, null, res);
		return res;
	}
	
	private static int getResource(int program, int[] args, int index) {
		return getResource(program, args, index, 1).get();
	}
	
	/**************************************************/
	
	private static String name31(int program, int index) {
		int uniformNameSize = GL31.glGetActiveUniformsi(program, index, GL31.GL_UNIFORM_NAME_LENGTH);
		return stripArray(GL31.glGetActiveUniformName(program, index, uniformNameSize));
	}
	
	private static String name43(int program, int index) {
		int length = getResource(program, new int[] { GL43.GL_NAME_LENGTH }, index);
		return stripArray(GL43.glGetProgramResourceName(program, GL43.GL_UNIFORM, index, length));
	}
	
	private static String stripArray(String name) {
		if (name.endsWith("[0]"))
			return name.substring(0, name.length() - 3);
		return name;
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
