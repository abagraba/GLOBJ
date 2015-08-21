package globj.objects.shaders;


import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL43;

import annotations.GLVersion;
import globj.core.Window;
import globj.objects.GLObject;
import globj.objects.GLObjectTracker;
import lwjgl.debug.GLDebug;



@GLVersion({ 3, 1 })
@NonNullByDefault
public class ShaderUniformBlock extends GLObject {
	
	private final GLObjectTracker<ShaderUniform>	uniforms	= new GLObjectTracker<ShaderUniform>();
	private final int								program;
	private final int								minimumSize;
	
	protected List<ShaderType> shaders = new ArrayList<ShaderType>();
	
	private ShaderUniformBlock(int program, int index) {
		super(name(program, index), index);
		this.program = program;
		if (Window.versionCheck(4, 3)) {
			minimumSize = getResource(program, GL43.GL_BUFFER_DATA_SIZE, index);
			for (ShaderType shader : ShaderType.values())
				if (getResource(program, shader.reference(), index) != 0)
					shaders.add(shader);
		}
		else {
			minimumSize = GL31.glGetActiveUniformBlocki(program, index, GL31.GL_UNIFORM_BLOCK_DATA_SIZE);
			for (ShaderType shader : ShaderType.values())
				if (GL31.glGetActiveUniformBlocki(program, id, shader.referenceLegacy()) != GL11.GL_FALSE)
					shaders.add(shader);
			GLDebug.write("Using legacy ShaderUniformBlock.");
		}
	}
	
	/**************************************************/
	
	/**
	 * @param program
	 *            the program
	 * @return the number of uniform blocks used in the program
	 */
	public static int numUniformBlocks(int program) {
		if (Window.versionCheck(4, 3))
			return GL43.glGetProgramInterfacei(program, GL43.GL_UNIFORM_BLOCK, GL43.GL_ACTIVE_RESOURCES);
		return GL20.glGetProgrami(program, GL31.GL_ACTIVE_UNIFORM_BLOCKS);
	}
	
	/**
	 * @param program
	 *            the program
	 * @return the number of uniform blocks used in the program
	 */
	public static int maxUniformBlocks(int program) {
		if (Window.versionCheck(4, 3))
			return GL43.glGetProgramInterfacei(program, GL43.GL_UNIFORM_BLOCK, GL43.GL_MAX_NUM_ACTIVE_VARIABLES);
		return GL20.glGetProgrami(program, GL31.GL_ACTIVE_UNIFORM_BLOCKS);
	}
	
	/**
	 * @param program
	 *            the program
	 * @param index
	 *            the uniform block index. On the range [0, {@link #numUniformBlocks(int)}).
	 * @return a ShaderUniformBlock object representing the uniform block
	 */
	public static ShaderUniformBlock buildUniformBlock(int program, int index) {
		return new ShaderUniformBlock(program, index);
	}
	
	public int getUniformBufferBinding() {
		if (Window.versionCheck(4, 3))
			return getResource(program, GL43.GL_BUFFER_BINDING, id);
		return GL31.glGetActiveUniformBlocki(program, id, GL31.GL_UNIFORM_BLOCK_BINDING);
	}
	
	/**************************************************/
	
	protected void addUniform(ShaderUniform uniform) {
		uniforms.add(uniform);
	}
	
	/**************************************************
	 ********************** Util **********************
	 **************************************************/
	
	private static IntBuffer getResource(int program, int[] args, int index, int results) {
		IntBuffer req = BufferUtils.createIntBuffer(args.length);
		IntBuffer res = BufferUtils.createIntBuffer(results);
		req.put(args).flip();
		GL43.glGetProgramResourceiv(program, GL43.GL_UNIFORM_BLOCK, index, req, null, res);
		return res;
	}
	
	private static int getResource(int program, int arg, int index) {
		return getResource(program, new int[] { arg }, index, 1).get();
	}
	
	private static String name(int program, int index) {
		if (Window.versionCheck(4, 3)) {
			int length = getResource(program, GL43.GL_NAME_LENGTH, index);
			return stripArray(GL43.glGetProgramResourceName(program, GL43.GL_UNIFORM_BLOCK, index, length));
		}
		int blockNameSize = GL31.glGetActiveUniformBlocki(program, index, GL31.GL_UNIFORM_BLOCK_NAME_LENGTH);
		return stripArray(GL31.glGetActiveUniformBlockName(program, index, blockNameSize));
		
	}
	
	/**************************************************
	 ********************* Getters ********************
	 **************************************************/
	
	/**
	 * @return the minimum size in bytes of this shader uniform block
	 */
	public int minimumSize() {
		return minimumSize;
	}
	
	/**
	 * @return the shader uniforms contained in this shader uniform block
	 */
	public GLObjectTracker<ShaderUniform> uniforms() {
		return uniforms;
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
		String min = minimumSize == -1 ? "" : String.format("Minimum Size: %d bytes", minimumSize);
		return String.format("Block: %-33s\t%s", name, min);
	}
	
}
