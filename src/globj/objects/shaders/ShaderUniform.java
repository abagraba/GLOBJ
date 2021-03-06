package globj.objects.shaders;


import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL43;

import annotations.GLVersion;
import globj.core.Window;
import globj.objects.GLObject;
import lwjgl.debug.GLDebug;



@GLVersion({ 3, 1 })
@NonNullByDefault
public class ShaderUniform extends GLObject {
	
	private int					location;
	private int					block;
	private ShaderUniformType	type;
	private int					arraySize;
	
	protected List<ShaderType> shaders = new ArrayList<ShaderType>();
	
	private ShaderUniform(int program, int index) {
		super(name(program, index), index);
		if (Window.versionCheck(4, 3)) {
			IntBuffer res = getResource(program, new int[] { GL43.GL_BLOCK_INDEX, GL43.GL_LOCATION, GL43.GL_ARRAY_SIZE, GL43.GL_TYPE }, index, 4);
			block = res.get();
			location = res.get();
			arraySize = res.get();
			ShaderUniformType uniformtype = ShaderUniformType.get(res.get());
			if (uniformtype == null) {
				GLDebug.write("Invalid ShaderUniformType.");
				uniformtype = ShaderUniformType.FLOAT_VEC4;
			}
			type = uniformtype;
		}
		else {
			this.location = GL20.glGetUniformLocation(program, name);
			this.block = GL31.glGetActiveUniformsi(program, index, GL31.GL_UNIFORM_BLOCK_INDEX);
			ShaderUniformType uniformtype = ShaderUniformType.get(GL31.glGetActiveUniformsi(program, index, GL31.GL_UNIFORM_TYPE));
			if (uniformtype == null) {
				GLDebug.write("Invalid ShaderUniformType.");
				uniformtype = ShaderUniformType.FLOAT_VEC4;
			}
			type = uniformtype;
			GLDebug.write("Using legacy ShaderUniform.");
		}
	}
	
	/**************************************************/
	
	public static ShaderUniform buildUniform(int program, int index) {
		return new ShaderUniform(program, index);
	}
	
	/**************************************************
	 ********************** Util **********************
	 **************************************************/
	
	@SuppressWarnings("all")
	private static IntBuffer getResource(int program, int[] args, int index, int results) {
		IntBuffer req = BufferUtils.createIntBuffer(args.length);
		IntBuffer res = BufferUtils.createIntBuffer(results);
		req.put(args).flip();
		GL43.glGetProgramResourceiv(program, GL43.GL_UNIFORM, index, req, null, res);
		return res;
	}
	
	private static int getResource(int program, int[] args, int index) {
		return getResource(program, args, index, 1).get();
	}
	
	private static String name(int program, int index) {
		if (Window.versionCheck(4, 3)) {
			int length = getResource(program, new int[] { GL43.GL_NAME_LENGTH }, index);
			return stripArray(GL43.glGetProgramResourceName(program, GL43.GL_UNIFORM, index, length));
		}
		int uniformNameSize = GL31.glGetActiveUniformsi(program, index, GL31.GL_UNIFORM_NAME_LENGTH);
		return stripArray(GL31.glGetActiveUniformName(program, index, uniformNameSize));
	}
	
	/**************************************************
	 ********************* Getters ********************
	 **************************************************/
	
	/**
	 * @return the location of this shader uniform
	 */
	public int location() {
		return location;
	}
	
	/**
	 * @return the block of this shader uniform
	 */
	public int block() {
		return block;
	}
	
	/**
	 * @return the type of this shader uniform
	 */
	public ShaderUniformType type() {
		return type;
	}
	
	/**
	 * @return the array size of this shader uniform
	 */
	public int arraySize() {
		return arraySize;
	}
	
	/**************************************************
	 ********************** Debug *********************
	 **************************************************/
	@Override
	public void debug() {
		GLDebug.write(this);
	}
	
	@Override
	public void debugQuery() {
		GLDebug.write(this);
	}
	
	private String typeName() {
		if (arraySize > 1)
			return type + " [" + arraySize + "]";
		return type.toString();
	}
	
	@SuppressWarnings("all")
	private static String stripArray(@Nullable String name) {
		if (name == null)
			return "N/A";
		if (name.endsWith("[0]"))
			return name.substring(0, name.length() - 3);
		return name;
	}
	
	@Override
	@Nullable
	public String toString() {
		String loc = location == -1 ? "" : String.format("[%d]", location);
		return String.format("%-8s%32s\t%s", loc, name, typeName());
	}
	
}
