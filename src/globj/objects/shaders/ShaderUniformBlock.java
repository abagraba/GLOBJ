package globj.objects.shaders;


import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL43;

import globj.core.GL;
import globj.objects.GLObject;
import globj.objects.GLObjectTracker;
import lwjgl.debug.GLDebug;



@NonNullByDefault
public class ShaderUniformBlock extends GLObject {
	
	private final GLObjectTracker<ShaderUniform>	uniforms	= new GLObjectTracker<ShaderUniform>();
	private final int								blockIndex;
	private final int								minimumSize;
	
	protected List<ShaderType> shaders = new ArrayList<ShaderType>();
	
	private ShaderUniformBlock(int program, int index, boolean legacy) {
		super(name31(program, index), index);
		blockIndex = GL31.glGetUniformBlockIndex(program, name);
		for (ShaderType shader : ShaderType.values())
			if (GL31.glGetActiveUniformBlocki(program, id, shader.referenceLegacy()) != GL11.GL_FALSE)
				shaders.add(shader);
		minimumSize = -1;
		if (legacy)
			GLDebug.write("Using legacy ShaderUniformBlock.");
	}
	
	private ShaderUniformBlock(int program, int index) {
		super(name43(program, index), index);
		blockIndex = GL31.glGetUniformBlockIndex(program, name);
		for (ShaderType shader : ShaderType.values())
			if (getResource(program, shader.reference(), index) != 0)
				shaders.add(shader);
		IntBuffer res = getResource(program, new int[] { GL43.GL_BUFFER_DATA_SIZE }, index, 1);
		minimumSize = res.get();
	}
	
	/**************************************************/
	
	public static ShaderUniformBlock buildUniformBlock(int program, int index) {
		if (GL.versionCheck(4, 3))
			return new ShaderUniformBlock(program, index);
		else
			return new ShaderUniformBlock(program, index, false);
	}
	
	/**************************************************/
	
	@SuppressWarnings("all")
	protected void addUniform(ShaderUniform uniform) {
		uniforms.add(uniform);
	}
	
	/**************************************************
	 ********************** Util **********************
	 **************************************************/
	
	@SuppressWarnings("all")
	private static IntBuffer getResource(int program, int[] args, int index, int results) {
		IntBuffer req = BufferUtils.createIntBuffer(args.length);
		IntBuffer res = BufferUtils.createIntBuffer(results);
		req.put(args).flip();
		GL43.glGetProgramResource(program, GL43.GL_UNIFORM_BLOCK, index, req, null, res);
		return res;
	}
	
	private static int getResource(int program, int arg, int index) {
		return getResource(program, new int[] { arg }, index, 1).get();
	}
	
	private static String name31(int program, int index) {
		int blockNameSize = GL31.glGetActiveUniformBlocki(program, index, GL31.GL_UNIFORM_BLOCK_NAME_LENGTH);
		return stripArray(GL31.glGetActiveUniformBlockName(program, index, blockNameSize));
	}
	
	private static String name43(int program, int index) {
		int length = getResource(program, GL43.GL_NAME_LENGTH, index);
		return stripArray(GL43.glGetProgramResourceName(program, GL43.GL_UNIFORM_BLOCK, index, length));
	}
	
	/**************************************************
	 ********************* Getters ********************
	 **************************************************/
	
	/**
	 * @return the block index of this shader uniform block
	 */
	public int blockIndex() {
		return blockIndex;
	}
	
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
			return name.substring(0, name.length() - 3) + "[]";
		return name;
	}
	
	@Override
	@Nullable
	public String toString() {
		if (minimumSize != -1)
			return String.format("%-8s%24s%-16s%d bytes", "Block:", name, "Minimum Size:", minimumSize);
		return String.format("%-8s%24s", "Block:", name);
	}
	
}
